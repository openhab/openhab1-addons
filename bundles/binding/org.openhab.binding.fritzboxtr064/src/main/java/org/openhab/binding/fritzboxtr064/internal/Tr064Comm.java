/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import static java.util.Collections.singleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Detail;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.WordUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/***
 * Controls communication and parsing for TR064 communication with
 * FritzBox
 *
 * @author gitbock
 * @version 1.8.0
 *
 */
public class Tr064Comm {
    private static final Logger logger = LoggerFactory.getLogger(Tr064Comm.class);
    private static final String DEFAULTUSER = "dslf-config"; // is used when no username is provided.
    private static final String TR064DOWNLOADFILE = "tr64desc.xml"; // filename of all available TR064 on fbox

    // access details for fbox
    private String _url = null;
    private String _user = null;
    private String _pw = null;

    // all services fbox offers mapped by service id
    private Map<String, Tr064Service> _allServices = null;

    // mappig table for mapping item command to tr064 parameters
    private Map<String, ItemMap> _allItemMap = null;

    // http client object used to communicate with fbox (needed for reading/writing soap requests)
    private CloseableHttpClient _httpClient = null;
    private HttpClientContext _httpClientContext = null; // for reusing auth (?)

    public Tr064Comm(String _url, String user, String pass) { // base URL from config
        this._url = _url;
        this._user = user;
        this._pw = pass;
        _allServices = new HashMap<>();
        _allItemMap = new HashMap<>();
        init();
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String _url) {
        this._url = _url;
    }

    public String getUser() {
        return _user;
    }

    public void setUser(String _user) {
        this._user = _user;
    }

    public String getPw() {
        return _pw;
    }

    public void setPw(String _pw) {
        this._pw = _pw;
    }

    /***
     * makes sure all values are set properly
     * before starting communications
     */
    private void init() {
        if (_user == null) {
            _user = DEFAULTUSER;
        }
        if (_httpClient == null) {
            _httpClient = createTr064HttpClient(_url); // create http client used for communication
        }
        if (_allServices.isEmpty()) { // no services are known yet?
            readAllServices(); // can be done w/out item mappings and w/out auth
        }
        if (_allItemMap.isEmpty()) { // no mappings present yet?
            generateItemMappings();
        }
    }

    public String getTr064Value(ItemConfiguration request) {
        Map<ItemConfiguration, String> values = getTr064Values(singleton(request));
        return values.get(request);
    }

