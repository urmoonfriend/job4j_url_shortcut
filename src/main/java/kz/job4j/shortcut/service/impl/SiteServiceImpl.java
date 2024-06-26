package kz.job4j.shortcut.service.impl;

import kz.job4j.shortcut.enums.Role;
import kz.job4j.shortcut.model.Site;
import kz.job4j.shortcut.model.request.SignUpRequest;
import kz.job4j.shortcut.repository.SiteRepository;
import kz.job4j.shortcut.service.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            throw new RuntimeException("Сайт с таким username уже существует");
        }

        if (repository.findBySiteName(site.getSiteName()).isPresent()) {
            throw new RuntimeException("Сайт уже существует");
        }

        return save(site);
    }

    @Override
    public Site create(SignUpRequest request) {
        return create(new Site()
                .setUsername(request.getUsername())
                .setPassword(request.getPassword())
                .setSiteName(request.getSite())
                .setRole(request.getRole() != null
                        ? request.getRole() : Role.ROLE_USER));
    }

    /**
     * Получение сайта по имени пользователя
     *
     * @return сайт
     */
    @Override
    public Site getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Site не найден"));
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

    /**
     * Получение текущего сайта
     *
     * @return текущий сайт
     */
    @Override
    public Site getCurrentSite() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    /**
     * Выдача прав администратора текущему сайту
     * <p>
     */
    @Deprecated
    @Override
    public void getAdmin() {
        var user = getCurrentSite();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }

}