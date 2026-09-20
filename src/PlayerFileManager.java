import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PlayerFileManager {

    private static final String HEADER =
            "name,primaryPosition,secondaryPosition,pace,shooting,passing,defending,stamina,goalkeeping";

    public boolean savePlayers(ArrayList<Player> players, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(HEADER);

            for (Player player : players) {
                writer.println(player.toCsv());
            }

            return true;
        } catch (IOException e) {
            System.out.println("Could not save players: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Player> loadPlayers(String fileName) {
        ArrayList<Player> players = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1 && line.toLowerCase().startsWith("name,")) {
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length != 9) {
                    System.out.println("Skipped invalid CSV line " + lineNumber + ".");
                    continue;
                }

                try {
                    String name = parts[0].trim();
                    String primaryPosition = parts[1].trim();
                    String secondaryPosition = parts[2].trim();
                    int pace = Integer.parseInt(parts[3].trim());
                    int shooting = Integer.parseInt(parts[4].trim());
                    int passing = Integer.parseInt(parts[5].trim());
                    int defending = Integer.parseInt(parts[6].trim());
                    int stamina = Integer.parseInt(parts[7].trim());
                    int goalkeeping = Integer.parseInt(parts[8].trim());

                    players.add(new Player(name, primaryPosition, secondaryPosition,
                            pace, shooting, passing, defending, stamina, goalkeeping));
                } catch (NumberFormatException e) {
                    System.out.println("Skipped line " + lineNumber + " because a rating was invalid.");
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load players: " + e.getMessage());
        }

        return players;
    }
}
