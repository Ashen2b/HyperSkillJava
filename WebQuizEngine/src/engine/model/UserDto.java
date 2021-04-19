package engine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDto {
    @NotNull
    @Email
    @Pattern(regexp = "\\w+@\\w+\\..{2,}")
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 5)
    @Pattern(regexp = "\\S+")
    private String password;
}
