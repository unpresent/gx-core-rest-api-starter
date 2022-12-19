package ru.gx.core.api.standard_rests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/standard")
public class StandardRestExitController {
    @PostMapping("/exit")
    public void exit(
            @RequestParam(name = "code", required = false, defaultValue = "0") @Nullable Integer code
    ) {
        final var exitCode = code == null ? 0 : code;
        log.info(String.format("START /exit(%d)", exitCode));
        System.exit(exitCode);
    }
}
