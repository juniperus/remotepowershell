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

import java.util.Base64;

/**
 *
 * @author Alessio Pinna
 */
public class FileResult {
    private final String path;
    private final byte[] data;
    private final int statusCode;
    private final double executionTime;

    public FileResult(String path, CommandResult commandResult) {
        this.path = path;
        this.data = Base64.getDecoder().decode(commandResult.getOutput().trim());
        this.statusCode = commandResult.getStatusCode();
        this.executionTime = commandResult.getExecutionTime();
    }

    public String getPath() {
        return path;
    }

    public byte[] getData() {
        return data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getSize() {
        return data.length;
    }

    public double getExecutionTime() {
        return executionTime;
    }
    
}
