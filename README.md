## Project Name & Description

This is a recreation of the Pacman video game implemented using JavaFX. The user can control Pacman and eat the dots while being chased by a series of ghosts. 

The ghosts all chase Pacman using a modified version of breadth-first search. Pacman has three lives to try to to eat all of the dots before the game ends. A scorecounter keeps track of how many dots have been eaten by Pacman. 

Green "energizer" dots can be eaten by Pacman to enable Pacman to eat the ghosts. The ghosts stop executing breadth-first search and move randomly across the board. 


## Project Status

This project is completed

## Project Screen Shot(s)

#### Example:   

![ScreenShot](https://github.com/singhru27/Pacman/blob/main/screenshots/Default.png?raw=true)


## Installation and Setup Instructions

To run the program, you first need to make sure that Java runtime environment has been installed. Once you confirm this, go to the 

```
targets
```
directory and run the following JAR file (which has all dependencies included)
```
Pacman-1.0.0-SNAPSHOT-jar-with-dependencies
```

If you want to make changes to the project, you can view the source code in the 
```
src
```
directory. This project has been Mavenized. Make sure you have Maven set up on your computer. Then, move the 
```
cs0150-1.0.0.jar
```
to your local Maven repository before building (and make sure to update the POM file accordingly) if looking to make changes. 



