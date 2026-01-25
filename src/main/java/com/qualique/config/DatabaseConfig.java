package com.qualique.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Database configuration for production environment (Railway).
 * Parses the DATABASE_URL environment variable provided by Railway
 * and creates a proper JDBC DataSource.
 */
@Configuration
@Profile("prod")
@Slf4j
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Bean
    @Primary
    public DataSource dataSource() throws URISyntaxException {
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            throw new IllegalStateException("DATABASE_URL environment variable is not set");
        }

        log.info("Configuring database from DATABASE_URL");
        
        // Parse the DATABASE_URL (format: postgresql://user:password@host:port/database)
        URI dbUri = new URI(databaseUrl);
        
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String host = dbUri.getHost();
        int port = dbUri.getPort();
        String database = dbUri.getPath().substring(1); // Remove leading slash
        
        // Build JDBC URL with SSL (required for Railway PostgreSQL)
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?sslmode=require", host, port, database);
        
        log.info("Database host: {}, port: {}, database: {}", host, port, database);
        
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        // Connection pool settings optimized for Railway free tier
        dataSource.setMaximumPoolSize(5);
        dataSource.setMinimumIdle(1);
        dataSource.setIdleTimeout(300000); // 5 minutes
        dataSource.setConnectionTimeout(20000); // 20 seconds
        dataSource.setMaxLifetime(1200000); // 20 minutes
        
        return dataSource;
    }
}
