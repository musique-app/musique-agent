package com.projeto_musique.agent.core.connectivity;

import com.projeto_musique.agent.Properties;
import com.projeto_musique.agent.http.Client;
import com.projeto_musique.agent.http.Method;
import com.projeto_musique.agent.models.LoginRequest;
import com.projeto_musique.agent.models.LoginResult;
import com.projeto_musique.agent.models.exceptions.RequestException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * This class is responsible for the login process.
 */
@Slf4j
public class Login {

    /**
     * This method will use the API to get login into the app.
     *
     * @return LoginResult
     */
    public LoginResult request(String username, String password) throws RequestException {
        log.info("Logging username: {}", username);

        LoginRequest loginRequest = new LoginRequest(username, password);

        return Client.send(
                Properties.BASE_URL + "/api/auth",
                loginRequest,
                Collections.emptyMap(),
                Method.POST,
                LoginResult.class
        );
    }

}
