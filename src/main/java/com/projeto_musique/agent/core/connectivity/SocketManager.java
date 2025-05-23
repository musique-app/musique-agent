package com.projeto_musique.agent.core.connectivity;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

/**
 *
 */
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

            socket.on(Socket.EVENT_CONNECT, args1 -> {
                socket.emit("identification", token);
                System.out.println("Connected!");
                latch.countDown();
            });

            socket.on(Socket.EVENT_CONNECT_ERROR, args1 -> {
                for (Object arg : args1) {
                    System.err.println(arg);
                }
                System.err.println("Connect error:");
                latch.countDown();
            });

            socket.on(Socket.EVENT_DISCONNECT, args1 -> System.err.println("Disconnected"));

            socket.connect();

            latch.await();
            return socket.connected();
        } catch (URISyntaxException | InterruptedException e) {
            return false;
        }
    }

    public void closeSocket() {

    }

}
