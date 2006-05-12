/**
 * 
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO Dainis Describe Type SchoolChoiceBusinessHome
 * </p>
 *  Last modified: $Date: 2006/05/12 16:26:32 $ by $Author: igors $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.2.2.6 $
 */
public interface SchoolChoiceBusinessHome extends IBOHome {

	public SchoolChoiceBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
