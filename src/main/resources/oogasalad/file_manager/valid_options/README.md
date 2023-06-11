## Valid Options

_This directory contains the valid options for the Builder UI and their respective paramters._


[Valid Computer Actions](valid_computer_actions.json) contains the valid computer actions and their parameters. These 
are used in the Before or After Actions of stages of the Builder UI.

[Valid Conditions](valid_conditions.json) contains the valid conditions and their parameters. These are used to check 
that a phase has completed.

[Valid Player Actions](valid_player_actions.json) contains the valid player actions and their parameters. These are
used to allow a user to add rules that **can be played** by the player in a given phase.


*This directory connects to the Builder UI and is not the complete library of valid options, but it is 
the list of actions that users have permission to include in their game. 
To extend the UI to include a new action, add it to the appropriate file in this directory, and include [ParserClasses.properties](..%2FParserClasses.properties)
to be instantiated by the runner correctly at runtime.*