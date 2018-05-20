--create database
CREATE DATABASE ChesterandWill;

--SQL code for creating login table and inserting data into table

CREATE TABLE `login` (
  `userID` int(11) NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `login` VALUES (1,'cwilliams','password','Chester','Williams'),(2,'wluttmann','password2','Will','Luttmann');



--SQL code for creating schedule table and inserting data into table

CREATE TABLE `schedule` (
  `username` varchar(45) NOT NULL,
  `Monday` varchar(45) DEFAULT NULL,
  `Tuesday` varchar(45) DEFAULT NULL,
  `Wednesday` varchar(45) DEFAULT NULL,
  `Thursday` varchar(45) DEFAULT NULL,
  `Friday` varchar(45) DEFAULT NULL,
  `Saturday` varchar(45) DEFAULT NULL,
  `Sunday` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `schedule` VALUES ('cwilliams','8-6','8-5','8-4','8-3','8-5','OFF','OFF'),('wluttmann','8-11pm','8-11pm','8-11pm','8-11pm','8-11pm','OFF','OFF');


--*******************************************************************************
--**EDIT LINE 71 IN schedule_server.java TO REFLECT YOUR DATABASE AND LOGIN INFO
--**LOCATED IN SRC FOLDER
--*******************************************************************************


/* must include mysql-connector-java-5.1.41-bin.jar file to connect to Database
 **LOCATED IN MAIN PROJECT FOLDER
	In Netbeans IDE:
		-Right click project name
		-Go to properties
		-Select Libraries
		-Click Add JAR/Folder
		-Navigate to mysql-connector-java-5.1.41-bin.jar
		-Click OK
*/


/* INSTRUCTIONS FOR USE
Once JAR file is included and database is set up, run schedule_server.java to start the server. There is no GUI for the server, it runs in the background.

While the server is running. run Login.java to start the login.
	Valid logins are:
		cwilliams	password
		wluttmann	password2

Once correctly logged in, the login frame closes, opening the client gui.

From the client gui, click get schedule to display the schedule from the server.

When finished, click exit to close the frame and end communication to the server
*/
