/*
 * Created on 17.12.2003
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.util.Collection;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.business.IBOSession;

/**
 * @author laddi
 */
public interface SchoolReportBusiness extends IBOSession {

	public final static String PREFIX = "school_report.";
	
	public static final String FIELD_PERSONAL_ID = "personal_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_ADDRESS = "address";
	public static final String FIELD_ZIP_CODE = "zip_code";
	public static final String FIELD_AREA = "area";
	public static final String FIELD_EMAIL = "email";
	public static final String FIELD_PHONE = "phone";
	public static final String FIELD_GENDER = "gender";
	public static final String FIELD_LANGUAGE = "language";
	public static final String FIELD_CUSTODIAN = "custodian";
	public static final String FIELD_ALTERNATE_ADDRESS = "alternate_address";
	public static final String FIELD_YEARS_WITH_LANGUAGE = "years_with_language";
	public static final String FIELD_FROM_SCHOOL = "from_school";
	public static final String FIELD_APPLICATION_DATE = "application_date";
	public static final String FIELD_MESSAGE = "message";
	public static final String FIELD_LANGUAGE_CHOICE = "language_choice";
	
	public ReportableCollection getGroupReport(Collection schoolGroups, Collection columnNames, String freeText) throws RemoteException;
	public ReportableCollection getChoicesReport(Collection columnNames) throws RemoteException;
	
}