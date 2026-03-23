package com.maahir.researchLNK_backend.persistence.model;


import com.fasterxml.jackson.databind.JsonNode;
import com.maahir.researchLNK_backend.persistence.model.enums.EmbeddingStatus;
import com.maahir.researchLNK_backend.persistence.model.enums.Source;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "articles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_articles_source_source_id", columnNames = {"source", "source_id"})
        }
)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "article_vector", columnDefinition = "vector(384)")
    @JdbcTypeCode(SqlTypes.VECTOR)
    private float[] articleVector;

    @Enumerated(EnumType.STRING)
    @Column(name="source")
    private Source source;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name="doi")
    private String doi;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "abstract", columnDefinition = "TEXT")
    private String academicAbstract;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "src_updated_date")
    private Instant srcUpdatedDate;

    @Column(name="article_type")
    private String articleType;

    @Column(name="resolved_url")
    private String resolvedUrl;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name="is_open_access")
    private Boolean isOpenAccess;

    @Column(name="primary_topic")
    private String primaryTopic;

    @Enumerated(EnumType.STRING)
    @Column(name="embedding_status")
    private EmbeddingStatus embeddingStatus;

    @Column(name="created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    // TODO: Define strict POJOs for authors and topics when ingestion dev
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="authors_json", columnDefinition = "jsonb")
    private JsonNode authorsJson;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="topics_json", columnDefinition = "jsonb")
    private JsonNode topicsJson;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="raw_payload_json", columnDefinition = "jsonb")
    private JsonNode rawPayloadJson;

    @ManyToMany(mappedBy="likedArticles")
    private Set<User> users;
}
