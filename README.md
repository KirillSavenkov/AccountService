AccountService

Service provides possibility to increase or get amount for specified id.

Service works fast and stable under high loading.

Current release consists of 3 open-source applications:

- Server application. Is to be installed on server machine. Handles client's requests and operates with database.

- Client application. Is to be installed on client machine. Allows clients to send requests and get answers.

- Test client application. Optional. Is to be installed on client machine. Simulates group of clients sending requests to server at once. Allows to test server under high loading.

Folder ServerSource contains source of application for server.

Folder ClientSource contains source of application for client and test client.

Folder AccountService contains applications for server, client and test client.

Each folder inside AccountService contains executable JAR-file, README, configuration files and luancher bat-file.



_______________
Kirill Savenkov
ostrich@flyingsteps.org 
01.08.2014
