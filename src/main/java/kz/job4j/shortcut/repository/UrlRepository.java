package kz.job4j.shortcut.repository;

import kz.job4j.shortcut.model.Site;
import kz.job4j.shortcut.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UrlRepository extends JpaRepository<Url, UUID> {
    List<Url> findBySite(Site site);

    Optional<Url> findByUrl(String url);
}
