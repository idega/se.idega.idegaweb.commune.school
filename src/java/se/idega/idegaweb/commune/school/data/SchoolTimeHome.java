package se.idega.idegaweb.commune.school.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import javax.ejb.CreateException;

/**
 * Last modified: $Date: 2003/03/28 11:10:06 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolTimeHome extends IDOHome {
    SchoolTime create() throws CreateException, RemoteException;
}
