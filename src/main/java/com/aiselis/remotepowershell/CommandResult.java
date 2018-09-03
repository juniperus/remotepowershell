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

/**
 *
 * @author Alessio Pinna
 */
public class CommandResult {
    
    private final String output;
    private final String error;
    private final int statusCode;
    private final double executionTime;

    public CommandResult(String output, String error, int statusCode, double executionTime) {
        this.output = output;
        this.error = error;
        this.statusCode = statusCode;
        this.executionTime = executionTime;
    }

    public String getOutput() {
        return output;
    }

    public String getError() {
        return error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public double getExecutionTime() {
        return executionTime;
    }

}
