package com.projeto_musique.core;

import com.projeto_musique.models.SoundData;

/**
 * Interface for a sound output player.
 * This interface is important to have multiple codecs.
 */
public interface SoundPlayer {

    /**
     * Play the sound passing his data.
     *
     * @param soundData with data of the sound
     */
    void play(SoundData soundData);

    /**
     * Stop the sound.
     */
    void stop();

}
