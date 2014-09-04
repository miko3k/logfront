package org.deletethis.logfront.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.deletethis.logfront.colors.BasicStyle;

public class FilterPanel extends JPanel {

    private static final long serialVersionUID = 7149221816283440464L;

    private class Row {

        Component filler;
        JLabel label;
        JComponent panel;
    }

    public static enum RowId {

        DO_NOT_SHOW("Do not show"),
        SHOW_ONLY("Show only");

        private final String label;

        private RowId(String label) {
            this.label = label;
        }

        private String getLabel() {
            return label;
        }
    }

    private Map<RowId, Row> rows = new EnumMap<>(RowId.class);

    void updateVisiblity() {
        boolean isfirstVisibleRow = true;
        for(Row r : rows.values()) {
            if(r.panel.getComponentCount() > 0) {
                r.panel.setVisible(true);
                r.label.setVisible(true);

                // filler on first row should not be displayed
                if(isfirstVisibleRow) {
                    r.filler.setVisible(false);
                } else {
                    r.filler.setVisible(true);
                }

                isfirstVisibleRow = false;
            } else {
                r.panel.setVisible(false);
                r.label.setVisible(false);
                r.filler.setVisible(false);
            }
        }
    }

    private ContainerListener containerListener = new ContainerListener() {

        @Override
        public void componentAdded(ContainerEvent arg0) {
            updateVisiblity();
        }

        @Override
        public void componentRemoved(ContainerEvent arg0) {
            updateVisiblity();
        }
    };

    private int addRow(RowId rowId, int y, BasicStyle style) {
        int gap = style.getBasicGap();
        final Row row = new Row();
        row.filler = Box.createRigidArea(new Dimension(1, gap));
        row.label = new JLabel(rowId.getLabel());
        row.panel = new JPanel();
        row.panel.setLayout(new FlowLayout2(gap, gap));

        row.panel.addContainerListener(containerListener);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0;
        c.gridy = y;
        c.gridx = 0;
        add(row.filler, c);

        ++y;
        c.weightx = 0;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = y;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        add(row.label, c);

        c.weightx = 1;
        c.insets = new Insets(0, gap, 0, 0);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        add(row.panel, c);

        rows.put(rowId, row);

        return y + 1;
    }

    public FilterPanel(BasicStyle style) {
        setLayout(new GridBagLayout());
        int y = 0;
        for(RowId rowId : RowId.values()) {
            y = addRow(rowId, y, style);
        }
        // add dummy JPanel to the bottom, so result top aligned
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = y;
        c.weighty = 1;
        add(Box.createVerticalGlue(), c);

        updateVisiblity();

        //getRow(RowId.DO_NOT_LOG).add(new JLabel("hello"));
    }

    public JComponent getRow(RowId rowId) {
        return rows.get(rowId).panel;
    }

}
