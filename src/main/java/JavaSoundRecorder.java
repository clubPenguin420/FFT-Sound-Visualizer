import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class  JavaSoundRecorder {

    public static void main(String[] args) {

        JavaSoundRecorder bc = new JavaSoundRecorder();

        try {
            bc.run();

        } catch (UnsupportedAudioFileException | IOException
                | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void run() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException, InterruptedException
    {
        String filename = "test.wav";

        URL url = this.getClass().getResource(filename);
        System.out.println(url);

        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
        DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.open(ais);
        clip.start();
        Thread.sleep(6000);
        clip.close();
    }

    public static Double[] generate(int terms, double lb, double ub) {
        Double[] data = new Double[terms];
        double range = ub - lb;
        int x = 0;
        for(double i = lb; i < ub; i += range/terms, x++) {
            data[x] = Math.sin(2 * i) + Math.sin((5/4) * i) + Math.sin(5 * i);
        }
        return data;
    }
}