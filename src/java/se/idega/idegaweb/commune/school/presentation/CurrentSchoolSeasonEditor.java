package se.idega.idegaweb.commune.school.presentation;

import java.text.DateFormat;
import java.util.Collection;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.util.TextFormat;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class CurrentSchoolSeasonEditor extends Block {

  IWResourceBundle iwrb;
  IWBundle iwb;
  TextFormat tFormat;
  DateFormat dFormat;

  SchoolBusiness sabBean;
  SchoolChoiceBusiness socBean;
  CareBusiness careBean;
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void control(IWContext iwc) throws Exception{
    //debugParameters(iwc);
    initBeans(iwc);
    Form F = new Form();

    if(iwc.isParameterSet("sch_current_season")){
      saveArea(iwc);
      F.add(getListTable());
    }
    else if(iwc.isParameterSet("sch_delete_season")){
      int id = Integer.parseInt(iwc.getParameter("sch_delete_season"));
      sabBean.removeSchoolSeason(id);
      F.add(getListTable());
    }
    else
      F.add(getListTable());

     add(F);

  }

  private void initBeans(IWContext iwc) throws java.rmi.RemoteException{
    sabBean = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
    socBean = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc,SchoolChoiceBusiness.class);
    careBean = (CareBusiness) IBOLookup.getServiceInstance(iwc,CareBusiness.class);
  }

  private void saveArea(IWContext iwc)throws java.rmi.RemoteException{
    if(iwc.isParameterSet("sch_current_season")){
      Integer New = new Integer(iwc.getParameter("new_current"));
      Integer old  = New;
      if(iwc.isParameterSet("old_current"))
        old = new Integer(iwc.getParameter("old_current"));
      if(iwc.getParameter("sch_current_season").equals("true")){
        socBean.createCurrentSchoolSeason(New,old);
      }
    }
  }

  public PresentationObject getListTable() {
    Table T = new Table();
    int row = 1;

    Collection SchoolSeasons = new java.util.Vector(0);
    SchoolSeason current = null;
    try{
      SchoolSeasons = sabBean.findAllSchoolSeasons();
      current = careBean.getCurrentSeason();
      T.add(new HiddenInput("old_current",current.getPrimaryKey().toString()));

    }
    catch(java.rmi.RemoteException rex){

    }
    catch(javax.ejb.FinderException ex){

    }
    row++;
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name"),TextFormat.HEADER),1,row);
    T.add(tFormat.format(iwrb.getLocalizedString("start","Start"),TextFormat.HEADER),2,row);
    T.add(tFormat.format(iwrb.getLocalizedString("end","End"),TextFormat.HEADER),3,row);
    T.add(tFormat.format(iwrb.getLocalizedString("due_date","Duedate"),TextFormat.HEADER),4,row);
    T.add(tFormat.format(iwrb.getLocalizedString("current","Current"),TextFormat.HEADER),5,row);
    row++;

    java.util.Iterator iter = SchoolSeasons.iterator();
    SchoolSeason sarea ;
    RadioButton button = new RadioButton("new_current");
    RadioButton butt;
    while(iter.hasNext()){
      sarea = (SchoolSeason) iter.next();
      try{

      butt = (RadioButton)button.clone();
      butt.setValue(sarea.getPrimaryKey().toString());
      if(current!=null && current.getPrimaryKey().equals(sarea.getPrimaryKey()))
        butt.setSelected();

      T.add(tFormat.format(sarea.getSchoolSeasonName()),1,row);
      T.add(tFormat.format(dFormat.format(sarea.getSchoolSeasonStart())),2,row);
      T.add(tFormat.format(dFormat.format(sarea.getSchoolSeasonEnd())),3,row);
      T.add(tFormat.format(dFormat.format(sarea.getSchoolSeasonDueDate())),4,row);
      T.add(butt,5,row);
      }
      catch(Exception ex){}
      row++;
    }
    T.add(new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"sch_current_season","true"),3,6);
    return T;
  }


  public void main(IWContext iwc)throws Exception{
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    tFormat = TextFormat.getInstance();
    dFormat = DateFormat.getDateInstance(DateFormat.SHORT,iwc.getCurrentLocale());
    control(iwc);
  }
}