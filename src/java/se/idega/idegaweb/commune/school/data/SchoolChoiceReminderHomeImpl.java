package se.idega.idegaweb.commune.school.data;

import com.idega.data.*;
import com.idega.user.data.Group;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/12/29 13:49:39 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 */
public class SchoolChoiceReminderHomeImpl extends IDOFactory
    implements SchoolChoiceReminderHome {
    public SchoolChoiceReminder create () throws CreateException{
        return (SchoolChoiceReminder) createIDO ();
    }

    protected Class getEntityInterfaceClass() {
        return SchoolChoiceReminder.class;
    }

    public SchoolChoiceReminder [] findAll () throws FinderException,
                                                     RemoteException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids
                = ((SchoolChoiceReminderBMPBean)entity).ejbFindAll ();
        idoCheckInPooledEntity (entity);
        return (SchoolChoiceReminder []) getEntityCollectionForPrimaryKeys(ids)
                .toArray (new SchoolChoiceReminder [0]);
    }

    public SchoolChoiceReminder [] findUnhandled (final Group [] groups)
        throws FinderException, RemoteException {
        final IDOEntity entity = idoCheckOutPooledEntity();
        final Collection ids
                = ((SchoolChoiceReminderBMPBean)entity).ejbFindUnhandled
                (groups);
        idoCheckInPooledEntity (entity);
        final Collection objs = getEntityCollectionForPrimaryKeys(ids);
        final SchoolChoiceReminder [] reminders
                = (SchoolChoiceReminder []) objs.toArray
                (new SchoolChoiceReminder [objs.size ()]);
        return reminders;
    }
}
