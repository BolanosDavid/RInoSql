package ui.swing;

import javax.swing.*;
import java.awt.*;

public class ResultsPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea resultsArea;
    private JScrollPane scrollPane;

    public ResultsPanel() {
        setLayout(new BorderLayout());
        add(getScrollPane(), BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder("Resultados"));
    }

    public void setResults(String text) {
        getResultsArea().setText(text);
        getResultsArea().setCaretPosition(0);
    }

    public void clear() {
        setResults("");
    }

    public String getCurrentText() {
        return getResultsArea().getText();
    }

    private JTextArea getResultsArea() {
        if (resultsArea == null) {
            resultsArea = new JTextArea();
            resultsArea.setEditable(false);
            resultsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            resultsArea.setLineWrap(false);
        }
        return resultsArea;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane(getResultsArea());
        }
        return scrollPane;
    }
}
