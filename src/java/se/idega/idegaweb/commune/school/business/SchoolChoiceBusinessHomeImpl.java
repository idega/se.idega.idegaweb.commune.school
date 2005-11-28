/**
 * 
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO Dainis Describe Type SchoolChoiceBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2005/11/28 19:54:29 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.2.2.2 $
 */
public class SchoolChoiceBusinessHomeImpl extends IBOHomeImpl implements SchoolChoiceBusinessHome {

	protected Class getBeanInterfaceClass() {
		return SchoolChoiceBusiness.class;
	}

	public SchoolChoiceBusiness create() throws javax.ejb.CreateException {
		return (SchoolChoiceBusiness) super.createIBO();
	}
}
