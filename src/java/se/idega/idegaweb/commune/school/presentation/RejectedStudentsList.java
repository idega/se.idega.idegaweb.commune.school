package se.idega.idegaweb.commune.school.presentation;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoCustodianFound;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.data.SchoolChoiceHome;
import se.idega.util.PIDChecker;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.Link;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;

/**
 * @author Gimmi the great
 */
public class RejectedStudentsList extends SchoolCommuneBlock {

	Collection schoolChoices;
	int totalChoices;
	String tableWidth;
	int currentStartEntry = 0;
	int ENTRIES_PER_PAGE = 10;
	IWResourceBundle iwrb;
	
	private String PARAMETER_NEXT_START_ENTRY = "prm_se";
	
	public void init(IWContext iwc) throws Exception {
		SchoolChoiceHome scHome = (SchoolChoiceHome) IDOLookup.getHome(SchoolChoice.class);
		if (iwc.isParameterSet(this.PARAMETER_NEXT_START_ENTRY)) {
			try {
				this.currentStartEntry = Integer.parseInt(iwc.getParameter(this.PARAMETER_NEXT_START_ENTRY));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.iwrb = super.getResourceBundle();
		int schoolID = super.getSchoolID();
		int seasonID = super.getSchoolSeasonID();
		String[] statuses = new String[] {getSchoolChoiceBusiness(iwc).getCaseStatusDenied().getStatus()};
		this.schoolChoices = scHome.findBySchoolIDAndSeasonIDAndStatus(schoolID, seasonID, statuses, this.ENTRIES_PER_PAGE, this.currentStartEntry);
		this.totalChoices = scHome.countBySchoolIDAndSeasonIDAndStatus(schoolID, seasonID, statuses);
		drawTable(iwc);
	}
	
	private void drawTable(IWContext iwc) {
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		table.add(getPagesTable(), 1, 1);
		if (useStyleNames()) {
			table.setCellpaddingLeft(1, 1, 12);
			table.setCellpaddingRight(1, 1, 12);
		}
		if (this.tableWidth == null) {
			table.setWidth(getWidth());
		} else {
			table.setWidth(this.tableWidth);
		}
		table.setHeight(2, 3);
		table.add(getStudentList(iwc), 1, 3);
		add(table);
	}
	
	private Table getPagesTable() {
		Table table = new Table(3, 2);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		//table.setBorder(2);
		table.setWidth("100%");
		table.setWidth(1, "33%");
		table.setWidth(3, "33%");
		Link lNext = getSmallLink(localize("next", "Next"));
		Link lPrev = getSmallLink(localize("previous", "Previous"));
		
		table.setAlignment(3, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(getXlsLink(), 3, 1);
		
		if (this.currentStartEntry != 0) {
			lPrev.addParameter(this.PARAMETER_NEXT_START_ENTRY, Integer.toString(this.currentStartEntry - this.ENTRIES_PER_PAGE));
			table.add(lPrev, 1, 2);
		} else {
			table.add(getSmallText(localize("previous", "Previous")), 1, 2);
		}
		table.setAlignment(1, 2, Table.HORIZONTAL_ALIGN_LEFT);
		
		table.setAlignment(2, 2, Table.HORIZONTAL_ALIGN_CENTER);
		if (this.totalChoices == 0) {
			table.add(getSmallHeader("0/"+  ((this.totalChoices / this.ENTRIES_PER_PAGE)+1) ), 2, 2);
		} else {
			table.add(getSmallHeader(  ((this.currentStartEntry/this.totalChoices)+1)  +"/"+  ((this.totalChoices / this.ENTRIES_PER_PAGE)+1) ), 2, 2);
		}
		
		//table.add(getSmallHeader(  ((21/20)+1)   +"/"+  (Math.ceil(1 / 10)) ), 2, 1);
		if (this.currentStartEntry < (this.totalChoices - this.ENTRIES_PER_PAGE)) {
			lNext.addParameter(this.PARAMETER_NEXT_START_ENTRY, Integer.toString(this.currentStartEntry + this.ENTRIES_PER_PAGE));
			table.add(lNext, 3, 1);
		} else {
			table.add(getSmallText(localize("next", "Next")), 3, 2);
		}
		table.setAlignment(3, 2, Table.HORIZONTAL_ALIGN_RIGHT);
		
		return table;
	}
	
	private Table getStudentList(IWContext iwc) {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setWidth("100%");
		
		int row = 1;
		int column = 1;
		table.add(getSmallHeader(localize("school.name", "Name")), column++, row);
		table.add(getSmallHeader(localize("school.personal_id", "Personal ID")), column++, row);
		table.add(getSmallHeader(localize("school.address", "Address")), column++, row);
		table.add(getSmallHeader(localize("school.phone", "Phone")), column++, row);
		if (!useStyleNames()) {
			table.add(getSmallHeader(localize("school.gender", "Gender")), column++, row);
		}
		table.add(getSmallHeader(localize("school.from_school", "From School")), column++, row);
		if (useStyleNames()) {
			table.setCellpaddingLeft(1, row, 12);
			table.setCellpaddingRight(table.getColumns(), row, 12);
		}
		
		User applicant;
		School school;
		Address address;
		Phone phone;
		SchoolChoice choice;
		String name = null;
		Locale locale = iwc.getCurrentLocale();
		for (Iterator iter = this.schoolChoices.iterator(); iter.hasNext(); ) {
			choice = (SchoolChoice) iter.next();
			column = 1;
			++row;
			try {
				applicant = getUserBusiness(iwc).getUser(choice.getChildId());
				school = getBusiness().getSchoolBusiness().getSchool(new Integer(choice.getCurrentSchoolId()));
				address = getUserBusiness(iwc).getUsersMainAddress(applicant);
				
				if (useStyleNames()) {
					if (row % 2 == 0) {
						table.setRowStyleClass(row, getDarkRowClass());
					}
					else {
						table.setRowStyleClass(row, getLightRowClass());
					}
					table.setCellpaddingLeft(1, row, 12);
					table.setCellpaddingRight(table.getColumns(), row, 12);
				}
				
				name = getBusiness().getUserBusiness().getNameLastFirst(applicant, true);
				
				String emails = this.getParentsEmails(iwc, applicant);				
				if (emails!= null) {
					Link emailLink = this.getSmallLink(name);
					emailLink.setURL("mailto: " + emails);
					emailLink.setSessionId(false);
					table.add(emailLink, column++, row);
				} else {
					table.add(getSmallText(name), column++, row);
				}
				
				
				if (applicant.getPersonalID() != null) {
					table.add(getSmallText(PersonalIDFormatter.format(applicant.getPersonalID(), locale)), column++, row);
				}
				if (address != null && address.getStreetAddress() != null) {
					table.add(getSmallText(address.getStreetAddress()), column, row);
				}
				column++;
				try {
					phone = getUserBusiness(iwc).getUsersHomePhone(applicant);
					if (phone != null && phone.getNumber() != null) {
						table.add(getSmallText(phone.getNumber()), column, row);
					}
				} catch (NoPhoneFoundException npfe){
					log(npfe);
				}
				
				
				column++;
				if (!useStyleNames()) {
					if (PIDChecker.getInstance().isFemale(applicant.getPersonalID())) {
						table.add(getSmallText(localize("school.girl", "Girl")), column++, row);
					}
					else {
						table.add(getSmallText(localize("school.boy", "Boy")), column++, row);
					}
				}
				if (school != null) {
					String schoolName = school.getName();
					if (schoolName.length() > 20) {
						schoolName = schoolName.substring(0, 20) + "...";
					}
					table.add(getSmallText(schoolName), column++, row);
				}
				
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		
		
		if (!useStyleNames()) {
			table.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
			table.setRowColor(1, getHeaderColor());
		}
		else {
			table.setRowStyleClass(1, getHeaderRowClass());
		}
		++row;
		table.setHeight(row++, 3);
		table.add(getSmallText(localize("school.total_students","Total students")+" : "+this.totalChoices), 1, row);
		table.mergeCells(1, row, 4, row);
		if (useStyleNames()) {
			table.setCellpaddingLeft(1, row, 12);
		}
		return table;
	}

	public void setWidth(String width) {
		//this.tableWidth = width;
	}
	
	private SchoolChoiceBusiness getSchoolChoiceBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwac, SchoolChoiceBusiness.class);
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
							if (emails != null) {
								emails = emails + "; " + email.getEmailAddress();
							}
							else {
								emails = email.getEmailAddress();
							}							
						}														
					}
					catch (NoEmailFoundException nef) {
						log(nef);
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
	
	protected Link getXlsLink() {
		DownloadLink link = new DownloadLink(getBundle().getImage("shared/xls.gif"));
		link.setMediaWriterClass(RejectedStudentsListWriter.class);
		link.addParameter(RejectedStudentsListWriter.PARAMETER_SCHOOL_ID, super.getSchoolID());
		link.addParameter(RejectedStudentsListWriter.PARAMETER_SCHOOL_SEASON_ID, super.getSchoolSeasonID());
		return link;
	}	 
	
	
}
