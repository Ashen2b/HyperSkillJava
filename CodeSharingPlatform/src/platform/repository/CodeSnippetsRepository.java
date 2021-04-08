package platform.repository;

import org.springframework.data.repository.CrudRepository;
import platform.model.CodeSnippet;

import java.util.List;
import java.util.Optional;

public interface CodeSnippetsRepository extends CrudRepository<CodeSnippet, String> {
    Optional<List<CodeSnippet>> findAllBySecretFalseOrderByDateDesc();
}
