import imgui.app.Application;
import imgui.app.Configuration;
import imgui.type.ImBoolean;

public class SoundVisualizer extends Application {
    @Override
    protected void configure(Configuration config) {
        config.setTitle("Sound Visualizer");
    }

    @Override
    public void process() {
//        float[] nums = generate(128, 0, 2*Math.PI);
//        ImGui.text("Hello, World!");
//        ImGui.button("calculate");
//        ImGui.plotLines("Graph", nums, nums.length);
        SoundAnalyzer.show(new ImBoolean(true));
    }

    public static void main(String[] args) {
        launch(new SoundVisualizer());
    }


}