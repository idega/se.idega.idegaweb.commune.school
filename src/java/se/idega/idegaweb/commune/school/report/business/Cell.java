/*
 * $Id: Cell.java,v 1.5 2006/04/09 11:39:54 laddi Exp $
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
 * Last modified: $Date: 2006/04/09 11:39:54 $ by $Author: laddi $
 *
 * @author Anders Lindman
 * @version $Revision: 1.5 $
 */
public class Cell {

	public final static int CELLTYPE_NORMAL = 1;
	public final static int CELLTYPE_SUM = 2;
	public final static int CELLTYPE_TOTAL = 3;
	public final static int CELLTYPE_PERCENT = 4;
	public final static int CELLTYPE_ROW_HEADER = 5;
	
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
		this._reportModel = reportModel;
		this._row = row;
		this._column = column;
		this._rowMethod = rowMethod;
		this._columnMethod = columnMethod;
		this._rowParameter = rowParameter;
		this._columnParameter = columnParameter;
		this._cellType = cellType;
	}	
	
	/**
	 * Returns the row position in the report model for this cell.
	 */
	protected int getRow() {
		return this._row;
	}
	
	/**
	 * Returns the column position in the report model for this cell.
	 */
	protected int getColumn() {
		return this._column;
	}
	
	/**
	 * Returns the row method for this cell.
	 */
	protected int getRowMethod() {
		return this._rowMethod;
	}
	
	/**
	 * Returns the column method for this cell.
	 */
	protected int getColumnMethod() {
		return this._columnMethod;
	}
	
	/**
	 * Returns the row parameter for this cell.
	 */
	protected Object getRowParameter() {
		return this._rowParameter;
	}
	
	/**
	 * Returns the column parameter for this cell.
	 */
	protected Object getColumnParameter() {
		return this._columnParameter;
	}
	
	/**
	 * Returns the type for this cell.
	 */
	public int getCellType() {
		return this._cellType;
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
		if (this._value == VALUE_UNDEFINED) {
			try {
				this._value = this._reportModel.calculate(this);
			} catch (RemoteException e) {
				this._reportModel.log(e.getMessage());
			}
		}
		return this._value;
	}
	
	/**
	 * Returns the string value for this cell.
	 */
	public String getStringValue() {
		String s = null;
		if (this._cellType == CELLTYPE_ROW_HEADER) {
			s = this._rowParameter.toString();
		}
		return s;
	}
}
