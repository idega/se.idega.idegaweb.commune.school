/**
 * 
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO Dainis Describe Type SchoolChoiceBusinessHome
 * </p>
 *  Last modified: $Date: 2005/11/28 19:54:29 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.2.2.2 $
 */
public interface SchoolChoiceBusinessHome extends IBOHome {

	public SchoolChoiceBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
