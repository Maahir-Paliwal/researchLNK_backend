package com.maahir.researchLNK_backend.repository;

import com.maahir.researchLNK_backend.persistence.model.ArticleIngestionRun;
import com.maahir.researchLNK_backend.persistence.model.enums.IngestionStatus;
import com.maahir.researchLNK_backend.persistence.model.enums.Source;
import com.maahir.researchLNK_backend.persistence.repository.ArticleIngestionRunRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleIngestionRunRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg17");

    @Autowired
    private ArticleIngestionRunRepository articleIngestionRunRepository;

    @AfterEach
    public void clearDatabase() {
        articleIngestionRunRepository.deleteAll();
    }

    @Test
    public void testPersistArticleIngestionRun(){
        ArticleIngestionRun articleIngestionRun =
                ArticleIngestionRun.builder()
                        .source(Source.OPENALEX)
                        .ingestionStatus(IngestionStatus.RUNNING)
                        .startedAt(Instant.now())
                        .nextCursor("nextCursor")
                        .articlesSeenCount(100L)
                        .articlesInsertedCount(100L)
                        .articlesUpdatedCount(100L)
                        .articlesFailedCount(100L)
                .build();

        articleIngestionRun = articleIngestionRunRepository.save(articleIngestionRun);

        Long id = articleIngestionRun.getId();

        Optional<ArticleIngestionRun> articleIngestionRunOptional = articleIngestionRunRepository.findById(id);

        assertThat(articleIngestionRunOptional).isPresent();
        assertThat(articleIngestionRunOptional.get().getId()).isEqualTo(articleIngestionRun.getId());
        assertThat(articleIngestionRunOptional.get().getSource()).isEqualTo(articleIngestionRun.getSource());
        assertThat(articleIngestionRunOptional.get().getIngestionStatus()).isEqualTo(articleIngestionRun.getIngestionStatus());
        assertThat(articleIngestionRunOptional.get().getStartedAt()).isEqualTo(articleIngestionRun.getStartedAt());
        assertThat(articleIngestionRunOptional.get().getNextCursor()).isEqualTo(articleIngestionRun.getNextCursor());
        assertThat(articleIngestionRunOptional.get().getArticlesSeenCount()).isEqualTo(articleIngestionRun.getArticlesSeenCount());
        assertThat(articleIngestionRunOptional.get().getArticlesInsertedCount()).isEqualTo(articleIngestionRun.getArticlesInsertedCount());
        assertThat(articleIngestionRunOptional.get().getArticlesUpdatedCount()).isEqualTo(articleIngestionRun.getArticlesUpdatedCount());
        assertThat(articleIngestionRunOptional.get().getArticlesFailedCount()).isEqualTo(articleIngestionRun.getArticlesFailedCount());
        assertThat(articleIngestionRunOptional.get().getCreatedAt()).isNotNull();
        assertThat(articleIngestionRunOptional.get().getUpdatedAt()).isNotNull();
    }
}
