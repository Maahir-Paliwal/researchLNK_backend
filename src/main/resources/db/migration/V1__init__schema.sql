create extension if not exists vector;

CREATE TABLE article_ingestion_runs
(
    id                      BIGINT AUTO_INCREMENT NOT NULL,
    source                  VARCHAR(255)          NOT NULL,
    ingestion_status        VARCHAR(255)          NOT NULL,
    started_at              datetime              NOT NULL,
    finished_at             datetime              NULL,
    from_publication_date   datetime              NULL,
    next_cursor             VARCHAR(255)          NULL,
    articles_seen_count     BIGINT                NOT NULL,
    articles_inserted_count BIGINT                NOT NULL,
    articles_updated_count  BIGINT                NOT NULL,
    articles_failed_count   BIGINT                NOT NULL,
    error_message           TEXT                  NULL,
    created_at              datetime              NOT NULL,
    updated_at              datetime              NULL,
    CONSTRAINT pk_article_ingestion_runs PRIMARY KEY (id)
);

CREATE TABLE articles
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    article_vector VECTOR(384) NULL,
    source           VARCHAR(255)          NULL,
    source_id        VARCHAR(255)          NULL,
    doi              VARCHAR(255)          NULL,
    title            TEXT                  NULL,
    abstract         TEXT                  NULL,
    publication_date date                  NULL,
    src_updated_date datetime              NULL,
    article_type     VARCHAR(255)          NULL,
    resolved_url     VARCHAR(255)          NULL,
    pdf_url          VARCHAR(255)          NULL,
    is_open_access   BIT(1)                NULL,
    primary_topic    VARCHAR(255)          NULL,
    embedding_status VARCHAR(255)          NULL,
    created_at       datetime              NOT NULL,
    updated_at       datetime              NOT NULL,
    authors_json JSONB NULL,
    topics_json JSONB NULL,
    raw_payload_json JSONB NULL,
    CONSTRAINT pk_articles PRIMARY KEY (id)
);

CREATE TABLE connections
(
    status       VARCHAR(255) NOT NULL,
    created_at   datetime     NOT NULL,
    updated_at   datetime     NOT NULL,
    requester_id BIGINT       NOT NULL,
    user_low_id  BIGINT       NOT NULL,
    user_high_id BIGINT       NOT NULL,
    CONSTRAINT pk_connections PRIMARY KEY (user_low_id, user_high_id)
);

CREATE TABLE conversation_participants
(
    joined_at       datetime NOT NULL,
    last_read_at    datetime NULL,
    user_id         BIGINT   NOT NULL,
    conversation_id BIGINT   NOT NULL,
    CONSTRAINT pk_conversation_participants PRIMARY KEY (user_id, conversation_id)
);

CREATE TABLE conversations
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    title      VARCHAR(255)          NULL,
    created_at datetime              NOT NULL,
    CONSTRAINT pk_conversations PRIMARY KEY (id)
);

CREATE TABLE messages
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    content         TEXT                  NOT NULL,
    created_at      datetime              NOT NULL,
    conversation_id BIGINT                NOT NULL,
    user_id         BIGINT                NOT NULL,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE personal_publications
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    content          TEXT                  NULL,
    title            TEXT                  NULL,
    authors          TEXT                  NULL,
    publication_type VARCHAR(255)          NULL,
    profile_id       BIGINT                NOT NULL,
    CONSTRAINT pk_personal_publications PRIMARY KEY (id)
);

CREATE TABLE profiles
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    summary             TEXT                  NULL,
    institution         VARCHAR(255)          NULL,
    profile_picture_key VARCHAR(255)          NULL,
    position            VARCHAR(255)          NULL,
    user_id             BIGINT                NOT NULL,
    CONSTRAINT pk_profiles PRIMARY KEY (id)
);

CREATE TABLE swipe_cards
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    profile_id BIGINT                NOT NULL,
    CONSTRAINT pk_swipe_cards PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    user_name     VARCHAR(255)          NOT NULL,
    email         VARCHAR(255)          NOT NULL,
    password_hash VARCHAR(255)          NOT NULL,
    `role`        VARCHAR(255)          NOT NULL,
    is_active     BIT(1)                NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    orcid_id      VARCHAR(255)          NULL,
    created_at    datetime              NOT NULL,
    user_vector VECTOR(384) NULL,
    impression_vector VECTOR(384) NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_liked_articles
(
    article_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    CONSTRAINT pk_users_liked_articles PRIMARY KEY (article_id, user_id)
);

ALTER TABLE articles
    ADD CONSTRAINT uc_articles_source_source_id UNIQUE (source, source_id);

ALTER TABLE profiles
    ADD CONSTRAINT uc_profiles_user UNIQUE (user_id);

ALTER TABLE swipe_cards
    ADD CONSTRAINT uc_swipe_cards_profile UNIQUE (profile_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_user_name UNIQUE (user_name);

ALTER TABLE connections
    ADD CONSTRAINT FK_CONNECTIONS_ON_REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id);

ALTER TABLE connections
    ADD CONSTRAINT FK_CONNECTIONS_ON_USER_HIGH FOREIGN KEY (user_high_id) REFERENCES users (id);

ALTER TABLE connections
    ADD CONSTRAINT FK_CONNECTIONS_ON_USER_LOW FOREIGN KEY (user_low_id) REFERENCES users (id);

ALTER TABLE conversation_participants
    ADD CONSTRAINT FK_CONVERSATION_PARTICIPANTS_ON_CONVERSATION FOREIGN KEY (conversation_id) REFERENCES conversations (id);

ALTER TABLE conversation_participants
    ADD CONSTRAINT FK_CONVERSATION_PARTICIPANTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_CONVERSATION FOREIGN KEY (conversation_id) REFERENCES conversations (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE personal_publications
    ADD CONSTRAINT FK_PERSONAL_PUBLICATIONS_ON_PROFILE FOREIGN KEY (profile_id) REFERENCES profiles (id);

ALTER TABLE profiles
    ADD CONSTRAINT FK_PROFILES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE swipe_cards
    ADD CONSTRAINT FK_SWIPE_CARDS_ON_PROFILE FOREIGN KEY (profile_id) REFERENCES profiles (id);

ALTER TABLE users_liked_articles
    ADD CONSTRAINT fk_uselikart_on_article FOREIGN KEY (article_id) REFERENCES articles (id);

ALTER TABLE users_liked_articles
    ADD CONSTRAINT fk_uselikart_on_user FOREIGN KEY (user_id) REFERENCES users (id);

CREATE INDEX idx_users_user_vector_hnsw
    ON users
    USING hnsw(user_vector vector_cosine_ops);

CREATE INDEX idx_articles_article_vector_hnsw
    ON articles
    USING hnsw(article_vector vector_cosine_ops);