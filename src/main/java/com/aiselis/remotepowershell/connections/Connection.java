/*
 * Copyright 2018 Alessio Pinna <alessio@aiselis.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiselis.remotepowershell.connections;

import com.aiselis.remotepowershell.client.StripShellResponseHandler;
import com.aiselis.remotepowershell.services.WinRm;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.service.model.ServiceInfo;
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.addressing.policy.MetadataConstants;
import org.apache.cxf.ws.policy.PolicyConstants;
import org.apache.cxf.ws.policy.builder.primitive.PrimitiveAssertion;
import org.apache.neethi.Policy;

/**
 *
 * @author Alessio Pinna
 */
public abstract class Connection {
    
    protected String username;
    protected String endpoint;
    protected String password;
    protected String domain;
    protected boolean checkSSL;
    protected HostnameVerifier hostnameVerifier;
    protected BindingProvider bindingProvider;
    protected Client client;
    protected long receiveTimeout;
    

    public Connection() {
        domain = null;
        checkSSL = true;
        hostnameVerifier = null;
    }
    
    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setCredentials(String domain, String username, String password) {
        this.domain = domain;
        this.username = username;
        this.password = password;
    }
    
    public void setCheckSSL(boolean checkSSL) {
        this.checkSSL = checkSSL;
    }
    
    public void setCheckHostname(HostnameVerifier checkHostname) {
        this.hostnameVerifier = checkHostname;
    }
    
    public void setReceiveTimeout(long receviceTimeout) {
        this.receiveTimeout = receviceTimeout;
    }
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public WinRm configure(WinRm winrm) {
        client = ClientProxy.getClient(winrm);

        ServiceInfo serviceInfo = client.getEndpoint().getEndpointInfo().getService();
        serviceInfo.setProperty("soap.force.doclit.bare", true);
        
        bindingProvider = (BindingProvider) winrm;
        
        List<Handler> handlerChain = Arrays.<Handler>asList(new StripShellResponseHandler());
        bindingProvider.getBinding().setHandlerChain(handlerChain);

        Policy policy = new Policy();
        policy.addAssertion(new PrimitiveAssertion(MetadataConstants.USING_ADDRESSING_2004_QNAME));
        bindingProvider.getRequestContext().put(PolicyConstants.POLICY_OVERRIDE, policy);

        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
        
        this.authenticate();
       
        AsyncHTTPConduit httpClient = (AsyncHTTPConduit) this.client.getConduit();
        
        if (!this.checkSSL) {
            TLSClientParameters tlsClientParameters = new TLSClientParameters();
            tlsClientParameters.setDisableCNCheck(true);
            tlsClientParameters.setTrustManagers(new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }});
            httpClient.setTlsClientParameters(tlsClientParameters);
        }
        
        if (hostnameVerifier != null) {
            TLSClientParameters tlsClientParameters = new TLSClientParameters();
            tlsClientParameters.setHostnameVerifier(hostnameVerifier);
            httpClient.setTlsClientParameters(tlsClientParameters);
        }
        
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setAllowChunking(false);
        httpClientPolicy.setReceiveTimeout(receiveTimeout);

        httpClient.setClient(httpClientPolicy);
        httpClient.getClient().setAutoRedirect(true);
        return winrm;
    }
    
    protected abstract void authenticate();
    
}
