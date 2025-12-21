package ui;

import service.QueryDefinition;
import java.util.List;

public final class ConsoleMenu {

    public void printHeader() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println(" NosqlApplication - Neo4j Console");
        System.out.println("==============================================");
    }

    public void printMenu(List<QueryDefinition> queries) {
        System.out.println();
        System.out.println("0) Reset/Create demo graph (runs create.cypher)");
        for (QueryDefinition q : queries) {
            System.out.printf("%d) %s%n", q.id(), q.title());
        }
        System.out.println("9) Export last result to TXT");
        System.out.println("99) Exit");
        System.out.println();
    }
}
