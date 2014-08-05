Account Service Client v1.0.

Application provides possibility to increase and get users' amounts using Account Service Server v1.0.

Application requires installed JRE and internet connection.

Set server address in Config.txt.

To run the application run the file RunClient.bat.

Command input format:
<type> <id> <value>
<type> is either add or get.
<id> is user identificator (maybe negative).
<value> is value to add to amount (maybe negative). In case of get command don't fill this field.
Examples:
add -12 32
get 65

Also provided special command drop making server to drop statistics log.

Known problems:

- 'java' is not recognized as an internal or external command.

You have incorrectly installed JRE or do not have it at all.

- Nothing happens after inputting command.

If it is possible to input, repeat your command. It has wrong format.
In the other case check your internet connection.



_______________
Kirill Savenkov
ostrich@flyingsteps.org