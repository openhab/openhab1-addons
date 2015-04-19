package org.openhab.binding.fritzboxtr064.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

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
 *
 */
public class Tr064Comm {
	private static final Logger logger = LoggerFactory.getLogger(FritzboxTr064Binding.class);
	private final String DEFAULTUSER = "dslf-config"; //is used when no username is provided.
	
	//access details for fbox
	private String _url = null;
	private String _user = null;
	private String _pw = null;
	
	
	//all services fbox offers 
	private ArrayList<Tr064Service> _alServices = null;
	
	//mappig table for mapping item command to tr064 parameters
	private ArrayList<ItemMap> _alItemMap = null;
	
	
	//http client object used to communicate with fbox (needed for reading/writing soap requests)
	private CloseableHttpClient _httpClient = null;
	private HttpClientContext _httpClientContext = null; //for reusing auth (?)
	
	public Tr064Comm(String _url, String user, String pass) { // base URL from config
		this._url = _url;
		this._user = user;
		this._pw = pass;
		_alServices = new ArrayList<Tr064Service>();
		_alItemMap = new ArrayList<ItemMap>();
		init();
	}


	public String get_url() {
		return _url;
	}


	public void set_url(String _url) {
		this._url = _url;
	}


	public String get_user() {
		return _user;
	}


	public void set_user(String _user) {
		this._user = _user;
	}


	public String get_pw() {
		return _pw;
	}


	public void set_pw(String _pw) {
		this._pw = _pw;
	}
	
	/***
	 * makes sure all values are set properly
	 * before starting communications
	 */
	private void init(){
		if(_user == null){
			_user = DEFAULTUSER;  
		}
		if(_httpClient == null){
			_httpClient = createTr064HttpClient(_url); //create http client used for communication
		}
		if(_alServices.isEmpty()){ // no services are known yet?
			readAllServices(); //can be done w/out item mappings and w/out auth
		}
		if(_alItemMap.isEmpty()){ // no mappings present yet?
			generateItemMappings();
		}
	}
	
	
	

	/***
	 * Fetches a specific value from FritzBox
	 * 	 
	 * 
	 * @param request string from config including the command and optional parameters
	 * @return parsed value
	 */
	public String getTr064Value(String request){
		String value = null;
		
		//extract itemCommand from request
		String[] itemConfig = request.split(":");
		String itemCommand = itemConfig[0]; //command is always first
		
		//search for proper item Mapping
		ItemMap itemMap = determineItemMappingByItemCommand(itemCommand); 
		
		if(itemMap == null){
			logger.warn("No item mapping found for "+request+". function not implemented by fbox?");
			return "";
		}
		
		//determine which url etc. to connect to for accessing required value
		Tr064Service tr064service = determineServiceByItemMapping(itemMap);
		
		//construct soap Body which is added to soap msg later
		SOAPBodyElement bodyData = null; //holds data to be sent to fbox
		try {
			MessageFactory mf = MessageFactory.newInstance();
			SOAPMessage msg = mf.createMessage(); //empty message
			SOAPBody body = msg.getSOAPBody(); //std. SAOP body
			QName bodyName = new QName(tr064service.get_serviceType(), itemMap.get_readServiceCommand(), "u"); //header for body element
			bodyData = body.addBodyElement(bodyName);
			//only if input parameter is present
			if(itemConfig.length > 1){
				String dataInValue = itemConfig[1];
				QName dataNode = new QName(itemMap.get_readDataInName()); //service specific node name
				SOAPElement beDataNode = bodyData.addChildElement(dataNode);
				//if input is mac address, replace "-" with ":" as fbox wants
				if(itemMap.get_itemCommand().equals("maconline")){
					dataInValue = dataInValue.replaceAll("-", ":");
				}
				beDataNode.addTextNode(dataInValue); //add data which should be requested from fbox for this service
				//logger.debug(soapToString(msg));
			}
			
			
		} catch (Exception e) {
			logger.error("Error constructing request SOAP msg for getting parameter");
			logger.error(e.getMessage());
			logger.debug("Request was: "+request);
		}
		

		if (bodyData == null){
			logger.error("Could not determine data to be sent to fbox!");
			return null;
		}
		
		SOAPMessage smTr064Request = constructTr064Msg(bodyData); //construct entire msg with body element
		String soapActionHeader = tr064service.get_serviceType()+"#"+itemMap.get_readServiceCommand(); //needed to be sent with request (not in body -> header)
		SOAPMessage response = readSoapResponse(soapActionHeader, smTr064Request, _url+tr064service.get_controlUrl());
		//logger.debug(soapToString(smTr064Request));
		if(response == null){
			logger.error("Error retrieving SOAP response from fbox");
			return null;
		}
		
		//check if special "soap value parser" handler for extracting SOAP value is defined. If yes, use svp
		if(itemMap.get_soapValueParser() == null){ //extract dataOutName1 as default, no handler used
			NodeList nlDataOutNodes = response.getSOAPPart().getElementsByTagName(itemMap.get_readDataOutName());
			if(nlDataOutNodes != null & nlDataOutNodes.getLength() > 0){
				//extract value from soap response
				value = nlDataOutNodes.item(0).getTextContent();
			}
			else{
				logger.error("Fbox returned unexcpected response. Could not find expected datavalue "+itemMap.get_readDataOutName()+" in response "+soapToString(response));
			}

		}
		else{
			value = itemMap.get_soapValueParser().parseValueFromSoapMessage(response, itemMap); //itemMap is passed for accessing mapping in anonymous method (better way to do??)
		}
		return value;
	}
	
