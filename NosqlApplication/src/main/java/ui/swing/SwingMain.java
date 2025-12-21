package ui.swing;

import service.DbScriptRunner;
import service.Neo4jConfig;
import service.QueryCatalog;
import service.QueryService;

import javax.swing.*;

import org.neo4j.driver.Driver;

public class SwingMain {

    private final Driver driver;
    private final Neo4jConfig config;
    private MainFrame mainFrame;

    public SwingMain(Driver driver, Neo4jConfig config) {
        this.driver = driver;
        this.config = config;
    }

    public void start() {
        setLookAndFeel();
        getMainFrame().setVisible(true);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // fallback to default
        }
    }

    private MainFrame getMainFrame() {
        if (mainFrame == null) {
            QueryCatalog catalog = new QueryCatalog();
            QueryService service = new QueryService(driver, config.database());
            DbScriptRunner runner = new DbScriptRunner(service);
            mainFrame = new MainFrame(driver, catalog, service, runner);
        }
        return mainFrame;
    }
}
