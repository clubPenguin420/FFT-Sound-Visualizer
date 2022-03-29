import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiCond;
import imgui.internal.ImGui;
import imgui.type.ImBoolean;

import java.awt.Desktop;
import java.net.URI;

public class ImPlotExample {
    private static final String URL = "https://github.com/epezent/implot/tree/555ff68";
    private static final ImBoolean showDemo = new ImBoolean(false);
    static double lb = 0;
    static double ub = 2*Math.PI;
    static int terms = 32;

    public static Double[] xs = new Double[]{};
    public static Double[] ys = new Double[]{};

    static {
        ImPlot.createContext();
    }

    public static void show(ImBoolean showImPlotWindow) {
        generate(terms, lb, ub);
        ImGui.setNextWindowSize(800, 600, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX() + 100, ImGui.getMainViewport().getPosY() + 100, ImGuiCond.Once);
        if (ImGui.begin("Fourier Analysis", showImPlotWindow)) {
//            ImGui.text("This a demo for ImPlot");

            ImGui.alignTextToFramePadding();

            if (ImPlot.beginPlot("Time Domain")) {
                ImPlot.plotLine("Audio Wave", xs, ys);
//                ImPlot.plotBars("Bars", xs, ys);
                ImPlot.endPlot();
            }

            if (ImPlot.beginPlot("Frequency Domain")) {
                ImPlot.plotLine("Frequencies", xs, ys);
                ImPlot.endPlot();
            }
        }

        ImGui.end();
    }

    public static void generate(int terms, double lb, double ub) {
        double range = ub - lb;
        double i = lb;
        for(int x = 0; x < terms; i += range/terms, x++) {
            xs[x] = i;
            ys[x] = Math.sin(i);
        }
    }
}