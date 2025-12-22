package com.projeto_musique.agent;

import com.projeto_musique.agent.core.ConnectionMode;
import com.projeto_musique.agent.core.Engine;
import com.projeto_musique.agent.core.SoundPlayer;
import com.projeto_musique.agent.core.player.MP3;
import lombok.extern.slf4j.Slf4j;

/**
 * Bootstrap class for the project.
 * Only the Engine should be wired up.
 */
@Slf4j
final class Bootstrap {

    private Bootstrap() {
    }

    /**
     * Setup of the whole application.
     *
     * @return Engine for this JVM
     */
    public static Engine setup() {
        if (log.isInfoEnabled())
            log.info("Bootstrapping the application...");

        SoundPlayer soundPlayer = getSoundOutput();
        ConnectionMode mode = getConnectionMode();

        if (log.isInfoEnabled())
            log.info("Application bootstrapped successfully.");

        return new Engine(soundPlayer, mode);
    }

    /**
     * In boot time, this method is responsible to choose which implementation of the output
     * should be chosen.
     * It should recognize the hardware it is running in and possibly read some properties.
     *
     * @return SoundOutput
     */
    private static SoundPlayer getSoundOutput() {
        if (log.isDebugEnabled())
            log.debug("Using sound player: {}", "MP3");

        return new MP3();
    }

    /**
     * While booting, this method needs to verify what is the property for the connectionMode.
     * The app will then run with this mode until it restarts.
     *
     * @return ConnectionMode
     */
    private static ConnectionMode getConnectionMode() {
        if (log.isDebugEnabled())
            log.debug("Connection mode: {}", "ONLINE");

        return ConnectionMode.ONLINE;
    }

}
