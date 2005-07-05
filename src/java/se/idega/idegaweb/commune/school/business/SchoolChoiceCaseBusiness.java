/*
 * Created on Jul 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import com.idega.block.process.data.Case;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;


/**
 * <p>
 * TODO thomas Describe Type SchoolChoiceCaseBusiness
 * </p>
 *  Last modified: $Date: 2005/07/05 16:46:40 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public class SchoolChoiceCaseBusiness implements SchoolCaseBusiness {

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.business.SchoolCaseBusiness#hasCaseCode(java.lang.String)
	 */
	public boolean isCase(Case useCase) {
		return SchoolConstants.SCHOOL_CHOICE_CASE_CODE_KEY.equals(useCase.getCaseCode());
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.business.SchoolCaseBusiness#caseIsOpen(com.idega.block.process.data.Case)
	 */
	public boolean caseIsOpen(Case useCase, IWContext iwc) throws RemoteException, IBOLookupException, FinderException {
		SchoolChoiceBusiness schBuiz;
		schBuiz = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
		SchoolChoice choice = schBuiz.getSchoolChoice(((Integer) useCase.getPrimaryKey()).intValue());
		return (choice != null && !choice.getHasReceivedPlacementMessage());
	}
}
