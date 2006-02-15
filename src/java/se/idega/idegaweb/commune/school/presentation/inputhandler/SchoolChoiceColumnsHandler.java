/*
 * Created on 17.12.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation.inputhandler;

import se.idega.idegaweb.commune.school.business.SchoolReportBusiness;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;

/**
 * @author laddi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolChoiceColumnsHandler extends SchoolGroupColumnsHandler {

	public SchoolChoiceColumnsHandler() {
		super();
	}

	public SchoolChoiceColumnsHandler(String name) {
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
		addMenuElement(SchoolReportBusiness.FIELD_FROM_SCHOOL, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_FROM_SCHOOL, "From school"));
		addMenuElement(SchoolReportBusiness.FIELD_APPLICATION_DATE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_APPLICATION_DATE, "Application date"));
		addMenuElement(SchoolReportBusiness.FIELD_MESSAGE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_MESSAGE, "Message"));
		addMenuElement(SchoolReportBusiness.FIELD_EXTRA_MESSAGE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_EXTRA_MESSAGE, "Extra message"));
		addMenuElement(SchoolReportBusiness.FIELD_LANGUAGE_CHOICE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_LANGUAGE_CHOICE, "Language choice"));
		addMenuElement(SchoolReportBusiness.FIELD_NATIVE_LANGUAGE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_NATIVE_LANGUAGE, "Native language"));
		addMenuElement(SchoolReportBusiness.FIELD_CUSTODIAN, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_CUSTODIAN, "Custodian"));
		addMenuElement(SchoolReportBusiness.FIELD_ALTERNATE_ADDRESS, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_ALTERNATE_ADDRESS, "Alternate address"));
		addMenuElement(SchoolReportBusiness.FIELD_HANDICRAFT, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_HANDICRAFT, "Choice of handicraft"));
	}
}