package se.idega.idegaweb.commune.school.data;

import com.idega.data.*;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.*;

/**
 * Last modified: $Date: 2002/12/18 13:23:07 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
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
}
