package com.projeto_musique;

import com.projeto_musique.core.Engine;
import lombok.extern.slf4j.Slf4j;

/**
 * Main class for the agent of Projeto Musique.
 * It calls the bootstrap and gets the engine of the agent.
 * It then starts the engine.
 * <p>
 * Any problem while the app is booting or running will end up here.
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        try {
            if(log.isInfoEnabled())
                log.info("Starting the app...");

            Engine engine = Bootstrap.setup();
            engine.run();
        } catch (Exception e) {
            log.error("Error starting the app: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}