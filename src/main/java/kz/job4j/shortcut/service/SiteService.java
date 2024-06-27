package kz.job4j.shortcut.service;

import kz.job4j.shortcut.model.Site;
import kz.job4j.shortcut.model.dto.SignUpDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface SiteService {
    Site save(Site site);

    Optional<Site> findBySiteName(String site);

    Site create(Site site);

    Site create(SignUpDto request);

    Site getByUsername(String username);

    UserDetailsService userDetailsService();

}
