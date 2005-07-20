/*
 * Created on 2005-jul-13
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.business;





import com.idega.business.IBOHomeImpl;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolCommuneBusinessHomeImpl extends IBOHomeImpl implements
		SchoolCommuneBusinessHome {
	protected Class getBeanInterfaceClass() {
		return SchoolCommuneBusiness.class;
	}

	public SchoolCommuneBusiness create() throws javax.ejb.CreateException {
		return (SchoolCommuneBusiness) super.createIBO();
	}

}
