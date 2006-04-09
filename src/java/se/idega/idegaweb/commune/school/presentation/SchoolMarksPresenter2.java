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
import se.idega.idegaweb.commune.school.business.SchoolMarkValues;
import se.idega.idegaweb.commune.presentation.CommuneBlock;


/**
 * Presents school marks statistics in a formatted table (approved by Nacka)
 * <p>
 * $Id: SchoolMarksPresenter2.java,v 1.11 2006/04/09 11:39:53 laddi Exp $
 *
 * This block presents School Marks Statistics according to the specifications made my 
 * Jill Salander
 * Uppdragskontorer / Team SF
 * Nacka Kommun
 *
 * "Redovisning av betyg i skolår 9"
 *
 * The school marks originate from an imported "School Marks file" called 0182.dat which is delivered 
 * from SCB Statistiska Centralbyrån. This file is delevered once a year. 0182 is the SCB Commune code.
 *
 * The "School Marks" calculation takes about 3 minutes the first round. The application however stores
 * these first result in SchoolStatisticsDataBMPBean for fast retrieval on the main website.
 * 
 * All algorithms for school marks calculation are done in the SchoolMarksBusiness 
 * 
 * A last minute presentation change (2003-10-02) made me do a "Total" calculation in the presentation
 * see method sumSchoolTally. The schools are summed on the new field SchoolManagementType. 
 * All "kommunal" schools are added together and presented as a "total"-presentation (half way in the statistics). 
 * At the end the whole commune is summed (including the private schools). 
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

	private SchoolMarkValues _tallyEnglish;
	private SchoolMarkValues _tallyMaths;
	private SchoolMarkValues _tallySwedish;
	private SchoolMarkValues _tallySwedish2;
	private SchoolMarkValues _tallyAuth;
	private SchoolMarkValues _tallyTotal;
	
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
		Table table = getMarksTable();
		Collection marks = null;

		try {
			marks = this.smBiz.findSchoolMarksStatistics(iwc);
			Iterator iter = marks.iterator();
			row = 1;
			this.smBiz.resetMeanValues();
			resetTally(); 
			String smtOld = "";
			String smtNew = "";
			int count = 0;
			int mValue = 0;
			int mNumber = 0;
			while (iter.hasNext()) {
				SchoolStatistics stat = (SchoolStatistics) iter.next();
				smtNew = stat.getSchoolManagementType();
				if(smtOld.compareTo(smtNew) != 0) {
					smtOld = stat.getSchoolManagementType();
					if(count++  > 0) {
						table = insertTotals(this.iwrb.getLocalizedString("school_marks_stats.header1", "Totalt kommunala skolor"), table, row, roundAbout(mValue, mNumber));
						row +=10;
					}
				}
				table.add(getHeader(stat.getSchoolName()), 1, row);
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.eg", "Ej godkänt")), 2, row);			
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.g", "Godkänt")), 4, row);			
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.vg", "Väl godkänt")), 6, row);
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.mvg", "Mycket väl godkänt")), 8, row);
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.tot", "Totalt")), 10, row);
				table.setColor(1, row, "#aaaaaa");			
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

				table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER);
				row++;
				table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
				
				table.add(getHeader(this.iwrb.getLocalizedString("school_marks_stats.table_auth_marks_header", "Behörighetsgivande betyg")), 1, row);			
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 2, row);			
				table.add(getSmallText("%"), 3, row);			
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 4, row);			
				table.add(getSmallText("%"), 5, row);			
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 6, row);			
				table.add(getSmallText("%"), 7, row);			
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 8, row);			
				table.add(getSmallText("%"), 9, row);			
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 10, row);			
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.mean", "Medel")), 11, row);			

				row++;
				table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
				sumSchoolTally(stat.getEnglishMarks(), "English");
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.english", "Engelska")), 1, row);
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
				table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
				sumSchoolTally(stat.getMathsMarks(), "Maths");
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.maths", "Matematik")), 1, row);
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
				table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
				sumSchoolTally(stat.getSwedishMarks(), "Swedish");
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.swedish", "Svenska")), 1, row);
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
				table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
				if (containsValues(stat.getSwedish2Marks())) {
					sumSchoolTally(stat.getSwedish2Marks(), "Swedish2");
					table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.swedish2", "Svenska som andraspråk")), 1, row);
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
					table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
					table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
				}

				sumSchoolTally(stat.getSumAuthMarks(), "Auth");
				table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.sumauthmarks", "Summa behörighetsgivande poäng")), 1, row);
				table.add(getSmallText(stat.getSumAuthMarks().eg.number), 2, row);
				table.add(getSmallText(formatPct(stat.getSumAuthMarks().eg.percent)), 3, row);
				table.add(getSmallText(stat.getSumAuthMarks().g.number), 4, row);
				table.add(getSmallText(formatPct(stat.getSumAuthMarks().g.percent)), 5, row);
				table.add(getSmallText(stat.getSumAuthMarks().vg.number), 6, row);
				table.add(getSmallText(formatPct(stat.getSumAuthMarks().vg.percent)), 7, row);
				table.add(getSmallText(stat.getSumAuthMarks().mvg.number), 8, row);
				table.add(getSmallText(formatPct(stat.getSumAuthMarks().mvg.percent)), 9, row);
				table.add(getSmallText(stat.getSumAuthMarks().tot.number), 10, row);
				table.add(getSmallText(formatPct(stat.getSumAuthMarks().tot.percent)), 11, row);

				row++;
				table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
				sumSchoolTally(stat.getTotalMarks(), "Total");
				table.add(getHeader(this.iwrb.getLocalizedString("school_marks_stats.total", "Totalt denna skola")), 1, row);
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
				table.add(getHeader(this.iwrb.getLocalizedString("school_marks_stats.merite_value", "Meritvärde")+ ": " +stat.getMeriteValue().number), 1, row);

//				mValue += Integer.parseInt(stat.getMeriteValue().number);
//				mNumber++;

					mValue += Integer.parseInt(stat.getTotalAuthPoints());
				mNumber+= Integer.parseInt(stat.getTotalAuthStudents());
				
				row++;

				table.mergeCells(1, row, 11, row);
				row++;
			}
			table = insertTotals(this.iwrb.getLocalizedString("school_marks_stats.header2", "Totalt Nacka skolor"), table, row, roundAbout(mValue, mNumber));
		} catch (RemoteException e) {
			e.printStackTrace();		
			} catch (Exception e) {
			e.printStackTrace();		
		}
		this.mainForm.add(table);

	}

	private Table insertTotals(String header, Table table, int row, String meanMerite) {	
		table.add(getHeader(header), 1, row);			
		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.eg", "Ej godkänt")), 2, row);			
		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.g", "Godkänt")), 4, row);			
		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.vg", "Väl godkänt")), 6, row);
		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.mvg", "Mycket väl godkänt")), 8, row);
		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.tot", "Totalt")), 10, row);
		table.setColor(1, row, "#aaaaaa");			
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

		table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER);

		row++;
		table.setRowColor(row, "#e0e0e0");
		table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_CENTER);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);

		table.add(getHeader(this.iwrb.getLocalizedString("school_marks_stats.table_auth_marks_header", "Behörighetsgivande betyg")), 1, row);			
		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 2, row);			
		table.add(getSmallText("%"), 3, row);			
		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 4, row);			
		table.add(getSmallText("%"), 5, row);			
		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 6, row);			
		table.add(getSmallText("%"), 7, row);			
		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 8, row);			
		table.add(getSmallText("%"), 9, row);			
		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.number", "Antal")), 10, row);			
		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.mean", "Medel")), 11, row);			

		row++;
		table.setRowColor(row, "#e0e0e0");
		table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);

		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.english", "Engelska")), 1, row);
		table.add(getSmallText(this._tallyEnglish.eg.number), 2, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyEnglish.eg.percent, this._tallyEnglish.ceg))), 3, row);
		table.add(getSmallText(this._tallyEnglish.g.number), 4, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyEnglish.g.percent, this._tallyEnglish.cg))), 5, row);
		table.add(getSmallText(this._tallyEnglish.vg.number), 6, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyEnglish.vg.percent, this._tallyEnglish.cvg))), 7, row);
		table.add(getSmallText(this._tallyEnglish.mvg.number), 8, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyEnglish.mvg.percent, this._tallyEnglish.cmvg))), 9, row);
		table.add(getSmallText(this._tallyEnglish.tot.number), 10, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyEnglish.tot.percent, this._tallyEnglish.ctot))), 11, row);

		row++;
		table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
		table.setRowColor(row, "#e0e0e0");

		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.maths", "Matematik")), 1, row);
		table.add(getSmallText(this._tallyMaths.eg.number), 2, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyMaths.eg.percent, this._tallyMaths.ceg))), 3, row);
		table.add(getSmallText(this._tallyMaths.g.number), 4, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyMaths.g.percent, this._tallyMaths.cg))), 5, row);
		table.add(getSmallText(this._tallyMaths.vg.number), 6, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyMaths.vg.percent, this._tallyMaths.cvg))), 7, row);
		table.add(getSmallText(this._tallyMaths.mvg.number), 8, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyMaths.mvg.percent, this._tallyMaths.cmvg))), 9, row);
		table.add(getSmallText(this._tallyMaths.tot.number), 10, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyMaths.tot.percent, this._tallyMaths.ctot))), 11, row);

		row++;
		table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
		table.setRowColor(row, "#e0e0e0");

		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.swedish", "Svenska")), 1, row);
		table.add(getSmallText(this._tallySwedish.eg.number), 2, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallySwedish.eg.percent, this._tallySwedish.ceg))), 3, row);
		table.add(getSmallText(this._tallySwedish.g.number), 4, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallySwedish.g.percent, this._tallySwedish.cg))), 5, row);
		table.add(getSmallText(this._tallySwedish.vg.number), 6, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallySwedish.vg.percent, this._tallySwedish.cvg))), 7, row);
		table.add(getSmallText(this._tallySwedish.mvg.number), 8, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallySwedish.mvg.percent, this._tallySwedish.cmvg))), 9, row);
		table.add(getSmallText(this._tallySwedish.tot.number), 10, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallySwedish.tot.percent, this._tallySwedish.ctot))), 11, row);

		row++;
		table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
		table.setRowColor(row, "#e0e0e0");

		if (containsValues(this._tallySwedish2)) {
			table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.swedish2", "Svenska som andraspråk")), 1, row);
			table.add(getSmallText(this._tallySwedish2.eg.number), 2, row);
			table.add(getSmallText(formatPct(tallyMean(this._tallySwedish2.eg.percent, this._tallySwedish2.ceg))), 3, row);
			table.add(getSmallText(this._tallySwedish2.g.number), 4, row);
			table.add(getSmallText(formatPct(tallyMean(this._tallySwedish2.g.percent, this._tallySwedish2.cg))), 5, row);
			table.add(getSmallText(this._tallySwedish2.vg.number), 6, row);
			table.add(getSmallText(formatPct(tallyMean(this._tallySwedish2.vg.percent, this._tallySwedish2.cvg))), 7, row);
			table.add(getSmallText(this._tallySwedish2.mvg.number), 8, row);
			table.add(getSmallText(formatPct(tallyMean(this._tallySwedish2.mvg.percent, this._tallySwedish2.cmvg))), 9, row);
			table.add(getSmallText(this._tallySwedish2.tot.number), 10, row);
			table.add(getSmallText(formatPct(tallyMean(this._tallySwedish2.tot.percent, this._tallySwedish2.ctot))), 11, row);
			row++;
			table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
			table.setRowColor(row, "#e0e0e0");
		}

		table.add(getSmallText(this.iwrb.getLocalizedString("school_marks_stats.sumauthmarks", "Summa behörighetsgivande poäng")), 1, row);
		table.add(getSmallText(this._tallyAuth.eg.number), 2, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyAuth.eg.percent, this._tallyAuth.ceg))), 3, row);
		table.add(getSmallText(this._tallyAuth.g.number), 4, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyAuth.g.percent, this._tallyAuth.cg))), 5, row);
		table.add(getSmallText(this._tallyAuth.vg.number), 6, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyAuth.vg.percent, this._tallyAuth.cvg))), 7, row);
		table.add(getSmallText(this._tallyAuth.mvg.number), 8, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyAuth.mvg.percent, this._tallyAuth.cmvg))), 9, row);
		table.add(getSmallText(this._tallyAuth.tot.number), 10, row);
		table.add(getSmallText(formatPct(tallyMean(this._tallyAuth.tot.percent, this._tallyAuth.ctot))), 11, row);

		row++;
		table.setRowAlignment(row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_LEFT);
		table.setRowColor(row, "#e0e0e0");

		table.add(getHeader(header), 1, row);
		table.add(getHeader(this._tallyTotal.eg.number), 2, row);
		table.add(getHeader(formatPct(tallyMean(this._tallyTotal.eg.percent, this._tallyTotal.ceg))), 3, row);
		table.add(getHeader(this._tallyTotal.g.number), 4, row);
		table.add(getHeader(formatPct(tallyMean(this._tallyTotal.g.percent, this._tallyTotal.cg))), 5, row);
		table.add(getHeader(this._tallyTotal.vg.number), 6, row);
		table.add(getHeader(formatPct(tallyMean(this._tallyTotal.vg.percent, this._tallyTotal.cvg))), 7, row);
		table.add(getHeader(this._tallyTotal.mvg.number), 8, row);
		table.add(getHeader(formatPct(tallyMean(this._tallyTotal.mvg.percent, this._tallyTotal.cmvg))), 9, row);
		table.add(getHeader(this._tallyTotal.tot.number), 10, row);
		table.add(getHeader(formatPct(tallyMean(this._tallyTotal.tot.percent, this._tallyTotal.ctot))), 11, row);
		row++;
		table.add(getHeader(this.iwrb.getLocalizedString("school_marks_stats.merite_value", "Meritvärde")+ ": "+meanMerite), 1, row);
		table.setRowColor(row, "#e0e0e0");
		row++;

		table.mergeCells(1, row, 11, row);
		row++;
		return table;
	}

	private boolean containsValues(SchoolMarkValues data) {
		int results = 	Integer.parseInt(data.eg.number) +
						Integer.parseInt(data.g.number) +
						Integer.parseInt(data.vg.number) +
						Integer.parseInt(data.mvg.number);

		return results == 0 ? false : true;
	}

	private void sumSchoolTally(SchoolMarkValues values, String mode) {
		if (mode.compareTo("English") == 0) {
			this._tallyEnglish.eg.number	 = sumInt(this._tallyEnglish.eg.number, values.eg.number);
			this._tallyEnglish.g.number	 = sumInt(this._tallyEnglish.g.number, values.g.number);
			this._tallyEnglish.vg.number	 = sumInt(this._tallyEnglish.vg.number, values.vg.number);
			this._tallyEnglish.mvg.number = sumInt(this._tallyEnglish.mvg.number, values.mvg.number);
			this._tallyEnglish.tot.number = sumInt(this._tallyEnglish.tot.number, values.tot.number);
			this._tallyEnglish.eg.percent  = sumDec(this._tallyEnglish.eg.percent, values.eg.percent);
			this._tallyEnglish.g.percent	  = sumDec(this._tallyEnglish.g.percent, values.g.percent);
			this._tallyEnglish.vg.percent  = sumDec(this._tallyEnglish.vg.percent, values.vg.percent);
			this._tallyEnglish.mvg.percent = sumDec(this._tallyEnglish.mvg.percent, values.mvg.percent);
			this._tallyEnglish.tot.percent = sumDec(this._tallyEnglish.tot.percent, values.tot.percent);
			this._tallyEnglish.ceg		+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			this._tallyEnglish.cg		+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			this._tallyEnglish.cvg		+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			this._tallyEnglish.cmvg		+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			this._tallyEnglish.ctot		+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
		} else if (mode.compareTo("Maths") == 0) {
			this._tallyMaths.eg.number	 = sumInt(this._tallyMaths.eg.number, values.eg.number);
			this._tallyMaths.g.number	 = sumInt(this._tallyMaths.g.number, values.g.number);
			this._tallyMaths.vg.number	 = sumInt(this._tallyMaths.vg.number, values.vg.number);
			this._tallyMaths.mvg.number	 = sumInt(this._tallyMaths.mvg.number, values.mvg.number);
			this._tallyMaths.tot.number	 = sumInt(this._tallyMaths.tot.number, values.tot.number);
			this._tallyMaths.eg.percent	 = sumDec(this._tallyMaths.eg.percent, values.eg.percent);
			this._tallyMaths.g.percent	 = sumDec(this._tallyMaths.g.percent, values.g.percent);
			this._tallyMaths.vg.percent	 = sumDec(this._tallyMaths.vg.percent, values.vg.percent);
			this._tallyMaths.mvg.percent	 = sumDec(this._tallyMaths.mvg.percent, values.mvg.percent);
			this._tallyMaths.tot.percent	 = sumDec(this._tallyMaths.tot.percent, values.tot.percent);
			this._tallyMaths.ceg			+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			this._tallyMaths.cg			+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			this._tallyMaths.cvg			+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			this._tallyMaths.cmvg		+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			this._tallyMaths.ctot		+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
		} else if (mode.compareTo("Swedish") == 0) {
			this._tallySwedish.eg.number  = sumInt(this._tallySwedish.eg.number, values.eg.number);
			this._tallySwedish.g.number   = sumInt(this._tallySwedish.g.number, values.g.number);
			this._tallySwedish.vg.number  = sumInt(this._tallySwedish.vg.number, values.vg.number);
			this._tallySwedish.mvg.number = sumInt(this._tallySwedish.mvg.number, values.mvg.number);
			this._tallySwedish.tot.number = sumInt(this._tallySwedish.tot.number, values.tot.number);
			this._tallySwedish.eg.percent  = sumDec(this._tallySwedish.eg.percent, values.eg.percent);
			this._tallySwedish.g.percent   = sumDec(this._tallySwedish.g.percent, values.g.percent);
			this._tallySwedish.vg.percent  = sumDec(this._tallySwedish.vg.percent, values.vg.percent);
			this._tallySwedish.mvg.percent = sumDec(this._tallySwedish.mvg.percent, values.mvg.percent);
			this._tallySwedish.tot.percent = sumDec(this._tallySwedish.tot.percent, values.tot.percent);
			this._tallySwedish.ceg		+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			this._tallySwedish.cg		+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			this._tallySwedish.cvg		+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			this._tallySwedish.cmvg		+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			this._tallySwedish.ctot		+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
		} else if (mode.compareTo("Swedish2") == 0) {
			this._tallySwedish2.eg.number = sumInt(this._tallySwedish2.eg.number, values.eg.number);
			this._tallySwedish2.g.number  = sumInt(this._tallySwedish2.g.number, values.g.number);
			this._tallySwedish2.vg.number = sumInt(this._tallySwedish2.vg.number, values.vg.number);
			this._tallySwedish2.mvg.number= sumInt(this._tallySwedish2.mvg.number, values.mvg.number);
			this._tallySwedish2.tot.number= sumInt(this._tallySwedish2.tot.number, values.tot.number);
			this._tallySwedish2.eg.percent = sumDec(this._tallySwedish2.eg.percent, values.eg.percent);
			this._tallySwedish2.g.percent  = sumDec(this._tallySwedish2.g.percent, values.g.percent);
			this._tallySwedish2.vg.percent = sumDec(this._tallySwedish2.vg.percent, values.vg.percent);
			this._tallySwedish2.mvg.percent= sumDec(this._tallySwedish2.mvg.percent, values.mvg.percent);
			this._tallySwedish2.tot.percent= sumDec(this._tallySwedish2.tot.percent, values.tot.percent);
			this._tallySwedish2.ceg		+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			this._tallySwedish2.cg		+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			this._tallySwedish2.cvg		+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			this._tallySwedish2.cmvg		+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			this._tallySwedish2.ctot		+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
		} else if (mode.compareTo("Auth") == 0) {
			this._tallyAuth.eg.number 	 = sumInt(this._tallyAuth.eg.number, values.eg.number);
			this._tallyAuth.g.number 	 = sumInt(this._tallyAuth.g.number, values.g.number);
			this._tallyAuth.vg.number 	 = sumInt(this._tallyAuth.vg.number, values.vg.number);
			this._tallyAuth.mvg.number 	 = sumInt(this._tallyAuth.mvg.number, values.mvg.number);
			this._tallyAuth.tot.number 	 = sumInt(this._tallyAuth.tot.number, values.tot.number);
			this._tallyAuth.eg.percent 	 = sumDec(this._tallyAuth.eg.percent, values.eg.percent);
			this._tallyAuth.g.percent 	 = sumDec(this._tallyAuth.g.percent, values.g.percent);
			this._tallyAuth.vg.percent 	 = sumDec(this._tallyAuth.vg.percent, values.vg.percent);
			this._tallyAuth.mvg.percent 	 = sumDec(this._tallyAuth.mvg.percent, values.mvg.percent);
			this._tallyAuth.tot.percent	 = sumDec(this._tallyAuth.tot.percent, values.tot.percent);
			this._tallyAuth.ceg			+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			this._tallyAuth.cg			+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			this._tallyAuth.cvg			+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			this._tallyAuth.cmvg			+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			this._tallyAuth.ctot			+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
		} else if (mode.compareTo("Total") == 0) {
			this._tallyTotal.eg.number 	 = sumInt(this._tallyTotal.eg.number, values.eg.number);
			this._tallyTotal.g.number 	 = sumInt(this._tallyTotal.g.number, values.g.number);
			this._tallyTotal.vg.number 	 = sumInt(this._tallyTotal.vg.number, values.vg.number);
			this._tallyTotal.mvg.number 	 = sumInt(this._tallyTotal.mvg.number, values.mvg.number);
			this._tallyTotal.tot.number 	 = sumInt(this._tallyTotal.tot.number, values.tot.number);
			this._tallyTotal.eg.percent 	 = sumDec(this._tallyTotal.eg.percent, values.eg.percent);
			this._tallyTotal.g.percent 	 = sumDec(this._tallyTotal.g.percent, values.g.percent);
			this._tallyTotal.vg.percent   = sumDec(this._tallyTotal.vg.percent, values.vg.percent);
			this._tallyTotal.mvg.percent  = sumDec(this._tallyTotal.mvg.percent, values.mvg.percent);
			this._tallyTotal.tot.percent  = sumDec(this._tallyTotal.tot.percent, values.tot.percent);
			this._tallyTotal.ceg			+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			this._tallyTotal.cg			+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			this._tallyTotal.cvg			+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			this._tallyTotal.cmvg		+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			this._tallyTotal.ctot		+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
		}
	}

	private String sumInt(String sum, String add) {
			return "" + (Integer.parseInt(sum) + Integer.parseInt(add));
	}

	private String sumDec(String sum, String add) {
			return "" + (Float.parseFloat(sum) + Float.parseFloat(add));
	}

	private String tallyMean(String percent, int totalNumber) {
		if (totalNumber != 0) {
			return "" + (((Float.parseFloat(percent) / Float.parseFloat(""+totalNumber))));
		} else {
			return "0";
		}
	}


	private void resetTally() {
		this._tallyEnglish = new SchoolMarkValues();
		this._tallyMaths = new SchoolMarkValues();
		this._tallySwedish = new SchoolMarkValues();
		this._tallySwedish2 = new SchoolMarkValues();
		this._tallyAuth = new SchoolMarkValues();
		this._tallyTotal = new SchoolMarkValues();
		 
		this._tallyEnglish.eg.number = "0";
		this._tallyEnglish.g.number = "0";
		this._tallyEnglish.vg.number = "0";
		this._tallyEnglish.mvg.number = "0";
		this._tallyEnglish.tot.number = "0";
		this._tallyEnglish.eg.percent = "0";
		this._tallyEnglish.g.percent = "0";
		this._tallyEnglish.vg.percent = "0";
		this._tallyEnglish.mvg.percent = "0";
		this._tallyEnglish.tot.percent = "0";

		this._tallyMaths.eg.number = "0";
		this._tallyMaths.g.number = "0";
		this._tallyMaths.vg.number = "0";
		this._tallyMaths.mvg.number = "0";
		this._tallyMaths.tot.number = "0";
		this._tallyMaths.eg.percent = "0";
		this._tallyMaths.g.percent = "0";
		this._tallyMaths.vg.percent = "0";
		this._tallyMaths.mvg.percent = "0";
		this._tallyMaths.tot.percent = "0";

		this._tallySwedish.eg.number = "0";
		this._tallySwedish.g.number = "0";
		this._tallySwedish.vg.number = "0";
		this._tallySwedish.mvg.number = "0";
		this._tallySwedish.tot.number = "0";
		this._tallySwedish.eg.percent = "0";
		this._tallySwedish.g.percent = "0";
		this._tallySwedish.vg.percent = "0";
		this._tallySwedish.mvg.percent = "0";
		this._tallySwedish.tot.percent = "0";

		this._tallySwedish2.eg.number = "0";
		this._tallySwedish2.g.number = "0";
		this._tallySwedish2.vg.number = "0";
		this._tallySwedish2.mvg.number = "0";
		this._tallySwedish2.tot.number = "0";
		this._tallySwedish2.eg.percent = "0";
		this._tallySwedish2.g.percent = "0";
		this._tallySwedish2.vg.percent = "0";
		this._tallySwedish2.mvg.percent = "0";
		this._tallySwedish2.tot.percent= "0";

		this._tallyAuth.eg.number = "0";
		this._tallyAuth.g.number = "0";
		this._tallyAuth.vg.number = "0";
		this._tallyAuth.mvg.number = "0";
		this._tallyAuth.tot.number = "0";
		this._tallyAuth.eg.percent = "0";
		this._tallyAuth.g.percent = "0";
		this._tallyAuth.vg.percent = "0";
		this._tallyAuth.mvg.percent = "0";
		this._tallyAuth.tot.percent = "0";

		this._tallyTotal.eg.number = "0";
		this._tallyTotal.g.number = "0";
		this._tallyTotal.vg.number = "0";
		this._tallyTotal.mvg.number = "0";
		this._tallyTotal.tot.number = "0";
		this._tallyTotal.eg.percent = "0";
		this._tallyTotal.g.percent = "0";
		this._tallyTotal.vg.percent = "0";
		this._tallyTotal.mvg.percent = "0";
		this._tallyTotal.tot.percent = "0";
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


	private Table getMarksTable() {
		Table ret = new Table();
		ret.setColumns(11);
		ret.setRows(6);
		ret.setBorderColor("#cccccc");
		ret.setBorder(1);
		ret.setCellspacing("0");
		ret.setCellpadding("3");
		ret.setWidth(this.blockWidth);
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

	private String roundAbout(int t, int n) { 
		float x = (float) (Math.rint(((t/n) * 100)) / 100);
		int y = (int) x;
		return ""+y;
	}


	private SchoolMarksBusiness getSchoolMarksBusiness(IWContext iwc) throws Exception {
		return (SchoolMarksBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, SchoolMarksBusiness.class);
	}

}

