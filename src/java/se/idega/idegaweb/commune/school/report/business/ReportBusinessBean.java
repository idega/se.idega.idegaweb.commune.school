/*
 * $Id: ReportBusinessBean.java,v 1.1 2003/12/08 16:24:24 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.lang.reflect.Constructor;

/** 
 * Business logic for school reports.
 * <p>
 * Last modified: $Date: 2003/12/08 16:24:24 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class ReportBusinessBean extends com.idega.business.IBOServiceBean implements ReportBusiness  {

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
		query.setSelectCountPlacements();
		query.setOnlyNackaCitizens();
		query.setOnlyNackaSchools();
		if (schoolYearName.equals("F")) {
			query.setSchoolTypePreSchoolClass();
		} else {
			query.setSchoolTypeElementarySchool();
		}
		query.setSchoolYear(schoolYearName);
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
		query.setSelectCountPlacements();
		query.setOnlyNackaCitizens();
		query.setOnlySchoolsInOtherCommunes();
		if (schoolYearName.equals("F")) {
			query.setSchoolTypePreSchoolClass();
		} else {
			query.setSchoolTypeElementarySchool();
		}
		query.setSchoolYear(schoolYearName);
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
		query.setSelectCountPlacements();
		query.setOnlyNackaCitizens();
		if (schoolYearName.equals("F")) {
			query.setSchoolTypePreSchoolClass();
		} else {
			query.setSchoolTypeElementarySchool();
		}
		query.setSchoolYear(schoolYearName);
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
		query.setSelectCountPlacements();
		query.setOnlyNackaCitizens();
		if (schoolYearName.equals("F")) {
			query.setSchoolTypePreSchoolClass();
		} else {
			query.setSchoolTypeElementarySchool();
		}
		query.setSchoolYear(schoolYearName);
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
		query.setSelectCountPlacements();
		query.setOnlyNackaCitizens();
		query.setOnlyNackaSchools();
		if (schoolYearName.equals("F")) {
			return 0;
		} else {
			query.setSchoolTypeCompulsorySchool();
		}
		query.setSchoolYear(schoolYearName);
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
		query.setSelectCountPlacements();
		query.setOnlyNackaCitizens();
		query.setOnlySchoolsInOtherCommunes();
		if (schoolYearName.equals("F")) {
			return 0;
		} else {
			query.setSchoolTypeCompulsorySchool();
		}
		query.setSchoolYear(schoolYearName);
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
		query.setSelectCountPlacements();
		query.setOnlyNackaCitizens();
		if (schoolYearName.equals("F")) {
			return 0;
		} else {
			query.setSchoolTypeElementarySchool();
		}
		query.setSchoolYear(schoolYearName);
		query.setOnlyPrivateSchools();
		query.setNotForieignSchools();
		return query.execute();
	}

	/**
	 * @see com.idega.business.IBOServiceBean#log()
	 */
	public void log(String msg) {
		super.log(msg);
	}
}
