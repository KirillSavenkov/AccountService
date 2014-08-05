Account Service.



CONFIGURATION FILE



Configuration file "Config.txt" can be found in root directory. Do not change structure of this file, do not add any space chars (' ').



DATABASE CONFIGURATION



Database username. Default username is: "root". Username can not be empty.
Database password. Default value is: "". Password can be empty.
Database url. Default value is: "localhost".
Database name. Default value is: "userAmounts". There must not be database with same name.

Default parameters of database configuration allow working on local machine in case you have administrator rights.



SERVER CONFIGURATION



Number of serving threads. Default is 10. It is recommended to set this parameter from 1 to 200. Values under 1 are not allowed, upper bound is discussed below.

It is recommended to set this value close to server's number of computing cores. The optimal value is number of computing cores -1. Lower values will not allow server to use all computing power, higher values will make server work slower, extremely high values may cause errors. Lower optimal value may be caused by other running applications on server.