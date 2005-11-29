package se.idega.idegaweb.commune.school.presentation;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoCustodianFound;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.data.SchoolChoiceHome;
import se.idega.util.PIDChecker;
import com.idega.block.process.data.CaseLog;
import com.idega.block.process.data.CaseLogHome;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

public class RejectedStudentsListWriter extends DownloadWriter implements MediaWritable {

	public static final String PARAMETER_SCHOOL_ID = "school_id";
	public static final String PARAMETER_SCHOOL_SEASON_ID = "school_season_id";

	private MemoryFileBuffer buffer = null;
	
	private Locale locale;
	private IWResourceBundle iwrb;
	
	int currentStartEntry = 0;
	int ENTRIES_PER_PAGE = 100000;	
	
    int schoolID = 0;
	int seasonID = 0;	
	
	
	public void init(HttpServletRequest req, IWContext iwc) {
		
		locale = iwc.getApplicationSettings().getApplicationLocale();
		iwrb = iwc.getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
		
        schoolID = Integer.parseInt(req.getParameter(PARAMETER_SCHOOL_ID));
		seasonID = Integer.parseInt(req.getParameter(PARAMETER_SCHOOL_SEASON_ID));
		
		try {
			this.buffer = writeXls(iwc);
			setAsDownload(iwc, "rejected_students_list.xls", this.buffer.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MemoryFileBuffer writeXls(IWContext iwc) throws IOException {		
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);
        
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Rejected students list");
        
        int rowNum = 0;
        
        HSSFRow row = sheet.createRow((short) rowNum++);
        fillHeaderRow(wb, sheet, row);
        
        //retrieve data
        Collection schoolChoices = getSchoolChoices(iwc);
        
        //fill data into rows
        if (! schoolChoices.isEmpty()) {
    		User applicant;
    		School school;
    		Address address;
    		Phone phone;
    		SchoolChoice choice;
    		String name = null;
    		
        	for (Iterator iter = schoolChoices.iterator(); iter.hasNext(); ) {
        		choice = (SchoolChoice) iter.next();
    			row = sheet.createRow((short) rowNum++);
    			try {
    				applicant = getUserBusiness(iwc).getUser(choice.getChildId());
    				school = getSchoolCommuneBusiness(iwc).getSchoolBusiness().getSchool(new Integer(choice.getCurrentSchoolId()));
    				address = getUserBusiness(iwc).getUsersMainAddress(applicant);    				
  				
    				name = getSchoolCommuneBusiness(iwc).getUserBusiness().getNameLastFirst(applicant, true);
    				
    				row.createCell((short) 0).setCellValue(name);
    				
    				if (applicant.getPersonalID() != null) {    					
    					row.createCell((short) 1).setCellValue(PersonalIDFormatter.format(applicant.getPersonalID(), locale));
    				}    				
    				
    				String emails = this.getParentsEmails(iwc, applicant);
    				if (emails != null) {    					
    					row.createCell((short) 2).setCellValue(emails);
    				}     				
    				
    				if (address != null) {
    					row.createCell((short) 3).setCellValue(address.getStreetAddress());
    					row.createCell((short) 4).setCellValue(address.getPostalCode().getPostalCode());
    					row.createCell((short) 5).setCellValue(address.getCity());
    				}
    				
    				try {
    					phone = getUserBusiness(iwc).getUsersHomePhone(applicant);
    					
    					//getUserBusiness(iwc).getUsers
    					if (phone != null && phone.getNumber() != null) {
    						row.createCell((short) 6).setCellValue(phone.getNumber());
    					}
    				} catch (NoPhoneFoundException npfe){
    					npfe.printStackTrace();
    				}    				
    				
    				String genderString = null;
					if (PIDChecker.getInstance().isFemale(applicant.getPersonalID())) {
						genderString = iwrb.getLocalizedString("school.girl", "Girl");
					}
					else {
						genderString = iwrb.getLocalizedString("school.boy", "Boy");
					}
					row.createCell((short) 7).setCellValue(genderString);

    				if (school != null) {
    					String schoolName = school.getName();    					
    					row.createCell((short) 8).setCellValue(schoolName);
    				} 	

    				String rejectionDateString = getLocalizedTimestamp(iwc, getRejectionTimestamp(iwc, choice));  
    				if (rejectionDateString != null) {
    					row.createCell((short) 9).setCellValue(rejectionDateString);
    				}
    				
    			} catch (Exception e) {
    				e.printStackTrace(System.err);
    			}
    		}        	
        }
        
        wb.write(mos);
        
        buffer.setMimeType("application/x-msexcel");
        return buffer;
	}

	private IWTimestamp getRejectionTimestamp(IWContext iwc, SchoolChoice choice) throws IDOLookupException, FinderException, RemoteException {
		
		IWTimestamp timestamp = null;
		final String denied = getSchoolChoiceBusiness(iwc).getCaseStatusDenied().getStatus();
		
		CaseLogHome caseLogHome = null;	            
		caseLogHome = (CaseLogHome)IDOLookup.getHome(CaseLog.class);
		
		Collection coll =  caseLogHome.findAllCaseLogsByCase(choice);
		
		for (Iterator iter = coll.iterator(); iter.hasNext(); ) {
			CaseLog log = (CaseLog) iter.next();
			if(log.getStatusAfter().equals(denied)) {				
				timestamp = new IWTimestamp(log.getTimeStamp());
				break;
			}
		}
		
		return timestamp;
	}
	
	private String getLocalizedTimestamp(IWContext iwc, IWTimestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		IWCalendar cal = new IWCalendar(iwc.getCurrentLocale(), timestamp);
		if (cal != null) { 
			return cal.getLocaleDate(IWCalendar.SHORT);
		} else {	
			return null;
		}		
	}

	private Collection getSchoolChoices(IWContext iwc){
		Collection schoolChoices = new Vector();
		SchoolChoiceHome scHome;
		try {
			scHome = (SchoolChoiceHome) IDOLookup.getHome(SchoolChoice.class);
			String[] statuses = new String[] {getSchoolChoiceBusiness(iwc).getCaseStatusDenied().getStatus()};
			schoolChoices = scHome.findBySchoolIDAndSeasonIDAndStatus(schoolID, seasonID, statuses, ENTRIES_PER_PAGE, currentStartEntry);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return schoolChoices;
	}
	
	   /**
     * Fills given row with headers
     * 
     * @param wb
     * @param sheet
     * @param row
     */
    private void fillHeaderRow(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row) { 
        //create style of header font
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 11);
        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);
        
        // here we could create array of strings, I mean headers
        String[] headers = { 
        		iwrb.getLocalizedString("school.child", "Child"), 
        		iwrb.getLocalizedString("school.personal_id", "Personal ID"), 
        		iwrb.getLocalizedString("school.e-mail", "Email"), 
        		iwrb.getLocalizedString("school.address", "Address"),
        		iwrb.getLocalizedString("school.zip_code", "Zip code"), 
        		iwrb.getLocalizedString("school.city", "City"), 
        		iwrb.getLocalizedString("school.phone", "Phone"), 
        		iwrb.getLocalizedString("school.gender", "Gender"),
        		iwrb.getLocalizedString("school.last_provider", "Last provider"), 
        		iwrb.getLocalizedString("school.rejection_date", "Rejection date")};
        
        int[] headerWidths = { 30, 14, 25, 25, 10, 16, 16, 8, 30, 16 };
        
        HSSFCell cell;        
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell((short) i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
            
            sheet.setColumnWidth((short) i, (short) (headerWidths[i] * 256)); 
        }  
        
    }
    
	private SchoolChoiceBusiness getSchoolChoiceBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwac, SchoolChoiceBusiness.class);
	} 
	
	protected CommuneUserBusiness getUserBusiness(IWApplicationContext  iwc) throws IBOLookupException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
	
	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) throws RemoteException {
		return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);	
	}
	
	private String getParentsEmails(IWContext iwc, User child) throws RemoteException {
		String emails = null;
		Email email;

		try {
			Collection parents = getMemberFamilyLogic(iwc).getCustodiansFor(child);
			
			if (parents != null && !parents.isEmpty()) {
				Iterator iterPar = parents.iterator();
				while (iterPar.hasNext()) {
					User parent = (User) iterPar.next();					
					try {
						email = getCommuneUserBusiness(iwc).getUsersMainEmail(parent);
						if (email != null && email.getEmailAddress() != null && !email.getEmailAddress().equals(" ")) {							
							if (emails != null)
								emails = emails + ", " + email.getEmailAddress();
							else										
								emails = email.getEmailAddress();							
						}														
					}
					catch (NoEmailFoundException nef) {
						nef.printStackTrace();
					}					
				}
			}
		}catch (NoCustodianFound ncf) {
		}
		return emails;
	}
	
	private FamilyLogic getMemberFamilyLogic(IWContext iwc) throws RemoteException {
		return (FamilyLogic) com.idega.business.IBOLookup.getServiceInstance(iwc, FamilyLogic.class);
	}
	
	private CommuneUserBusiness getCommuneUserBusiness(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
	
	public String getMimeType() {
		if (buffer != null)
			return buffer.getMimeType();
		return super.getMimeType();
	}
	
	public void writeTo(OutputStream out) throws IOException {
		if (buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else
			System.err.println("buffer is null");
	}	
}
