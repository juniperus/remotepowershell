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
package com.aiselis.remotepowershell.client;

/**
 *
 * @author Alessio Pinna
 */
public final class WinRmParameters {
    public static final int MAX_ENVELOPER_SIZE = 153600;
    public static final String RESOURCE_URI = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/cmd";
    public static final String COMMAND_STATE_DONE = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandState/Done";
    public static final String SHELL_SIGNAL_TERMINATE = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/signal/terminate";
    public static final String WSMAN_XSD = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd";
    public static final String WSDL_SOAP = "http://schemas.xmlsoap.org/wsdl/soap12/";
    public static final String WSMAN_TIMEOUT_EXPIRED = "2150858793";
    public static final String WSMAN_SHELL_NOT_FOUND = "2150858843";
    
    private WinRmParameters () {
    }
    
}
