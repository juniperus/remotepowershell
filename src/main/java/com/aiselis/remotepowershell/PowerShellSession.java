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

import com.aiselis.remotepowershell.client.WinRmParameters;
import com.aiselis.remotepowershell.services.CommandResponse;
import com.aiselis.remotepowershell.services.Locale;
import com.aiselis.remotepowershell.services.OptionSetType;
import com.aiselis.remotepowershell.services.OptionType;
import com.aiselis.remotepowershell.services.SelectorSetType;
import com.aiselis.remotepowershell.services.SelectorType;
import com.aiselis.remotepowershell.services.Signal;
import com.aiselis.remotepowershell.services.WinRm;
import com.aiselis.remotepowershell.services.shell.CommandLine;
import com.aiselis.remotepowershell.services.shell.CommandStateType;
import com.aiselis.remotepowershell.services.shell.DesiredStreamType;
import com.aiselis.remotepowershell.services.shell.Receive;
import com.aiselis.remotepowershell.services.shell.ReceiveResponse;
import com.aiselis.remotepowershell.services.shell.StreamType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.soap.SOAPFaultException;
import org.w3c.dom.NodeList;

/**
 *
 * @author Alessio Pinna
 */
public class PowerShellSession implements AutoCloseable {

    private final WinRm winrm;
    private final SelectorSetType shellSelector;

    private final String operationTimeout;
    private final Locale locale;

    public PowerShellSession(WinRm winrm, String shellId, String operationTimeout, Locale locale) {
        this.winrm = winrm;
        this.shellSelector = createShellSelector(shellId);
        this.operationTimeout = operationTimeout;
        this.locale = locale;
    }

    private SelectorSetType createShellSelector(String shellId) {
        SelectorSetType selectorSetType = new SelectorSetType();
        SelectorType selectorType = new SelectorType();
        selectorType.setName("ShellId");
        selectorType.getContent().add(shellId);
        selectorSetType.getSelector().add(selectorType);
        return selectorSetType;
    }

    private int executeCommand(String cmd, Writer out, Writer err) {

        final CommandLine cmdLine = new CommandLine();
        cmdLine.setCommand(cmd);
        final OptionSetType optSetCmd = new OptionSetType();
        OptionType optConsolemodeStdin = new OptionType();
        optConsolemodeStdin.setName("WINRS_CONSOLEMODE_STDIN");
        optConsolemodeStdin.setValue("TRUE");
        optSetCmd.getOption().add(optConsolemodeStdin);
        OptionType optSkipCmdShell = new OptionType();
        optSkipCmdShell.setName("WINRS_SKIP_CMD_SHELL");
        optSkipCmdShell.setValue("FALSE");
        optSetCmd.getOption().add(optSkipCmdShell);

        CommandResponse cmdResponse = winrm.command(cmdLine, WinRmParameters.RESOURCE_URI, WinRmParameters.MAX_ENVELOPER_SIZE, operationTimeout, locale, shellSelector, optSetCmd);

        String commandId = cmdResponse.getCommandId();

        try {
            return receiveCommand(commandId, out, err);
        } finally {
            try {
                releaseCommand(commandId);
            } catch (SOAPFaultException soapFault) {
                assertFaultCode(soapFault, WinRmParameters.WSMAN_SHELL_NOT_FOUND);
            }
        }
    }

    public CommandResult execute(String command) {
        long startTime = System.nanoTime();
        String encoded = DatatypeConverter.printBase64Binary(command.getBytes(Charset.forName("UTF-16LE")));
        StringWriter out = new StringWriter();
        StringWriter err = new StringWriter();
        int code = this.executeCommand("powershell -encodedcommand " + encoded, out, err);
        long endTime = System.nanoTime();
        return new CommandResult(out.toString(), err.toString(), code, (endTime - startTime) / 1e6);
    }

