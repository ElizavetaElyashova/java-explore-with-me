package ru.practicum.ewm.stats;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@RequestBody EndpointHitDto endpointHit) {
        statsService.saveHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam String start, @RequestParam String end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }
}
