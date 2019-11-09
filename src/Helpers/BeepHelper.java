package Helpers;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class BeepHelper {
    /**
     * Sample Rate
     */
    private static final float SAMPLE_RATE = 8000f;
    
    /***
     * Plays beep at custom volume
     * @param hz
     * @param msecs
     * @param vol
     * @throws LineUnavailableException
     */
    public static void play(int hz, int msecs, double vol)  throws LineUnavailableException
    {
        byte[] buf = new byte[1];
        AudioFormat af =  new AudioFormat(SAMPLE_RATE, 8, 1, true, false); 
        
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        
        for (int i=0; i < msecs*8; i++) {
            double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
            sdl.write(buf,0,1);
        }
        
        sdl.drain();
        sdl.stop();
        sdl.close();
    }


    /**
     * Plays a beep at default volume
     * @param hz
     * @param msecs
     * @throws LineUnavailableException
     */
    public static void play(int hz, int msecs) throws LineUnavailableException
    {
        play(hz, msecs, 1.0);
    }
}
