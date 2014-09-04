package org.deletethis.logfront.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.deletethis.logfront.SearchCriteria;
import org.deletethis.logfront.colors.BasicStyle;

public class SearchPanel extends JPanel {

    private static final long serialVersionUID = -956734341064235213L;
    private final JTextField field;
    private final JButton prev;
    private final JButton next;
    private final JCheckBox matchCase;
	//private final JCheckBox highlightAll;
    //private final JButton closeButton;
    private final SearchPanelListener listener;
    private final Color notFoundColor, foundColor;

    private boolean showingFound;

    public SearchPanel(BasicStyle uistyle, SearchPanelListener list, Color notFoundColor) {
        this.listener = list;
        this.notFoundColor = notFoundColor;

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        int gap2 = uistyle.getBasicGap();

        // spacing will be based on default button default left margin
        add(field = new JTextField());
        add(Box.createRigidArea(new Dimension(gap2, 0)));
        add(prev = new JButton("Previous"));
        add(Box.createRigidArea(new Dimension(gap2, 0)));
        add(next = new JButton("Next"));
        add(Box.createRigidArea(new Dimension(gap2, 0)));
        add(matchCase = new JCheckBox("Match case"));
		//add(Box.createRigidArea(new Dimension(gap2,0)));
        //add(highlightAll = new JCheckBox("Highlight all"));

        this.foundColor = field.getBackground();
        this.showingFound = true;

        this.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent paramFocusEvent) {
            }

            @Override
            public void focusGained(FocusEvent paramFocusEvent) {
                field.requestFocus();
                field.selectAll();
                listener.onSerachPanelOpened(SearchPanel.this);
            }
        });

        field.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                listener.onSearchCriteriaChange(SearchPanel.this);
            }

            public void removeUpdate(DocumentEvent e) {
                listener.onSearchCriteriaChange(SearchPanel.this);
            }

            public void insertUpdate(DocumentEvent e) {
                listener.onSearchCriteriaChange(SearchPanel.this);
            }
        });
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onSearchNext(SearchPanel.this);
            }
        });
        matchCase.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                listener.onSearchCriteriaChange(SearchPanel.this);
            }
        });
        /*highlightAll.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
         listener.onSearchHighlightAll(SearchPanel.this);
         }
         });*/
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent paramActionEvent) {
                listener.onSearchNext(SearchPanel.this);
            }
        });
        prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent paramActionEvent) {
                listener.onSearchPrevious(SearchPanel.this);
            }
        });
    }

    public SearchCriteria getSearchCriteria() {
        String text = field.getText();

        if(text.isEmpty()) {
            return null;
        } else {
            return new SearchCriteria(text, matchCase.isSelected());
        }
    }

    //public boolean isHighlightAllSet() { return highlightAll.isSelected(); }

    public void showFound() {
        if(showingFound) {
            return;
        }
        field.setBackground(foundColor);
        showingFound = true;
    }

    public void showNotFound() {
        if(!showingFound) {
            return;
        }
        field.setBackground(notFoundColor);
        showingFound = false;
    }
}
