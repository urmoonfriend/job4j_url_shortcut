package kz.job4j.shortcut.controller;

import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.response.StatisticResponse;
import kz.job4j.shortcut.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping()
    public ResponseEntity<ResultMessage<List<StatisticResponse>>> getStatistics() {
        return ResponseEntity.ok(ResultMessage.success(statisticService.getStatistic()));
    }
}
