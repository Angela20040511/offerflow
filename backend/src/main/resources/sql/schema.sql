DROP TABLE IF EXISTS interview_record;
DROP TABLE IF EXISTS favorite_job;
DROP TABLE IF EXISTS application_record;
DROP TABLE IF EXISTS resume;
DROP TABLE IF EXISTS guide_doc;
DROP TABLE IF EXISTS job; 
DROP TABLE IF EXISTS region_city;
DROP TABLE IF EXISTS region_province;
DROP TABLE IF EXISTS job_category;
DROP TABLE IF EXISTS subsidiary_company;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    username    VARCHAR(64)  NOT NULL UNIQUE,
    password    VARCHAR(128) NOT NULL,
    role        VARCHAR(32)  NOT NULL,
    email       VARCHAR(128) NOT NULL,
    phone       VARCHAR(32)  NOT NULL,
    avatar      VARCHAR(255) DEFAULT '/static/avatar/default.png',
    status      TINYINT      DEFAULT 1,
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE subsidiary_company
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    group_name      VARCHAR(128) NOT NULL,
    subsidiary_name VARCHAR(128) NOT NULL,
    short_name      VARCHAR(64)  NOT NULL,
    business_line   VARCHAR(128) NOT NULL,
    intro           TEXT,
    status          TINYINT      NOT NULL DEFAULT 1,
    sort_num        INT          NOT NULL DEFAULT 1,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE job_category
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(128) NOT NULL,
    parent_id     BIGINT       NULL,
    sort_num      INT          NOT NULL DEFAULT 1,
    status        TINYINT      NOT NULL DEFAULT 1,
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE region_province
(
    code     VARCHAR(32) PRIMARY KEY,
    name     VARCHAR(64) NOT NULL,
    sort_num INT         NOT NULL DEFAULT 1
);

CREATE TABLE region_city
(
    code          VARCHAR(32) PRIMARY KEY,
    province_code VARCHAR(32) NOT NULL,
    name          VARCHAR(64) NOT NULL,
    sort_num      INT         NOT NULL DEFAULT 1,
    CONSTRAINT fk_region_city_province FOREIGN KEY (province_code) REFERENCES region_province (code)
);

CREATE TABLE job
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    subsidiary_id        BIGINT       NOT NULL,
    job_category_id      BIGINT       NOT NULL,
    title                VARCHAR(128) NOT NULL,
    company              VARCHAR(128) NOT NULL,
    city                 VARCHAR(64)  NOT NULL,
    category             VARCHAR(64)  NOT NULL,
    type                 VARCHAR(64)  NOT NULL,
    business_line        VARCHAR(128) NOT NULL,
    province_code        VARCHAR(32)  NOT NULL,
    city_code            VARCHAR(32)  NOT NULL,
    work_mode            VARCHAR(32)  NOT NULL,
    duration_type        VARCHAR(32)  NOT NULL,
    internship_length    VARCHAR(64),
    required_skills_json JSON         NOT NULL,
    tags                 JSON         NOT NULL,
    salary_min           INT          NOT NULL,
    salary_max           INT          NOT NULL,
    description          TEXT         NOT NULL,
    requirement          TEXT         NOT NULL,
    status               VARCHAR(32)  NOT NULL,
    publish_time         DATETIME     NULL,
    create_time          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_job_subsidiary FOREIGN KEY (subsidiary_id) REFERENCES subsidiary_company (id),
    CONSTRAINT fk_job_category FOREIGN KEY (job_category_id) REFERENCES job_category (id),
    CONSTRAINT fk_job_province FOREIGN KEY (province_code) REFERENCES region_province (code),
    CONSTRAINT fk_job_city FOREIGN KEY (city_code) REFERENCES region_city (code)
);

CREATE TABLE resume
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id              BIGINT       NOT NULL,
    title                VARCHAR(128) NOT NULL,
    resume_name          VARCHAR(128) NOT NULL,
    resume_type          VARCHAR(64)  NOT NULL DEFAULT 'GENERAL',
    target_subsidiary_id BIGINT       NULL,
    target_category_id   BIGINT       NULL,
    basic_info_json      JSON         NOT NULL,
    education_json       JSON         NOT NULL,
    experience_json      JSON         NOT NULL,
    project_json         JSON         NOT NULL,
    skills_json          JSON         NOT NULL,
    template_code        VARCHAR(64)  NOT NULL DEFAULT 'classic',
    pdf_url              VARCHAR(255) NOT NULL DEFAULT '/files/resume/default-resume.pdf',
    complete_score       INT          NOT NULL DEFAULT 0,
    is_default           TINYINT      NOT NULL DEFAULT 0,
    create_time          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_user FOREIGN KEY (user_id) REFERENCES sys_user (id),
    CONSTRAINT fk_resume_subsidiary FOREIGN KEY (target_subsidiary_id) REFERENCES subsidiary_company (id),
    CONSTRAINT fk_resume_category FOREIGN KEY (target_category_id) REFERENCES job_category (id)
);

CREATE TABLE guide_doc
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    code        VARCHAR(64)  NOT NULL UNIQUE,
    title       VARCHAR(128) NOT NULL,
    file_url    VARCHAR(255) NOT NULL,
    sort_num    INT          NOT NULL DEFAULT 1,
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE favorite_job
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT   NOT NULL,
    job_id      BIGINT   NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_job (user_id, job_id),
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES sys_user (id),
    CONSTRAINT fk_favorite_job FOREIGN KEY (job_id) REFERENCES job (id)
);

CREATE TABLE application_record
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id            BIGINT      NOT NULL,
    job_id             BIGINT      NOT NULL,
    resume_id          BIGINT      NOT NULL,
    stage              VARCHAR(32) NOT NULL,
    hr_note            VARCHAR(500),
    match_score        INT         NOT NULL DEFAULT 0,
    system_match_score INT         NOT NULL DEFAULT 0,
    hr_review_score    INT         NULL,
    hr_review_status   VARCHAR(32) NULL,
    hr_review_time     DATETIME    NULL,
    apply_time         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_job_application (user_id, job_id),
    CONSTRAINT fk_application_user FOREIGN KEY (user_id) REFERENCES sys_user (id),
    CONSTRAINT fk_application_job FOREIGN KEY (job_id) REFERENCES job (id),
    CONSTRAINT fk_application_resume FOREIGN KEY (resume_id) REFERENCES resume (id)
);

CREATE TABLE interview_record
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    application_id BIGINT      NOT NULL,
    round_num      INT         NOT NULL,
    interview_time DATETIME    NOT NULL,
    interviewer    VARCHAR(64) NOT NULL,
    status         VARCHAR(32) NOT NULL,
    remark         VARCHAR(255),
    create_time    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_interview_application FOREIGN KEY (application_id) REFERENCES application_record (id)
);
