package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import platform.model.CodeSnippet;
import platform.service.CodeSnippetService;

import java.util.ArrayList;

@Controller
public class WebController {
    private final CodeSnippetService codeService;

    @Autowired
    public WebController(CodeSnippetService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("code/new")
    public String getCodeNew() {
        return "create";
    }

    @GetMapping("code/{id}")
    public String getCodeById(@PathVariable String id, Model model) {
        CodeSnippet snippet = codeService.getCodeSnippetById(id);
        model.addAttribute("snippet", snippet);
        return "code";
    }

    @GetMapping("code/latest")
    public String getLatestSnippets(Model model) {
        ArrayList<CodeSnippet> snippets = codeService.getLatestSnippetsWeb();
        model.addAttribute("snippets", snippets);
        return "latest";
    }
}
