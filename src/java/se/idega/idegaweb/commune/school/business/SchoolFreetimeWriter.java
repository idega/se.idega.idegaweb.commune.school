package se.idega.idegaweb.commune.school.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.data.SchoolChoice;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class SchoolFreetimeWriter implements MediaWritable {

	private MemoryFileBuffer buffer = null;
	private SchoolCommuneBusiness business;
	//private CommuneUserBusiness userBusiness;
	private Locale locale;
	private Map students;
	private Map addresses;
	private Map phones;
	private IWResourceBundle iwrb;

	public final static String prmSchoolId = "school_id";
	public final static String prmSchoolSeasonID = "school_season_id";
	public final static String prmPrintType = "print_type";
	public final static String XLS = "xls";
	public final static String PDF = "pdf";
	private School school;
	
	public SchoolFreetimeWriter() {
	}
	
	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			locale = iwc.getApplicationSettings().getApplicationLocale();
			business = getSchoolCommuneBusiness(iwc);
			//userBusiness = getCommuneUserBusiness(iwma.getIWApplicationContext());
			iwrb = iwc.getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
			
			if (req.getParameter(prmSchoolId) != null && req.getParameter(prmSchoolSeasonID) != null) {
				school = business.getSchoolBusiness().getSchool(new Integer(req.getParameter(prmSchoolId)));
				Collection choices = business.getSchoolChoiceBusiness().findBySchoolAndFreeTime(Integer.parseInt(req.getParameter(prmSchoolId)),Integer.parseInt(req.getParameter(prmSchoolSeasonID)),true);
				students = business.getUserMapFromChoices(choices);
				addresses = business.getUserAddressMapFromChoicesUserIdPK(choices);
				phones = business.getUserPhoneMapFromChoicesUserIdPK(choices);

				List ordered = new Vector(choices);
		
				Collections.sort(ordered,new SchoolChoiceComparator(SchoolChoiceComparator.NAME_SORT,null,null,students,addresses));
		
				String type = req.getParameter(prmPrintType);
				if (type.equals(PDF)) {
					buffer = writePDF(ordered);
				}
				else if (type.equals(XLS)) {
					buffer = writeXLS(ordered);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMimeType() {
		if (buffer != null)
			return buffer.getMimeType();
		return "application/pdf";
	}
	
	public void writeTo(OutputStream out) throws IOException {
		if (buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else
			System.err.println("buffer is null");
	}
	
	public MemoryFileBuffer writeXLS(List list) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		if (!list.isEmpty()) {			
	    HSSFWorkbook wb = new HSSFWorkbook();
	    HSSFSheet sheet = wb.createSheet(school.getName());
	    sheet.setColumnWidth((short)0, (short) (30 * 256));
	    sheet.setColumnWidth((short)1, (short) (14 * 256));
	    sheet.setColumnWidth((short)2, (short) (30 * 256));
	    sheet.setColumnWidth((short)3, (short) (14 * 256));
	    HSSFFont font = wb.createFont();
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    HSSFCellStyle style = wb.createCellStyle();
	    style.setFont(font);

	    HSSFRow row = sheet.createRow(0);
	    HSSFCell cell = row.createCell((short)0);
	    cell.setCellValue(iwrb.getLocalizedString("school.name","Name"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short)1);
	    cell.setCellValue(iwrb.getLocalizedString("school.personal_id","Personal ID"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short)2);
	    cell.setCellValue(iwrb.getLocalizedString("school.address","Address"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short)3);
	    cell.setCellValue(iwrb.getLocalizedString("school.phone","Phone"));
	    cell.setCellStyle(style);

			User student;
			Address address;
			Phone phone;
			SchoolChoice studentMember;
			
			int cellRow = 1;
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				row = sheet.createRow(cellRow);
				studentMember = (SchoolChoice) iter.next();
				student = (User) students.get(new Integer(studentMember.getChildId()));
				address = (Address) addresses.get(new Integer(studentMember.getChildId()));
				phone = (Phone) phones.get(new Integer(studentMember.getChildId()));

				Name name = new Name(student.getFirstName(), student.getMiddleName(), student.getLastName());
		    row.createCell((short)0).setCellValue(name.getName(locale, true));
		    row.createCell((short)1).setCellValue(PersonalIDFormatter.format(student.getPersonalID(), locale));
		    if (address != null)
			    row.createCell((short)2).setCellValue(address.getStreetAddress());
			  if (phone != null)
			    row.createCell((short)3).setCellValue(phone.getNumber());
			  cellRow++;
			}
			wb.write(mos);
		}
		buffer.setMimeType("application/x-msexcel");
		return buffer;
	}
	
	public MemoryFileBuffer writePDF(List list) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		if (!list.isEmpty()) {
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter writer = PdfWriter.getInstance(document, mos);
			document.addTitle(school.getName());
			document.addAuthor("Idega Reports");
			document.addSubject(school.getName());
			document.open();
			
			User student;
			Address address;
			Phone phone;
			SchoolChoice studentMember;
			Cell cell;
			
			String[] headers = {iwrb.getLocalizedString("school.name","Name"), iwrb.getLocalizedString("school.personal_id","Personal ID"), iwrb.getLocalizedString("school.address","Address"), iwrb.getLocalizedString("school.phone","Phone")};
			int[] sizes = { 35, 20, 35, 10 };

			Table datatable = getTable(headers, sizes);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				studentMember = (SchoolChoice) iter.next();
				student = (User) students.get(new Integer(studentMember.getChildId()));
				address = (Address) addresses.get(new Integer(studentMember.getChildId()));
				phone = (Phone) phones.get(new Integer(studentMember.getChildId()));

				Name name = new Name(student.getFirstName(), student.getMiddleName(), student.getLastName());
				cell = new Cell(new Phrase(name.getName(locale, true), new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				cell = new Cell(new Phrase(PersonalIDFormatter.format(student.getPersonalID(), locale), new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				String streetAddress = "";
				if (address != null)
					streetAddress = address.getStreetAddress();
				cell = new Cell(new Phrase(streetAddress, new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				String phoneNumber = "";
				if (phone != null)
					phoneNumber = phone.getNumber();
				cell = new Cell(new Phrase(phoneNumber, new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				if (!writer.fitsPage(datatable)) {
					datatable.deleteLastRow();
					document.add(datatable);
					document.newPage();
					datatable = getTable(headers, sizes);
				}
			}
			document.add(datatable);
			document.close();
			writer.setPdfVersion(PdfWriter.VERSION_1_2);
		}
		
		buffer.setMimeType("application/pdf");
		return buffer;
	}
	
	private Table getTable(String[] headers, int[] sizes) throws BadElementException, DocumentException {
		Table datatable = new Table(headers.length);
		datatable.setPadding(0.0f);
		datatable.setSpacing(0.0f);
		datatable.setBorder(Rectangle.NO_BORDER);
		datatable.setWidth(100);
		if (sizes != null)
			datatable.setWidths(sizes);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = new Cell(new Phrase(headers[i], new Font(Font.HELVETICA, 12, Font.BOLD)));
			cell.setBorder(Rectangle.BOTTOM);
			datatable.addCell(cell);
		}
		datatable.setDefaultCellBorderWidth(0);
		datatable.setDefaultCellBorder(Rectangle.NO_BORDER);
		datatable.setDefaultRowspan(1);
		return datatable;
	}

	protected SchoolCommuneBusiness getSchoolCommuneBusiness(IWApplicationContext iwc) throws RemoteException {
		return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);	
	}

	protected CommuneUserBusiness getCommuneUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);	
	}
}