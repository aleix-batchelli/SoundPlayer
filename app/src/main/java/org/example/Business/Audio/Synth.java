package org.example.Business.Audio;

/**
 * Abstract base class for synthesizers.
 * Defines the contract for making sound.
 */
public abstract class Synth {
    
    // Standard CD quality sample rate (44.1 kHz)
    protected static final float SAMPLE_RATE = 44100f;

    /**
     * Generates a tone of a specific frequency for a specific duration.
     * * @param freq The frequency in Hertz (Hz).
     * @param durationMs The duration in milliseconds.
     */
    public abstract void makeSound(double freq, int durationMs);
}