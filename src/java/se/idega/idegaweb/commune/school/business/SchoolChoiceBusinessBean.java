package se.idega.idegaweb.commune.school.business;

import se.idega.idegaweb.commune.school.data.*;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;

import com.idega.business.IBOServiceBean;
import com.idega.data.*;
import com.idega.block.process.data.*;
import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class SchoolChoiceBusinessBean extends com.idega.block.process.business.CaseBusinessBean implements SchoolChoiceBusiness{

  public SchoolChoiceHome getSchoolChoiceHome() throws java.rmi.RemoteException{
    return (SchoolChoiceHome) this.getIDOHome(SchoolChoice.class);
  }

  public CurrentSchoolSeasonHome getCurrentSchoolSeasonHome() throws java.rmi.RemoteException{
    return (CurrentSchoolSeasonHome) this.getIDOHome(CurrentSchoolSeason.class);
  }

  public SchoolSeasonHome getSchoolSeasonHome()throws java.rmi.RemoteException{
    return (SchoolSeasonHome) this.getIDOHome(SchoolSeason.class);
  }

  public SchoolSeason getCurrentSeason()throws java.rmi.RemoteException,javax.ejb.FinderException{
    CurrentSchoolSeason season =  getCurrentSchoolSeasonHome().findCurrentSeason();
    return getSchoolSeasonHome().findByPrimaryKey(season.getCurrent());
  }

  public SchoolChoice getSchoolChoice(int schoolChoiceId) throws FinderException,RemoteException {
    return getSchoolChoiceHome().findByPrimaryKey(new Integer(schoolChoiceId));
  }

  public List createSchoolChoices(
    int userId,
      int childId,
      int current_school,
      int chosen_school_1,
      int chosen_school_2,
      int chosen_school_3,
      int grade,
      int method,
      int workSituation1,
      int workSituation2,
      String language,
      String message,
      boolean changeOfSchool,
      boolean keepChildrenCare,
      boolean autoAssign,
      boolean custodiansAgree,
      boolean schoolCatalogue
      )throws IDOCreateException{

    int caseCount = 3;
    java.sql.Timestamp time= new java.sql.Timestamp(System.currentTimeMillis());
    List returnList = new Vector(3);

    javax.transaction.UserTransaction trans = this.getSessionContext().getUserTransaction();
    try {
      trans.begin();
      CaseStatus first = getCaseStatusOpen();
      CaseStatus other = getCaseStatus("TYST");
      int[] schoolIds = {chosen_school_1,chosen_school_2,chosen_school_3};
      for (int i = 0; i < caseCount; i++) {

        SchoolChoice choice = createSchoolChoice(userId,childId,current_school,
        schoolIds[i],grade,i+1,method,workSituation1,workSituation2,
        language,message,time,changeOfSchool,keepChildrenCare,autoAssign,
        custodiansAgree,schoolCatalogue,i==0?first:other);
      }
      trans.commit();
      return returnList;

    }
    catch (Exception ex) {
      try{
      trans.rollback();
      }
      catch(javax.transaction.SystemException e){
        throw new IDOCreateException(e.getMessage());
      }
      throw new IDOCreateException(ex.getMessage());
    }
  }

  private SchoolChoice createSchoolChoice(
      int userId,
      int childId,
      int current_school,
      int chosen_school,
      int grade,
      int choiceOrder,
      int method,
      int workSituation1,
      int workSituation2,
      String language,
      String message,
      java.sql.Timestamp choiceDate,
      boolean changeOfSchool,
      boolean keepChildrenCare,
      boolean autoAssign,
      boolean custodiansAgree,
      boolean schoolCatalogue,
      CaseStatus caseStatus
      )throws CreateException,RemoteException{
    SchoolChoiceHome home = this.getSchoolChoiceHome();
    SchoolChoice choice = home.create();
    try{
      choice.setOwner(getUser(userId));
    }
    catch(FinderException fex){
      throw new IDOCreateException(fex);
    }

    choice.setChildId(childId);
    choice.setCurrentSchoolId(current_school);
    choice.setChosenSchoolId(chosen_school);
    choice.setGrade(grade);
    choice.setChoiceOrder(choiceOrder);
    choice.setMethod(method );
    choice.setWorksituation1(workSituation1);
    choice.setWorksituation2(workSituation2);
    choice.setLanguageChoice(language);
    choice.setChangeOfSchool(changeOfSchool);
    choice.setKeepChildrenCare(keepChildrenCare);
    choice.setAutoAssign(autoAssign);
    choice.setCustodiansAgree(custodiansAgree);
    choice.setSchoolCatalogue(schoolCatalogue);
    choice.setSchoolChoiceDate(choiceDate);
    choice.setMessage(message);

    choice.setCaseStatus(caseStatus);


    try{
      choice.store();
    }
    catch(IDOStoreException idos){
      throw new IDOCreateException(idos);
    }
    return choice;
  }

  public void createCurrentSchoolSeason(Integer newKey,Integer oldKey)throws java.rmi.RemoteException{
      CurrentSchoolSeasonHome shome = getCurrentSchoolSeasonHome();
      CurrentSchoolSeason season;
      try{
        season = shome.findByPrimaryKey(oldKey);
        if(oldKey.intValue()!=newKey.intValue()){
          season.remove();
          season = shome.create();
        }
      }
      catch(javax.ejb.RemoveException rme){throw new java.rmi.RemoteException(rme.getMessage());}
      catch(javax.ejb.CreateException cre){throw new java.rmi.RemoteException(cre.getMessage());}
      catch(javax.ejb.FinderException fe){
        //fe.printStackTrace();
        try {
          season = shome.create();
        }
        catch(javax.ejb.CreateException ce){
          //ce.printStackTrace();
          throw new java.rmi.RemoteException(ce.getMessage());
        }
      }
      season.setCurrent(newKey);
      season.store();
  }

}