    public CommandResult execute(List<String> script) {
        String command = String.join(" ", script);
        long startTime = System.nanoTime();
        String encoded = DatatypeConverter.printBase64Binary(command.getBytes(Charset.forName("UTF-16LE")));
        StringWriter out = new StringWriter();
        StringWriter err = new StringWriter();
        int code = this.executeCommand("powershell -encodedcommand " + encoded, out, err);
        long endTime = System.nanoTime();
        return new CommandResult(out.toString(), err.toString(), code, (endTime - startTime) / 1e6);
    }

    public FileResult downloadFile(String path) {
        String downloadScript = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("download-script.ps1")))
                .lines().collect(Collectors.joining("\n")).replace("{{path}}", path);
        return new FileResult(path, this.execute(downloadScript));
    }
    
    public DirectoryResult listDirectory(String path) {
        String listDirectoryScript = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("get-directory-script.ps1")))
                .lines().collect(Collectors.joining("\n")).replace("{{path}}", path);
        return new DirectoryResult(path, this.execute(listDirectoryScript));
    }

    private int receiveCommand(String commandId, Writer out, Writer err) {
        while (true) {
            final Receive receive = new Receive();
            DesiredStreamType stream = new DesiredStreamType();
            stream.setCommandId(commandId);
            stream.setValue("stdout stderr");
            receive.setDesiredStream(stream);

            try {
                ReceiveResponse receiveResponse = winrm.receive(receive, WinRmParameters.RESOURCE_URI, WinRmParameters.MAX_ENVELOPER_SIZE, operationTimeout, locale, shellSelector);
                getStreams(receiveResponse, out, err);

                CommandStateType state = receiveResponse.getCommandState();
                if (WinRmParameters.COMMAND_STATE_DONE.equals(state.getState())) {
                    return state.getExitCode().intValue();
                }
            } catch (SOAPFaultException soapFault) {
                assertFaultCode(soapFault, WinRmParameters.WSMAN_TIMEOUT_EXPIRED);
            }
        }
    }

    private void assertFaultCode(SOAPFaultException soapFault, String code) {
        try {
            NodeList faultDetails = soapFault.getFault().getDetail().getChildNodes();
            for (int i = 0; i < faultDetails.getLength(); i++) {
                if (faultDetails.item(i).getLocalName().equals("WSManFault")) {
                    if (faultDetails.item(i).getAttributes().getNamedItem("Code").getNodeValue().equals(code)) {
                        return;
                    } else {
                        throw soapFault;
                    }
                }
            }
            throw soapFault;
        } catch (NullPointerException e) {
            throw soapFault;
        }
    }

    private void getStreams(ReceiveResponse receiveResponse, Writer out, Writer err) {
        List<StreamType> streams = receiveResponse.getStream();
        for (StreamType s : streams) {
            byte[] value = s.getValue();
            if (value == null) {
                continue;
            }
            if (out != null && "stdout".equals(s.getName())) {
                try {
                    //TODO use passed locale?
                    if (value.length > 0) {
                        out.write(new String(value));
                    }
                    if (Boolean.TRUE.equals(s.isEnd())) {
                        out.close();
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            if (err != null && "stderr".equals(s.getName())) {
                try {
                    if (value.length > 0) {
                        err.write(new String(value));
                    }
                    if (Boolean.TRUE.equals(s.isEnd())) {
                        err.close();
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        try {
            out.close();
            err.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void releaseCommand(String commandId) {
        final Signal signal = new Signal();
        signal.setCommandId(commandId);
        signal.setCode(WinRmParameters.SHELL_SIGNAL_TERMINATE);
        winrm.signal(signal, WinRmParameters.RESOURCE_URI, WinRmParameters.MAX_ENVELOPER_SIZE, operationTimeout, locale, shellSelector);
    }

    @Override
    public void close() {
        try {
            winrm.delete(null, WinRmParameters.RESOURCE_URI, WinRmParameters.MAX_ENVELOPER_SIZE, operationTimeout, locale, shellSelector);
        } catch (SOAPFaultException soapFault) {
            assertFaultCode(soapFault, WinRmParameters.WSMAN_SHELL_NOT_FOUND);
        }
    }
}
