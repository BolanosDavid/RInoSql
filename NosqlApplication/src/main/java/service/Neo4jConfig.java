package service;

import java.util.Objects;

/**
 * Neo4j connection configuration.
 * Overrides via env vars: NEO4J_URI, NEO4J_USER, NEO4J_PASSWORD, NEO4J_DB (optional)
 */
public final class Neo4jConfig {
    private final String uri;
    private final String user;
    private final String password;
    private final String database; // optional

    public Neo4jConfig(String uri, String user, String password, String database) {
        this.uri = Objects.requireNonNull(uri);
        this.user = Objects.requireNonNull(user);
        this.password = Objects.requireNonNull(password);
        this.database = database;
    }

    public String uri() { return uri; }
    public String user() { return user; }
    public String password() { return password; }
    public String database() { return database; }

    public static Neo4jConfig fromEnv() {
        String uri = firstNonBlank(System.getenv("NEO4J_URI"), "neo4j+s://c01c132e.databases.neo4j.io");
        String user = firstNonBlank(System.getenv("NEO4J_USER"), "neo4j");
        String password = firstNonBlank(System.getenv("NEO4J_PASSWORD"), "OlhwwV6QYh7NtMl-XBmGE5ZiQwfhxRIGLU_zUkHZ2RM");
        String db = firstNonBlank(System.getenv("NEO4J_DB"), "neo4j");
        return new Neo4jConfig(uri, user, password, db);
    }

    private static String firstNonBlank(String v, String fallback) {
        return (v == null || v.isBlank()) ? fallback : v;
    }

    private static String blankToNull(String v) {
        return (v == null || v.isBlank()) ? null : v;
    }
}
