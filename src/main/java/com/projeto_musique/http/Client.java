package com.projeto_musique.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto_musique.models.exceptions.RequestException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Implementation of an http request sender.
 */
public class Client {

    /**
     * Default mapper by jackson.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Client method to send requests.
     *
     * @param uri               of the request
     * @param body              of the request
     * @param method            used by http client
     * @param responseModelType the type of response expected
     * @param <T>               generic for the response expected
     * @return if status code is less than 300, then a responseModelType instance will be returned
     * @throws RequestException if status code is bigger or equals than 300, an error message with the response will be triggered
     */
    public static <T> T send(String uri, Object body, Method method, Class<T> responseModelType) throws RequestException {
        HttpClient client = HttpClient.newHttpClient();

        try {
            String jsonBody = mapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .method(method.name(), HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 300)
                throw new RequestException("Status code: " + response.statusCode() + " " + response.body());

            return mapper.readValue(response.body(), responseModelType);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