	/***
	 * Sets a parameter in fbox. Called from event bus
	 * @param request config string from itemconfig
	 * @param cmd command to set
	 */
	
	public void setTr064Value(String request, Command cmd){
		//search for proper item Mapping
		ItemMap itemMap = determineItemMappingByItemCommand(request); 
		
		//determine which url etc. to connect to for accessing required value
		Tr064Service tr064service = determineServiceByItemMapping(itemMap);
		//construct soap Body which is added to soap msg later
		SOAPBodyElement bodyData = null; //holds data to be sent to fbox
		try {
			MessageFactory mf = MessageFactory.newInstance();
			SOAPMessage msg = mf.createMessage(); //empty message
			SOAPBody body = msg.getSOAPBody(); //std. SAOP body
			QName bodyName = new QName(tr064service.get_serviceType(), itemMap.get_writeServiceCommand(), "u"); //header for body element
			bodyData = body.addBodyElement(bodyName);
			//convert String command into numeric
			String setDataInValue = cmd.toString().equalsIgnoreCase("on") ? "1" : "0";
			QName dataNode = new QName(itemMap.get_writeDataInName()); //service specific node name
			SOAPElement beDataNode = bodyData.addChildElement(dataNode);
			beDataNode.addTextNode(setDataInValue); //add data which should be requested from fbox for this service
			//logger.debug(soapToString(msg));
			
		} catch (Exception e) {
			logger.error("Error constructing request SOAP msg for setting parameter");
			logger.error(e.getMessage());
			logger.debug("Request was: "+request + "\ncommand was "+cmd.toString());
		}
		
		if (bodyData == null){
			logger.error("Could not determine data to be sent to fbox!");
			return;
		}
		
		SOAPMessage smTr064Request = constructTr064Msg(bodyData); //construct entire msg with body element
		String soapActionHeader = tr064service.get_serviceType()+"#"+itemMap.get_writeServiceCommand(); //needed to be sent with request (not in body -> header)
		SOAPMessage response = readSoapResponse(soapActionHeader, smTr064Request, _url+tr064service.get_controlUrl());
		//logger.debug(soapToString(smTr064Request));
		if(response == null){
			logger.error("Error retrieving SOAP response from fbox");
			return;
		}
		logger.debug(soapToString(response));
		
		//Check if error received
		try {
			if(response.getSOAPBody().getFault()  != null){
				logger.error("Error received from fbox while trying to set parameter");
				logger.debug("Soap Response was: "+soapToString(response));
			}
		} catch (SOAPException e) {
			logger.error("Could not parse soap response from fbox while setting parameter");
			logger.debug("Error was "+e.getMessage());
			logger.debug("Soap Response was: "+soapToString(response));
		}

	}
	
	
	/***
	 * Creates a apache HTTP Client object, ignoring SSL Exceptions like self signed certificates
	 * and sets Auth. Scheme to Digest Auth
	 * @param fboxUrl the URL from config file of fbox to connect to
	 * @return the ready-to-use httpclient for tr064 requests
	 */
	private CloseableHttpClient createTr064HttpClient(String fboxUrl){
		CloseableHttpClient hc = null;
		//Convert URL String from config in easy explotable URI object
		URIBuilder uriFbox = null;
		try {
			uriFbox = new URIBuilder(fboxUrl);
		} catch (URISyntaxException e) {
			logger.error("Invalid fbox URL!");
			logger.error(e.getMessage());
			return null;
		}
		//Create context of the http client
		_httpClientContext = HttpClientContext.create();
		CookieStore cookieStore = new BasicCookieStore();
		_httpClientContext.setCookieStore(cookieStore);
		
		//SETUP AUTH
		//Auth is specific for this target
		HttpHost target = new HttpHost(uriFbox.getHost(), uriFbox.getPort(), uriFbox.getScheme());
        //Add digest authentication with username/pw from global config
		CredentialsProvider credp = new BasicCredentialsProvider();
        credp.setCredentials(new AuthScope(target.getHostName(), target.getPort()), new UsernamePasswordCredentials(_user, _pw));
        // Create AuthCache instance. Manages authentication based on server response
        AuthCache authCache = new BasicAuthCache();
        // Generate DIGEST scheme object, initialize it and add it to the local auth cache. Digeste is standard for fbox auth SOAP
        DigestScheme digestAuth = new DigestScheme();
        digestAuth.overrideParamter("realm", "HTTPS Access"); //known from fbox specification
        digestAuth.overrideParamter("nonce", ""); //never known at first request
        authCache.put(target, digestAuth);
        // Add AuthCache to the execution context
        _httpClientContext.setAuthCache(authCache);
        
        //SETUP SSL TRUST
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        SSLConnectionSocketFactory sslsf = null;
        try {
			sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy()); //accept self signed certs
			sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build(), null, null, new NoopHostnameVerifier()); //dont verify hostname against cert CN 
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
        
