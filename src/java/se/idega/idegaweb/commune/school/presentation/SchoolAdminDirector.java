package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

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
	SchoolBusiness schoolBuiz;
	Integer schoolChoiceApproverPageId = null;
	IWResourceBundle iwrb;
	
	
	public String getBundleIdentifier(){
		return CommuneBlock.IW_BUNDLE_IDENTIFIER;
	}
	

	private void init(IWContext iwc) throws RemoteException{
		usrBuiz =(UserBusiness) IBOLookup.getServiceInstance(iwc,UserBusiness.class);
		grpBuiz = (GroupBusiness) IBOLookup.getServiceInstance(iwc,GroupBusiness.class);
		schoolBuiz = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
		
	}

	public void main(IWContext iwc) throws Exception{
		iwrb = getResourceBundle(iwc);
		
		if(iwc.isLoggedOn() && schoolChoiceApproverPageId!=null){
			init(iwc);
			int userId = iwc.getUserId();
			User user = usrBuiz.getUser(userId);
			Group rootGroup = schoolBuiz.getRootSchoolAdministratorGroup();
			// if user is a SchoolAdministrator
			if(user.hasRelationTo(rootGroup)){
				Collection schools = schoolBuiz.getSchoolHome().findAllBySchoolGroup(user);
				if(!schools.isEmpty()){
					Table T = new Table();
					int row = 1;
					int col = 1;
					T.add(new Text(iwrb.getLocalizedString("school_choice.links_to_choice_approvers","Links to school choice approvers")),col,row++);
					
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
	
	public Link getApproverLink(School school) {
		Link L = new Link(school.getName());
		L.setPage(this.schoolChoiceApproverPageId.intValue());
		L.addParameter(SchoolChoiceApprover.prmSchoolId,school.getPrimaryKey().toString());
		return L;
	}
	
	public void setSchoolChoiceApproverPage(int pageId){
		this.schoolChoiceApproverPageId = new Integer(pageId);
	}
	
	
}
