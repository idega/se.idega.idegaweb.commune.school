package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;

/**
 * @author Laddi
 */
public class SchoolNameText extends Text {

	/**
	 * Creates a new <code>SchoolNameText</code>.
	 */
	public SchoolNameText() {
		super();
	}
	
	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		int schoolID = getSchoolCommuneSession(iwc).getSchoolID();
		if (schoolID != -1) {
			String schoolName = getSchoolBusiness(iwc).getSchool(new Integer(schoolID)).getSchoolName();	
			setText(schoolName);
		}
		else {
			setText("");
		}
	}
	
	private SchoolCommuneSession getSchoolCommuneSession(IWContext iwc) throws RemoteException {
		return (SchoolCommuneSession) IBOLookup.getSessionInstance(iwc, SchoolCommuneSession.class);
	}

	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}
}