package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import is.idega.idegaweb.member.business.MemberFamilyLogic;
import com.idega.core.data.*;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.text.TextFormat;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolChoiceApprover extends CommuneBlock {

	private IWBundle iwb;
	private IWResourceBundle iwrb;
	private Collection cases;
	private TextFormat tf;
	private UserBusiness userBean;
	private SchoolBusiness schoolBean;
	private SchoolChoiceBusiness choiceBean;
	private MessageBusiness msgBean;
	private DateFormat df;
	private int schoolId = -1;
	private int seasonId = -1;

	private String prmPrelimIds = "prelim_chk_ids";
	private String prmNoRoom = "no_room";
	private String prmGroupChoice = "group_choice";
	private String prmGroupPrms = "group_parameters";
	private String prmPupilInfo = "pupil_info";
	public static String prmSchoolId = "school_id";
	public String prmSave = "appr_save";
	private String prmPupilView = "ppl_vw";
	private boolean pupilList = false;
	private School school;

	public void control(IWContext iwc) throws RemoteException {
		debugParameters(iwc);
		init(iwc);
		parse(iwc);
		String[] statusToSearch = { "UBEH", "PLAC", "PREL" };
		String code = "MBSKOLV";
		if (schoolId > 0) {
			if(iwc.isParameterSet(prmPupilInfo)){
				Integer choiceId = new Integer(iwc.getParameter(prmPupilInfo));
				add(getPupilInfo(iwc,choiceId.intValue()));
			}
			else{
				try {
					seasonId = ((Integer)choiceBean.getCurrentSeason().getPrimaryKey()).intValue();
					school = schoolBean.getSchool(new Integer(schoolId));
					if (pupilList)
						cases = choiceBean.getSchoolChoiceHome().findByCodeAndStatus(code, statusToSearch, schoolId,seasonId,"group_place");
					else
						cases = choiceBean.getSchoolChoiceHome().findByCodeAndStatus(code, statusToSearch, schoolId,seasonId);
				}
				catch (javax.ejb.FinderException ex) {
					ex.printStackTrace();
				}
				if (cases != null){
					if(pupilList){
						add(getPupilList());
					}
					else
						add(getChoiceList());
				}
				
			}
		}
		else {
			add(tf.format(iwrb.getLocalizedString("school_choice.no_school_chosen"), tf.HEADER));
		}

	}

	public void init(IWContext iwc) throws RemoteException {
		
		userBean = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		schoolBean = (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
		choiceBean = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
		msgBean = (MessageBusiness) IBOLookup.getServiceInstance(iwc,MessageBusiness.class);
		if (iwc.isParameterSet(prmSchoolId)) {
			schoolId = Integer.parseInt(iwc.getParameter(prmSchoolId));
			iwc.setSessionAttribute(prmSchoolId,new Integer(schoolId));
		}
		else if(iwc.getSessionAttribute(prmSchoolId)!=null){
			schoolId = ((Integer) iwc.getSessionAttribute(prmSchoolId)).intValue();
		}
		pupilList = iwc.isParameterSet(prmPupilView);
	}
	
	public void parse(IWContext iwc) throws RemoteException {
		// saving preliminaries or placering actions
		if(iwc.isParameterSet(prmSave) && iwc.getParameter(prmSave).equals("true")){
			// preliminary handling
			if(iwc.isParameterSet(prmPrelimIds)){
				String[] ids = iwc.getParameterValues(prmPrelimIds);
				for (int i = 0; i < ids.length; i++) {
					Integer pk = new Integer(ids[i]);
					choiceBean.preliminaryAction(pk);
					// create message
					//msgBean.createUserMessage();
				}
			}
			// placering handling
			if(iwc.isParameterSet(prmGroupPrms)){
				String[] prmNames = iwc.getParameterValues(prmGroupPrms);
				int startIndex = prmGroupChoice.length();
				for (int i = 0; i < prmNames.length; i++) {
					String prm = prmNames[i];
					String val = iwc.getParameter(prm);
					if(!"".equals(val)){
						String pk  = prm.substring(startIndex);
						choiceBean.groupPlaceAction(new Integer(pk),val);
					}
				}
			}
		
		}
		// no room action
		else if(iwc.isParameterSet(prmNoRoom)){
			Integer pk = new Integer(iwc.getParameter(prmNoRoom));
			choiceBean.noRoomAction(pk);
		}
	}

	public PresentationObject getChoiceList() throws RemoteException {
		Form F = new Form();
		DataTable T = new DataTable();
		T.setUseTop(false);
		T.setUseBottom(false);
		T.setUseTitles(false);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.child", "Child"), tf.HEADER), 1, 1);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.grade", "Grade"), tf.HEADER), 2, 1);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.choice_date", "Choice date"), tf.HEADER), 3, 1);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.change_from", "Change from"), tf.HEADER), 4, 1);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.preliminary", "Preliminary"), tf.HEADER), 5, 1);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.not_room", "No room"), tf.HEADER), 7, 1);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.group_place", "Group"), tf.HEADER), 6, 1);

		Iterator iter = cases.iterator();
		SchoolChoice choice;
		User child;
		int row = 2;
		while (iter.hasNext()) {
			choice = (SchoolChoice) iter.next();
			String sid = choice.getPrimaryKey().toString();
			child = userBean.getUser(choice.getChildId());
			T.add(getPupilInfoLink(child,choice), 1, row);
			T.add(tf.format(choice.getGrade()), 2, row);
			T.add(tf.format(df.format(choice.getSchoolChoiceDate())), 3, row);
			if (choice.getChangeOfSchool()) {
				School changeFromSchool = schoolBean.getSchool(new Integer(choice.getCurrentSchoolId()));
				T.add(tf.format(changeFromSchool.getSchoolName()), 4, row);

			}
			String status = choice.getCaseStatus().getStatus();
			// Preliminary part ( check boxes ) Or Denial
			if (status.equals(choice.getCaseStatusCreated())) {
				CheckBox prelimCheck = new CheckBox(prmPrelimIds);
				prelimCheck.setValue(sid);
				T.add(prelimCheck, 5, row);

				SubmitButton denyChoice = new SubmitButton(iwrb.getLocalizedImageButton(" no_room", "No room"), prmNoRoom, sid);
				T.add(denyChoice, 7, row);
			}
			// Class Placing Change to dropdown from Schoolblock class entity
			else if (status.equals(choice.getCaseStatusPreliminary())) {
				String prmName = prmGroupChoice+sid;
				T.add(new HiddenInput(prmGroupPrms,prmName),6,row);
				TextInput groupChoice = new TextInput(prmName);
				groupChoice.setLength(8);
				T.add(groupChoice, 6, row);
			}
			else if (status.equals(choice.getCaseStatusPlaced())) {
				
				T.add(tf.format(choice.getGroupPlace()), 6, row);
			}

			row++;
		}
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),prmSave,"true");
		Link L  = new Link(iwrb.getLocalizedImageButton("show_pupil_list","Show pupillist"));
		L.addParameter(prmSchoolId,this.schoolId);
		L.addParameter(prmPupilView,"true");
		T.add(new HiddenInput(prmSchoolId,String.valueOf( this.schoolId) ) );
		T.addButton(save);
		T.addButton(L);
		F.add(getSchoolInfo());
		F.add(T);
		return F;
	}
	
	public PresentationObject getPupilList() throws RemoteException {
		Form F = new Form();
		DataTable T = new DataTable();
		T.setUseTop(false);
		T.setUseBottom(false);
		T.setUseTitles(false);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.child", "Child"), tf.HEADER), 1, 1);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.group_place", "Group"), tf.HEADER), 2, 1);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.choice_date", "Choice date"), tf.HEADER), 3, 1);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.change_from", "Change from"), tf.HEADER), 4, 1);
		T.add(tf.format(iwrb.getLocalizedString("school_choice.regroup_place", "Regroup"), tf.HEADER), 5, 1);

		Iterator iter = cases.iterator();
		SchoolChoice choice;
		User child;
		int row = 2;
		while (iter.hasNext()) {
			choice = (SchoolChoice) iter.next();
			String sid = choice.getPrimaryKey().toString();
			child = userBean.getUser(choice.getChildId());
			String status = choice.getCaseStatus().getStatus();
			if (status.equals(choice.getCaseStatusPlaced())) {
				T.add(getPupilInfoLink(child,choice), 1, row);
				T.add(tf.format(choice.getGroupPlace()), 2, row);
				T.add(tf.format(df.format(choice.getSchoolChoiceDate())), 3, row);
				if (choice.getChangeOfSchool()) {
					School changeFromSchool = schoolBean.getSchool(new Integer(choice.getCurrentSchoolId()));
					T.add(tf.format(changeFromSchool.getSchoolName()), 4, row);
	
				}
				String prmName = prmGroupChoice+sid;
				T.add(new HiddenInput(prmGroupPrms,prmName),5,row);
				TextInput groupChoice = new TextInput(prmName);
				groupChoice.setLength(8);
				T.add(groupChoice, 5, row);
			
			}

			row++;
		}
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),prmSave,"true");
		
		Link L = new Link(iwrb.getLocalizedString("back","Back"));
		L.addParameter(prmSchoolId,this.schoolId);
		T.add(new HiddenInput(prmPupilView,"true"));
		T.add(new HiddenInput(prmSchoolId,String.valueOf( this.schoolId) ) );
		
		T.addButton(save);
		T.addButton(L);
		F.add(T);
		return F;
	}
	
	
	public PresentationObject getPupilInfo(IWContext iwc,int choiceId)throws RemoteException{
		SchoolChoice choice = null;
		try {
			choice = choiceBean.getSchoolChoice(choiceId);
		}
		catch (FinderException e) {
		}
		Table T = new Table();
		if(choice!=null){
			User child = userBean.getUser(choice.getChildId());
			
			int row = 1;
			T.add(tf.format(iwrb.getLocalizedString("child","Child"),tf.HEADER),1,row);		
			T.add(tf.format(iwrb.getLocalizedString("address","Address"),tf.HEADER),2,row);
			row++;
			T.add(tf.format(child.getNameLastFirst()),1,row);
			Collection addresses  = child.getAddresses();
			if(!addresses.isEmpty()){
				Iterator iter = addresses.iterator();
				if(iter.hasNext()){
					Address addr = (Address) iter.next();
					T.add(tf.format(addr.toString()),2,row);
				}
			}
			row++;
			MemberFamilyLogic ml = (MemberFamilyLogic) IBOLookup.getServiceInstance(iwc,MemberFamilyLogic.class);
	       
			Collection parents = null;
			try {
				parents = ml.getCustodiansFor(child);
				if(!parents.isEmpty()){
	        		Iterator piter = parents.iterator();
	        		int count = 1;
	        		while(piter.hasNext()){
	        			User p = (User) piter.next();
	        			T.add(tf.format(iwrb.getLocalizedString( "custodian","Custodian")+" "+count,tf.HEADER),1,row);
	        			T.add(tf.format(iwrb.getLocalizedString("phone","Phone"),tf.HEADER),2,row);
	        			row++;
	        			T.add(tf.format(p.getNameLastFirst()),1,row);
	        			row++;
	        		}
	        	}
				
			}
			catch (Exception e) {
			}
			
			T.add(tf.format(iwrb.getLocalizedString("personal_id","Personal ID"),tf.HEADER),1,row);
			T.add(tf.format(child.getPersonalID()),2,row);
			row++;
			if(choice!=null){
				T.add(tf.format(iwrb.getLocalizedString("school_choice.grade","Grade"),tf.HEADER),1,row);
				T.add(tf.format(choice.getGrade()),2,row);
				row++;
				T.add(tf.format(iwrb.getLocalizedString("school_choice.choice_date", "Choice date"),tf.HEADER),1,row);
				T.add(tf.format(df.format( choice.getSchoolChoiceDate() ) ),2,row);
				row++;
				
				T.add(tf.format(iwrb.getLocalizedString("school_choice.change_from", "Change from"),tf.HEADER),1,row);
				if (choice.getChangeOfSchool()) {
						School changeFromSchool = schoolBean.getSchool(new Integer(choice.getCurrentSchoolId()));
						T.add(tf.format(changeFromSchool.getSchoolName()), 2, row);
		
				}
				row++;
				T.add(tf.format(iwrb.getLocalizedString("school_choice.language", "Language"),tf.HEADER),1,row);
				String language = choice.getLanguageChoice();
				if(language!=null)
					T.add(tf.format(language),2,row);
				row++;
			}
			
			Link back = new Link(tf.format(iwrb.getLocalizedString("back","Back")));
			back.addParameter(prmSchoolId,this.schoolId);
			T.add(back,1,row);
		}
		return T;
	}
	
	private PresentationObject getSchoolInfo()throws RemoteException{
		Table T = new Table();
		int row = 1,col = 1;
		T.add(tf.format(iwrb.getLocalizedString("school","School"),tf.HEADER),1,1);
		T.add(tf.format(school.getSchoolName()),1,2);
		T.add(tf.format(iwrb.getLocalizedString("address","Address"),tf.HEADER),2,1);
		T.add(tf.format(school.getSchoolName()),2,2);
		T.add(tf.format(iwrb.getLocalizedString("phone","Phone"),tf.HEADER),3,1);
		T.add(tf.format(school.getSchoolPhone()),3,2);
		
		return T;
	}
	
	private Link getPupilInfoLink(User child,SchoolChoice choice) throws  RemoteException{
		Link L = new Link(tf.format(child.getNameLastFirst()));
		L.addParameter(prmPupilInfo,choice.getPrimaryKey().toString());
		return L;
	}
	
	

	public void main(IWContext iwc) throws RemoteException{
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		tf = TextFormat.getInstance();
		df = DateFormat.getDateInstance(df.SHORT, iwc.getCurrentLocale());
		control(iwc);
	}
}