package engine.controller;

import engine.model.UserDto;
import engine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor

@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/api/register")
    public void registerUserAccount(@Valid @RequestBody UserDto userDto) {
        userService.registerUserAccount(userDto);
    }
}
