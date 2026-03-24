package com.maahir.researchLNK_backend.repository;

import com.maahir.researchLNK_backend.persistence.model.Article;
import com.maahir.researchLNK_backend.persistence.model.enums.EmbeddingStatus;
import com.maahir.researchLNK_backend.persistence.model.enums.Source;
import com.maahir.researchLNK_backend.persistence.repository.ArticleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg17");

    @Autowired
    private ArticleRepository articleRepository;

    @AfterEach
    public void clearDatabase() {
        articleRepository.deleteAll();
    }

    @Test
    public void testPersistArticle(){
        Article article = Article.builder()
                .source(Source.OPENALEX)
                .sourceId("testSrcId")
                .doi("testSrcDoi")
                .title("title")
                .academicAbstract("abstract")
                .publicationDate(LocalDate.now())
                .srcUpdatedDate(Instant.now())
                .articleType("articleType")
                .resolvedUrl("resolvedUrl")
                .pdfUrl("pdfUrl")
                .isOpenAccess(true)
                .primaryTopic("topic")
                .embeddingStatus(EmbeddingStatus.PENDING)
                .build();

        article = articleRepository.save(article);

        Long id = article.getId();

        Optional<Article> articleOptional = articleRepository.findById(id);

        assertThat(articleOptional).isPresent();
        assertThat(articleOptional.get().getId()).isEqualTo(article.getId());
        assertThat(articleOptional.get().getSource()).isEqualTo(article.getSource());
        assertThat(articleOptional.get().getSourceId()).isEqualTo(article.getSourceId());
        assertThat(articleOptional.get().getDoi()).isEqualTo(article.getDoi());
        assertThat(articleOptional.get().getTitle()).isEqualTo(article.getTitle());
        assertThat(articleOptional.get().getAcademicAbstract()).isEqualTo(article.getAcademicAbstract());
        assertThat(articleOptional.get().getPublicationDate()).isEqualTo(article.getPublicationDate());
        assertThat(articleOptional.get().getSrcUpdatedDate()).isEqualTo(article.getSrcUpdatedDate());
        assertThat(articleOptional.get().getArticleType()).isEqualTo(article.getArticleType());
        assertThat(articleOptional.get().getResolvedUrl()).isEqualTo(article.getResolvedUrl());
        assertThat(articleOptional.get().getPdfUrl()).isEqualTo(article.getPdfUrl());
        assertThat(articleOptional.get().getIsOpenAccess()).isEqualTo(article.getIsOpenAccess());
        assertThat(articleOptional.get().getPrimaryTopic()).isEqualTo(article.getPrimaryTopic());
        assertThat(articleOptional.get().getEmbeddingStatus()).isEqualTo(article.getEmbeddingStatus());
        assertThat(articleOptional.get().getCreatedAt()).isNotNull();
        assertThat(articleOptional.get().getUpdatedAt()).isNotNull();
    }
}
