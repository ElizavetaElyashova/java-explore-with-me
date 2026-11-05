package ru.practicum.ewm.stats.service;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.StatsMapper;
import ru.practicum.ewm.stats.StatsRepository;
import ru.practicum.ewm.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void saveHit(EndpointHitDto endpointHitDto) {
        statsRepository.save(StatsMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startDateTime = LocalDateTime.parse(start, StatsMapper.getFormatter());
        LocalDateTime endDateTime = LocalDateTime.parse(end, StatsMapper.getFormatter());
        List<ViewStats> stats;
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                stats = statsRepository.getStatsUniqueIpsForAll(startDateTime, endDateTime);
            } else {
                stats = statsRepository.getStatsUniqueIps(startDateTime, endDateTime, uris);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                stats = statsRepository.getStatsForAll(startDateTime, endDateTime);
            } else {
                stats = statsRepository.getStats(startDateTime, endDateTime, uris);
            }
        }
        return stats.stream()
                .map(StatsMapper::toViewStatsDto)
                .sorted(Comparator.comparing(ViewStatsDto::getHits).reversed())
                .toList();
    }
}
