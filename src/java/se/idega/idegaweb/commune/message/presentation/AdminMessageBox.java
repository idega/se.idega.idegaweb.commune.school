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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author Roar
 * In addition to being a regular MessageBox, AdminMessageBox views messages handled by 
 * the groups the user is member of and shows the school provider the message is sent to, if any.
 * 
 */
public class AdminMessageBox extends MessageBox {

	/**
	 * @see MessageBox
	 */
	Collection getMessages(IWContext iwc, User user) throws Exception{
		// get my messages
		Collection messages = super.getMessages(iwc, user);	

		// add messages belonging to my groups 
		UserBusiness userBusiness = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		Collection groupCollection = userBusiness.getUserGroups(user);
						
		Iterator g = groupCollection.iterator();
		while(g.hasNext()){
			messages.addAll(getMessageBusiness(iwc).findMessages((Group) g.next()));
		}
		
		//removing duplicates
		Iterator m = messages.iterator();
		Map map = new HashMap();
		while (m.hasNext()){
			Integer id = new Integer(((Message) m.next()).getNodeID());
			if (map.containsKey(id)){
				m.remove();
			} else {
				map.put(id, null);
			}
		}
		
		return messages;
	}
	
	/**
	 * @see MessageBox
	 */
	void addTableHeader(Table messageTable) {
		super.addTableHeader(messageTable);
		messageTable.add(getSmallHeader(localize("message.provider", "Provider")), 3, 1);
	}	
	
	/**
	 * @see MessageBox
	 */	
	void addMessageToTable(IWContext iwc, Table messageTable, Message msg, int row, DateFormat dateFormat) throws Exception{
		super.addMessageToTable(iwc, messageTable, msg, row, dateFormat);
		SchoolChoice sc = null;
		try{
			sc = getSchoolChoiceBusiness().getSchoolChoice(msg.getNodeID());
		} catch (FinderException ex){
			//ignore
		}
		
		if (sc != null){
			School provider = sc.getChosenSchool();
			messageTable.add(provider.getName(), 3, row);
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
