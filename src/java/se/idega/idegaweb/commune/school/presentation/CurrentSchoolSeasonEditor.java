package se.idega.idegaweb.commune.school.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.util.text.TextFormat;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.business.*;
import com.idega.util.IWTimestamp;

import java.util.Collection;
import com.idega.business.IBOLookup;
import java.text.DateFormat;

import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.data.CurrentSchoolSeason;


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
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_delete_season")){
      int id = Integer.parseInt(iwc.getParameter("sch_delete_season"));
      sabBean.removeSchoolSeason(id);
      F.add(getListTable(null));
    }
    else
      F.add(getListTable(null));

     add(F);

  }

  private void initBeans(IWContext iwc) throws java.rmi.RemoteException,javax.ejb.CreateException{
    sabBean = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
    socBean = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc,SchoolChoiceBusiness.class);
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

  public PresentationObject getListTable(SchoolSeason area) {
    Table T = new Table();
    int row = 1;

    Collection SchoolSeasons = new java.util.Vector(0);
    SchoolSeason current = null;
    try{
      SchoolSeasons = sabBean.findAllSchoolSeasons();
      current = socBean.getCurrentSeason();
      T.add(new HiddenInput("old_current",current.getPrimaryKey().toString()));

    }
    catch(java.rmi.RemoteException rex){

    }
    catch(javax.ejb.FinderException ex){

    }
    row++;
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name"),tFormat.HEADER),1,row);
    T.add(tFormat.format(iwrb.getLocalizedString("start","Start"),tFormat.HEADER),2,row);
    T.add(tFormat.format(iwrb.getLocalizedString("end","End"),tFormat.HEADER),3,row);
    T.add(tFormat.format(iwrb.getLocalizedString("due_date","Duedate"),tFormat.HEADER),4,row);
    T.add(tFormat.format(iwrb.getLocalizedString("current","Current"),tFormat.HEADER),5,row);
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
    tFormat = tFormat.getInstance();
    dFormat = DateFormat.getDateInstance(dFormat.SHORT,iwc.getCurrentLocale());
    control(iwc);
  }
}