    /***
     * Fetches the values for the given item configurations from the FritzBox. Calls the FritzBox
     * SOAP services delivering the values for the item configurations. The resulting map contains
     * the values of all item configurations returned by the invoked services. This can be more
     * items than were given as parameter.
     *
     * @param request string from config including the command and optional parameters
     * @return Parsed values for all item configurations returned by the invoked services.
     */
    public Map<ItemConfiguration, String> getTr064Values(Collection<ItemConfiguration> itemConfigurations) {
        Map<ItemConfiguration, String> values = new HashMap<>();

        for (ItemConfiguration itemConfiguration : itemConfigurations) {

            String itemCommand = itemConfiguration.getItemCommand();

            if (values.containsKey(itemCommand)) {
                // item value already read by earlier MultiItemMap
                continue;
            }

            // search for proper item Mapping
            ItemMap itemMap = determineItemMappingByItemCommand(itemCommand);

            if (itemMap == null) {
                logger.warn("No item mapping found for {}. Function not implemented by your FritzBox (?)",
                        itemConfiguration);
                continue;
            }

            // determine which url etc. to connect to for accessing required value
            Tr064Service tr064service = determineServiceByItemMapping(itemMap);

            // construct soap Body which is added to soap msg later
            SOAPBodyElement bodyData = null; // holds data to be sent to fbox
            try {
                MessageFactory mf = MessageFactory.newInstance();
                SOAPMessage msg = mf.createMessage(); // empty message
                SOAPBody body = msg.getSOAPBody(); // std. SAOP body
                QName bodyName = new QName(tr064service.getServiceType(), itemMap.getReadServiceCommand(), "u"); // header
                                                                                                                 // for
                                                                                                                 // body
                                                                                                                 // element
                bodyData = body.addBodyElement(bodyName);
                // only if input parameter is present
                if (itemConfiguration.getDataInValue().isPresent()) {
                    if (itemMap instanceof ParametrizedItemMap) {
                        String dataInName = ((ParametrizedItemMap) itemMap).getReadDataInName();
                        String dataInValue = itemConfiguration.getDataInValue().get();
                        QName dataNode = new QName(dataInName); // service specific node name
                        SOAPElement beDataNode = bodyData.addChildElement(dataNode);
                        // if input is mac address, replace "-" with ":" as fbox wants
                        if (itemConfiguration.getItemCommand().equals("maconline")) {
                            dataInValue = dataInValue.replaceAll("-", ":");
                        }
                        beDataNode.addTextNode(dataInValue); // add data which should be requested from fbox for this
                                                             // service
                    } else {
                        logger.warn("item map for command {} does not support dataInValue", itemCommand);
                    }
                }
                logger.trace("Raw SOAP Request to be sent to FritzBox: {}", soapToString(msg));

            } catch (Exception e) {
                logger.warn("Error constructing request SOAP msg for getting parameter. {}", e.getMessage());
                logger.debug("Request was: {}", itemConfiguration);
            }

            if (bodyData == null) {
                logger.warn("Could not determine data to be sent to FritzBox!");
                return null;
            }

            SOAPMessage smTr064Request = constructTr064Msg(bodyData); // construct entire msg with body element
            String soapActionHeader = tr064service.getServiceType() + "#" + itemMap.getReadServiceCommand(); // needed
                                                                                                             // to be
                                                                                                             // sent
                                                                                                             // with
                                                                                                             // request
                                                                                                             // (not
                                                                                                             // in body
                                                                                                             // ->
                                                                                                             // header)
            SOAPMessage response = readSoapResponse(soapActionHeader, smTr064Request,
                    _url + tr064service.getControlUrl());
            logger.trace("Raw SOAP Response from FritzBox: {}", soapToString(response));
            if (response == null) {
                logger.warn("Error retrieving SOAP response from FritzBox");
                continue;
            }

            values.putAll(
                    itemMap.getSoapValueParser().parseValuesFromSoapMessage(response, itemMap, itemConfiguration));
        }

        return values;
    }

    /***
     * Sets a parameter in fbox. Called from event bus
     *
     * @param request config string from itemconfig
     * @param cmd command to set
     */

