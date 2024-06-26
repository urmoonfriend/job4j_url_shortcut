package kz.job4j.shortcut.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity(name = "urls")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "url", unique = true, nullable = false)
    private String url;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
