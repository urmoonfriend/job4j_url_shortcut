package kz.job4j.shortcut.service;

import kz.job4j.shortcut.model.Url;
import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.dto.UrlDto;

public interface UrlService {

    ResultMessage<UrlDto> findByIdAndIncreaseCount(String id);

    ResultMessage<Url> create(Url url);

}
