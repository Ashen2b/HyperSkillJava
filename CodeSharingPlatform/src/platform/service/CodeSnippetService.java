package platform.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import platform.repository.CodeSnippetsRepository;
import platform.model.CodeSnippet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CodeSnippetService {

    private final CodeSnippetsRepository snippetsRepository;

    public CodeSnippetService(CodeSnippetsRepository snippetsRepository) {
        this.snippetsRepository = snippetsRepository;
    }

    public String addSnippet(CodeSnippet code) {
        return new JSONObject().put("id", snippetsRepository.save(code).getId()).toString();
    }

    // Didn't get into something more clear so next two methods kind of overloaded...
    public CodeSnippet getCodeSnippetById(String id) {
        Optional<CodeSnippet> result = snippetsRepository.findById(id);
        if (result.isPresent()) {
            CodeSnippet snippet = result.get();
            if (snippet.isAvailable()) {
                decrementViewsLimit(snippet);
                return snippet;
            } else {
                snippetsRepository.deleteById(id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public String getCodeSnippetByIdJson(String id) {
        Optional<CodeSnippet> result = snippetsRepository.findById(id);
        if (result.isPresent()) {
            CodeSnippet snippet = result.get();
            if (snippet.isNotSecret()) {
                return new JSONObject().put("code", snippet.getCode())
                        .put("date", snippet.getDate())
                        .put("time", 0)
                        .put("views", 0).toString();
            } else if (snippet.isAvailable()) {
                if (decrementViewsLimit(snippet)) {
                    JSONObject object = new JSONObject().put("code", snippet.getCode())
                            .put("date", snippet.getDate())
                            .put("time", snippet.getTimeLeft())
                            .put("views", snippet.getViewsLeft());
                    return object.toString();
                } else {
                    snippetsRepository.deleteById(id);
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public JSONArray getLatestSnippetsJson() {
        Optional<List<CodeSnippet>> snippetOptional = snippetsRepository.findAllBySecretFalseOrderByDateDesc();
        return snippetOptional.map(this::makeJsonArray).orElseGet(JSONArray::new);
    }

    public ArrayList<CodeSnippet> getLatestSnippetsWeb() {
        Optional<List<CodeSnippet>> snippetOptional = snippetsRepository.findAllBySecretFalseOrderByDateDesc();
        return snippetOptional.map(this::makeSnippetsArray).orElseGet(ArrayList::new);
    }

    private ArrayList<CodeSnippet> makeSnippetsArray(List<CodeSnippet> snippetList) {
        if (snippetList.size() <= 10) {
            return new ArrayList<>(snippetList);
        } else {
            return new ArrayList<>(snippetList.subList(0, 10));
        }
    }

    private JSONArray makeJsonArray(List<CodeSnippet> snippetList) {
        JSONArray jsonArray = new JSONArray();
        if (snippetList.size() <= 10) {
            for (CodeSnippet snippet: snippetList) {
                jsonArray.put(new JSONObject().put("code", snippet.getCode())
                        .put("date", snippet.getDate())
                        .put("time", 0)
                        .put("views", 0));
            }
        } else {
            for (int i = 0; i < 10; i++) {
                CodeSnippet snippet = snippetList.get(i);
                jsonArray.put(new JSONObject().put("code", snippet.getCode())
                        .put("date", snippet.getDate())
                        .put("time", 0)
                        .put("views", 0));
            }
        }
        return jsonArray;
    }

    public boolean decrementViewsLimit(CodeSnippet snippet) {
        if (snippet.isNotOriginalViews()) {
            snippet.decrementViews();
            snippetsRepository.save(snippet);
        }
        return snippet.getViewsLeft() > 0;
    }
}
