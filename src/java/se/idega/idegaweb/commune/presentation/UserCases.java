package se.idega.idegaweb.commune.presentation;
import java.text.DateFormat;
import java.util.*;
import javax.ejb.EJBException;
import se.idega.idegaweb.commune.message.data.*;
import se.idega.idegaweb.commune.business.CommuneCaseBusiness;
import se.idega.idegaweb.commune.message.business.*;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.builder.data.IBPage;
import com.idega.core.user.data.User;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.Converter;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;
/**
 * Title: UserCases
 * Description: A class to view Cases for a User in the idegaWeb Commune application
 * Copyright:    Copyright idega Software (c) 2002
 * Company:	idega Software
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class UserCases extends CommuneBlock {
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private final static int ACTION_VIEW_CASE_LIST = 1;
	/*
	private final static int ACTION_VIEW_MESSAGE = 2;
	private final static int ACTION_SHOW_DELETE_INFO = 3;
	private final static int ACTION_DELETE_MESSAGE = 4;
	
	private final static String PARAM_VIEW_MESSAGE = "msg_view_msg";
	private final static String PARAM_VIEW_MESSAGE_LIST = "msg_view_msg_list";
	private final static String PARAM_MESSAGE_ID = "msg_id";
	private final static String PARAM_SHOW_DELETE_INFO = "msg_s_delete_i";
	private final static String PARAM_DELETE_MESSAGE = "msg_delete_message";
	*/
	private final static String PARAM_CASE_ID = "USC_CASE_ID";
	private final static String PARAM_MANAGER_ID = ManagerView.PARAM_MANAGER_ID;
	private Table mainTable = null;
	private int manager_page_id = -1;
	public UserCases() {
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	public void main(IWContext iwc) {
		//this.setResourceBundle(getResourceBundle(iwc));
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW_CASE_LIST :
					viewCaseList(iwc);
					/*   break;
					 case ACTION_VIEW_MESSAGE:
					   viewMessage(iwc);
					   break;
					 case ACTION_SHOW_DELETE_INFO:
					   showDeleteInfo(iwc);
					   break;
					 case ACTION_DELETE_MESSAGE:
					   deleteMessage(iwc);
					   viewMessageList(iwc);
					   break;*/
				default :
					break;
			}
			super.add(mainTable);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}
	public void add(PresentationObject po) {
		if (mainTable == null) {
			mainTable = new Table();
			mainTable.setCellpadding(14);
			mainTable.setCellspacing(0);
			mainTable.setColor(getBackgroundColor());
			mainTable.setWidth(600);
		}
		mainTable.add(po);
	}
	private int parseAction(IWContext iwc) {
		int action = ACTION_VIEW_CASE_LIST;
		/*if(iwc.isParameterSet(PARAM_VIEW_MESSAGE)){
		  action = ACTION_VIEW_MESSAGE;
		}
		if(iwc.isParameterSet(PARAM_SHOW_DELETE_INFO)){
		  action = ACTION_SHOW_DELETE_INFO;
		}
		if(iwc.isParameterSet(PARAM_DELETE_MESSAGE)){
		  action = ACTION_DELETE_MESSAGE;
		}*/
		return action;
	}
	private void viewCaseList(IWContext iwc) throws Exception {
		add(getLocalizedHeader("usercases.my_cases", "My cases"));
		add(new Break(2));
		if (iwc.isLoggedOn()) {
			Collection cases = getCommuneCaseBusiness(iwc).getAllCasesDefaultVisibleForUser(Converter.convertToNewUser(iwc.getUser()));
			if (cases != null & !cases.isEmpty()) {
				Form f = new Form();
				ColumnList messageList = new ColumnList(6);
				f.add(messageList);
				messageList.setBackroundColor("#e0e0e0");
				messageList.setHeader(localize("usercases.case_number", "Case number"), 1);
				messageList.setHeader(localize("usercases.concerning", "Concerning"), 2);
				messageList.setHeader(localize("usercases.name", "Name"), 3);
				messageList.setHeader(localize("usercases.date", "Date"), 4);
				messageList.setHeader(localize("usercases.manager", "Manager"), 5);
				messageList.setHeader(localize("usercases.status", "Status"), 6);
				Collection messages = getCaseBusiness(iwc).getAllCasesForUser(Converter.convertToNewUser(iwc.getUser()));
				Text caseNumber = null;
				Text caseType = null;
				Text caseOwnerName = null;
				Text date = null;
				Text manager = null;
				Text status = null;
				//CheckBox deleteCheck = null;
				boolean isRead = false;
				DateFormat dateFormat = com.idega.util.CustomDateFormat.getDateTimeInstance(iwc.getCurrentLocale());
				if (cases != null) {
					Collection casesVector = cases;
					//Collection casesVector = new Vector(cases);
					//Collections.sort(messageVector,new MessageComparator());
					Iterator iter = casesVector.iterator();
					while (iter.hasNext()) {
						try {
							Case theCase = (Case) iter.next();
							Date caseDate = new Date(theCase.getCreated().getTime());
							//isRead = getCaseBusiness(iwc).isMessageRead(msg);
							caseNumber = getSmallText(theCase.getPrimaryKey().toString());
							caseType =
								getSmallText(
									getCaseBusiness(iwc).getCaseBusiness(theCase.getCaseCode()).getLocalizedCaseDescription(
										theCase,
										iwc.getCurrentLocale()));
							//subject.addParameter(PARAM_VIEW_MESSAGE,"true");
							//subject.addParameter(PARAM_MESSAGE_ID,msg.getPrimaryKey().toString());
							/*if ( !isRead )
								subject.setBold();
							date = this.getSmallText(dateFormat.format(msgDate));
							if ( !isRead )
								date.setBold();
							*/
							date = this.getSmallText(dateFormat.format(caseDate));
							String managerName = null;
							int managerID = -1;
							try {
								Group handler = theCase.getHandler();
								managerID = ((Integer)handler.getPrimaryKey()).intValue();
								managerName = getUserBusiness(iwc).getNameOfGroupOrUser(handler);
							} catch (Exception e) {
								//manager = this.getSmallText("-");
							}
							if (managerName == null) {
								manager = this.getSmallText("-");
							} else {
								manager = this.getSmallText(managerName);
								if (getManagerPage() != -1) {
									Link managerLink = new Link(manager);
									managerLink.setPage(getManagerPage());
									managerLink.addParameter(PARAM_MANAGER_ID, managerID);
									manager = managerLink;
								}
							}
							//deleteCheck = new CheckBox(PARAM_CASE_ID,msg.getPrimaryKey().toString());
							caseOwnerName = getSmallText(theCase.getOwner().getFirstName());
							status =
								getSmallText(
									getCaseBusiness(iwc).getLocalizedCaseStatusDescription(
										theCase.getCaseStatus(),
										iwc.getCurrentLocale()));
							messageList.add(caseNumber);
							messageList.add(caseType);
							messageList.add(caseOwnerName);
							messageList.add(date);
							messageList.add(manager);
							messageList.add(status);
						} catch (Exception e) {
							add(e);
							e.printStackTrace();
						}
						//messageList.add(deleteCheck);
					}
				}
				//SubmitButton deleteButton = new SubmitButton(this.getLocalizedString("message.delete", "Delete", iwc));
				//deleteButton.setAsImageButton(true);
				messageList.skip(2);
				//PresentationObject[] bottomRow = new PresentationObject[3];
				//bottomRow[2] = deleteButton;
				//messageList.addBottomRow(bottomRow);
				add(f);
			} else {
				add(getSmallText(localize("usercases.no_ongoing_cases", "No ongoing cases")));
			}
		}
		//f.addParameter(PARAM_SHOW_DELETE_INFO,"true");
	}
	/*
	  private void viewCase(IWContext iwc)throws Exception{
	    Message msg = getMessage(iwc.getParameter(PARAM_MESSAGE_ID),iwc);
	    getMessageBusiness(iwc).markMessageAsRead(msg);
	
	    add(getLocalizedHeader("message.message","Message"));
	    add(new Break(2));
	    add(getLocalizedText("message.from","From"));
	    add(getText(": "));
	    //add(getLink(msg.getSenderName()));
	    add(new Break(2));
	    add(getLocalizedText("message.date","Date"));
	    add(getText(": "+(new IWTimestamp(msg.getCreated())).getLocaleDate(iwc)));
	    add(new Break(2));
	    add(getLocalizedText("message.subject","Subject"));
	    add(getText(": "+msg.getSubject()));
	    add(new Break(2));
	    add(getText(msg.getBody()));
	
	    add(new Break(2));
	    Table t = new Table();
	    t.setWidth("100%");
	    t.setAlignment(1,1,"right");
	    Link l = getLocalizedLink("message.back", "Back");
	    l.addParameter(PARAM_VIEW_MESSAGE_LIST,"true");
	    l.setAsImageButton(true);
	    t.add(l,1,1);
	    add(t);
	  }
	
	  private void showDeleteInfo(IWContext iwc)throws Exception{
	    String[] ids = iwc.getParameterValues(PARAM_MESSAGE_ID);
	    int msgId = 0;
	    int nrOfMessagesToDelete = 0;
	    if(ids!=null){
	      nrOfMessagesToDelete = ids.length;
	      msgId = Integer.parseInt(ids[0]);
	    }
	
	    if(nrOfMessagesToDelete==1){
	      add(getLocalizedHeader("message.delete_message","Delete message"));
	    }else{
	      add(getLocalizedHeader("message.delete_messages","Delete messages"));
	    }
	    add(new Break(2));
	
	    String s = null;
	    if(nrOfMessagesToDelete==0){
	      s = localize("message.no_messages_to_delete","No messages selected. You have to mark the message(s) to delete.");
	    }else if(nrOfMessagesToDelete==1){
	      Message msg = getMessageBusiness(iwc).getUserMessage(msgId);
	      s = localize("message.one_message_to_delete","Do you really want to delete the message with subject: ")+msg.getSubject()+"?";
	    }else{
	      s = localize("message.messages_to_delete","Do you really want to delete the selected messages?");
	    }
	
	    Table t = new Table(1,5);
	    t.setWidth("100%");
	    t.add(getText(s),1,1);
	    t.setAlignment(1,1,"center");
	    if(nrOfMessagesToDelete==0){
	      Link l = getLocalizedLink("message.back","back");
	      l.addParameter(PARAM_VIEW_MESSAGE_LIST,"true");
	      l.setAsImageButton(true);
	      t.add(l,1,4);
	    }else{
	      Link l = getLocalizedLink("message.ok","OK");
	      l.addParameter(PARAM_DELETE_MESSAGE,"true");
	      for(int i=0; i<ids.length; i++){
	        l.addParameter(PARAM_MESSAGE_ID,ids[i]);
	      }
	      l.setAsImageButton(true);
	      t.add(l,1,4);
	      t.add(getText(" "),1,4);
	      l = getLocalizedLink("message.cancel","Cancel");
	      l.addParameter(PARAM_VIEW_MESSAGE_LIST,"true");
	      l.setAsImageButton(true);
	      t.add(l,1,4);
	    }
	    t.setAlignment(1,4,"center");
	    add(t);
	  }
	
	  private void deleteMessage(IWContext iwc)throws Exception{
	    String[] ids = iwc.getParameterValues(PARAM_MESSAGE_ID);
	    for(int i=0; i<ids.length; i++){
	      getMessageBusiness(iwc).deleteUserMessage(Integer.parseInt(ids[i]));
	    }
	  }*/
	private CaseBusiness getCaseBusiness(IWContext iwc) throws Exception {
		return (CaseBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CaseBusiness.class);
	}
	private CommuneCaseBusiness getCommuneCaseBusiness(IWContext iwc) throws Exception {
		return (CommuneCaseBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CommuneCaseBusiness.class);
	}
	private UserBusiness getUserBusiness(IWContext iwc) throws Exception {
		return (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	}	
	
	private Case getCase(String id, IWContext iwc) throws Exception {
		int msgId = Integer.parseInt(id);
		Case msg = getCaseBusiness(iwc).getCase(msgId);
		return msg;
	}
	public void setManagerPage(IBPage page) {
		manager_page_id = page.getID();
	}
	public void setManagerPage(int ib_page_id) {
		manager_page_id = ib_page_id;
	}
	public int getManagerPage() {
		return manager_page_id;
	}
}
