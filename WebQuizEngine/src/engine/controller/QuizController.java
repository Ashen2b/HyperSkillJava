package engine.controller;

import engine.model.Answer;
import engine.model.Feedback;
import engine.model.QuizDto;
import engine.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/{id}")
    public QuizDto getQuiz(@PathVariable("id") long id) {
        return quizService.getQuiz(id);
    }

    @GetMapping
    public List<QuizDto> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @PostMapping
    public QuizDto createQuiz(@Valid @RequestBody QuizDto quizDto, Authentication auth) {
        String userName = auth.getName();
        return quizService.createQuiz(quizDto, userName);
    }

    @PostMapping("/{id}/solve")
    public Feedback solveQuiz(@PathVariable("id") long id, @Valid @RequestBody Answer answer) {
        return quizService.solveQuiz(id, answer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@accessChecker.isQuizAuthor(#id, authentication)")
    public void deleteQuizById(@PathVariable("id") long id) {
        quizService.deleteQuizById(id);
    }
}
