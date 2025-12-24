package org.example.Business.Model;

/**
 * Representa una nota musical individual con su frecuencia y duración.
 * [cite_start]Utilizado para la secuencia de reproducción en las canciones[cite: 33, 34].
 */
public class Note {
    private double frequency;
    private int durationMs;

    public Note() {
    }

    public Note(double frequency, int durationMs) {
        this.frequency = frequency;
        this.durationMs = durationMs;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }
}