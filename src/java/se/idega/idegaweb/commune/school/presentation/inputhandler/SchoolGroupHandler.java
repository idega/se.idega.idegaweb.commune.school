/*
 * Created on 17.12.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation.inputhandler;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClass;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.InputHandler;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.SelectionBox;

/**
 * @author laddi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolGroupHandler extends SelectionBox implements InputHandler {

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

	public void main(IWContext iwc) {
		try {
			Collection groups = getSchoolBusiness(iwc).findSchoolClassesBySchoolAndSeasonAndYears(getSchoolSession(iwc).getSchoolID(), getSchoolSession(iwc).getSchoolSeasonID(), getSchoolSession(iwc).getSchoolGroupIDs(), false);
		
			Iterator iter = groups.iterator();
			while (iter.hasNext()) {
				SchoolClass group = (SchoolClass) iter.next();
				addMenuElement(group.getPrimaryKey().toString(), group.getSchoolClassName());
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
		
	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		this.setName(name);
		if (value != null) {
			this.setContent(value);
		}
		this.setStyleClass("commune_Interface");
		this.setAsNotEmpty(iwrb.getLocalizedString("school_report.must_select_group", "You have to select at least one group."));
		return this;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getResultingObject(java.lang.String[], com.idega.presentation.IWContext)
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		Collection groups = null;
		int count = values.length;
		if (values != null && count > 0) {
			groups = new ArrayList();
			
			for(int i=0; i<values.length; i++) {
				SchoolClass group = getSchoolBusiness(iwc).findSchoolClass(values[i]);
				groups.add(group);
			}
		}

		return groups;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.Object, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		if (value != null) {
			StringBuffer buffer = new StringBuffer();
			Iterator iter = ((Collection)value).iterator();
			while (iter.hasNext()) {
				SchoolClass element = (SchoolClass) iter.next();
				buffer.append(element.getSchoolClassName());
				if (iter.hasNext()) {
					buffer.append(", ");
				}
			}
			return buffer.toString();
		}
		return null;
	}
	
	private SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	private SchoolCommuneSession getSchoolSession(IWUserContext iwuc) {
		try {
			return (SchoolCommuneSession) IBOLookup.getSessionInstance(iwuc, SchoolCommuneSession.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public PresentationObject getHandlerObject(String name, Collection values, IWContext iwc) {
		String value = (String) Collections.min(values);
		return getHandlerObject(name, value, iwc);
	}


	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#convertResultingObjectToType(java.lang.Object, java.lang.String)
	 */
	public Object convertSingleResultingObjectToType(Object value, String className) {
		return value;
	}
}