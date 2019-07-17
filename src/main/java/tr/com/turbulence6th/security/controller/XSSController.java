package tr.com.turbulence6th.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class XSSController {

    private String name;

    @PostMapping("/xss/save")
    public String save(String name) {
        this.name = name;
        return "Saved your name";
    }

    @GetMapping("/xss/get")
    public String get() {
        return name;
    }
}
