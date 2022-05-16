import imgui.ImVec4;
import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiCond;
import imgui.internal.ImGui;
import imgui.type.ImBoolean;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

public class ImPlotExample {
    static double lb = -2*Math.PI;
    static double ub = 2*Math.PI;
    static int terms = 4096;
    static ImBoolean showDemo = new ImBoolean(true);
    static ImVec4 color = new ImVec4();

    public static Double[] xd = new Double[terms];
    public static Double[] yd = new Double[terms];

    public static Double[] xf = new Double[terms/2];
    public static Double[] yf = new Double[terms/2];
    static boolean stopCapture = false;
    static ByteArrayOutputStream
            byteArrayOutputStream;
    static AudioFormat audioFormat;
    static TargetDataLine targetDataLine;
    static AudioInputStream audioInputStream;
    static SourceDataLine sourceDataLine;

    static {
        ImPlot.createContext();
    }

    public static void show(ImBoolean showImPlotWindow) {
//        generate(terms, lb, ub);
        convertData(FFT.generate(terms, lb, ub), xd, yd);
        formatFFT(FFT.fft(FFT.generate(terms, lb, ub)), xf, yf);
//        yf = formatFFTData(FFT.fft(FFT.generate(terms, lb, ub)));
        ImGui.setNextWindowSize(900, 700, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX(), ImGui.getMainViewport().getPosY(), ImGuiCond.Once);
        if (ImGui.begin("Fourier Analysis", showImPlotWindow)) {
//            ImGui.text("This a demo for ImPlot");

            ImGui.alignTextToFramePadding();

            if(ImGui.button("Record")) {
//                System.out.println("Record");
                captureAudio();
            }
            if(ImGui.button("Stop Record")) {
                stopCapture = true;
            }
            if(ImGui.button("Analyse")) {
                playAudio();
            }

            if (ImPlot.beginPlot("Time Domain")) {
                ImPlot.plotLine("Audio Wave", xd, yd);
//                ImPlot.plotBars("Bars", xs, ys);
                ImPlot.endPlot();
            }

            if (ImPlot.beginPlot("Frequency Domain")) {
                ImPlot.plotLine("Frequencies", xf, yf);
                ImPlot.endPlot();
            }


//            if (showDemo.get()) {
//                ImPlot.showDemoWindow(showDemo);
//                ImGui.showDemoWindow(showDemo);
//            }
//            ImGui.text("Top 10 frequencies: ");
//            sort();
//            ImGui.text(String.format("%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n", xf[0], xf[1], xf[2], xf[3], xf[4], xf[5], xf[6], xf[7], xf[8], xf[9]));
        }

        ImGui.end();
    }

    public static void sort()
    {
        int n = xf.length;
        for (int i = 1; i < n; ++i) {
            Double key = yf[i];
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && yf[j] < key) {
                xf[j + 1] = xf[j];
                j = j - 1;
            }
            xf[j + 1] = key;
        }
    }
    public static void generate(int terms, double lb, double ub) {
        double range = ub - lb;
        double i = lb;
        for(int x = 0; x < terms; i += range/terms, x++) {
            xd[x] = i;
            yd[x] = Math.sin(i);
        }
    }

    public static void convertData(Complex[] cdata, Double[] x, Double[] y) {
        int i = 0;
        double j = lb;
        for(Complex num : cdata) {
            x[i] = j;
            y[i] = num.re();
            ++i;
            j += (ub - lb) / terms;
        }
    }

    public static void formatFFT(Complex[] cdata, Double[] x, Double[] y) {
        int i = 0;
        double j = 0;
        for(Complex num : cdata) {
            if(i > terms/2 - 1) {
                break;
            }
            x[i] = j;
            y[i] = num.re();
            ++i;
//            j += (ub - lb) / terms;
            j++;
        }
    }

    private static void captureAudio(){
        try{
            //Get everything set up for
            // capture
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo =
                    new DataLine.Info(
                            TargetDataLine.class,
                            audioFormat);
            targetDataLine = (TargetDataLine)
                    AudioSystem.getLine(
                            dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            //Create a thread to capture the
            // microphone data and start it
            // running.  It will run until
            // the Stop button is clicked.
            Thread captureThread =
                    new Thread(
                            new CaptureThread());
            captureThread.start();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }//end catch
    }//end captureAudio method

    //This method plays back the audio
    // data that has been saved in the
    // ByteArrayOutputStream
    private static void playAudio() {
        try{
            //Get everything set up for
            // playback.
            //Get the previously-saved data
            // into a byte array object.
            byte audioData[] =
                    byteArrayOutputStream.
                            toByteArray();
            //Get an input stream on the
            // byte array containing the data
            System.out.println(Arrays.toString(audioData));
            InputStream byteArrayInputStream
                    = new ByteArrayInputStream(
                    audioData);
            AudioFormat audioFormat =
                    getAudioFormat();
            audioInputStream =
                    new AudioInputStream(
                            byteArrayInputStream,
                            audioFormat,
                            audioData.length/audioFormat.
                                    getFrameSize());
            DataLine.Info dataLineInfo =
                    new DataLine.Info(
                            SourceDataLine.class,
                            audioFormat);
            sourceDataLine = (SourceDataLine)
                    AudioSystem.getLine(
                            dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            //Create a thread to play back
            // the data and start it
            // running.  It will run until
            // all the data has been played
            // back.
            Thread playThread =
                    new Thread(new PlayThread());
            playThread.start();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }//end catch
    }//end playAudio

    //This method creates and returns an
    // AudioFormat object for a given set
    // of format parameters.  If these
    // parameters don't work well for
    // you, try some of the other
    // allowable parameter values, which
    // are shown in comments following
    // the declarations.
    private static AudioFormat getAudioFormat(){
        float sampleRate = 8000.0F;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 1;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }//end getAudioFormat
//===================================//

    //Inner class to capture data from
// microphone
    static class CaptureThread extends Thread{
        //An arbitrary-size temporary holding
        // buffer
        byte tempBuffer[] = new byte[10000];
        public void run(){
            byteArrayOutputStream =
                    new ByteArrayOutputStream();
            stopCapture = false;
            try{//Loop until stopCapture is set
                // by another thread that
                // services the Stop button.
                while(!stopCapture){
                    //Read data from the internal
                    // buffer of the data line.
                    int cnt = targetDataLine.read(
                            tempBuffer,
                            0,
                            tempBuffer.length);
                    if(cnt > 0){
                        //Save data in output stream
                        // object.
                        byteArrayOutputStream.write(
                                tempBuffer, 0, cnt);
                    }//end if
                }//end while
                byteArrayOutputStream.close();
            }catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }//end catch
        }//end run
    }//end inner class CaptureThread
    //===================================//
//Inner class to play back the data
// that was saved.
    static class PlayThread extends Thread{
        byte tempBuffer[] = new byte[10000];

        public void run(){
            try{
                int cnt;
                //Keep looping until the input
                // read method returns -1 for
                // empty stream.
                while((cnt = audioInputStream.
                        read(tempBuffer, 0,
                                tempBuffer.length)) != -1){
                    if(cnt > 0){
                        //Write data to the internal
                        // buffer of the data line
                        // where it will be delivered
                        // to the speaker.
                        sourceDataLine.write(
                                tempBuffer, 0, cnt);
                    }//end if
                }//end while
                //Block and wait for internal
                // buffer of the data line to
                // empty.
                sourceDataLine.drain();
                sourceDataLine.close();
            }catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }//end catch
        }//end run
    }//end inner class PlayThread


}