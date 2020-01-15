package dk.romeri.demo.manager;

import dk.romeri.demo.base.Game;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HTTPManager {
    private static final String ENDPOINT = "http://13.74.31.101/api/points";
    private static RestTemplate rest = new RestTemplate();
    private static HttpHeaders headers = new HttpHeaders();

    public static HttpStatus statusCode;

    public static String get() {
        System.out.println("Requesting new scores from SKAT...");
        HttpEntity<String> request = new HttpEntity<>("", headers);
        ResponseEntity<String> response = rest.exchange(ENDPOINT, HttpMethod.GET, request, String.class);
        statusCode = response.getStatusCode();
        System.out.println("Request was done successfully, status code is: " + statusCode);
        return response.getBody();
    }

    public static String post(Game game) {
        System.out.println("Posting sums to SKAT...");

        Map<String, String> params = new HashMap<>();
        params.put("token", game.getToken());
        params.put("points", Arrays.toString(game.getSums()));

        System.out.println("Token for the game is: "+game.getToken());
        System.out.println("Points: "+ Arrays.toString(game.getSums()));

        HttpEntity<?> post = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rest.exchange(ENDPOINT, HttpMethod.POST, post, String.class);
        statusCode = response.getStatusCode();
        System.out.println("Post was done successfully, status code is: " + statusCode);
        return response.getBody();
    }
}
