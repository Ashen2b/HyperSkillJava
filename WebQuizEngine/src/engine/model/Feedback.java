package engine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Feedback {
    public static final Feedback RIGHT = new Feedback(true, "Congratulations, you're right!");
    public static final Feedback WRONG = new Feedback(false, "Wrong answer! Please, try again.");

    private boolean success;
    private String feedback;
}