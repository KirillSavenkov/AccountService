Account Service Test Client v1.0.

Application provides possibility to test Account Service v1.0 under high loading.

Application requires installed JRE and internet connection.

Configure the application in Test Client Config.txt (NOTE! Not Config.txt).

To run the application run the file RunTestClient.bat.

Command input format:
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

Known problems:

- 'java' is not recognized as an internal or external command.

You have incorrectly installed JRE or do not have it at all.



_______________
Kirill Savenkov
ostrich@flyingsteps.org