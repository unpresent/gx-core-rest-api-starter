package ru.gx.core.api.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Скорее всего нужно будет убрать!
 */
@SuppressWarnings("unused")
@RestController
public class RedirectController {
    @GetMapping("/swagger-ui")
    public String swagger() {
        return "redirect:swagger-ui.html";
    }
}
