package se.idega.idegaweb.commune.school.data;

import com.idega.block.process.data.Case;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Last modified: $Date: 2002/12/18 13:23:07 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan N�teberg</a>
 * @version $Revision: 1.3 $
 */
public interface SchoolChoiceReminder extends IDOEntity, Case {
    String CASE_CODE_KEY = "SCREMIN";
    
    String getText () throws RemoteException;
    Date getEventDate () throws RemoteException;
    Date getReminderDate () throws RemoteException;
    int getUserId () throws RemoteException;
    
    void setText (String text) throws RemoteException;
    void setEventDate (Date eventDate) throws RemoteException;
    void setReminderDate (Date reminderDate) throws RemoteException;
    void setUser (User user) throws RemoteException;
}