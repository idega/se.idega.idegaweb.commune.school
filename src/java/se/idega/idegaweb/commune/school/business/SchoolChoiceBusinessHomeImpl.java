/**
 * 
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO Dainis Describe Type SchoolChoiceBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2006/04/27 09:41:19 $ by $Author: igors $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.2.2.5 $
 */
public class SchoolChoiceBusinessHomeImpl extends IBOHomeImpl implements SchoolChoiceBusinessHome {

	protected Class getBeanInterfaceClass() {
		return SchoolChoiceBusiness.class;
	}

	public SchoolChoiceBusiness create() throws javax.ejb.CreateException {
		return (SchoolChoiceBusiness) super.createIBO();
	}
}
