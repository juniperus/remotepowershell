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
package com.aiselis.remotepowershell.services;

import com.aiselis.remotepowershell.services.shell.CommandLine;
import com.aiselis.remotepowershell.services.shell.Receive;
import com.aiselis.remotepowershell.services.shell.ReceiveResponse;
import com.aiselis.remotepowershell.services.shell.Shell;
import com.aiselis.remotepowershell.services.transfer.ResourceCreated;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 *
 * @author Alessio Pinna
 */
@WebService(targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", name = "WinRm")
@XmlSeeAlso(value = {ObjectFactory.class, com.aiselis.remotepowershell.services.ObjectFactory.class, com.aiselis.remotepowershell.services.shell.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface WinRm {

    @WebResult(name = "CommandResponse", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", partName = "parameters")
    @Action(input = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Command", output = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandResponse")
    @WebMethod(operationName = "Command", action = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Command")
    public CommandResponse command(@WebParam(partName = "body", name = "CommandLine", targetNamespace = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell") CommandLine cl, @WebParam(partName = "ResourceURI", name = "ResourceURI", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) String string, @WebParam(partName = "MaxEnvelopeSize", name = "MaxEnvelopeSize", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) int i, @WebParam(partName = "OperationTimeout", name = "OperationTimeout", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) String string1, @WebParam(partName = "Locale", name = "Locale", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) Locale locale, @WebParam(partName = "SelectorSet", name = "SelectorSet", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) SelectorSetType sst, @WebParam(partName = "OptionSet", name = "OptionSet", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) OptionSetType ost);

    @WebResult(name = "ReceiveResponse", targetNamespace = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell", partName = "ReceiveResponse")
    @Action(input = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Receive", output = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/ReceiveResponse")
    @WebMethod(operationName = "Receive", action = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Receive")
    public ReceiveResponse receive(@WebParam(partName = "Receive", name = "Receive", targetNamespace = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell") Receive rcv, @WebParam(partName = "ResourceURI", name = "ResourceURI", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) String string, @WebParam(partName = "MaxEnvelopeSize", name = "MaxEnvelopeSize", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) int i, @WebParam(partName = "OperationTimeout", name = "OperationTimeout", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) String string1, @WebParam(partName = "Locale", name = "Locale", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) Locale locale, @WebParam(partName = "SelectorSet", name = "SelectorSet", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) SelectorSetType sst);

    @WebResult(name = "ResourceCreated", targetNamespace = "http://schemas.xmlsoap.org/ws/2004/09/transfer", partName = "ResourceCreated")
    @Action(input = "http://schemas.xmlsoap.org/ws/2004/09/transfer/Create", output = "http://schemas.xmlsoap.org/ws/2004/09/transfer/CreateResponse")
    @WebMethod(operationName = "Create", action = "http://schemas.xmlsoap.org/ws/2004/09/transfer/Create")
    public ResourceCreated create(@WebParam(partName = "Shell", name = "Shell", targetNamespace = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell") Shell shell, @WebParam(partName = "ResourceURI", name = "ResourceURI", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) String string, @WebParam(partName = "MaxEnvelopeSize", name = "MaxEnvelopeSize", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) int i, @WebParam(partName = "OperationTimeout", name = "OperationTimeout", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) String string1, @WebParam(partName = "Locale", name = "Locale", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) Locale locale, @WebParam(partName = "OptionSet", name = "OptionSet", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) OptionSetType ost);

    @WebResult(name = "SignalResponse", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", partName = "SignalResponse")
    @Action(input = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Signal", output = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/SignalResponse")
    @WebMethod(operationName = "Signal", action = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Signal")
    public SignalResponse signal(@WebParam(partName = "Signal", name = "Signal", targetNamespace = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell") Signal signal, @WebParam(partName = "ResourceURI", name = "ResourceURI", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) String string, @WebParam(partName = "MaxEnvelopeSize", name = "MaxEnvelopeSize", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) int i, @WebParam(partName = "OperationTimeout", name = "OperationTimeout", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) String string1, @WebParam(partName = "Locale", name = "Locale", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) Locale locale, @WebParam(partName = "SelectorSet", name = "SelectorSet", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) SelectorSetType sst);

    @WebResult(name = "DeleteResponse", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", partName = "result")
    @Action(input = "http://schemas.xmlsoap.org/ws/2004/09/transfer/Delete", output = "http://schemas.xmlsoap.org/ws/2004/09/transfer/DeleteResponse")
    @WebMethod(operationName = "Delete", action = "http://schemas.xmlsoap.org/ws/2004/09/transfer/Delete")
    public DeleteResponse delete(@WebParam(partName = "parameters", name = "Delete", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd") Delete delete, @WebParam(partName = "ResourceURI", name = "ResourceURI", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) String string, @WebParam(partName = "MaxEnvelopeSize", name = "MaxEnvelopeSize", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) int i, @WebParam(partName = "OperationTimeout", name = "OperationTimeout", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) String string1, @WebParam(partName = "Locale", name = "Locale", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) Locale locale, @WebParam(partName = "SelectorSet", name = "SelectorSet", targetNamespace = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd", header = true) SelectorSetType sst);
}
