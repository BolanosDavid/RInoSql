package ui;

import org.neo4j.driver.Driver;
import service.Neo4jConfig;
import service.Neo4jDriverFactory;
import ui.swing.SwingMain;

import javax.swing.*;
import java.util.Arrays;

public final class Main {
    public static void main(String[] args) {
        Neo4jConfig config = Neo4jConfig.defaults();
        boolean consoleMode = Arrays.asList(args).contains("--console");

        System.out.println("Neo4j URI: " + config.url());
        System.out.println("Neo4j USER: " + config.user());
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
