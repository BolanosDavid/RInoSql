package ui;

import java.util.Scanner;

public final class ConsoleInput {
    private final Scanner scanner = new Scanner(System.in);

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) {
                    System.out.println("Valor fuera de rango [" + min + ", " + max + "].");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido.");
            }
        }
    }

    public String readString(String prompt, String defaultValue) {
        System.out.print(prompt + (defaultValue != null ? " [" + defaultValue + "]" : "") + ": ");
        String s = scanner.nextLine();
        return (s == null || s.isBlank()) ? defaultValue : s.trim();
    }
}
