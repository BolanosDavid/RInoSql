package ui.swing;

import service.ParamDefinition;
import service.ParamType;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamFormPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel formPanel;
    private JLabel placeholderLabel;
    private final Map<String, JComponent> inputs = new HashMap<>();

    public ParamFormPanel() {
        setLayout(new BorderLayout());
        add(getFormPanel(), BorderLayout.NORTH);
        setBorder(BorderFactory.createTitledBorder("Parámetros"));
    }

    public void setParameters(List<ParamDefinition> params) {
        inputs.clear();
        JPanel panel = getFormPanel();
        panel.removeAll();
        if (params == null || params.isEmpty()) {
            panel.add(getPlaceholderLabel());
            revalidate();
            repaint();
            return;
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        for (ParamDefinition def : params) {
            JLabel label = new JLabel(def.prompt());
            JComponent input = createInput(def);

            gbc.gridx = 0;
            gbc.weightx = 0;
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
            panel.add(input, gbc);

            gbc.gridy++;
        }

        revalidate();
        repaint();
    }

    public Map<String, Object> collectValues() {
        Map<String, Object> values = new HashMap<>();
        for (Map.Entry<String, JComponent> e : inputs.entrySet()) {
            String name = e.getKey();
            JComponent comp = e.getValue();
            ParamType type = (ParamType) comp.getClientProperty("paramType");
            ParamDefinition def = (ParamDefinition) comp.getClientProperty("paramDef");
            values.put(name, readValue(comp, def, type));
        }
        return values;
    }

    private Object readValue(JComponent comp, ParamDefinition def, ParamType type) {
        try {
            return switch (type) {
                case STRING -> readString(comp, def);
                case INT -> ((Number) ((JSpinner) comp).getValue()).intValue();
                case LONG -> ((Number) ((JSpinner) comp).getValue()).longValue();
                case DOUBLE -> parseDouble((JFormattedTextField) comp, def);
                case BOOLEAN -> ((JCheckBox) comp).isSelected();
                default -> readString(comp, def);
            };
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Parámetro '" + def.prompt() + "': " + ex.getMessage(), ex);
        }
    }

    private Object parseDouble(JFormattedTextField field, ParamDefinition def) {
        String text = field.getText();
        if ((text == null || text.isBlank()) && def.defaultValue() != null) {
            return Double.parseDouble(def.defaultValue());
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Introduce un número decimal válido.");
        }
    }

    private String readString(JComponent comp, ParamDefinition def) {
        JTextField tf = (JTextField) comp;
        String text = tf.getText();
        if ((text == null || text.isBlank()) && def.defaultValue() != null) {
            return def.defaultValue();
        }
        return text == null ? "" : text.trim();
    }

    private JComponent createInput(ParamDefinition def) {
        JComponent input = switch (def.type()) {
            case STRING -> buildTextField(def.defaultValue());
            case INT -> buildSpinner(def.defaultValue(), Integer::parseInt);
            case LONG -> buildSpinner(def.defaultValue(), Long::parseLong);
            case DOUBLE -> buildDoubleField(def.defaultValue());
            case BOOLEAN -> buildCheckBox(def.defaultValue());
            default -> buildTextField(def.defaultValue());
        };
        input.putClientProperty("paramType", def.type());
        input.putClientProperty("paramDef", def);
        inputs.put(def.name(), input);
        return input;
    }

    private JTextField buildTextField(String defaultValue) {
        JTextField tf = new JTextField();
        if (defaultValue != null) {
            tf.setText(defaultValue);
        }
        return tf;
    }

    private JSpinner buildSpinner(String defaultValue, java.util.function.Function<String, Number> parser) {
        Number initial = 0;
        if (defaultValue != null) {
            try {
                initial = parser.apply(defaultValue);
            } catch (NumberFormatException ignored) {
                initial = 0;
            }
        }
        SpinnerNumberModel model = new SpinnerNumberModel(initial.longValue(), Long.MIN_VALUE, Long.MAX_VALUE, 1);
        return new JSpinner(model);
    }

    private JFormattedTextField buildDoubleField(String defaultValue) {
        NumberFormat format = NumberFormat.getNumberInstance();
        JFormattedTextField field = new JFormattedTextField(format);
        if (defaultValue != null) {
            field.setText(defaultValue);
        }
        return field;
    }

    private JCheckBox buildCheckBox(String defaultValue) {
        JCheckBox checkBox = new JCheckBox();
        if (defaultValue != null) {
            checkBox.setSelected(Boolean.parseBoolean(defaultValue));
        }
        return checkBox;
    }

    private JPanel getFormPanel() {
        if (formPanel == null) {
            formPanel = new JPanel(new GridBagLayout());
        }
        return formPanel;
    }

    private JLabel getPlaceholderLabel() {
        if (placeholderLabel == null) {
            placeholderLabel = new JLabel("(Sin parámetros)");
        }
        return placeholderLabel;
    }
}
