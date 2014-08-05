Account Service Server v1.0.


Application creates server which can handle clent's requests sent using Account Service Client v1.0 or Account Service Test Client v1.0.


System requirements.

- Installed JRE 1.7 or higher.
- Support of MySQL.
- Console.
- Vacant 4778 port.

To check your JRE version input "java -version" in console.


Running application.


To run application run executable jar-file Server.jar using console. Server is ready to serve clients after "Server is ready to server client." appers in console. It make take a few seconds.
Windows users can simply use bat-file "RunServer.bat".


Server configurations.


All configuration parameters can be found in "Config.txt".
NB! Do not change structure in "Config.txt", do nod add or remove any space symbols.
Database username. Must be nonempty. Default value is "root".
Database password. Can be emplty. Default value is empty.
Database url. Must be nonempty. Defaul value is "localhost". Change this value to your server's url. Use default settings to run database server at the local machine.
Number of serving threads. Must be positive integer. Default value is 10. Recommended value is not greater than maximal number of parallel processed allowed decreased by 1. Use exactly this value if it is not expected from server to execute any hard-computative operations except of this one. In the other case optimal value will be decreased.
Cache maximum size. Must be positive integer. Default value is 10000. Recommended value is a bit greater than expected number of online users.


Statistics.


Account Service Server v1.0 automatically generates and writes in log file "Statistics.txt" statistics of server working.
Statistics can be dropped with clearing of log by the command "drop" sent by client.
Statistics log does not drop automatically after server restart. New statistics will be appended.


Known problems.


- 'java' is not recognized as an internal or external command.

You have incorrectly installed JRE or do not have it at all.

- java.lang.UnsupportedClassVersionError: ServerRunner : Unsupported major.minor version ...

Your JRE version is not at least 1.7.

- com.mysql.jdbc.CommunicationException: Communications link failure due to underlying exception:

Your server does not support MySQL.

- java.sql.SQLException: Access denied for user ...

Incorrect login or password.

- Java.net.BindException: Address already in use: JVM_Bind

Port 4778 is already used. Maybe you try to run new Account Service Server v1.0 while there is another one same application running on server. It is impossible.



_______________
Kirill Savenkov
ostrich@flyingsteps.org
06.08.2014
