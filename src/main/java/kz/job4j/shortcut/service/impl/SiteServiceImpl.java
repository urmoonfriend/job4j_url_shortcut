package kz.job4j.shortcut.service.impl;

import kz.job4j.shortcut.exceptions.SiteAlreadyExistsException;
import kz.job4j.shortcut.exceptions.SiteNotFoundException;
import kz.job4j.shortcut.exceptions.UsernameAlreadyExistsException;
import kz.job4j.shortcut.model.Site;
import kz.job4j.shortcut.model.dto.SignUpDto;
import kz.job4j.shortcut.repository.SiteRepository;
import kz.job4j.shortcut.service.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {
    private final SiteRepository repository;

    /**
     * Сохранение сайта
     *
     * @return сохраненный сайт
     */
    @Override
    public Site save(Site site) {
        return repository.save(site);
    }

    @Override
    public Optional<Site> findBySiteName(String site) {
        return repository.findBySiteName(site);
    }

    /**
     * Создание сайта
     *
     * @return созданный сайт
     */

    @Override
    public Site create(Site site) {
        if (repository.existsByUsername(site.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        if (repository.findBySiteName(site.getSiteName()).isPresent()) {
            throw new SiteAlreadyExistsException();
        }

        return save(site);
    }

    @Override
    public Site create(SignUpDto request) {
        return create(new Site()
                .setUsername(request.getUsername())
                .setPassword(request.getPassword())
                .setSiteName(request.getSite()));
    }

    /**
     * Получение сайта по имени пользователя
     *
     * @return сайт
     */
    @Override
    public Site getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(SiteNotFoundException::new);
    }

    /**
     * Получение сайта по имени
     * <p>
     * Нужен для Spring Security
     *
     * @return сайт
     */
    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

}