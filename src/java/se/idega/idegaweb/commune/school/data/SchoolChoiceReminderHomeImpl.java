package se.idega.idegaweb.commune.school.data;

import com.idega.data.IDOFactory;
import java.rmi.RemoteException;
import javax.ejb.CreateException;

/**
 * Last modified: $Date: 2002/12/17 07:01:22 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public class SchoolChoiceReminderHomeImpl extends IDOFactory
    implements SchoolChoiceReminderHome {
    public SchoolChoiceReminder create () throws CreateException{
        return (SchoolChoiceReminder) createIDO ();
    }

    protected Class getEntityInterfaceClass() {
        return SchoolChoiceReminder.class;
    }
}
