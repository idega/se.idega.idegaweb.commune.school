package se.idega.idegaweb.commune.school.presentation;

import java.util.Collection;
import java.util.Iterator;
import java.rmi.RemoteException;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.Form;
import com.idega.presentation.text.Text;

import se.idega.idegaweb.commune.school.business.SchoolMarksBusiness;
import se.idega.idegaweb.commune.school.business.SchoolStatistics;
import se.idega.idegaweb.commune.presentation.CommuneBlock;


/**
 * Presents school marks statistics in a formatted table (first version)
 * not approved by Nacka :-(
 * <p>
 * $Id: SchoolMarksPresenter.java,v 1.4 2006/04/09 11:39:53 laddi Exp $
 *
 * @author <a href="mailto:kjell@lindman.com">Kjell Lindman</a>
 * @version $version$
 */
public class SchoolMarksPresenter  extends CommuneBlock {

	SchoolMarksBusiness smBiz = null;

	private Form mainForm = null;
	private IWResourceBundle iwrb = null;
	private String blockWidth = "100%";
	private String headerColor = "#003366";
	
	private String headerStyle 	= "font-face:verdana; font-size:10px; font-weight: bold; color:#000000;";
	private String linkStyle 	= "font-face:verdana; font-size:10px; font-weight: bold; color:#000080;";
	private String textStyle 	= "font-face:verdana; font-size:10px; font-weight:plain;";

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void setWidth(String width) {
		this.blockWidth = width;
	}

	public String setWidth() {
		return this.blockWidth;
	}

	public void setHeaderColor(String color) {
		this.headerColor = color;
	}

	public void setLinkStyle(String style) {
		this.linkStyle = style;
	}

	public String getHeaderColor() {
		return this.headerColor;
	}

	public void setHeaderStyle(String style) {
		this.headerStyle = style;
	}

	public void setTextStyle(String style) {
		this.textStyle = style;
	}

	public String getHeaderStyle() {
		return this.headerStyle;
	}
	
	public String getLinkStyle() {
		return this.linkStyle;
	}

	public String getTextStyle() {
		return this.textStyle;
	}

