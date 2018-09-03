# Remote PowerShell
Java library for launch remote PowerShell commands and scripts in a remote Windows host via WinRm service. This library implements (by internal PowerShell scripts) some functions that are non available in WinRm.

### Add maven dependency
Add
```
<dependency>
    <groupId>com.aiselis</groupId>
    <artifactId>remote-powershell</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Create session
Create new PowerShell session using the builder:
```
PowerShellClient client = builder.endpoint("https://host:5986/wsman")
    .credentials("domain", "user", "password")
    .checkSSL(false)
    .authNTLM()
    .workingDirectory("c:\\")
    .build();
    
PowerShellSession session = client.getSession();
```
For now this library supports only basic, NTLM and Kerberos schemes. The credentials can be without domain:
```
credentials("user", "password")
```
The client must closed when finished:
```
client.disconnect();
```

### Run PowerShell script
A PowerShell script can be run on the remote machine from the current session passing a simple string
```
CommandResult output = session.execute("<PowerShell script or command>");
```
or passing a list of strings
```
List<String> commands = new List<>();
commands.add("command 1");
commands.add("command 2");
...
CommandResult output = session.execute(command);
```
CommandResult contains:
  * **output.getStatusCode()**: the status code of command launched (0 => execution OK);
  * **output.getOutput()**: the regular output of the execution of command on remote machine, empty if command failed;
  * **output.getError()**: the message error if command fails execution. It is empty if the command is executes successfully;
  * **output.getExecutionTime()**: the remote command execution time in milliseconds.

### Get remote file
Natively WinRM service does not have calls to download files from a remote machine, but in Remote PowerShell is implemented a routine that allows you to do it easily.
```
FileResult file = session.downloadFile("C:\\directory\\file.txt");
```
FileResult contains:
  * **file.getPath()**: the remote path of file;
  * **file.getData()**: the raw content of the file expressed as byte[];
  * **file.getSize()**: the file length in bytes;
  * **file.getStatusCode()**: the transfer status code (0 => transfer OK);
  * **file.getExecutionTime()**: the transfer time of the file in milliseconds.

### Get remote dir content
Another service that not exists natively in WinRM is the content list of a directory and also this is implemented in Remote PowerShell.
```
DirectoryResult dirContent = client.getSession().listDirectory("c:\\");
```
DirectoryResult contains:
  * **dirContent.getPath()**: the remote path of file;
  * **dirContent.getEntries()**: the list of the items contained in directory as List<Entry>;
  * **dirContent.getStatusCode()**: the transfer status code (0 => transfer OK);
  * **dirContent.getExecutionTime()**: the transfer time of the file in milliseconds.

Entry:
  * **entry.getName()**: the name of item;
  * **entry.getCreation()**: creation date;
  * **entry.getLastWrite()**: last write date;
  * **entry.getLastAccess()**: last access date;
  * **entry.getSize()**: size in bytes;
  * **entry.getAttributes()**: windows attributes.