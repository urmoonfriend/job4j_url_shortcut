package kz.job4j.shortcut.service.impl;

import kz.job4j.shortcut.model.Site;
import kz.job4j.shortcut.model.Statistic;
import kz.job4j.shortcut.model.Url;
import kz.job4j.shortcut.model.response.StatisticResponse;
import kz.job4j.shortcut.repository.StatisticRepository;
import kz.job4j.shortcut.repository.UrlRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatisticServiceImplTest {

    @Autowired
    private StatisticServiceImpl statisticService;

    @MockBean
    private StatisticRepository statisticRepository;

    @MockBean
    private UrlRepository urlRepository;

    private List<Statistic> statistics;

    private List<Url> urls;

    private List<StatisticResponse> expected;

    @BeforeAll
    public void setup() {
        expected = List.of(new StatisticResponse()
                        .setUrl("https://www.google.com")
                        .setCount(5),
                new StatisticResponse()
                        .setUrl("https://yandex.ru")
                        .setCount(33),
                new StatisticResponse()
                        .setUrl("https://mail.ru")
                        .setCount(50));

        statistics = List.of(new Statistic()
                        .setId(1L)
                        .setUrlId(UUID.fromString("ba0cd0ef-1cb7-488a-bfec-1094c4d80bba"))
                        .setCount(5),
                new Statistic()
                        .setId(2L)
                        .setUrlId(UUID.fromString("04ab42e0-96dd-4b1d-a2a2-ef74f6195c5b"))
                        .setCount(33),
                new Statistic()
                        .setId(3L)
                        .setUrlId(UUID.fromString("bb2355fd-7c2d-4914-841d-f43d5e6b6017"))
                        .setCount(50));

        urls = List.of(new Url()
                        .setId(UUID.fromString("ba0cd0ef-1cb7-488a-bfec-1094c4d80bba"))
                        .setUrl("https://www.google.com")
                        .setSite(new Site().setSiteName("www.google.com"))
                        .setCreatedAt(LocalDateTime.now()),
                new Url()
                        .setId(UUID.fromString("04ab42e0-96dd-4b1d-a2a2-ef74f6195c5b"))
                        .setUrl("https://yandex.ru")
                        .setSite(new Site().setSiteName("yandex.ru"))
                        .setCreatedAt(LocalDateTime.now()),
                new Url()
                        .setId(UUID.fromString("bb2355fd-7c2d-4914-841d-f43d5e6b6017"))
                        .setUrl("https://mail.ru")
                        .setSite(new Site().setSiteName("mail.ru"))
                        .setCreatedAt(LocalDateTime.now()));
    }

    @Test
    public void whenGetStatisticThenOk() {
        when(statisticRepository.findAll()).thenReturn(statistics);
        urls.forEach(url -> when(urlRepository.findById(url.getId())).thenReturn(Optional.of(url)));
        List<StatisticResponse> result = statisticService.getStatistic();
        assertThat(result).isEqualTo(expected);
    }
}
