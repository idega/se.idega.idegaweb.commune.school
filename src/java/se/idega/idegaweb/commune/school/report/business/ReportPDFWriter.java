/*
 * $Id: ReportPDFWriter.java,v 1.8 2004/03/01 10:25:12 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.awt.Point;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileBMPBean;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/** 
 * Creates report files in Adobe PDF format.
 * <p>
 * Last modified: $Date: 2004/03/01 10:25:12 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.8 $
 */
public class ReportPDFWriter {

	private final static String REPORT_FOLDER_NAME = "School Report Files";

	private ReportModel _reportModel = null;
	private IWResourceBundle _iwrb = null;
	private Font _normalFont = null;
	private Font _boldFont = null;
	int _widths[] = null;
	
	/**
	 * Constructs a report PDF writer object.
	 * @param reportModel the report model to write as a PDF file
	 * @param iwrb the resource bundle to use for translating text labels
	 */	
	public ReportPDFWriter(ReportModel reportModel, IWResourceBundle iwrb) {
		_reportModel = reportModel;
		_iwrb = iwrb;
	}	
	
	/**
	 * Creates the PDF report file.
	 */
	public ICFile createFile() {
		ICFile reportFolder = null;
		ICFileHome fileHome = null;

		try {
			fileHome = (ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class);
			reportFolder = fileHome.findByFileName(REPORT_FOLDER_NAME);
		} catch (FinderException e) {
			try {
				ICFile root = fileHome.findByFileName(ICFileBMPBean.IC_ROOT_FOLDER_NAME);
				reportFolder = fileHome.create();
				reportFolder.setName(REPORT_FOLDER_NAME);
				reportFolder.setMimeType("application/vnd.iw-folder");
				reportFolder.store();
				root.addChild(reportFolder);
			} catch (Exception e2) {
				System.out.println(e2);
				return null;
			}
		} catch (IDOLookupException e) {
			System.out.println(e);
			return null;
		}

		ICFile exportFile = null;
		String filename = _reportModel.getReportTitleLocalizationKey() + ".pdf";
		
		try {
			MemoryFileBuffer buffer = getPDFBuffer();
			MemoryInputStream mis = new MemoryInputStream(buffer);

			try {
				exportFile = fileHome.findByFileName(filename);
				if (exportFile != null) {
					exportFile.remove();
				}
			} catch (FinderException e) {}

			exportFile = fileHome.create();
			exportFile.setFileValue(mis);
			exportFile.setMimeType("application/pdf");
			exportFile.setName(filename);
			exportFile.setFileSize(buffer.length());
			exportFile.store();
			
			reportFolder.addChild(exportFile);
		} catch (Exception e) {
			System.out.println(e);
		}
		return exportFile;
	}
	
	/*
	 * Returns the memory buffer for the PDF file. 
	 */
	private MemoryFileBuffer getPDFBuffer() throws DocumentException {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);
		
		Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
		PdfWriter writer = PdfWriter.getInstance(document, mos);
		
		String titleKey = _reportModel.getReportTitleLocalizationKey();
		String title = localize(titleKey, titleKey);
		_normalFont = new Font(Font.HELVETICA, 8, Font.NORMAL);
		_boldFont = new Font(Font.HELVETICA, 8, Font.BOLD);
		
		document.addTitle(title);
		document.addAuthor("Agura IT Reports");
		document.addSubject(title);
		document.open();
		
		String dateString = new Date(System.currentTimeMillis()).toString();
		
		document.add(new Phrase(title + " " + dateString + "\n\n", _boldFont));
		document.add(new Phrase("\n\n", _boldFont));

		int cols = _reportModel.getColumnSize() + 1;
		Table table = new Table(cols);
		_widths = new int[cols];
		for (int i = 0; i < cols; i++) {
			_widths[i] = 1;
		}

		table.setCellspacing(1.5f);
		
		buildColumnHeaders(table);
		buildRowHeaders(table);		
		buildReportCells(table);		

		int totalWidth = 0;
		for (int i = 0; i < cols; i++) {
			_widths[i] += 2;
			totalWidth += _widths[i];
		}
		int width = (100 * totalWidth) / 140;
		if (width > 100) {
			width = 100;
		}
		table.setWidth(width);
		table.setWidths(_widths);
		document.add(table);
		document.close();
		writer.setPdfVersion(PdfWriter.VERSION_1_2);

