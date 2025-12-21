package ui.swing;

import service.QueryDefinition;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.ListSelectionModel;
import java.awt.*;
import java.util.List;

public class QueryListPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final DefaultListModel<QueryDefinition> model = new DefaultListModel<>();
    private JList<QueryDefinition> queryList;
    private JScrollPane scrollPane;

    public QueryListPanel() {
        setLayout(new BorderLayout());
        add(getScrollPane(), BorderLayout.CENTER);
    }

    public void setQueries(List<QueryDefinition> queries) {
        model.clear();
        for (QueryDefinition q : queries) {
            model.addElement(q);
        }
    }

    public QueryDefinition getSelectedQuery() {
        return getQueryList().getSelectedValue();
    }

    public void addSelectionListener(ListSelectionListener listener) {
        getQueryList().addListSelectionListener(listener);
    }

    public ListSelectionModel getSelectionModel() {
        return getQueryList().getSelectionModel();
    }

    public void setSelectedIndex(int index) {
        getQueryList().setSelectedIndex(index);
    }

    private JList<QueryDefinition> getQueryList() {
        if (queryList == null) {
            queryList = new JList<>(model);
            queryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            queryList.setCellRenderer(new QueryListRenderer());
        }
        return queryList;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane(getQueryList());
            scrollPane.setBorder(BorderFactory.createTitledBorder("Consultas"));
        }
        return scrollPane;
    }

    private static class QueryListRenderer extends DefaultListCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof QueryDefinition q) {
                setText(q.id() + ") " + q.title());
                setToolTipText(q.description());
            }
            return this;
        }
    }
}
