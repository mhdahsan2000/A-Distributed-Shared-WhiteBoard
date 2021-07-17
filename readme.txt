The principal objective of this project is to setup and implement a shared distributed white board using centralized client-server architecture. 
Allows multiple users to do multiple drawings on a shared white board with a GUI.

Using RMI protocol, Cleint-server Architecture (Centralized Architecture), and TCP-IP Connection.

Instructions to run
the project :
------------------------------------

There are 4 Jar files for this project which are : 
--------------------------------------------------

1. Server - Run server first as this needs to be setup to create a centralized system.
2. CreateWhiteBoard - Then run CreateWhiteBoard where the manager would create the first white board.(This is a Client)
3. JoinWhiteBoard - Other users can join the white board by running the JoinWhiteBoard jar file. (This is a Client)
4. PaintManager - And finally to get a multi-colored board run the Paint Manager. 

How to run the 4 JAR Files ? 

1. Open cmd on windows or terminal on Mac and change directory to your source folder. 
2. Run the server using following command (Generic format) :
	To run the server (ALWAYS USE PORT 8001 on the localhost) as the rmi registry would be created on port 8001 and the server would start at port 8002. 
	java -Djava.rmi.server.codebase=file:"Location to root folder" -jar Server.jar 127.0.0.1 8001
Eg:  java -Djava.rmi.server.codebase=file:"C:\Users\Admin\Desktop\Assignment2files" -jar Server.jar 127.0.0.1 8001

The server and client communication mostly happens with the help of RMI or remote method invocation. There is no need to start a new rmi registry(from commandline) as the script would automatically create one ! 
 
 3. Run CreateWhiteBoard and JoinWhiteBoard using the following commands: 
	i) To Create a whiteBoard (ALWAYS USE PORT 8002 on the localhost) as the rmi registry would be created on port 8001 and the server would start at port 8002.
        So the client is essentially getting connected to the port 8002. 
 
	java -Djava.rmi.server.codebase=file:"Location to root folder" -jar CreateWhiteBoard.jar 127.0.0.1 8002
	Eg:  java -Djava.rmi.server.codebase=file:"C:\Users\Admin\Desktop\Assignment2files" -jar CreateWhiteBoard.jar 127.0.0.1 8002


	ii) To Join a whiteBoard (ALWAYS USE THE PORT 8002), connect to port 8002.
	java -Djava.rmi.server.codebase=file:"Location to root folder" -jar JoinWhiteBoard.jar 127.0.0.1 8002
	Eg:  java -Djava.rmi.server.codebase=file:"C:\Users\Admin\Desktop\Assignment2files" -jar JoinWhiteBoard.jar 127.0.0.1 8002 

4. To run the Paint-Mananger (To get a multi colored board).
	java -Djava.rmi.server.codebase=file:"Location to root folder" -jar PaintManager.jar
	Eg:  java -Djava.rmi.server.codebase=file:"C:\Users\Admin\Desktop\Assignment2files" -jar PaintManager.jar
	
	
// The port numbers had to be fixed for creation of rmi registry (8001 ans 8002) although it can be changed within the source code. 
// To deal with concurrency the keyword Sychronised is used. 
