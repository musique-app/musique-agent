package com.projeto_musique.core;

import com.projeto_musique.http.Client;
import com.projeto_musique.http.Method;
import com.projeto_musique.models.CheckConnectionResponse;
import com.projeto_musique.models.exceptions.RequestException;

/**
 * Check if connection is available.
 */
public class ConnectionChecker {

    /**
     * Method to check connection.
     *
     * @return true if connection is available and false is server doesn't allow connection
     * @throws RequestException if connection is not available
     */
    public boolean check() throws RequestException {
        CheckConnectionResponse response = Client.send("https://portal-musique-backend.herokuapp.com/api/auth", Method.HEAD, CheckConnectionResponse.class);
        return response.success();
    }

}
