package kz.job4j.shortcut.service.impl;

import kz.job4j.shortcut.model.Site;
import kz.job4j.shortcut.model.Statistic;
import kz.job4j.shortcut.model.Url;
import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.dto.UrlDto;
import kz.job4j.shortcut.repository.StatisticRepository;
import kz.job4j.shortcut.repository.UrlRepository;
import kz.job4j.shortcut.service.SiteService;
import kz.job4j.shortcut.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlServiceImpl implements UrlService {
    private final SiteService siteService;
    private final UrlRepository urlRepository;
    private final StatisticRepository statisticRepository;

    @Override
    @Transactional
    public ResultMessage<UrlDto> findByIdAndIncreaseCount(String id) {
        ResultMessage<UrlDto> result = ResultMessage.failure("Url not found");
        var urlOptional = urlRepository.findById(UUID.fromString(id));
        if (urlOptional.isPresent()) {
            statisticRepository.updateCount(urlOptional.get().getId());
            result = ResultMessage.success(new UrlDto().setUrl(urlOptional.get().getUrl()));
        }
        return result;
    }

    /**
     * Создание url
     *
     * @return созданный url
     */
    @Override
    @Transactional
    public Optional<ResultMessage<Url>> create(Url url) {
        ResultMessage<Url> result = ResultMessage.failure("Ошибка сервера");
        try {
            if (urlRepository.findByUrl(url.getUrl()).isPresent()) {
                result = ResultMessage.failure("Url уже существует");
            } else {
                ResultMessage<String> extractDomainResult = extractDomain(url);
                if (extractDomainResult.isSuccess()) {
                    Optional<Site> siteOptional =
                            siteService.findBySiteName(extractDomainResult.getMessage());
                    if (siteOptional.isPresent()) {
                        url.setSite(siteOptional.get());
                        Url savedUrl = urlRepository.save(url);
                        statisticRepository.save(
                                new Statistic()
                                        .setUrlId(savedUrl.getId())
                                        .setCount(0));
                        result = ResultMessage.success(savedUrl);
                    } else {
                        result = ResultMessage.failure("Site не найден");
                    }
                } else {
                    result = ResultMessage.failure(extractDomainResult.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("create method error: [{}]", e.getMessage(), e);
        }
        return Optional.of(result);
    }

    protected ResultMessage<String> extractDomain(Url url) {
        try {
            URL parsedUrl = new URL(url.getUrl());
            return ResultMessage.success(parsedUrl.getHost());
        } catch (Exception e) {
            return ResultMessage.failure("Некорректный Url");
        }
    }
}