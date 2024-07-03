package kz.job4j.shortcut.service.impl;

import kz.job4j.shortcut.model.Site;
import kz.job4j.shortcut.model.Statistic;
import kz.job4j.shortcut.model.Url;
import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.dto.UrlDto;
import kz.job4j.shortcut.repository.SiteRepository;
import kz.job4j.shortcut.repository.StatisticRepository;
import kz.job4j.shortcut.repository.UrlRepository;
import kz.job4j.shortcut.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UrlServiceImplTest {

    @Autowired
    private UrlService urlService;

    @MockBean
    private UrlRepository urlRepository;

    @MockBean
    private SiteRepository siteRepository;

    @MockBean
    private StatisticRepository statisticRepository;

    @Test
    public void whenFindByIdAnIncreaseCountThenOK() {
        UUID id = UUID.randomUUID();
        Url url = new Url().setUrl("someId");
        when(urlRepository.findById(any()))
                .thenReturn(Optional.of(url));

        ResultMessage<UrlDto> result = urlService.findByIdAndIncreaseCount(id.toString());

        assertThat(result).isEqualTo(ResultMessage.success(new UrlDto().setUrl("someId")));
    }

    @Test
    public void whenFindByUrlAnIncreaseCountThenNotFound() {
        UUID id = UUID.randomUUID();
        when(urlRepository.findById(any()))
                .thenReturn(Optional.empty());
        ResultMessage<UrlDto> result = urlService.findByIdAndIncreaseCount(id.toString());
        assertThat(result).isEqualTo(ResultMessage.failure("Url not found"));
    }

    @Test
    public void whenCreateThenOk() {
        String correctUrl = "https://www.google.com";
        String siteName = "www.google.com";
        Site site = new Site()
                .setId(1L)
                .setUsername("login")
                .setPassword("password")
                .setSiteName(siteName);
        Url url = new Url().setUrl(correctUrl);
        Url expected = new Url().setUrl(correctUrl);

        when(urlRepository.findByUrl(correctUrl)).thenReturn(Optional.empty());
        when(siteRepository.findBySiteName(siteName)).thenReturn(Optional.of(site));
        when(urlRepository.save(any(Url.class))).thenReturn(expected);
        when(statisticRepository.save(any())).thenReturn(new Statistic());
        Optional<ResultMessage<Url>> result = urlService.create(url);
        assertThat(result).isEqualTo(Optional.of(ResultMessage.success(expected)));
    }

    @Test
    public void whenCreateThenUrlAlreadyExists() {
        String correctUrl = "https://www.google.com";
        Url url = new Url().setUrl(correctUrl);
        Url expected = new Url().setUrl(correctUrl);
        when(urlRepository.findByUrl(correctUrl)).thenReturn(Optional.of(expected));
        Optional<ResultMessage<Url>> result = urlService.create(url);
        assertThat(result).isEqualTo(Optional.of(ResultMessage.failure("Url уже существует")));
    }

    @Test
    public void whenCreateThenSiteNotFound() {
        String correctUrl = "https://www.google.com";
        String siteName = "www.google.com";
        Url url = new Url().setUrl(correctUrl);
        when(urlRepository.findByUrl(correctUrl)).thenReturn(Optional.empty());
        when(siteRepository.findBySiteName(siteName)).thenReturn(Optional.empty());
        Optional<ResultMessage<Url>> result = urlService.create(url);
        assertThat(result).isEqualTo(Optional.of(ResultMessage.failure("Site не найден")));
    }

    @Test
    public void whenCreateThenServerError() {
        String correctUrl = "https://www.google.com";
        Url url = new Url().setUrl(correctUrl);
        when(urlRepository.findByUrl(correctUrl)).thenThrow(RuntimeException.class);
        Optional<ResultMessage<Url>> result = urlService.create(url);
        assertThat(result).isEqualTo(Optional.of(ResultMessage.failure("Ошибка сервера")));
    }
}
