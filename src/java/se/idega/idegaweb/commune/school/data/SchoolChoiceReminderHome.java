package se.idega.idegaweb.commune.school.data;

import com.idega.data.IDOHome;
import com.idega.user.data.Group;
import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/12/29 13:49:39 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public interface SchoolChoiceReminderHome extends IDOHome {
    SchoolChoiceReminder create() throws CreateException, RemoteException;
    SchoolChoiceReminder [] findAll () throws FinderException, RemoteException;
    SchoolChoiceReminder [] findUnhandled (Group [] groups)
        throws FinderException, RemoteException;
}
