package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.data.SchoolChoiceHome;
import se.idega.util.PIDChecker;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.business.UserBusiness;
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
		if (iwc.isParameterSet(PARAMETER_NEXT_START_ENTRY)) {
			try {
				currentStartEntry = Integer.parseInt(iwc.getParameter(PARAMETER_NEXT_START_ENTRY));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		iwrb = super.getResourceBundle();
		int schoolID = super.getSchoolID();
		int seasonID = super.getSchoolSeasonID();
		String[] statuses = new String[] {getSchoolChoiceBusiness(iwc).getCaseStatusDenied().getStatus()};
		schoolChoices = scHome.findBySchoolIDAndSeasonIDAndStatus(schoolID, seasonID, statuses, ENTRIES_PER_PAGE, currentStartEntry);
		totalChoices = scHome.countBySchoolIDAndSeasonIDAndStatus(schoolID, seasonID, statuses);
		drawTable(iwc);
	}
	
	private void drawTable(IWContext iwc) {
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		table.add(getPagesTable(), 1, 1);
		if (tableWidth == null) {
			table.setWidth(getWidth());
		} else {
			table.setWidth(tableWidth);
		}
		table.add(getStudentList(iwc), 1, 2);
		add(table);
	}
	
	private Table getPagesTable() {
		Table table = new Table(3, 1);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setWidth("100%");
		table.setWidth(1, "33%");
		table.setWidth(3, "33%");
		Link lNext = getSmallLink(localize("next", "Next"));
		Link lPrev = getSmallLink(localize("previous", "Previous"));
		
		if (currentStartEntry != 0) {
			lPrev.addParameter(PARAMETER_NEXT_START_ENTRY, Integer.toString(currentStartEntry - ENTRIES_PER_PAGE));
			table.add(lPrev, 1, 1);
		} else {
			table.add(getSmallText(localize("previous", "Previous")), 1, 1);
		}
		table.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_LEFT);
		
		table.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_CENTER);
		if (totalChoices == 0) {
			table.add(getSmallHeader("0/"+  ((totalChoices / ENTRIES_PER_PAGE)+1) ), 2, 1);
		} else {
			table.add(getSmallHeader(  ((currentStartEntry/totalChoices)+1)  +"/"+  ((totalChoices / ENTRIES_PER_PAGE)+1) ), 2, 1);
		}
		
		//table.add(getSmallHeader(  ((21/20)+1)   +"/"+  (Math.ceil(1 / 10)) ), 2, 1);
		if (currentStartEntry < (totalChoices - ENTRIES_PER_PAGE)) {
			lNext.addParameter(PARAMETER_NEXT_START_ENTRY, Integer.toString(currentStartEntry + ENTRIES_PER_PAGE));
			table.add(lNext, 3, 1);
		} else {
			table.add(getSmallText(localize("next", "Next")), 3, 1);
		}
		table.setAlignment(3, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		
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
		table.add(getSmallHeader(localize("school.gender", "Gender")), column++, row);
		table.add(getSmallHeader(localize("school.from_school", "From School")), column++, row);
		
		User applicant;
		School school;
		Address address;
		Phone phone;
		SchoolChoice choice;
		String name = null;
		Locale locale = iwc.getCurrentLocale();
		for (Iterator iter = schoolChoices.iterator(); iter.hasNext(); ) {
			choice = (SchoolChoice) iter.next();
			column = 1;
			++row;
			try {
				applicant = getUserBusiness(iwc).getUser(choice.getChildId());
				school = getBusiness().getSchoolBusiness().getSchool(new Integer(choice.getCurrentSchoolId()));
				address = getUserBusiness(iwc).getUsersMainAddress(applicant);
				phone = getUserBusiness(iwc).getUsersHomePhone(applicant);
				name = getBusiness().getUserBusiness().getNameLastFirst(applicant, true);
				table.add(getSmallText(name), column++, row);
				if (applicant.getPersonalID() != null) {
					table.add(getSmallText(PersonalIDFormatter.format(applicant.getPersonalID(), locale)), column++, row);
				}
				if (address != null && address.getStreetAddress() != null) {
					table.add(getSmallText(address.getStreetAddress()), column, row);
				}
				column++;
				if (phone != null && phone.getNumber() != null) {
					table.add(getSmallText(phone.getNumber()), column, row);
				}
				column++;
				if (PIDChecker.getInstance().isFemale(applicant.getPersonalID())) {
					table.add(getSmallText(localize("school.girl", "Girl")), column++, row);
				}	else {
					table.add(getSmallText(localize("school.boy", "Boy")), column++, row);
				}
				if (school != null) {
					String schoolName = school.getName();
					if (schoolName.length() > 20)
						schoolName = schoolName.substring(0, 20) + "...";
					table.add(getSmallText(schoolName), column++, row);
				}
				
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		
		
		table.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
		table.setRowColor(1, getHeaderColor());
		++row;
		table.add(getSmallText(localize("school.total_students","Total students")+" : "+totalChoices), 1, row);
		table.mergeCells(1, row, 4, row);
		return table;
	}

	public void setWidth(String width) {
		//this.tableWidth = width;
	}
	
	private SchoolChoiceBusiness getSchoolChoiceBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwac, SchoolChoiceBusiness.class);
	}
	
	private UserBusiness getUserBusiness(IWContext iwc) throws RemoteException {
		return (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	}
	
}
