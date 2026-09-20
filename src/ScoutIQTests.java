import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class ScoutIQTests {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testPrimaryPositionBonus();
        testLineupHasElevenUniquePlayers();
        testRankingOrder();
        testOverallFitRange();

        System.out.println();
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testPrimaryPositionBonus() {
        Player player = new Player("Test", "ST", "RW", 80, 90, 70, 40, 80, 5);
        int strikerScore = player.getSuitabilityScore("ST");
        check(strikerScore > 0 && strikerScore <= 100, "Primary-position suitability score stays in range");
    }

    private static void testLineupHasElevenUniquePlayers() {
        ArrayList<Player> players = makeRoster();
        LineupGenerator generator = new LineupGenerator();
        Formation formation = Formation.create433();
        LinkedHashMap<String, Player> lineup = generator.generateLineup(players, formation);

        Set<Player> unique = new HashSet<>(lineup.values());
        unique.remove(null);

        check(lineup.size() == 11, "Generated lineup contains 11 spots");
        check(unique.size() == 11, "Generated lineup does not reuse a player");
    }

    private static void testRankingOrder() {
        ArrayList<Player> players = makeRoster();
        LineupGenerator generator = new LineupGenerator();
        ArrayList<Player> ranked = generator.rankPlayersForPosition(players, "ST");

        boolean ordered = true;
        for (int i = 0; i < ranked.size() - 1; i++) {
            if (ranked.get(i).getSuitabilityScore("ST") < ranked.get(i + 1).getSuitabilityScore("ST")) {
                ordered = false;
            }
        }

        check(ordered, "Player ranking is sorted from highest to lowest fit");
    }

    private static void testOverallFitRange() {
        ArrayList<Player> players = makeRoster();
        Formation formation = Formation.create442();
        LineupGenerator generator = new LineupGenerator();
        TeamAnalyzer analyzer = new TeamAnalyzer();
        LinkedHashMap<String, Player> lineup = generator.generateLineup(players, formation);
        int fit = analyzer.calculateOverallFit(lineup, formation);

        check(fit >= 0 && fit <= 100, "Team overall fit stays in the 0-100 range");
    }

    private static ArrayList<Player> makeRoster() {
        ArrayList<Player> roster = new ArrayList<>();
        roster.add(new Player("P1", "GK", "-", 40, 20, 60, 30, 75, 90));
        roster.add(new Player("P2", "LB", "LM", 85, 55, 75, 80, 88, 5));
        roster.add(new Player("P3", "CB", "CDM", 70, 40, 72, 90, 85, 5));
        roster.add(new Player("P4", "CB", "-", 68, 38, 70, 88, 86, 5));
        roster.add(new Player("P5", "RB", "RM", 86, 52, 75, 78, 89, 5));
        roster.add(new Player("P6", "CM", "CDM", 78, 70, 88, 70, 90, 5));
        roster.add(new Player("P7", "CM", "CAM", 80, 75, 86, 65, 88, 5));
        roster.add(new Player("P8", "CAM", "CM", 82, 84, 91, 45, 84, 5));
        roster.add(new Player("P9", "LW", "LM", 92, 84, 80, 40, 82, 5));
        roster.add(new Player("P10", "ST", "RW", 88, 93, 74, 35, 85, 5));
        roster.add(new Player("P11", "RW", "RM", 91, 86, 82, 42, 83, 5));
        roster.add(new Player("P12", "CDM", "CM", 74, 60, 83, 87, 92, 5));
        roster.add(new Player("P13", "LM", "LW", 87, 76, 82, 58, 88, 5));
        roster.add(new Player("P14", "RM", "RW", 88, 79, 80, 55, 86, 5));
        return roster;
    }

    private static void check(boolean condition, String name) {
        if (condition) {
            System.out.println("PASS - " + name);
            passed++;
        } else {
            System.out.println("FAIL - " + name);
            failed++;
        }
    }
}
