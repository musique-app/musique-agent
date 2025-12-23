package com.projeto_musique.agent.core.connectivity;

import com.projeto_musique.agent.Properties;
import com.projeto_musique.agent.core.Engine;
import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

/**
 * Manages a Socket.IO connection to a specified server.
 * <p>
 * Handles opening, identifying, listening for messages, and closing
 * a Socket.IO connection. This is used to receive audio commands
 * (e.g., play ads) from the server.
 * </p>
 */
@Slf4j
public class SocketManager {

    /**
     * Engine instance used to trigger playback of audio or ads.
     */
    private final Engine engine;

    /**
     * The Socket.IO client instance used for the connection.
     */
    private Socket socket;

    public SocketManager(Engine engine) {
        this.engine = engine;
    }

    /**
     * Opens a Socket.IO connection to the server and identifies the client
     * using the provided access token.
     * <p>
     * Sets up listeners for connection, identification, join, errors,
     * incoming messages (playAudio), and disconnection events.
     * </p>
     *
     * @param accessToken The access token used to identify the client.
     * @return True if the socket successfully connected and identified; false otherwise.
     */
    public boolean openSocket(String accessToken) {
        log.info("Opening socket for access token: {}", accessToken);

        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;
        options.timeout = 5000;

        CountDownLatch latch = new CountDownLatch(1);

        try {
            // Connect to the server
            socket = IO.socket(Properties.BASE_URL, options);

            // On connect
            socket.on(Socket.EVENT_CONNECT, args -> log.debug("Connected event"));

            // On identify
            socket.on("identify", args -> {
                socket.emit("identification", accessToken);
                log.debug("Identify");
                latch.countDown();
            });

            // On join
            socket.on("join", args -> log.debug("Joined: {}", args[0]));

            // On connect error
            socket.on(Socket.EVENT_CONNECT_ERROR, args -> {
                for (Object arg : args) {
                    log.error("Connect error: {}", arg);
                }
                latch.countDown();
            });

            // On playAudio message
            socket.on("playAudio", args -> {
                log.debug("Message received: {}", args[0]);

                int startIndex = args[0].toString().indexOf("http");
                engine.playAd(args[0].toString().substring(startIndex, args[0].toString().length() - 2));
            });

            // On disconnect
            socket.on(Socket.EVENT_DISCONNECT, args -> log.debug("Disconnected"));

            socket.connect();

            // Wait until identification or error
            latch.await();

            boolean isConnected = socket.connected();
            log.info("Socket connected: {}", isConnected);

            return isConnected;
        } catch (URISyntaxException | InterruptedException e) {
            log.error("Failed to open socket", e);
            return false;
        }
    }

    /**
     * Closes the Socket.IO connection.
     * <p>
     * Disconnects the socket and logs the closure.
     * </p>
     */
    public void closeSocket() {
        log.info("Closing the socket...");
        if (socket != null) {
            socket.close();
            log.info("Socket closed.");
        }
    }

}
