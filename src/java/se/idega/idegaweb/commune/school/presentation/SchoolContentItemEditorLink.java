/**
 * Created on 23.1.2003
 *
 * This class does something very clever.
 */
package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.presentation.SchoolContentItem;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;

/**
 * @author laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolContentItemEditorLink extends SchoolContentItem {

	/**
	 * @see com.idega.block.school.presentation.SchoolContentItem#getObject()
	 */
	protected PresentationObject getObject() throws RemoteException {
		Link link = SchoolContentEditor.getLink(_school, _iwrb.getLocalizedString("school_content_editor","School Content Editor"));

		if (super.hasEditPermission()) {
			return link;
		}else if (super.getSchoolBusiness(_iwc).hasEditPermission(_iwc.getCurrentUser(), _school)) {
			return link;
		}
		return null;
	}

	protected School getSchool(IWContext iwc) throws RemoteException {
		int schoolID = getSchoolCommuneSession(iwc).getSchoolID();
		if (schoolID != -1) {
			try {
				return getSchoolBusiness(iwc).getSchoolHome().findByPrimaryKey(new Integer(schoolID));
			}
			catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	protected SchoolCommuneSession getSchoolCommuneSession(IWContext iwc) throws RemoteException {
		return (SchoolCommuneSession) IBOLookup.getSessionInstance(iwc, SchoolCommuneSession.class);	
	}

	protected SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException{
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}
}