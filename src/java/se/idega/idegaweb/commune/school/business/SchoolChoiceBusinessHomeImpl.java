/**
 * 
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO Dainis Describe Type SchoolChoiceBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2005/11/27 17:38:41 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.4 $
 */
public class SchoolChoiceBusinessHomeImpl extends IBOHomeImpl implements SchoolChoiceBusinessHome {

	protected Class getBeanInterfaceClass() {
		return SchoolChoiceBusiness.class;
	}

	public SchoolChoiceBusiness create() throws javax.ejb.CreateException {
		return (SchoolChoiceBusiness) super.createIBO();
	}
}
