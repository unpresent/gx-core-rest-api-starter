package ru.gx.core.api.standard_rests;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gx.core.longtime.LongtimeProcess;
import ru.gx.core.longtime.LongtimeProcessCache;

import java.util.UUID;

@RestController
@RequestMapping(path = "/longtime-process")
@RequiredArgsConstructor
@Tag(name = "Контроллер для отслеживания выполнения длительных процессов.")
public class LongtimeProcessController {

    private final LongtimeProcessCache longtimeProcessCache;

    @GetMapping("/get")
    @Operation(summary = "Получить состояние процесса по id.")
    public LongtimeProcess getLongtimeProcess(
            @Parameter(description = "Id длительного процесса.")
            @RequestParam(name = "longtimeProcessId") final UUID longtimeProcessId) {
        return longtimeProcessCache.get(longtimeProcessId);
    }
}
