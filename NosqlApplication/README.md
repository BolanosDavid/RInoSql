# NosqlApplication (Neo4j console app)

Console application (Java 21) to run queries against Neo4j.

## Run (Maven)
Set env vars:
- NEO4J_URI=bolt://localhost:7687
- NEO4J_USER=neo4j
- NEO4J_PASSWORD=your_password
- (optional) NEO4J_DB=neo4j

Run:
- mvn -q exec:java

## Menu
- 0) Reset/Create demo graph (runs create.cypher from resources)
- 1..6) Run example queries
- 9) Export last printed result to TXT
