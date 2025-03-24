package com.projeto_musique;

import com.projeto_musique.core.ConnectionMode;
import com.projeto_musique.core.Engine;
import com.projeto_musique.core.SoundPlayer;
import com.projeto_musique.core.player.MP3;
import com.projeto_musique.models.exceptions.ConnectionException;

/**
 * Bootstrap class for the project.
 * Only the Engine should be wired up.
 */
public class Bootstrap {

    /**
     * Setup of the whole application.
     *
     * @return Engine for this JVM
     */
    public static Engine setup() throws ConnectionException {
        SoundPlayer soundPlayer = getSoundOutput();
        ConnectionMode mode = getConnectionMode();

        Engine engine = new Engine(soundPlayer, mode);
        if (!engine.checkConnection()) throw new ConnectionException("Connection cannot be established");
        return engine;
    }

    /**
     * In boot time, this method is responsible to choose which implementation of the output
     * should be chosen.
     * It should recognize the hardware it is running in and possibly read some properties.
     *
     * @return SoundOutput
     */
    private static SoundPlayer getSoundOutput() {
        return new MP3();
    }

    /**
     * While booting, this method needs to verify what is the property for the connectionMode.
     * The app will then run with this mode until it restarts.
     *
     * @return ConnectionMode
     */
    private static ConnectionMode getConnectionMode() {
        return ConnectionMode.ONLINE;
    }

}
