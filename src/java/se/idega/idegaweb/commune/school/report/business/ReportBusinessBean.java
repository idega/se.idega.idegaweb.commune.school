/*
 * $Id: ReportBusinessBean.java,v 1.3 2003/12/10 16:33:01 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.lang.reflect.Constructor;
import java.rmi.RemoteException;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolSeason;

/** 
 * Business logic for school reports.
 * <p>
 * Last modified: $Date: 2003/12/10 16:33:01 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.3 $
 */
public class ReportBusinessBean extends com.idega.business.IBOServiceBean implements ReportBusiness  {

	private SchoolSeason _currentSchoolSeason = null;
	private int _schoolSeasonId = -1;
	private int _schoolSeasonStartYear = -1;
	
	/**
	 * Factory method for creating a report model with the specified class.
	 * @param reportModelClass the report model class to instantiate 
	 */
	public ReportModel createReportModel(Class reportModelClass) {
		ReportModel reportModel = null;		
		try {
			Class[] constructorArgumentTypes = { ReportBusiness.class };
			Object[] constructorArgs = { this };
			Constructor classConstructor = reportModelClass.getConstructor(constructorArgumentTypes);
			reportModel = (ReportModel) classConstructor.newInstance(constructorArgs);
		} catch (ClassCastException e) {
			log("createReportModel(): The specified class must be of type " + ReportModel.class.getName());
		} catch (Exception e) {
			log("createReportModel(): The specified class must have a constructor " +
					"with the following parameter type: " + ReportBusiness.class.getName());
		}
		
		return reportModel;
	}
	
