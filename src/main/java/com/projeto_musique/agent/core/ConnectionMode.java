package com.projeto_musique.agent.core;

import lombok.Getter;

/**
 * Running mode for this application.
 */
@Getter
public enum ConnectionMode {

    /**
     * Full online mode is activated. It means that if the connection is broken, the app will stop
     * playing music as soon as the buffer ends.
     */
    ONLINE(0),

    /**
     * Full offline mode is activated. It means that even if a connection can be established,
     * it won't use online music.
     */
    OFFLINE(1),

    /**
     * This mode means that online mode is preferred, but if the connection is broken, it will try to
     * enter in offline mode.
     */
    ONLINE_AND_OFFLINE(2);

    /**
     * A mode can be configured using an int positive number.
     */
    private final int mode;

    ConnectionMode(int mode) {
        this.mode = mode;
    }

}
