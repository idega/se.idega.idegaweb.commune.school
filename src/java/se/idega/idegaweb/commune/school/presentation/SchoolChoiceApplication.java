package se.idega.idegaweb.commune.school.presentation;

import se.idega.idegaweb.commune.school.business.*;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.presentation.CitizenChildren;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;

import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Table;
import com.idega.user.data.User;
import com.idega.user.business.UserBusiness;
import com.idega.business.IBOLookup;

import java.util.Collection;
import java.util.Iterator;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolChoiceApplication extends CommuneBlock {

  IWBundle iwb;
  IWResourceBundle iwrb;
  UserBusiness userbuiz;
  Collection schoolHelp = null;

  public void control(IWContext iwc) throws Exception{
    String ID = iwc.getParameter(CitizenChildren.getChildIDParameterName());
    if(ID!=null){
      int childId = Integer.parseInt(ID);
      userbuiz = (UserBusiness) IBOLookup.getServiceInstance(iwc,UserBusiness.class);

      User child = userbuiz.getUser(childId);
      if(child!=null){

        add(getSchoolChoiceForm(child));
      }
    }
  }

  public PresentationObject getSchoolChoiceForm(User child)throws java.rmi.RemoteException{
    Form myForm = new Form();
    Table T = new Table(1,4);
    T.add(getChildInfo(child),1,1);
    T.add(getCurrentSchool(child),1,2);
    T.add(getChoiceSchool(child),1,3);
    T.add(getParentInfo(child),1,4);
    myForm.add(T);
    return myForm;
  }

  public void main(IWContext iwc)throws Exception{
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    control(iwc);
  }

  private PresentationObject getChildInfo(User child)throws java.rmi.RemoteException{
    Table T = new Table(6,5);
    T.mergeCells(2,1,3,1);
    T.mergeCells(2,2,3,2);
    T.mergeCells(1,3,6,3);
    T.mergeCells(1,4,3,4);
    T.mergeCells(1,5,3,5);
    T.add(getSmallHeader(iwrb.getLocalizedString("last_name","Last name")),1,1);
    T.add(getSmallHeader(iwrb.getLocalizedString("first_name","First name")),2,1);
    T.add(getText(child.getLastName()),1,2);
    T.add(getText(child.getFirstName()),2,2);

    T.add(getSmallHeader(iwrb.getLocalizedString("address","Address")),1,4);
    T.add(getSmallHeader(iwrb.getLocalizedString("zip_and_area","Zip area")),4,4);


    return T;
  }

  private PresentationObject getCurrentSchool(User child){
    Table T = new Table(4,7);
    T.mergeCells(1,1,4,1);

    T.add(getHeader(iwrb.getLocalizedString("child_is_now_in","Child is now in :")),1,1);
    DropdownMenu drpTypes = new DropdownMenu("sch_types_before");
    DropdownMenu drpAreas = new DropdownMenu("sch_area_before");
    DropdownMenu drpSchools = new DropdownMenu("sch_school_before");
    DropdownMenu drpGrade = new DropdownMenu("sch_grade_before");

    T.add(getSmallHeader(iwrb.getLocalizedString("school_name","School name")),3,4);
    T.add(getSmallHeader(iwrb.getLocalizedString("grade","Grade")),4,4);


    T.add(drpTypes,1,5);
    T.add(drpAreas,2,5);
    T.add(drpSchools,3,5);
    T.add(drpGrade,4,5);

    return T;
  }

  private PresentationObject getChoiceSchool(User child){
    Table T = new Table();
    T.add(getHeader(iwrb.getLocalizedString("choice_for_schoolyear","Choice for the schoolyear")),1,1);
    return T;
  }

  private PresentationObject getParentInfo(User child){
    Table T = new Table();
    return T;
  }


}