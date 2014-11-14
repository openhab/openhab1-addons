/*
 * Copyright (C) 2011 Teleal GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openhab.binding.sonos.internal;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.teleal.cling.model.message.StreamRequestMessage;
import org.teleal.cling.model.message.StreamResponseMessage;
import org.teleal.cling.model.message.UpnpHeaders;
import org.teleal.cling.model.message.UpnpMessage;
import org.teleal.cling.model.message.UpnpRequest;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.transport.impl.apache.HeaderUtil;
import org.teleal.cling.transport.impl.apache.StreamClientConfigurationImpl;
import org.teleal.cling.transport.spi.InitializationException;
import org.teleal.cling.transport.spi.StreamClient;
import org.teleal.common.util.Exceptions;

/**
 * Implementation based on <a href="http://hc.apache.org/">Apache HTTP Components</a>.
 * <p>
 * This implementation works on Android.
 * </p>
 *
 * @author Christian Bauer
 */
public class StreamClientImpl implements StreamClient<StreamClientConfigurationImpl> {

    final private static Logger log = Logger.getLogger(StreamClient.class.getName());

    final protected StreamClientConfigurationImpl configuration;
    final protected ThreadSafeClientConnManager clientConnectionManager;
    final protected DefaultHttpClient httpClient;
    final protected HttpParams globalParams = new BasicHttpParams();

