/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.sonos.internal;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
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
import org.teleal.cling.model.message.header.UpnpHeader;
import org.teleal.cling.transport.impl.apache.HeaderUtil;
import org.teleal.cling.transport.impl.apache.StreamClientConfigurationImpl;
import org.teleal.cling.transport.spi.StreamClient;

/**
 * This code is copied from the {@link StreamClientImpl} of the cling 2.X branch and modified.
 * 
 * @author Martin Ehmke
 */
public class StreamClientImpl implements StreamClient<StreamClientConfigurationImpl>
{
	final private static Logger log = Logger.getLogger(StreamClient.class.getName());

	final protected HttpParams globalParams = new BasicHttpParams();

	final protected StreamClientConfigurationImpl configuration;

	final protected ThreadSafeClientConnManager clientConnectionManager;

	final protected DefaultHttpClient httpClient;

	/**
	 * Constructs a new {@link StreamClientImpl}.
	 * 
	 * @param configuration
	 *            to use for client configuration.
	 */
	public StreamClientImpl(StreamClientConfigurationImpl configuration)
	{
		this.configuration = configuration;
		HttpProtocolParams.setContentCharset(globalParams, getConfiguration().getContentCharset());
		HttpProtocolParams.setUseExpectContinue(globalParams, false);

		// These are some safety settings, we should never run into these timeouts as we
		// do our own expiration checking

		if (getConfiguration().getSocketBufferSize() != -1)
			HttpConnectionParams.setSocketBufferSize(globalParams, getConfiguration().getSocketBufferSize());

		HttpConnectionParams.setStaleCheckingEnabled(globalParams, getConfiguration().getStaleCheckingEnabled());

		// Only register 80, not 443 and SSL
		SchemeRegistry registry = new SchemeRegistry();
		// This is the key difference to the cling 1.X version. Constructor call of Scheme
		// is in a different order.
		registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

		clientConnectionManager = new ThreadSafeClientConnManager(registry);
		clientConnectionManager.setMaxTotal(getConfiguration().getMaxTotalConnections());

		httpClient = new DefaultHttpClient(clientConnectionManager, globalParams);
		if (getConfiguration().getRequestRetryCount() != -1)
		{
			httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(getConfiguration()
					.getRequestRetryCount(), false));
		}
	}

	@Override
	public StreamResponseMessage sendRequest(StreamRequestMessage message)
	{
		if (log.isLoggable(Level.FINE))
			log.fine("Sending HTTP request: " + message);

		try
		{
			return httpClient.execute(createRequest(message), createResponseHandler());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected ResponseHandler<StreamResponseMessage> createResponseHandler()
	{
		return new ResponseHandler<StreamResponseMessage>()
		{
			public StreamResponseMessage handleResponse(final HttpResponse httpResponse) throws IOException
			{

				StatusLine statusLine = httpResponse.getStatusLine();
				if (log.isLoggable(Level.FINE))
					log.fine("Received HTTP response: " + statusLine);

				// Status
				UpnpResponse responseOperation = new UpnpResponse(statusLine.getStatusCode(),
						statusLine.getReasonPhrase());

				// Message
				StreamResponseMessage responseMessage = new StreamResponseMessage(responseOperation);

				// Headers
				responseMessage.setHeaders(new UpnpHeaders(HeaderUtil.get(httpResponse)));

				// Body
				HttpEntity entity = httpResponse.getEntity();
				if (entity == null || entity.getContentLength() == 0)
				{
					log.fine("HTTP response message has no entity");
					return responseMessage;
				}

				byte data[] = EntityUtils.toByteArray(entity);
				if (data != null)
				{
					if (responseMessage.isContentTypeMissingOrText())
					{
						log.fine("HTTP response message contains text entity");
						responseMessage.setBodyCharacters(data);
					}
					else
					{
						log.fine("HTTP response message contains binary entity");
						responseMessage.setBody(UpnpMessage.BodyType.BYTES, data);
					}
				}
				else
				{
					log.fine("HTTP response message has no entity");
				}

				return responseMessage;
			}
		};
	}

	protected HttpUriRequest createRequest(StreamRequestMessage requestMessage)
	{
		UpnpRequest requestOperation = requestMessage.getOperation();
		HttpUriRequest request;
		switch (requestOperation.getMethod())
		{
		case GET:
			request = new HttpGet(requestOperation.getURI());
			break;
		case SUBSCRIBE:
			request = new HttpGet(requestOperation.getURI())
			{
				@Override
				public String getMethod()
				{
					return UpnpRequest.Method.SUBSCRIBE.getHttpName();
				}
			};
			break;
		case UNSUBSCRIBE:
			request = new HttpGet(requestOperation.getURI())
			{
				@Override
				public String getMethod()
				{
					return UpnpRequest.Method.UNSUBSCRIBE.getHttpName();
				}
			};
			break;
		case POST:
			HttpEntityEnclosingRequest post = new HttpPost(requestOperation.getURI());
			post.setEntity(createHttpRequestEntity(requestMessage));
			request = (HttpUriRequest) post; // Fantastic API
			break;
		case NOTIFY:
			HttpEntityEnclosingRequest notify = new HttpPost(requestOperation.getURI())
			{
				@Override
				public String getMethod()
				{
					return UpnpRequest.Method.NOTIFY.getHttpName();
				}
			};
			notify.setEntity(createHttpRequestEntity(requestMessage));
			request = (HttpUriRequest) notify; // Fantastic API
			break;
		default:
			throw new RuntimeException("Unknown HTTP method: " + requestOperation.getHttpMethodName());
		}

		// Headers
		request.setParams(getRequestParams(requestMessage));
		HeaderUtil.add(request, requestMessage.getHeaders());

		return request;
	}

	protected HttpEntity createHttpRequestEntity(UpnpMessage upnpMessage)
	{
		if (upnpMessage.getBodyType().equals(UpnpMessage.BodyType.BYTES))
		{
			if (log.isLoggable(Level.FINE))
				log.fine("Preparing HTTP request entity as byte[]");
			return new ByteArrayEntity(upnpMessage.getBodyBytes());
		}
		else
		{
			if (log.isLoggable(Level.FINE))
				log.fine("Preparing HTTP request entity as string");
			try
			{
				String charset = upnpMessage.getContentTypeCharset();
				return new StringEntity(upnpMessage.getBodyString(), charset != null ? charset : "UTF-8");
			}
			catch (Exception ex)
			{
				// WTF else am I supposed to do with this exception?
				throw new RuntimeException(ex);
			}
		}
	}

	protected HttpParams getRequestParams(StreamRequestMessage requestMessage)
	{
		HttpParams localParams = new BasicHttpParams();

		localParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, requestMessage.getOperation()
				.getHttpMinorVersion() == 0 ? HttpVersion.HTTP_1_0 : HttpVersion.HTTP_1_1);

		// DefaultHttpClient adds HOST header automatically in its default processor

		// Add the default user agent if not already set on the message
		if (!requestMessage.getHeaders().containsKey(UpnpHeader.Type.USER_AGENT))
		{
			HttpProtocolParams.setUserAgent(
					localParams,
					getConfiguration().getUserAgentValue(requestMessage.getUdaMajorVersion(),
							requestMessage.getUdaMinorVersion()));
		}

		return new DefaultedHttpParams(localParams, globalParams);
	}

	@Override
	public void stop()
	{
		log.fine("Shutting down HTTP client connection manager/pool");
		clientConnectionManager.shutdown();
	}

	@Override
	public StreamClientConfigurationImpl getConfiguration()
	{
		return configuration;
	}
}
