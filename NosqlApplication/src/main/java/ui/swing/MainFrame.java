package ui.swing;

import org.neo4j.driver.Driver;
import service.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainFrame extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Driver driver;
    private final QueryCatalog catalog;
    private final QueryService queryService;
    private final DbScriptRunner scriptRunner;

    private JMenuBar menuBar;
    private JMenu databaseMenu;
    private JMenuItem runCreateScriptItem;
    private JPanel mainPanel;
    private JSplitPane splitPane;
    private JPanel rightPanel;
    private JPanel actionPanel;
    private JButton executeButton;
    private JButton clearButton;
    private JButton exportButton;
    private QueryListPanel queryListPanel;
    private ParamFormPanel paramFormPanel;
    private ResultsPanel resultsPanel;
    private StatusBar statusBar;

    private String lastFormattedResult;

    public MainFrame(Driver driver, QueryCatalog catalog, QueryService queryService, DbScriptRunner scriptRunner) {
        this.driver = driver;
        this.catalog = catalog;
        this.queryService = queryService;
        this.scriptRunner = scriptRunner;
        setTitle("NosqlApplication - Neo4j");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(getAppMenuBar());
        setContentPane(getMainPanel());
        setPreferredSize(new Dimension(1100, 700));
        pack();
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                driver.close();
            }
        });
        initializeData();
    }

    private void initializeData() {
        getQueryListPanel().setQueries(catalog.all());
        if (!catalog.all().isEmpty()) {
            getQueryListPanel().getSelectionModel().setSelectionInterval(0, 0);
            onQuerySelected(catalog.all().get(0));
        }
        getStatusBar().setStatus("Listo");
    }

    private JMenuBar getAppMenuBar() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            menuBar.add(getDatabaseMenu());
        }
        return menuBar;
    }

    private JMenu getDatabaseMenu() {
        if (databaseMenu == null) {
            databaseMenu = new JMenu("Base de datos");
            databaseMenu.add(getRunCreateScriptItem());
        }
        return databaseMenu;
    }

    private JMenuItem getRunCreateScriptItem() {
        if (runCreateScriptItem == null) {
            runCreateScriptItem = new JMenuItem("Ejecutar create.cypher");
            runCreateScriptItem.addActionListener(e -> runCreateScript());
        }
        return runCreateScriptItem;
    }

    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(getSplitPane(), BorderLayout.CENTER);
            mainPanel.add(getStatusBar(), BorderLayout.SOUTH);
        }
        return mainPanel;
    }

    private JSplitPane getSplitPane() {
        if (splitPane == null) {
            splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getQueryListPanel(), getRightPanel());
            splitPane.setDividerLocation(320);
        }
        return splitPane;
    }

    private JPanel getRightPanel() {
        if (rightPanel == null) {
            rightPanel = new JPanel(new BorderLayout());
            rightPanel.add(getParamFormPanel(), BorderLayout.NORTH);
            rightPanel.add(getResultsPanel(), BorderLayout.CENTER);
            rightPanel.add(getActionPanel(), BorderLayout.SOUTH);
        }
        return rightPanel;
    }

    private JPanel getActionPanel() {
        if (actionPanel == null) {
            actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            actionPanel.add(getExecuteButton());
            actionPanel.add(getClearButton());
            actionPanel.add(getExportButton());
        }
        return actionPanel;
    }

    private JButton getExecuteButton() {
        if (executeButton == null) {
            executeButton = new JButton("Ejecutar");
            executeButton.addActionListener(e -> executeSelectedQuery());
        }
        return executeButton;
    }

    private JButton getClearButton() {
        if (clearButton == null) {
            clearButton = new JButton("Limpiar");
            clearButton.addActionListener(e -> clearResults());
        }
        return clearButton;
    }

    private JButton getExportButton() {
        if (exportButton == null) {
            exportButton = new JButton("Exportar...");
            exportButton.addActionListener(e -> exportResults());
        }
        return exportButton;
    }

    private QueryListPanel getQueryListPanel() {
        if (queryListPanel == null) {
            queryListPanel = new QueryListPanel();
            queryListPanel.addSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    onQuerySelected(queryListPanel.getSelectedQuery());
                }
            });
        }
        return queryListPanel;
    }

    private ParamFormPanel getParamFormPanel() {
        if (paramFormPanel == null) {
            paramFormPanel = new ParamFormPanel();
        }
        return paramFormPanel;
    }

    private ResultsPanel getResultsPanel() {
        if (resultsPanel == null) {
            resultsPanel = new ResultsPanel();
        }
        return resultsPanel;
    }

    private StatusBar getStatusBar() {
        if (statusBar == null) {
            statusBar = new StatusBar();
        }
        return statusBar;
    }

    private void onQuerySelected(QueryDefinition definition) {
        if (definition == null) {
            getParamFormPanel().setParameters(List.of());
            return;
        }
        getParamFormPanel().setParameters(definition.params());
        getResultsPanel().clear();
        getStatusBar().setStatus("Consulta seleccionada: " + definition.title());
    }

    private void executeSelectedQuery() {
        QueryDefinition definition = getQueryListPanel().getSelectedQuery();
        if (definition == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una consulta.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Map<String, Object> params;
        try {
            params = getParamFormPanel().collectValues();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setBusy(true);
        getStatusBar().setStatus("Ejecutando consulta...");

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                List<Map<String, Object>> rows = queryService.runRead(definition.cypher(), params);
                return ResultFormatter.format(rows);
            }

            @Override
            protected void done() {
                try {
                    String formatted = get();
                    getResultsPanel().setResults(formatted);
                    lastFormattedResult = "== " + definition.title() + " ==\n" + formatted;
                    getStatusBar().setStatus("Consulta completada");
                } catch (InterruptedException | ExecutionException e) {
                    handleError("Error ejecutando consulta", e.getCause() == null ? e : e.getCause());
                } finally {
                    setBusy(false);
                }
            }
        };
        worker.execute();
    }

    private void runCreateScript() {
        setBusy(true);
        getStatusBar().setStatus("Ejecutando create.cypher...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                scriptRunner.runResourceScript(CypherQueries.CREATE_SCRIPT_RESOURCE);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(MainFrame.this, "Script ejecutado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    getStatusBar().setStatus("Grafo creado");
                } catch (InterruptedException | ExecutionException e) {
                    handleError("Error ejecutando script", e.getCause() == null ? e : e.getCause());
                } finally {
                    setBusy(false);
                }
            }
        };
        worker.execute();
    }

    private void exportResults() {
        if (lastFormattedResult == null || lastFormattedResult.isBlank()) {
            JOptionPane.showMessageDialog(this, "No hay resultados para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Exportar resultados");
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path path = chooser.getSelectedFile().toPath();
        setBusy(true);
        getStatusBar().setStatus("Exportando resultados...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                FileExporter.writeText(path, lastFormattedResult);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(MainFrame.this, "Exportado a: " + path, "Exportación", JOptionPane.INFORMATION_MESSAGE);
                    getStatusBar().setStatus("Exportación completada");
                } catch (InterruptedException | ExecutionException e) {
                    handleError("Error exportando resultados", e.getCause() == null ? e : e.getCause());
                } finally {
                    setBusy(false);
                }
            }
        };
        worker.execute();
    }

    private void clearResults() {
        getResultsPanel().clear();
        lastFormattedResult = null;
        getStatusBar().setStatus("Listo");
    }

    private void setBusy(boolean busy) {
        getExecuteButton().setEnabled(!busy);
        getExportButton().setEnabled(!busy);
        getClearButton().setEnabled(!busy);
        getRunCreateScriptItem().setEnabled(!busy);
    }

    private void handleError(String title, Throwable error) {
        String message = error.getMessage() == null ? error.toString() : error.getMessage();
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
        getStatusBar().setStatus("Error: " + message);
    }
}
