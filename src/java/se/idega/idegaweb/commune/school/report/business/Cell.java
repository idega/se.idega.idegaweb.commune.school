/*
 * $Id: Cell.java,v 1.1 2003/12/08 16:24:24 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.rmi.RemoteException;

/** 
 * Holder for report cell values. A report cell holds methods, parameters, type and
 * the parent report model for calculating the cell's value.
 * <p>
 * Last modified: $Date: 2003/12/08 16:24:24 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class Cell {

	public final static int CELLTYPE_NORMAL = 1;
	public final static int CELLTYPE_SUM = 2;
	public final static int CELLTYPE_TOTAL = 3;
	
	private final static float VALUE_UNDEFINED = Float.NEGATIVE_INFINITY;

	private ReportModel _reportModel = null;
	private int _row = 0;
	private int _column = 0;
	private int _rowMethod = 0;
	private int _columnMethod = 0;
	private Object _rowParameter = null;
	private Object _columnParameter = null;
	private int _cellType = 0;
	private float _value = VALUE_UNDEFINED;
	
	/**
	 * Constructs a report cell with the specified values.
	 * @param parentModel the report model used for calculating the cell value
	 * @param row the row position for the cell in the report model
	 * @param column the column position for the cell in the report model
	 * @param rowMethod the index for the method to use for calculating the row-dependent cell value 
	 * @param columnMethod the index for the method to use for calculating the column-dependent cell value
	 * @param rowParameter the row-dependent parameter 
	 * @param columnParameter the column-dependent parameter 
	 */	
	public Cell(ReportModel reportModel,
				int row,
				int column,
				int rowMethod,
				int columnMethod,
				Object rowParameter,
				Object columnParameter,
				int cellType) {
		_reportModel = reportModel;
		_row = row;
		_column = column;
		_rowMethod = rowMethod;
		_columnMethod = columnMethod;
		_rowParameter = rowParameter;
		_columnParameter = columnParameter;
		_cellType = cellType;
	}	
	
	/**
	 * Returns the row position in the report model for this cell.
	 */
	protected int getRow() {
		return _row;
	}
	
	/**
	 * Returns the column position in the report model for this cell.
	 */
	protected int getColumn() {
		return _column;
	}
	
	/**
	 * Returns the row method for this cell.
	 */
	protected int getRowMethod() {
		return _rowMethod;
	}
	
	/**
	 * Returns the column method for this cell.
	 */
	protected int getColumnMethod() {
		return _columnMethod;
	}
	
	/**
	 * Returns the row parameter for this cell.
	 */
	protected Object getRowParameter() {
		return _rowParameter;
	}
	
	/**
	 * Returns the column parameter for this cell.
	 */
	protected Object getColumnParameter() {
		return _columnParameter;
	}
	
	/**
	 * Returns the type for this cell.
	 */
	public int getCellType() {
		return _cellType;
	}
	
	/**
	 * Returns the numeric value for this cell as a truncated integer.
	 */
	public int getValue() {
		return (int) getFloatValue();
	}
	
	/**
	 * Returns the numeric value for this cell.
	 */
	public float getFloatValue() {
		if (_value == VALUE_UNDEFINED) {
			try {
				_value = _reportModel.calculate(this);
			} catch (RemoteException e) {
				_reportModel.log(e.getMessage());
			}
		}
		return _value;
	}
}
