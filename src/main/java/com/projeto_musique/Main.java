package com.projeto_musique;

import com.projeto_musique.core.Engine;
import com.projeto_musique.models.exceptions.ConnectionException;

/**
 * Main class for the agent of Projeto Musique.
 * It calls the bootstrap and gets the engine of the agent.
 * It then starts the engine.
 * <p>
 * Any problem while the app is booting or running will end up here.
 */
public class Main {

    public static void main(String[] args) {
        try {
            Engine engine = Bootstrap.setup();
            engine.run();
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }

}