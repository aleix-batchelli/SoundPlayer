package org.example.Business.Audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundSynthSinus extends SoundSynth {

    @Override
    public void makeSound(double freq, int durationMs) {
        if (freq <= 0) return; // Ignore silent/invalid frequencies

        try {
            // 1. Define Audio Format: SampleRate, SampleSize(bits), Channels, Signed, BigEndian
            // We use 8-bit for simplicity (1 byte per sample)
            AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
            
            // 2. Open the line
            SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();

            // 3. Calculate number of samples needed for the duration
            // (duration / 1000) * samples_per_second
            int numberOfSamples = (int) ((durationMs * SAMPLE_RATE) / 1000);
            byte[] buffer = new byte[numberOfSamples];

            // 4. Generate the Sine Wave
            for (int i = 0; i < numberOfSamples; i++) {
                // Math: sin(2 * PI * frequency * time_index)
                double angle = 2.0 * Math.PI * freq * (i / SAMPLE_RATE);
                
                // Scale amplitude to max byte size (127 for signed 8-bit)
                // We use 100 to avoid clipping/distortion at the edges
                buffer[i] = (byte) (Math.sin(angle) * 100); 
            }

            // 5. Write to the audio output
            sdl.write(buffer, 0, buffer.length);
            
            // 6. Clean up
            sdl.drain(); // Wait for data to finish playing
            sdl.close();

        } catch (LineUnavailableException e) {
            System.err.println("Error de Audio: " + e.getMessage());
        }
    }
}