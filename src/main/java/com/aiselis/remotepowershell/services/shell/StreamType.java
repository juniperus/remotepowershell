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
package com.aiselis.remotepowershell.services.shell;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author Alessio Pinna
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StreamType", propOrder = {
    "value"
})
public class StreamType {

    @XmlValue
    protected byte[] value;
    @XmlAttribute(name = "Name", required = true)
    protected String name;
    @XmlAttribute(name = "CommandId")
    protected String commandId;
    @XmlAttribute(name = "End")
    protected Boolean end;
    @XmlAttribute(name = "Unit")
    @XmlSchemaType(name = "anyURI")
    protected String unit;
    @XmlAttribute(name = "EndUnit")
    protected Boolean endUnit;

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String value) {
        this.commandId = value;
    }

    public Boolean isEnd() {
        return end;
    }

    public void setEnd(Boolean value) {
        this.end = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String value) {
        this.unit = value;
    }

    public Boolean isEndUnit() {
        return endUnit;
    }

    public void setEndUnit(Boolean value) {
        this.endUnit = value;
    }

}
