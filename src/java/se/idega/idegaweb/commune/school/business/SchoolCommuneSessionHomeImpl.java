/*
 * Created on 2005-maj-18
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
public class SchoolCommuneSessionHomeImpl extends IBOHomeImpl implements
		SchoolCommuneSessionHome {
	protected Class getBeanInterfaceClass() {
		return SchoolCommuneSession.class;
	}

	public SchoolCommuneSession create() throws javax.ejb.CreateException {
		return (SchoolCommuneSession) super.createIBO();
	}

}
