package se.idega.idegaweb.commune.school.data;

import com.idega.data.IDOFactory;
import javax.ejb.CreateException;

/**
 * Last modified: $Date: 2003/03/28 11:10:06 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public class SchoolTimeHomeImpl extends IDOFactory implements SchoolTimeHome {

    public SchoolTime create () throws CreateException{
        return (SchoolTime) createIDO ();
    }

    protected Class getEntityInterfaceClass() {
        return SchoolTime.class;
    }
}
