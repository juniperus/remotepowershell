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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Alessio Pinna
 */
public class DirectoryResult {

    public class Entry {
        private final String name;
        private final Date lastWrite;
        private final Date lastAccess;
        private final Date creation;
        private final String attributes;
        private final long size;

        public Entry(String name, Date lastWrite, Date lastAccess, Date creation, String attributes, long size) {
            this.name = name;
            this.lastWrite = lastWrite;
            this.lastAccess = lastAccess;
            this.creation = creation;
            this.attributes = attributes;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public Date getLastWrite() {
            return lastWrite;
        }

        public Date getLastAccess() {
            return lastAccess;
        }

        public Date getCreation() {
            return creation;
        }

        public String getAttributes() {
            return attributes;
        }

        public long getSize() {
            return size;
        }
        
    }

    private final String path;
    private final double executionTime;
    private final int statusCode;
    private final List<Entry> entries;

    public DirectoryResult(String path, CommandResult commandResult) {
        this.path = path;
        this.executionTime = commandResult.getExecutionTime();
        this.statusCode = commandResult.getStatusCode();
        this.entries = new ArrayList<>();
        Pattern regex = Pattern.compile("Name\\s+: (.+)\\s+LastWriteTime\\s+: ([0-9]{14}[0-9:\\+-]{0,6})\\s+LastAccessTime\\s+: ([0-9]{14}[0-9:\\+-]{0,6})\\s+CreationTime\\s+: ([0-9]{14}[0-9:\\+-]{0,6})\\s+Mode\\s+: ([adhsr-]+)\\s+Length\\s+: {0,1}([0-9]{0,})");
        Matcher matcher = regex.matcher(commandResult.getOutput());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssXXX");
        while (matcher.find()) {
            try {
                entries.add(new Entry(
                        matcher.group(1),
                        format.parse(matcher.group(2)),
                        format.parse(matcher.group(3)),
                        format.parse(matcher.group(4)),
                        matcher.group(5),
                        (matcher.group(6).isEmpty()) ? 0 : Long.parseUnsignedLong(matcher.group(6))
                ));
            } catch (ParseException ex) {
            }
        }
    }

    public String getPath() {
        return path;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<Entry> getEntries() {
        return entries;
    }
    
}
