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
      SchoolChoice choice = null;
      for (int i = 0; i < caseCount; i++) {

        choice = createSchoolChoice(userId,childId,current_school,
        schoolIds[i],grade,i+1,method,workSituation1,workSituation2,
        language,message,time,changeOfSchool,keepChildrenCare,autoAssign,
        custodiansAgree,schoolCatalogue,i==0?first:other,choice);
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
      CaseStatus caseStatus,
      Case parentCase
      )throws CreateException,RemoteException{
    SchoolChoiceHome home = this.getSchoolChoiceHome();
    SchoolChoice choice = home.create();
    SchoolSeason season = null;
    try{
      choice.setOwner(getUser(userId));
      season = getCurrentSeason();
    }
    catch(FinderException fex){
      throw new IDOCreateException(fex);
    }

    choice.setChildId(childId);
    if(current_school >0)
   		choice.setCurrentSchoolId(current_school);
    choice.setChosenSchoolId(chosen_school);
    //if(grade >0 )
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
    if(season!=null){
    	Integer seasonId = (Integer)season.getPrimaryKey();
    	choice.setSchoolSeasonId(seasonId.intValue());
    }
    	

    choice.setCaseStatus(caseStatus);
    if(parentCase!=null)
   		choice.setParentCase(parentCase);


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

	public void createTestFamily(){

	  javax.transaction.UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();

			com.idega.user.data.User[] familyMembers = new com.idega.user.data.User[5];
			is.idega.idegaweb.member.business.MemberFamilyLogic ml = (is.idega.idegaweb.member.business.MemberFamilyLogic)getServiceInstance(is.idega.idegaweb.member.business.MemberFamilyLogic.class);
		  se.idega.idegaweb.commune.business.CommuneUserBusiness ub = (se.idega.idegaweb.commune.business.CommuneUserBusiness) getServiceInstance(se.idega.idegaweb.commune.business.CommuneUserBusiness.class);

			familyMembers[0] = ub.createCitizenByPersonalIDIfDoesNotExist("Gunnar","Páll","Daníelsson","0101");
			familyMembers[1] = ub.createCitizenByPersonalIDIfDoesNotExist("Páll","","Helgason","0202");
			familyMembers[2] = ub.createCitizenByPersonalIDIfDoesNotExist("Tryggvi","","Lárusson","0303");
			familyMembers[3] = ub.createCitizenByPersonalIDIfDoesNotExist("Þórhallur","","Daníelsson","0404");
			familyMembers[4] = ub.createCitizenByPersonalIDIfDoesNotExist("Jón","Karl","Jónsson","0505");

			ml.setAsChildFor(familyMembers[1],familyMembers[0]);
			ml.setAsChildFor(familyMembers[2],familyMembers[0]);
			ml.setAsChildFor(familyMembers[3],familyMembers[1]);
			ml.setAsChildFor(familyMembers[4],familyMembers[1]);


			trans.commit();
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (Exception e) {

			}


		}
	}
	
	public boolean noRoomAction(Integer pk){
		javax.transaction.UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();
			SchoolChoice choice = this.getSchoolChoiceHome().findByPrimaryKey(pk);
			choice.setCaseStatus(getCaseStatus("TYST"));
			choice.store();
			Iterator children = choice.getChildren();
			if(children.hasNext()){
				Case child = (Case) children.next();
				child.setCaseStatus(getCaseStatusOpen());
				child.store();
			}
		
			trans.commit();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try {
				trans.rollback();
			}
			catch (Exception e) {
				e.printStackTrace();
			}


		}
		return false;
	}
	
	public boolean  preliminaryAction(Integer pk){
		try {
			SchoolChoice choice = getSchoolChoiceHome().findByPrimaryKey(pk);
			choice.setCaseStatus(getCaseStatus("PREL"));
			choice.store();
			return true;		    
		}
		catch (Exception e) {
		}	
		return false;
	}
	
	public boolean groupPlaceAction(Integer pk , String group){
		try {
			SchoolChoice choice = getSchoolChoiceHome().findByPrimaryKey(pk);
			choice.setGroupPlace(group);
			choice.setCaseStatus(getCaseStatus("PLAC"));
			choice.store();
			return true;		    
		}
		catch (Exception e) {
		}	
		return false;
	}

}
