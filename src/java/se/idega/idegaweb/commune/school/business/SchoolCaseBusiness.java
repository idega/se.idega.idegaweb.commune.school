/*
 * Created on Jul 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.block.process.data.Case;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;


/**
 * <p>
 * TODO thomas Describe Type Choice
 * </p>
 *  Last modified: $Date: 2005/07/05 16:46:40 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolCaseBusiness {
	
	boolean isCase(Case useCase);
	
	boolean caseIsOpen(Case useCase,IWContext iwc) throws RemoteException, IBOLookupException, FinderException;
}
