package com.projeto_musique.agent.core.player;

import com.projeto_musique.agent.models.SoundData;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

/**
 * MP3 audio player implementation for playing a main stream and ads.
 * <p>
 * This class allows playback of a continuous main MP3 stream and plays
 * an ad on top of it, fading the main volume down during the ad and back up
 * after the ad finishes.
 * Mono ads are automatically converted to stereo.
 * </p>
 */
@Slf4j
public class MP3 implements SoundPlayer {

    /**
     * Flag to indicate whether playback should continue.
     */
    private volatile boolean running = true;

    /**
     * Current volume for the main stream (0.0 to 1.0).
     */
    private volatile float mainVolume = 1.0f;

    /**
     * Current volume for the ad (0.0 to 1.0).
     */
    private volatile float adVolume = 0f;

    /**
     * Byte buffer holding the fully decoded ad PCM data.
     */
    private byte[] adBuffer = null;

    /**
     * Current read pointer in the ad buffer.
     */
    private int adPointer = 0;

    /**
     * Thread handling the continuous playback loop.
     */
    private Thread playbackThread;

    /**
     * URL of the main MP3 stream.
     */
    private String mainStreamUrl;

    /**
     * Starts playback of the main MP3 stream.
     *
     * @param soundData Contains the URL of the main MP3 stream.
     */
    @Override
    public void play(SoundData soundData) {
        mainStreamUrl = soundData.streamURL();
        log.info("Playing main stream: {}", mainStreamUrl);

        running = true;
        playbackThread = new Thread(this::playbackLoop, "Audio-Playback-Thread");
        playbackThread.start();
    }

    /**
     * Plays an advertisement on top of the main stream.
     * The main stream volume is faded down while the ad plays and
     * restored afterward.
     * Mono ads are converted to stereo automatically.
     *
     * @param adUrl URL of the MP3 advertisement to play.
     */
    @Override
    public void playAd(String adUrl) {
        log.info("Playing ad: {}", adUrl);

        new Thread(() -> {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                AudioInputStream adStream = AudioSystem.getAudioInputStream(
                        new BufferedInputStream(new URL(adUrl).openStream()));

                AudioFormat targetFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        44100,
                        16,
                        1,
                        2,
                        44100,
                        false
                );

                AudioInputStream pcmAd = AudioSystem.getAudioInputStream(targetFormat, adStream);
                byte[] buf = new byte[4096];
                int r;
                while ((r = pcmAd.read(buf)) != -1) {
                    byteArrayOutputStream.write(buf, 0, r);
                }
                pcmAd.close();

                byte[] mono = byteArrayOutputStream.toByteArray();
                adBuffer = monoToStereo(mono, mono.length);
                adPointer = 0;

                new Thread(() -> fadeVolume(true, 500)).start();

            } catch (Exception e) {
                log.error("Failed to load ad: {}", adUrl, e);
            }
        }).start();
    }

    /**
     * Stops playback of both the main stream and any playing ad.
     */
    @Override
    public void stop() {
        log.info("Stopping playback");
        running = false;
        if (playbackThread != null) playbackThread.interrupt();
    }

    /**
     * Internal loop that continuously reads from the main stream,
     * mixes it with the ad (if present), and writes to the audio output line.
     */
    private void playbackLoop() {
        try {
            AudioInputStream mainStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(new URL(mainStreamUrl).openStream()));

            AudioFormat mainFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    44100,
                    16,
                    2,
                    4,
                    44100,
                    false
            );

            AudioInputStream decodedMain = AudioSystem.getAudioInputStream(mainFormat, mainStream);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, mainFormat);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(mainFormat);
            line.start();

            byte[] mainBuffer = new byte[8192];

            while (running) {
                int read = decodedMain.read(mainBuffer, 0, mainBuffer.length);
                if (read < 0) break;

                byte[] output = new byte[read];

                for (int i = 0; i < read; i += 4) {
                    short mainL = (short) ((mainBuffer[i + 1] << 8) | (mainBuffer[i] & 0xff));
                    short mainR = (short) ((mainBuffer[i + 3] << 8) | (mainBuffer[i + 2] & 0xff));

                    short adL = 0, adR = 0;
                    if (adBuffer != null && adPointer + 4 <= adBuffer.length) {
                        adL = (short) ((adBuffer[adPointer + 1] << 8) | (adBuffer[adPointer] & 0xff));
                        adR = (short) ((adBuffer[adPointer + 3] << 8) | (adBuffer[adPointer + 2] & 0xff));
                        adPointer += 4;
                        if (adPointer >= adBuffer.length) {
                            adBuffer = null;
                            new Thread(() -> fadeVolume(false, 500)).start();
                        }
                    }

                    int mixL = (int) (mainL * mainVolume + adL * adVolume);
                    int mixR = (int) (mainR * mainVolume + adR * adVolume);

                    if (mixL > Short.MAX_VALUE) mixL = Short.MAX_VALUE;
                    if (mixL < Short.MIN_VALUE) mixL = Short.MIN_VALUE;
                    if (mixR > Short.MAX_VALUE) mixR = Short.MAX_VALUE;
                    if (mixR < Short.MIN_VALUE) mixR = Short.MIN_VALUE;

                    output[i] = (byte) (mixL & 0xff);
                    output[i + 1] = (byte) ((mixL >> 8) & 0xff);
                    output[i + 2] = (byte) (mixR & 0xff);
                    output[i + 3] = (byte) ((mixR >> 8) & 0xff);
                }

                line.write(output, 0, read);
            }

            line.drain();
            line.close();
        } catch (Exception e) {
            log.error("Playback error", e);
        }
    }

    /**
     * Converts a mono PCM buffer to stereo PCM.
     * Each mono sample is duplicated to left and right channels.
     *
     * @param monoBuffer The mono PCM data.
     * @param bytesRead  Number of bytes in the mono buffer.
     * @return PCM data in stereo format.
     */
    private byte[] monoToStereo(byte[] monoBuffer, int bytesRead) {
        int frames = bytesRead / 2;
        byte[] stereo = new byte[frames * 4];

        for (int i = 0; i < frames; i++) {
            byte low = monoBuffer[i * 2];
            byte high = monoBuffer[i * 2 + 1];

            stereo[i * 4] = low;
            stereo[i * 4 + 1] = high;

            stereo[i * 4 + 2] = low;
            stereo[i * 4 + 3] = high;
        }

        return stereo;
    }

    /**
     * Fades the main stream volume down or up and sets the ad volume.
     *
     * @param down       True to fade down main volume, false to fade up.
     * @param durationMs Duration of the fade in milliseconds.
     */
    private void fadeVolume(boolean down, int durationMs) {
        int steps = 20;
        float from = down ? 1.0f : 0.2f;
        float to = down ? 0.2f : 1.0f;
        int stepTime = durationMs / steps;
        float delta = (to - from) / steps;

        for (int i = 0; i <= steps; i++) {
            mainVolume = from + delta * i;
            adVolume = down ? 1.0f : 0f;
            try {
                Thread.sleep(stepTime);
            } catch (InterruptedException ignored) {
            }
        }
    }

}
