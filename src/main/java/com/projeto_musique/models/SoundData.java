package com.projeto_musique.models;

/**
 * Sound data to be passed to the sound processor.
 *
 * @param streamURL If the sound comes as a stream, it is passed in this argument.
 */
public record SoundData(String streamURL) {

}
