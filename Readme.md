# Eriantys Board Game 
## Software engineering project a.y. 2021/22

Group: AM31

Professor : Alessandro Margara

Final Score : 30 cum laude

## Implemented functions: 

| Functionality |     Status     |
|:-----------------------|:--------------:|
| Basic rules |     🟢     |
| Complete rules |     🟢      |
| Socket | 🟢 |
| GUI | 🟢 |
| CLI |     🟢     |
| Multiple games | 🟢 |
| 4 players | 🟢 |
| Persistence | 🟢 |

key :   
🟢 Completed     
🟡 Work in progress  
🔴 Not implemented

## Tests coverage

The global class coverage is 100%.

| Package | Class | Method Coverage        | Line Coverage |
|:-----------------------|:--------------:|:-----------------------|:--------------:|
| model | global | 95% (139/145)          | 90% (611/676) |
| controller | ActionController | 96% (25/26)            | 80% (262/326) |
| controller | ActionParser | 92% (12/13)            | 90% (90/99)  |
| controller | GameHandler | 94% (16/17)            | 90% (64/71) |
| controller | TurnController | 90% (10/11)            | 91% (75/82)|

note: the method missing in controller tests is always the addPropertyChangeListener.

## Running

Open a terminal and go to the project target directory (which has to be previously built with maven). Once there, execute this command:

```
java --enable-preview -jar AM31-1.0-SNAPSHOT-jar-with-dependencies.jar
```
You'll have to choose if you want to launch server, CLI client or GUI client by typing the option number on your keyboard.

In order to play, you'll have to launch at least one server and two clients (either CLI or GUI).

## Team:
* [Federica Di Filippo](https://github.com/FedericaDiFilippo)
* [Carmine Faino](https://github.com/CarmineFaino)
* [Christian Di Diego](https://github.com/ChristianDiDiego)

## Software used:

**AstahUML** - UML diagrams

**Intellij IDEA Ultimate** - main IDE 