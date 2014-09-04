package org.deletethis.logfront.widgets;

public interface SearchPanelListener {

    public void onSearchCriteriaChange(SearchPanel panel);

    public void onSearchNext(SearchPanel panel);

    public void onSearchPrevious(SearchPanel panel);

    public void onSerachPanelOpened(SearchPanel searchPanel);
}
