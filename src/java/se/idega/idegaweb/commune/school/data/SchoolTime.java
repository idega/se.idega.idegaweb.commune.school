package se.idega.idegaweb.commune.school.data;

import com.idega.block.school.data.*;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Last modified: $Date: 2003/03/28 11:10:06 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 * @see se.idega.idegaweb.commune.block.importer.business.NackaStudentTimeImportFileHandler
 */
public interface SchoolTime extends IDOEntity {
	public void setUser (User user) throws RemoteException;
    public void setSchool (School school) throws RemoteException;
    public void setHours (int hours) throws RemoteException;
    public void setSeason (SchoolSeason season) throws RemoteException;
    public void setRegistrationTime (Date date) throws RemoteException;
}
