/**
 * 
 */
package se.idega.idegaweb.commune.school.business;





import com.idega.business.IBOHomeImpl;

/**
 * @author Dainis
 *
 */
public class SchoolChoiceBusinessHomeImpl extends IBOHomeImpl implements
		SchoolChoiceBusinessHome {
	protected Class getBeanInterfaceClass() {
		return SchoolChoiceBusiness.class;
	}

	public SchoolChoiceBusiness create() throws javax.ejb.CreateException {
		return (SchoolChoiceBusiness) super.createIBO();
	}

}
