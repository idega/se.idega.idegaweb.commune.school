/*
 * Created on 2003-okt-08
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.presentation.IWContext;

/**
 * @author Göran Borgman
 *
 * Business object with helper methods for CentralPlacingEditor
 */
public class CentralPlacementBusinessBean extends IBOServiceBean 
																					implements CentralPlacementBusiness {
	/**
	 * Stores a new placement(SchoolClassMember) with resources
	 *
	 */
	public SchoolClassMember storeSchoolClassMember(IWContext iwc, int studentID, 
										int schoolClassID, Timestamp registerDate, int registrator, String notes)
																											 throws RemoteException{		
		UserTransaction trans = getSessionContext().getUserTransaction();
		SchoolClassMember member = null;
		try {
			trans.begin();
			// TODO Store placement

			member = getSchoolBusiness(iwc).storeSchoolClassMember(studentID, schoolClassID, 
																									registerDate, registrator, notes);
																									
			trans.commit();
		} catch (Exception e) {
			try {
				trans.rollback();
				e.printStackTrace();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}

		}									
											
		// int studentID, int schoolClassID, Timestamp registerDate, int registrator, String notes
		return member; 
		
	}
		
	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}

}
