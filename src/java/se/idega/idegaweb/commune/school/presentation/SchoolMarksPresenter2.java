package se.idega.idegaweb.commune.school.presentation;

import java.util.Collection;
import java.util.Iterator;
import java.rmi.RemoteException;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;

import se.idega.idegaweb.commune.school.business.SchoolMarksBusiness;
import se.idega.idegaweb.commune.school.business.SchoolStatistics;
import se.idega.idegaweb.commune.presentation.CommuneBlock;


/**
 * Presents school marks statistics in a formatted table (approved by Nacka)
 * <p>
 * $Id: SchoolMarksPresenter2.java,v 1.1 2003/09/25 07:59:39 kjell Exp $
 *
 * @author <a href="mailto:kjell@lindman.com">Kjell Lindman</a>
 * @version $version$
 */
public class SchoolMarksPresenter2  extends CommuneBlock {

	SchoolMarksBusiness smBiz = null;

	private Form mainForm = null;
	private IWResourceBundle iwrb = null;
	private String blockWidth = "100%";
	private String headerColor = "#003366";
	private int currPage = 0;
	
	private String headerStyle 	= "font-face:verdana; font-size:10px; font-weight: bold; color:#000000;";
	private String linkStyle 	= "font-face:verdana; font-size:10px; font-weight: bold; color:#000080;";
	private String textStyle 	= "font-face:verdana; font-size:10px; font-weight:plain;";

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void setWidth(String width) {
		blockWidth = width;
	}

	public String setWidth() {
		return blockWidth;
	}

	public void setHeaderColor(String color) {
		headerColor = color;
	}

	public void setLinkStyle(String style) {
		linkStyle = style;
	}

	public String getHeaderColor() {
		return headerColor;
	}

	public void setHeaderStyle(String style) {
		headerStyle = style;
	}

	public void setTextStyle(String style) {
		textStyle = style;
	}

	public String getHeaderStyle() {
		return headerStyle;
	}
	
	public String getLinkStyle() {
		return linkStyle;
	}

	public String getTextStyle() {
		return textStyle;
	}

