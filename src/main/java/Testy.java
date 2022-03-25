import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;

public class Testy extends Application {
    @Override
    protected void configure(Configuration config) {
        config.setTitle("Sound Visualizer");
    }

    @Override
    public void process() {
        float[] nums = generate(128, 0, 2*Math.PI);
        ImGui.text("Hello, World!");
        ImGui.button("calculate");
        ImGui.plotLines("Graph", nums, nums.length);
    }

    public static void main(String[] args) {
        launch(new Testy());
    }

    public static float[] generate(int terms, double lb, double ub) {
        float[] data = new float[terms];
        double range = ub - lb;
        double i = 0;
        for(int x = 0; x < terms; i += range/terms, x++) {
            data[x] = (float)(Math.sin(i));
        }
        return data;
    }
}