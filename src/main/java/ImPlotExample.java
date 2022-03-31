import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiCond;
import imgui.internal.ImGui;
import imgui.type.ImBoolean;

import java.awt.Desktop;
import java.net.URI;

public class ImPlotExample {
    static double lb = -2*Math.PI;
    static double ub = 2*Math.PI;
    static int terms = 4096;

    public static Double[] xd = new Double[terms];
    public static Double[] yd = new Double[terms];

    public static Double[] xf = new Double[terms];
    public static Double[] yf = new Double[terms];

    static {
        ImPlot.createContext();
    }

    public static void show(ImBoolean showImPlotWindow) {
//        generate(terms, lb, ub);
        convertData(FFT.generate(terms, lb, ub), xd, yd);
        convertData(FFT.fft(FFT.generate(terms, lb, ub)), xf, yf);
//        yf = formatFFTData(FFT.fft(FFT.generate(terms, lb, ub)));
        ImGui.setNextWindowSize(900, 700, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX(), ImGui.getMainViewport().getPosY(), ImGuiCond.Once);
        if (ImGui.begin("Fourier Analysis", showImPlotWindow)) {
//            ImGui.text("This a demo for ImPlot");

            ImGui.alignTextToFramePadding();

            if (ImPlot.beginPlot("Time Domain")) {
                ImPlot.plotLine("Audio Wave", xd, yd);
//                ImPlot.plotBars("Bars", xs, ys);
                ImPlot.endPlot();
            }

            if (ImPlot.beginPlot("Frequency Domain")) {
                ImPlot.plotLine("Frequencies", xf, yf);
                ImPlot.endPlot();
            }
        }

        ImGui.end();
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

    public static Double[] formatFFTData(Complex[] cdata) {
        convertData(cdata, xf, yf);
        Double[] data = new Double[terms/2];
        System.arraycopy(yf, 0, data, 0, data.length);
        Double[] newX = new Double[terms/2];
        for(int i = 0; i < data.length; ++i) {
            newX[i] = (double)i;
        }
        xf = newX;
        return data;
    }
}