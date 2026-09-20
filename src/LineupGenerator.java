import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LineupGenerator {

    public LinkedHashMap<String, Player> generateLineup(ArrayList<Player> players, Formation formation) {
        LinkedHashMap<String, Player> lineup = new LinkedHashMap<>();
        Set<Player> usedPlayers = new HashSet<>();

        for (Map.Entry<String, String> entry : formation.getSpots().entrySet()) {
            String lineupSpot = entry.getKey();
            String role = entry.getValue();
            Player best = findBestAvailable(players, usedPlayers, role);
            lineup.put(lineupSpot, best);

            if (best != null) {
                usedPlayers.add(best);
            }
        }

        return lineup;
    }

    public Player findBestAvailable(ArrayList<Player> players, Set<Player> usedPlayers, String position) {
        Player bestPlayer = null;
        int bestScore = -1;

        for (Player player : players) {
            if (!usedPlayers.contains(player)) {
                int score = player.getSuitabilityScore(position);

                if (score > bestScore) {
                    bestScore = score;
                    bestPlayer = player;
                }
            }
        }

        return bestPlayer;
    }

    public Player findBestForPosition(ArrayList<Player> players, String position) {
        Set<Player> nobodyUsed = new HashSet<>();
        return findBestAvailable(players, nobodyUsed, position);
    }

    public ArrayList<Player> rankPlayersForPosition(ArrayList<Player> players, final String position) {
        ArrayList<Player> ranked = new ArrayList<>(players);

        ranked.sort(new Comparator<Player>() {
            @Override
            public int compare(Player first, Player second) {
                int firstScore = first.getSuitabilityScore(position);
                int secondScore = second.getSuitabilityScore(position);
                return Integer.compare(secondScore, firstScore);
            }
        });

        return ranked;
    }

    public ArrayList<Player> recommendBench(ArrayList<Player> players,
                                             LinkedHashMap<String, Player> lineup,
                                             int benchSize) {
        Set<Player> used = new HashSet<>();
        for (Player player : lineup.values()) {
            if (player != null) {
                used.add(player);
            }
        }

        ArrayList<Player> available = new ArrayList<>();
        for (Player player : players) {
            if (!used.contains(player)) {
                available.add(player);
            }
        }

        available.sort(new Comparator<Player>() {
            @Override
            public int compare(Player first, Player second) {
                return Integer.compare(second.getOverallRating(), first.getOverallRating());
            }
        });

        ArrayList<Player> bench = new ArrayList<>();
        int limit = Math.min(benchSize, available.size());
        for (int i = 0; i < limit; i++) {
            bench.add(available.get(i));
        }

        return bench;
    }
}
