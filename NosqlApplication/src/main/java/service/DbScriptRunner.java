package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class DbScriptRunner {

    private final QueryService queryService;

    public DbScriptRunner(QueryService queryService) {
        this.queryService = queryService;
    }

    public void runResourceScript(String resourcePath) {
        String script = readResource(resourcePath);
        List<String> statements = splitStatements(script);
        for (String st : statements) {
            queryService.runWrite(st, Map.of());
        }
    }

    private static String readResource(String resourcePath) {
        try (InputStream in = DbScriptRunner.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) throw new IllegalStateException("No se encontró el recurso: " + resourcePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                return br.lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el recurso: " + resourcePath, e);
        }
    }

    private static List<String> splitStatements(String script) {
        String withoutLineComments = script.replaceAll("(?m)^\s*//.*$", "");
        return List.of(withoutLineComments.split(";"))
                .stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }
}
