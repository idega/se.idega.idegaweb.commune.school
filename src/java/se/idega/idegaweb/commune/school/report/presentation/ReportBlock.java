/*
 * $Id: ReportBlock.java,v 1.3 2003/12/09 09:51:08 laddi Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.report.business.Cell;
import se.idega.idegaweb.commune.school.report.business.Header;
import se.idega.idegaweb.commune.school.report.business.ReportBusiness;
import se.idega.idegaweb.commune.school.report.business.ReportModel;

import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

/** 
 * This is the base class for school report blocks.
 * <p>
 * Last modified: $Date: 2003/12/09 09:51:08 $ by $Author: laddi $
 *
 * @author Anders Lindman
 * @version $Revision: 1.3 $
 */
public class ReportBlock extends CommuneBlock {

	public final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.school";

	private final static int ACTION_DEFAULT = 1;

	private Class _reportModelClass = null;
	private ReportModel _reportModel = null;

	public ReportBlock() {
	}
	
	/**
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public ReportBlock(Class reportModelClass) {
		_reportModelClass = reportModelClass;
	}
	
	/**
	 * @see com.idega.presentation.Block#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) {
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT:
					handleDefaultAction(iwc);
					break;
			}
		} catch (Exception e) {
			log(e);
			add(new ExceptionWrapper(e, this));
		}
	}

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		iwc.toString(); // remove
		int action = ACTION_DEFAULT;
		return action;
	}

	/*
	 * Handles the default action for this block.
	 */	
	private void handleDefaultAction(IWContext iwc) {
		try {
			setReportModel(getReportBusiness(iwc).createReportModel(getReportModelClass()));
		} catch (RemoteException e) {
			log(e);
		}
		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(getCellpadding());
		table.setBorder(1);
		table.setBorderColor("#999999");
		buildReportTable(table);
		add(table);
	}

	/*
	 * Returns a report business object.
	 */
	private ReportBusiness getReportBusiness(IWContext iwc) throws RemoteException {
		return (ReportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ReportBusiness.class);
	}	
	
	/*
	 * Sets the report model for this block. 
	 */
	private void setReportModel(ReportModel reportModel) {
		_reportModel = reportModel;
	}
	
	/**
	 * Returns the report model for this block. 
	 */
	protected ReportModel getReportModel() {
		return _reportModel;
	}

	/**
	 * Builds the report column headers.
	 */
	protected void buildColumnHeaders(Table table) {
		Header[] headers = _reportModel.getColumnHeaders();
		table.mergeCells(1, 1, 1, 2);
		int column = 2;
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			Header[] children = header.getChildren();
			if (children == null) {
				table.mergeCells(column, 1, column, 2);
				table.setAlignment(column, 1, Table.HORIZONTAL_ALIGN_CENTER);
				table.setVerticalAlignment(column, 1, Table.VERTICAL_ALIGN_BOTTOM);
				String s = localize(header.getLocalizationKey(), header.getLocalizationKey());
				table.add(getSmallText(s), column, 1);
				column++;
			} else {
				table.mergeCells(column, 1, column + children.length - 1, 1);
				table.setAlignment(column, 1, Table.HORIZONTAL_ALIGN_CENTER);
				String s = localize(header.getLocalizationKey(), header.getLocalizationKey());
				table.add(getSmallText(s), column, 1);
				for (int j = 0; j < children.length; j++) {
					table.setAlignment(column + j, 2, Table.HORIZONTAL_ALIGN_CENTER);
					Header child = children[j];
					s = localize(child.getLocalizationKey(), child.getLocalizationKey());
					table.add(getSmallText(s), column + j, 2);
				}
				column += children.length;
			}
		}
	}

	/**
	 * Builds the report row headers.
	 */
	protected void buildRowHeaders(Table table) {
		Header[] headers = _reportModel.getRowHeaders();
		int row = 3;
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			Header[] children = header.getChildren();
			if (children == null) {
				String s = localize(header.getLocalizationKey(), header.getLocalizationKey());
				table.add(getSmallHeader(s), 1, row);
				row++;
			} else {
				table.mergeCells(1, row, _reportModel.getColumnSize() + 1, row);
				table.setHeight(1, row, "26");
				table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_BOTTOM);
				String s = localize(header.getLocalizationKey(), header.getLocalizationKey());
				table.add(getSmallText(s), 1, row);
				row++;
				for (int j = 0; j < children.length; j++) {
					Header child = children[j];
					s = localize(child.getLocalizationKey(), child.getLocalizationKey());
					table.add(getSmallHeader(s), 1, row);
					row++;
				}
			}
		}
	}

	/**
	 * Builds the report cells.
	 */
	protected void buildReportCells(Table table) {
		int cellRow = 0;
		int tableRow = 3;
		Header[] rowHeaders = _reportModel.getRowHeaders();
		for (int i = 0; i < rowHeaders.length; i++) {
			int rowCount = 0;
			Header header = rowHeaders[i];
			Header[] children = header.getChildren();
			if (children != null) {
				tableRow++;
				rowCount = children.length;
			} else {
				rowCount = 1;
			}
			for (int j = 0; j < rowCount; j++) {
				for (int cellColumn = 0; cellColumn < _reportModel.getColumnSize(); cellColumn++) {
					Cell cell = _reportModel.getCell(cellRow, cellColumn);
					Text text = getSmallText("" + cell.getValue());
					int tableColumn = cellColumn + 2;
					table.add(text, tableColumn, tableRow);
					table.setAlignment(tableColumn, tableRow, Table.HORIZONTAL_ALIGN_RIGHT);
				}
				cellRow++;
				tableRow++;
			}
		}
	}
	
	/**
	 * Returns a default small text object for reports. 
	 */
	public Text getSmallText(String s) {
		return super.getSmallText(s);
	}
	
	/**
	 * Returns a default small header text object for reports. 
	 */
	public Text getSmallHeader(String s) {
		return super.getSmallHeader(s);
	}
	
	/**
	 * Returns a default header text object for reports. 
	 */
	public Text getHeader(String s) {
		return super.getHeader(s);
	}
	
	/**
	 * Returns the report model class to use in this block.
	 */
	protected Class getReportModelClass() {
		return _reportModelClass;
	}
	
	/**
	 * Builds the table for the entire report.
	 */
	protected void buildReportTable(Table table) {
		buildColumnHeaders(table);
		buildRowHeaders(table);
		buildReportCells(table);
	}
}
