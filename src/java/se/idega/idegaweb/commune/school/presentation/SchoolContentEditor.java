package se.idega.idegaweb.commune.school.presentation;

import com.idega.block.school.data.School;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;

import java.rmi.RemoteException;
/**
 * @author gimmi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolContentEditor
	extends com.idega.block.school.presentation.SchoolContentEditor {

	protected com.idega.block.school.presentation.SchoolUserEditor getSchoolUserEditor(IWContext iwc) throws RemoteException {
		return new SchoolUserEditor(iwc);
	}

	public static Link getLink(School school, PresentationObject po) throws RemoteException {
		Link link = new Link(po);
		link.setWindowToOpen( SchoolContentEditor.class);
		link.addParameter(PARAMETER_SCHOOL_ID, school.getPrimaryKey().toString() );
		return link;
	}

	public static Link getLink(School school, String string) throws RemoteException {
		Link link = new Link(string);
		link.setWindowToOpen( SchoolContentEditor.class);
		link.addParameter(PARAMETER_SCHOOL_ID, school.getPrimaryKey().toString() );
		return link;
	}
}