        //BUILDER
        //setup builder with parameters defined before
        hc = HttpClientBuilder.create()
        .setSSLSocketFactory(sslsf) //set the SSL options which trust every self signed cert
        .setDefaultCredentialsProvider(credp) // set auth options using digest
		.build();

        return hc;
	}
	
	/***
	 * converts SOAP msg into string
	 * @param sm
	 * @return
	 */
	private String soapToString(SOAPMessage sm){
		String strMsg ="";
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
	private SOAPMessage readSoapResponse(String soapActionHeader, SOAPMessage request, String serviceUrl){
		SOAPMessage response = null;
		
        //Soap Body to post
        HttpPost postSoap = new HttpPost(serviceUrl); //url is service specific
        postSoap.addHeader("SOAPAction", soapActionHeader); //add the Header specific for this request
        HttpEntity entBody = null;
        HttpResponse resp = null; //stores raw response from fbox
		try {
			entBody = new StringEntity(soapToString(request), ContentType.create("text/xml", "UTF-8")); //add body
			postSoap.setEntity(entBody);
			resp = _httpClient.execute(postSoap,_httpClientContext);
			//String httpResponse = EntityUtils.toString(resp.getEntity());
			//logger.debug(httpResponse);
			
			//Fetch content data
			StatusLine slResponse = resp.getStatusLine();
			HttpEntity heResponse = resp.getEntity();
			
			//Check for (auth-) error
			if(slResponse.getStatusCode() == 401){
				logger.error("Could not read response from fbox. Unauthorized! Check User/PW in config. Create user for tr064 requests");
				return null;
			}
			
			
			//Parse response into SOAP Message
			response = MessageFactory.newInstance().createMessage(null, heResponse.getContent());
			
		} catch (UnsupportedEncodingException e) {
			logger.error("Encoding not supported: "+e.getMessage().toString());
			return null;
		} catch (ClientProtocolException e) {
			logger.error("Client Protocol not supported: "+e.getMessage().toString());
			return null;
		} catch (IOException e) {
			logger.error("Cannot send/receive: "+e.getMessage().toString());
			return null;
		} catch (UnsupportedOperationException e) {
			logger.error("Operation unsupported: "+e.getMessage().toString());
			return null;
		} catch (SOAPException e) {
			logger.error("SOAP Error: "+e.getMessage().toString());
			return null;
		}
		//logger.debug(soapToString(response));
		
        
        return response;
		
	}
	
	
	/***
	 * sets all required namespaces and prepares the SOAP message to send
	 * creates skeleton + body data
	 * @param bodyData is attached to skeleton to form entire SOAP message
	 * @return ready to send SOAP message
	 */
	private SOAPMessage constructTr064Msg(SOAPBodyElement bodyData){
		SOAPMessage soapMsg = null;
		
		try {
			MessageFactory msgFac;
			msgFac = MessageFactory.newInstance();
			soapMsg = msgFac.createMessage();
			soapMsg.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
			soapMsg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING,"UTF-8");
			SOAPPart part = soapMsg.getSOAPPart();
			
			//valid for entire SOAP msg
			String namespace = "s";

	
			//create suitable fbox envelope
			SOAPEnvelope envelope = part.getEnvelope();
			envelope.setPrefix(namespace);
			envelope.removeNamespaceDeclaration("SOAP-ENV"); //delete standard namespace which was already set
			envelope.addNamespaceDeclaration(namespace, "http://schemas.xmlsoap.org/soap/envelope/");
			Name nEncoding = envelope.createName("encodingStyle", namespace, "http://schemas.xmlsoap.org/soap/encoding/");
			envelope.addAttribute(nEncoding, "http://schemas.xmlsoap.org/soap/encoding/");
			
			//create empty header
			SOAPHeader header = envelope.getHeader();
			header.setPrefix(namespace);
			
			//create body with command based on parameter
			SOAPBody body = envelope.getBody();
			body.setPrefix(namespace);
			body.addChildElement(bodyData); //bodyData already prepared. Needs only be added
			
		} catch (Exception e) {
			logger.error("Error creating SOAP message for fbox request with data "+bodyData);
			e.printStackTrace();
		}
		
		
		return soapMsg;
		
	}
	
	
	/***
	 * looks for the proper item mapping for the item command given from item file
	 * @param itemCommand Strinf item command
	 * @return found itemMap object if found, or null
	 */
	private ItemMap determineItemMappingByItemCommand(String itemCommand){
		ItemMap foundMapping = null;
		
		//iterate over all itemMappings to find proper mapping for requested item command
		Iterator<ItemMap> itMap = _alItemMap.iterator();
		while(itMap.hasNext()){
			ItemMap currentMap = itMap.next();
			if(itemCommand.equals(currentMap.get_itemCommand())){
				foundMapping = currentMap;
				break;
			}
		}
		if(foundMapping == null){
			logger.debug("No mapping found for item command "+itemCommand);
		}
		return foundMapping;
	}
	
	
	
	/***
	 * determines Service including which URL to connect to for value request
	 * @param the itemmap for which the service is searched
	 * @return the found service or null
	 */
	private Tr064Service determineServiceByItemMapping(ItemMap mapping) {
		Tr064Service foundService = null;
		
		//search which service matches the item mapping
		Iterator<Tr064Service> it = _alServices.iterator();
		while(it.hasNext()){
			Tr064Service currentService = it.next();
			if(currentService.get_serviceId().contains(mapping.get_serviceId())){
				foundService = currentService;
				break;
			}
		}
		if(foundService == null){
			logger.debug("No tr064 service found for service id "+mapping.get_serviceId());
		}
		return foundService;
	}
	
	/***
	 * Connects to fbox service xml to get a list of all services
	 *  which are offered by TR064. Saves it into local list
	 */
	private void readAllServices(){
		
		Document xml = getFboxXmlResponse(_url+"/tr64desc.xml");
		NodeList nlServices = xml.getElementsByTagName("service"); //get all service nodes
		Node currentNode = null;
		XPath xPath = XPathFactory.newInstance().newXPath();
		for(int i=0; i < nlServices.getLength(); i++){ // iterate over all services fbox offered us 
			currentNode = nlServices.item(i);
            Tr064Service trS = new Tr064Service(); 
			try {
            	trS.set_controlUrl((String) xPath.evaluate("controlURL", currentNode, XPathConstants.STRING));
				trS.set_eventSubUrl((String) xPath.evaluate("eventSubURL", currentNode, XPathConstants.STRING));
            	trS.set_scpdurl((String) xPath.evaluate("SCPDURL", currentNode, XPathConstants.STRING));
            	trS.set_serviceId((String) xPath.evaluate("serviceId", currentNode, XPathConstants.STRING));
            	trS.set_serviceType((String) xPath.evaluate("serviceType", currentNode, XPathConstants.STRING));
			} catch (XPathExpressionException e) {
				logger.debug("Could not parse service " + currentNode.getTextContent());
				e.printStackTrace();
			}
			_alServices.add(trS);
		}
	}
	
	/***
	 * populates local static mapping table
	 * can be refactored to read from config file later?
	 * sets the praser based on the itemcommand -> soap value parser "svp" anonymous method
	 * for each mapping
	 * 
	 */
	private void generateItemMappings(){
		//services available from fbox. Needed for e.g. wifi select 5GHz/Guest Wifi
		if(_alServices.isEmpty()){ // no services are known yet?
			readAllServices();
		}
		
		//Mac Online Checker
		ItemMap imMacOnline = new ItemMap("maconline", "GetSpecificHostEntry", "LanDeviceHosts-com:serviceId:Hosts1", "NewMACAddress", "NewActive");
		imMacOnline.set_soapValueParser(new SoapValueParser() {
			
			@Override
			public String parseValueFromSoapMessage(SOAPMessage sm, ItemMap mapping) {
				logger.debug("Parsing fbox response for maconline");
				String value = "";
				//maconline: if fault is present could also indicate not a fault but MAC is not known
				try {
					SOAPBody sbResponse = sm.getSOAPBody();
					if(sbResponse.hasFault()){
						SOAPFault sf = sbResponse.getFault();
						Detail detail = sf.getDetail();
						if(detail != null){
							NodeList nlErrorCode = detail.getElementsByTagName("errorCode");
							Node nErrorCode = nlErrorCode.item(0);
							String errorCode = nErrorCode.getTextContent();
							if(errorCode.equals("714")){
								value = "MAC not known to FritzBox!";
								logger.debug(value);
							}
							else{
								logger.error("Error received from fbox: "+soapToString(sm));
								logger.error("SOAP request was:\n"+soapToString(sm));
								value = "ERROR";
							}
						}
					}
					else{
						//logger.debug(soapToString(sm));
						SOAPBody sb = sm.getSOAPBody();
						//parameter name to extract is taken from mapping
						NodeList nlActive = sb.getElementsByTagName(mapping.get_readDataOutName());
						if(nlActive.getLength() > 0){
							Node nActive = nlActive.item(0);
							value = nActive.getTextContent();
							logger.debug("parsed as "+value);
						}
					}
				} catch (SOAPException e) {
					logger.error("Error parsing SOAP response from fbox");
					e.printStackTrace();
				}
				return value;
			}
		});
		_alItemMap.add(imMacOnline);
		
		
		_alItemMap.add(new ItemMap("modelName", "GetInfo", "DeviceInfo-com:serviceId:DeviceInfo1", "", "NewModelName"));
		_alItemMap.add(new ItemMap("wanip", "GetExternalIPAddress", "urn:WANPPPConnection-com:serviceId:WANPPPConnection1", "", "NewExternalIPAddress"));
		
		//Wifi 2,4GHz
		ItemMap imWifi24Switch = new ItemMap("wifi24Switch", "GetInfo", "urn:WLANConfiguration-com:serviceId:WLANConfiguration1", "", "NewEnable");
		imWifi24Switch.set_writeServiceCommand("SetEnable");
		imWifi24Switch.set_writeDataInName("NewEnable");
		_alItemMap.add(imWifi24Switch);
		
		//wifi 5GHz
		ItemMap imWifi50Switch = new ItemMap("wifi50Switch", "GetInfo", "urn:WLANConfiguration-com:serviceId:WLANConfiguration2", "", "NewEnable");
		imWifi50Switch.set_writeServiceCommand("SetEnable");
		imWifi50Switch.set_writeDataInName("NewEnable");
				
		//guest wifi
		ItemMap imWifiGuestSwitch = new ItemMap("wifiGuestSwitch", "GetInfo", "urn:WLANConfiguration-com:serviceId:WLANConfiguration3", "", "NewEnable");
		imWifiGuestSwitch.set_writeServiceCommand("SetEnable");
		imWifiGuestSwitch.set_writeDataInName("NewEnable");
				
		//check if 5GHz wifi and/or guest wifi is available. 
		Tr064Service svc5GHzWifi = determineServiceByItemMapping(imWifi50Switch);
		Tr064Service svcGuestWifi = determineServiceByItemMapping(imWifiGuestSwitch);
		
		if(svc5GHzWifi != null && svcGuestWifi != null){ //WLANConfiguration3+2 present -> guest wifi + 5Ghz present
			//prepared properly, only needs to be added
			_alItemMap.add(imWifi50Switch);			
			_alItemMap.add(imWifiGuestSwitch);
			logger.debug("Found 2,4 Ghz, 5Ghz and Guest Wifi");
		}
		
		if(svc5GHzWifi != null && svcGuestWifi == null){ //WLANConfiguration3 not present but 2 -> no 5Ghz Wifi available but Guest Wifi
			//remap itemMap for Guest Wifi from 3 to 2
			imWifiGuestSwitch.set_serviceId("urn:WLANConfiguration-com:serviceId:WLANConfiguration2");
			_alItemMap.add(imWifiGuestSwitch);//only add guest wifi, no 5Ghz
			logger.debug("Found 2,4 Ghz and Guest Wifi");
		}
		if(svc5GHzWifi == null && svcGuestWifi == null){ //WLANConfiguration3+2 not present > no 5Ghz Wifi or Guest Wifi
			logger.debug("Found 2,4 Ghz Wifi");
		}
		
		
		
		
		
		
		
		
	}
	

	/***
	 * sets up a raw http(s) connection to Fbox and gets xml response
	 * as XML Document, ready for parsing
	 * @return
	 */
	private Document getFboxXmlResponse(String url){
		Document tr064response = null;
		HttpGet httpGet = new HttpGet(url);
		try {
			CloseableHttpResponse resp = _httpClient.execute(httpGet, _httpClientContext);
			int responseCode = resp.getStatusLine().getStatusCode();
			if(responseCode == 200){
				HttpEntity entity = resp.getEntity();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				tr064response = db.parse(entity.getContent());
				EntityUtils.consume(entity);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tr064response;
	}
}

