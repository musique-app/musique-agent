package com.projeto_musique.core.player;

import com.projeto_musique.core.SoundPlayer;
import com.projeto_musique.models.SoundData;
import javazoom.jl.player.advanced.AdvancedPlayer;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;

/**
 * Implementation of a sound using MP3 codec.
 */
@Slf4j
public class MP3 implements SoundPlayer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void play(SoundData soundData) {
        try {
            InputStream audioStream = new URL(soundData.streamURL()).openStream();
            AdvancedPlayer player = new AdvancedPlayer(audioStream);
            player.play();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
