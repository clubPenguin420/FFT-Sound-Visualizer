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

    private static Double[] xs = new Double[]{};
    private static Double[] ys = new Double[]{};

    static {
        ImPlot.createContext();
    }

    public static void show(ImBoolean showImPlotWindow) {
        ImGui.setNextWindowSize(500, 400, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX() + 100, ImGui.getMainViewport().getPosY() + 100, ImGuiCond.Once);
        if (ImGui.begin("ImPlot Demo", showImPlotWindow)) {
            ImGui.text("This a demo for ImPlot");

            ImGui.alignTextToFramePadding();

            if (ImPlot.beginPlot("Example Plot")) {
                ImPlot.plotLine("Line", xs, ys);
                ImPlot.plotBars("Bars", xs, ys);
                ImPlot.endPlot();
            }

            if (ImPlot.beginPlot("Example Scatterplot")) {
                ImPlot.plotScatter("Scatter", xs, ys);
                ImPlot.endPlot();
            }
        }

        ImGui.end();
    }

    public static Double[] generate(int terms, double lb, double ub) {
        Double[] data = new Double[terms];
        double range = ub - lb;
        double i = 0;
        for(int x = 0; x < terms; i += range/terms, x++) {
            xs[x] = i;
            ys[x] = (Math.sin(i));
        }
        return data;
    }
}