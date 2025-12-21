package service;

import org.neo4j.driver.*; 
import org.neo4j.driver.Record;

import java.util.*;

public final class QueryService {

    private final Driver driver;
    private final String database; 

    public QueryService(Driver driver, String database) {
        this.driver = Objects.requireNonNull(driver);
        this.database = database;
    }

    public List<Map<String, Object>> runRead(String cypher, Map<String, Object> params) {
        SessionConfig cfg = (database == null)
                ? SessionConfig.defaultConfig()
                : SessionConfig.builder().withDatabase(database).build();

        try (Session session = driver.session(cfg)) {
            return session.executeRead(tx -> {
                Result result = tx.run(cypher, params == null ? Map.of() : params);
                List<Map<String, Object>> rows = new ArrayList<>();
                while (result.hasNext()) {
                    Record record = result.next();
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (String key : record.keys()) {
                        row.put(key, record.get(key).asObject());
                    }
                    rows.add(row);
                }
                return rows;
            });
        }
    }

    public void runWrite(String cypher, Map<String, Object> params) {
        SessionConfig cfg = (database == null)
                ? SessionConfig.defaultConfig()
                : SessionConfig.builder().withDatabase(database).build();

        try (Session session = driver.session(cfg)) {
            session.executeWrite(tx -> { tx.run(cypher, params == null ? Map.of() : params); return null; });
        }
    }
}
