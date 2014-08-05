Account Service Client v1.0.


Application provides possibility to increase and get users' amounts using Account Service Server v1.0.


System requirements.

- Installed JRE 1.5 or higher.
- Console.

To check your JRE version input "java -version" in console.


Running the application.


To run the application run executable jar-file Client.jar using console.
Windows users can simply run bat-file RunClient.bat.

After running you should input a command.
Command format:
<type> <id> <value>
<type> is either add or get.
<id> is user identificator (maybe negative).
<value> is value to add to amount (maybe negative). In case of get command don't fill this field.
Examples:
add -12 32
get 65

Also provided special command "drop" making server to drop statistics log.


Client configurations.


Set configurations in the file "Config.txt".
Do not change structure of Config.txt, do not add or remove any space symbols.
Server address. Must be nonempty. Default value is "localhost". Default value is used for running both server and client at the same local machine.


Known problems:


- 'java' is not recognized as an internal or external command.

You have incorrectly installed JRE or do not have it at all.

- Nothing happens after inputting command.

If it is possible to input, repeat your command. It has wrong format.
In the other case check your internet connection.



_______________
Kirill Savenkov
ostrich@flyingsteps.org
06.08.2014
