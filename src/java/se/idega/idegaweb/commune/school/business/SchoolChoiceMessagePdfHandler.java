/*
 * Created on Jan 8, 2004
 *
 */
package se.idega.idegaweb.commune.school.business;


import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.business.MessagePdfHandler;
import se.idega.idegaweb.commune.message.data.PrintMessage;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.printing.business.CommuneUserTagMap;
import se.idega.idegaweb.commune.printing.business.ContentCreationException;
import se.idega.idegaweb.commune.printing.business.DocumentBusiness;
import se.idega.idegaweb.commune.printing.business.DocumentPrintContext;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import com.idega.block.process.data.Case;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.component.business.BundleRegistrationListener;
import com.idega.core.component.business.RegisterException;
import com.idega.core.component.data.ICObject;
import com.idega.idegaweb.IWBundle;
import com.idega.user.data.User;
import com.lowagie.text.Document;
import com.lowagie.text.ElementTags;
import com.lowagie.text.xml.SAXmyHandler;
import com.lowagie.text.xml.XmlPeer;

/**
 * SchoolChoiceMessagePdfHandler
 * @author aron 
 * @version 1.0
 */
public class SchoolChoiceMessagePdfHandler implements MessagePdfHandler,BundleRegistrationListener {
	
	public final static String CODE_OLD_SCHOOL_CHANGE ="OSCCHNG";
	public final static String CODE_NEW_SCHOOL_CHANGE ="NSCCHNG";
	public final static String CODE_SCHOOL_MOVEAWAY ="SCHMVAW";
	//public final static String CODE_SEPERATED_CHANGE ="SPPCHNG";
	public final static String CODE_APPLYING_SINGLEPARENT_APPLICATION_NEW ="APAPNEW";
	public final static String CODE_APPLYING_SINGLEPARENT_APPLICATION_CHANGE = "APAPCHG";
	public final static String CODE_NONAPPLYING_SINGLEPARENT_APPLICATION_NEW ="PRAPNEW";
	public final static String CODE_NONAPPLYING_SINGLEPARENT_APPLICATION_CHANGE = "PAPCHNG";
	public final static String CODE_CHILD_SELF_APPLICATION_CHANGE = "CHLDCHG";
	public final static String CODE_CHILD_SELF_APPLICATION_NEW = "CHLDNEW";
	public final static String CODE_APPLICATION_REJECT ="APREJCT";
	public final static String CODE_APPLICATION_REACTIVATE ="APREACT";
	public final static String CODE_PRELIMINARY ="PRELIMI";
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.message.business.MessagePdfHandler#createMessageContent(se.idega.idegaweb.commune.printing.business.DocumentPrintContext)
	 */
	public void createMessageContent(DocumentPrintContext dpc) throws ContentCreationException {
		String code =dpc.getMessage().getContentCode();
		if(code!=null){
		if(CODE_PRELIMINARY.equals(code)){
			createPreliminaryContent(dpc,code);
		}
		else if(CODE_APPLYING_SINGLEPARENT_APPLICATION_NEW.equals(code)){
			createApplyingSeparateParentApplicationContent(dpc,code);
		}
		else if(CODE_APPLYING_SINGLEPARENT_APPLICATION_CHANGE.equals(code)){
			createApplyingSeparateParentChangeContent(dpc,code);
		}
		else if(CODE_NONAPPLYING_SINGLEPARENT_APPLICATION_NEW.equals(code)){
			createNonApplyingSeparateParentApplicationContent(dpc,code);
		}
		else if(CODE_NONAPPLYING_SINGLEPARENT_APPLICATION_CHANGE.equals(code)){
			createNonApplyingSeparateParentChangeContent(dpc,code);
		}
		else if(CODE_CHILD_SELF_APPLICATION_CHANGE.equals(code)){
			createChildSelfApplicationContent(dpc,code);
		}
		else if(CODE_CHILD_SELF_APPLICATION_NEW.equals(code)){
			createChildSelfChangeContent(dpc,code);
		}
		else if(CODE_APPLICATION_REACTIVATE.equals(code)){
			createReactivateContent(dpc,code);
		}
		else if(CODE_OLD_SCHOOL_CHANGE.equals(code)){
			createOldHeadmasterContent(dpc,code);
		}
		else if(CODE_NEW_SCHOOL_CHANGE.equals(code)){
			createNewHeadmasterContent(dpc,code);
		}
		else{
			createDefaultContent(dpc);
		}}
		else{
		createDefaultContent(dpc);
		}
	}
	
