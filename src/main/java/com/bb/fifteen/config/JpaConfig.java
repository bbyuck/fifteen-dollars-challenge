package com.bb.fifteen.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA 관련 설정
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.bb.fifteen.domain"})
public class JpaConfig {

    @PersistenceContext
    private EntityManager em;
}
