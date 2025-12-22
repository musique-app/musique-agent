package com.projeto_musique.agent.core.connectivity;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

/**
 * Manages a Socket.IO connection to a specified server.
 */
@Slf4j
public class SocketManager {

    private Socket socket;

    public boolean openSocket(String token) {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;
        options.timeout = 5000;

        CountDownLatch latch = new CountDownLatch(1);

        try {
            socket = IO.socket("https://portal-musique-backend.herokuapp.com/", options);

            socket.on(Socket.EVENT_CONNECT, args -> {
                socket.emit("identification", token);
                if (log.isDebugEnabled())
                    log.debug("Connected!");
                latch.countDown();
            });

            socket.on(Socket.EVENT_CONNECT_ERROR, args -> {
                for (Object arg : args) {
                    System.err.println(arg);
                }
                log.error("Connect error:");
                latch.countDown();
            });

            socket.on(Socket.EVENT_DISCONNECT, args -> log.error("Disconnected"));

            socket.connect();

            latch.await();
            return socket.connected();
        } catch (URISyntaxException | InterruptedException e) {
            return false;
        }
    }

    public void closeSocket() {
        socket.close();
    }

}
