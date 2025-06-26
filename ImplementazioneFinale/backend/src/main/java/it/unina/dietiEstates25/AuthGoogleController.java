package it.unina.dietiEstates25;

import it.unina.dietiEstates25.db.DatabaseConfig;
import it.unina.dietiEstates25.dto.response.AgenteDTO;
import it.unina.dietiEstates25.service.ClienteService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static it.unina.dietiEstates25.AuthController.createJWT;

@Path("/auth")
public class AuthGoogleController {

    private static final String CLIENT_ID = "603934349943-q9t7jpn8i2hb0ivlgg0u1rco1nm6hji5.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = DatabaseConfig.getClientSecret();
    private static final String REDIRECT_URI = "http://localhost:9094/auth/exchange_token";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private final HttpClient client = HttpClient.newHttpClient();


    @GET
    @Path("/exchange_token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response exchangeToken(@QueryParam("code") String code) {
        try {
            String tokenResponse = fetchAccessToken(code);
            JsonNode tokenJson = new ObjectMapper().readTree(tokenResponse);
            String accessToken = tokenJson.get("access_token").asText();
            String userInfoResponse = fetchUserInfo(accessToken);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode userInfoJson = objectMapper.readTree(userInfoResponse);
            String googleId = userInfoJson.get("id").asText();
            String email = userInfoJson.get("email").asText();
            String token = createJWT(email, TimeUnit.DAYS.toMillis(365));
            AgenteDTO response=ClienteService.addCliente(email, googleId);
            if(response != null) {
                response.setToken(token);
            }

            return Response.status(Response.Status.OK).entity(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante l'autenticazione").build();
        }
    }

    private String fetchAccessToken(String code) {
        String params = "code=" + code +
                "&client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&redirect_uri=" + REDIRECT_URI +
                "&grant_type=authorization_code";

        return sendPostRequest(params);
    }

    private String fetchUserInfo(String accessToken){
        try {
            String url = USER_INFO_URL + "?access_token=" + accessToken;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }catch (IOException e){
            return null;
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return null;
        }
    }

    private String sendPostRequest(String params){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(AuthGoogleController.TOKEN_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(params, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }catch (IOException e) {
            return null;
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
