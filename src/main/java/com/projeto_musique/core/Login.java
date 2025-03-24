package com.projeto_musique.core;

import com.projeto_musique.http.Client;
import com.projeto_musique.http.Method;
import com.projeto_musique.models.LoginResult;
import com.projeto_musique.models.exceptions.RequestException;

/**
 * This class is responsible for the login process.
 */
public class Login {

    /**
     * This method will use the API to get login into the app.
     *
     * @return LoginResult
     */
    public LoginResult request(String username, String password) throws RequestException {
        LoginRequest loginRequest = new LoginRequest(username, password);

        return Client.send(
                "https://portal-musique-backend.herokuapp.com/api/auth",
                loginRequest,
                Method.POST,
                LoginResult.class
        );
    }

    private record LoginRequest(String username, String password) {

    }

}
