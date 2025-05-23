package com.projeto_musique.agent.models.exceptions;

/**
 * If a response from a request isn't between 200 and 300 status codes, this exception will be triggered.
 */
public class RequestException extends Exception {

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
