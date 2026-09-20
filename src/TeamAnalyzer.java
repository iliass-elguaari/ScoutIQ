import java.util.LinkedHashMap;
import java.util.Map;

public class TeamAnalyzer {

    public int calculateOverallFit(LinkedHashMap<String, Player> lineup, Formation formation) {
        int total = 0;
        int count = 0;

        for (Map.Entry<String, Player> entry : lineup.entrySet()) {
            Player player = entry.getValue();
            if (player != null) {
                String role = formation.getRoleForSpot(entry.getKey());
                total += player.getSuitabilityScore(role);
                count++;
            }
        }

        if (count == 0) {
            return 0;
        }
        return total / count;
    }

    public int calculateDefense(LinkedHashMap<String, Player> lineup, Formation formation) {
        return averageByRoles(lineup, formation, new String[] {"LB", "CB", "RB"});
    }

    public int calculateMidfield(LinkedHashMap<String, Player> lineup, Formation formation) {
        return averageByRoles(lineup, formation, new String[] {"CDM", "CM", "CAM", "LM", "RM"});
    }

    public int calculateAttack(LinkedHashMap<String, Player> lineup, Formation formation) {
        return averageByRoles(lineup, formation, new String[] {"LW", "RW", "ST"});
    }

    public int calculateGoalkeeping(LinkedHashMap<String, Player> lineup, Formation formation) {
        return averageByRoles(lineup, formation, new String[] {"GK"});
    }

    private int averageByRoles(LinkedHashMap<String, Player> lineup,
                               Formation formation,
                               String[] roles) {
        int total = 0;
        int count = 0;

        for (Map.Entry<String, Player> entry : lineup.entrySet()) {
            String role = formation.getRoleForSpot(entry.getKey());
            if (containsRole(roles, role) && entry.getValue() != null) {
                total += entry.getValue().getSuitabilityScore(role);
                count++;
            }
        }

        if (count == 0) {
            return 0;
        }
        return total / count;
    }

    private boolean containsRole(String[] roles, String role) {
        for (String allowed : roles) {
            if (allowed.equals(role)) {
                return true;
            }
        }
        return false;
    }
}
