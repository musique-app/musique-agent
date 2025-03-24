package com.projeto_musique.core;

import com.projeto_musique.models.LoginResult;
import com.projeto_musique.models.SoundData;
import com.projeto_musique.models.exceptions.ConnectionException;
import com.projeto_musique.models.exceptions.RequestException;

/**
 * This is the core of the application. All the process should pass in this class.
 * The "Projeto Musique" works by logging in the app and getting access to the music.
 * After the login, the Engine should call the sound output with the data of the music that it should play.
 * <p>
 * This class is also responsible to check the connection with the network if it operates in online mode.
 */
public class Engine {

    /**
     * Login logic for the app.
     */
    private final Login login;

    /**
     * The specific output.
     */
    private final SoundPlayer soundPlayer;

    /**
     * Connection mode for this run.
     */
    private final ConnectionMode connectionMode;

    public Engine(SoundPlayer soundPlayer, ConnectionMode connectionMode) {
        this.login = new Login();
        this.soundPlayer = soundPlayer;
        this.connectionMode = connectionMode;
    }

    /**
     * Run the whole application.
     *
     * @throws ConnectionException if connection mode is set to 0, and the connection is broken while running
     */
    public void run() throws ConnectionException {
        try {
            LoginResult loginResult = login.request("polemonunidade", "123456789");
            // TODO: 19/03/2025 Implementation
            while (true) {
                String rawStreamUrl = loginResult.getUser().getCompany().getStream();
                String streamUrl = rawStreamUrl.substring(9, rawStreamUrl.indexOf(".mp3\"") + 4);
                SoundData soundData = new SoundData(streamUrl);
                soundPlayer.play(soundData);
            }
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if connection is available.
     * If connection mode is set higher than 0, the response will always be true.
     *
     * @return true if connection mode is set to be bigger than 0. If connection mode is 0, returns true if connection
     * is available.
     */
    public boolean checkConnection() {
        if (connectionMode == ConnectionMode.ONLINE) {
            // TODO: 17/03/2025 Check connection. For now it only returns true for test purpose
            return true;
        }

        return true;
    }

}
