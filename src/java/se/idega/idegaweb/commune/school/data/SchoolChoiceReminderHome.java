package se.idega.idegaweb.commune.school.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/12/18 13:23:07 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface SchoolChoiceReminderHome extends IDOHome {
    SchoolChoiceReminder create() throws CreateException, RemoteException;
    SchoolChoiceReminder [] findAll () throws FinderException, RemoteException;
}
