/*
 * $Id: SchoolExportBusinessBean.java,v 1.1 2004/01/29 14:11:23 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.business;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileBMPBean;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;

/** 
 * Business logic for exporting student placement text files.
 * <p>
 * Last modified: $Date: 2004/01/29 14:11:23 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class SchoolExportBusinessBean extends com.idega.business.IBOServiceBean implements SchoolExportBusiness  {
	 
	private final static String EXPORT_FOLDER_NAME = "School Export Files";
	
	/**
	 * Returns a school business instance. 
	 */	
	protected SchoolBusiness getSchoolBusiness() {
		SchoolBusiness sb = null;
		try {
			sb = (SchoolBusiness) this.getServiceInstance(SchoolBusiness.class);
		} catch (IBOLookupException e) {
			log(e);
			throw new IBORuntimeException(e.getMessage());
		}
		return sb;
	}	
	
	/**
	 * Returns a school Class member home instance. 
	 */	
	protected SchoolClassMemberHome getSchoolClassMemberHome() {
		SchoolClassMemberHome home = null;
		try {
			home = (SchoolClassMemberHome) this.getIDOHome(SchoolClassMember.class);
		} catch (RemoteException e) {
			log(e);
			throw new IBORuntimeException(e.getMessage());
		}
		return home;
	}	
	
	/**
	 * Returns all valid high school placements.
	 */
	public Collection findAllPlacementsByCategory(SchoolCategory category) {
		Collection placements = null;
		try {
			SchoolBusiness sb = getSchoolBusiness();
			SchoolSeason season = sb.getCurrentSchoolSeason();
			int seasonId = ((Integer) season.getPrimaryKey()).intValue();
			String highSchoolCategory = (String) category.getPrimaryKey();
			SchoolClassMemberHome home = getSchoolClassMemberHome();
			placements = home.findByCategorydManagementCommune(
					highSchoolCategory,
					"COMMUNE",
					1,
					seasonId);
		} catch (RemoteException e) {
			log(e);
		} catch (FinderException e) {
			log(e);
		}
		return placements;
	}	
	
	/**
	 * Returns all valid elementary school placements.
	 */
	public Collection findAllElementarySchoolPlacements() {
		Collection placements = null;
		try {
			placements = findAllPlacementsByCategory(getSchoolBusiness().getCategoryElementarySchool());
		} catch (RemoteException e) {
			log(e);
		}
		return placements;
	}	
	
	/**
	 * Returns all valid high school placements.
	 */
	public Collection findAllHighSchoolPlacements() {
		Collection placements = null;
		try {
			placements = findAllPlacementsByCategory(getSchoolBusiness().getCategoryHighSchool());
		} catch (RemoteException e) {
			log(e);
		}
		return placements;
	}
	
	/**
	 * Exports the specified collection of placements as a text file.
	 */
	public void exportFile(Collection placements, String filename) {
		ICFile exportFolder = null;
		ICFileHome fileHome = null;

		try {
			fileHome = (ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class);
			exportFolder = fileHome.findByFileName(EXPORT_FOLDER_NAME);
		} catch (FinderException e) {
			try {
				ICFile root = fileHome.findByFileName(ICFileBMPBean.IC_ROOT_FOLDER_NAME);
				exportFolder = fileHome.create();
				exportFolder.setName(EXPORT_FOLDER_NAME);
				exportFolder.setMimeType("application/vnd.iw-folder");
				exportFolder.store();
				root.addChild(exportFolder);
			} catch (Exception e2) {
				log(e2);
				return;
			}
		} catch (IDOLookupException e) {
			log(e);
			return;
		}

		ICFile exportFile = null;
		
		try {
			exportFile = fileHome.create();
			String text = createCommaSeparatedText(placements);
			byte[] bytes = text.getBytes();

			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			exportFile.setFileValue(bais);
			exportFile.setMimeType("text/plain");
			exportFile.setName(filename);
			exportFile.setFileSize(text.length());
			exportFile.store();
			
			exportFolder.addChild(exportFile);
		} catch (Exception e) {
			log(e);
		}
	}

	/*
	 * Returns a comma separated text with placements.
	 */
	private String createCommaSeparatedText(Collection placements) {
		if (placements == null) {
			return "no placements found.";
		}
		StringBuffer sb = new StringBuffer();
		Iterator iter = placements.iterator();
		int line = 0;
		while (iter.hasNext()) {
			SchoolClassMember placement = (SchoolClassMember) iter.next();
			User student = placement.getStudent();
			SchoolClass schoolClass = placement.getSchoolClass();
			School school = schoolClass.getSchool();
			
			String personalId = student.getPersonalID();
			String lastName = student.getLastName();
			String firstName = student.getFirstName() + " " + student.getMiddleName();
			String givenName = student.getLastName() + "," + student.getFirstName();
			String middleName = "";
			String schoolName = school.getName();
			String schoolClassName = schoolClass.getName();
			String addressProtection = "";
			String email = "";
			
			sb.append(personalId).append(";");
			sb.append(lastName).append(";");
			sb.append(firstName).append(";");
			sb.append(givenName).append(";");
			sb.append(middleName).append(";");
			sb.append(schoolName).append(";");
			sb.append(schoolClassName).append(";");
			sb.append(addressProtection).append(";");
			sb.append(email).append("\n");
			
			line++;
			if (line % 100 == 0) {
				System.out.println("SCHOOL EXPORT: line " + line);
			}
		}
		return sb.toString();
	}
	
	/**
	 * Returns all export files. 
	 */
	public Iterator getAllExportFiles() {
		Iterator exportFiles = null;
		try {
			ICFileHome fileHome = (ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class);
			ICFile exportFolder = fileHome.findByFileName(EXPORT_FOLDER_NAME);
			exportFiles = exportFolder.getChildren();
		} catch (Exception e) {
			log(e);
		}
		return exportFiles;
	}
}
