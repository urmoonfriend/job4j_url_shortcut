package kz.job4j.shortcut.repository;

import kz.job4j.shortcut.model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Modifying
    @Transactional
    @Query(" update Statistic s set s.count = s.count + 1 "
            + " where s.urlId = :urlId ")
    void updateCount(@Param("urlId") UUID urlId);
}
