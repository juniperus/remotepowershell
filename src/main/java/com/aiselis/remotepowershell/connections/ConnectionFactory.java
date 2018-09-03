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

import org.apache.http.client.config.AuthSchemes;

/**
 *
 * @author Alessio Pinna
 */
public class ConnectionFactory {

    public static Connection getAuthentication(String scheme) {
        Connection connection = null;
        switch (scheme) {
            case AuthSchemes.BASIC:
                connection = new ConnectionBasic();
                break;
            case AuthSchemes.NTLM:
                connection = new ConnectionNtlm();
                break;
            case AuthSchemes.KERBEROS:
                connection = new ConnectionKerberos();
                break;
            default:
                throw new UnsupportedOperationException("No such authentication scheme " + scheme);
        }
        return connection;
    }
}
