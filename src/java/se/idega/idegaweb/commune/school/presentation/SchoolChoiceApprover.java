package se.idega.idegaweb.commune.school.presentation;

import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import com.idega.presentation.Block;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.idegaweb.*;
import java.util.*;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.business.IBOLookup;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import com.idega.util.text.TextFormat;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import java.text.DateFormat;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;

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
  private DateFormat df;
  private int schoolId  = 1;

  private String prmPrelimIds = "prelim_chk_ids";
  private String prmNoRoom = "no_room";
  private String prmGroupChoice = "group_choice";
  public static String prmSchoolId = "school_id";

  public void control(IWContext iwc)throws java.rmi.RemoteException{
    debugParameters(iwc);
    init(iwc);
    String[] statusToSearch = {"UBEH","PLAC","PREL"};
    String code = "MBSKOLV";
    if(schoolId > 0){
      try{
      cases = choiceBean.getSchoolChoiceHome().findByCodeAndStatus(code,statusToSearch,schoolId);
      }
      catch(javax.ejb.FinderException ex){

      }
      if(cases !=null)
        add(getChoiceList());
    }
    else{
      add(tf.format(iwrb.getLocalizedString("school_choice.no_school_chosen") ,tf.HEADER));
    }

  }

  public void init(IWContext iwc)throws java.rmi.RemoteException{
    userBean = (UserBusiness)IBOLookup.getServiceInstance(iwc,UserBusiness.class);
    schoolBean = (SchoolBusiness)IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
    choiceBean = (SchoolChoiceBusiness)IBOLookup.getServiceInstance(iwc,SchoolChoiceBusiness.class);
    if(iwc.isParameterSet(prmSchoolId)){
      schoolId = Integer.parseInt(iwc.getParameter(prmSchoolId));
    }
  }

  public PresentationObject getChoiceList()throws java.rmi.RemoteException{
    Form F = new Form();
    DataTable T = new DataTable();
    T.setUseTop(false);
    T.setUseBottom(false);
    T.setUseTitles(false);
    T.add(tf.format(iwrb.getLocalizedString("school_choice.child","Child"),tf.HEADER),1,1);
    T.add(tf.format(iwrb.getLocalizedString("school_choice.grade","Grade"),tf.HEADER),2,1);
    T.add(tf.format(iwrb.getLocalizedString("school_choice.choice_date","Choice date"),tf.HEADER),3,1);
    T.add(tf.format(iwrb.getLocalizedString("school_choice.change_from","Change from"),tf.HEADER),4,1);
    T.add(tf.format(iwrb.getLocalizedString("school_choice.preliminary","Preliminary"),tf.HEADER),5,1);
    T.add(tf.format(iwrb.getLocalizedString("school_choice.not_room","No room"),tf.HEADER),7,1);
    T.add(tf.format(iwrb.getLocalizedString("school_choice.group_place","Group"),tf.HEADER),6,1);

    Iterator iter = cases.iterator();
    SchoolChoice choice;
    User child;
    int row  = 2;
    while(iter.hasNext()){
      choice = (SchoolChoice) iter.next();
      String sid = choice.getPrimaryKey().toString();
      child = userBean.getUser(choice.getChildId());
      T.add(tf.format(child.getNameLastFirst()),1,row);
      T.add(tf.format(choice.getChosenSchoolId()),2,row);
      T.add(tf.format(df.format(choice.getSchoolChoiceDate())),3,row);
      if(choice.getChangeOfSchool()){
        School changeFromSchool = schoolBean.getSchool(new Integer(choice.getCurrentSchoolId()));
        T.add(tf.format(changeFromSchool.getSchoolName()),4,row);

      }
      String status = choice.getCaseStatus().getStatus();
      // Preliminary part ( check boxes ) Or Denial
      if(status.equals(choice.getCaseStatusCreated())){
        CheckBox prelimCheck = new CheckBox(prmPrelimIds);
        prelimCheck.setValue(sid);
        T.add(prelimCheck,5,row);

        SubmitButton denyChoice = new SubmitButton(iwrb.getLocalizedImageButton(" no_room","No room"),prmNoRoom,sid);
        T.add(denyChoice,7,row);
      }
      // Class Placing Change to dropdown from Schoolblock class entity
      else if( status.equals(choice.getCaseStatusPreliminary())){
        TextInput groupChoice = new TextInput(prmGroupChoice);

        T.add(groupChoice,6,row);
      }


      row++;
    }
    F.add(T);
    return F;
  }

  public void main(IWContext iwc)throws java.rmi.RemoteException{
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    tf = TextFormat.getInstance();
    df = DateFormat.getDateInstance(df.SHORT,iwc.getCurrentLocale());
    control(iwc);
  }
}