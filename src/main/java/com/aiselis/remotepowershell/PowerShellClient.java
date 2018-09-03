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
package com.aiselis.remotepowershell;

import com.aiselis.remotepowershell.client.WinRmClientSettings;
import com.aiselis.remotepowershell.client.RetryingProxyHandler;
import com.aiselis.remotepowershell.client.WinRmParameters;
import com.aiselis.remotepowershell.connections.Connection;
import com.aiselis.remotepowershell.connections.ConnectionFactory;
import com.aiselis.remotepowershell.services.Locale;
import com.aiselis.remotepowershell.services.OptionSetType;
import com.aiselis.remotepowershell.services.OptionType;
import com.aiselis.remotepowershell.services.WinRm;
import com.aiselis.remotepowershell.services.shell.EnvironmentVariable;
import com.aiselis.remotepowershell.services.shell.EnvironmentVariableList;
import com.aiselis.remotepowershell.services.shell.Shell;
import com.aiselis.remotepowershell.services.transfer.ResourceCreated;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduit;
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduitFactory;
import org.w3c.dom.Element;

/**
 *
 * @author Alessio Pinna
 */
public class PowerShellClient {

    private final WinRmClientSettings settings;
    private final RetryingProxyHandler retryingHandler;
    private final WinRm winrm;
    private final Bus bus;

    private static String getShellId(ResourceCreated resourceCreated) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        for (Element el : resourceCreated.getAny()) {
            String shellId;
            try {
                shellId = xpath.evaluate("//*[local-name()='Selector' and @Name='ShellId']", el);
            } catch (XPathExpressionException e) {
                throw new IllegalStateException(e);
            }
            if (shellId != null && !shellId.isEmpty()) {
                return shellId;
            }
        }
        throw new IllegalStateException("Shell ID not found in " + resourceCreated);
    }
    
    private static WinRm createWinRmInstance(Bus bus) {
        Bus prevBus = BusFactory.getAndSetThreadDefaultBus(bus);
        try {
            Service service = Service.create(new QName(WinRmParameters.WSMAN_XSD, "WinRmService"));
            QName portName = new QName(WinRmParameters.WSMAN_XSD, "WinRmPort");
            service.addPort(portName, WinRmParameters.WSDL_SOAP, "");
            return service.getPort(portName, WinRm.class);
        } finally {
            if (BusFactory.getThreadDefaultBus(false) != prevBus) {
                BusFactory.setThreadDefaultBus(prevBus);
            }
        }
    }

    public PowerShellClient(WinRmClientSettings settings) {
        this.settings = settings;
        this.bus = BusFactory.newInstance().createBus();
        this.bus.getProperties().put(AsyncHTTPConduit.USE_ASYNC, true);
        this.bus.getProperties().put(AsyncHTTPConduitFactory.USE_POLICY, AsyncHTTPConduitFactory.UseAsyncPolicy.ALWAYS);
        WinRm service = this.createWinRmInstance(this.bus);
        Connection configurator = ConnectionFactory.getAuthentication(settings.getAuthScheme());
        configurator.setCheckHostname(settings.getHostnameVerifier());
        configurator.setCredentials(settings.getDomain(), settings.getUsername(), settings.getPassword());
        configurator.setCheckSSL(settings.isCheckSSL());
        configurator.setReceiveTimeout(settings.getOperationTimeout());
        configurator.setEndpoint(settings.getEndpoint().toString());
        service = configurator.configure(service);
        retryingHandler = new RetryingProxyHandler(service, settings.getRetriesForConnection());
        this.winrm = (WinRm) Proxy.newProxyInstance(WinRm.class.getClassLoader(),
                new Class[]{WinRm.class, BindingProvider.class},
                retryingHandler);
    }

    public PowerShellSession getSession() {
        final Shell shell = new Shell();
        shell.getInputStreams().add("stdin");
        shell.getOutputStreams().add("stdout");
        shell.getOutputStreams().add("stderr");
        if (settings.getWorkingDirectory() != null) {
            shell.setWorkingDirectory(settings.getWorkingDirectory());
        }
        if (settings.getEnvironment() != null && !settings.getEnvironment().isEmpty()) {
            EnvironmentVariableList env = new EnvironmentVariableList();
            List<EnvironmentVariable> vars = env.getVariable();
            for (Map.Entry<String, String> entry : settings.getEnvironment().entrySet()) {
                EnvironmentVariable var = new EnvironmentVariable();
                var.setName(entry.getKey());
                var.setValue(entry.getValue());
                vars.add(var);
            }
            shell.setEnvironment(env);
        }

        final OptionSetType optSetCreate = new OptionSetType();
        OptionType optNoProfile = new OptionType();
        optNoProfile.setName("WINRS_NOPROFILE");
        optNoProfile.setValue("FALSE");
        optSetCreate.getOption().add(optNoProfile);
        OptionType optCodepage = new OptionType();
        optCodepage.setName("WINRS_CODEPAGE");
        optCodepage.setValue("437");
        optSetCreate.getOption().add(optCodepage);
        Locale locale = new Locale();
        locale.setLang(settings.getLocale().toLanguageTag());
        BigDecimal bdMs = BigDecimal.valueOf(settings.getOperationTimeout());
        BigDecimal bdSec = bdMs.divide(BigDecimal.valueOf(1000));
        DecimalFormat df = new DecimalFormat("PT#.###S", new DecimalFormatSymbols(java.util.Locale.ROOT));
        String operationTimeout = df.format(bdSec);
        ResourceCreated resourceCreated = winrm.create(shell, WinRmParameters.RESOURCE_URI, WinRmParameters.MAX_ENVELOPER_SIZE, operationTimeout, locale, optSetCreate);
        String shellId = getShellId(resourceCreated);
        return new PowerShellSession(winrm, shellId, operationTimeout, locale);
    }

    public void disconnect() {
        bus.shutdown(true);
    }
}