		return buffer;
	}
	
	/**
	 * Builds the report column headers.
	 */
	protected void buildColumnHeaders(Table table) throws BadElementException {
		Header[] headers = _reportModel.getColumnHeaders();
		com.lowagie.text.Cell cell = new com.lowagie.text.Cell();
		cell.setRowspan(2);
		table.addCell(cell, new Point(0, 0));
		int column = 1;
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			Header[] children = header.getChildren();
			if (children == null) {
				String s = null;
				if (header.getHeaderType() == Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER) {
					s = header.getLocalizationKey();
				} else {
					s = localize(header.getLocalizationKey(), header.getLocalizationKey());
				}
				cell = new com.lowagie.text.Cell(new Phrase(s, _normalFont));
				cell.setRowspan(2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM); 
				table.addCell(cell, new Point(0, column));				
				setColSize(s, column, true);
				column++;
			} else {
				String s = null;
				if (header.getHeaderType() == Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER) {
					s = header.getLocalizationKey();
				} else {
					s = localize(header.getLocalizationKey(), header.getLocalizationKey());
				}
				cell = new com.lowagie.text.Cell(new Phrase(s, _normalFont));
				cell.setColspan(children.length);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
				table.addCell(cell, new Point(0, column));
				if (children.length == 1) {
					setColSize(s, column, false);
				}
				for (int j = 0; j < children.length; j++) {
					Header child = children[j];
					s = null;
					if (child.getHeaderType() == Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER) {
						s = child.getLocalizationKey();
					} else {
						s = localize(child.getLocalizationKey(), child.getLocalizationKey());
					}
					cell = new com.lowagie.text.Cell(new Phrase(s, _normalFont));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM); 
					cell.setNoWrap(true);
					table.addCell(cell, new Point(1, column + j));
					setColSize(s, column + j, false);
				}
				column += children.length;
			}
		}
	}

	/**
	 * Builds the report row headers.
	 */
	protected void buildRowHeaders(Table table) throws BadElementException {
		Header[] headers = _reportModel.getRowHeaders();
		int row = 2;
		String s = null;
		com.lowagie.text.Cell cell = null;
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			Header[] children = header.getChildren();
			if (children == null) {
				int headerType = header.getHeaderType();
				if (headerType == Header.HEADERTYPE_ROW_SPACER) {
					s = " ";
				} else if (headerType == Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER
						|| header.getHeaderType() == Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL) {
					s = header.getLocalizationKey();
				} else {
					s = localize(header.getLocalizationKey(), header.getLocalizationKey());					
				}
				cell = new com.lowagie.text.Cell(new Phrase(s, _boldFont));
				if (headerType == Header.HEADERTYPE_ROW_LABEL || headerType == Header.HEADERTYPE_ROW_SPACER) {
					cell.setColspan(_reportModel.getColumnSize() + 1);
				}
				cell.setNoWrap(true);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE); 
				table.addCell(cell, new Point(row, 0));
				setColSize(s, 0, false);
				row++;
			} else {
				if (header.getHeaderType() == Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER
						|| header.getHeaderType() == Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL) {
					s = header.getLocalizationKey();
				} else {
					s = localize(header.getLocalizationKey(), header.getLocalizationKey());					
				}
				cell = new com.lowagie.text.Cell(new Phrase(s, _normalFont));
				cell.setColspan(_reportModel.getColumnSize() + 1);
				cell.setLeading(16);
				table.addCell(cell, new Point(row, 0));
				row++;
				for (int j = 0; j < children.length; j++) {
					Header child = children[j];
					int headerType = child.getHeaderType();
					if (headerType == Header.HEADERTYPE_ROW_SPACER) {
						s = " ";
					} else if (headerType == Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER ||
							headerType == Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL) {
						s = child.getLocalizationKey();
					} else {
						s = localize(child.getLocalizationKey(), child.getLocalizationKey());					
					}
					cell = new com.lowagie.text.Cell(new Phrase(s, _boldFont));
					if (headerType == Header.HEADERTYPE_ROW_LABEL || headerType == Header.HEADERTYPE_ROW_SPACER) {
						cell.setColspan(_reportModel.getColumnSize() + 1);
					}
					cell.setNoWrap(true);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE); 
					table.addCell(cell, row, 0);
					setColSize(s, 0, false);
					row++;
				}
			}
		}
	}

	/**
	 * Builds the report cells.
	 */
	protected void buildReportCells(Table table) throws BadElementException {
		int cellRow = 0;
		int tableRow = 2;
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
					int align = Element.ALIGN_RIGHT;
					Font font = _normalFont;
					String s = null;
					
					switch (cell.getCellType()) {
						case Cell.CELLTYPE_PERCENT:
							s = formatter.format(cell.getFloatValue());
							break;
						case Cell.CELLTYPE_ROW_HEADER:
							s = cell.getStringValue();
							font = _boldFont;
							align = Element.ALIGN_LEFT;
							break;
						case Cell.CELLTYPE_SUM:
							s = formatNumber(cell.getValue());
							font = _boldFont;
							break;
						case Cell.CELLTYPE_TOTAL:
							s = formatNumber(cell.getValue());
							font = _boldFont;
							break;
						default:
							s = formatNumber(cell.getValue());
							break;
					}
					int tableColumn = cellColumn + 1;
					com.lowagie.text.Cell pdfCell = new com.lowagie.text.Cell(new Phrase(s, font));
					pdfCell.setHorizontalAlignment(align);
					pdfCell.setNoWrap(true);
					pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE); 
					table.addCell(pdfCell, new Point(tableRow, tableColumn));
					setColSize(s, tableColumn, false);
				}
				cellRow++;
				tableRow++;
			}
		}
		_reportModel.close();
	}

	/*
	 * Sets a column size.
	 */
	private void setColSize(String text, int column, boolean wrap) {
		String s = "";
		if (wrap) {
			StringTokenizer st = new StringTokenizer(text, " ");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				int tlen = token.length();
				if (tlen > s.length()) {
					s = token;
				}
			}
		} else {
			s = text;
		}
		int len = s.length();
		if (_widths[column] < len) {
			_widths[column] = len;
		}
	}
	
	/*
	 * Returns a string with space for numbers larger than 999.
	 */
	String formatNumber(int n) {
		if (n == 0) {
			return " ";
		}
		String s = "" + n;
		int length = s.length();
		if (length > 3) {
			s = s.substring(0, (length - 3)) + " " + s.substring(length - 3);
		}
		return s;
	}
	
	/*
	 * Returns the localized text for the specified key.
	 */
	private String localize(String key, String defaultText) {
		return _iwrb.getLocalizedString(key, defaultText);
	}
}
