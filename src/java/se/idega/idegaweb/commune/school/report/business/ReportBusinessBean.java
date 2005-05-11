/*
 * $Id: ReportBusinessBean.java,v 1.35 2005/05/11 07:15:37 laddi Exp $
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
 * Last modified: $Date: 2005/05/11 07:15:37 $ by $Author: laddi $
 *
 * @author Anders Lindman
 * @version $Revision: 1.35 $
 */
public class ReportBusinessBean extends com.idega.business.IBOServiceBean implements ReportBusiness  {

	private final static int SCHOOL_TYPE_ELEMENTARY_SCHOOL = 4;
	private final static int SCHOOL_TYPE_PRE_SCHOOL_CLASS = 5;
	private final static int SCHOOL_TYPE_COMPULSORY_SCHOOL = 28;
	private final static int SCHOOL_TYPE_HIGH_SCHOOL = 26;
	private final static int SCHOOL_TYPE_COMPULSORY_HIGH_SCHOOL = 27;
	private final static int SCHOOL_TYPE_PRE_SCHOOL = 1;
	private final static int SCHOOL_TYPE_FAMILY_DAYCARE = 2;
	private final static int SCHOOL_TYPE_GENERAL_PRE_SCHOOL = 33;
	private final static int SCHOOL_TYPE_GENERAL_FAMILY_DAYCARE = 34;
	private final static int SCHOOL_TYPE_AFTER_SCHOOL_6 = 29;
	private final static int SCHOOL_TYPE_FAMILY_AFTER_SCHOOL_6 = 46;
	private final static int SCHOOL_TYPE_AFTER_SCHOOL_7_9 = 30;
	private final static int SCHOOL_TYPE_FAMILY_AFTER_SCHOOL_7_9 = 47;
	
