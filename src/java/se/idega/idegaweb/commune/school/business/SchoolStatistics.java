package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import se.idega.idegaweb.commune.school.data.SchoolStatisticsData;

/**
 * Helpclass to hold and calculate school marks statistics
 * <p>
 * $Id: SchoolStatistics.java,v 1.4 2006/04/09 11:39:53 laddi Exp $
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
		this.scbCode = code;
		this.schoolName = name;
		this.schoolManagementType = smt; 
		try {
			this.smBiz = getSchoolMarksBusiness(iwc);
			this.smBiz.calculateStatistics(this.scbCode);
			this.schoolStatisticsData = this.smBiz.getStoredValues(this.scbCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public String getSchoolSCBCode() {
			return this.scbCode;
	}

	public String getSchoolName() {
			return this.schoolName;
	}

	public String getSchoolManagementType() {
			return this.schoolManagementType;
	}

	public void setSchoolSCBCode(String code) {
			this.scbCode = code;
	}

	public void setSchoolName(String name) {
			this.schoolName = name;
	}

	public void setSchoolManagementType(String smt) {
		this.schoolManagementType = smt;
	}

	public PercentValue getMeriteValue() throws RemoteException {
		/**
		 * MeritVärde
		 */
		return this.smBiz.getMeritValue(this.schoolStatisticsData);
	}
	
	public PercentValue getMarksPoints()  throws RemoteException {
		/**
		 * Betygspoäng
		 */
		return this.smBiz.getMarksPoints(this.schoolStatisticsData);
	}
	
	public PercentValue getAuthPoints()  throws RemoteException {
		/**
		 * Behörighetspoäng
		 */
		return this.smBiz.getAuthPoints(this.schoolStatisticsData) ;
	}

	public PercentValue getMarksNumberShare()  throws RemoteException {
		/**
		 * Antal och andel G, VG och MVG
		 */
		return this.smBiz.getMarksNumberShare(this.schoolStatisticsData);
	}
	
	public PercentValue getMarksFailed()  throws RemoteException {
		/**
		 * Antal och andel ej godkända
		 */
		return this.smBiz.getMarksFailed(this.schoolStatisticsData);
	}

	public PercentValue getMarksNoGoal()  throws RemoteException {
		/**
		 * Antal som ej uppnått mål i behörighetsgivande ämne
		 */
		return this.smBiz.getMarksNoGoal(this.schoolStatisticsData);
	}

	public SchoolMarkValues getEnglishMarks()  throws RemoteException {
		/**
		 * English
		 */
		return this.smBiz.getEnglishMarks(this.schoolStatisticsData);
	}

	public SchoolMarkValues getMathsMarks()  throws RemoteException {
		/**
		 * Math
		 */
		return this.smBiz.getMathsMarks(this.schoolStatisticsData);
	}

	public SchoolMarkValues getSwedishMarks()  throws RemoteException {
		/**
		 * Swedish
		 */
		return this.smBiz.getSwedishMarks(this.schoolStatisticsData);
	}

	public SchoolMarkValues getSwedish2Marks()  throws RemoteException {
		/**
		 * Swedish22
		 */
		return this.smBiz.getSwedish2Marks(this.schoolStatisticsData);
	}

	public SchoolMarkValues getSumAuthMarks()  throws RemoteException {
		/**
		 * Summed Auth marks
		 */
		return this.smBiz.getSumAuthMarks(this.schoolStatisticsData);
	}

	public SchoolMarkValues getTotalMarks()  throws RemoteException {
		/**
		 * Summed Auth marks
		 */
		return this.smBiz.getTotalMarks(this.schoolStatisticsData);
	}

	public String getTotalAuthPoints()  {
		/**
		 * Summed Total Auth Point
		 */
		return this.schoolStatisticsData.getTotalAuthPoints(); 
	}

	public String getTotalAuthStudents()  {
		/**
		 * Summed Total Auth Point
		 */
		return this.schoolStatisticsData.getTotalAuthStudents(); 
	}


	private SchoolMarksBusiness getSchoolMarksBusiness(IWContext iwc) throws Exception {
		return (SchoolMarksBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, SchoolMarksBusiness.class);
	}
	
}


