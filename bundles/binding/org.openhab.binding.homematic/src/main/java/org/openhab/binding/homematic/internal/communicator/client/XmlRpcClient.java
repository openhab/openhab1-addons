/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.rpc.RpcRequest;
import org.openhab.binding.homematic.internal.rpc.XmlRpcRequest;
import org.openhab.binding.homematic.internal.rpc.XmlRpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client implementation for sending messages via XML-RPC to the Homematic
 * server.
 *
 * @author Gerhard Riegler
 * @since 1.9.0
 */
public class XmlRpcClient extends BinRpcClient {
    private final static Logger logger = LoggerFactory.getLogger(XmlRpcClient.class);
    private HttpClient httpClient;

    public XmlRpcClient() {
        httpClient = new HttpClient(new SimpleHttpConnectionManager(true));
        HttpClientParams params = httpClient.getParams();
        Long timeout = context.getConfig().getTimeout() * 1000L;
        params.setConnectionManagerTimeout(timeout);
        params.setSoTimeout(timeout.intValue());
        params.setContentCharset("ISO-8859-1");
        params.setParameter(HttpMethodParams.RETRY_HANDLER, new HttpMethodRetryHandler() {

            @Override
            public boolean retryMethod(HttpMethod method, IOException exception, int executionCount) {
                return false;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RpcRequest createRpcRequest(String methodName) {
        return new XmlRpcRequest(methodName);
    }

    /**
     * Returns the XML-RPC url.
     */
    @Override
    protected String getRpcCallbackUrl() {
        return "http://" + context.getConfig().getCallbackHost() + ":" + context.getConfig().getCallbackPort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected synchronized Object[] sendMessage(HmInterface hmInterface, RpcRequest request)
            throws HomematicClientException {
        PostMethod post = null;
        try {
            if (TRACE_ENABLED) {
                logger.trace("Client XmlRpcRequest: \n{}", request);
            }

            post = new PostMethod(String.format("http://%s:%s", context.getConfig().getHost(), hmInterface.getPort()));
            RequestEntity re = new ByteArrayRequestEntity(request.createMessage());
            post.setRequestEntity(re);
            httpClient.executeMethod(post);

            String result = post.getResponseBodyAsString();
            if (TRACE_ENABLED) {
                logger.trace("Client XmlRpcResponse: \n{}", result);
            }

            Object[] data = new XmlRpcResponse(post.getResponseBodyAsStream()).getResponseData();
            if (data != null && data.length > 0) {
                checkIfFault(data);
            } else if (data == null) {
                throw new IOException("Unknown Result: " + data);
            }
            return data;
        } catch (ConnectException cex) {
            if (HmInterface.WIRED == hmInterface || HmInterface.CUXD == hmInterface
                    || HmInterface.HMIP == hmInterface) {
                logger.info("Interface {} not available, disabling support.", hmInterface);
                return null;
            }
            throw new HomematicClientException("Can't connect to interface " + hmInterface + ": " + cex.getMessage(),
                    cex);
        } catch (Exception ex) {
            throw new HomematicClientException(ex.getMessage() + " sending (" + request + ")", ex);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
    }
}
