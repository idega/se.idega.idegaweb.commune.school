/*
 * Created on 28.5.2004
 */
package se.idega.idegaweb.commune.school.presentation.inputhandler;

import se.idega.idegaweb.commune.school.business.SchoolReportBusiness;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;


/**
 * @author laddi
 */
public class SchoolGroupColumnsHandlerAlternate extends SchoolGroupColumnsHandler {

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
		addMenuElement(SchoolReportBusiness.FIELD_TERMINATION_DATE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_TERMINATION_DATE, "Termination date"));
		addMenuElement(SchoolReportBusiness.FIELD_CUSTODIAN, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_CUSTODIAN, "Custodian"));
		addMenuElement(SchoolReportBusiness.FIELD_ALTERNATE_ADDRESS, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_ALTERNATE_ADDRESS, "Alternate address"));
		addMenuElement(SchoolReportBusiness.FIELD_YEARS_WITH_LANGUAGE, getLocalizedString(iwrb, SchoolReportBusiness.FIELD_YEARS_WITH_LANGUAGE, "Years with language"));
	}
}