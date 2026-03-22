package com.maahir.researchLNK_backend.persistence.model;

import com.maahir.researchLNK_backend.persistence.model.enums.IngestionStatus;
import com.maahir.researchLNK_backend.persistence.model.enums.Source;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "article_ingestion_runs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleIngestionRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="source", nullable = false)
    private Source source;

    @Enumerated(EnumType.STRING)
    @Column(name="ingestion_status", nullable = false)
    private IngestionStatus ingestionStatus;

    @Column(name="started_at", nullable = false)
    private Instant startedAt;

    @Column(name="finished_at")
    private Instant finishedAt;

    @Column(name="from_publication_date")
    private Date fromPublicationDate;

    @Column(name="next_cursor")
    private String nextCursor;

    @Column(name="articles_seen_count", nullable = false)
    private Long articlesSeenCount;

    @Column(name="articles_inserted_count", nullable = false)
    private Long articlesInsertedCount;

    @Column(name="articles_updated_count", nullable = false)
    private Long articlesUpdatedCount;

    @Column(name="articles_failed_count", nullable = false)
    private Long articlesFailedCount;

    @Column(name="error_message")
    private String errorMessage;

    @Column(name="created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

}
