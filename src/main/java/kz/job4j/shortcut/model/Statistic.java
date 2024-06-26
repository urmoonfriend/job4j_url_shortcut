package kz.job4j.shortcut.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "statistic")
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID urlId;

    @Column(nullable = false)
    private Integer count = 0;
}
