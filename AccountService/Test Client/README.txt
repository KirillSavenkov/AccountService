Account Service Test Client v1.0.


Application provides possibility to test Account Service v1.0 under high loading.


System requirements.

- Installed JRE 1.5 or higher.
- Console.

To check your JRE version input "java -version" in console.


Running the application.


To run the application run executable jar-file TestClient.jar using console.
Windows users can simply run bat-file RunTestClient.bat.

After running you should input a command.
Command format:
<type> <minId> <maxId> <repeat> <value (if necessary)>
<type> is either add or get.
<minId> is minimal id of user which is under operation.
<maxId> is maximal id of user which is under operation.
<repeat> is number of repeating of each operation.
<value> is value to add for add operations.
All (maxId - minId + 1) * repeat requests will be done concurrently.
minId must be not more than maxId, repeat must be at least 1.
Examples:
add 1 100 2 1. This command will add 1 to all accounts with id between 1 and 100 2 times.
get 1 3 1. This command will get accounts with id between 1 and 3.
add 1 1 100 1. This command will add 1 to account with id 1 100 times.



Test Client configurations.


Set configurations in the file "Test Client Config.txt". NB! Not "Config.txt".
Do not change structure of "Test Client Config.txt", do not add or remove any space symbols.
Minimal allowed id. Must be (not neccessary positive) integer. Default value is -11111111. Command with <minId> less than this parameter will not be allowed.
Maximal allowed id. Must be (not neccessary positive) integer. Default value is 11111111. Command with <maxId> greater than this parameter will not be allowed.
Maximal allowed "get" requests. Must be positive integer. Default value is 11111111. Command with total number of get-requests greater than this parameter will not be allowed.
Maximal allowed "add" requests. Must be positive integer. Default value is 11111111. Command with total number of add-requests greater than this parameter will not be allowed.
Server address. Must be nonempty. Default value is "localhost".


Known problems:

- 'java' is not recognized as an internal or external command.

You have incorrectly installed JRE or do not have it at all.



_______________
Kirill Savenkov
ostrich@flyingsteps.org
06.08.2014
