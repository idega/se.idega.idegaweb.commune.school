/*
 * Created on 19.10.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import se.idega.idegaweb.commune.school.data.SchoolChoice;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class StudentPlacings extends SchoolCommuneBlock {
	
	private boolean showChoicesTable = false;
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.presentation.SchoolCommuneBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		
		int row = 1;
		GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back",localize("back","Back")));
		if (getResponsePage() != null)
			back.setPageToOpen(getResponsePage());

		if (getSession().getStudentID() != -1) {
			table.add(getInformationTable(iwc), 1, row++);
			table.setRowHeight(row++, "16");
			table.add(getSmallHeader(localize("school.placements", "Placements")), 1, row++);
			table.setRowHeight(row++, "3");
			table.add(getPlacingsTable(iwc), 1, row++);
			if (showChoicesTable) {
				table.setRowHeight(row++, "16");
				table.add(getSmallHeader(localize("school.choices", "Choices")), 1, row++);
				table.setRowHeight(row++, "3");
				table.add(getChoicesTable(iwc), 1, row++);
			}
			table.setRowHeight(row++, "16");
			table.add(back, 1, row++);
		}
		else {
			table.add(getLocalizedHeader("school.no_student_found","No student found."), 1, 1);
			table.add(back, 1, 3);
		}
		
		add(table);
	}
	
	protected Table getPlacingsTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(4);
		table.setRowColor(1, getHeaderColor());
		int column = 1;
		int row = 1;
			
		table.add(getLocalizedSmallHeader("school.school","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("school.group","Group"), column++, row);
		table.add(getLocalizedSmallHeader("school.valid_from","Valid from"), column++, row);
		table.add(getLocalizedSmallHeader("school.removed","Removed"), column++, row++);
		
		SchoolClassMember member;
		SchoolClass group;
		School provider;
		IWTimestamp validFrom;
		IWTimestamp terminated = null;
		
		Collection placings = getBusiness().getSchoolBusiness().findClassMemberInSchool(getSession().getStudentID(), getSession().getSchoolID());
		Iterator iter = placings.iterator();
		while (iter.hasNext()) {
			column = 1;
			member = (SchoolClassMember) iter.next();
			group = member.getSchoolClass();
			provider = group.getSchool();
			validFrom = new IWTimestamp(member.getRegisterDate());
			if (member.getRemovedDate() != null)
				terminated = new IWTimestamp(member.getRemovedDate());

			if (row % 2 == 0)
				table.setRowColor(row, getZebraColor1());
			else
				table.setRowColor(row, getZebraColor2());
	
			table.add(getSmallText(provider.getSchoolName()), column++, row);
			table.add(getSmallText(group.getSchoolClassName()), column++, row);
			table.add(getSmallText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			if (member.getRemovedDate() != null)
				table.add(getSmallText(terminated.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row++);
			else
				table.add(getSmallText("-"), column++, row++);
		}
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);

		return table;
	}

	protected Table getChoicesTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(6);
		table.setRowColor(1, getHeaderColor());
		int column = 1;
		int row = 1;
		
		CaseBusiness caseBusiness = (CaseBusiness) IBOLookup.getServiceInstance(iwc, CaseBusiness.class);
		Locale currentLocale = iwc.getCurrentLocale();
		
		table.add(getLocalizedSmallHeader("school.school","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("school.status","Status"), column++, row);
		table.add(getLocalizedSmallHeader("school.created","Created"), column++, row);
		table.add(getLocalizedSmallHeader("school.grade","Grade"), column++, row);
		table.add(getLocalizedSmallHeader("school.order","Order"), column++, row);
		table.add(getLocalizedSmallHeader("school.language","Language"), column++, row);
		
		School provider =getSession().getSchool();
		SchoolChoice choice;
		Timestamp created;
		IWTimestamp iwCreated;
		int grade;
		String langChoice;
		String status;
		int orderChoice;
		Collection choices = getBusiness().getSchoolChoiceBusiness().findByStudentAndSchool(getSession().getStudentID(), getSession().getSchoolID());
		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			++row;
			column = 1;
			choice = (SchoolChoice) iter.next();
			status = caseBusiness.getLocalizedCaseStatusDescription(choice.getCaseStatus(), currentLocale);
			created = choice.getCreated();
			if (created != null) {
				iwCreated = new IWTimestamp(created);
			} else {
				iwCreated = null;
			}
			try {
				grade = choice.getGrade() +1;
			} catch (Exception e) {
				grade = -1;
			}
			orderChoice = choice.getChoiceOrder();
			langChoice = choice.getLanguageChoice();
			
			if (row % 2 == 0)
				table.setRowColor(row, getZebraColor1());
			else
				table.setRowColor(row, getZebraColor2());
			
			table.add(getSmallText(provider.getSchoolName()), column++, row);
			table.add(getSmallText(status), column++, row);
			if (iwCreated != null) {
				table.add(getSmallText(iwCreated.getLocaleDate(currentLocale, IWTimestamp.SHORT)), column++, row);
			} else {
				table.add(getSmallText("-"), column++, row);
			}
			
			if (grade != -1) {
				table.add(getSmallText(Integer.toString(grade)), column++, row);
			} else {
				table.add(getSmallText("-"), column++, row);
			}
			
			if (orderChoice != -1) {
				table.add(getSmallText(Integer.toString(orderChoice)), column++, row);
			} else {
				table.add(getSmallText("-"), column++, row);
			}
			if (langChoice != null) {
				table.add(getSmallText(langChoice), column++, row);
			} else {
				table.add(getSmallText("-"), column++, row);
			}
		}
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		
		return table;
	}
	
	
	protected Table getInformationTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setColumns(3);
		table.setWidth(1, "100");
		table.setWidth(2, "6");
		int row = 1;
		
		User child = getBusiness().getUserBusiness().getUser(getSession().getStudentID());
		if (child != null) {
			Address address = getBusiness().getUserBusiness().getUsersMainAddress(child);
			Collection parents = getBusiness().getUserBusiness().getParentsForChild(child);
			
			table.add(getLocalizedSmallHeader("school.student","Student"), 1, row);
			table.add(getSmallText(child.getNameLastFirst(true)), 3, row);
			table.add(getSmallText(" - "), 3, row);
			table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), 3, row++);
			
			if (address != null) {
				table.add(getLocalizedSmallHeader("school.address","Address"), 1, row);
				table.add(getSmallText(address.getStreetAddress()), 3, row);
				if (address.getPostalAddress() != null)
					table.add(getSmallText(", "+address.getPostalAddress()), 3, row);
				row++;
			}
			
			table.setHeight(row++, 12);
			
			if (parents != null) {
				table.add(getLocalizedSmallHeader("school.parents","Parents"), 1, row);
				Phone phone;
				Email email;

				Iterator iter = parents.iterator();
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					address = getBusiness().getUserBusiness().getUsersMainAddress(parent);
					email = getBusiness().getUserBusiness().getEmail(parent);
					phone = getBusiness().getUserBusiness().getHomePhone(parent);

					table.add(getSmallText(parent.getNameLastFirst(true)), 3, row);
					table.add(getSmallText(" - "), 3, row);
					table.add(getSmallText(PersonalIDFormatter.format(parent.getPersonalID(), iwc.getCurrentLocale())), 3, row++);
			
					if (address != null) {
						table.add(getSmallText(address.getStreetAddress()), 3, row);
						if (address.getPostalAddress() != null)
							table.add(getSmallText(", "+address.getPostalAddress()), 3, row);
						row++;
					}
					if (phone != null && phone.getNumber() != null) {
						table.add(getSmallText(localize("school.phone","Phone")+": "), 3, row);
						table.add(getSmallText(phone.getNumber()), 3, row++);
					}
					if (email != null && email.getEmailAddress() != null) {
						Link link = getSmallLink(email.getEmailAddress());
						link.setURL("mailto:"+email.getEmailAddress(), false, false);
						table.add(link, 3, row++);
					}
			
					table.setHeight(row++, 12);
				}
			}
		}
		
		return table;
	}
	
	public void setShowChoicesTable(boolean show) {
		this.showChoicesTable = show;
	}
}
