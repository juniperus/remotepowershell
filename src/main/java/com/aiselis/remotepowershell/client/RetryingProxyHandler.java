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

import com.aiselis.remotepowershell.services.WinRm;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

/**
 *
 * @author Alessio Pinna
 */
public class RetryingProxyHandler implements InvocationHandler {

    private final WinRm winrm;
    private final int retriesForConnectionFailures;

    public RetryingProxyHandler(WinRm winrm, int retriesForConnectionFailures) {
        this.winrm = winrm;
        this.retriesForConnectionFailures = retriesForConnectionFailures;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Don't retry the "command" - could lead to unexpected side effects of having the script run multiple times.
        if (method.getName().equals("command")) {
            return method.invoke(winrm, args);
        } else {
            return invokeWithRetry(method, args);
        }
    }

    public Object invokeWithRetry(Method method, Object[] args)
            throws IllegalAccessException, InvocationTargetException {
        List<Throwable> exceptions = new ArrayList<>();

        for (int i = 0; i < retriesForConnectionFailures + 1; i++) {
            try {
                return method.invoke(winrm, args);
            } catch (InvocationTargetException targetException) {
                Throwable e = targetException.getTargetException();
                if (e instanceof SOAPFaultException) {
                    throw (SOAPFaultException)e;
                } else if (e instanceof WebServiceException) {
                    WebServiceException wsException = (WebServiceException) e;
                    if (!(wsException.getCause() instanceof IOException)) {
                        throw new RuntimeException("Exception occurred while making winrm call", wsException);
                    }
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Exception occured while making winrm call", e.initCause(wsException));
                    }
                    exceptions.add(wsException);
                } else {
                    throw new IllegalStateException("Failure when calling " + method + Arrays.toString(args), e);
                }
            }
        }
        throw new RuntimeException("failed task " + method.getName(), exceptions.get(0));
    }

}