	/**
	 * Returns the number of student placements for elementary schools
	 * in Nacka commune for the specified school year.
	 * Only students in Nacka commune are counted. 
	 */
	public int getElementaryNackaCommunePlacementCount(String schoolYearName) {
		ReportQuery query = new ReportQuery();
		query.setSelectCountPlacements(getSchoolSeasonId());
		query.setOnlyNackaCitizens();
		query.setOnlyNackaSchools();
		if (schoolYearName.equals("F")) {
			query.setSchoolTypePreSchoolClass();
		} else {
			query.setSchoolTypeElementarySchool();
		}
		if (schoolYearName.equals("0")) {
			query.setOnlyStudentsBorn(getSchoolSeasonStartYear() - 6);
			query.setSchoolYear("1");
		} else {
			query.setSchoolYear(schoolYearName);			
		}
		query.setNotPrivateSchools();
		query.setNotForieignSchools();
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for elementary schools
	 * outside Nacka commune for the specified school year. 
	 * Only students in Nacka commune are counted. 
	 */
	public int getElementaryOtherCommunesPlacementCount(String schoolYearName) {
		ReportQuery query = new ReportQuery();
		query.setSelectCountPlacements(getSchoolSeasonId());
		query.setOnlyNackaCitizens();
		query.setOnlySchoolsInOtherCommunes();
		if (schoolYearName.equals("F")) {
			query.setSchoolTypePreSchoolClass();
		} else {
			query.setSchoolTypeElementarySchool();
		}
		if (schoolYearName.equals("0")) {
			query.setOnlyStudentsBorn(getSchoolSeasonStartYear() - 6);
			query.setSchoolYear("1");
		} else {
			query.setSchoolYear(schoolYearName);			
		}
		query.setNotPrivateSchools();
		query.setNotForieignSchools();
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for elementary private schools
	 * for the specified school year. 
	 * Only students in Nacka commune are counted. 
	 */
	public int getElementaryPrivateSchoolPlacementCount(String schoolYearName) {
		ReportQuery query = new ReportQuery();
		query.setSelectCountPlacements(getSchoolSeasonId());
		query.setOnlyNackaCitizens();
		if (schoolYearName.equals("F")) {
			query.setSchoolTypePreSchoolClass();
		} else {
			query.setSchoolTypeElementarySchool();
		}
		if (schoolYearName.equals("0")) {
			query.setOnlyStudentsBorn(getSchoolSeasonStartYear() - 6);
			query.setSchoolYear("1");
		} else {
			query.setSchoolYear(schoolYearName);			
		}
		query.setOnlyPrivateSchools();
		query.setNotForieignSchools();
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for foreign schools
	 * for the specified school year. 
	 * Only students in Nacka commune are counted. 
	 */
	public int getElementaryForeignSchoolPlacementCount(String schoolYearName) {
		ReportQuery query = new ReportQuery();
		query.setSelectCountPlacements(getSchoolSeasonId());
		query.setOnlyNackaCitizens();
		if (schoolYearName.equals("F")) {
			query.setSchoolTypePreSchoolClass();
		} else {
			query.setSchoolTypeElementarySchool();
		}
		if (schoolYearName.equals("0")) {
			query.setOnlyStudentsBorn(getSchoolSeasonStartYear() - 6);
			query.setSchoolYear("1");
		} else {
			query.setSchoolYear(schoolYearName);			
		}
		query.setOnlyForieignSchools();
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for compulsory schools
	 * in Nacka commune for the specified school year.
	 * Only students in Nacka commune are counted. 
	 */
	public int getCompulsoryNackaCommunePlacementCount(String schoolYearName) {
		ReportQuery query = new ReportQuery();
		query.setSelectCountPlacements(getSchoolSeasonId());
		query.setOnlyNackaCitizens();
		query.setOnlyNackaSchools();
		if (schoolYearName.equals("F")) {
			return 0;
		} else {
			query.setSchoolTypeCompulsorySchool();
		}
		if (schoolYearName.equals("0")) {
			query.setOnlyStudentsBorn(getSchoolSeasonStartYear() - 6);
			query.setSchoolYear("S1");
		} else {
			query.setSchoolYear(schoolYearName);			
		}
		query.setNotPrivateSchools();
		query.setNotForieignSchools();
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for compulsory schools
	 * outside Nacka commune for the specified school year. 
	 * Only students in Nacka commune are counted. 
	 */
	public int getCompulsoryOtherCommunesPlacementCount(String schoolYearName) {
		ReportQuery query = new ReportQuery();
		query.setSelectCountPlacements(getSchoolSeasonId());
		query.setOnlyNackaCitizens();
		query.setOnlySchoolsInOtherCommunes();
		if (schoolYearName.equals("F")) {
			return 0;
		} else {
			query.setSchoolTypeCompulsorySchool();
		}
		if (schoolYearName.equals("0")) {
			query.setOnlyStudentsBorn(getSchoolSeasonStartYear() - 6);
			query.setSchoolYear("S1");
		} else {
			query.setSchoolYear(schoolYearName);			
		}
		query.setNotPrivateSchools();
		query.setNotForieignSchools();
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for compulsory private schools
	 * for the specified school year. 
	 * Only students in Nacka commune are counted. 
	 */
	public int getCompulsoryPrivateSchoolPlacementCount(String schoolYearName) {
		ReportQuery query = new ReportQuery();
		query.setSelectCountPlacements(getSchoolSeasonId());
		query.setOnlyNackaCitizens();
		if (schoolYearName.equals("F")) {
			return 0;
		} else {
			query.setSchoolTypeElementarySchool();
		}
		if (schoolYearName.equals("0")) {
			query.setOnlyStudentsBorn(getSchoolSeasonStartYear() - 6);
			query.setSchoolYear("S1");
		} else {
			query.setSchoolYear(schoolYearName);			
		}
		query.setOnlyPrivateSchools();
		query.setNotForieignSchools();
		return query.execute();
	}
	
	/**
	 * Returns the current school season.
	 */
	public SchoolSeason getCurrentSchoolSeason() {
		if (_currentSchoolSeason == null) {
			try {
				SchoolBusiness sb = getSchoolBusiness();
				_currentSchoolSeason = sb.getCurrentSchoolSeason();
			} catch (Exception e) {}
		}
		return _currentSchoolSeason;
	}
	
	/**
	 * Returns the current school season id.
	 */
	public int getSchoolSeasonId() {
		if (_schoolSeasonId == -1) {
			try {
				_schoolSeasonId = ((Integer) getCurrentSchoolSeason().getPrimaryKey()).intValue();
			} catch (Exception e) {}
		}
		return _schoolSeasonId;
	}

	private int getSchoolSeasonStartYear() {
		if (_schoolSeasonStartYear == -1) {
			SchoolSeason ss = getCurrentSchoolSeason();
			try {
				String s = ss.getSchoolSeasonStart().toString();
				_schoolSeasonStartYear = Integer.parseInt(s.substring(0, 4));
			} catch (Exception e) {}
		}
		return _schoolSeasonStartYear;
	}
	
	/**
	 * Returns a school business instance.
	 */
	SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
	}
	
	
	/**
	 * @see com.idega.business.IBOServiceBean#log()
	 */
	public void log(String msg) {
		super.log(msg);
	}
}
