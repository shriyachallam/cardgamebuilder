# custom card game builder and runner

## running the program

* The main class allows you to login/sign up to start building and playing card games. The user account is aunthenticated through firebase. A member is allowed to create new games and a non-member can only play games. 

## to build games

* Every game is structured as the following stages: Game -> Round -> Turn -> Phase. Each stage has a before and after section which are comprised of computer actions that occur before or after the stage body. Each game is comprised of a list of rounds and the computer actions occur before and after all the rounds, etc. 

## to play games

* To play games, a group of players must all use the same computer. Add the players names that align with their username. 