	private void createPreliminaryContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, getHandlerCode()+"_"+code+"_letter.xml");
	}
	private void createReactivateContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, getHandlerCode()+"_"+code+"_letter.xml");
	}
	
	private void createApplyingSeparateParentApplicationContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, getHandlerCode()+"_"+code+"_letter.xml");
	}
	
	private void createApplyingSeparateParentChangeContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, getHandlerCode()+"_"+code+"_letter.xml");
	}
	
	private void createNonApplyingSeparateParentApplicationContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, getHandlerCode()+"_"+code+"_letter.xml");
	}
	
	private void createNonApplyingSeparateParentChangeContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, getHandlerCode()+"_"+code+"_letter.xml");
	}
	
	private void createOldHeadmasterContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, getHandlerCode()+"_"+code+"_letter.xml");
	}
	
	private void createNewHeadmasterContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, getHandlerCode()+"_"+code+"_letter.xml");
	}
	
	private void createChildSelfApplicationContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, getHandlerCode()+"_"+code+"_letter.xml");
	}
	
	private void createChildSelfChangeContent(DocumentPrintContext dpc,String code)throws ContentCreationException{
		createContent( dpc, getHandlerCode()+"_"+code+"_letter.xml");
	}
	
	private void createDefaultContent(DocumentPrintContext dpc)throws ContentCreationException{
		createContent(dpc,"default_letter.xml");
	}
	
	private void createContent(DocumentPrintContext dpc,String xml)throws ContentCreationException{
		try {
			System.out.println("Creating pdf using layout \"" + xml + "\"");
			
			DocumentBusiness docBuiz =getDocumentBusiness(dpc);
			
			Document document =dpc.getDocument();
			PrintMessage msg = dpc.getMessage();
			User owner = msg.getOwner();
			Locale locale = dpc.getLocale();
			docBuiz.createDefaultLetterHeader(dpc.getDocument(),docBuiz.getAddressString(owner),dpc.getPdfWriter());
			IWBundle iwb = dpc.getIWApplicationContext().getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
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
			if(parentCase.getCaseCode().toString().equals(schoolChoiceService.getSchoolChoiceCaseCode())){
				SchoolChoice choice = schoolChoiceService.getSchoolChoiceHome().findByPrimaryKey(parentCase.getPrimaryKey());
				SchoolChoice choice1=null,choice2=null,choice3=null;
				List choices =schoolChoiceService.getConnectedSchoolchoices(choice);
				int size =choices.size();
				if(size>=1)
					choice1 = (SchoolChoice)choices.get(0);
				if(size>=2)
					choice2 = (SchoolChoice)choices.get(1);
				if(size>=3)
					choice3 = (SchoolChoice)choices.get(2);
				
				
				//DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,dpc.getLocale());
				XmlPeer peer = new XmlPeer(ElementTags.CHUNK, "mbskolv_season");
				peer.setContent(choice.getSchoolSeason().getName());
				tagmap.put(peer.getAlias(), peer);
				
				peer = new XmlPeer(ElementTags.CHUNK, "mbskolv_school");
				peer.setContent(choice.getChosenSchool().getName());
				tagmap.put(peer.getAlias(), peer);
				
				peer = new XmlPeer(ElementTags.CHUNK, "mbskolv_child_name");
				peer.setContent(choice.getChild().getName());
				tagmap.put(peer.getAlias(), peer);
				
				peer = new XmlPeer(ElementTags.CHUNK, "mbskolv_child_personalid");
				peer.setContent(choice.getChild().getPersonalID());
				tagmap.put(peer.getAlias(), peer);
				
				if(choice1!=null){
					peer = new XmlPeer(ElementTags.CHUNK, "mbskolv_first_school");
					peer.setContent(choice1.getChosenSchool().getName());
					tagmap.put(peer.getAlias(), peer);
				}
				
				if(choice2!=null){
					peer = new XmlPeer(ElementTags.CHUNK, "mbskolv_second_school");
					peer.setContent(choice2.getChosenSchool().getName());
					tagmap.put(peer.getAlias(), peer);
				}
				if(choice3!=null){
					peer = new XmlPeer(ElementTags.CHUNK, "mbskolv_third_school");
					peer.setContent(choice3.getChosenSchool().getName());
					tagmap.put(peer.getAlias(), peer);
				}
				
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
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.message.business.MessagePdfHandler#getHandlerCode()
	 */
	public String getHandlerCode() {
		return SchoolConstants.SCHOOL_CHOICE_CASE_CODE_KEY;
	}

	

	

	/* (non-Javadoc)
	 * @see com.idega.core.component.business.BundleRegistrationListener#registerInBundle(com.idega.idegaweb.IWBundle, com.idega.core.component.data.ICObject)
	 */
	public void registerInBundle(IWBundle bundle, ICObject ico) throws RegisterException {
		try {
			MessageBusiness msgBuiz =(MessageBusiness) IBOLookup.getServiceInstance(bundle.getApplication().getIWApplicationContext(),MessageBusiness.class);
			msgBuiz.createMessageHandlerInfo(this,ico);
		}
		catch (Exception e) {
			throw new RegisterException(e);
		}
	}

}