    public void setTr064Value(ItemConfiguration request, Command cmd) {
        String itemCommand = request.getItemCommand();

        // search for proper item Mapping
        ItemMap itemMapForCommand = determineItemMappingByItemCommand(itemCommand);

        if (!(itemMapForCommand instanceof WritableItemMap)) {
            logger.warn("Item command {} does not support setting values", itemCommand);
            return;
        }
        WritableItemMap itemMap = (WritableItemMap) itemMapForCommand;

        Tr064Service tr064service = determineServiceByItemMapping(itemMap);

        // determine which url etc. to connect to for accessing required value
        // construct soap Body which is added to soap msg later
        SOAPBodyElement bodyData = null; // holds data to be sent to fbox
        try {
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage msg = mf.createMessage(); // empty message
            SOAPBody body = msg.getSOAPBody(); // std. SAOP body
            QName bodyName = new QName(tr064service.getServiceType(), itemMap.getWriteServiceCommand(), "u"); // header
                                                                                                              // for
                                                                                                              // body
                                                                                                              // element
            bodyData = body.addBodyElement(bodyName);
            // only if input parameter is present
            if (request.getDataInValue().isPresent()) {
                String dataInValueAdd = request.getDataInValue().get(); // additional parameter to set e.g. id of TAM to
                                                                        // set
                QName dataNode = new QName(itemMap.getWriteDataInNameAdditional()); // name of additional para to set
                SOAPElement beDataNode = bodyData.addChildElement(dataNode);
                beDataNode.addTextNode(dataInValueAdd); // add value which should be set
            }

            // convert String command into numeric
            String setDataInValue = cmd.toString().equalsIgnoreCase("on") ? "1" : "0";
            QName dataNode = new QName(itemMap.getWriteDataInName()); // service specific node name
            SOAPElement beDataNode = bodyData.addChildElement(dataNode);
            beDataNode.addTextNode(setDataInValue); // add data which should be requested from fbox for this service
            logger.debug("SOAP Msg to send to FritzBox for setting data: {}", soapToString(msg));

        } catch (Exception e) {
            logger.error("Error constructing request SOAP msg for setting parameter. {}", e.getMessage());
            logger.debug("Request was: {}. Command was: {}.", request, cmd.toString());
        }

        if (bodyData == null) {
            logger.error("Could not determine data to be sent to FritzBox!");
            return;
        }

        SOAPMessage smTr064Request = constructTr064Msg(bodyData); // construct entire msg with body element
        String soapActionHeader = tr064service.getServiceType() + "#" + itemMap.getWriteServiceCommand(); // needed to
                                                                                                          // be sent
                                                                                                          // with
                                                                                                          // request
                                                                                                          // (not in
                                                                                                          // body ->
                                                                                                          // header)
        SOAPMessage response = readSoapResponse(soapActionHeader, smTr064Request, _url + tr064service.getControlUrl());
        if (response == null) {
            logger.error("Error retrieving SOAP response from FritzBox");
            return;
        }
        logger.debug("SOAP response from FritzBox: {}", soapToString(response));

        // Check if error received
        try {
            if (response.getSOAPBody().getFault() != null) {
                logger.error("Error received from FritzBox while trying to set parameter");
                logger.debug("Soap Response was: {}", soapToString(response));
            }
        } catch (SOAPException e) {
            logger.error("Could not parse soap response from FritzBox while setting parameter. {}", e.getMessage());
            logger.debug("Soap Response was: {}", soapToString(response));
        }

    }

    /***
     * Creates a apache HTTP Client object, ignoring SSL Exceptions like self signed certificates
     * and sets Auth. Scheme to Digest Auth
     *
     * @param fboxUrl the URL from config file of fbox to connect to
     * @return the ready-to-use httpclient for tr064 requests
     */
    private CloseableHttpClient createTr064HttpClient(String fboxUrl) {
        CloseableHttpClient hc = null;
        // Convert URL String from config in easy explotable URI object
        URIBuilder uriFbox = null;
        try {
            uriFbox = new URIBuilder(fboxUrl);
        } catch (URISyntaxException e) {
            logger.error("Invalid FritzBox URL! {}", e.getMessage());
            return null;
        }
        // Create context of the http client
        _httpClientContext = HttpClientContext.create();
        CookieStore cookieStore = new BasicCookieStore();
        _httpClientContext.setCookieStore(cookieStore);

        // SETUP AUTH
        // Auth is specific for this target
        HttpHost target = new HttpHost(uriFbox.getHost(), uriFbox.getPort(), uriFbox.getScheme());
        // Add digest authentication with username/pw from global config
        CredentialsProvider credp = new BasicCredentialsProvider();
        credp.setCredentials(new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials(_user, _pw));
        // Create AuthCache instance. Manages authentication based on server response
        AuthCache authCache = new BasicAuthCache();
        // Generate DIGEST scheme object, initialize it and add it to the local auth cache. Digeste is standard for fbox
        // auth SOAP
        DigestScheme digestAuth = new DigestScheme();
        digestAuth.overrideParamter("realm", "HTTPS Access"); // known from fbox specification
        digestAuth.overrideParamter("nonce", ""); // never known at first request
        authCache.put(target, digestAuth);
        // Add AuthCache to the execution context
        _httpClientContext.setAuthCache(authCache);

        // SETUP SSL TRUST
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        SSLConnectionSocketFactory sslsf = null;
        try {
            sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy()); // accept self signed certs
            sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build(), null, null, new NoopHostnameVerifier()); // dont
                                                                                                                       // verify
                                                                                                                       // hostname
                                                                                                                       // against
                                                                                                                       // cert
                                                                                                                       // CN
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        // Set timeout values
        RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setSocketTimeout(4000).setConnectTimeout(4000)
                .setConnectionRequestTimeout(4000).build();

