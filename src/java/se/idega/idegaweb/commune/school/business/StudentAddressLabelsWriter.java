/*
 * $Id: StudentAddressLabelsWriter.java,v 1.4 2004/03/22 09:20:19 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileBMPBean;
import com.idega.core.file.data.ICFileHome;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.user.data.User;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/** 
 * This MediaWritable class generates a PDF stream with student address labels.
 * <p>
 * Last modified: $Date: 2004/03/22 09:20:19 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.4 $
 * @see com.idega.io.MediaWritable
 */
public class StudentAddressLabelsWriter {

	private final static String REPORT_FOLDER_NAME = "Export Files";

	private final static int NR_OF_COLUMNS = 3;
	private final static int NR_OF_ROWS = 8;
	private final static int NR_OF_ADDRESSES_PER_PAGE = NR_OF_COLUMNS * NR_OF_ROWS;	
	private final static int ADDRESS_TABLE_WIDTH = 195;
	private final static int ADDRESS_TABLE_HEIGHT = 102;
	private final static int LEFT_MARGIN = 35;
	private final static int TOP_START = 798;

	private SchoolCommuneBusiness business;
	private CommuneUserBusiness userBusiness;
	private Font font = null;
	private String filename = null;
		
	
	/**
	 * Creates the student address labels PDF file.
	 */
	public ICFile createFile(String[] schoolClassIds, IWMainApplication iwma) throws Exception {
		ICFile reportFolder = null;
		ICFileHome fileHome = null;

		try {
			fileHome = (ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class);
			reportFolder = fileHome.findByFileName(REPORT_FOLDER_NAME);
		} catch (FinderException e) {
			try {
				ICFile root = fileHome.findByFileName(ICFileBMPBean.IC_ROOT_FOLDER_NAME);
				reportFolder = fileHome.create();
				reportFolder.setName(REPORT_FOLDER_NAME);
				reportFolder.setMimeType("application/vnd.iw-folder");
				reportFolder.store();
				root.addChild(reportFolder);
			} catch (Exception e2) {
				System.out.println(e2);
				return null;
			}
		} catch (IDOLookupException e) {
			System.out.println(e);
			return null;
		}

		ICFile exportFile = null;
		
		try {
			MemoryFileBuffer buffer = getPDFBuffer(schoolClassIds, iwma);
			MemoryInputStream mis = new MemoryInputStream(buffer);

			try {
				exportFile = fileHome.findByFileName(filename);
				if (exportFile != null) {
					exportFile.remove();
				}
			} catch (FinderException e) {}

			exportFile = fileHome.create();
			exportFile.setFileValue(mis);
			exportFile.setMimeType("application/pdf");
			exportFile.setName(filename);
			exportFile.setFileSize(buffer.length());
			exportFile.store();
			
			reportFolder.addChild(exportFile);
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}
		return exportFile;
	}

	/**
	 * Creates PDF address labels for the specified school classes.   
	 */
	protected MemoryFileBuffer getPDFBuffer(String[] schoolClassIds, IWMainApplication iwma) throws Exception {
		business = getSchoolCommuneBusiness(iwma.getIWApplicationContext());
		userBusiness = getCommuneUserBusiness(iwma.getIWApplicationContext());

		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter writer = PdfWriter.getInstance(document, mos);

		document.addTitle("Student address labels");
		document.addAuthor("Idega Reports");
		document.addSubject("Student address labels");
		document.open();
		
		font = new Font(Font.HELVETICA, 9, Font.BOLD);
		
		int studentCount = 0;

		for (int i = 0; i < schoolClassIds.length; i++) {
			SchoolClass schoolClass = business.getSchoolBusiness().findSchoolClass(new Integer(schoolClassIds[i]));
			School school = schoolClass.getSchool();
			filename = "student_address_labels_" + school.getPrimaryKey() + ".pdf";
			Collection students = business.getSchoolBusiness().findStudentsInClass(((Integer)schoolClass.getPrimaryKey()).intValue());
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				if (studentCount > 0 && studentCount % NR_OF_ADDRESSES_PER_PAGE == 0) {
					document.newPage();
				}
				SchoolClassMember student = (SchoolClassMember) iter.next();
				addAddress(writer, student, studentCount++);
			}
		}

		if (studentCount == 0) {
			throw new Exception("No students.");
		}
		
		document.close();

		writer.setPdfVersion(PdfWriter.VERSION_1_2);
		buffer.setMimeType("application/pdf");
		return buffer;
	}
	
	/**
	 * Adds a student address to the specified document. 
	 */
	protected void addAddress(PdfWriter writer, SchoolClassMember member, int studentCount) throws RemoteException {

		User student = member.getStudent();
		Address address = userBusiness.getUsersMainAddress(student);
		PostalCode postalCode = address != null ? address.getPostalCode() : null;
		
		String name = student.getFirstName() + " " + student.getLastName();
		String streetAddress = address != null ? address.getStreetAddress() : "";
		String postalAddress = "";
		if (address != null && postalCode != null) {
			String zip = postalCode.getPostalCode();
			if (zip.length() > 4) {
				zip = zip.substring(0, 3) + " " + zip.substring(3, 5);
			}
			postalAddress = postalCode.getName() + "  " + zip;
		}

		PdfPTable table = new PdfPTable(1);
		table.setTotalWidth(ADDRESS_TABLE_WIDTH);
		table.getDefaultCell().setPadding(3);
		PdfPCell cell;

		cell = new PdfPCell(new Phrase(name, font));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(streetAddress, font));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(postalAddress, font));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		int row = (studentCount / NR_OF_COLUMNS) % NR_OF_ROWS;
		int column = studentCount % NR_OF_COLUMNS;
		table.writeSelectedRows(0, -1, LEFT_MARGIN + column * ADDRESS_TABLE_WIDTH, 
				TOP_START - row * ADDRESS_TABLE_HEIGHT, writer.getDirectContent());
	}
	
	protected SchoolCommuneBusiness getSchoolCommuneBusiness(IWApplicationContext iwc) throws RemoteException {
		return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);	
	}

	protected CommuneUserBusiness getCommuneUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);	
	}
}