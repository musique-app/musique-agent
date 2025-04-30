package com.projeto_musique.core;

import com.projeto_musique.core.connectivity.ConnectionChecker;
import com.projeto_musique.core.connectivity.Login;
import com.projeto_musique.core.connectivity.SocketManager;
import com.projeto_musique.models.LoginResult;
import com.projeto_musique.models.SoundData;
import com.projeto_musique.models.exceptions.ConnectionException;
import com.projeto_musique.models.exceptions.RequestException;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the core of the application. All the process should pass in this class.
 * The "Projeto Musique" works by logging in the app and getting access to the music.
 * After the login, the Engine should call the sound output with the data of the music that it should play.
 * <p>
 * This class is also responsible to check the connection with the network if it operates in online mode.
 */
@Slf4j
public class Engine {

    /**
     * Login logic for the app.
     */
    private final Login login;

    /**
     * Socket manager.
     */
    private final SocketManager socketManager;

    /**
     * Util object to check if the connection is available.
     */
    private final ConnectionChecker connectionChecker;

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
        this.socketManager = new SocketManager();
        this.connectionChecker = new ConnectionChecker();
        this.soundPlayer = soundPlayer;
        this.connectionMode = connectionMode;
    }

    /**
     * Run the whole application.
     *
     * @throws Exception if anything goes wrong.
     */
    @SuppressWarnings("ALL")
    public void run() throws Exception {
        if (log.isInfoEnabled()) log.info("Application started in {} mode", connectionMode.name());

        while (true) {
            try {
                SoundData soundData = getSoundData();
                if (soundData == null) {
                    log.error("Unable to get sound data");
                    Thread.sleep(5000);
                    continue;
                }
                soundPlayer.play(soundData);
            } catch (ConnectionException | RequestException e) {
                log.error(e.getMessage(), e);
                Thread.sleep(5000);
            }
        }
    }

    private SoundData getSoundData() throws ConnectionException, RequestException {
        switch (connectionMode) {
            case ONLINE -> {
                // if (!connectionChecker.check()) throw new ConnectionException("Connection cannot be established");
                String username = System.getenv("USERNAME");
                String password = System.getenv("PASSWORD");
                LoginResult loginResult = login.request(username, password);
                if (loginResult == null) return null;

                boolean success = socketManager.openSocket(loginResult.getToken().getAccessToken());
                if (!success) return null;

                String rawStreamUrl = loginResult.getUser().getCompany().getStream();
                String streamUrl = rawStreamUrl.substring(9, rawStreamUrl.indexOf(".mp3\"") + 4);
                return new SoundData(streamUrl);
            }
            case OFFLINE -> {
                // TODO: 02/04/2025 Implement OFFLINE mode
            }
            case ONLINE_AND_OFFLINE -> {
                // TODO: 02/04/2025 Implement OFFLINE/ONLINE mode
            }
        }

        throw new IllegalArgumentException("Invalid connection mode");
    }

}
