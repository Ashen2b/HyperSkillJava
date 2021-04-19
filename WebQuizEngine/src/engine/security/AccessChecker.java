package engine.security;

import engine.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Component
public class AccessChecker {
    private final QuizRepository quizRepository;

    public boolean isQuizAuthor(long quizId, Authentication authentication) {
        return quizRepository.findById(quizId)
                .map(quiz -> authentication.getName().equals(quiz.getUser().getEmail()))
                .orElse(true);
    }
}
