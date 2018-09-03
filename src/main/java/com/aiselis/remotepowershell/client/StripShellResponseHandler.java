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

import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author Alessio Pinna
 */
public class StripShellResponseHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        boolean isResponse = Boolean.FALSE.equals(context.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY));
        if (isResponse) {
            QName action = (QName) context.get(SOAPMessageContext.WSDL_OPERATION);
            if ("Create".equals(action.getLocalPart())) {
                Iterator<?> childIter = getBodyChildren(context);
                while(childIter.hasNext()) {
                    Object node = childIter.next();
                    if (node instanceof SOAPElement) {
                        SOAPElement el = (SOAPElement) node;
                        if ("Shell".equals(el.getLocalName())) {
                            childIter.remove();
                        }
                    }
                }
            }
        }
        return true;
    }

    private Iterator<?> getBodyChildren(SOAPMessageContext context) {
        try {
            SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
            SOAPBody body = envelope.getBody();
            return body.getChildElements();
        } catch (SOAPException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

}
