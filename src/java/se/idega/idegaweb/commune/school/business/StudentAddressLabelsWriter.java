/*
 * $Id: StudentAddressLabelsWriter.java,v 1.7 2004/10/20 15:46:25 aron Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import se.idega.idegaweb.commune.Constants;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileBMPBean;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
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
 * Last modified: $Date: 2004/10/20 15:46:25 $ by $Author: aron $
 *
 * @author Anders Lindman
 * @version $Revision: 1.7 $
 * @see com.idega.io.MediaWritable
 */
public class StudentAddressLabelsWriter {

	private final static String REPORT_FOLDER_NAME = "Export Files";

	private final static int NR_OF_COLUMNS = 3;
	private final static int NR_OF_ROWS = 8;
	private final static int NR_OF_ADDRESSES_PER_PAGE = NR_OF_COLUMNS * NR_OF_ROWS;	
	private final static int ADDRESS_TABLE_WIDTH = 195;
	private final static int ADDRESS_TABLE_HEIGHT = 101;
	private final static int LEFT_MARGIN = 35;
	private final static int TOP_START = 802;

	private final static String KP = "sal.";
	
	private final static String KEY_TO_CUSTODIAN_FOR = KP + "to_custodian_for";
	
	private SchoolCommuneBusiness business;
	private CommuneUserBusiness userBusiness;
	private Font font = null;
	private String filename = null;
	
	private static final String MIME_PDF = "application/pdf";
	private static final String MIME_XLS = "application/vnd.ms-excel";
		
	
	/**
	 * Creates the student address labels PDF file.
	 */
	public ICFile createFile(String[] schoolClassIds, IWMainApplication iwma) throws Exception {
	    getSchoolCommuneBusiness(iwma.getIWApplicationContext());
	    getCommuneUserBusiness(iwma.getIWApplicationContext());
	    Collection receivers = getReceivers(schoolClassIds);
	    MemoryFileBuffer buffer = getPDFBuffer(iwma.getIWApplicationContext(),receivers);
	    return createFile(iwma.getIWApplicationContext(),buffer);
	}
	
	/**
	 * Creates a address label PDF file for a collection of MailReceiver objects
	 * @param mailReceivers
	 * @return
	 * @throws Exception
	 */
	public ICFile createPDFFile(IWApplicationContext iwac,Collection mailReceivers,String fileName) throws Exception{
	    this.filename = fileName;
	    return createFile(iwac,getPDFBuffer(iwac,mailReceivers));
	}
	
	public ICFile createXLSFile(IWApplicationContext iwac,Collection mailReceivers,String fileName)throws Exception{
	    this.filename = fileName;
	    return createFile(iwac,getXLSBuffer(iwac,mailReceivers));
	}
	
