package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.business.*;
import com.idega.block.school.business.*;
import com.idega.block.school.data.*;
import com.idega.user.business.*;
import com.idega.business.*;
import com.idega.user.data.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWConstants;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import java.util.*;

/**
 * @author aron
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolAdminDirector extends CommuneBlock {
	
	GroupBusiness grpBuiz;
	UserBusiness usrBuiz;
	CommuneUserBusiness commBuiz;
	SchoolBusiness schoolBuiz;
	Integer schoolChoiceApproverPageId = null;
	IWResourceBundle iwrb;
	
	
	public String getBundleIdentifier(){
		return this.IW_BUNDLE_IDENTIFIER;
	}
	

	private void init(IWContext iwc) throws RemoteException{
		usrBuiz =(UserBusiness) IBOLookup.getServiceInstance(iwc,UserBusiness.class);
		grpBuiz = (GroupBusiness) IBOLookup.getServiceInstance(iwc,GroupBusiness.class);
		commBuiz = (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc,CommuneUserBusiness.class);
		schoolBuiz = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
	}

	public void main(IWContext iwc) throws Exception{
		iwrb = getResourceBundle(iwc);
		
		if(iwc.isLoggedOn() && schoolChoiceApproverPageId!=null){
			init(iwc);
			int userId = iwc.getUserId();
			User user = usrBuiz.getUser(userId);
			Group rootGroup = commBuiz.getRootSchoolAdministratorGroup();
			// if user is a SchoolAdministrator
			if(user.hasRelationTo(rootGroup)){
				Collection schools = schoolBuiz.getSchoolHome().findAllBySchoolGroup(user);
				if(!schools.isEmpty()){
					Table T = new Table();
					int row = 1;
					int col = 1;
					T.add(iwrb.getLocalizedString("school_choice.links_to_choice_approvers","Links to school choice approvers"),col,row++);
					
					Iterator iter = schools.iterator();
					while(iter.hasNext()){
						School school = (School) iter.next();
						T.add(getApproverLink(school),col,row++);
					}
					add(T);
				}
				/*
				else 
				add("no schools");
				*/
			}
			/*
			else
				add("has no relation to School admin root group");
			*/
		}
		/*
		else
			add("not logged on");
		*/
	}
	
	public Link getApproverLink(School school)throws RemoteException{
		Link L = new Link(school.getName());
		L.setPage(this.schoolChoiceApproverPageId.intValue());
		L.addParameter(SchoolChoiceApprover.prmSchoolId,school.getPrimaryKey().toString());
		return L;
	}
	
	public void setSchoolChoiceApproverPage(int pageId){
		this.schoolChoiceApproverPageId = new Integer(pageId);
	}
	
	
}