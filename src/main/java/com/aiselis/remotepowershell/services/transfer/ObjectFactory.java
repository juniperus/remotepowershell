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
package com.aiselis.remotepowershell.services.transfer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.aiselis.remotepowershell.services.shell.Shell;

/**
 *
 * @author Alessio Pinna
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ResourceCreated_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/09/transfer", "ResourceCreated");
    private final static QName _Shell_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/09/transfer", "Shell");

    public ObjectFactory() {
    }

    public ResourceCreated createResourceCreated() {
        return new ResourceCreated();
    }

    @XmlElementDecl(namespace = "http://schemas.xmlsoap.org/ws/2004/09/transfer", name = "ResourceCreated")
    public JAXBElement<ResourceCreated> createResourceCreated(ResourceCreated value) {
        return new JAXBElement<ResourceCreated>(_ResourceCreated_QNAME, ResourceCreated.class, null, value);
    }

    @XmlElementDecl(namespace = "http://schemas.xmlsoap.org/ws/2004/09/transfer", name = "Shell")
    public JAXBElement<Shell> createShell(Shell value) {
        return new JAXBElement<Shell>(_Shell_QNAME, Shell.class, null, value);
    }

}
