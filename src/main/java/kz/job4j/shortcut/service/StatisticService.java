package kz.job4j.shortcut.service;

import kz.job4j.shortcut.model.response.StatisticResponse;

import java.util.List;

public interface StatisticService {
    List<StatisticResponse> getStatistic();
}
