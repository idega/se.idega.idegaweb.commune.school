/*
 * Created on 17.12.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation.inputhandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import se.idega.idegaweb.commune.school.business.SchoolReportBusiness;

import com.idega.business.HiddenInputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.SelectionBox;

/**
 * @author laddi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolGroupColumnsHandler extends SelectionBox implements HiddenInputHandler {

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private static final String PREFIX = SchoolReportBusiness.PREFIX;

	public SchoolGroupColumnsHandler() {
		super();
	}

	public SchoolGroupColumnsHandler(String name) {
		super(name);
	}
	
	public void main(IWContext iwc) {
		IWResourceBundle iwrb = this.getResourceBundle(iwc);

		addMenuElement(SchoolReportBusiness.FIELD_PERSONAL_ID, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_PERSONAL_ID, "Personal ID"));
		addMenuElement(SchoolReportBusiness.FIELD_NAME, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_NAME, "Name"));
		addMenuElement(SchoolReportBusiness.FIELD_ADDRESS, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_ADDRESS, "Address"));
		addMenuElement(SchoolReportBusiness.FIELD_ZIP_CODE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_ZIP_CODE, "Zip code"));
		addMenuElement(SchoolReportBusiness.FIELD_AREA, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_AREA, "Area"));
		addMenuElement(SchoolReportBusiness.FIELD_EMAIL, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_EMAIL, "E-mail"));
		addMenuElement(SchoolReportBusiness.FIELD_PHONE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_PHONE, "Phone"));
		addMenuElement(SchoolReportBusiness.FIELD_GENDER, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_GENDER, "Gender"));
		addMenuElement(SchoolReportBusiness.FIELD_NATIVE_LANGUAGE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_NATIVE_LANGUAGE, "Language"));
		addMenuElement(SchoolReportBusiness.FIELD_LANGUAGE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_LANGUAGE, "Language"));
		addMenuElement(SchoolReportBusiness.FIELD_SWEDISH_AS_SECOND_LANGUAGE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_SWEDISH_AS_SECOND_LANGUAGE, "Swedish as second language"));
		addMenuElement(SchoolReportBusiness.FIELD_TERMINATION_DATE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_TERMINATION_DATE, "Termination date"));
		addMenuElement(SchoolReportBusiness.FIELD_CUSTODIAN, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_CUSTODIAN, "Custodian"));
		addMenuElement(SchoolReportBusiness.FIELD_ALTERNATE_ADDRESS, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_ALTERNATE_ADDRESS, "Alternate address"));
		addMenuElement(SchoolReportBusiness.FIELD_YEARS_WITH_LANGUAGE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_YEARS_WITH_LANGUAGE, "Years with language"));
	}
	
	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		this.setName(name);
		if (value != null) {
			this.setContent(value);
		}
		this.setStyleClass("commune_Interface");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getResultingObject(java.lang.String[], com.idega.presentation.IWContext)
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		Collection names = null;
		int count = values.length;
		if (values != null && count > 0) {
			names = new ArrayList();
			
			for(int i=0; i<values.length; i++) {
				names.add(values[i]);
			}
		}

		return names;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.Object, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		if (value != null) {
			StringBuffer buffer = new StringBuffer();
			Iterator iter = ((Collection)value).iterator();
			while (iter.hasNext()) {
				String element = (String) iter.next();
				buffer.append(element);
				if (iter.hasNext()) {
					buffer.append(", ");
				}
			}
			return buffer.toString();
		}
		return null;
	}
	
	public String getLocalizedString(IWResourceBundle iwrb, String key, String defaultValue) {
		return iwrb.getLocalizedString(PREFIX + key, defaultValue);
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