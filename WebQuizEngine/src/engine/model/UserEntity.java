package engine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter
@Setter
@Entity(name = "users_list")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = ".+@.+\\..+")
    @Column(unique = true)
    private String email;

    @Size(min = 5)
    @Pattern(regexp = "\\S+")
    private String password;
}
