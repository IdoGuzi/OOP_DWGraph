# Pokemon Game

This game is a module in our OOP course at Ariel university writes in JAVA.
The object of the game is to earn as many points as possible while eating Pokemon using smart agents.

In fact the arena is a directed weighted graph
## Description

Our implementation communicates with the university server. We get different stages from the server where each stage includes a different arena, number of Pokemons, and the amount of agents allowed. Our main goal is to earn as many points as possible when there are some important variables that determine the score. For example, the amount of calls to the server, the number of Pokemons we ate, the points each Pokemon is worth, and so on. The game ends after a pre-determined time which is also received from the server, hopefully we will earn as many points as possible at the given time.

## Installation and Running

Use the following class to run the game 


```bash
EX2.java
```
This class will run a GUI as well.

An other good option is to run the  
```bash
EX2.jar
```
This file will run a GUI as well.


## Usage
The game is reset to level 0. To change the level you must enter the following line at EX2:

```bash
 Game g = new Game(0);     //change 0 to the wanted level - there is 23 levels
```

## Contributing & Support
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change, we are open to here your ideas. The improvements that can be made are in an algorithm that determines to the agent which Pokemon should be reached and improve run times. 
An interesting improvement is to treat the problem as TSP problem and use heuristics algorithems.
For the brave ones, perhaps try and prove that P = NP and earn yourself a 1,000,000$ Price
## License
free  --> note: we use AWS as a server from the university account to get the stages.
