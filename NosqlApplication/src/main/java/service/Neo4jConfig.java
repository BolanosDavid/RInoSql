package service;

import java.util.Objects;

/**
 * Neo4j connection configuration
 */
public final class Neo4jConfig {
	
    public static final String DEFAULT_URL = "neo4j://localhost:7687";
    public static final String DEFAULT_USER = "neo4j";
    public static final String DEFAULT_PASSWORD = "neo4j";
    public static final String DEFAULT_DB = "neo4j";

    private final String url;
    private final String user;
    private final String password;
    private final String database; // optional

    public Neo4jConfig(String uri, String user, String password, String database) {
        this.url = Objects.requireNonNull(uri, "url");
        this.user = Objects.requireNonNull(user, "user");
        this.password = Objects.requireNonNull(password, "password");
        this.database = database;
    }
    
    public static Neo4jConfig defaults() {
        return new Neo4jConfig(DEFAULT_URL, DEFAULT_USER, DEFAULT_PASSWORD, DEFAULT_DB);
    }
    
    public String url() { return url; }
    public String user() { return user; }
    public String password() { return password; }
    public String database() { return database; }

}
