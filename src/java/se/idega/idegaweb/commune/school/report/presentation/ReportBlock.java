/*
 * $Id: ReportBlock.java,v 1.34 2004/09/08 07:13:19 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.NumberFormat;

import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.report.business.Cell;
import se.idega.idegaweb.commune.school.report.business.Header;
import se.idega.idegaweb.commune.school.report.business.ReportBusiness;
import se.idega.idegaweb.commune.school.report.business.ReportModel;

import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;

/** 
 * This is the base class for school report blocks.
 * <p>
 * Last modified: $Date: 2004/09/08 07:13:19 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.34 $
 */
public class ReportBlock extends CommuneBlock {

	public final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.school";

	private final static int ACTION_DEFAULT = 1;
	
	protected final static String PP = "school_report."; // Parameter prefix
	
	protected final static String PARAMETER_REPORT_MODEL = PP + "report_model";

	private final static String KP = "school_report."; // Localization key prefix

	private final static String KEY_SESSION_TIMEOUT = KP + "session_timeout";
//	private final static String KEY_PRINT = KP + "print";
	
	private Class _reportModelClass = null;
	private ReportModel _reportModel = null;
	
	private boolean _showPrintButton = false;
	private boolean _showTitle = false;
	private boolean _showDate = false;

	/**
	 * Default constructor.
	 */
	public ReportBlock() {
	}
	
	/**
	 * Constructs a report block with the specified report model class.
	 */
	public ReportBlock(Class reportModelClass) {
		_reportModelClass = reportModelClass;
	}

	/**
	 * Constructs a report block with the specified report model.
	 */
	public ReportBlock(ReportModel reportModel) {
		setReportModel(reportModel);
	}
	
	/**
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	/**
	 * Return property indicating if a print button is visible. 
	 */
	public boolean getShowPrintButton() {
		return _showPrintButton;
	}
	
	/**
	 * Sets property indicating if a print button is visible. 
	 */
	public void setShowPrintButton(boolean showPrintButton) {
		_showPrintButton = showPrintButton;
	}
	
	/**
	 * Return property indicating if the report title is visible. 
	 */
	public boolean getShowTitle() {
		return _showTitle;
	}
	
	/**
	 * Sets property indicating if the report title is visible. 
	 */
	public void setShowTitle(boolean showTitle) {
		_showTitle = showTitle;
	}
	
	/**
	 * Return property indicating if the report created date is visible. 
	 */
	public boolean getShowDate() {
		return _showDate;
	}
	
