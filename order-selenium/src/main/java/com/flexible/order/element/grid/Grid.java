package com.flexible.order.element.grid;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

/**
 * 
 * @author Mariusz Malinowski
 * @since 2014.11.09
 */
public class Grid {
    public Grid(String gridId) {

    }

    public int getColumnCount() {
        throw new NotImplementedException("to be implemented");
    }

    public int getRowCount() {
        throw new NotImplementedException("to be implemented");
    }

    public int getRowGroupCount() {
        throw new NotImplementedException("to be implemented");
    }

    public String getColumnLabel(int index) {
        throw new NotImplementedException("to be implemented");
    }

    public GridRowGroup getGridRowGroup(int index) {
        throw new NotImplementedException("to be implemented");
    }

    public GridRow getGridRow(int groupIndex, int rowIndex) {
        throw new NotImplementedException("to be implemented");
    }

    public List<String> getColumnNames() {
        throw new NotImplementedException("to be implemented");
    }

    public String getGridName() {
        throw new NotImplementedException("to be implemented");
    }

    public GridFooter getGridFooter() {
        throw new NotImplementedException("to be implemented");
    }
}
