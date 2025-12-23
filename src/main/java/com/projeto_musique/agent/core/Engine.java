package com.projeto_musique.agent.core;

import com.projeto_musique.agent.Properties;
import com.projeto_musique.agent.core.connectivity.Login;
import com.projeto_musique.agent.core.connectivity.SocketManager;
import com.projeto_musique.agent.core.connectivity.Stream;
import com.projeto_musique.agent.core.player.SoundPlayer;
import com.projeto_musique.agent.models.GetStreamResult;
import com.projeto_musique.agent.models.LoginResult;
import com.projeto_musique.agent.models.SoundData;
import com.projeto_musique.agent.models.exceptions.ConnectionException;
import com.projeto_musique.agent.models.exceptions.RequestException;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the core of the application. All the process should pass in this class.
 * The "Musique" works by logging in the app and getting access to the music.
 * After the login, the Engine should call the sound output with the data of the music that it should play.
 * <p>
 * This class is also responsible to check the connection with the network if it operates in online mode.
 */
@Slf4j
public final class Engine {

    /**
     * Login logic for the app.
     */
    private final Login login;

    /**
     * Stream logic for the app.
     */
    private final Stream stream;

    /**
     * Socket manager.
     */
    private final SocketManager socketManager;

    /**
     * The specific output.
     */
    private final SoundPlayer soundPlayer;

    /**
     * Connection mode for this run.
     */
    private final ConnectionMode connectionMode;

    /**
     * Thread running the engine.
     */
    private final Thread runningThread;

    /**
     * Indicates if the application is running.
     */
    private boolean running;

    public Engine(SoundPlayer soundPlayer, ConnectionMode connectionMode) {
        this.login = new Login();
        this.stream = new Stream();
        this.socketManager = new SocketManager(this);
        this.soundPlayer = soundPlayer;
        this.connectionMode = connectionMode;

        this.runningThread = createRunningThread();
    }

    /**
     * Start the whole application.
     */
    public void start() {
        log.info("Starting application in {} mode", connectionMode.name());

        running = true;
        runningThread.start();

        log.info("Application started successfully.");
    }

    /**
     * Play an advertisement.
     */
    public void playAd(String substring) {
        log.info("Playing advertisement: {}", substring);

        soundPlayer.playAd(substring);
    }

    /**
     * Stop the whole application.
     */
    public void stop() {
        log.info("Stopping the application...");

        running = false;
        soundPlayer.stop();
        socketManager.closeSocket();

        log.info("Application stopped.");
    }

    /**
     * Create the main running thread.
     *
     * @return the created thread.
     */
    @SuppressWarnings("BusyWait")
    private Thread createRunningThread() {
        return new Thread(() -> {
            while (running) {
                try {
                    try {
                        log.info("Trying to connect...");
                        SoundData soundData = getSoundData();
                        if (soundData == null) {
                            log.error("Unable to get sound data");
                            Thread.sleep(5000);
                            continue;
                        }

                        log.info("Connected successfully. Playing sound...");
                        soundPlayer.play(soundData);
                        while (true) Thread.onSpinWait();
                    } catch (ConnectionException | RequestException e) {
                        log.error(e.getMessage(), e);
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Engine thread interrupted", e);
                }
            }
        }, "Engine-Thread");
    }

    /**
     * Get the sound data based on the connection mode.
     *
     * @return the sound data.
     * @throws ConnectionException if a connection error occurs.
     * @throws RequestException    if a request error occurs.
     */
    private SoundData getSoundData() throws ConnectionException, RequestException {
        switch (connectionMode) {
            case ONLINE -> {
                String username = System.getenv(Properties.ENV_USERNAME_KEY);
                String password = System.getenv(Properties.ENV_PASSWORD_KEY);
                LoginResult loginResult = login.request(username, password);
                if (loginResult == null)
                    return null;

                boolean success = socketManager.openSocket(loginResult.getAccessToken().getToken());
                if (!success)
                    return null;

                GetStreamResult streamResult = stream.request(loginResult.getAccessToken().getToken());

                String rawStreamUrl = streamResult.getStream();
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
