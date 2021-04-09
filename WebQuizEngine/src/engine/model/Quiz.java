package engine.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String title;
    private String text;
    private ArrayList<String> options;

    public Quiz() {
        this.title = "Test title";
        this.text = "Test text";
        this.options = new ArrayList<>(List.of("Option 1", "Option 2", "Option 3", "Option 4"));
    }
}