        // BUILDER
        // setup builder with parameters defined before
        hc = HttpClientBuilder.create().setSSLSocketFactory(sslsf) // set the SSL options which trust every self signed
                                                                   // cert
                .setDefaultCredentialsProvider(credp) // set auth options using digest
                .setDefaultRequestConfig(rc) // set the request config specifying timeout
                .build();

        return hc;
    }

    /***
     * converts SOAP msg into string
     *
     * @param sm
     * @return
     */
    public static String soapToString(SOAPMessage sm) {
        String strMsg = "";
        try {
            ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
            sm.writeTo(xmlStream);
            strMsg = new String(xmlStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strMsg;
    }

    /***
     *
     * @param soapActionHeader String in HTTP Header. specific for each TR064 service
     * @param request the SOAPMEssage Object to send to fbox as request
     * @param serviceUrl URL to sent the SOAP Message to (service specific)
     * @return
     */
    private SOAPMessage readSoapResponse(String soapActionHeader, SOAPMessage request, String serviceUrl) {
        SOAPMessage response = null;

        // Soap Body to post
        HttpPost postSoap = new HttpPost(serviceUrl); // url is service specific
        postSoap.addHeader("SOAPAction", soapActionHeader); // add the Header specific for this request
        HttpEntity entBody = null;
        HttpResponse resp = null; // stores raw response from fbox
        boolean exceptionOccurred = false;
        try {
            entBody = new StringEntity(soapToString(request), ContentType.create("text/xml", "UTF-8")); // add body
            postSoap.setEntity(entBody);
            resp = _httpClient.execute(postSoap, _httpClientContext);

            // Fetch content data
            StatusLine slResponse = resp.getStatusLine();
            HttpEntity heResponse = resp.getEntity();

            // Check for (auth-) error
            if (slResponse.getStatusCode() == 401) {
                logger.error(
                        "Could not read response from FritzBox. Unauthorized! Check User/PW in config. Create user for tr064 requests");
                _httpClientContext.getTargetAuthState().reset();
                return null;
            }

            // Parse response into SOAP Message
            response = MessageFactory.newInstance().createMessage(null, heResponse.getContent());

        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding not supported: {}", e.getMessage().toString());
            response = null;
            exceptionOccurred = true;
        } catch (ClientProtocolException e) {
            logger.error("Client Protocol not supported: {}", e.getMessage().toString());
            response = null;
            exceptionOccurred = true;
        } catch (IOException e) {
            logger.error("Cannot send/receive: {}", e.getMessage().toString());
            response = null;
            exceptionOccurred = true;
        } catch (UnsupportedOperationException e) {
            logger.error("Operation unsupported: {}", e.getMessage().toString());
            response = null;
            exceptionOccurred = true;
        } catch (SOAPException e) {
            logger.error("SOAP Error: {}", e.getMessage().toString());
            response = null;
            exceptionOccurred = true;
        } finally {
            // Make sure connection is released. If error occurred make sure to print in log
            if (exceptionOccurred) {
                logger.error("Releasing connection to FritzBox because of error!");
                _httpClientContext.getTargetAuthState().reset();
            } else {
                logger.debug("Releasing connection");
            }
            postSoap.releaseConnection();
        }

        return response;

    }

    /***
     * sets all required namespaces and prepares the SOAP message to send
     * creates skeleton + body data
     *
     * @param bodyData is attached to skeleton to form entire SOAP message
     * @return ready to send SOAP message
     */
    private SOAPMessage constructTr064Msg(SOAPBodyElement bodyData) {
        SOAPMessage soapMsg = null;

        try {
            MessageFactory msgFac;
            msgFac = MessageFactory.newInstance();
            soapMsg = msgFac.createMessage();
            soapMsg.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
            soapMsg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            SOAPPart part = soapMsg.getSOAPPart();

            // valid for entire SOAP msg
            String namespace = "s";

            // create suitable fbox envelope
            SOAPEnvelope envelope = part.getEnvelope();
            envelope.setPrefix(namespace);
            envelope.removeNamespaceDeclaration("SOAP-ENV"); // delete standard namespace which was already set
            envelope.addNamespaceDeclaration(namespace, "http://schemas.xmlsoap.org/soap/envelope/");
            Name nEncoding = envelope.createName("encodingStyle", namespace,
                    "http://schemas.xmlsoap.org/soap/encoding/");
            envelope.addAttribute(nEncoding, "http://schemas.xmlsoap.org/soap/encoding/");

            // create empty header
            SOAPHeader header = envelope.getHeader();
            header.setPrefix(namespace);

            // create body with command based on parameter
            SOAPBody body = envelope.getBody();
            body.setPrefix(namespace);
            body.addChildElement(bodyData); // bodyData already prepared. Needs only be added

        } catch (Exception e) {
            logger.error("Error creating SOAP message for fbox request with data {}", bodyData);
            e.printStackTrace();
        }

        return soapMsg;

    }

    /***
     * looks for the proper item mapping for the item command given from item file
     *
     * @param itemCommand String item command
     * @return found itemMap object if found, or null
     */
    private ItemMap determineItemMappingByItemCommand(String itemCommand) {
        ItemMap foundMapping = _allItemMap.get(itemCommand);

        if (foundMapping == null) {
            logger.error("No mapping found for item command {}", itemCommand);
        }
        return foundMapping;
    }

    /***
     * determines Service including which URL to connect to for value request
     *
     * @param the itemmap for which the service is searched
     * @return the found service or null
     */
    private Tr064Service determineServiceByItemMapping(ItemMap mapping) {
        Tr064Service foundService = _allServices.get(mapping.getServiceId());

        if (foundService == null) {
            logger.warn("No tr064 service found for service id {}", mapping.getServiceId());
        }
        return foundService;
    }

    /***
     * Connects to fbox service xml to get a list of all services
     * which are offered by TR064. Saves it into local list
     */
    private void readAllServices() {
        Document xml = getFboxXmlResponse(_url + "/" + TR064DOWNLOADFILE);
        if (xml == null) {
            logger.error("Could not read xml response services");
            return;
        }
        NodeList nlServices = xml.getElementsByTagName("service"); // get all service nodes
        Node currentNode = null;
        XPath xPath = XPathFactory.newInstance().newXPath();
        for (int i = 0; i < nlServices.getLength(); i++) { // iterate over all services fbox offered us
            currentNode = nlServices.item(i);
            Tr064Service trS = new Tr064Service();
            try {
                trS.setControlUrl((String) xPath.evaluate("controlURL", currentNode, XPathConstants.STRING));
                trS.setEventSubUrl((String) xPath.evaluate("eventSubURL", currentNode, XPathConstants.STRING));
                trS.setScpdurl((String) xPath.evaluate("SCPDURL", currentNode, XPathConstants.STRING));
                trS.setServiceId((String) xPath.evaluate("serviceId", currentNode, XPathConstants.STRING));
                trS.setServiceType((String) xPath.evaluate("serviceType", currentNode, XPathConstants.STRING));
            } catch (XPathExpressionException e) {
                logger.debug("Could not parse service {}", currentNode.getTextContent());
                e.printStackTrace();
            }
            _allServices.put(trS.getServiceId(), trS);
        }
    }

    /**
     * populates local static mapping table
     * todo: refactore to read from config file later?
     * sets the parser based on the itemcommand -> soap value parser "svp" anonymous method
     * for each mapping
     *
     */
    private void generateItemMappings() {
        // services available from fbox. Needed for e.g. wifi select 5GHz/Guest Wifi
        if (_allServices.isEmpty()) { // no services are known yet?
            readAllServices();
        }

        // Mac Online Checker
        SingleItemMap imMacOnline = new SingleItemMap("maconline", "GetSpecificHostEntry",
                "urn:LanDeviceHosts-com:serviceId:Hosts1", "NewMACAddress", "NewActive", new SoapValueParser() {

                    @Override
                    protected String parseValueFromSoapFault(ItemConfiguration itemConfiguration, SOAPFault soapFault,
                            ItemMap mapping) {
                        String value = null;
                        Detail detail = soapFault.getDetail();
                        if (detail != null) {
                            NodeList nlErrorCode = detail.getElementsByTagName("errorCode");
                            Node nErrorCode = nlErrorCode.item(0);
                            String errorCode = nErrorCode.getTextContent();
                            if (errorCode.equals("714")) {
                                value = "MAC not known to FritzBox!";
                                logger.debug(value);
                            }
                        }

                        if (value == null) {
                            value = super.parseValueFromSoapFault(itemConfiguration, soapFault, mapping);
                        }

                        return value;
                    }
                });
        addItemMap(imMacOnline);

        addItemMap(new MultiItemMap(Arrays.asList("modelName", "manufacturerName", "softwareVersion", "serialNumber"),
                "GetInfo", "urn:DeviceInfo-com:serviceId:DeviceInfo1", name -> "New" + WordUtils.capitalize(name)));
        addItemMap(new SingleItemMap("wanip", "GetExternalIPAddress",
                "urn:WANPPPConnection-com:serviceId:WANPPPConnection1", "", "NewExternalIPAddress"));
        addItemMap(new SingleItemMap("externalWanip", "GetExternalIPAddress",
                "urn:WANIPConnection-com:serviceId:WANIPConnection1", "", "NewExternalIPAddress"));

        // WAN Status
        addItemMap(new MultiItemMap(
                Arrays.asList("wanWANAccessType", "wanLayer1UpstreamMaxBitRate", "wanLayer1DownstreamMaxBitRate",
                        "wanPhysicalLinkStatus"),
                "GetCommonLinkProperties", "urn:WANCIfConfig-com:serviceId:WANCommonInterfaceConfig1",
                name -> name.replace("wan", "New")));
        addItemMap(new SingleItemMap("wanTotalBytesSent", "GetTotalBytesSent",
                "urn:WANCIfConfig-com:serviceId:WANCommonInterfaceConfig1", "", "NewTotalBytesSent"));
        addItemMap(new SingleItemMap("wanTotalBytesReceived", "GetTotalBytesReceived",
                "urn:WANCIfConfig-com:serviceId:WANCommonInterfaceConfig1", "", "NewTotalBytesReceived"));

        // DSL Status
        addItemMap(new MultiItemMap(
                Arrays.asList("dslEnable", "dslStatus", "dslUpstreamCurrRate", "dslDownstreamCurrRate",
                        "dslUpstreamMaxRate", "dslDownstreamMaxRate", "dslUpstreamNoiseMargin",
                        "dslDownstreamNoiseMargin", "dslUpstreamAttenuation", "dslDownstreamAttenuation"),
                "GetInfo", "urn:WANDSLIfConfig-com:serviceId:WANDSLInterfaceConfig1",
                name -> name.replace("dsl", "New")));
        addItemMap(new MultiItemMap(Arrays.asList("dslFECErrors", "dslHECErrors", "dslCRCErrors"), "GetStatisticsTotal",
                "urn:WANDSLIfConfig-com:serviceId:WANDSLInterfaceConfig1", name -> name.replace("dsl", "New")));

        // Wifi 2,4GHz
        SingleItemMap imWifi24Switch = new SingleItemMap("wifi24Switch", "GetInfo",
                "urn:WLANConfiguration-com:serviceId:WLANConfiguration1", "", "NewEnable");
        imWifi24Switch.setWriteServiceCommand("SetEnable");
        imWifi24Switch.setWriteDataInName("NewEnable");
        addItemMap(imWifi24Switch);

        // wifi 5GHz
        SingleItemMap imWifi50Switch = new SingleItemMap("wifi50Switch", "GetInfo",
                "urn:WLANConfiguration-com:serviceId:WLANConfiguration2", "", "NewEnable");
        imWifi50Switch.setWriteServiceCommand("SetEnable");
        imWifi50Switch.setWriteDataInName("NewEnable");

        // guest wifi
        SingleItemMap imWifiGuestSwitch = new SingleItemMap("wifiGuestSwitch", "GetInfo",
                "urn:WLANConfiguration-com:serviceId:WLANConfiguration3", "", "NewEnable");
        imWifiGuestSwitch.setWriteServiceCommand("SetEnable");
        imWifiGuestSwitch.setWriteDataInName("NewEnable");

        // check if 5GHz wifi and/or guest wifi is available.
        Tr064Service svc5GHzWifi = determineServiceByItemMapping(imWifi50Switch);
        Tr064Service svcGuestWifi = determineServiceByItemMapping(imWifiGuestSwitch);

        if (svc5GHzWifi != null && svcGuestWifi != null) { // WLANConfiguration3+2 present -> guest wifi + 5Ghz present
            // prepared properly, only needs to be added
            addItemMap(imWifi50Switch);
            addItemMap(imWifiGuestSwitch);
            logger.debug("Found 2,4 Ghz, 5Ghz and Guest Wifi");
        }

        if (svc5GHzWifi != null && svcGuestWifi == null) { // WLANConfiguration3 not present but 2 -> no 5Ghz Wifi
                                                           // available but Guest Wifi
            // remap itemMap for Guest Wifi from 3 to 2
            imWifiGuestSwitch.setServiceId("urn:WLANConfiguration-com:serviceId:WLANConfiguration2");
            addItemMap(imWifiGuestSwitch);// only add guest wifi, no 5Ghz
            logger.debug("Found 2,4 Ghz and Guest Wifi");
        }
        if (svc5GHzWifi == null && svcGuestWifi == null) { // WLANConfiguration3+2 not present > no 5Ghz Wifi or Guest
                                                           // Wifi
            logger.debug("Found 2,4 Ghz Wifi");
        }

        // Phonebook Download
        // itemcommand is dummy: not a real item
        ItemMap imPhonebook = new SingleItemMap("phonebook", "GetPhonebook",
                "urn:X_AVM-DE_OnTel-com:serviceId:X_AVM-DE_OnTel1", "NewPhonebookID", "NewPhonebookURL");
        addItemMap(imPhonebook);

        // TAM (telephone answering machine) Switch
        SingleItemMap imTamSwitch = new SingleItemMap("tamSwitch", "GetInfo",
                "urn:X_AVM-DE_TAM-com:serviceId:X_AVM-DE_TAM1", "NewIndex", "NewEnable");
        imTamSwitch.setWriteServiceCommand("SetEnable");
        imTamSwitch.setWriteDataInName("NewEnable");
        imTamSwitch.setWriteDataInNameAdditional("NewIndex"); // additional Parameter to set
        addItemMap(imTamSwitch);

        // New Messages per TAM ID
        // two requests needed: First gets URL to download tam info from, 2nd contains info of messages
        SingleItemMap imTamNewMessages = new SingleItemMap("tamNewMessages", "GetMessageList",
                "urn:X_AVM-DE_TAM-com:serviceId:X_AVM-DE_TAM1", "NewIndex", "NewURL", new SoapValueParser() {

                    @Override
                    protected String parseValueFromSoapBody(ItemConfiguration itemConfiguration, SOAPBody soapBody,
                            ItemMap mapping) {
                        String value = null;

                        // extract URL from soap response
                        String url = super.parseValueFromSoapBody(itemConfiguration, soapBody, mapping);

                        if (url != null) {
                            Document xmlTamInfo = getFboxXmlResponse(url);
                            if (xmlTamInfo != null) {
                                logger.debug("Parsing xml message TAM info {}", Helper.documentToString(xmlTamInfo));
                                NodeList nlNews = xmlTamInfo.getElementsByTagName("New"); // get all Nodes containing
                                                                                          // "new", indicating message
                                                                                          // was not listened to

                                // When <new> contains 1 -> message is new, when 0, message not new -> Counting 1s
                                int newMessages = 0;
                                for (int i = 0; i < nlNews.getLength(); i++) {
                                    if (nlNews.item(i).getTextContent().equals("1")) {
                                        newMessages++;
                                    }
                                }
                                value = Integer.toString(newMessages);
                                logger.debug("Parsed new messages as: {}", value);
                            } else {
                                logger.warn("Failed to read TAM info from URL {}", url);
                                // cause was already logged earlier
                            }
                        }

                        return value;
                    }
                });
        addItemMap(imTamNewMessages);

        // Missed calls
        // two requests: 1st fetches URL to download call list, 2nd fetches xml call list
        SingleItemMap imMissedCalls = new SingleItemMap("missedCallsInDays", "GetCallList",
                "urn:X_AVM-DE_OnTel-com:serviceId:X_AVM-DE_OnTel1", "NewDays", "NewCallListURL", new SoapValueParser() {

                    @Override
                    protected String parseValueFromSoapBody(ItemConfiguration itemConfiguration, SOAPBody soapBody,
                            ItemMap mapping) {
                        String value = null;

                        // extract URL from soap response
                        String url = super.parseValueFromSoapBody(itemConfiguration, soapBody, mapping);

                        // extract how many days of call list should be examined for missed calls
                        String days = "3"; // default
                        if (!itemConfiguration.getAdditionalParameters().isEmpty()) {
                            days = itemConfiguration.getAdditionalParameters().get(0); // set the days as defined in
                                                                                       // item config.
                            // Otherwise default value of 3 is used
                        }

                        if (url != null) {
                            // only get missed calls of the last x days
                            url = url + "&days=" + days;
                            logger.debug("Downloading call list using url {}", url);
                            Document callListInfo = getFboxXmlResponse(url); // download call list
                            if (callListInfo != null) {
                                logger.debug("Parsing xml message call list info {}",
                                        Helper.documentToString(callListInfo));
                                NodeList nlTypes = callListInfo.getElementsByTagName("Type"); // get all Nodes
                                                                                              // containing "Type". Type
                                                                                              // 2 => missed

                                // When <type> contains 2 -> call was missed -> Counting only 2 entries
                                int missedCalls = 0;
                                for (int i = 0; i < nlTypes.getLength(); i++) {
                                    if (nlTypes.item(i).getTextContent().equals("2")) {
                                        missedCalls++;
                                    }
                                }
                                value = Integer.toString(missedCalls);
                                logger.debug("Parsed new messages as: {}", value);
                            } else {
                                logger.warn("Failed to read call list info from URL {}", url);
                                // cause was already logged earlier
                            }
                        }

                        return value;
                    }
                });
        addItemMap(imMissedCalls);

    }

    private void addItemMap(ItemMap itemMap) {
        for (String itemCommand : itemMap.getItemCommands()) {
            if (_allItemMap.containsKey(itemCommand)) {
                throw new IllegalStateException("ItemMap for itemCommand " + itemCommand + " already defined");
            }
            _allItemMap.put(itemCommand, itemMap);
        }
    }

    /***
     * sets up a raw http(s) connection to Fbox and gets xml response
     * as XML Document, ready for parsing
     *
     * @return
     */
    public Document getFboxXmlResponse(String url) {
        Document tr064response = null;
        HttpGet httpGet = new HttpGet(url);
        boolean exceptionOccurred = false;
        try {
            CloseableHttpResponse resp = _httpClient.execute(httpGet, _httpClientContext);
            int responseCode = resp.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                HttpEntity entity = resp.getEntity();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                tr064response = db.parse(entity.getContent());
                EntityUtils.consume(entity);
            } else {
                logger.error("Failed to receive valid response from httpGet");
            }

        } catch (Exception e) {
            exceptionOccurred = true;
            logger.error("Failed to receive valid response from httpGet: {}", e.getMessage());
        } finally {
            // Make sure connection is released. If error occurred make sure to print in log
            if (exceptionOccurred) {
                logger.error("Releasing connection to FritzBox because of error!");
            } else {
                logger.debug("Releasing connection");
            }
            httpGet.releaseConnection();
        }
        return tr064response;
    }
}
