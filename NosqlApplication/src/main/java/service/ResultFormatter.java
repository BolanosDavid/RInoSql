package service;

import java.util.*;

public final class ResultFormatter {

    private ResultFormatter() {}

    public static String format(List<Map<String, Object>> rows) {
        if (rows == null || rows.isEmpty()) return "(sin resultados)";

        List<String> cols = new ArrayList<>(rows.get(0).keySet());
        Map<String, Integer> w = new LinkedHashMap<>();
        for (String c : cols) w.put(c, c.length());

        for (Map<String, Object> row : rows) {
            for (String c : cols) {
                String v = stringify(row.get(c));
                w.put(c, Math.max(w.get(c), v.length()));
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(lineHeader(cols, w)).append("\n");
        sb.append(lineSep(cols, w)).append("\n");
        for (Map<String, Object> row : rows) sb.append(lineRow(cols, w, row)).append("\n");
        return sb.toString();
    }

    private static String lineHeader(List<String> cols, Map<String, Integer> w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.size(); i++) {
            String c = cols.get(i);
            sb.append(pad(c, w.get(c)));
            if (i < cols.size() - 1) sb.append(" | ");
        }
        return sb.toString();
    }

    private static String lineRow(List<String> cols, Map<String, Integer> w, Map<String, Object> row) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.size(); i++) {
            String c = cols.get(i);
            sb.append(pad(stringify(row.get(c)), w.get(c)));
            if (i < cols.size() - 1) sb.append(" | ");
        }
        return sb.toString();
    }

    private static String lineSep(List<String> cols, Map<String, Integer> w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.size(); i++) {
            sb.append("-".repeat(w.get(cols.get(i))));
            if (i < cols.size() - 1) sb.append("-+-");
        }
        return sb.toString();
    }

    private static String pad(String s, int w) {
        if (s.length() >= w) return s;
        return s + " ".repeat(w - s.length());
    }

    private static String stringify(Object v) {
        return (v == null) ? "null" : String.valueOf(v);
    }
}
