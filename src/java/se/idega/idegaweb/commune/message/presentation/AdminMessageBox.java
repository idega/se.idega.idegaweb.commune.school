/*
 * Created on 13.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.message.presentation;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author Roar
 * In addition to being a regular MessageBox, AdminMessageBox views messages handled by 
 * the groups the user is member of.
 * 
 */
public class AdminMessageBox extends MessageBox {

	/**
	 * @see MessageBox
	 */
	Collection getMessages(IWContext iwc, User user, MessageBusiness messageBusiness) throws Exception{
		// get my messages
		Collection messages = super.getMessages(iwc, user, messageBusiness);	

		// add messages belonging to my group 
		UserBusiness userBusiness = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		Collection groupCollection = userBusiness.getUserGroups(user);
						
		Iterator g = groupCollection.iterator();
		while(g.hasNext()){
			messages.addAll(messageBusiness.findMessages((Group) g.next()));
		}
		
		return messages;
	}
	
	/**
	 * @see MessageBox
	 */
	void addTableHeader(Table messageTable) {
		super.addTableHeader(messageTable);
		messageTable.add(getSmallHeader(localize("message.owner", "Owner")), 3, 1);
	}	
	
	/**
	 * @see MessageBox
	 */	
	void addMessageToTable(IWContext iwc, Table messageTable, Message msg, int row, DateFormat dateFormat) throws Exception{
		super.addMessageToTable(iwc, messageTable, msg, row, dateFormat);
		messageTable.add(msg.getOwner().getName(), 3, row);		
	}
	
	/**
	 * @see MessageBox
	 */	
	int getDeleteColumn(){
		return 4;
	}
	
}
