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
 * $Id: SchoolMarksPresenter2.java,v 1.4 2003/10/06 13:30:34 laddi Exp $
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
			resetTally(); 
			String smtOld = "";
			String smtNew = "";
			int count = 0;
			while (iter.hasNext()) {
				SchoolStatistics stat = (SchoolStatistics) iter.next();
				smtNew = stat.getSchoolManagementType();
				if(smtOld.compareTo(smtNew) != 0) {
					smtOld = stat.getSchoolManagementType();
					if(count++  > 0) {
						table = insertTotals(iwrb.getLocalizedString("school_marks_stats.header1", "Totalt kommunala skolor"), table, row);
						row +=9;
					}
				}
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
				
				table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setColumnAlignment(7, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setColumnAlignment(8, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setColumnAlignment(9, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setColumnAlignment(10, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setColumnAlignment(11, Table.HORIZONTAL_ALIGN_RIGHT);

				table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_CENTER);
				table.setAlignment(4, row, Table.HORIZONTAL_ALIGN_CENTER);
				table.setAlignment(6, row, Table.HORIZONTAL_ALIGN_CENTER);
				table.setAlignment(8, row, Table.HORIZONTAL_ALIGN_CENTER);
				table.setAlignment(10, row, Table.HORIZONTAL_ALIGN_CENTER);

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
				sumSchoolTally(stat.getEnglishMarks(), "English");
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
				sumSchoolTally(stat.getMathsMarks(), "Maths");
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
				sumSchoolTally(stat.getSwedishMarks(), "Swedish");
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
				if (containsValues(stat.getSwedish2Marks())) {
					sumSchoolTally(stat.getSwedish2Marks(), "Swedish2");
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
				}

				sumSchoolTally(stat.getSumAuthMarks(), "Auth");
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
				sumSchoolTally(stat.getTotalMarks(), "Total");
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
			table = insertTotals(iwrb.getLocalizedString("school_marks_stats.header2", "Totalt Nacka skolor"), table, row);
		} catch (RemoteException e) {
			e.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();		
		}
		mainForm.add(table);

	}

	private Table insertTotals(String header, Table table, int row) {	
		table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.eg", "Ej godkänt")), 2, row);			
		table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.g", "Godkänt")), 4, row);			
		table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.vg", "Väl godkänt")), 6, row);
		table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.mvg", "Mycket väl godkänt")), 8, row);
		table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.tot", "Totalt")), 10, row);
		table.setColor(2, row, "#aaaaaa");			
		table.setColor(4, row, "#aaaaaa");			
		table.setColor(6, row, "#aaaaaa");			
		table.setColor(8, row, "#aaaaaa");			
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.setAlignment(4, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.setAlignment(6, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.setAlignment(8, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.setAlignment(10, row, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColor(10, row, "#aaaaaa");			
		table.mergeCells(2, row, 3, row);
		table.mergeCells(4, row, 5, row);
		table.mergeCells(6, row, 7, row);
		table.mergeCells(8, row, 9, row);
		table.mergeCells(10, row, 11, row);

		row++;
		table.setRowColor(row, "#e0e0e0");
		
		table.add(getHeader(header), 1, row);			
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
		table.setRowColor(row, "#e0e0e0");

		table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.english", "Engelska")), 1, row);
		table.add(getSmallText(_tallyEnglish.eg.number), 2, row);
		table.add(getSmallText(formatPct(tallyMean(_tallyEnglish.eg.percent, _tallyEnglish.ceg))), 3, row);
		table.add(getSmallText(_tallyEnglish.g.number), 4, row);
		table.add(getSmallText(formatPct(tallyMean(_tallyEnglish.g.percent, _tallyEnglish.cg))), 5, row);
		table.add(getSmallText(_tallyEnglish.vg.number), 6, row);
		table.add(getSmallText(formatPct(tallyMean(_tallyEnglish.vg.percent, _tallyEnglish.cvg))), 7, row);
		table.add(getSmallText(_tallyEnglish.mvg.number), 8, row);
		table.add(getSmallText(formatPct(tallyMean(_tallyEnglish.mvg.percent, _tallyEnglish.cmvg))), 9, row);
		table.add(getSmallText(_tallyEnglish.tot.number), 10, row);
		table.add(getSmallText(formatPct(tallyMean(_tallyEnglish.tot.percent, _tallyEnglish.ctot))), 11, row);

		row++;
		table.setRowColor(row, "#e0e0e0");

		table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.maths", "Matematik")), 1, row);
		table.add(getSmallText(_tallyMaths.eg.number), 2, row);
		table.add(getSmallText(formatPct(tallyMean(_tallyMaths.eg.percent, _tallyMaths.ceg))), 3, row);
		table.add(getSmallText(_tallyMaths.g.number), 4, row);
		table.add(getSmallText(formatPct(tallyMean(_tallyMaths.g.percent, _tallyMaths.cg))), 5, row);
		table.add(getSmallText(_tallyMaths.vg.number), 6, row);
		table.add(getSmallText(formatPct(tallyMean(_tallyMaths.vg.percent, _tallyMaths.cvg))), 7, row);
		table.add(getSmallText(_tallyMaths.mvg.number), 8, row);
		table.add(getSmallText(formatPct(tallyMean(_tallyMaths.mvg.percent, _tallyMaths.cmvg))), 9, row);
		table.add(getSmallText(_tallyMaths.tot.number), 10, row);
		table.add(getSmallText(formatPct(tallyMean(_tallyMaths.tot.percent, _tallyMaths.ctot))), 11, row);

		row++;
		table.setRowColor(row, "#e0e0e0");

		table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.swedish", "Svenska")), 1, row);
		table.add(getSmallText(_tallySwedish.eg.number), 2, row);
		table.add(getSmallText(formatPct(tallyMean(_tallySwedish.eg.percent, _tallySwedish.ceg))), 3, row);
		table.add(getSmallText(_tallySwedish.g.number), 4, row);
		table.add(getSmallText(formatPct(tallyMean(_tallySwedish.g.percent, _tallySwedish.cg))), 5, row);
		table.add(getSmallText(_tallySwedish.vg.number), 6, row);
		table.add(getSmallText(formatPct(tallyMean(_tallySwedish.vg.percent, _tallySwedish.cvg))), 7, row);
		table.add(getSmallText(_tallySwedish.mvg.number), 8, row);
		table.add(getSmallText(formatPct(tallyMean(_tallySwedish.mvg.percent, _tallySwedish.cmvg))), 9, row);
		table.add(getSmallText(_tallySwedish.tot.number), 10, row);
		table.add(getSmallText(formatPct(tallyMean(_tallySwedish.tot.percent, _tallySwedish.ctot))), 11, row);

		row++;
		table.setRowColor(row, "#e0e0e0");

		if (containsValues(_tallySwedish2)) {
			table.add(getSmallText(iwrb.getLocalizedString("school_marks_stats.swedish2", "Svenska som andraspråk")), 1, row);
			table.add(getSmallText(_tallySwedish2.eg.number), 2, row);
			table.add(getSmallText(formatPct(tallyMean(_tallySwedish2.eg.percent, _tallySwedish2.ceg))), 3, row);
			table.add(getSmallText(_tallySwedish2.g.number), 4, row);
			table.add(getSmallText(formatPct(tallyMean(_tallySwedish2.g.percent, _tallySwedish2.cg))), 5, row);
			table.add(getSmallText(_tallySwedish2.vg.number), 6, row);
			table.add(getSmallText(formatPct(tallyMean(_tallySwedish2.vg.percent, _tallySwedish2.cvg))), 7, row);
			table.add(getSmallText(_tallySwedish2.mvg.number), 8, row);
			table.add(getSmallText(formatPct(tallyMean(_tallySwedish2.mvg.percent, _tallySwedish2.cmvg))), 9, row);
			table.add(getSmallText(_tallySwedish2.tot.number), 10, row);
			table.add(getSmallText(formatPct(tallyMean(_tallySwedish2.tot.percent, _tallySwedish2.ctot))), 11, row);
			row++;
			table.setRowColor(row, "#e0e0e0");
		}

		table.add(getHeader(iwrb.getLocalizedString("school_marks_stats.sumauthmarks", "Summa behörighetsgivande poäng")), 1, row);
		table.add(getHeader(_tallyAuth.eg.number), 2, row);
		table.add(getHeader(formatPct(tallyMean(_tallyAuth.eg.percent, _tallyAuth.ceg))), 3, row);
		table.add(getHeader(_tallyAuth.g.number), 4, row);
		table.add(getHeader(formatPct(tallyMean(_tallyAuth.g.percent, _tallyAuth.cg))), 5, row);
		table.add(getHeader(_tallyAuth.vg.number), 6, row);
		table.add(getHeader(formatPct(tallyMean(_tallyAuth.vg.percent, _tallyAuth.cvg))), 7, row);
		table.add(getHeader(_tallyAuth.mvg.number), 8, row);
		table.add(getHeader(formatPct(tallyMean(_tallyAuth.mvg.percent, _tallyAuth.cmvg))), 9, row);
		table.add(getHeader(_tallyAuth.tot.number), 10, row);
		table.add(getHeader(formatPct(tallyMean(_tallyAuth.tot.percent, _tallyAuth.ctot))), 11, row);

		row++;
		table.setRowColor(row, "#e0e0e0");

		table.add(getHeader(header), 1, row);
		table.add(getHeader(_tallyTotal.eg.number), 2, row);
		table.add(getHeader(formatPct(tallyMean(_tallyTotal.eg.percent, _tallyTotal.ceg))), 3, row);
		table.add(getHeader(_tallyTotal.g.number), 4, row);
		table.add(getHeader(formatPct(tallyMean(_tallyTotal.g.percent, _tallyTotal.cg))), 5, row);
		table.add(getHeader(_tallyTotal.vg.number), 6, row);
		table.add(getHeader(formatPct(tallyMean(_tallyTotal.vg.percent, _tallyTotal.cvg))), 7, row);
		table.add(getHeader(_tallyTotal.mvg.number), 8, row);
		table.add(getHeader(formatPct(tallyMean(_tallyTotal.mvg.percent, _tallyTotal.cmvg))), 9, row);
		table.add(getHeader(_tallyTotal.tot.number), 10, row);
		table.add(getHeader(formatPct(tallyMean(_tallyTotal.tot.percent, _tallyTotal.ctot))), 11, row);
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
			_tallyEnglish.eg.number	 = sumInt(_tallyEnglish.eg.number, values.eg.number);
			_tallyEnglish.g.number	 = sumInt(_tallyEnglish.g.number, values.g.number);
			_tallyEnglish.vg.number	 = sumInt(_tallyEnglish.vg.number, values.vg.number);
			_tallyEnglish.mvg.number = sumInt(_tallyEnglish.mvg.number, values.mvg.number);
			_tallyEnglish.tot.number = sumInt(_tallyEnglish.tot.number, values.tot.number);
			_tallyEnglish.eg.percent  = sumDec(_tallyEnglish.eg.percent, values.eg.percent);
			_tallyEnglish.g.percent	  = sumDec(_tallyEnglish.g.percent, values.g.percent);
			_tallyEnglish.vg.percent  = sumDec(_tallyEnglish.vg.percent, values.vg.percent);
			_tallyEnglish.mvg.percent = sumDec(_tallyEnglish.mvg.percent, values.mvg.percent);
			_tallyEnglish.tot.percent = sumDec(_tallyEnglish.tot.percent, values.tot.percent);
			_tallyEnglish.ceg		+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			_tallyEnglish.cg		+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			_tallyEnglish.cvg		+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			_tallyEnglish.cmvg		+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			_tallyEnglish.ctot		+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
		} else if (mode.compareTo("Maths") == 0) {
			_tallyMaths.eg.number	 = sumInt(_tallyMaths.eg.number, values.eg.number);
			_tallyMaths.g.number	 = sumInt(_tallyMaths.g.number, values.g.number);
			_tallyMaths.vg.number	 = sumInt(_tallyMaths.vg.number, values.vg.number);
			_tallyMaths.mvg.number	 = sumInt(_tallyMaths.mvg.number, values.mvg.number);
			_tallyMaths.tot.number	 = sumInt(_tallyMaths.tot.number, values.tot.number);
			_tallyMaths.eg.percent	 = sumDec(_tallyMaths.eg.percent, values.eg.percent);
			_tallyMaths.g.percent	 = sumDec(_tallyMaths.g.percent, values.g.percent);
			_tallyMaths.vg.percent	 = sumDec(_tallyMaths.vg.percent, values.vg.percent);
			_tallyMaths.mvg.percent	 = sumDec(_tallyMaths.mvg.percent, values.mvg.percent);
			_tallyMaths.tot.percent	 = sumDec(_tallyMaths.tot.percent, values.tot.percent);
			_tallyMaths.ceg			+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			_tallyMaths.cg			+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			_tallyMaths.cvg			+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			_tallyMaths.cmvg		+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			_tallyMaths.ctot		+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
		} else if (mode.compareTo("Swedish") == 0) {
			_tallySwedish.eg.number  = sumInt(_tallySwedish.eg.number, values.eg.number);
			_tallySwedish.g.number   = sumInt(_tallySwedish.g.number, values.g.number);
			_tallySwedish.vg.number  = sumInt(_tallySwedish.vg.number, values.vg.number);
			_tallySwedish.mvg.number = sumInt(_tallySwedish.mvg.number, values.mvg.number);
			_tallySwedish.tot.number = sumInt(_tallySwedish.tot.number, values.tot.number);
			_tallySwedish.eg.percent  = sumDec(_tallySwedish.eg.percent, values.eg.percent);
			_tallySwedish.g.percent   = sumDec(_tallySwedish.g.percent, values.g.percent);
			_tallySwedish.vg.percent  = sumDec(_tallySwedish.vg.percent, values.vg.percent);
			_tallySwedish.mvg.percent = sumDec(_tallySwedish.mvg.percent, values.mvg.percent);
			_tallySwedish.tot.percent = sumDec(_tallySwedish.tot.percent, values.tot.percent);
			_tallySwedish.ceg		+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			_tallySwedish.cg		+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			_tallySwedish.cvg		+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			_tallySwedish.cmvg		+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			_tallySwedish.ctot		+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
		} else if (mode.compareTo("Swedish2") == 0) {
			_tallySwedish2.eg.number = sumInt(_tallySwedish2.eg.number, values.eg.number);
			_tallySwedish2.g.number  = sumInt(_tallySwedish2.g.number, values.g.number);
			_tallySwedish2.vg.number = sumInt(_tallySwedish2.vg.number, values.vg.number);
			_tallySwedish2.mvg.number= sumInt(_tallySwedish2.mvg.number, values.mvg.number);
			_tallySwedish2.tot.number= sumInt(_tallySwedish2.tot.number, values.tot.number);
			_tallySwedish2.eg.percent = sumDec(_tallySwedish2.eg.percent, values.eg.percent);
			_tallySwedish2.g.percent  = sumDec(_tallySwedish2.g.percent, values.g.percent);
			_tallySwedish2.vg.percent = sumDec(_tallySwedish2.vg.percent, values.vg.percent);
			_tallySwedish2.mvg.percent= sumDec(_tallySwedish2.mvg.percent, values.mvg.percent);
			_tallySwedish2.tot.percent= sumDec(_tallySwedish2.tot.percent, values.tot.percent);
			_tallySwedish2.ceg		+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			_tallySwedish2.cg		+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			_tallySwedish2.cvg		+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			_tallySwedish2.cmvg		+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			_tallySwedish2.ctot		+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
		} else if (mode.compareTo("Auth") == 0) {
			_tallyAuth.eg.number 	 = sumInt(_tallyAuth.eg.number, values.eg.number);
			_tallyAuth.g.number 	 = sumInt(_tallyAuth.g.number, values.g.number);
			_tallyAuth.vg.number 	 = sumInt(_tallyAuth.vg.number, values.vg.number);
			_tallyAuth.mvg.number 	 = sumInt(_tallyAuth.mvg.number, values.mvg.number);
			_tallyAuth.tot.number 	 = sumInt(_tallyAuth.tot.number, values.tot.number);
			_tallyAuth.eg.percent 	 = sumDec(_tallyAuth.eg.percent, values.eg.percent);
			_tallyAuth.g.percent 	 = sumDec(_tallyAuth.g.percent, values.g.percent);
			_tallyAuth.vg.percent 	 = sumDec(_tallyAuth.vg.percent, values.vg.percent);
			_tallyAuth.mvg.percent 	 = sumDec(_tallyAuth.mvg.percent, values.mvg.percent);
			_tallyAuth.tot.percent	 = sumDec(_tallyAuth.tot.percent, values.tot.percent);
			_tallyAuth.ceg			+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			_tallyAuth.cg			+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			_tallyAuth.cvg			+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			_tallyAuth.cmvg			+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			_tallyAuth.ctot			+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
		} else if (mode.compareTo("Total") == 0) {
			_tallyTotal.eg.number 	 = sumInt(_tallyTotal.eg.number, values.eg.number);
			_tallyTotal.g.number 	 = sumInt(_tallyTotal.g.number, values.g.number);
			_tallyTotal.vg.number 	 = sumInt(_tallyTotal.vg.number, values.vg.number);
			_tallyTotal.mvg.number 	 = sumInt(_tallyTotal.mvg.number, values.mvg.number);
			_tallyTotal.tot.number 	 = sumInt(_tallyTotal.tot.number, values.tot.number);
			_tallyTotal.eg.percent 	 = sumDec(_tallyTotal.eg.percent, values.eg.percent);
			_tallyTotal.g.percent 	 = sumDec(_tallyTotal.g.percent, values.g.percent);
			_tallyTotal.vg.percent   = sumDec(_tallyTotal.vg.percent, values.vg.percent);
			_tallyTotal.mvg.percent  = sumDec(_tallyTotal.mvg.percent, values.mvg.percent);
			_tallyTotal.tot.percent  = sumDec(_tallyTotal.tot.percent, values.tot.percent);
			_tallyTotal.ceg			+= Integer.parseInt(values.eg.number) != 0 ? 1 : 0;
			_tallyTotal.cg			+= Integer.parseInt(values.g.number) != 0 ? 1 : 0;
			_tallyTotal.cvg			+= Integer.parseInt(values.vg.number) != 0 ? 1 : 0;
			_tallyTotal.cmvg		+= Integer.parseInt(values.mvg.number) != 0 ? 1 : 0;
			_tallyTotal.ctot		+= Integer.parseInt(values.tot.number) != 0 ? 1 : 0;
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
		_tallyEnglish = new SchoolMarkValues();
		_tallyMaths = new SchoolMarkValues();
		_tallySwedish = new SchoolMarkValues();
		_tallySwedish2 = new SchoolMarkValues();
		_tallyAuth = new SchoolMarkValues();
		_tallyTotal = new SchoolMarkValues();
		 
		_tallyEnglish.eg.number = "0";
		_tallyEnglish.g.number = "0";
		_tallyEnglish.vg.number = "0";
		_tallyEnglish.mvg.number = "0";
		_tallyEnglish.tot.number = "0";
		_tallyEnglish.eg.percent = "0";
		_tallyEnglish.g.percent = "0";
		_tallyEnglish.vg.percent = "0";
		_tallyEnglish.mvg.percent = "0";
		_tallyEnglish.tot.percent = "0";

		_tallyMaths.eg.number = "0";
		_tallyMaths.g.number = "0";
		_tallyMaths.vg.number = "0";
		_tallyMaths.mvg.number = "0";
		_tallyMaths.tot.number = "0";
		_tallyMaths.eg.percent = "0";
		_tallyMaths.g.percent = "0";
		_tallyMaths.vg.percent = "0";
		_tallyMaths.mvg.percent = "0";
		_tallyMaths.tot.percent = "0";

		_tallySwedish.eg.number = "0";
		_tallySwedish.g.number = "0";
		_tallySwedish.vg.number = "0";
		_tallySwedish.mvg.number = "0";
		_tallySwedish.tot.number = "0";
		_tallySwedish.eg.percent = "0";
		_tallySwedish.g.percent = "0";
		_tallySwedish.vg.percent = "0";
		_tallySwedish.mvg.percent = "0";
		_tallySwedish.tot.percent = "0";

		_tallySwedish2.eg.number = "0";
		_tallySwedish2.g.number = "0";
		_tallySwedish2.vg.number = "0";
		_tallySwedish2.mvg.number = "0";
		_tallySwedish2.tot.number = "0";
		_tallySwedish2.eg.percent = "0";
		_tallySwedish2.g.percent = "0";
		_tallySwedish2.vg.percent = "0";
		_tallySwedish2.mvg.percent = "0";
		_tallySwedish2.tot.percent= "0";

		_tallyAuth.eg.number = "0";
		_tallyAuth.g.number = "0";
		_tallyAuth.vg.number = "0";
		_tallyAuth.mvg.number = "0";
		_tallyAuth.tot.number = "0";
		_tallyAuth.eg.percent = "0";
		_tallyAuth.g.percent = "0";
		_tallyAuth.vg.percent = "0";
		_tallyAuth.mvg.percent = "0";
		_tallyAuth.tot.percent = "0";

		_tallyTotal.eg.number = "0";
		_tallyTotal.g.number = "0";
		_tallyTotal.vg.number = "0";
		_tallyTotal.mvg.number = "0";
		_tallyTotal.tot.number = "0";
		_tallyTotal.eg.percent = "0";
		_tallyTotal.g.percent = "0";
		_tallyTotal.vg.percent = "0";
		_tallyTotal.mvg.percent = "0";
		_tallyTotal.tot.percent = "0";
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