	private final static String SCHOOL_CATEGORY_CHILD_CARE = "CHILD_CARE";
	
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
		_currentSchoolSeason = null;
		_schoolSeasonId = -1;
		_schoolSeasonStartYear = -1;
		_schools = null;
		_schoolAreas = null;
		_schoolsByArea = null;
		_studyPaths = null;
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
				_currentSchoolSeason = sb.getCurrentSchoolSeason(sb.getCategoryElementarySchool());
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
	 * Returns all school areas for private schools. 
	 */
	public Collection getPrivateSchoolAreas() {
		if (_schoolAreas == null) {
			Collection managementTypes = new ArrayList();
			managementTypes.add("COMPANY");
			managementTypes.add("FOUNDATION");
			managementTypes.add("OTHER");
			SchoolHome schoolHome = null;
			try {
				schoolHome = getSchoolHome();
				SchoolAreaHome home = getSchoolBusiness().getSchoolAreaHome();
				_schoolAreas = home.findAllBySchoolTypeCityAndManagementTypes(SCHOOL_TYPE_ELEMENTARY_SCHOOL, 
						"Nacka", managementTypes);
			} catch (Exception e) {}
			ArrayList areas = new ArrayList();
			Iterator iter = _schoolAreas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();
				int areaId = ((Integer) area.getPrimaryKey()).intValue();
				try {
					Collection schools = schoolHome.findAllByAreaTypeManagementCommune(
							areaId,
							SCHOOL_TYPE_ELEMENTARY_SCHOOL,
							managementTypes,
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
	 * Returns all school areas for pre school operation. 
	 */
	public Collection getPreSchoolOperationAreas() {
		if (_schoolAreas == null) {
			Collection managementTypes = new ArrayList();
			managementTypes.add("COMMUNE");
			managementTypes.add("COMPANY");
			managementTypes.add("FOUNDATION");
			managementTypes.add("OTHER");
			managementTypes.add("COOPERATIVE_COMMUNE_LIABILITY");
			Collection schoolTypes = new ArrayList();
			schoolTypes.add(new Integer(SCHOOL_TYPE_PRE_SCHOOL));
			schoolTypes.add(new Integer(SCHOOL_TYPE_FAMILY_DAYCARE));
			schoolTypes.add(new Integer(SCHOOL_TYPE_GENERAL_PRE_SCHOOL));
			schoolTypes.add(new Integer(SCHOOL_TYPE_GENERAL_FAMILY_DAYCARE));
			SchoolHome schoolHome = null;
			try {
				schoolHome = getSchoolHome();
				SchoolAreaHome home = getSchoolBusiness().getSchoolAreaHome();
				_schoolAreas = home.findAllSchoolAreas();
			} catch (Exception e) {}
			ArrayList areas = new ArrayList();
			Iterator iter = _schoolAreas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();
				int areaId = ((Integer) area.getPrimaryKey()).intValue();
				try {
					Collection schools = schoolHome.findAllByAreaTypeManagementCommune(
							areaId,
							schoolTypes,
							managementTypes,
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
	 * Returns all school areas for after schools for 6 years children. 
	 */
	public Collection getAfterSchool6Areas() {
		if (_schoolAreas == null) {
			Collection managementTypes = new ArrayList();
			managementTypes.add("COMMUNE");
			managementTypes.add("COMPANY");
			managementTypes.add("FOUNDATION");
			managementTypes.add("OTHER");
			managementTypes.add("COOPERATIVE_COMMUNE_LIABILITY");
			Collection schoolTypes = new ArrayList();
			schoolTypes.add(new Integer(SCHOOL_TYPE_AFTER_SCHOOL_6));
			schoolTypes.add(new Integer(SCHOOL_TYPE_FAMILY_AFTER_SCHOOL_6));
			SchoolHome schoolHome = null;
			try {
				schoolHome = getSchoolHome();
				SchoolAreaHome home = getSchoolBusiness().getSchoolAreaHome();
				_schoolAreas = home.findAllSchoolAreas();
			} catch (Exception e) {}
			ArrayList areas = new ArrayList();
			Iterator iter = _schoolAreas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();
				int areaId = ((Integer) area.getPrimaryKey()).intValue();
				try {
					Collection schools = schoolHome.findAllByAreaTypeManagementCommune(
							areaId,
							schoolTypes,
							managementTypes,
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
	 * Returns all school areas for after schools for 7-9 years children. 
	 */
	public Collection getAfterSchool7_9Areas() {
		if (_schoolAreas == null) {
			Collection managementTypes = new ArrayList();
			managementTypes.add("COMMUNE");
			managementTypes.add("COMPANY");
			managementTypes.add("FOUNDATION");
			managementTypes.add("OTHER");
			managementTypes.add("COOPERATIVE_COMMUNE_LIABILITY");
			Collection schoolTypes = new ArrayList();
			schoolTypes.add(new Integer(SCHOOL_TYPE_AFTER_SCHOOL_7_9));
			schoolTypes.add(new Integer(SCHOOL_TYPE_FAMILY_AFTER_SCHOOL_7_9));
			SchoolHome schoolHome = null;
			try {
				schoolHome = getSchoolHome();
				SchoolAreaHome home = getSchoolBusiness().getSchoolAreaHome();
				_schoolAreas = home.findAllSchoolAreas();
			} catch (Exception e) {}
			ArrayList areas = new ArrayList();
			Iterator iter = _schoolAreas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();
				int areaId = ((Integer) area.getPrimaryKey()).intValue();
				try {
					Collection schools = schoolHome.findAllByAreaTypeManagementCommune(
							areaId,
							schoolTypes,
							managementTypes,
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
	 * Returns schools matching the speficied parameters.
	 * @param areaId the school area id
	 * @param schoolTypes the collection of school types (Integer)
	 * @param managementTypes the collection of management types (String) 
	 */
	public Collection getCommuneSchools(int areaId, Collection schoolTypes, Collection managementTypes) {
		Collection schools = null;
		try {
			SchoolHome schoolHome = getSchoolHome();
			schools = schoolHome.findAllByAreaTypeManagementCommune(
					areaId,
					schoolTypes,
					managementTypes,
					NACKA_COMMUNE_ID);
		} catch (Exception e) {
			log(e);
		}
		return schools;
	}

	/**
	 * Returns all child care providers.
	 */
	public Collection getChildCareProviders() {
		if (_schools == null) {
			try {
				SchoolBusiness sb = getSchoolBusiness();
				_schools = sb.findAllSchoolsByCategory(SCHOOL_CATEGORY_CHILD_CARE);
			} catch (Exception e) {
				log(e);
			}
		}
		return _schools;
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
	 * Returns all private for the specified area. 
	 */
	public Collection getPrivateSchools(SchoolArea schoolArea) {
		if (_schoolsByArea == null) {
			try {
				_schoolsByArea = new TreeMap();
				Collection managementTypes = new ArrayList();
				managementTypes.add("COMPANY");
				managementTypes.add("FOUNDATION");
				managementTypes.add("OTHER");
				SchoolHome schoolHome = getSchoolHome();
				Collection areas = getPrivateSchoolAreas();
				Iterator areaIter = areas.iterator();
				while (areaIter.hasNext()) {
					SchoolArea area = (SchoolArea) areaIter.next();
					int schoolAreaId = ((Integer) area.getPrimaryKey()).intValue();
					Collection schools = schoolHome.findAllByAreaTypeManagementCommune(
							schoolAreaId,
							SCHOOL_TYPE_ELEMENTARY_SCHOOL,
							managementTypes,
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
			ArrayList l = new ArrayList();
			try {
				SchoolHome home = getSchoolBusiness().getSchoolHome();
				Collection c = home.findAllBySchoolType(SCHOOL_TYPE_COMPULSORY_HIGH_SCHOOL);
				Iterator iter = c.iterator();
				while (iter.hasNext()) {
					School school = (School) iter.next();
					if ((school.getCommuneId() == NACKA_COMMUNE_ID) && !school.getName().equals("Utlandselever")) {
						l.add(school);
					}
				}
				_schools = l;
			} catch (Exception e) {}
		}
		return _schools;
	}
	
	/**
	 * Returns all private high schools. 
	 */
	public Collection getPrivateHighSchools() {
		if (_schools == null) {
			try {
				_schools = new ArrayList();
				SchoolHome home = getSchoolBusiness().getSchoolHome();
				School s1 = home.findBySchoolName("Cybergymnasiet");
				School s2 = home.findBySchoolName("Kunskapsgymnasiet  i Orminge");
				School s3 = home.findBySchoolName("Mediagymnasiet");
				_schools.add(s1);
				_schools.add(s2);
				_schools.add(s3);
			} catch (Exception e) {
				log(e);
			}
		}
		return _schools;
	}
	
	/**
	 * Returns all Nacka commune high schools. 
	 */
	public Collection getNackaCommuneHighSchools() {
		if (_schools == null) {
			try {
				_schools = new ArrayList();
				SchoolHome home = getSchoolBusiness().getSchoolHome();
				School s1 = home.findBySchoolName("Internationella Skolan i Nacka");
				School s2 = home.findBySchoolName("Nacka Gymnasium A-enheten");
				School s3 = home.findBySchoolName("Nacka Gymnasium B-enheten");
				School s4 = home.findBySchoolName("Nacka Gymnasium C-enheten");
				School s5 = home.findByPrimaryKey(new Integer(1938)); // Saltsjobadens Samskola
				_schools.add(s1);
				_schools.add(s2);
				_schools.add(s3);
				_schools.add(s4);
				_schools.add(s5);
			} catch (Exception e) {
				log(e);
				e.printStackTrace();
			}
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
	 * Returns all study paths including sub paths (directions). 
	 */
	public Collection getAllStudyPathsIncludingDirections() {
		if (_studyPaths == null) {
			try {
				_studyPaths = new ArrayList();
				SchoolStudyPathHome home = (SchoolStudyPathHome) com.idega.data.IDOLookup.getHome(SchoolStudyPath.class);
				Collection c = home.findAllStudyPaths();
				Iterator iter = c.iterator();
				while (iter.hasNext()) {
					SchoolStudyPath sp = (SchoolStudyPath) iter.next();
					if (sp.getDescription().length() > 3) {
						_studyPaths.add(sp);
					}
				}
			} catch (Exception e) {}
		}
		return _studyPaths;
	}

	/**
	 * Returns the id for school type elementary school.
	 */
	public int getElementarySchoolTypeId() {
		return SCHOOL_TYPE_ELEMENTARY_SCHOOL;
	}

	/**
	 * Returns the id for school type pre-school class.
	 */
	public int getPreSchoolClassTypeId() {
		return SCHOOL_TYPE_PRE_SCHOOL_CLASS;
	}

	/**
	 * Returns the id for school type compulsory school.
	 */
	public int getCompulsorySchoolTypeId() {
		return SCHOOL_TYPE_COMPULSORY_SCHOOL;
	}

	/**
	 * Returns the id for school type high school.
	 */
	public int getHighSchoolTypeId() {
		return SCHOOL_TYPE_HIGH_SCHOOL;
	}

	/**
	 * Returns the id for school type compulsory high school.
	 */
	public int getCompulsoryHighSchoolTypeId() {
		return SCHOOL_TYPE_COMPULSORY_HIGH_SCHOOL;
	}

	/**
	 * Returns the id for school type pre school.
	 */
	public int getPreSchoolTypeId() {
		return SCHOOL_TYPE_PRE_SCHOOL;
	}

	/**
	 * Returns the id for school type family daycare.
	 */
	public int getFamilyDayCareSchoolTypeId() {
		return SCHOOL_TYPE_FAMILY_DAYCARE;
	}
	
	/**
	 * Returns the id for school type general pre school.
	 */
	public int getGeneralPreSchoolTypeId() {
		return SCHOOL_TYPE_GENERAL_PRE_SCHOOL;
	}

	/**
	 * Returns the id for school type family daycare.
	 */
	public int getGeneralFamilyDaycareSchoolTypeId() {
		return SCHOOL_TYPE_GENERAL_FAMILY_DAYCARE;
	}
	
	/**
	 * Returns the id for school type after school 6 years children.
	 */
	public int getAfterSchool6TypeId() {
		return SCHOOL_TYPE_AFTER_SCHOOL_6;
	}

	/**
	 * Returns the id for school type family after school 6 years children.
	 */
	public int getFamilyAfterSchool6TypeId() {
		return SCHOOL_TYPE_FAMILY_AFTER_SCHOOL_6;
	}
	
	/**
	 * Returns the id for school type after school 7-9 years children.
	 */
	public int getAfterSchool7_9TypeId() {
		return SCHOOL_TYPE_AFTER_SCHOOL_7_9;
	}

	/**
	 * Returns the id for school type family after school 7-9 years children.
	 */
	public int getFamilyAfterSchool7_9TypeId() {
		return SCHOOL_TYPE_FAMILY_AFTER_SCHOOL_7_9;
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
