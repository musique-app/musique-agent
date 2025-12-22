package com.projeto_musique.agent.core.connectivity;

import com.projeto_musique.agent.http.Client;
import com.projeto_musique.agent.http.Method;
import com.projeto_musique.agent.models.CheckConnectionResponse;
import com.projeto_musique.agent.models.exceptions.RequestException;
import lombok.extern.slf4j.Slf4j;

/**
 * Check if connection is available.
 */
@Slf4j
public class ConnectionChecker {

    /**
     * Method to check connection.
     *
     * @return true if connection is available and false is server doesn't allow connection
     * @throws RequestException if connection is not available
     */
    public boolean check() throws RequestException {
        if (log.isDebugEnabled())
            log.debug("Checking connection to server...");

        CheckConnectionResponse response = Client.send("https://portal-musique-backend.herokuapp.com/api/auth", Method.HEAD, CheckConnectionResponse.class);
        return response.success();
    }

}
