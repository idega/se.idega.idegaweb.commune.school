/*
 * $Id: ReportBusinessBean.java,v 1.15 2004/01/12 10:28:24 anders Exp $
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolAreaHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathHome;

/** 
 * Business logic for school reports.
 * <p>
 * Last modified: $Date: 2004/01/12 10:28:24 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.15 $
 */
public class ReportBusinessBean extends com.idega.business.IBOServiceBean implements ReportBusiness  {

	private final static int SCHOOL_TYPE_ELEMENTARY_SCHOOL = 4;
	private final static int SCHOOL_TYPE_COMPULSORY_SCHOOL = 28;
	private final static int SCHOOL_TYPE_COMPULSORY_HIGH_SCHOOL = 27;
	private final static String MANAGEMENT_TYPE_COMMUNE = "COMMUNE";
	private final static int NACKA_COMMUNE_ID = 1;
	
	private SchoolSeason _currentSchoolSeason = null;
	private int _schoolSeasonId = -1;
	private int _schoolSeasonStartYear = -1;
	private Collection _schools = null;
	private Collection _schoolAreas = null;
	private Map _schoolsByArea = null;
	private Collection _studyPaths = null;
	
	/**
	 * Factory method for creating a report model with the specified Class.
	 * @param reportModelClass the report model Class to instantiate 
	 */
	public ReportModel createReportModel(Class reportModelClass) {
		ReportModel reportModel = null;		
		try {
			Class[] constructorArgumentTypes = { ReportBusiness.class };
			Object[] constructorArgs = { this };
			Constructor classConstructor = reportModelClass.getConstructor(constructorArgumentTypes);
			reportModel = (ReportModel) classConstructor.newInstance(constructorArgs);
		} catch (ClassCastException e) {
			log("createReportModel(): The specified Class must be of type " + ReportModel.class.getName());
		} catch (Exception e) {
			log("createReportModel(): The specified Class must have a constructor " +
					"with the following parameter type: " + ReportBusiness.class.getName());
		}
		
		return reportModel;
	}

	/**
	 * Returns the specified age if in high school age period from, and -1 if not.
	 */
	public int getHighSchoolStudentAgeFrom(int age) {
		int ageFrom = -1;
		switch (age) {
			case 17:
				ageFrom = age;
				break;
			case 18:
				ageFrom = age;
				break;
			case 19:
				ageFrom = age;
				break;
		}
		return ageFrom;
	}