    public StreamClientImpl(StreamClientConfigurationImpl configuration) throws InitializationException {
        this.configuration = configuration;

        ConnManagerParams.setMaxTotalConnections(globalParams, getConfiguration().getMaxTotalConnections());
        HttpConnectionParams.setConnectionTimeout(globalParams, getConfiguration().getConnectionTimeoutSeconds() * 1000);
        HttpConnectionParams.setSoTimeout(globalParams, getConfiguration().getDataReadTimeoutSeconds() * 1000);
        HttpProtocolParams.setContentCharset(globalParams, getConfiguration().getContentCharset());
        if(getConfiguration().getSocketBufferSize() != -1) {
        	
        	// Android configuration will set this to 8192 as its httpclient is based 
        	// on a random pre 4.0.1 snapshot whose BasicHttpParams do not set a default value for socket buffer size.
        	// This will also avoid OOM on the HTC Thunderbolt where default size is 2Mb (!):
        	// http://stackoverflow.com/questions/5358014/android-httpclient-oom-on-4g-lte-htc-thunderbolt
        	
        	HttpConnectionParams.setSocketBufferSize(globalParams, getConfiguration().getSocketBufferSize());
        }
        HttpConnectionParams.setStaleCheckingEnabled(globalParams, getConfiguration().getStaleCheckingEnabled());


        // This is a pretty stupid API... https://issues.apache.org/jira/browse/HTTPCLIENT-805
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory())); // The 80 here is... useless
        clientConnectionManager = new ThreadSafeClientConnManager(globalParams, registry);
        httpClient = new DefaultHttpClient(clientConnectionManager, globalParams);
        if(getConfiguration().getRequestRetryCount() != -1) {
        	httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(getConfiguration().getRequestRetryCount(), false));
        }
        
        /*
        // TODO: Ugh! And it turns out that by default it doesn't even use persistent connections properly!
        @Override
        protected ConnectionReuseStrategy createConnectionReuseStrategy() {
            return new NoConnectionReuseStrategy();
        }

        @Override
        protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
            return new ConnectionKeepAliveStrategy() {
                public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                    return 0;
                }
            };
        }
        httpClient.removeRequestInterceptorByClass(RequestConnControl.class);
        */
    }
    @Override
    public StreamClientConfigurationImpl getConfiguration() {
        return configuration;
    }

    @Override
    public StreamResponseMessage sendRequest(StreamRequestMessage requestMessage) {

        final UpnpRequest requestOperation = requestMessage.getOperation();
        log.fine("Preparing HTTP request message with method '" + requestOperation.getHttpMethodName() + "': " + requestMessage);

        try {

            // Create the right HTTP request
            HttpUriRequest httpRequest = createHttpRequest(requestMessage, requestOperation);

            // Set all the headers on the request
            httpRequest.setParams(getRequestParams(requestMessage));
            HeaderUtil.add(httpRequest, requestMessage.getHeaders());

            log.fine("Sending HTTP request: " + httpRequest.getURI());
            return httpClient.execute(httpRequest, createResponseHandler());

        } catch (MethodNotSupportedException ex) {
            log.warning("Request aborted: " + ex.toString());
            return null;
        } catch (ClientProtocolException ex) {
            log.warning("HTTP protocol exception executing request: " + requestMessage);
            log.warning("Cause: " + Exceptions.unwrap(ex));
            return null;
        } catch (IOException ex) {
            log.fine("Client connection was aborted: " + ex.getMessage()); // Don't log stacktrace
            return null;
        }
    }

    @Override
    public void stop() {
        log.fine("Shutting down HTTP client connection manager/pool");
        clientConnectionManager.shutdown();
    }

    protected HttpUriRequest createHttpRequest(UpnpMessage upnpMessage, UpnpRequest upnpRequestOperation) throws MethodNotSupportedException {

        switch (upnpRequestOperation.getMethod()) {
            case GET:
                return new HttpGet(upnpRequestOperation.getURI());
            case SUBSCRIBE:
                return new HttpGet(upnpRequestOperation.getURI()) {
                    @Override
                    public String getMethod() {
                        return UpnpRequest.Method.SUBSCRIBE.getHttpName();
                    }
                };
            case UNSUBSCRIBE:
                return new HttpGet(upnpRequestOperation.getURI()) {
                    @Override
                    public String getMethod() {
                        return UpnpRequest.Method.UNSUBSCRIBE.getHttpName();
                    }
                };
            case POST:
                HttpEntityEnclosingRequest post = new HttpPost(upnpRequestOperation.getURI());
                post.setEntity(createHttpRequestEntity(upnpMessage));
                return (HttpUriRequest) post; // Fantastic API
            case NOTIFY:
                HttpEntityEnclosingRequest notify = new HttpPost(upnpRequestOperation.getURI()) {
                    @Override
                    public String getMethod() {
                        return UpnpRequest.Method.NOTIFY.getHttpName();
                    }
                };
                notify.setEntity(createHttpRequestEntity(upnpMessage));
                return (HttpUriRequest) notify; // Fantastic API
            default:
                throw new MethodNotSupportedException(upnpRequestOperation.getHttpMethodName());
        }

    }

    protected HttpEntity createHttpRequestEntity(UpnpMessage upnpMessage) {
        if (upnpMessage.getBodyType().equals(UpnpMessage.BodyType.BYTES)) {
            log.fine("Preparing HTTP request entity as byte[]");
            return new ByteArrayEntity(upnpMessage.getBodyBytes());
        } else {
            log.fine("Preparing HTTP request entity as string");
            try {
                String charset = upnpMessage.getContentTypeCharset();
                return new StringEntity(upnpMessage.getBodyString(), charset != null ? charset : "UTF-8");
            } catch (Exception ex) {
                // WTF else am I supposed to do with this exception?
                throw new RuntimeException(ex);
            }
        }
    }

    protected ResponseHandler<StreamResponseMessage> createResponseHandler() {
        return new ResponseHandler<StreamResponseMessage>() {
            public StreamResponseMessage handleResponse(final HttpResponse httpResponse) throws IOException {

                StatusLine statusLine = httpResponse.getStatusLine();
                log.fine("Received HTTP response: " + statusLine);

                // Status
                UpnpResponse responseOperation =
                        new UpnpResponse(statusLine.getStatusCode(), statusLine.getReasonPhrase());

                // Message
                StreamResponseMessage responseMessage = new StreamResponseMessage(responseOperation);

                // Headers
                responseMessage.setHeaders(new UpnpHeaders(HeaderUtil.get(httpResponse)));

                // Body
                HttpEntity entity = httpResponse.getEntity();
                if (entity == null || entity.getContentLength() == 0) return responseMessage;

                if (responseMessage.isContentTypeMissingOrText()) {
                    log.fine("HTTP response message contains text entity");
                    responseMessage.setBody(UpnpMessage.BodyType.STRING, EntityUtils.toString(entity));
                } else {
                    log.fine("HTTP response message contains binary entity");
                    responseMessage.setBody(UpnpMessage.BodyType.BYTES, EntityUtils.toByteArray(entity));
                }

                return responseMessage;
            }
        };
    }

    protected HttpParams getRequestParams(StreamRequestMessage requestMessage) {
        HttpParams localParams = new BasicHttpParams();

        localParams.setParameter(
                CoreProtocolPNames.PROTOCOL_VERSION,
                requestMessage.getOperation().getHttpMinorVersion() == 0 ? HttpVersion.HTTP_1_0 : HttpVersion.HTTP_1_1
        );

        // DefaultHttpClient adds HOST header automatically in its default processor

        // Let's add the user-agent header on every request
        HttpProtocolParams.setUserAgent(
                localParams,
                getConfiguration().getUserAgentValue(requestMessage.getUdaMajorVersion(), requestMessage.getUdaMinorVersion())
        );

        return new DefaultedHttpParams(localParams, globalParams);
    }


}
