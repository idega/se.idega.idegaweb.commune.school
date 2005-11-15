/**
 * 
 */
package se.idega.idegaweb.commune.school.business;





import com.idega.business.IBOHome;

/**
 * @author Dainis
 *
 */
public interface SchoolChoiceBusinessHome extends IBOHome {
	public SchoolChoiceBusiness create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
