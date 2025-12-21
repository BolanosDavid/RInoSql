package ui;

import org.neo4j.driver.Driver;
import service.Neo4jConfig;
import service.Neo4jDriverFactory;
import ui.swing.SwingMain;

import javax.swing.*;
import java.util.Arrays;

public final class Main {
    public static void main(String[] args) {
        Neo4jConfig config = Neo4jConfig.fromEnv();
        boolean consoleMode = Arrays.asList(args).contains("--console");

        System.out.println("Neo4j URI: " + config.uri());
        System.out.println("Neo4j USER: " + config.user());
        if ("neo4j".equals(config.password())) {
            System.out.println("Aviso: estás usando la contraseña por defecto 'password'. Cambia NEO4J_PASSWORD.");
        }

        if (consoleMode) {
            try (Driver driver = Neo4jDriverFactory.createDriver(config)) {
                driver.verifyConnectivity();
                new AppController(driver, config).run();
            }
            return;
        }

        Driver driver = Neo4jDriverFactory.createDriver(config);
        driver.verifyConnectivity();
        Runtime.getRuntime().addShutdownHook(new Thread(driver::close));
        SwingUtilities.invokeLater(() -> new SwingMain(driver, config).start());
    }
}
