package se.idega.idegaweb.commune.school.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class SchoolChoiceWriter implements MediaWritable {

	private MemoryFileBuffer buffer = null;
	private SchoolCommuneBusiness business;
	private CommuneUserBusiness userBusiness;
	private Locale locale;
	private IWResourceBundle iwrb;
	
	public final static String prmSeasonId = "season_id";
	public final static String prmSchoolId = "school_id";
	public final static String prmGrade = "grade";
	public final static String PARAMETER_SHOW_PRIORITY_COLUMN = "show_priority_column";
	public final static String PARAMETER_SHOW_HANDICRAFT_COLUMN = "show_handicraft_column";
	
	private int season;
	private int school;
	private int grade;
	private boolean showPriorityColumn = false;
	private boolean showHandicraftColumn = false;
	
	public SchoolChoiceWriter() {
	}
	
	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			this.locale = iwc.getApplicationSettings().getApplicationLocale();
			this.business = getSchoolCommuneBusiness(iwc);
			this.userBusiness = getCommuneUserBusiness(iwc);
			this.iwrb = iwc.getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(this.locale);
			
			if (req.getParameter(prmSeasonId) != null && req.getParameter(prmSchoolId) != null) {
				this.season = Integer.parseInt(req.getParameter(prmSeasonId));
				this.school = Integer.parseInt(req.getParameter(prmSchoolId));
				this.grade = Integer.parseInt(req.getParameter(prmGrade));				
				this.setShowPriorityColumn(Boolean.valueOf(req.getParameter(PARAMETER_SHOW_PRIORITY_COLUMN)).booleanValue());
				this.setShowHandicraftColumn(Boolean.valueOf(req.getParameter(PARAMETER_SHOW_HANDICRAFT_COLUMN)).booleanValue());
				this.buffer = writeXLS(this.school, this.season, this.grade);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMimeType() {
		if (this.buffer != null) {
			return this.buffer.getMimeType();
		}
		return "application/x-msexcel";
	}
	
	public void writeTo(OutputStream out) throws IOException {
		if (this.buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(this.buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else {
			System.err.println("buffer is null");
		}
	}
	
	public MemoryFileBuffer writeXLS(int schoolID, int seasonID, int grade) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);
		String[] validStatuses = new String[] { SchoolChoiceBMPBean.CASE_STATUS_PRELIMINARY, SchoolChoiceBMPBean.CASE_STATUS_MOVED };
		Collection students = this.business.getSchoolChoiceBusiness().getApplicantsForSchool(schoolID, seasonID, grade, validStatuses, null, SchoolChoiceComparator.NAME_SORT, -1, -1);
		boolean showLanguage = false;
		if (grade >= 12) {
			showLanguage = true;
		}
		
		if (!students.isEmpty()) {
		    HSSFWorkbook wb = new HSSFWorkbook();
		    HSSFSheet sheet = wb.createSheet(this.iwrb.getLocalizedString("school.school_choices","School choices"));
		    sheet.setColumnWidth((short)0, (short) (30 * 256));
		    sheet.setColumnWidth((short)1, (short) (14 * 256));
		    sheet.setColumnWidth((short)2, (short) (30 * 256));
		    sheet.setColumnWidth((short)3, (short) (14 * 256));
			sheet.setColumnWidth((short)4, (short) (30 * 256));
			sheet.setColumnWidth((short)5, (short) (14 * 256));
			sheet.setColumnWidth((short)6, (short) (14 * 256));
			HSSFFont font = wb.createFont();
		    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		    font.setFontHeightInPoints((short)12);
		    HSSFCellStyle style = wb.createCellStyle();
		    style.setFont(font);
	
			int cellRow = 0;
			int cellColumn = 0;
			HSSFRow row = sheet.createRow(cellRow++);
			HSSFCell cell = row.createCell((short)cellColumn++);
		    cell.setCellValue(this.iwrb.getLocalizedString("school.name","Name"));
		    cell.setCellStyle(style);
		    cell = row.createCell((short)cellColumn++);
		    cell.setCellValue(this.iwrb.getLocalizedString("school.personal_id","Personal ID"));
		    cell.setCellStyle(style);
		    cell = row.createCell((short)cellColumn++);
		    cell.setCellValue(this.iwrb.getLocalizedString("school.address","Address"));
		    cell.setCellStyle(style);
			cell = row.createCell((short)cellColumn++);
			cell.setCellValue(this.iwrb.getLocalizedString("school.gender","Gender"));
			cell.setCellStyle(style);
		    cell = row.createCell((short)cellColumn++);
		    cell.setCellValue(this.iwrb.getLocalizedString("school.from_school","From School"));
		    cell.setCellStyle(style);
		    if (showLanguage) {
			    cell = row.createCell((short)cellColumn++);
			    cell.setCellValue(this.iwrb.getLocalizedString("school.language","Language"));
			    cell.setCellStyle(style);
			}
		    cell = row.createCell((short)cellColumn++);
		    cell.setCellValue(this.iwrb.getLocalizedString("school.created","Created"));
		    cell.setCellStyle(style);
		    
		    if(this.getShowPriorityColumn()) {
			    cell = row.createCell((short)cellColumn++);			    
			    cell.setCellValue(this.iwrb.getLocalizedString("school_choice.priority", "Priority"));
			    cell.setCellStyle(style);	    
		    }
		    
		    if(this.isShowHandicraftColumn()) {
			    cell = row.createCell((short)cellColumn++);			    
			    cell.setCellValue(this.iwrb.getLocalizedString("school.handicraft", "Handicraft"));
			    cell.setCellStyle(style);	    
		    }		    
	    
		    SchoolChoice choice;
		    School school;
		    User applicant;
		    Address address;
		    IWTimestamp created;
		    
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				row = sheet.createRow(cellRow++);
				cellColumn = 0;
				choice = (SchoolChoice) iter.next();
				created = new IWTimestamp(choice.getCreated());
				applicant = choice.getChild();
				school = this.business.getSchoolBusiness().getSchool(new Integer(choice.getCurrentSchoolId()));
				address = this.userBusiness.getUsersMainAddress(applicant);
				
				Name name = new Name(applicant.getFirstName(), applicant.getMiddleName(), applicant.getLastName());
				row.createCell((short)cellColumn++).setCellValue(name.getName(this.locale, true));
			    row.createCell((short)cellColumn++).setCellValue(PersonalIDFormatter.format(applicant.getPersonalID(), this.locale));
			    if (address != null) {
						row.createCell((short)cellColumn).setCellValue(address.getStreetAddress());
					}
			    cellColumn++;
			    
			    if (applicant.getGender().isFemaleGender()) {
						row.createCell((short)cellColumn++).setCellValue(this.iwrb.getLocalizedString("school.girl", "Girl"));
					}
					else {
						row.createCell((short)cellColumn++).setCellValue(this.iwrb.getLocalizedString("school.boy", "Boy"));
					}
	
			    if (school != null) {
			    	String schoolName = school.getName();
			    	if (choice.getStatus().equalsIgnoreCase(SchoolChoiceBMPBean.CASE_STATUS_MOVED)) {
							schoolName += " (" + this.iwrb.getLocalizedString("school.moved", "Moved") + ")";
						}
			    	row.createCell((short)cellColumn).setCellValue(schoolName);
			    }
			    cellColumn++;
			    
			    if (showLanguage) {
			    	if (choice.getLanguageChoice() != null) {
							row.createCell((short)cellColumn).setCellValue(this.iwrb.getLocalizedString(choice.getLanguageChoice(),""));
						}
			    	cellColumn++;
			    }
			    
			    row.createCell((short)cellColumn++).setCellValue(created.getLocaleDate(this.locale, IWTimestamp.SHORT));
			    
			    if (this.getShowPriorityColumn()) {
			    	String priority = choice.getPriority() ? this.iwrb.getLocalizedString("school_choice.yes", "Yes") : this.iwrb.getLocalizedString("school_choice.no", "No");
			    	row.createCell((short)cellColumn++).setCellValue(priority);
			    }
			    
			    if (this.isShowHandicraftColumn()) {
					SchoolStudyPath handicraft = choice.getHandicraft();
					if (handicraft != null) {
						row.createCell((short) cellColumn++).setCellValue(
								this.iwrb.getLocalizedString(handicraft.getLocalizedKey(), handicraft.getLocalizedKey()));
					}
				}
			    
			}
			wb.write(mos);
		}
		buffer.setMimeType("application/x-msexcel");
		return buffer;
	}
	
	protected SchoolCommuneBusiness getSchoolCommuneBusiness(IWApplicationContext iwc) throws RemoteException {
		return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);	
	}

	protected CommuneUserBusiness getCommuneUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);	
	}

	public boolean getShowPriorityColumn() {
		return this.showPriorityColumn;
	}

	public void setShowPriorityColumn(boolean showPriorityColumn) {
		this.showPriorityColumn = showPriorityColumn;
	}
	
	public boolean isShowHandicraftColumn() {
		return this.showHandicraftColumn;
	}
	
	public void setShowHandicraftColumn(boolean showHandicraftColumn) {
		this.showHandicraftColumn = showHandicraftColumn;
	}	
}