package ru.gx.core.rest.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Скорее всего нужно будет убрать!
 */
@Controller
public class RedirectController {
    @GetMapping("/")
    public String forwardHome() {
        return "index.html";
    }

    @GetMapping("/swagger-ui")
    public String swagger() {
        return "redirect:swagger-ui.html";
    }

    @GetMapping(value = {
            "/{path:[^.]*}",
            "/{path:[^.]*}/{path1:[^.]*}",
            "/{path:[^.]*}/{path1:[^.]*}/{path2:[^.]*}",
            "/{path:[^.]*}/{path1:[^.]*}/{path2:[^.]*}/{path3:[^.]*}",
            "/{path:[^.]*}/{path1:[^.]*}/{path2:[^.]*}/{path3:[^.]*}/{path4:[^.]*}",
            "/{path:[^.]*}/{path1:[^.]*}/{path2:[^.]*}/{path3:[^.]*}/{path4:[^.]*}/{path5:[^.]*}",
            "/{path:[^.]*}/{path1:[^.]*}/{path2:[^.]*}/{path3:[^.]*}/{path4:[^.]*}/{path5:[^.]*}/{path6:[^.]*}"
            // add more if required ...
    })
    public String forwardAll() {
        return "forward:/";
    }
}
