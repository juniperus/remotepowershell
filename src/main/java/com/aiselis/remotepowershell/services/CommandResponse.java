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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Alessio Pinna
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommandResponse", propOrder = {
    "commandId"
})
public class CommandResponse {

    @XmlElement(name = "CommandId", namespace = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell")
    protected String commandId;

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String value) {
        this.commandId = value;
    }

}
