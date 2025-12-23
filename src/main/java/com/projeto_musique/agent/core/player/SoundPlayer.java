package com.projeto_musique.agent.core.player;

import com.projeto_musique.agent.models.SoundData;

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
     * Play an advertisement sound from a substring URL.
     *
     * @param adUrl URL substring of the ad sound
     */
    void playAd(String adUrl);

    /**
     * Stop the sound.
     */
    void stop();

}
