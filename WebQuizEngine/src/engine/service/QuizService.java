package engine.service;

import engine.model.Answer;
import engine.model.Feedback;
import engine.model.QuizDto;
import engine.model.QuizEntity;
import engine.repository.QuizRepository;
import engine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    public QuizDto getQuiz(long id) {
        QuizEntity quizEntity = quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return convertToDto(quizEntity);
    }

    public List<QuizDto> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public QuizDto createQuiz(QuizDto quizDto, String userName) {
        userRepository.findByEmail(userName).ifPresent(quizDto::setUser);
        QuizEntity quizEntity = convertToEntity(quizDto);
        return convertToDto(quizRepository.save(quizEntity));
    }

    public Feedback solveQuiz(long id, Answer answer) {
        QuizEntity quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return quiz.getAnswer().equals(answer.getAnswer()) ? Feedback.RIGHT : Feedback.WRONG;
    }

    public void deleteQuizById(long id) {
        if (!quizRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        quizRepository.deleteById(id);
    }

    private QuizEntity convertToEntity(QuizDto quizDto) {
        QuizEntity entity = new QuizEntity();
        entity.setTitle(quizDto.getTitle());
        entity.setText(quizDto.getText());
        entity.setOptions(quizDto.getOptions());
        entity.setAnswer(quizDto.getAnswer());
        entity.setUser(quizDto.getUser());
        return entity;
    }

    private QuizDto convertToDto(QuizEntity quizEntity) {
        QuizDto quizDto = new QuizDto();
        quizDto.setId(quizEntity.getId());
        quizDto.setTitle(quizEntity.getTitle());
        quizDto.setText(quizEntity.getText());
        quizDto.setOptions(quizEntity.getOptions());
        quizDto.setAnswer(quizEntity.getAnswer());
        quizDto.setUser(quizEntity.getUser());
        return quizDto;
    }
}
