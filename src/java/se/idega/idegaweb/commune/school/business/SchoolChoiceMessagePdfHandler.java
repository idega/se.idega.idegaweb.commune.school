/*
 * Created on Jan 8, 2004
 *
 */
package se.idega.idegaweb.commune.school.business;


import java.rmi.RemoteException;

import java.util.HashMap;
import java.util.Locale;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.idega.block.process.data.Case;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.user.data.User;
import com.idega.idegaweb.IWBundle;

import com.lowagie.text.Document;
import com.lowagie.text.ElementTags;

import com.lowagie.text.xml.SAXmyHandler;
import com.lowagie.text.xml.XmlPeer;

import se.idega.idegaweb.commune.message.business.MessagePdfHandler;
import se.idega.idegaweb.commune.message.data.PrintMessage;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.printing.business.CommuneUserTagMap;
import se.idega.idegaweb.commune.printing.business.ContentCreationException;
import se.idega.idegaweb.commune.printing.business.DocumentBusiness;
import se.idega.idegaweb.commune.printing.business.DocumentPrintContext;
import se.idega.idegaweb.commune.school.data.SchoolChoice;

/**
 * SchoolChoiceMessagePdfHandler
 * @author aron 
 * @version 1.0
 */
public class SchoolChoiceMessagePdfHandler implements MessagePdfHandler {
	
	public final static String CODE_OLD_SCHOOL_CHANGE ="OSCCHNG";
	public final static String CODE_NEW_SCHOOL_CHANGE ="NSCCHNG";
	public final static String CODE_SCHOOL_MOVEAWAY ="SCHMVAW";
	//public final static String CODE_SEPERATED_CHANGE ="SPPCHNG";
	public final static String CODE_SINGLEPARENT_APPLICATION_NEW ="PRAPNEW";
	public final static String CODE_SINGLEPARENT_APPLICATION_CHANGE = "PAPCHNG";
	public final static String CODE_APPLICATION_REJECT ="APREJCT";
	public final static String CODE_PRELIMINARY ="PRELIMI";
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.message.business.MessagePdfHandler#createMessageContent(se.idega.idegaweb.commune.printing.business.DocumentPrintContext)
	 */
	public void createMessageContent(DocumentPrintContext dpc) throws ContentCreationException {
		String code =dpc.getMessage().getContentCode();
		
		if(CODE_PRELIMINARY.equals(code)){
			createPreliminaryContent(dpc,code);
		}
		else if(CODE_SINGLEPARENT_APPLICATION_NEW.equals(code)){
			createSeparateParentApplicationContent(dpc,code);
		}
		else if(CODE_SINGLEPARENT_APPLICATION_CHANGE.equals(code)){
			createSeparateParentChangeContent(dpc,code);
		}
		else if(CODE_OLD_SCHOOL_CHANGE.equals(code)){
			createOldHeadmasterContent(dpc,code);
		}
		else if(CODE_NEW_SCHOOL_CHANGE.equals(code)){
			createNewHeadmasterContent(dpc,code);
		}
		else{
			createDefaultContent(dpc);
		}
		
	}
	
	
	
	private void createPreliminaryContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, "MBSKOLV_"+code+"_letter.xml");
	}
	
	private void createSeparateParentApplicationContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, "MBSKOLV_"+code+"_letter.xml");
	}
	
	private void createSeparateParentChangeContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, "MBSKOLV_"+code+"_letter.xml");
	}
	
	private void createOldHeadmasterContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, "MBSKOLV_"+code+"_letter.xml");
	}
	
	private void createNewHeadmasterContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, "MBSKOLV_"+code+"_letter.xml");
	}
	
	private void createDefaultContent(DocumentPrintContext dpc)throws ContentCreationException{
		createContent(dpc,"default_letter.xml");
	}
	
	private void createContent(DocumentPrintContext dpc,String xml)throws ContentCreationException{
		try {
			DocumentBusiness docBuiz =getDocumentBusiness(dpc);
			
			Document document =dpc.getDocument();
			PrintMessage msg = dpc.getMessage();
			User owner = msg.getOwner();
			Locale locale = dpc.getLocale();
			docBuiz.createDefaultLetterHeader(dpc.getDocument(),docBuiz.getAddressString(owner),dpc.getPdfWriter());
			IWBundle iwb = dpc.getIWApplicationContext().getApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
			//document.add(new Paragraph("\n\n\n\n\n\n\n\n"));
			
			
			
			HashMap tagmap = new CommuneUserTagMap(dpc.getIWApplicationContext(),owner);
			tagmap.putAll(docBuiz.getMessageTagMap(msg,locale));
			tagmap.putAll(getSchoolChoiceTagMap(dpc));
			
			String letterUrl  = docBuiz.getXMLLetterUrl(iwb,locale,xml,true);
			
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			SAXmyHandler handler = new SAXmyHandler(document,tagmap);
			handler.setControlOpenClose(false); 

			parser.parse(letterUrl,handler);

			
			docBuiz.createCommuneFooter(dpc.getPdfWriter());
		}
		
		catch (Exception e) {
			throw new ContentCreationException(e);
		}
	}
	
	private SchoolChoiceBusiness getSchoolChoiceBusiness(DocumentPrintContext dpc) throws IBOLookupException{
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(dpc.getIWApplicationContext(),SchoolChoiceBusiness.class);
	}
	
	private DocumentBusiness getDocumentBusiness(DocumentPrintContext dpc) throws IBOLookupException{
		return (DocumentBusiness) IBOLookup.getServiceInstance(dpc.getIWApplicationContext(),DocumentBusiness.class);
	}
	
	// TODO add more tags
	private HashMap getSchoolChoiceTagMap(DocumentPrintContext  dpc){
		HashMap tagmap =new HashMap();
		PrintMessage msg =dpc.getMessage();
		Case parentCase =msg.getParentCase();
		try {
			SchoolChoiceBusiness schoolChoiceService =getSchoolChoiceBusiness(dpc);
			if(parentCase.getCaseCode().equals(schoolChoiceService.getSchoolChoiceCaseCode())){
				SchoolChoice choice = schoolChoiceService.getSchoolChoiceHome().findByPrimaryKey(parentCase.getPrimaryKey());
				//DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,dpc.getLocale());
				XmlPeer peer = new XmlPeer(ElementTags.CHUNK, "mbskolv_season");
				peer.setContent(choice.getSchoolSeason().getName());
				tagmap.put(peer.getAlias(), peer);
				
				peer = new XmlPeer(ElementTags.CHUNK, "mbskolv_school");
				peer.setContent(choice.getChosenSchool().getName());
				tagmap.put(peer.getAlias(), peer);
				
			}
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		
		return  tagmap;
		
	}
}
