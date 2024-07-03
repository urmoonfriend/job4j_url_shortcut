package kz.job4j.shortcut.service.impl;

import kz.job4j.shortcut.exceptions.SiteAlreadyExistsException;
import kz.job4j.shortcut.exceptions.SiteNotFoundException;
import kz.job4j.shortcut.exceptions.UsernameAlreadyExistsException;
import kz.job4j.shortcut.model.Site;
import kz.job4j.shortcut.repository.SiteRepository;
import kz.job4j.shortcut.service.SiteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SiteServiceImplTest {

    @Autowired
    private SiteService siteService;

    private Site site;

    @BeforeEach
    public void setUp() {
        site = new Site();
    }

    @MockBean
    private SiteRepository siteRepository;

    @Test
    public void whenSaveThenOk() {
        when(siteRepository.save(any())).thenReturn(site);
        Site result = siteService.save(site);
        assertThat(result).isEqualTo(site);
    }

    @Test
    public void whenFindBySiteNameThenOk() {
        when(siteRepository.findBySiteName(any())).thenReturn(Optional.of(site));
        Optional<Site> result = siteService.findBySiteName("siteName");
        assertThat(result).isEqualTo(Optional.of(site));
    }

    @Test
    public void whenFindBySiteNameThenNotFound() {
        when(siteRepository.findBySiteName(any())).thenReturn(Optional.empty());
        Optional<Site> result = siteService.findBySiteName("siteName");
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void whenCreateThenOk() {
        when(siteRepository.existsByUsername(any())).thenReturn(false);
        when(siteRepository.findBySiteName(any())).thenReturn(Optional.empty());
        when(siteRepository.save(any())).thenReturn(site);
        Site result = siteService.create(site);
        assertThat(result).isEqualTo(site);
    }

    @Test
    public void whenCreateThenUsernameAlreadyExists() {
        when(siteRepository.existsByUsername(any())).thenReturn(true);
        when(siteRepository.findBySiteName(any())).thenReturn(Optional.empty());
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            siteService.create(new Site().setUsername("existingUsername").setSiteName("newSite"));
        });

        verify(siteRepository, times(1)).existsByUsername(anyString());
        verify(siteRepository, times(0)).findBySiteName(anyString());
        verify(siteRepository, times(0)).save(any(Site.class));
    }

    @Test
    public void whenCreateThenSiteAlreadyExistsException() {
        when(siteRepository.existsByUsername(any())).thenReturn(false);
        when(siteRepository.findBySiteName(any())).thenReturn(Optional.of(site));
        assertThrows(SiteAlreadyExistsException.class, () -> {
            siteService.create(new Site().setUsername("existingUsername").setSiteName("newSite"));
        });

        verify(siteRepository, times(1)).existsByUsername(anyString());
        verify(siteRepository, times(1)).findBySiteName(anyString());
        verify(siteRepository, times(0)).save(any(Site.class));
    }

    @Test
    public void whenGetByUsernameThenOk() {
        when(siteRepository.findByUsername(any())).thenReturn(Optional.of(site));
        Site result = siteService.getByUsername("siteName");
        assertThat(result).isEqualTo(site);
    }

    @Test
    public void whenGetByUsernameThenNotFound() {
        when(siteRepository.findBySiteName(any())).thenReturn(Optional.empty());
        assertThrows(SiteNotFoundException.class, () -> siteService.getByUsername("siteName"));
    }
}
