/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.deletethis.logfront.colors;

/**
 *
 * @author miko
 */
public interface GlobalStyle extends BasicStyle {

    FilterEntryStyle getFilterEntryStyle();

    LogViewStyle getLogViewStyle();

    MatcherStyle getMatcherStyle();
    
}
