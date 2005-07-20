package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolCommuneSessionBean extends IBOSessionBean implements SchoolCommuneSession {

	public static final String PARAMETER_SCHOOL_ID = "sch_s_id";
	protected static final String PARAMETER_SCHOOL_YEAR_ID = "sch_s_y_id";
	protected static final String PARAMETER_SCHOOL_SEASON_ID = "sch_s_s_id";
	protected static final String PARAMETER_SCHOOL_CLASS_ID = "sch_s_c_id";
	protected static final String PARAMETER_STUDENT_ID = "sch_st_id";
	protected static final String PARAMETER_STUDENT__UNIQUE_ID = "sch_st_uq_id";
	protected static final String PARAMETER_GROUP_IDS = "sch_group_ids";
	protected static final String PARAMETER_STUDY_PATH_ID = "sch_study_path_ids";
	
	protected int _schoolID = -1;
	protected School _school = null;
	protected int _schoolYearID = -1;
	protected int _schoolSeasonID = -1;
	protected int _schoolClassID = -1;
	protected int _studentID = -1;
	private String _studentUniqueID = null;
	private int _userID = -1;
	private int _studyPathID = -1;
	
	private String[] _schoolGroupIDs = null;

	public CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CommuneUserBusiness.class);
	}
	
	public SchoolCommuneBusiness getSchoolCommuneBusiness() throws RemoteException {
		return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolCommuneBusiness.class);
	}
	
	/**
	 * Returns the schoolID.
	 * @return int
	 */
	public int getSchoolID() throws RemoteException {
		if (getUserContext().isLoggedOn()) {
			User user = getUserContext().getCurrentUser();
			int userID = ((Integer)user.getPrimaryKey()).intValue();
			
			if (_userID == userID) {
				if (_schoolID != -1) {
					return _schoolID;
				}
				else {
					return getSchoolIDFromUser(user);
				}
			}
			else {
				_userID = userID;
				_schoolSeasonID = getSchoolCommuneBusiness().getCurrentSchoolSeasonID();
				_schoolYearID = -1;
				_schoolClassID = -1;
				return getSchoolIDFromUser(user);
			}
		}
		else {
			return -1;	
		}
	}
	
	private int getSchoolIDFromUser(User user) throws RemoteException {
		_schoolID = -1;
		if (user != null) {
			try {
				School school = getCommuneUserBusiness().getFirstManagingSchoolForUser(user);
				if (school != null) {
					_school = school;
					_schoolID = ((Integer) school.getPrimaryKey()).intValue();
				}
			}
			catch (FinderException fe) {
				_school = null;
				_schoolID = -1;
			}
		}
		return _schoolID;
	}

	/**
	 * Returns the schoolClassID.
	 * @return int
	 */
	public int getSchoolClassID() {
		return _schoolClassID;
	}

	/**
	 * Returns the schoolSeasonID.
	 * @return int
	 */
	public int getSchoolSeasonID() {
		return _schoolSeasonID;
	}

	/**
	 * Returns the schoolYearID.
	 * @return int
	 */
	public int getSchoolYearID() {
		return _schoolYearID;
	}
	
	/**
	 * Returns the studyPathID.
	 * @return int
	 */
	public int getStudyPathID() {
		return _studyPathID;
	}
	
	/**
	 * Sets the studyPathID.
	 * @param studyPathID The studyPathID to set
	 */
	public void setStudyPathID(int studyPathID) {
		_studyPathID = studyPathID;
	}


	/**
	 * Sets the schoolClassID.
	 * @param schoolClassID The schoolClassID to set
	 */
	public void setSchoolClassID(int schoolClassID) {
		_schoolClassID = schoolClassID;
	}

	/**
	 * Sets the schoolID.
	 * @param schoolID The schoolID to set
	 */
	public void setSchoolID(int schoolID) {
		//Fix for bug #nacp81  Roar 02.09.03
		if (_schoolID != schoolID){
			setSchoolClassID(-1);
		}
		//end fix
		_schoolID = schoolID;
	}

	/**
	 * Sets the schoolSeasonID.
	 * @param schoolSeasonID The schoolSeasonID to set
	 */
	public void setSchoolSeasonID(int schoolSeasonID) {
		_schoolSeasonID = schoolSeasonID;
	}

	/**
	 * Sets the schoolYearID.
	 * @param schoolYearID The schoolYearID to set
	 */
	public void setSchoolYearID(int schoolYearID) {
		_schoolYearID = schoolYearID;
	}

	/**
	 * Returns the SchoolID parameter.
	 * @return String
	 */
	public String getParameterStudyPathID() {
		return PARAMETER_STUDY_PATH_ID;
	}
	
	/**
	 * Returns the SchoolID parameter.
	 * @return String
	 */
	public String getParameterSchoolID() {
		return PARAMETER_SCHOOL_ID;
	}

	/**
	 * Returns the SchoolClassID parameter.
	 * @return String
	 */
	public String getParameterSchoolClassID() {
		return PARAMETER_SCHOOL_CLASS_ID;
	}

	/**
	 * Returns the SchoolSeasonID parameter.
	 * @return String
	 */
	public String getParameterSchoolSeasonID() {
		return PARAMETER_SCHOOL_SEASON_ID;
	}

	/**
	 * Returns the SchoolYearID parameter.
	 * @return String
	 */
	public String getParameterSchoolYearID() {
		return PARAMETER_SCHOOL_YEAR_ID;
	}

	/**
	 * Returns the StudentID parameter.
	 * @return String
	 */
	public String getParameterStudentID() {
		return PARAMETER_STUDENT_ID;
	}
	
	/**
	 * Returns the StudentUniqueID parameter.
	 * @return String
	 */
	public String getParameterStudentUniqueID() {
		return PARAMETER_STUDENT__UNIQUE_ID;
	}
	
	/**
	 * Returns the GroupIDs parameter.
	 * @return String
	 */
	public String getParameterSchoolGroupIDs() {
		return PARAMETER_GROUP_IDS;
	}
	
	/**
	 * @return Returns the studentID.
	 */
	public int getStudentID() {
		return this._studentID;
	}
	
	/**
	 * @return Returns the studentUniqueID.
	 */
	public String getStudentUniqueID() {
		return this._studentUniqueID;
	}
	
	/**
	 * @param studentID The studentID to set.
	 */
	public void setStudentID(int studentID) {
		this._studentID = studentID;
	}
	
	/**
	 * @param studentUniqueID The studentID to set.
	 */
	public void setStudentUniqueID(String studentUniqueID) {
		this._studentUniqueID = studentUniqueID;
	}
	
	/**
	 * @return Returns the schoolGroupIDs.
	 */
	public String[] getSchoolGroupIDs() {
		return this._schoolGroupIDs;
	}
	
	/**
	 * @param schoolGroupIDs The schoolGroupIDs to set.
	 */
	public void setSchoolGroupIDs(String[] schoolGroupIDs) {
		this._schoolGroupIDs = schoolGroupIDs;
	}

	/**
	 * @return Returns the school.
	 */
	public School getSchool() {
		return this._school;
	}
}