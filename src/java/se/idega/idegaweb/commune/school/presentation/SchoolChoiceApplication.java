package se.idega.idegaweb.commune.school.presentation;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.presentation.CitizenChildren;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;

import com.idega.block.school.business.SchoolAreaBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolTypeBusiness;
import com.idega.block.school.data.*;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.PersonalIDFormatter;
import com.idega.core.data.Address;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega</p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolChoiceApplication extends CommuneBlock {

  IWBundle iwb;
  IWResourceBundle iwrb;
  UserBusiness userbuiz;
  Collection schoolTypes = null;
  List[] schools = null;
  int preTypeId = -1;
  int preAreaId = -1;
  DateFormat df;

  private String prefix = "sch_app_";
  private String prmSendCatalogue = prefix+"snd_cat";
  private String prmCustodiansAgree = prefix+"cus_agr";
  private String prmFirstSchool = prefix+"fst_scl";
  private String prmSecondSchool = prefix+"snd_scl";
  private String prmThirdSchool = prefix+"trd_scl";
  private String prmFirstArea = prefix+"fst_ara";
  private String prmSecondArea = prefix+"snd_ara";
  private String prmThirdArea = prefix+"trd_ara";
  private String prmPreSchool = prefix+"pre_scl";
  private String prmPreArea = prefix+"pre_ara";
  private String prmPreType = prefix+"pre_typ";
  private String prmType = prefix+"cho_typ";
  private String prmPreGrade = prefix+"pre_grd";
  private String prmMessage = prefix+"msg";
  private String prmAutoAssign = prefix+"aut_ass";
  private String prmSchoolChange = prefix+"scl_chg";
  private String prmSixYearCare = prefix+"six_car";
  private String prmLanguage = prefix+"cho_lng";
  private String prmAction = prefix+"snd_frm";
  private String prmChildId = CitizenChildren.getChildIDParameterName();
  private String prmParentId = CitizenChildren.getParentIDParameterName();
  private String prmForm = prefix+"the_frm";
  //private String prmCaseOwner = prefix+"cse_own";

  private boolean valSendCatalogue = false;
  private boolean valSixyearCare = false ;
  private boolean valAutoAssign = false ;
  private boolean valSchoolChange = false;
  private boolean valCustodiansAgree = false ;
  private String valMessage = "";
  private String valLanguage = "";
  private int valFirstSchool = -1;
  private int valSecondSchool = -1;
  private int valThirdSchool = -1;
  private int valFirstArea = -1;
  private int valSecondArea = -1;
  private int valThirdArea = -1;
  private int valPreGrade = -1;
  private int valPreType = -1;
  private int valPreArea = -1;
  private int valPreSchool = -1;
  private int valType = -1;
  private int childId = -1;
  private int valCaseOwner = -1;
  private int valMethod = 1; // Citizen:1; Quick: 2; Automatic: 3
  private boolean showAgree = false;
  protected boolean quickAdmin = false;
  SchoolChoiceBusiness schBuiz;
  SchoolClassMember schoolClassMember = null;
    SchoolClass schoolClass = null;
    School school = null;
    SchoolYear schoolYear = null;


  public void control(IWContext iwc) throws Exception{
    //debugParameters(iwc);
    String ID = iwc.getParameter(prmChildId);
		if(iwc.isLoggedOn()){
			if(ID!=null){
				childId = Integer.parseInt(ID);
				userbuiz = (UserBusiness) IBOLookup.getServiceInstance(iwc,UserBusiness.class);

				User child = userbuiz.getUser(childId);
				if(child!=null){
          initSchoolChild(iwc,child);
          boolean hasChoosed = false;
          Collection currentChildChoices = null;
					parse(iwc);
					boolean saved = false;
					if(iwc.isParameterSet(prmAction) && iwc.getParameter(prmAction).equals("true")){
						saved = saveSchoolChoice(iwc);
					}
          else{
            int CurrentSeasonID = ((Integer)schBuiz.getCurrentSeason().getPrimaryKey()).intValue();
            currentChildChoices = schBuiz.getSchoolChoiceHome().findByChildAndSeason(childId,CurrentSeasonID);
            if(!currentChildChoices.isEmpty())
              hasChoosed = true;
          }

					schoolTypes = getSchoolTypes(iwc,"SCHOOL");
					if(saved){
						add(getSchoolChoiceAnswer (iwc,child) );
          }
          else if(hasChoosed){
            add(getAlreadyChosenAnswer(iwc,child,currentChildChoices));
          }
					else{
            add(getSchoolChoiceForm(iwc,child));
					}
				}
			}
			else
			  add(iwrb.getLocalizedString("school.no_student_id_provided","No student provided"));
		}
		else
			add(iwrb.getLocalizedString("school.need_to_be_logged_on","You need to log in"));
  }

  private boolean saveSchoolChoice(IWContext iwc) {
    /** @todo Add some rule checking */

		// Checking if same school chosen more than once
		if(valFirstSchool == valSecondSchool || valSecondSchool == valThirdSchool ||valThirdSchool == valFirstSchool)
			return false;
    try{
      schBuiz.createSchoolChoices(valCaseOwner,childId,valPreSchool,
          valFirstSchool,valSecondSchool,valThirdSchool,valPreGrade,valMethod,-1,-1,
          valLanguage,valMessage,valSchoolChange,valSixyearCare,valAutoAssign,
          valCustodiansAgree,valSendCatalogue);
      return true;

    }catch(Exception ex){
      ex.printStackTrace();
    }
    return false;
  }

  private void parse(IWContext iwc){
    valSendCatalogue = iwc.isParameterSet(prmSendCatalogue);
    valSixyearCare = iwc.isParameterSet(prmSixYearCare);
    valAutoAssign = iwc.isParameterSet(prmAutoAssign);
    valSchoolChange = iwc.isParameterSet(prmSchoolChange);
    valCustodiansAgree = iwc.isParameterSet(prmCustodiansAgree);
    valMessage = iwc.getParameter(prmMessage);
    valLanguage = iwc.getParameter(prmLanguage);
    valFirstSchool = iwc.isParameterSet(prmFirstSchool)?Integer.parseInt(iwc.getParameter(prmFirstSchool)):-1;
    valSecondSchool = iwc.isParameterSet(prmSecondSchool)?Integer.parseInt(iwc.getParameter(prmSecondSchool)):-1;
    valThirdSchool = iwc.isParameterSet(prmThirdSchool)?Integer.parseInt(iwc.getParameter(prmThirdSchool)):-1;
    valFirstArea = iwc.isParameterSet(prmFirstArea)?Integer.parseInt(iwc.getParameter(prmFirstArea)):-1;
    valSecondArea = iwc.isParameterSet(prmSecondArea)?Integer.parseInt(iwc.getParameter(prmSecondArea)):-1;
    valThirdArea = iwc.isParameterSet(prmThirdArea)?Integer.parseInt(iwc.getParameter(prmThirdArea)):-1;
    valPreGrade = iwc.isParameterSet(prmPreGrade)?Integer.parseInt(iwc.getParameter(prmPreGrade)):-1;
    valPreType = iwc.isParameterSet(prmPreType)?Integer.parseInt(iwc.getParameter(prmType)):-1;
    valPreArea = iwc.isParameterSet(prmPreArea)?Integer.parseInt(iwc.getParameter(prmPreArea)):-1;
    valPreSchool = iwc.isParameterSet(prmPreSchool)?Integer.parseInt(iwc.getParameter(prmPreSchool)):-1;
    valType = iwc.isParameterSet(prmType)?Integer.parseInt(iwc.getParameter(prmType)):-1;
    valCaseOwner = iwc.isParameterSet(prmParentId)?Integer.parseInt(iwc.getParameter(prmParentId)):-1;
    if(!quickAdmin){
      valCaseOwner = iwc.getUserId();
    }
    else{
      valMethod = 2;
    }

  }

  public PresentationObject getSchoolChoiceForm(IWContext iwc,User child)throws java.rmi.RemoteException{
    Form myForm = new Form();
    myForm.setName(prmForm);
    Table T = new Table(1,8);
    T.setCellpadding(3);
    T.add(getCurrentSchoolSeasonInfo(iwc),1,1);
    T.add(getChildInfo(iwc,child),1,2);
    T.add(getCurrentSchool(iwc,child),1,3);
    T.add(getChoiceSchool(iwc,child),1,4);
    T.add(getParentInfo(iwc,child),1,5);
    T.add(getMessagePart(iwc),1,6);

	String url = "javascript:MySubmit()";
	Link save = new Link(iwrb.getLocalizedImageButton("save","Save"),url);
	T.add(save,1,7);
	T.add(new HiddenInput(prmAction,"false"));
    //T.add(new SubmitButton(iwrb.getLocalizedString("school.submit_application","Send"),prmAction,"true"),1,7);
    T.add(new HiddenInput(prmChildId,child.getPrimaryKey().toString() ) );
    myForm.add(T);

    Page p = this.getParentPage();
    if(p!=null){
      Script S = p.getAssociatedScript();
      Script F = new Script();
      S.addFunction("initFilter",getInitFilterScript());
      S.addFunction("submitFunction",getMySubmitScript());
      S.addFunction("checkApplication",getSchoolCheckScript());

      S.addFunction("changeFilter",getFilterScript(iwc));
      if(valPreType > 0 || valPreArea > 0 || valPreSchool > 0 ){
        F.addFunction("f1",getInitFilterCallerScript(iwc,prmPreType,prmPreArea,prmPreSchool,valPreType,valPreArea,valPreSchool));
      }
      if(valType > 0){
        if(valFirstArea > 0 || valFirstSchool > 0)
          F.addFunction("f2",getInitFilterCallerScript(iwc,prmType,prmFirstArea,prmFirstSchool,valType,valFirstArea,valFirstSchool));
        if(valSecondArea > 0 || valSecondSchool > 0)
          F.addFunction("f3",getInitFilterCallerScript(iwc,prmType,prmSecondArea,prmSecondSchool,valType,valSecondArea,valSecondSchool));
        if(valThirdArea > 0 || valThirdSchool > 0)
          F.addFunction("f4",getInitFilterCallerScript(iwc,prmType,prmThirdArea,prmThirdSchool,valType,valThirdArea,valThirdSchool));

      }
      T.add(F,1,T.getColumns());
    }
    return myForm;
  }

   public PresentationObject getSchoolChoiceAnswer(IWContext iwc,User child)throws java.rmi.RemoteException{
   		DataTable T = new DataTable();
   		T.setUseBottom(false);
   		T.setUseTop(false);
   		T.setUseTitles(false);
   		String text1 = iwrb.getLocalizedString("school_choice.receipt_1","The school choice for ");
   		String text2= iwrb.getLocalizedString("school_choice.receipt_2","has been received. ");
   		String text3= iwrb.getLocalizedString("school_choice.receipt_3","The application will be processed by each school you applied for .");
   		String text4= iwrb.getLocalizedString("school_choice.receipt_4","Thank you");

   		int row = 1;
   		T.add(getText( text1),1,row);
   		T.add(getHeader(child.getName()),1,row);
   		T.add(getText(text2),1,row);
   		row++;
   		T.add(getText(text3),1,row);
   		row++;
   		T.add(getText(text4),1,row);

   		return T;
   }

   public PresentationObject getAlreadyChosenAnswer(IWContext iwc,User child,Collection choices)throws java.rmi.RemoteException{
   		DataTable T = new DataTable();
   		T.setUseBottom(false);
   		T.setUseTop(false);
   		T.setUseTitles(false);
   		String text1 = iwrb.getLocalizedString("school_choice.already_1","The school choice for ");
   		String text2= iwrb.getLocalizedString("school_choice.already_2","has already been received. ");

   		int row = 1;
   		T.add(getText( text1),1,row);
   		T.add(getHeader(child.getName()),1,row);
   		T.add(getText(text2),1,row);
   		row++;
   		return T;
   }

  public PresentationObject getCurrentSchoolSeasonInfo(IWContext iwc) throws RemoteException {
  	SchoolChoiceBusiness choiceBean  = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc,SchoolChoiceBusiness.class);
  	SchoolSeason season = null;
		try {
			season = choiceBean.getCurrentSeason();
		}
		catch (RemoteException e) {
		}
		catch (FinderException e) {
		}
  	Table T = new Table();
  	if(season !=null){
  		String message = iwrb.getLocalizedString("school_choice.last_date_for_choice_is","Last date to choose is");
  		String date = df.format(season.getSchoolSeasonDueDate());
  		Text t = getHeader(message+" "+date);
  		t.setFontColor("FF0000");
  		T.add(t,1,1);

  	}
  	return T;

  }

  public void main(IWContext iwc)throws Exception{
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    df = DateFormat.getDateInstance(df.SHORT,iwc.getCurrentLocale());
    schBuiz = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc,SchoolChoiceBusiness.class);
    control(iwc);
  }

  private void initSchoolChild(IWContext iwc,User child){

    try{
      schoolClassMember = schBuiz.getSchoolBusiness().getSchoolClassMemberHome().findByUserAndSeason(child,schBuiz.getCurrentSeason());
      schoolClass = schBuiz.getSchoolBusiness().getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassMember.getSchoolClassId()));
      school = schBuiz.getSchool(schoolClass.getSchoolId());
      schoolYear = schBuiz.getSchoolBusiness().getSchoolYearHome().findByPrimaryKey(new Integer(schoolClass.getSchoolYearId()));
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  private PresentationObject getChildInfo(IWContext iwc,User child)throws java.rmi.RemoteException{
    Table T = new Table(6,5);
    T.setColor("#ffffcc");
	T.setCellpadding(3);
    //T.mergeCells(2,1,3,1);
    //T.mergeCells(2,2,3,2);
    T.mergeCells(1,3,6,3);
    T.mergeCells(1,4,3,4);
    T.mergeCells(1,5,3,5);
    T.add(getSmallHeader(iwrb.getLocalizedString("last_name","Last name")),1,1);
    T.add(getSmallHeader(iwrb.getLocalizedString("first_name","First name")),2,1);
    T.add(getSmallHeader(iwrb.getLocalizedString("personal_id","Personal ID")),3,1);
    T.add(getText(child.getLastName()),1,2);
    T.add(getText(child.getFirstName()),2,2);
    String personalID = PersonalIDFormatter.format(child.getPersonalID(),iwc.getApplication().getSettings().getApplicationLocale());
    T.add(getText(personalID),3,2);

    T.add(getSmallHeader(iwrb.getLocalizedString("address","Address")),1,4);
    T.add(getSmallHeader(iwrb.getLocalizedString("zip_and_area","Zip area")),4,4);
    Address address = userbuiz.getUsersMainAddress(child);
    T.add(getText(address.getStreetAddress()),1,5);
    try{
    T.add(getText(address.getPostalAddress()),4,5);
    }catch(Exception ex){}


    return T;
  }

  private PresentationObject getCurrentSchool(IWContext iwc,User child)throws java.rmi.RemoteException{
    Table T = new Table(4,7);
    T.mergeCells(1,1,4,1);

    T.add(getHeader(iwrb.getLocalizedString("school.child_is_now_in","Child is now in :")),1,1);

    if(schoolClassMember!=null){
      T.add(getSmallHeader(iwrb.getLocalizedString("school.school_name","School name")),2,3);
      T.add(getSmallHeader(iwrb.getLocalizedString("school.school_class","School class")),3,3);
      T.add(getSmallHeader(iwrb.getLocalizedString("school.school_year","School year")),4,3);

      T.add(getText(school.getName()),2,4);
      T.add(getText(schoolClass.getName()),3,4);
      T.add(getText(schoolYear.getName()),4,4);
      T.add(new HiddenInput(prmPreSchool,school.getPrimaryKey().toString()));
      int year = 0;
      try{
        year = Integer.parseInt(schoolYear.getName());

      }
      catch(NumberFormatException nfe){

      }
      year++;
      T.add(new HiddenInput(prmPreGrade,String.valueOf(year)));
    }
    else {
      DropdownMenu drpTypes = getTypeDrop(prmPreType);
      drpTypes.setOnChange(getFilterCallerScript(iwc,prmPreType,prmPreArea,prmPreSchool,1));
      drpTypes.setWidth("20");
      DropdownMenu drpAreas = new DropdownMenu(prmPreArea);
      drpAreas.addMenuElementFirst("-1",iwrb.getLocalizedString("school.area","School area..........."));
      drpAreas.setOnChange(getFilterCallerScript(iwc,prmPreType,prmPreArea,prmPreSchool,2));
      //drpAreas.setWidth("50");
      DropdownMenu drpSchools = new DropdownMenu(prmPreSchool);
      drpSchools.addMenuElementFirst("-1",iwrb.getLocalizedString("school.school","School................"));
      //drpSchools.setWidth("20");
      DropdownMenu drpGrade = new DropdownMenu(prmPreGrade);
      drpGrade.addMenuElement("-1","");
      for (int i = 1; i < 10; i++) {
        drpGrade.addMenuElement(String.valueOf(i));
      }
      drpGrade.setSelectedElement(String.valueOf(valPreGrade));

      T.add(getSmallHeader(iwrb.getLocalizedString("school.school_name","School name")),3,3);
      T.add(getSmallHeader(iwrb.getLocalizedString("school.grade","Grade")),4,3);
      T.add(drpTypes,1,4);
      T.add(drpAreas,2,4);
      T.add(drpSchools,3,4);
      T.add(drpGrade,4,4);
    }

    return T;
  }

  private DropdownMenu getTypeDrop(String name)throws java.rmi.RemoteException{
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement("-1",iwrb.getLocalizedString("school.school_type","School type"));
    Iterator iter = schoolTypes.iterator();
    while(iter.hasNext()){
      SchoolType type = (SchoolType) iter.next();
      drp.addMenuElement(type.getPrimaryKey().toString(),type.getSchoolTypeName());
    }

    return drp;
  }

  private PresentationObject getChoiceSchool(IWContext iwc,User child)throws java.rmi.RemoteException{
    Table T = new Table(3,10);
    T.mergeCells(1,1,3,1);
    T.add(getHeader(iwrb.getLocalizedString("school.choice_for_schoolyear","Choice for the schoolyear")),1,1);
    Date d = child.getDateOfBirth();
    if(d==null)
      d = new Date();
    Age age = new Age(d);

    DropdownMenu typeDrop = getTypeDrop(prmType);
    CheckBox chkSixYear = new CheckBox(prmSixYearCare,"true");
    CheckBox chkAutoAssign = new CheckBox(prmAutoAssign,"true");
    CheckBox chkSchoolChange = new CheckBox(prmSchoolChange,"true");
    chkSixYear.setChecked(valSixyearCare);
    chkAutoAssign.setChecked(valAutoAssign);
    chkSchoolChange.setChecked(valSchoolChange);
    TextInput txtLangChoice = new TextInput(prmLanguage);
    DropdownMenu drpFirstArea = new DropdownMenu(prmFirstArea);
    DropdownMenu drpSecondArea = new DropdownMenu(prmSecondArea);
    DropdownMenu drpThirdArea = new DropdownMenu(prmThirdArea);
    DropdownMenu drpFirstSchool = new DropdownMenu(prmFirstSchool);
    DropdownMenu drpSecondSchool = new DropdownMenu(prmSecondSchool);
    DropdownMenu drpThirdSchool = new DropdownMenu(prmThirdSchool);

    	drpFirstArea.addMenuElementFirst("-1",iwrb.getLocalizedString("school.area_first",        "School Area...................."));
		drpSecondArea.addMenuElementFirst("-1",iwrb.getLocalizedString("school.area_second",      "School Area ..................."));
		drpThirdArea.addMenuElementFirst("-1",iwrb.getLocalizedString("school.area_third",        "School Area...................."));
		drpFirstSchool.addMenuElementFirst("-1",iwrb.getLocalizedString("school.school_first",    "School........................."));
		drpSecondSchool.addMenuElementFirst("-1",iwrb.getLocalizedString("school.school_second",  "School........................."));
		drpThirdSchool.addMenuElementFirst("-1",iwrb.getLocalizedString("school.school_third",    "School........................."));


    T.add(typeDrop,1,3);
    T.mergeCells(2,3,3,3);
    T.mergeCells(2,4,3,4);
    T.mergeCells(2,5,3,5);
    typeDrop.setOnChange(getFilterCallerScript(iwc,prmType,prmFirstArea,prmFirstSchool,1));
    typeDrop.setOnChange(getFilterCallerScript(iwc,prmType,prmSecondArea,prmSecondSchool,1));
    typeDrop.setOnChange(getFilterCallerScript(iwc,prmType,prmThirdArea,prmThirdSchool,1));
    drpFirstArea.setOnChange(getFilterCallerScript(iwc,prmType,prmFirstArea,prmFirstSchool,2));
    drpSecondArea.setOnChange(getFilterCallerScript(iwc,prmType,prmSecondArea,prmSecondSchool,2));
    drpThirdArea.setOnChange(getFilterCallerScript(iwc,prmType,prmThirdArea,prmThirdSchool,2));
    if(age.getYears()==6){
      T.add(chkAutoAssign,2,3);
      T.add(getSmallText(iwrb.getLocalizedString("school.six_year_childcare","I want my six years old to be within childcare")),2,3);
    }
    T.add(chkAutoAssign,2,4);
    T.add(getSmallText(iwrb.getLocalizedString("school.assign_school_placing","Assign me a school placing")),2,4);
    T.add(chkSchoolChange,2,5);
    T.add(getSmallText(iwrb.getLocalizedString("school.change_of_school","Change of school")),2,5);
    T.add(getSmallText(iwrb.getLocalizedString("school.first_choice","First choice")),1,6);
    T.add(drpFirstArea,2,6);
    T.add(drpFirstSchool,3,6);
    T.add(getSmallText(iwrb.getLocalizedString("school.second_choice","Second choice")),1,7);
    T.add(drpSecondArea,2,7);
    T.add(drpSecondSchool,3,7);
    T.add(getSmallText(iwrb.getLocalizedString("school.third_choice","Third choice")),1,8);
    T.add(drpThirdArea,2,8);
    T.add(drpThirdSchool,3,8);



    return T;
  }

  private PresentationObject getParentInfo(IWContext iwc,User child)throws java.rmi.RemoteException{
    Table T = new Table();
		int row = 1;
    T.add(getSmallHeader(iwrb.getLocalizedString("school.custodians","Custodians")),1,row);
		row++;
    MemberFamilyLogic mlogic = (MemberFamilyLogic) IBOLookup.getServiceInstance(iwc,MemberFamilyLogic.class);
    try{
      Collection parents = mlogic.getCustodiansFor(child);
      Iterator iter = parents.iterator();
      String address = "";
      boolean caseOwning = true;
      while(iter.hasNext()){
				User parent = (User) iter.next();
				String addr = userbuiz.getUsersMainAddress(parent).getStreetAddress();
				T.add(getSmallText(parent.getNameLastFirst()),1,row);
				T.add(getSmallText(addr),3,row);
				row++;

				// checkiing for same parent address
				showAgree = address.equalsIgnoreCase(addr);
				address = addr;
        if(quickAdmin && caseOwning && valCaseOwner == -1){
          valCaseOwner = ((Integer) parent.getPrimaryKey()).intValue();
          caseOwning = false;
        }
      }
      if(valCaseOwner !=-1)
        T.add(new HiddenInput(prmParentId,String.valueOf(valCaseOwner)));
    }
    catch(NoCustodianFound ex){
		ex.printStackTrace();
      T.add(getSmallErrorText(iwrb.getLocalizedString("school.no_registered_custodians","No registered custodians")),1,row);
    }
    return T;
  }

  private PresentationObject getMessagePart(IWContext iwc)throws java.rmi.RemoteException{
    Table T = new Table();


    CheckBox chkSendCatalogue = new CheckBox(prmSendCatalogue,"true");
    chkSendCatalogue.setChecked(valSendCatalogue);
    TextArea taMessage = new TextArea(prmMessage,65,6);
    if(showAgree){
    	CheckBox chkCustodiansAgree = new CheckBox(prmCustodiansAgree,"true");
    	chkCustodiansAgree.setChecked(valCustodiansAgree);
    	T.add(chkCustodiansAgree,1,1);
    	T.add(getSmallText(iwrb.getLocalizedString("school.custodians_agree","Parents/Custodians agree")),1,1);
    }
    T.add(chkSendCatalogue,1,2);
    T.add(getSmallText(iwrb.getLocalizedString("school.send_catalogue","Send school catalogue")),1,2);
    T.add(taMessage,1,3);
    return T;
  }

  private Collection getSchoolTypes(IWContext iwc,String category){
    try{
      SchoolTypeBusiness sBuiz = (SchoolTypeBusiness) IBOLookup.getServiceInstance(iwc,SchoolTypeBusiness.class);
      return sBuiz.findAllSchoolTypesInCategory(category);
    }
    catch(Exception ex){

    }
    return null;
  }

  private Collection getSchools(IWContext iwc,int area,int type){
   try{
      SchoolBusiness sBuiz = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
      return sBuiz.findAllSchoolsByAreaAndType(area,type);

    }
    catch(Exception ex){
      ex.printStackTrace();
      return new java.util.Vector();
    }
  }

  private Collection getSchoolAreasWithType(IWContext iwc,int type){
    try{
      SchoolAreaBusiness saBuiz = (SchoolAreaBusiness) IBOLookup.getServiceInstance(iwc,SchoolAreaBusiness.class);
      return saBuiz.findAllSchoolAreasByType(type);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    return null;
  }

  private Collection getSchoolByAreaAndType(IWContext iwc,int area,int type){
    try{
      SchoolBusiness sBuiz = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
      return sBuiz.findAllSchoolsByAreaAndType(area,type);

    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    return null;
  }

  private String getFilterCallerScript(IWContext iwc,String typeName,String areaName,String schoolName,int Type){
    StringBuffer script = new StringBuffer("changeFilter(");
    script.append(Type);
    script.append(",");
    script.append("this.form.elements['");
    script.append(typeName);
    script.append("'],");
    script.append("this.form.elements['");
    script.append(areaName);
    script.append("'],");
    script.append("this.form.elements['");
    script.append(schoolName);
    script.append("'])");
    return script.toString();
  }

  private String getInitFilterCallerScript(IWContext iwc,String typeName,String areaName,String schoolName,int typeSel,int areaSel,int schoolSel){
    StringBuffer script = new StringBuffer("initFilter(");
    script.append("document.forms['").append(prmForm).append("'].elements['");
    script.append(typeName);
    script.append("'],");
    script.append("document.forms['").append(prmForm).append("'].elements['");
    script.append(areaName);
    script.append("'],");
    script.append("document.forms['").append(prmForm).append("'].elements['");
    script.append(schoolName);
    script.append("'],");
    script.append(typeSel);
    script.append(",");
    script.append(areaSel);
    script.append(",");
    script.append(schoolSel);
    script.append(")");
    return script.toString();
  }

  private String getFilterScript(IWContext iwc)throws java.rmi.RemoteException{
    StringBuffer s = new StringBuffer();
    s.append("function changeFilter(index,type,area,school){").append(" \n\t");
    s.append("var typeSelect = type;").append(" \n\t");
    s.append("var areaSelect = area;").append(" \n\t");
    s.append("var schoolSelect = school;").append(" \n\t");
    s.append("var selected = 0;").append(" \n\t");
    s.append("if(index == 1){").append(" \n\t\t");
    s.append("selected = typeSelect.options[typeSelect.selectedIndex].value;").append("\n\t\t");
    s.append("areaSelect.options.length = 0;").append("\n\t\t");
    s.append("schoolSelect.options.length = 0;").append("\n\t\t");
    s.append("areaSelect.options[areaSelect.options.length] = new Option(\"");
    s.append(iwrb.getLocalizedString("choose_area","Choose Area")).append("\",\"-1\",true,true);").append("\n\t\t");
    //s.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"Veldu skóla\",\"-1\",true,true);").append("\n\t");
    s.append("}else if(index == 2){").append(" \n\t\t");
    s.append("selected = areaSelect.options[areaSelect.selectedIndex].value;").append("\n\t\t");
    s.append("schoolSelect.options.length = 0;").append("\n\t\t");
    s.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"");
    s.append(iwrb.getLocalizedString("choose_school","Choose School")).append("\",\"-1\",true,true);").append("\n\t");
    s.append("} else if(index == 3){").append("\n\t\t");
    s.append("selected = schoolSelect.options[schoolSelect.selectedIndex].value;").append("\n\t");
    s.append("}").append("\n\t\t\t");

    // Data Filling ::

    StringBuffer t = new StringBuffer("if(index==1){\n\t");
    StringBuffer a = new StringBuffer("else if(index==2){\n\t");
    StringBuffer c = new StringBuffer("else if(index==3){\n\t");

    Collection Types = this.schoolTypes;
    if(Types!=null && !Types.isEmpty()){
      Iterator iter = Types.iterator();
      SchoolType type;
      SchoolArea area;
      School school;
      Collection areas;
      Collection schools;
      // iterate through schooltypes
      while(iter.hasNext()){
        type = (SchoolType) iter.next();

        Integer tPK = (Integer) type.getPrimaryKey();
        //System.err.println("checking type "+tPK.toString());
        areas = getSchoolAreasWithType(iwc,tPK.intValue());
        if(areas!=null && !areas.isEmpty()){
          Iterator iter2 = areas.iterator();
          t.append("if(selected == \"").append(tPK.toString()).append("\"){").append("\n\t\t");

           Hashtable aHash = new Hashtable();

          // iterate through areas whithin types
          while(iter2.hasNext()) {
            area = (SchoolArea) iter2.next();
            Integer aPK = (Integer)area.getPrimaryKey();
            // System.err.println("checking area "+aPK.toString());
            if(!aHash.containsKey(aPK)){
            	aHash.put(aPK,aPK);
	            schools = getSchoolByAreaAndType(iwc,aPK.intValue(),tPK.intValue());
	            if(schools!=null ){
	              Iterator iter3 = schools.iterator();
	              a.append("if(selected == \"").append(aPK.toString()).append("\"){").append("\n\t\t");
	              Hashtable hash = new Hashtable();
	              // iterator through schools whithin area and type
	              while(iter3.hasNext()){
	                school = (School) iter3.next();
	                String pk = school.getPrimaryKey().toString();
	                //System.err.println("checking school "+pk.toString());
	                if(!hash.containsKey(pk)){
		                a.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"");
		                a.append(school.getSchoolName()).append("\",\"");
		                a.append(pk).append("\");\n\t\t");
		                hash.put(pk,pk);
	                }

	              }
	              a.append("}\n\t\t");
	        	}
            }
            else{
            System.err.println("shools empty");
          }
          t.append("areaSelect.options[areaSelect.options.length] = new Option(\"");
          t.append(area.getSchoolAreaName()).append("\",\"");
          t.append(area.getPrimaryKey().toString()).append("\");").append("\n\t\t");;

          }
          t.append("}\n\t");
        }
        else
           System.err.println("areas empty");
      }
    }
    else
      System.err.println("types empty");


    s.append("\n\n");


    t.append("\n\t }");
    a.append("\n\t }");
    c.append("\n\t }");

    s.append(t.toString());
    s.append(a.toString());
    s.append(c.toString());
    s.append("\n}");

    return s.toString();
  }

  public String getInitFilterScript(){
    StringBuffer s = new StringBuffer();
    s.append("function initFilter(type,area,school,type_sel,area_sel,school_sel){ \n  ");
    s.append("changeFilter( 1 ,type,area,school); \n  ");
    s.append("type.selectedIndex = type_sel; \n  ");
    s.append("changeFilter(2,type,area,school); \n  ");
    s.append("area.selectedIndex = area_sel; \n  ");
    s.append("changeFilter(3,type,area,school); \n ");
    s.append("school.selectedIndex = school_sel; \n}");
    return s.toString();
  }

	public String getSchoolCheckScript(){
	  StringBuffer s = new StringBuffer();
		s.append("\nfunction checkApplication(){\n\t");
		//s.append("\n\t\t alert('").append("checking choices").append("');");
		s.append("\n\t var currSchool = ").append("document.").append(prmForm).append(".elements['").append(prmPreSchool).append("'];");
		s.append("\n\t var dropOne = ").append("document.").append(prmForm).append(".elements['").append(prmFirstSchool).append("'];");
		s.append("\n\t var dropTwo = ").append("document.").append(prmForm).append(".elements['").append(prmSecondSchool).append("'];");
		s.append("\n\t var dropThree = ").append("document.").append(prmForm).append(".elements['").append(prmThirdSchool).append("'];");
		s.append("\n\t var gradeDrop = ").append("document.").append(prmForm).append(".elements['").append(prmPreGrade).append("'];");
		s.append("\n\t var one = ").append("dropOne.options[dropOne.selectedIndex].value;");
		s.append("\n\t var two = ").append("dropTwo.options[dropTwo.selectedIndex].value;");
		s.append("\n\t var  three = ").append("dropThree.options[dropThree.selectedIndex].value;");
		s.append("\n\t var  year = ").append("gradeDrop.options[gradeDrop.selectedIndex].value;");
		s.append("\n\t var  school = ").append("currSchool.options[currSchool.selectedIndex].value;");

		// current school check
		s.append("\n\t if(school <= 0){");
		String msg1 = iwrb.getLocalizedString("school_choice.must_set_current_school","You must provide current shool");
		s.append("\n\t\t\t alert('").append(msg1).append("');");
		s.append("\n\t\t ");
		s.append("\n\t }");

		// year check
		s.append("\n\t else if(year <= 0 && school > 0){");
		String msg2 = iwrb.getLocalizedString("school_choice.must_set_grade","You must provide current shool year");
		s.append("\n\t\t\t alert('").append(msg2).append("');");
		s.append("\n\t\t ");
		s.append("\n\t }");

		// schoolchoices checked
		s.append("\n\t else if(one && two && three){");
		s.append("\n\t if(one == two || two == three || three == one){");
		String msg = iwrb.getLocalizedString("school_school.must_not_be_the_same","Please do not choose the same school more than once");
		s.append("\n\t\t\t alert('").append(msg).append("');");
		s.append("\n\t\t\t return false;");
		s.append("\n\t\t }");
		s.append("\n\t }");
		s.append("\n\t else{");
		s.append("\n\t\t alert('").append("no choices").append("');");
		s.append("\n\t\t return false;");
		s.append("\n\t }");
		//s.append("\n\t\t alert('").append("nothing wrong").append("');");
		s.append("\n\t return true;");
		s.append("\n}\n");
		return s.toString();
	}

	private String getMySubmitScript(){
		StringBuffer s = new StringBuffer();
		s.append("\n function MySubmit(){");
		s.append("\n\t if(checkApplication()) {");
		s.append("\n\t\t document.").append(prmForm).append(".elements['").append(prmAction).append("'].value='true';");
		s.append("\n\t\t document.").append(prmForm).append(".submit();");
		s.append("\n\t }");
		//s.append("\n\t else{");
		//s.append("\n\t\t alert('").append(iwrb.getLocalizedString("school_choice.unfinished","You have to finish the application")).append("');");
		//s.append("\n\t }");
		s.append("\n}\n");
		return s.toString();

		/*
		if(checkApplication()){
			document.
		}
		else{

		}
		*/
	}

  public void setAsAdminQuickChoice(boolean quick){
    this.quickAdmin = quick;
  }


}