	private ICFile createFile(IWApplicationContext iwac,MemoryFileBuffer buffer)throws Exception{
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
		    
			
			MemoryInputStream mis = new MemoryInputStream(buffer);

			try {
				exportFile = fileHome.findByFileName(filename);
				if (exportFile != null) {
					exportFile.remove();
				}
			} catch (FinderException e) {}

			exportFile = fileHome.create();
			
			IWBundle iwb = iwac.getIWMainApplication().getBundle(Constants.IW_BUNDLE_IDENTIFIER);
			if(iwb.getProperty("LabelWriter.dumpFileToCacheFolder")!=null){
				String folder = iwac.getIWMainApplication().getRealPath(iwac.getIWMainApplication().getCacheDirectoryURI()+"/prints");
				java.io.File tfile = com.idega.util.FileUtil.getFileAndCreateIfNotExists(folder,filename);
				java.io.FileOutputStream fos = new java.io.FileOutputStream(tfile);
				java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
				while (mis.available() > 0) {
					baos.write(mis.read());
				}
				baos.writeTo(fos);
				baos.flush();
		    		baos.close();
		    		mis.reset();
			}
			
			exportFile.setFileValue(mis);
			exportFile.setMimeType(buffer.getMimeType());
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
	
	private Collection getReceivers(String[] schoolClassIds) throws NumberFormatException, RemoteException, FinderException{
	    ArrayList receivers = new ArrayList();
	    Collection students;
	    SchoolClass schoolClass = null;
	    
	    SchoolClassMember student;
	    MailReceiver receiver;
	    Integer classID;
	    SchoolBusiness schoolBuiz = business.getSchoolBusiness();
	    for (int i = 0; i < schoolClassIds.length; i++) {
			 //schoolClass = business.getSchoolBusiness().findSchoolClass(new Integer(schoolClassIds[i]));
			 classID = Integer.valueOf(schoolClassIds[i]);
			 students = schoolBuiz.findStudentsInClass(classID.intValue());
			 Iterator iter = students.iterator();
			 while (iter.hasNext()) {
			     student = (SchoolClassMember) iter.next();
			     receiver = new MailReceiver(null,userBusiness,new Integer(student.getClassMemberId()));
			     receivers.add(receiver);
			 }
	    }
	    if(schoolClass!=null)
	        filename = "student_address_labels_"+schoolClass.getSchoolId()+".pdf";
	    else
	        filename = "student_address_labels_##.pdf";
	    return receivers;
			
	}

	/**
	 * Creates PDF address labels for the specified school classes.   
	 */
	protected MemoryFileBuffer getPDFBuffer(IWApplicationContext iwac,Collection receivers) throws Exception {
		business = getSchoolCommuneBusiness(iwac);
		userBusiness = getCommuneUserBusiness(iwac);
		
		IWResourceBundle iwrb = iwac.getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(
				iwac.getApplicationSettings().getApplicationLocale());

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

		Iterator iter = receivers.iterator();
		while (iter.hasNext()) {
		  if (studentCount > 0 && studentCount % NR_OF_ADDRESSES_PER_PAGE == 0) {
			document.newPage();
		  }
				
			addAddress(writer, iwrb, (MailReceiver)iter.next(), studentCount++);
		 }
		

		if (studentCount == 0) {
			throw new Exception("No students.");
		}
		
		document.close();

		writer.setPdfVersion(PdfWriter.VERSION_1_2);
		buffer.setMimeType(MIME_PDF);
		return buffer;
	}
	
	/**
	 * Adds a student address to the specified document. 
	 */
	protected void addAddress(PdfWriter writer, IWResourceBundle iwrb, MailReceiver student, int studentCount) throws RemoteException {

		//User student = member.getStudent();
		//Address address = userBusiness.getUsersMainAddress(student);
		//PostalCode postalCode = address != null ? address.getPostalCode() : null;
		
		//String name = student.getFirstName() + " " + student.getLastName();
		//String streetAddress = address != null ? address.getStreetAddress() : "";
		//String postalAddress = "";
		//if (address != null && postalCode != null) {
		//	String zip = postalCode.getPostalCode();
		//	if (zip.length() > 4) {
		//		zip = zip.substring(0, 3) + " " + zip.substring(3, 5);
		//	}
		//	postalAddress = zip + "  " + postalCode.getName();
		//}

		PdfPTable table = new PdfPTable(1);
		table.setTotalWidth(ADDRESS_TABLE_WIDTH);
		table.getDefaultCell().setPadding(3);
		PdfPCell cell;

		cell = new PdfPCell(new Phrase(iwrb.getLocalizedString(KEY_TO_CUSTODIAN_FOR, "To custodian for") + ":", font));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(student.getStudentName(), font));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(student.getStreetAddress(), font));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(student.getPostalAddress(), font));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		int row = (studentCount / NR_OF_COLUMNS) % NR_OF_ROWS;
		int column = studentCount % NR_OF_COLUMNS;
		table.writeSelectedRows(0, -1, LEFT_MARGIN + column * ADDRESS_TABLE_WIDTH, 
				TOP_START - row * ADDRESS_TABLE_HEIGHT, writer.getDirectContent());
	}
	
	protected MemoryFileBuffer getXLSBuffer(IWApplicationContext iwac,Collection receivers) throws Exception {
	    business = getSchoolCommuneBusiness(iwac);
		userBusiness = getCommuneUserBusiness(iwac);
		
		//IWResourceBundle iwrb = iwac.getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(
		//		iwac.getApplicationSettings().getApplicationLocale());

		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);
		
		HSSFWorkbook wb = new HSSFWorkbook();
	    HSSFSheet sheet = wb.createSheet();
	    short colWidth = (short) (30 * 256);
	 
	    sheet.setColumnWidth((short)0, colWidth);
	    sheet.setColumnWidth((short)1, colWidth);
	    sheet.setColumnWidth((short)2, colWidth);
	    
	    MailReceiver receiver;
	    HSSFRow row;
	    Iterator iter = receivers.iterator();
	    int rowCount = 0;
	    int col = 0;
		while (iter.hasNext()) {
		   receiver = (MailReceiver) iter.next();
		   row = sheet.getRow(rowCount);
		   if(row==null)
		       row = sheet.createRow(rowCount);
		   row.createCell((short)col).setCellValue(receiver.getStudentName());
		   row = row = sheet.getRow(rowCount+1);
		   if(row==null)
		       row = sheet.createRow(rowCount+1);
		   row.createCell((short)col).setCellValue(receiver.getStreetAddress());
		   row = row = sheet.getRow(rowCount+2);
		   if(row==null)
		       row = sheet.createRow(rowCount+2);
		   row.createCell((short)col).setCellValue(receiver.getPostalAddress());
		   
		   col++;
		   if(col==NR_OF_COLUMNS){
		       col = 0;
		       rowCount+=4; 
		   }
		  
		  
		}
	    wb.write(mos);
		
		buffer.setMimeType(MIME_XLS);
		return buffer;
	    
	}
	
	protected SchoolCommuneBusiness getSchoolCommuneBusiness(IWApplicationContext iwc) throws RemoteException {
	    if(business==null)
	        business =  (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);
	    return business;
	}

	protected CommuneUserBusiness getCommuneUserBusiness(IWApplicationContext iwc) throws RemoteException {
	    if(userBusiness==null)
	        userBusiness= (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);	
	    return userBusiness;
	}
}