package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import se.idega.idegaweb.commune.school.data.SchoolStatisticsData;

/**
 * Helpclass to hold and calculate school marks statistics
 * <p>
 * $Id: SchoolStatistics.java,v 1.2 2003/10/06 11:58:45 kjell Exp $
 *
 * @author <a href="mailto:kjell@lindman.com">Kjell Lindman</a>
 * @author <a href="mailto:anders.lindman@ncmedia.com">Anders Lindman</a>
 * @version $version$
 */
public class SchoolStatistics {
 
	private String scbCode;
	private String schoolName;
	SchoolMarksBusiness smBiz = null;
	SchoolStatisticsData schoolStatisticsData = null;
	String schoolManagementType = null;

	public SchoolStatistics(IWContext iwc,  String code, String name, String smt ) {
		scbCode = code;
		schoolName = name;
		schoolManagementType = smt; 
		try {
			smBiz = getSchoolMarksBusiness(iwc);
			smBiz.calculateStatistics(scbCode);
			schoolStatisticsData = smBiz.getStoredValues(scbCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getSchoolSCBCode() {
			return scbCode;
	}

	public String getSchoolName() {
			return schoolName;
	}

	public String getSchoolManagementType() {
			return schoolManagementType;
	}

	public void setSchoolSCBCode(String code) {
			scbCode = code;
	}

	public void setSchoolName(String name) {
			schoolName = name;
	}

	public void setSchoolManagementType(String smt) {
		schoolManagementType = smt;
	}

	public PercentValue getMeriteValue() throws RemoteException {
		/**
		 * MeritVärde
		 */
		return smBiz.getMeritValue(schoolStatisticsData);
	}
	
	public PercentValue getMarksPoints()  throws RemoteException {
		/**
		 * Betygspoäng
		 */
		return smBiz.getMarksPoints(schoolStatisticsData);
	}
	
	public PercentValue getAuthPoints()  throws RemoteException {
		/**
		 * Behörighetspoäng
		 */
		return smBiz.getAuthPoints(schoolStatisticsData) ;
	}

	public PercentValue getMarksNumberShare()  throws RemoteException {
		/**
		 * Antal och andel G, VG och MVG
		 */
		return smBiz.getMarksNumberShare(schoolStatisticsData);
	}
	
	public PercentValue getMarksFailed()  throws RemoteException {
		/**
		 * Antal och andel ej godkända
		 */
		return smBiz.getMarksFailed(schoolStatisticsData);
	}

	public PercentValue getMarksNoGoal()  throws RemoteException {
		/**
		 * Antal som ej uppnått mål i behörighetsgivande ämne
		 */
		return smBiz.getMarksNoGoal(schoolStatisticsData);
	}

	public SchoolMarkValues getEnglishMarks()  throws RemoteException {
		/**
		 * English
		 */
		return smBiz.getEnglishMarks(schoolStatisticsData);
	}

	public SchoolMarkValues getMathsMarks()  throws RemoteException {
		/**
		 * Math
		 */
		return smBiz.getMathsMarks(schoolStatisticsData);
	}

	public SchoolMarkValues getSwedishMarks()  throws RemoteException {
		/**
		 * Swedish
		 */
		return smBiz.getSwedishMarks(schoolStatisticsData);
	}

	public SchoolMarkValues getSwedish2Marks()  throws RemoteException {
		/**
		 * Swedish22
		 */
		return smBiz.getSwedish2Marks(schoolStatisticsData);
	}

	public SchoolMarkValues getSumAuthMarks()  throws RemoteException {
		/**
		 * Summed Auth marks
		 */
		return smBiz.getSumAuthMarks(schoolStatisticsData);
	}

	public SchoolMarkValues getTotalMarks()  throws RemoteException {
		/**
		 * Summed Auth marks
		 */
		return smBiz.getTotalMarks(schoolStatisticsData);
	}


	private SchoolMarksBusiness getSchoolMarksBusiness(IWContext iwc) throws Exception {
		return (SchoolMarksBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, SchoolMarksBusiness.class);
	}
	
}


