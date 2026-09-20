import java.util.LinkedHashMap;
import java.util.Map;

public class Formation {
    private String name;
    private LinkedHashMap<String, String> spots;

    public Formation(String name, LinkedHashMap<String, String> spots) {
        this.name = name;
        this.spots = spots;
    }

    public String getName() {
        return name;
    }

    public LinkedHashMap<String, String> getSpots() {
        return spots;
    }

    public static Formation create433() {
        LinkedHashMap<String, String> spots = new LinkedHashMap<>();
        spots.put("GK", "GK");
        spots.put("LB", "LB");
        spots.put("CB1", "CB");
        spots.put("CB2", "CB");
        spots.put("RB", "RB");
        spots.put("CM1", "CM");
        spots.put("CM2", "CM");
        spots.put("CAM", "CAM");
        spots.put("LW", "LW");
        spots.put("ST", "ST");
        spots.put("RW", "RW");
        return new Formation("4-3-3", spots);
    }

    public static Formation create442() {
        LinkedHashMap<String, String> spots = new LinkedHashMap<>();
        spots.put("GK", "GK");
        spots.put("LB", "LB");
        spots.put("CB1", "CB");
        spots.put("CB2", "CB");
        spots.put("RB", "RB");
        spots.put("LM", "LM");
        spots.put("CM1", "CM");
        spots.put("CM2", "CM");
        spots.put("RM", "RM");
        spots.put("ST1", "ST");
        spots.put("ST2", "ST");
        return new Formation("4-4-2", spots);
    }

    public static Formation create4231() {
        LinkedHashMap<String, String> spots = new LinkedHashMap<>();
        spots.put("GK", "GK");
        spots.put("LB", "LB");
        spots.put("CB1", "CB");
        spots.put("CB2", "CB");
        spots.put("RB", "RB");
        spots.put("CDM1", "CDM");
        spots.put("CDM2", "CDM");
        spots.put("LW", "LW");
        spots.put("CAM", "CAM");
        spots.put("RW", "RW");
        spots.put("ST", "ST");
        return new Formation("4-2-3-1", spots);
    }

    public static Formation fromChoice(int choice) {
        if (choice == 1) {
            return create433();
        }
        if (choice == 2) {
            return create442();
        }
        if (choice == 3) {
            return create4231();
        }
        return null;
    }

    public String getRoleForSpot(String spot) {
        return spots.get(spot);
    }

    public void printShape() {
        System.out.println("Formation: " + name);
        for (Map.Entry<String, String> entry : spots.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
