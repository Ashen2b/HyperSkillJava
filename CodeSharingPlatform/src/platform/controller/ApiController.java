package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import platform.model.CodeSnippet;
import platform.service.CodeSnippetService;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final CodeSnippetService codeService;

    @Autowired
    public ApiController(CodeSnippetService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("code/{id}")
    public String getCodeJson(@PathVariable String id) {
        return codeService.getCodeSnippetByIdJson(id);
    }

    @GetMapping("code/latest")
    public String getLatestSnippets() {
        return codeService.getLatestSnippetsJson().toString();
    }

    @PostMapping(value = "code/new", consumes = "application/json")
    public String addSnippet(@RequestBody CodeSnippet code) {
        return codeService.addSnippet(code);
    }
}