	public void main(IWContext iwc) throws Exception {
		iwrb = getResourceBundle(iwc);
		mainForm = new Form();
		init(iwc);
		try {
			viewSchoolMarksList(iwc);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
		add(mainForm);	
	}

	private void viewSchoolMarksList(IWContext iwc) {	
		
		iwrb.getLocalizedString("school_marks_statistics.merite", "Meritvärde");
		int row = 0;
		
		Table table = getMarksTable();

		Collection marks = null;
		try {
			marks = smBiz.findSchoolMarksStatistics(iwc);
			Iterator iter = marks.iterator();
			
			row = 1;

			smBiz.resetMeanValues(); 

			while (iter.hasNext()) {
				SchoolStatistics stat = (SchoolStatistics) iter.next();
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.eg", "Ej godkänt")), 2, row);			
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.g", "Godkänt")), 4, row);			
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.vg", "Väl godkänt")), 6, row);
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.mvg", "Mycket väl godkänt")), 8, row);
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.tot", "Totalt")), 10, row);
				table.setColor(2, row, "#aaaaaa");			
				table.setColor(4, row, "#aaaaaa");			
				table.setColor(6, row, "#aaaaaa");			
				table.setColor(8, row, "#aaaaaa");			
				table.setColor(10, row, "#aaaaaa");			
				table.mergeCells(2, row, 3, row);
				table.mergeCells(4, row, 5, row);
				table.mergeCells(6, row, 7, row);
				table.mergeCells(8, row, 9, row);
				table.mergeCells(10, row, 11, row);
				row++;
				table.add(getHeader(stat.getSchoolName()), 1, row);
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 2, row);			
				table.add(getSmallText("%"), 3, row);			
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 4, row);			
				table.add(getSmallText("%"), 5, row);			
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 6, row);			
				table.add(getSmallText("%"), 7, row);			
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 8, row);			
				table.add(getSmallText("%"), 9, row);			
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 10, row);			
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.mean", "Medel")), 11, row);			
				row++;
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.english", "Engelska")), 1, row);

				table.add(getSmallText(stat.getEnglishMarks().eg.number), 2, row);
				table.add(getSmallText(formatPct(stat.getEnglishMarks().eg.percent)), 3, row);
				table.add(getSmallText(stat.getEnglishMarks().g.number), 4, row);
				table.add(getSmallText(formatPct(stat.getEnglishMarks().g.percent)), 5, row);
				table.add(getSmallText(stat.getEnglishMarks().vg.number), 6, row);
				table.add(getSmallText(formatPct(stat.getEnglishMarks().vg.percent)), 7, row);
				table.add(getSmallText(stat.getEnglishMarks().mvg.number), 8, row);
				table.add(getSmallText(formatPct(stat.getEnglishMarks().mvg.percent)), 9, row);
				table.add(getSmallText(stat.getEnglishMarks().tot.number), 10, row);
				table.add(getSmallText(formatPct(stat.getEnglishMarks().tot.percent)), 11, row);

				row++;
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.maths", "Matematik")), 1, row);
				table.add(getSmallText(stat.getMathsMarks().eg.number), 2, row);
				table.add(getSmallText(formatPct(stat.getMathsMarks().eg.percent)), 3, row);
				table.add(getSmallText(stat.getMathsMarks().g.number), 4, row);
				table.add(getSmallText(formatPct(stat.getMathsMarks().g.percent)), 5, row);
				table.add(getSmallText(stat.getMathsMarks().vg.number), 6, row);
				table.add(getSmallText(formatPct(stat.getMathsMarks().vg.percent)), 7, row);
				table.add(getSmallText(stat.getMathsMarks().mvg.number), 8, row);
				table.add(getSmallText(formatPct(stat.getMathsMarks().mvg.percent)), 9, row);
				table.add(getSmallText(stat.getMathsMarks().tot.number), 10, row);
				table.add(getSmallText(formatPct(stat.getMathsMarks().tot.percent)), 11, row);

				row++;
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.swedish", "Svenska")), 1, row);
				table.add(getSmallText(stat.getSwedishMarks().eg.number), 2, row);
				table.add(getSmallText(formatPct(stat.getSwedishMarks().eg.percent)), 3, row);
				table.add(getSmallText(stat.getSwedishMarks().g.number), 4, row);
				table.add(getSmallText(formatPct(stat.getSwedishMarks().g.percent)), 5, row);
				table.add(getSmallText(stat.getSwedishMarks().vg.number), 6, row);
				table.add(getSmallText(formatPct(stat.getSwedishMarks().vg.percent)), 7, row);
				table.add(getSmallText(stat.getSwedishMarks().mvg.number), 8, row);
				table.add(getSmallText(formatPct(stat.getSwedishMarks().mvg.percent)), 9, row);
				table.add(getSmallText(stat.getSwedishMarks().tot.number), 10, row);
				table.add(getSmallText(formatPct(stat.getSwedishMarks().tot.percent)), 11, row);

				row++;
				table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.swedish2", "Svenska som andraspråk")), 1, row);
				table.add(getSmallText(stat.getSwedish2Marks().eg.number), 2, row);
				table.add(getSmallText(formatPct(stat.getSwedish2Marks().eg.percent)), 3, row);
				table.add(getSmallText(stat.getSwedish2Marks().g.number), 4, row);
				table.add(getSmallText(formatPct(stat.getSwedish2Marks().g.percent)), 5, row);
				table.add(getSmallText(stat.getSwedish2Marks().vg.number), 6, row);
				table.add(getSmallText(formatPct(stat.getSwedish2Marks().vg.percent)), 7, row);
				table.add(getSmallText(stat.getSwedish2Marks().mvg.number), 8, row);
				table.add(getSmallText(formatPct(stat.getSwedish2Marks().mvg.percent)), 9, row);
				table.add(getSmallText(stat.getSwedish2Marks().tot.number), 10, row);
				table.add(getSmallText(formatPct(stat.getSwedish2Marks().tot.percent)), 11, row);

				row++;
				table.add(getHeader(iwrb.getLocalizedString("school_marks_stats.sumauthmarks", "Summa behörighetsgivande poäng")), 1, row);
				table.add(getHeader(stat.getSumAuthMarks().eg.number), 2, row);
				table.add(getHeader(formatPct(stat.getSumAuthMarks().eg.percent)), 3, row);
				table.add(getHeader(stat.getSumAuthMarks().g.number), 4, row);
				table.add(getHeader(formatPct(stat.getSumAuthMarks().g.percent)), 5, row);
				table.add(getHeader(stat.getSumAuthMarks().vg.number), 6, row);
				table.add(getHeader(formatPct(stat.getSumAuthMarks().vg.percent)), 7, row);
				table.add(getHeader(stat.getSumAuthMarks().mvg.number), 8, row);
				table.add(getHeader(formatPct(stat.getSumAuthMarks().mvg.percent)), 9, row);
				table.add(getHeader(stat.getSumAuthMarks().tot.number), 10, row);
				table.add(getHeader(formatPct(stat.getSumAuthMarks().tot.percent)), 11, row);

				row++;
				table.add(getHeader(iwrb.getLocalizedString("school_marks_stats.total", "Totalt denna skola")), 1, row);
				table.add(getHeader(stat.getTotalMarks().eg.number), 2, row);
				table.add(getHeader(formatPct(stat.getTotalMarks().eg.percent)), 3, row);
				table.add(getHeader(stat.getTotalMarks().g.number), 4, row);
				table.add(getHeader(formatPct(stat.getTotalMarks().g.percent)), 5, row);
				table.add(getHeader(stat.getTotalMarks().vg.number), 6, row);
				table.add(getHeader(formatPct(stat.getTotalMarks().vg.percent)), 7, row);
				table.add(getHeader(stat.getTotalMarks().mvg.number), 8, row);
				table.add(getHeader(formatPct(stat.getTotalMarks().mvg.percent)), 9, row);
				table.add(getHeader(stat.getTotalMarks().tot.number), 10, row);
				table.add(getHeader(formatPct(stat.getTotalMarks().tot.percent)), 11, row);
				row++;
				table.add(getHeader(iwrb.getLocalizedString("school_marks_stats.merite_value", "Meritvärde")+ ": " +stat.getMeriteValue().number), 1, row);
				row++;


				table.mergeCells(1, row, 11, row);
				row++;

			}

			
		} catch (RemoteException e) {
			e.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();		
		}
		mainForm.add(table);

	}

	private String formatPct(String number) {
		try {
			if(number == null) {
				return "0";
			}
			float pctDec = (float) (Math.rint(Float.parseFloat(number) * 10) / 10);
			return "" + pctDec;
		} catch (NumberFormatException e) {}
		return "0";		
	}

	private String round(String value, int dec) {

		try {
			float ggr = (float) Math.pow(10, dec);
			float n = Float.parseFloat(value);
			float pctDec = 0;
			if(dec == 0) {
				int p = (""+value).indexOf(".");
				if (p == -1) {
					p = (""+value).length();
				}
				return (""+value).substring(0, p);
			} else {
				return "" + (Math.rint(n * ggr) / ggr);
			}
		} catch (NumberFormatException e) {}
		return "0";			
	}


	private Table getMarksTable() {
		
		Table ret = new Table();
		ret.setColumns(11);
		ret.setRows(6);
		ret.setBorderColor("#cccccc");
		ret.setBorder(1);
		ret.setCellspacing("0");
		ret.setCellpadding("3");
		ret.setWidth(blockWidth);

		
		

		return ret;
	}

	private void init(IWContext iwc) {
		try {
			smBiz = getSchoolMarksBusiness(iwc);
		} catch (RemoteException e) {
			e.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();		
		}
	}

	private SchoolMarksBusiness getSchoolMarksBusiness(IWContext iwc) throws Exception {
		return (SchoolMarksBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, SchoolMarksBusiness.class);
	}

}

