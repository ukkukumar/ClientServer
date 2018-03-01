# ClientServer
Client Server in Java

This repository contains Client and Server programs.

Usage:

Server:

java -jar Userver.jar <port> <file-name>
  
Client:

java -jar Uclient.jar <ip-address> <port>

Example usage:

java -jar Userver.jar 50002 "ADT_PIC_Code.pdf"
java -jar Uclient.jar 127.0.0.1 50002

Following are the list of files:

Userver.java - Java source file for Server program
Uclient.java - Java source file for Client program
Userver.class - Compiled Server Class
Uclient.class - Compiled Client Class
Userver.jar - Server JAR file
Uclient.jar - Client JAR file
ADT_PIC_Code.pdf - Sample file to transfer
client_manifest.txt - Manifest file to convert Uclient.class to Uclient.jar
server_manifest.txt - Manifest file to conver Userver.class to Userver.jar