	/**
	 * Returns the specified age if in high school age period to, and -1 if not.
	 */
	public int getHighSchoolStudentAgeTo(int age) {
		int ageTo = -1;
		switch (age) {
			case 16:
				ageTo = age;
				break;
			case 17:
				ageTo = age;
				break;
			case 18:
				ageTo = age;
				break;
		}
		return ageTo;
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
	
	/**
	 * Returns the year for the current school season start. 
	 */
	public int getSchoolSeasonStartYear() {
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
	 * Returns all school areas for elemtary schools. 
	 */
	public Collection getElementarySchoolAreas() {
		if (_schoolAreas == null) {
			try {
				SchoolAreaHome home = getSchoolBusiness().getSchoolAreaHome();
				_schoolAreas = home.findAllBySchoolTypeAndCity(SCHOOL_TYPE_ELEMENTARY_SCHOOL, "Nacka");
			} catch (Exception e) {}
		}
		return _schoolAreas;
	}
	
	/**
	 * Returns all school areas for compulsory schools. 
	 */
	public Collection getCompulsorySchoolAreas() {
		if (_schoolAreas == null) {
			SchoolHome schoolHome = null;
			try {
				schoolHome = getSchoolHome();
				SchoolAreaHome home = getSchoolBusiness().getSchoolAreaHome();
				_schoolAreas = home.findAllBySchoolTypeAndCity(SCHOOL_TYPE_COMPULSORY_SCHOOL, "Nacka");
			} catch (Exception e) {}
			ArrayList areas = new ArrayList();
			Iterator iter = _schoolAreas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();
				int areaId = ((Integer) area.getPrimaryKey()).intValue();
				try {
					Collection schools = schoolHome.findAllByAreaTypeCommune(
							areaId,
							SCHOOL_TYPE_COMPULSORY_SCHOOL,
							NACKA_COMMUNE_ID);
					if (schools.size() > 0) {
						areas.add(area);
					}
				} catch (Exception e) {}
			}
			_schoolAreas = areas;
		}
		return _schoolAreas;
	}

	/**
	 * Returns all elementary for the specified area. 
	 */
	public Collection getElementarySchools(SchoolArea schoolArea) {
		if (_schoolsByArea == null) {
			try {
				_schoolsByArea = new TreeMap();
				SchoolHome schoolHome = getSchoolHome();
				Collection areas = getElementarySchoolAreas();
				Iterator areaIter = areas.iterator();
				while (areaIter.hasNext()) {
					SchoolArea area = (SchoolArea) areaIter.next();
					int schoolAreaId = ((Integer) area.getPrimaryKey()).intValue();
					Collection schools = schoolHome.findAllByAreaTypeManagementCommune(
							schoolAreaId,
							SCHOOL_TYPE_ELEMENTARY_SCHOOL,
							MANAGEMENT_TYPE_COMMUNE,
							NACKA_COMMUNE_ID);
					_schoolsByArea.put(area.getName(), schools);
				}
			} catch (Exception e) {
				log(e);
			}
		}
		if (_schoolsByArea != null) {
			return (Collection) _schoolsByArea.get(schoolArea.getName());
		} else {
			return new ArrayList();
		}
	}

	/**
	 * Returns all compulsory for the specified area. 
	 */
	public Collection getCompulsorySchools(SchoolArea schoolArea) {
		if (_schoolsByArea == null) {
			try {
				_schoolsByArea = new TreeMap();
				SchoolHome schoolHome = getSchoolHome();
				Collection areas = getCompulsorySchoolAreas();
				Iterator areaIter = areas.iterator();
				while (areaIter.hasNext()) {
					SchoolArea area = (SchoolArea) areaIter.next();
					int schoolAreaId = ((Integer) area.getPrimaryKey()).intValue();
					Collection schools = schoolHome.findAllByAreaTypeCommune(
							schoolAreaId,
							SCHOOL_TYPE_COMPULSORY_SCHOOL,
							NACKA_COMMUNE_ID);
					_schoolsByArea.put(area.getName(), schools);
				}
			} catch (Exception e) {
				log(e);
			}
		}
		if (_schoolsByArea != null) {
			return (Collection) _schoolsByArea.get(schoolArea.getName());
		} else {
			return new ArrayList();
		}
	}
	
	/**
	 * Returns all compulsory high schools. 
	 */
	public Collection getCompulsoryHighSchools() {
		if (_schools == null) {
			try {
				SchoolHome home = getSchoolBusiness().getSchoolHome();
				_schools = home.findAllBySchoolType(SCHOOL_TYPE_COMPULSORY_HIGH_SCHOOL);
			} catch (Exception e) {}
		}
		return _schools;
	}
	
	/**
	 * Returns all study paths. 
	 */
	public Collection getAllStudyPaths() {
		if (_studyPaths == null) {
			try {
				SchoolStudyPathHome home = (SchoolStudyPathHome) com.idega.data.IDOLookup.getHome(SchoolStudyPath.class);
				_studyPaths = home.findAllStudyPathsByCodeLength(2);
			} catch (Exception e) {}
		}
		return _studyPaths;
	}
	
	/**
	 * Returns a school business instance.
	 */
	SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
	}
	
	/**
	 * Returns a school home instance. 
	 */	
	protected SchoolHome getSchoolHome() throws RemoteException {
		return (SchoolHome) com.idega.data.IDOLookup.getHome(School.class);
	}	
	
	/**
	 * @see com.idega.business.IBOServiceBean#log()
	 */
	public void log(String msg) {
		super.log(msg);
	}
}
