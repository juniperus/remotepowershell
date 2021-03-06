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
package com.aiselis.remotepowershell.schemes;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.impl.auth.NTLMScheme;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

/**
 *
 * @author Alessio Pinna
 */
public class SpNegoNTLMSchemeFactory extends NTLMSchemeFactory {

    @Override
    public AuthScheme create(HttpContext context) {
        return new NTLMScheme() {

            @Override
            public String getSchemeName() {
                return AuthSchemes.SPNEGO;
            }

            @Override
            public Header authenticate(Credentials credentials, HttpRequest request)
                    throws AuthenticationException {
                Header hdr = super.authenticate(credentials, request);
                return new BasicHeader(hdr.getName(), hdr.getValue().replace("NTLM", getSchemeName()));
            }
            
        };
    }
}
