package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.school.business.SchoolUserBusinessBean;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.User;

/**
 * @author gimmi
 */
public class SchoolUserEditor extends com.idega.block.school.presentation.SchoolUserEditor {

	private final String SCHOOL_ADMINISTATION_GROUP_PARAMETER_NAME = "school.anordnar_skolar_group_id";
	private final String CHILDCARE_ADMINISTATION_GROUP_PARAMETER_NAME = "school.anordnar_barnomsorg_group_id";

	private GroupBusiness groupBusiness = null;
	
	private static String CATEGORY_SCHOOL = "SCHOOL";
	private static String CATEGORY_CHILDCARE = "CHILDCARE";

	public SchoolUserEditor() {
		super();	
	}

	public SchoolUserEditor(IWContext iwc) throws RemoteException{
		super(iwc);
		groupBusiness = (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
	}
	
	public String getBundleIdentifier(){
		return CommuneBlock.IW_BUNDLE_IDENTIFIER;
	}	

	protected void postSaveNew(School school, User user, int userType) throws RemoteException {
		int groupId = -1;
		try {
			Collection coll = school.getSchoolTypes();
			
			if (coll != null && !coll.isEmpty()) {
				SchoolType schoolType;
				SchoolTypeHome stHome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
				String category;
				
				Iterator iter = coll.iterator();
				while (iter.hasNext()) {
					try {
						schoolType = (SchoolType) iter.next();
						category = schoolType.getSchoolCategory();
						groupId = -1;
						if (category != null && category.equals(CATEGORY_SCHOOL)) {
							groupId = getSchoolAdminGroupId();
						}else if (category != null && category.equals(CATEGORY_CHILDCARE)) {
							groupId = getChildCareAdminGroupId();	
						}
						if (userType != SchoolUserBusinessBean.USER_TYPE_TEACHER && groupId > 0) {
							groupBusiness.addUser(groupId, user);
						} 
					} catch (FinderException e) {
						e.printStackTrace(System.err);
					}
				}
			} 
		} catch (IDORelationshipException e) {
			e.printStackTrace(System.err);
		}
		
	}
	
	protected void postSaveUpdate(School school, User user, int userType) throws RemoteException {
		postSaveNew(school, user, userType);
	}
	
	private int getSchoolAdminGroupId() {
		String groupId = getCommuneBundle().getProperty(SCHOOL_ADMINISTATION_GROUP_PARAMETER_NAME);
		if (groupId != null) {
				return Integer.parseInt(groupId);
		}	
		return -1;
	}
	
	private int getChildCareAdminGroupId() {
			String groupId = getCommuneBundle().getProperty(CHILDCARE_ADMINISTATION_GROUP_PARAMETER_NAME);
			if (groupId != null) {
					return Integer.parseInt(groupId);
			}	
			return -1;
		}

	protected IWBundle getCommuneBundle() {
		return this.getIWApplicationContext().getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
	}
	
	public void main(IWContext iwc) throws RemoteException {
		groupBusiness = (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
		
		CATEGORY_SCHOOL = getSchoolUserBusiness(iwc).getSchoolBusiness().getElementarySchoolSchoolCategory();
		CATEGORY_CHILDCARE = getSchoolUserBusiness(iwc).getSchoolBusiness().getChildCareSchoolCategory();
		
		super.main(iwc);	
	}
	
}

