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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;
import org.w3c.dom.Element;

/**
 *
 * @author Alessio Pinna
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Shell", propOrder = {
    "shellId",
    "environment",
    "workingDirectory",
    "lifetime",
    "idleTimeout",
    "inputStreams",
    "outputStreams",
    "any"
})
public class Shell {

    @XmlElement(name = "ShellId")
    @XmlSchemaType(name = "anyURI")
    protected String shellId;
    @XmlElement(name = "Environment")
    protected EnvironmentVariableList environment;
    @XmlElement(name = "WorkingDirectory")
    protected String workingDirectory;
    @XmlElement(name = "Lifetime")
    protected Duration lifetime;
    @XmlElement(name = "IdleTimeout")
    protected Duration idleTimeout;
    @XmlList
    @XmlElement(name = "InputStreams")
    protected List<String> inputStreams;
    @XmlList
    @XmlElement(name = "OutputStreams")
    protected List<String> outputStreams;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    public String getShellId() {
        return shellId;
    }

    public void setShellId(String value) {
        this.shellId = value;
    }

    public EnvironmentVariableList getEnvironment() {
        return environment;
    }

    public void setEnvironment(EnvironmentVariableList value) {
        this.environment = value;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String value) {
        this.workingDirectory = value;
    }

    public Duration getLifetime() {
        return lifetime;
    }

    public void setLifetime(Duration value) {
        this.lifetime = value;
    }

    public Duration getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(Duration value) {
        this.idleTimeout = value;
    }

    public List<String> getInputStreams() {
        if (inputStreams == null) {
            inputStreams = new ArrayList<String>();
        }
        return this.inputStreams;
    }

    public List<String> getOutputStreams() {
        if (outputStreams == null) {
            outputStreams = new ArrayList<String>();
        }
        return this.outputStreams;
    }

    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

}
