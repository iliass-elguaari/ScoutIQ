import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ScoutIQ {
    private static ArrayList<Player> players = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final LineupGenerator lineupGenerator = new LineupGenerator();
    private static final TeamAnalyzer teamAnalyzer = new TeamAnalyzer();
    private static final PlayerFileManager fileManager = new PlayerFileManager();

    public static void main(String[] args) {
        boolean running = true;

        printTitle();

        while (running) {
            showMenu();
            int choice = readInt("Choose an option: ");

            if (choice == 1) {
                addPlayer();
            } else if (choice == 2) {
                editPlayer();
            } else if (choice == 3) {
                removePlayer();
            } else if (choice == 4) {
                showPlayers();
            } else if (choice == 5) {
                searchPlayer();
            } else if (choice == 6) {
                rankPlayers();
            } else if (choice == 7) {
                comparePlayers();
            } else if (choice == 8) {
                generateLineup();
            } else if (choice == 9) {
                savePlayers();
            } else if (choice == 10) {
                loadPlayers();
            } else if (choice == 11) {
                loadSampleRoster();
            } else if (choice == 0) {
                running = false;
            } else {
                System.out.println("Invalid option.");
            }
        }

        System.out.println("ScoutIQ closed.");
        scanner.close();
    }

    private static void printTitle() {
        System.out.println("====================================");
        System.out.println("              SCOUTIQ");
        System.out.println("   Soccer Squad Analysis Engine");
        System.out.println("====================================");
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("1. Add player");
        System.out.println("2. Edit player");
        System.out.println("3. Remove player");
        System.out.println("4. Show full roster");
        System.out.println("5. Search player by name");
        System.out.println("6. Rank players for a position");
        System.out.println("7. Compare two players");
        System.out.println("8. Generate smart lineup");
        System.out.println("9. Save roster to CSV");
        System.out.println("10. Load roster from CSV");
        System.out.println("11. Load built-in sample roster");
        System.out.println("0. Exit");
    }

    private static void addPlayer() {
        Player player = readPlayerFromUser(null);
        players.add(player);
        System.out.println(player.getName() + " added.");
    }

    private static void editPlayer() {
        if (players.isEmpty()) {
            System.out.println("No players available.");
            return;
        }

        showPlayers();
        int index = readInt("Player number to edit: ") - 1;

        if (!validPlayerIndex(index)) {
            System.out.println("Invalid player number.");
            return;
        }

        Player oldPlayer = players.get(index);
        System.out.println("Re-enter the player's information.");
        Player updatedPlayer = readPlayerFromUser(oldPlayer.getName());
        players.set(index, updatedPlayer);
        System.out.println("Player updated.");
    }

    private static Player readPlayerFromUser(String defaultName) {
        String name;
        while (true) {
            if (defaultName == null) {
                System.out.print("Player name: ");
            } else {
                System.out.print("Player name (press Enter to keep " + defaultName + "): ");
            }

            name = scanner.nextLine().trim();
            if (name.isEmpty() && defaultName != null) {
                name = defaultName;
            }

            if (!name.isEmpty() && !name.contains(",")) {
                break;
            }
            System.out.println("Name cannot be empty or contain a comma.");
        }

        String primary = readPosition("Primary position");
        String secondary = readSecondaryPosition();
        int pace = readRating("Pace");
        int shooting = readRating("Shooting");
        int passing = readRating("Passing");
        int defending = readRating("Defending");
        int stamina = readRating("Stamina");
        int goalkeeping = readRating("Goalkeeping");

        return new Player(name, primary, secondary, pace, shooting,
                passing, defending, stamina, goalkeeping);
    }

    private static void removePlayer() {
        if (players.isEmpty()) {
            System.out.println("No players available.");
            return;
        }

        showPlayers();
        int index = readInt("Player number to remove: ") - 1;

        if (!validPlayerIndex(index)) {
            System.out.println("Invalid player number.");
            return;
        }

        Player removed = players.remove(index);
        System.out.println(removed.getName() + " removed.");
    }

    private static void showPlayers() {
        if (players.isEmpty()) {
            System.out.println("No players available.");
            return;
        }

        System.out.println();
        System.out.println("ROSTER (" + players.size() + " players)");
        System.out.println("----------------------------------------------");

        for (int i = 0; i < players.size(); i++) {
            System.out.println((i + 1) + ". " + players.get(i));
        }
    }

    private static void searchPlayer() {
        if (players.isEmpty()) {
            System.out.println("No players available.");
            return;
        }

        System.out.print("Search name: ");
        String search = scanner.nextLine().trim().toLowerCase();
        boolean found = false;

        for (Player player : players) {
            if (player.getName().toLowerCase().contains(search)) {
                System.out.println(player);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching player found.");
        }
    }

    private static void rankPlayers() {
        if (players.isEmpty()) {
            System.out.println("No players available.");
            return;
        }

        String position = readPosition("Position to rank");
        ArrayList<Player> ranked = lineupGenerator.rankPlayersForPosition(players, position);
        int limit = Math.min(10, ranked.size());

        System.out.println();
        System.out.println("TOP PLAYERS FOR " + position);
        System.out.println("-----------------------------");

        for (int i = 0; i < limit; i++) {
            Player player = ranked.get(i);
            System.out.println((i + 1) + ". " + player.getName()
                    + " - " + player.getSuitabilityScore(position) + "/100");
        }
    }

    private static void comparePlayers() {
        if (players.size() < 2) {
            System.out.println("You need at least two players.");
            return;
        }

        showPlayers();
        int firstIndex = readInt("First player number: ") - 1;
        int secondIndex = readInt("Second player number: ") - 1;

        if (!validPlayerIndex(firstIndex) || !validPlayerIndex(secondIndex)) {
            System.out.println("Invalid player number.");
            return;
        }

        String position = readPosition("Position to compare them for");
        Player first = players.get(firstIndex);
        Player second = players.get(secondIndex);
        int firstScore = first.getSuitabilityScore(position);
        int secondScore = second.getSuitabilityScore(position);

        System.out.println(first.getName() + ": " + firstScore + "/100");
        System.out.println(second.getName() + ": " + secondScore + "/100");

        if (firstScore > secondScore) {
            System.out.println(first.getName() + " is the stronger fit for " + position + ".");
        } else if (secondScore > firstScore) {
            System.out.println(second.getName() + " is the stronger fit for " + position + ".");
        } else {
            System.out.println("They have the same suitability score.");
        }
    }

    private static void generateLineup() {
        if (players.size() < 11) {
            System.out.println("You need at least 11 players to generate a full lineup.");
            return;
        }

        Formation formation = chooseFormation();
        if (formation == null) {
            System.out.println("Invalid formation choice.");
            return;
        }

        LinkedHashMap<String, Player> lineup = lineupGenerator.generateLineup(players, formation);

        System.out.println();
        System.out.println("SMART " + formation.getName() + " LINEUP");
        System.out.println("--------------------------------");

        for (Map.Entry<String, Player> entry : lineup.entrySet()) {
            String spot = entry.getKey();
            String role = formation.getRoleForSpot(spot);
            Player player = entry.getValue();

            if (player == null) {
                System.out.println(spot + ": No player available");
            } else {
                int score = player.getSuitabilityScore(role);
                System.out.println(spot + ": " + player.getName() + " (fit " + score + "/100)");
            }
        }

        printTeamAnalysis(lineup, formation);
        printBench(lineup);
    }

    private static void printTeamAnalysis(LinkedHashMap<String, Player> lineup, Formation formation) {
        System.out.println();
        System.out.println("TEAM ANALYSIS");
        System.out.println("-------------");
        System.out.println("Goalkeeping: " + teamAnalyzer.calculateGoalkeeping(lineup, formation) + "/100");
        System.out.println("Defense:     " + teamAnalyzer.calculateDefense(lineup, formation) + "/100");
        System.out.println("Midfield:    " + teamAnalyzer.calculateMidfield(lineup, formation) + "/100");
        System.out.println("Attack:      " + teamAnalyzer.calculateAttack(lineup, formation) + "/100");
        System.out.println("Overall fit: " + teamAnalyzer.calculateOverallFit(lineup, formation) + "/100");
    }

    private static void printBench(LinkedHashMap<String, Player> lineup) {
        ArrayList<Player> bench = lineupGenerator.recommendBench(players, lineup, 5);

        if (bench.isEmpty()) {
            return;
        }

        System.out.println();
        System.out.println("RECOMMENDED BENCH");
        System.out.println("-----------------");

        for (int i = 0; i < bench.size(); i++) {
            Player player = bench.get(i);
            System.out.println((i + 1) + ". " + player.getName()
                    + " (overall " + player.getOverallRating() + ")");
        }
    }

    private static Formation chooseFormation() {
        System.out.println("1. 4-3-3");
        System.out.println("2. 4-4-2");
        System.out.println("3. 4-2-3-1");
        int choice = readInt("Choose formation: ");
        return Formation.fromChoice(choice);
    }

    private static void savePlayers() {
        if (players.isEmpty()) {
            System.out.println("No players to save.");
            return;
        }

        System.out.print("File name (example: roster.csv): ");
        String fileName = scanner.nextLine().trim();

        if (fileManager.savePlayers(players, fileName)) {
            System.out.println("Saved " + players.size() + " players to " + fileName + ".");
        }
    }

    private static void loadPlayers() {
        System.out.print("File name to load: ");
        String fileName = scanner.nextLine().trim();
        ArrayList<Player> loaded = fileManager.loadPlayers(fileName);

        if (!loaded.isEmpty()) {
            players = loaded;
            System.out.println("Loaded " + players.size() + " players.");
        } else {
            System.out.println("No players were loaded.");
        }
    }

    private static void loadSampleRoster() {
        players.clear();

        players.add(new Player("Malik", "GK", "-", 45, 20, 66, 32, 78, 91));
        players.add(new Player("Theo", "LB", "LM", 84, 55, 76, 80, 88, 12));
        players.add(new Player("Sami", "CB", "CDM", 72, 42, 71, 90, 86, 10));
        players.add(new Player("Hugo", "CB", "-", 68, 39, 74, 88, 89, 9));
        players.add(new Player("Nico", "RB", "RM", 86, 52, 75, 79, 90, 11));
        players.add(new Player("Amir", "CM", "CDM", 77, 70, 88, 72, 91, 8));
        players.add(new Player("Leo", "CM", "CAM", 79, 74, 86, 68, 87, 7));
        players.add(new Player("Rayan", "CAM", "CM", 82, 83, 91, 48, 84, 6));
        players.add(new Player("Noah", "LW", "LM", 92, 84, 80, 40, 82, 5));
        players.add(new Player("Victor", "ST", "RW", 87, 93, 74, 35, 85, 5));
        players.add(new Player("Enzo", "RW", "RM", 91, 86, 82, 42, 83, 6));
        players.add(new Player("Jonas", "CB", "RB", 75, 45, 69, 84, 88, 8));
        players.add(new Player("Milan", "CM", "CAM", 81, 78, 84, 65, 89, 7));
        players.add(new Player("Iker", "ST", "LW", 84, 88, 77, 38, 90, 5));
        players.add(new Player("Luca", "LW", "CAM", 89, 80, 85, 44, 86, 6));
        players.add(new Player("Marco", "CDM", "CM", 74, 61, 82, 86, 92, 7));
        players.add(new Player("Dylan", "LM", "LW", 87, 76, 82, 58, 88, 6));
        players.add(new Player("Owen", "RM", "RW", 88, 79, 80, 55, 86, 6));

        System.out.println("Loaded 18 fictional sample players.");
    }

    private static String readPosition(String label) {
        while (true) {
            System.out.print(label + " (GK, LB, CB, RB, CDM, CM, CAM, LM, RM, LW, RW, ST): ");
            String position = scanner.nextLine().trim().toUpperCase();

            if (Player.isValidPosition(position)) {
                return position;
            }

            System.out.println("Unknown position.");
        }
    }

    private static String readSecondaryPosition() {
        while (true) {
            System.out.print("Secondary position (or - for none): ");
            String position = scanner.nextLine().trim().toUpperCase();

            if (position.equals("-") || Player.isValidPosition(position)) {
                return position;
            }

            System.out.println("Unknown position.");
        }
    }

    private static int readRating(String label) {
        while (true) {
            int rating = readInt(label + " (0-100): ");

            if (rating >= 0 && rating <= 100) {
                return rating;
            }

            System.out.println("Rating must be between 0 and 100.");
        }
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Enter a whole number.");
            }
        }
    }

    private static boolean validPlayerIndex(int index) {
        return index >= 0 && index < players.size();
    }
}
