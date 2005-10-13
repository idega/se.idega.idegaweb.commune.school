/*
 * Created on 13.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.message.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;

import com.idega.block.process.message.data.Message;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

/**
 * @author Roar
 * In addition to being a regular MessageBox, AdminMessageBox views messages handled by 
 * the groups the user is member of and shows the school provider the message is sent to, if any.
 * 
 */
public class AdminMessageBox extends MessageBox {

	private Collection _groups;
	
	/**
	 * @see MessageBox
	 */
	Collection getMessages(IWContext iwc, User user, int numberOfEntries, int startingEntry) throws Exception{
		return getMessageBusiness(iwc).findMessages(user, _groups, numberOfEntries, startingEntry);
	}
	
	int getNumberOfMessages(IWContext iwc, User user) {
		try {
			UserBusiness userBusiness = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			_groups = userBusiness.getUserGroups(user);
			return getMessageBusiness(iwc).getNumberOfMessages(user, _groups);
		}
		catch (Exception e) {
			return 0;
		}
	}
	/**
	 * @see MessageBox
	 */
	void addTableHeader(Table messageTable, int row) {
		super.addTableHeader(messageTable, row);
		messageTable.add(getSmallHeader(localize("message.provider", "Provider")), getDateColumn() + 1, row);
	}	
	
	/**
	 * @see MessageBox
	 */	
	void addMessageToTable(IWContext iwc, Table messageTable, Message msg, int row, DateFormat dateFormat, int messageNumber) throws Exception{
		super.addMessageToTable(iwc, messageTable, msg, row, dateFormat, messageNumber);
		SchoolChoice sc = null;
		try{
			sc = getSchoolChoiceBusiness().getSchoolChoice(msg.getNodeID());
		} catch (FinderException ex){
			//ignore
		}
		
		if (sc != null){
			School provider = sc.getChosenSchool();
			messageTable.add(provider.getName(), getDateColumn() + 1, row);
		}
	}
	
	public SchoolChoiceBusiness getSchoolChoiceBusiness() throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolChoiceBusiness.class);	
	}	
	
	/**
	 * @see MessageBox
	 */	
	int getDeleteColumn(){
		return super.getDeleteColumn() + 1;
	}
	
}
