package kz.job4j.shortcut.repository;

import kz.job4j.shortcut.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Long> {
    Optional<Site> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Site> findBySiteName(String site);
}
