package ui;

import org.neo4j.driver.Driver;
import service.*;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AppController {

    private final QueryCatalog catalog = new QueryCatalog();
    private final QueryService queryService;
    private final DbScriptRunner scriptRunner;
    private final ConsoleMenu menu = new ConsoleMenu();
    private final ConsoleInput input = new ConsoleInput();

    private String lastPrintedResult = null;

    public AppController(Driver driver, Neo4jConfig config) {
        this.queryService = new QueryService(driver, config.database());
        this.scriptRunner = new DbScriptRunner(queryService);
    }

    public void run() {
        while (true) {
            menu.printHeader();
            menu.printMenu(catalog.all());

            int choice = input.readInt("Elige opción: ", 0, 99);

            if (choice == 99) {
                System.out.println("Hasta luego.");
                return;
            }

            if (choice == 0) {
                System.out.println("Ejecutando script de creación...");
                scriptRunner.runResourceScript(CypherQueries.CREATE_SCRIPT_RESOURCE);
                System.out.println("OK. Grafo creado.");
                continue;
            }

            if (choice == 9) {
                if (lastPrintedResult == null) {
                    System.out.println("No hay resultados previos para exportar.");
                    continue;
                }
                String filename = input.readString("Nombre del fichero de salida", "resultados.txt");
                FileExporter.writeText(Path.of(filename), lastPrintedResult);
                System.out.println("Exportado a: " + filename);
                continue;
            }

            try {
                QueryDefinition q = catalog.byId(choice);
                executeQuery(q);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void executeQuery(QueryDefinition q) {
        System.out.println();
        System.out.println("== " + q.title() + " ==");
        if (q.description() != null && !q.description().isBlank()) {
            System.out.println(q.description());
        }

        Map<String, Object> params = new HashMap<>();
        for (ParamDefinition p : q.params()) {
            Object value = switch (p.type()) {
                case STRING -> input.readString(p.prompt(), p.defaultValue());
                case INT -> Integer.parseInt(input.readString(p.prompt(), p.defaultValue()));
                case LONG -> Long.parseLong(input.readString(p.prompt(), p.defaultValue()));
                case DOUBLE -> Double.parseDouble(input.readString(p.prompt(), p.defaultValue()));
                case BOOLEAN -> Boolean.parseBoolean(input.readString(p.prompt(), p.defaultValue()));
            };
            params.put(p.name(), value);
        }

        List<Map<String, Object>> rows = queryService.runRead(q.cypher(), params);
        String formatted = ResultFormatter.format(rows);

        System.out.println();
        System.out.println(formatted);

        lastPrintedResult = "== " + q.title() + " ==\n" + formatted + "\n";
    }
}
