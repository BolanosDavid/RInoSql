package service;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public final class Neo4jDriverFactory {
    private Neo4jDriverFactory() {}

    public static Driver createDriver(Neo4jConfig config) {
        return GraphDatabase.driver(
                config.url(),
                AuthTokens.basic(config.user(), config.password())
        );
    }
}
