import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Player {
    private static final Set<String> VALID_POSITIONS = new HashSet<>(Arrays.asList(
            "GK", "LB", "CB", "RB", "CDM", "CM", "CAM", "LM", "RM", "LW", "RW", "ST"));

    private String name;
    private String primaryPosition;
    private String secondaryPosition;
    private int pace;
    private int shooting;
    private int passing;
    private int defending;
    private int stamina;
    private int goalkeeping;

    public Player(String name, String primaryPosition, String secondaryPosition,
                  int pace, int shooting, int passing,
                  int defending, int stamina, int goalkeeping) {
        this.name = name.trim();
        this.primaryPosition = normalizePosition(primaryPosition);
        this.secondaryPosition = normalizeSecondaryPosition(secondaryPosition);
        this.pace = clampRating(pace);
        this.shooting = clampRating(shooting);
        this.passing = clampRating(passing);
        this.defending = clampRating(defending);
        this.stamina = clampRating(stamina);
        this.goalkeeping = clampRating(goalkeeping);
    }

    private static int clampRating(int rating) {
        if (rating < 0) {
            return 0;
        }
        if (rating > 100) {
            return 100;
        }
        return rating;
    }

    private static String normalizePosition(String position) {
        if (position == null) {
            return "CM";
        }

        String normalized = position.trim().toUpperCase();
        if (!VALID_POSITIONS.contains(normalized)) {
            return "CM";
        }
        return normalized;
    }

    private static String normalizeSecondaryPosition(String position) {
        if (position == null || position.trim().equals("-") || position.trim().isEmpty()) {
            return "-";
        }

        String normalized = position.trim().toUpperCase();
        if (!VALID_POSITIONS.contains(normalized)) {
            return "-";
        }
        return normalized;
    }

    public static boolean isValidPosition(String position) {
        if (position == null) {
            return false;
        }
        return VALID_POSITIONS.contains(position.trim().toUpperCase());
    }

    public String getName() {
        return name;
    }

    public String getPrimaryPosition() {
        return primaryPosition;
    }

    public String getSecondaryPosition() {
        return secondaryPosition;
    }

    public int getPace() {
        return pace;
    }

    public int getShooting() {
        return shooting;
    }

    public int getPassing() {
        return passing;
    }

    public int getDefending() {
        return defending;
    }

    public int getStamina() {
        return stamina;
    }

    public int getGoalkeeping() {
        return goalkeeping;
    }

    public int getSuitabilityScore(String position) {
        String role = position.toUpperCase();
        int score;

        if (role.equals("GK")) {
            score = (goalkeeping * 75 + passing * 10 + stamina * 10 + pace * 5) / 100;
        } else if (role.equals("CB")) {
            score = (defending * 50 + stamina * 20 + pace * 15 + passing * 15) / 100;
        } else if (role.equals("LB") || role.equals("RB")) {
            score = (defending * 30 + pace * 25 + stamina * 25 + passing * 20) / 100;
        } else if (role.equals("CDM")) {
            score = (defending * 30 + passing * 30 + stamina * 25 + pace * 10 + shooting * 5) / 100;
        } else if (role.equals("CM")) {
            score = (passing * 35 + stamina * 25 + defending * 15 + pace * 15 + shooting * 10) / 100;
        } else if (role.equals("CAM")) {
            score = (passing * 35 + shooting * 30 + pace * 20 + stamina * 15) / 100;
        } else if (role.equals("LM") || role.equals("RM") || role.equals("LW") || role.equals("RW")) {
            score = (pace * 35 + passing * 25 + shooting * 25 + stamina * 15) / 100;
        } else if (role.equals("ST")) {
            score = (shooting * 45 + pace * 25 + stamina * 20 + passing * 10) / 100;
        } else {
            return 0;
        }

        if (primaryPosition.equals(role)) {
            score += 7;
        } else if (secondaryPosition.equals(role)) {
            score += 3;
        }

        if (score > 100) {
            score = 100;
        }

        return score;
    }

    public int getOverallRating() {
        if (primaryPosition.equals("GK")) {
            return getSuitabilityScore("GK");
        }
        return (pace + shooting + passing + defending + stamina) / 5;
    }

    public String toCsv() {
        return name + "," + primaryPosition + "," + secondaryPosition + ","
                + pace + "," + shooting + "," + passing + ","
                + defending + "," + stamina + "," + goalkeeping;
    }

    @Override
    public String toString() {
        return name
                + " | " + primaryPosition + "/" + secondaryPosition
                + " | PAC " + pace
                + " | SHO " + shooting
                + " | PAS " + passing
                + " | DEF " + defending
                + " | STA " + stamina
                + " | GK " + goalkeeping;
    }
}
