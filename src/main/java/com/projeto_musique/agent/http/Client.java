package com.projeto_musique.agent.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto_musique.agent.models.exceptions.RequestException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Implementation of an http request sender.
 */
@Slf4j
public class Client {

    /**
     * Default mapper by jackson.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Default http client by java.
     */
    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * Client method to send requests with body.
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
        if (log.isDebugEnabled())
            log.debug("Sending {} request to: {} with body: {}", method.name(), uri, body);

        try {
            String jsonBody = mapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .method(method.name(), HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            return sendRequest(request, responseModelType);
        } catch (IOException | InterruptedException e) {
            log.error("Error sending request", e);
            throw new RequestException(e.getMessage(), e);
        }
    }

    /**
     * Client method to send requests without body.
     *
     * @param uri               of the request
     * @param method            used by http client
     * @param responseModelType the type of response expected
     * @param <T>               generic for the response expected
     * @return if status code is less than 300, then a responseModelType instance will be returned
     * @throws RequestException if status code is bigger or equals than 300, an error message with the response will be triggered
     */
    public static <T> T send(String uri, Method method, Class<T> responseModelType) throws RequestException {
        if (log.isDebugEnabled())
            log.debug("Sending {} request to: {} without body", method.name(), uri);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .method(method.name(), HttpRequest.BodyPublishers.noBody())
                    .build();

            return sendRequest(request, responseModelType);
        } catch (IOException | InterruptedException e) {
            log.error("Error sending request", e);
            throw new RequestException(e.getMessage(), e);
        }
    }

    private static <T> T sendRequest(HttpRequest request, Class<T> responseModelType) throws IOException, InterruptedException, RequestException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (log.isDebugEnabled())
            log.debug("Response received with status: {} and body: {}", response.statusCode(), response.body());

        if (response.statusCode() >= 300)
            throw new RequestException("Status code: " + response.statusCode() + " " + response.body());

        return mapper.readValue(response.body(), responseModelType);
    }

}
