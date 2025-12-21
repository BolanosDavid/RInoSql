package ui.swing;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel statusLabel;

    public StatusBar() {
        setLayout(new BorderLayout());
        add(getStatusLabel(), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setStatus("Listo");
    }

    public void setStatus(String message) {
        getStatusLabel().setText(message);
    }

    private JLabel getStatusLabel() {
        if (statusLabel == null) {
            statusLabel = new JLabel();
        }
        return statusLabel;
    }
}
