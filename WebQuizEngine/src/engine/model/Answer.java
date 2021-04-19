package engine.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Answer {
    private Set<@NotNull Integer> answer = new HashSet<>();
}
