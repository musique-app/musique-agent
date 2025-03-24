package com.projeto_musique.models.exceptions;

/**
 * Any connection problem will trigger this exception.
 */
public class ConnectionException extends Exception {

    public ConnectionException(String message) {
        super(message);
    }

}
