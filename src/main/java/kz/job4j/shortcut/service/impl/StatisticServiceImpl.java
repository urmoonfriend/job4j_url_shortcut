package kz.job4j.shortcut.service.impl;

import kz.job4j.shortcut.model.response.StatisticResponse;
import kz.job4j.shortcut.repository.StatisticRepository;
import kz.job4j.shortcut.repository.UrlRepository;
import kz.job4j.shortcut.service.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository urlCountRepository;
    private final UrlRepository urlRepository;

    @Override
    public List<StatisticResponse> getStatistic() {
        List<StatisticResponse> statisticResponses = new ArrayList<>();
        urlCountRepository.findAll().forEach(urlCount ->
                urlRepository.findById(urlCount.getUrlId()).ifPresent(
                        url -> statisticResponses.add(
                                new StatisticResponse()
                                        .setUrl(url.getUrl())
                                        .setCount(urlCount.getCount())
                        )

                )
        );
        return statisticResponses;
    }
}
