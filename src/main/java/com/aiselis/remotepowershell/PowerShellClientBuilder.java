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
package com.aiselis.remotepowershell;

import com.aiselis.remotepowershell.client.WinRmClientSettings;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import org.apache.http.client.config.AuthSchemes;

/**
 *
 * @author Alessio Pinna
 */
public class PowerShellClientBuilder {

    private final WinRmClientSettings settings;

    public PowerShellClientBuilder() {
        settings = new WinRmClientSettings();
        settings.setLocale(Locale.US);
        settings.setAuthScheme(AuthSchemes.NTLM);
        settings.setOperationTimeout(60l * 1000l);
        settings.setRetriesForConnection(1);
        settings.setCheckSSL(true);
        settings.setDomain(null);
        settings.setEnvironment(new HashMap<>());
        settings.setHostnameVerifier(null);
        settings.setWorkingDirectory("C:\\");
    }

    public PowerShellClientBuilder endpoint(String endpoint) throws MalformedURLException {
        settings.setEndpoint(new URL(endpoint));
        return this;
    }

    public PowerShellClientBuilder checkSSL(boolean checkSSL) {
        settings.setCheckSSL(checkSSL);
        return this;
    }

    public PowerShellClientBuilder credentials(String username, String password) {
        settings.setUsername(username);
        settings.setPassword(password);
        return this;
    }

    public PowerShellClientBuilder credentials(String domain, String username, String password) {
        settings.setDomain(domain);
        settings.setUsername(username);
        settings.setPassword(password);
        return this;
    }

    public PowerShellClientBuilder authBasic() {
        settings.setAuthScheme(AuthSchemes.BASIC);
        return this;
    }

    public PowerShellClientBuilder authKerberos() {
        settings.setAuthScheme(AuthSchemes.KERBEROS);
        return this;
    }

    public PowerShellClientBuilder authNTLM() {
        settings.setAuthScheme(AuthSchemes.NTLM);
        return this;
    }
    
    public PowerShellClientBuilder locale(Locale locale) {
        settings.setLocale(locale);
        return this;
    }
    
    public PowerShellClientBuilder workingDirectory(String workingDirectory) {
        settings.setWorkingDirectory(workingDirectory);
        return this;
    }

    public PowerShellClientBuilder operationTimeout(long operationTimeout) {
        settings.setOperationTimeout(operationTimeout);
        return this;
    }
    
    public PowerShellClientBuilder retriesForConnection(int retriesForConnection) {
        settings.setRetriesForConnection(retriesForConnection);
        return this;
    }
    
    public PowerShellClientBuilder environment(Map<String, String> environment) {
        settings.setEnvironment(environment);
        return this;
    }
    
    public PowerShellClientBuilder hostnameVerifier(HostnameVerifier hostnameVerifier) {
        settings.setHostnameVerifier(hostnameVerifier);
        return this;
    }
    
    public PowerShellClient build() {
        return new PowerShellClient(settings);
    }

}
