package dk.romeri.demo.controller;

import dk.romeri.demo.base.Game;
import dk.romeri.demo.manager.HTTPManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class HomeController {
    private Game game;
    private boolean success = false;

    @GetMapping
    public String home(Model model) {
        getNewGameAndCalculate();
        model.addAttribute("game", game);
        return "index";
    }

    private void getNewGameAndCalculate() {
        try {
            System.out.println("Decoding JSON and creating Game object...");

            game = Game.fromJSON();

            System.out.println("Calculating sums...");

            int sumIndexPointer = 0;
            int[] sums = new int[Math.min(game.getPointsSize(), 10)];
            int[][] points = game.getPoints();

            System.out.println("Points recieved from SKAT: " + Arrays.deepToString(game.getPoints()));

            // Loop each frame
            for (int i = 0; i < sums.length; i++) {
                // Get sum from last frame
                int acc = i == 0 ? 0 : sums[sumIndexPointer-1];

                if(i == 9) { // If last frame with two additional rolls, do something different

                    if(points[i][0] == 10) {
                        acc += points[i][0] + points[i + 1][0] + points[i + 1][1];
                    } else {
                        acc += points[i][0] + points[i][1] + points[i + 1][0];
                    }

                    sums[sumIndexPointer] = acc;
                    break;

                } else if(points[i][0] + points[i][1] == 10 && points[i][0] != 10) { // Check for spare

                    acc += points[i][0] + points[i][1] + points[i + 1][0];

                } else if(points[i][0] == 10) { // Check for strike

                    acc += points[i][0] + points[i][1];

                   if(points[i + 1][0] == 10) { // Another strike
                       acc += points[i + 1][0] + points[i + 2][0];
                   } else {
                       acc += points[i + 1][0] + points[i + 1][1];
                   }

                } else { // If just a frame

                    acc += points[i][0] + points[i][1];

                }

                // Add points to sum
                sums[sumIndexPointer] = acc;

                // Increase pointer
                sumIndexPointer++;
            }

            // Sets the sum for the game
            game.setSums(sums);

            // Post results and get the response
            String response = HTTPManager.post(game);
            JSONObject json = (JSONObject) new JSONParser().parse(response);
            System.out.println("Success was: " + json.get("success"));

            // Set var to false or true based on success of right calculation
            success = (Boolean) json.get("success");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean getSuccess() {
        return success;
    }
}
