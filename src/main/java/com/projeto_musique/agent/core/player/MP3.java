package com.projeto_musique.agent.core.player;

import com.projeto_musique.agent.core.SoundPlayer;
import com.projeto_musique.agent.models.SoundData;
import javazoom.jl.player.advanced.AdvancedPlayer;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;

/**
 * Implementation of a sound using MP3 codec.
 */
@Slf4j
public class MP3 implements SoundPlayer {

    private AdvancedPlayer player;

    /**
     * {@inheritDoc}
     */
    @Override
    public void play(SoundData soundData) {
        if(log.isDebugEnabled()) log.debug("Playing MP3 sound from: {}", soundData.streamURL());

        try {
            InputStream audioStream = new URL(soundData.streamURL()).openStream();
            player = new AdvancedPlayer(audioStream);
            player.play();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        if(log.isDebugEnabled()) log.debug("Stopping MP3 sound");

        if (player != null) player.stop();
    }

}