	/**
	 * Sets property indicating if the report created date is visible. 
	 */
	public void setShowDate(boolean showDate) {
		_showDate = showDate;
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
		if (getReportModel() == null) {
			try {
				setReportModel(getReportBusiness(iwc).createReportModel(getReportModelClass()));
			} catch (RemoteException e) {
				log(e);
			}
		}
		if (getReportModel() == null) {
			add(getErrorText(localize(KEY_SESSION_TIMEOUT, "You session has timed out. Please login again.")));
			return;
		}
		if (_showPrintButton) {
//			PrintButton pb = new PrintButton(localize(KEY_PRINT, "Print"));
//			pb = (PrintButton) getButton(pb);
//			add(pb);
//			add(new Break());
//			add(new Break());
		}
		if (getShowTitle() || getShowDate()) {
			Table table = new Table();
			table.setCellspacing(getCellspacing());
			table.setCellpadding(getCellpadding());
			if (getShowTitle()) {
				String key = getReportModel().getReportTitleLocalizationKey();
				String title = localize(key, key) + " ";
				table.add(getSmallText(title), 1, 1);
			}
			if (getShowDate()) {
				Date date = new Date(System.currentTimeMillis());
				table.add(getSmallText(date.toString()), 1, 1);
			}
			add(table);
			add(new Break());
		}
		Table table = new Table();
		table.setWidth("400");
		table.setCellspacing(0);
		table.setCellpadding(getCellpadding());
		table.setBorder(1);
		table.setBorderColor("#999999");
		buildReportTable(table);
		add(table);
		
		iwc.getSession().setAttribute(PARAMETER_REPORT_MODEL, getReportModel());
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
				String s = null;
				if (header.getHeaderType() == Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER) {
					s = header.getLocalizationKey();
				} else {
					s = localize(header.getLocalizationKey(), header.getLocalizationKey());
				}
				table.add(getSmallText(s), column, 1);
				column++;
			} else {
				table.mergeCells(column, 1, column + children.length - 1, 1);
				table.setAlignment(column, 1, Table.HORIZONTAL_ALIGN_CENTER);
				String s = null;
				if (header.getHeaderType() == Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER) {
					s = header.getLocalizationKey();
				} else {
					s = localize(header.getLocalizationKey(), header.getLocalizationKey());
				}
				table.add(getSmallText(s), column, 1);
				for (int j = 0; j < children.length; j++) {
					table.setAlignment(column + j, 2, Table.HORIZONTAL_ALIGN_CENTER);
					Header child = children[j];
					s = null;
					if (child.getHeaderType() == Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER) {
						s = child.getLocalizationKey();
					} else {
						s = localize(child.getLocalizationKey(), child.getLocalizationKey());
					}
					table.add(getSmallText(s), column + j, 2);
					table.setVerticalAlignment(column + j, 2, Table.VERTICAL_ALIGN_BOTTOM);
					table.setNoWrap(column + j, 2);
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
		String s = null;
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			Header[] children = header.getChildren();
			if (children == null) {
				int headerType = header.getHeaderType();
				if (headerType == Header.HEADERTYPE_ROW_SPACER) {
					s = "&nbsp;";
				} else if (headerType == Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER
						|| header.getHeaderType() == Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL) {
					s = header.getLocalizationKey();
				} else {
					s = localize(header.getLocalizationKey(), header.getLocalizationKey());					
				}
				if (headerType == Header.HEADERTYPE_ROW_LABEL || headerType == Header.HEADERTYPE_ROW_SPACER) {
					table.mergeCells(1, row, _reportModel.getColumnSize() + 1, row);
				}
				table.add(getSmallHeader(s), 1, row);
				table.setNoWrap(1, row);
				if (headerType == Header.HEADERTYPE_ROW_LABEL) {
					table.setColor(1, row, "#e0e0e0");
				}
				row++;
			} else {
				table.mergeCells(1, row, _reportModel.getColumnSize() + 1, row);
				table.setHeight(1, row, "26");
				table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_BOTTOM);
				if (header.getHeaderType() == Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER
						|| header.getHeaderType() == Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL) {
					s = header.getLocalizationKey();
				} else {
					s = localize(header.getLocalizationKey(), header.getLocalizationKey());					
				}
				table.add(getSmallText(s), 1, row);
				row++;
				for (int j = 0; j < children.length; j++) {
					Header child = children[j];
					int headerType = child.getHeaderType();
					if (headerType == Header.HEADERTYPE_ROW_SPACER) {
						s = "&nbsp;";
					} else if (headerType == Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER ||
							headerType == Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL) {
						s = child.getLocalizationKey();
					} else {
						s = localize(child.getLocalizationKey(), child.getLocalizationKey());					
					}
					if (headerType == Header.HEADERTYPE_ROW_LABEL || headerType == Header.HEADERTYPE_ROW_SPACER) {
						table.mergeCells(1, row, _reportModel.getColumnSize() + 1, row);
					}
					table.add(getSmallHeader(s), 1, row);
					table.setNoWrap(1, row);
					if (headerType == Header.HEADERTYPE_ROW_LABEL) {
						table.setColor(1, row, "#e0e0e0");
					}
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
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMaximumFractionDigits(1);
		for (int i = 0; i < rowHeaders.length; i++) {
			int rowCount = 0;
			Header header = rowHeaders[i];
			Header[] children = header.getChildren();
			boolean hasChildren = false;
			if (children != null) {
				hasChildren = true;
				tableRow++;
				rowCount = children.length;
			} else {
				int headerType = header.getHeaderType();
				if (headerType == Header.HEADERTYPE_ROW_LABEL || 
						headerType == Header.HEADERTYPE_ROW_SPACER) {
					rowCount = 0;
					tableRow++;
				} else {
					rowCount = 1;
				}
			}
			for (int j = 0; j < rowCount; j++) {
				if (hasChildren) {
					Header child = children[j];
					int headerType = child.getHeaderType();
					if (headerType == Header.HEADERTYPE_ROW_LABEL || 
							headerType == Header.HEADERTYPE_ROW_SPACER) {
						tableRow++;
						continue;
					}
				}
				for (int cellColumn = 0; cellColumn < _reportModel.getColumnSize(); cellColumn++) {
					Cell cell = _reportModel.getCell(cellRow, cellColumn);
					Text text = null;
					String align = Table.HORIZONTAL_ALIGN_RIGHT;
					
					switch (cell.getCellType()) {
						case Cell.CELLTYPE_PERCENT:
							String s = formatter.format(cell.getFloatValue());
							text = getSmallText(s);
							break;
						case Cell.CELLTYPE_ROW_HEADER:
							s = cell.getStringValue();
							text = getSmallHeader(s);
							align = Table.HORIZONTAL_ALIGN_LEFT;
							break;
						case Cell.CELLTYPE_SUM:
							text = getSmallHeader(formatNumber(cell.getValue()));
							break;
						case Cell.CELLTYPE_TOTAL:
							text = getSmallHeader(formatNumber(cell.getValue()));
							break;
						default:
							text = getSmallText(formatNumber(cell.getValue()));
							break;
					}
					int tableColumn = cellColumn + 2;
					table.add(text, tableColumn, tableRow);
					table.setAlignment(tableColumn, tableRow, align);
					table.setNoWrap(tableColumn, tableRow);
				}
				cellRow++;
				tableRow++;
			}
		}
		_reportModel.close();
	}

	/*
	 * Returns a string with space for numbers larger than 999, blank if zero.
	 */
	String formatNumber(int n) {
		if (n == 0) {
			return "&nbsp;";
		}
		String s = "" + n;
		int length = s.length();
		if (length > 3) {
			s = s.substring(0, (length - 3)) + " " + s.substring(length - 3);
		}
		return s;
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
