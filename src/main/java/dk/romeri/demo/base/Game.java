package dk.romeri.demo.base;

import dk.romeri.demo.manager.HTTPManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Game {
    private String token;
    private int[][] points;
    private int[] sums;

    public Game(String token, int[][] points) {
        this.token = token;
        this.points = points;
    }

    public String getToken() {
        return token;
    }

    public int[][] getPoints() {
        return points;
    }

    public void setSums(int[] sums) {
        this.sums = sums;
    }

    public int[] getSums() {
        return sums;
    }

    public int getPointsSize() {
        /*
            Method used for calculating the size of sums array
         */

        int acc = 0; // Accumulator

        for (int[] point : points) {
            if (point[0] != 0 || point[1] != 0) { // If one of them is not zero this is a real frame
                acc++;
            }
        }

        return acc;
    }

    public static Game fromJSON() throws ParseException {
        JSONObject json = (JSONObject) new JSONParser().parse(HTTPManager.get()); // Parse the string as JSON
        String token = (String) json.get("token"); // Get token
        int[][] points = new int[11][2]; // Create necessary array

        JSONArray pointsArray = (JSONArray) json.get("points"); // Get points array from JSON

        for(int i = 0; i < pointsArray.size(); i++) { // Iterate through each frame
            JSONArray frame = (JSONArray) pointsArray.get(i);

            points[i][0] = (int)(long) frame.get(0);
            points[i][1] = (int)(long) frame.get(1);
        }

        return new Game(token, points); // Return new Game
    }
}