	public void main(IWContext iwc) throws Exception {
		this.iwrb = getResourceBundle(iwc);
		this.mainForm = new Form();
		init(iwc);
		try {
			viewSchoolMarksList(iwc);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
		add(this.mainForm);	
	}

	private void viewSchoolMarksList(IWContext iwc) {	
		
		this.iwrb.getLocalizedString("school_marks_statistics.merite", "Meritvärde");
		int row = 0;
		
		Table table1 = getMarksTable(this.iwrb.getLocalizedString("school_marks_stats.merite", "Meritvärde"),
									 this.iwrb.getLocalizedString("school_marks_stats.marks", "Betygspoäng"),
									 this.iwrb.getLocalizedString("school_marks_stats.auth", "Behörighetspoäng"));

		Table table2 = getMarksTable(this.iwrb.getLocalizedString("school_marks_stats.mvgshare", "Antal och andel G, VG, MVG"),
									 this.iwrb.getLocalizedString("school_marks_stats.failed", "Antal och andel ej godkända"),
									 this.iwrb.getLocalizedString("school_marks_stats.nogoal", "Antal som ej uppnått mål i beh. givamde ämne"));
										

		Collection marks = null;
		try {
			marks = this.smBiz.findSchoolMarksStatistics(iwc);
			Iterator iter1 = marks.iterator();
			Iterator iter2 = marks.iterator();
			
			this.smBiz.resetMeanValues();
			while (iter1.hasNext()) {
				SchoolStatistics stat = (SchoolStatistics) iter1.next();

				this.smBiz.sumMeriteValue(""+stat.getMeriteValue().number);
				this.smBiz.sumMarksPoints(""+stat.getMarksPoints().number);
				this.smBiz.sumAuthPoints(""+stat.getAuthPoints().number);

				this.smBiz.sumMarksNumberShare(""+stat.getMarksNumberShare().number);
				this.smBiz.sumMarksFailed(""+stat.getMarksFailed().number);
				this.smBiz.sumMarksNoGoal(""+stat.getMarksNoGoal().number);
					
			}
			row = 3;
			
			while (iter2.hasNext()) {
				SchoolStatistics stat = (SchoolStatistics) iter2.next();
				table1.add(getSmallText(stat.getSchoolName()), 1, row);
				
				table1.add(getSmallText(stat.getMeriteValue().number), 2, row);
				table1.add(formatPct(stat.getMeriteValue().number, this.smBiz.getSumMeriteValue()), 3, row);

				table1.add(getSmallText(stat.getMarksPoints().number), 4, row);
				table1.add(formatPct(stat.getMarksPoints().number, this.smBiz.getSumMarksPoints()), 5, row);

				table1.add(getSmallText(stat.getAuthPoints().number), 6, row);
				table1.add(formatPct(stat.getAuthPoints().number, this.smBiz.getSumAuthPoints()), 7, row);

				table2.add(getSmallText(stat.getSchoolName()), 1, row);

				table2.add(getSmallText(stat.getMarksNumberShare().number), 2, row);
				table2.add(formatPct(stat.getMarksNumberShare().number, this.smBiz.getSumMarksNumberShare()), 3, row);

				table2.add(getSmallText(stat.getMarksFailed().number), 4, row);
				table2.add(formatPct(stat.getMarksFailed().number, this.smBiz.getSumMarksFailed()), 5, row);

				table2.add(getSmallText(stat.getMarksNoGoal().number), 6, row);
				table2.add(formatPct(stat.getMarksNoGoal().number, this.smBiz.getSumMarksNoGoal()), 7, row);

				if (row % 2 == 0) { 
					table1.setRowColor(row, getZebraColor1());
					table2.setRowColor(row, getZebraColor1());
				} else {
					table1.setRowColor(row, getZebraColor2());
					table2.setRowColor(row, getZebraColor2());
				}
				row++;
			}

			table1.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.wholecom", "Hela kommunen")), 1, row+1);			
			table1.add(getSmallText(round(this.smBiz.getCommuneMeanMeriteValue(), 0)), 2, row+1);
			table1.add(getSmallText(round(this.smBiz.getCommuneMeanMarksPoints(), 2)), 4, row+1);
			table1.add(getSmallText(round(this.smBiz.getCommuneMeanAuthPoints(), 0)), 6, row+1);

			table2.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.wholecom", "Hela kommunen")), 1, row+1);			
			table2.add(getSmallText(round(this.smBiz.getCommuneMeanMarksNumberShare(), 0)), 2, row+1);
			table2.add(getSmallText(round(this.smBiz.getCommuneMeanMarksFailed(), 0)), 4, row+1);
			table2.add(getSmallText(round(this.smBiz.getCommuneMeanMarksNoGoal(), 0)), 6, row+1);
			
		} catch (RemoteException e) {
			e.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();		
		}


/*
		try {

			SchoolStatistics stat = smBiz.findCommuneStatistics(iwc);

			table1.add(getSmallText(stat.getMeriteValue().number), 2, row+1);
			table1.add(getSmallText(stat.getMeriteValue().percent), 3, row+1);
			table1.add(getSmallText(stat.getMarksPoints().number), 4, row+1);
			table1.add(getSmallText(stat.getMarksPoints().percent), 5, row+1);
			table1.add(getSmallText(stat.getAuthPoints().number), 6, row+1);
			table1.add(getSmallText(stat.getAuthPoints().percent), 7, row+1);
			
			table2.add(getSmallText(stat.getMarksNumberShare().number), 2, row+1);
			table2.add(getSmallText(stat.getMarksNumberShare().percent), 3, row+1);
			table2.add(getSmallText(stat.getMarksFailed().number), 4, row+1);
			table2.add(getSmallText(stat.getMarksFailed().percent), 5, row+1);
			table2.add(getSmallText(stat.getMarksNoGoal().number), 6, row+1);
			table2.add(getSmallText(stat.getMarksNoGoal().percent), 7, row+1);
			
		} catch (RemoteException e) {
			e.printStackTrace();		
		}	
*/
		this.mainForm.add(table1);
		this.mainForm.add(new Break());
		this.mainForm.add(table2);

	}

	private Text formatPct(String number, String sum) {

		try {
			float n = Float.parseFloat(number);
			float s = Float.parseFloat(sum);
			float pct = 0;
			if(s != 0) {
				pct = (n / s)*100;
			}
			float pctDec = (float) (Math.rint(pct * 100) / 100);
			return getSmallText("" + pctDec);
		} catch (NumberFormatException e) {}
		return getSmallText("0");		
	}

	private String round(String value, int dec) {

		try {
			float ggr = (float) Math.pow(10, dec);
			float n = Float.parseFloat(value);
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


	private Table getMarksTable(String header1, String header2, String header3) {
		
		Table ret = new Table();
		ret.setColumns(7);
		ret.setRows(14);
		ret.setBorderColor("#cccccc");
		ret.setBorder(1);
		ret.setCellspacing("0");
		ret.setCellpadding("3");
		ret.setWidth(this.blockWidth);
		ret.mergeCells(1, 1, 1, 2);

		ret.mergeCells(2, 1, 3, 1);
		ret.mergeCells(4, 1, 5, 1);
		ret.mergeCells(6, 1, 7, 1);

		ret.add(getSmallText(header1), 2, 1);			
		ret.add(getSmallText(header2), 4, 1);			
		ret.add(getSmallText(header3), 6, 1);

		ret.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.value", "Värde")), 2, 2);			
		ret.add(getSmallText("%"), 3, 2);			
		ret.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.value", "Värde")), 4, 2);			
		ret.add(getSmallText("%"), 5, 2);			
		ret.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.value", "Värde")), 6, 2);			
		ret.add(getSmallText("%"), 7, 2);			

		ret.setColor(2, 1, "#aaaaaa");			
		ret.setColor(4, 1, "#aaaaaa");			
		ret.setColor(6, 1, "#aaaaaa");			
		return ret;
	}

	private void init(IWContext iwc) {
		try {
			this.smBiz = getSchoolMarksBusiness(iwc);
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

