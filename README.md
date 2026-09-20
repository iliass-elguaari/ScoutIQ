# ScoutIQ

ScoutIQ is a Java soccer squad analysis engine that ranks players by positional fit, compares players, generates lineups, analyzes team balance, recommends a bench, and saves/loads rosters from CSV files.

The project was designed to be understandable without external frameworks while still demonstrating object-oriented programming, collections, algorithms, file I/O, sorting, data validation, and testing.

## Main Features

- Add, edit, remove, search, and display players
- Primary and secondary player positions
- Position-specific suitability scoring from 0 to 100
- Rank the top players for any supported position
- Compare two players for a chosen role
- Generate a smart lineup without reusing players
- Supported formations:
  - 4-3-3
  - 4-4-2
  - 4-2-3-1
- Team ratings for goalkeeping, defense, midfield, attack, and overall fit
- Recommended bench from unused players
- Save and load CSV rosters
- Built-in fictional sample roster
- Simple automated test suite

## Why the Project Is Interesting

ScoutIQ is more than a player database. The main logic is a role-based scoring system. A striker is judged differently from a center-back, and a goalkeeper is judged differently from both.

For example, the striker score values shooting more heavily, while a center-back score values defending and stamina more heavily. Players also receive a small bonus when the requested role matches their primary or secondary position.

The lineup generator then walks through the spots in a formation and selects the highest-scoring unused player for each role. A HashSet is used to make sure the same player cannot be selected twice.

This is a greedy lineup algorithm. It is intentionally simple enough to explain clearly: it makes the best available choice for each position in order. It does not claim to mathematically guarantee the global best possible lineup.

## Project Structure

```text
ScoutIQ_Final/
├── src/
│   ├── Player.java
│   ├── Formation.java
│   ├── LineupGenerator.java
│   ├── TeamAnalyzer.java
│   ├── PlayerFileManager.java
│   ├── ScoutIQ.java
│   └── ScoutIQTests.java
├── data/
│   └── sample_players.csv
├── run.sh
├── test.sh
├── .gitignore
└── README.md
```

## What Each Class Does

### Player
Stores one player's data and calculates how suitable the player is for a requested position.

### Formation
Stores the positions required by a formation. LinkedHashMap is used so the lineup prints in a predictable order.

### LineupGenerator
Ranks players, finds the best available player for a role, builds a lineup, and recommends unused players for the bench.

### TeamAnalyzer
Calculates goalkeeping, defense, midfield, attack, and overall lineup scores.

### PlayerFileManager
Handles saving and loading player data using CSV files.

### ScoutIQ
Contains the console menu and connects all of the other classes together.

### ScoutIQTests
Runs small automated checks for scoring, lineup uniqueness, ranking order, and team rating ranges.

## How to Run on macOS / Linux

From the project folder:

```bash
./run.sh
```

Or manually:

```bash
mkdir -p out
javac -d out src/*.java
java -cp out ScoutIQ
```

## Run the Tests

```bash
./test.sh
```

## Example Flow

1. Run the program.
2. Choose `11` to load the built-in sample roster.
3. Choose `6` to rank players for a position.
4. Choose `8` to generate a lineup.
5. Select a formation.
6. ScoutIQ prints the lineup, team ratings, and recommended bench.

## Concepts Demonstrated

- Classes and objects
- Constructors
- Encapsulation with private fields
- Getters
- Static methods and fields
- ArrayList
- HashSet
- LinkedHashMap
- Loops and conditionals
- Sorting with Comparator
- File reading and writing
- Exception handling
- Input validation
- Simple automated testing
- Greedy selection algorithm

## How to Explain It in an Interview

A short explanation:

> I built ScoutIQ in Java to analyze soccer players and automatically generate lineups. Each player has attributes such as pace, shooting, passing, defending, stamina, and goalkeeping. I created a position-specific scoring algorithm, so the importance of each attribute changes depending on the role. The lineup generator uses a greedy algorithm to pick the best available unused player for each formation spot. I used ArrayList for the roster, HashSet to prevent duplicate selections, LinkedHashMap to keep formation order, CSV file I/O for persistence, and a small test suite to verify the main logic.

If asked why HashSet is used:

> I need to quickly know whether a player has already been selected. HashSet is a good fit because membership checks are fast on average.

If asked why LinkedHashMap is used:

> I wanted a mapping from lineup spot to player, but I also wanted the positions to print in the same order they were inserted.

If asked about the algorithm:

> The generator uses a greedy strategy. For each position in the formation, it scans the available players, calculates their suitability score for that role, and picks the highest-scoring player who has not already been used.

If asked what you would improve later:

> I would replace the greedy lineup selection with a global assignment optimization algorithm, add real match data, and build a graphical or web interface.

## Resume Bullet

**ScoutIQ — Java Soccer Squad Analysis Engine**  
Built a Java-based soccer analytics application that ranks players using position-specific weighted scoring, generates lineups across multiple formations with duplicate prevention, analyzes team balance, recommends substitutes, and supports CSV persistence; implemented with OOP, ArrayList, HashSet, LinkedHashMap, sorting, file I/O, validation, and automated tests.
