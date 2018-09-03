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

import com.aiselis.remotepowershell.schemes.SpNegoNTLMSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;

/**
 *
 * @author Alessio Pinna
 */
public class ConnectionNtlm extends Connection {
        
    @Override
    protected void authenticate() {
        Credentials creds = new NTCredentials(this.username, this.password, null, this.domain);

        Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
                .register(AuthSchemes.BASIC, new BasicSchemeFactory())
                .register(AuthSchemes.SPNEGO, new SpNegoNTLMSchemeFactory())
                .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
                .build();

        this.bindingProvider.getRequestContext().put(Credentials.class.getName(), creds);
        this.bindingProvider.getRequestContext().put("http.autoredirect", true);
        this.bindingProvider.getRequestContext().put(AuthSchemeProvider.class.getName(), authSchemeRegistry);
    }

}
