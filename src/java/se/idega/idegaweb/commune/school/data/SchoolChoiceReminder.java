package se.idega.idegaweb.commune.school.data;

import com.idega.data.IDOEntity;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Last modified: $Date: 2002/12/17 07:01:22 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolChoiceReminder extends IDOEntity {
    void setText (String text) throws RemoteException;
    void setEventDate (Date eventDate) throws RemoteException;
    void setReminderDate (Date reminderDate) throws RemoteException;
}
