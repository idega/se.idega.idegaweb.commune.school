package se.idega.idegaweb.commune.school.data;

import com.idega.block.process.data.*;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.data.EntityControl;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;
import com.idega.user.data.UserBMPBean;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolChoiceBMPBean extends AbstractCaseBMPBean implements SchoolChoice,Case {

  final static public String SCHOOLCHOICE = "comm_sch_choice";
  final static public String CASECODE =SchoolChoiceBusinessBean.SCHOOL_CHOICE_CASECODE;

  public final static String SCHOOL_SEASON = "school_season_id";
  public final static String SCHOOL_TYPE = "school_type_id";
  public final static String CURRENT_SCHOOL = "curr_school_id";
  public final static String GRADE = "grade";
  public final static String CHOSEN_SCHOOL = "school_id";
  public final static String CHILD = "child_id";

  public final static String CHANGEOFSCHOOL = "change_of_school";
  public final static String KEEPCHILDRENCARE = "keep_children_care";
  public final static String AUTOASSIGNMENT = "auto_assignment";
  public final static String CUSTODIANSAGGREE = "custodians_agree";
  public final static String SCHOOLCATALOGUE = "school_catalogue";

  public final static String LANGUAGECHOICE = "language_choice";
  public final static String SCHOOLCHOICEDATE = "school_choice_date";
  public final static String MESSAGE = "message_body";
  public final static String CHOICEORDER = "choice_order";
  public final static String METHOD = "choice_method";

  public final static String WORK_SITUATION_1 = "work_situation_1";
  public final static String WORK_SITUATION_2 = "work_situation_2";
  public final static String GROUP_PLACE = "group_place";
  
  public final static String CASE_STATUS_CREATED     = "UBEH";
  public final static String CASE_STATUS_QUIET       = "TYST";
  public final static String CASE_STATUS_PRELIMINARY = "PREL";
  public final static String CASE_STATUS_PLACED      = "PLAC";
  public final static String CASE_STATUS_GROUPED     = "GROU";
  public final static String CASE_STATUS_MOVED       = "FLYT";
  
  private static final String[] CASE_STATUS_KEYS = {"UBEH","TYST","PREL","PLAC","GROU","FLYT"};
  private static final String[] CASE_STATUS_DESCRIPTIONS = {"Case open","Sleep","Preliminary","Placed","Grouped","Moved"};

  public String getCaseStatusCreated(){
    return "UBEH";
  }
  public String getCaseStatusQuiet(){
    return "TYST";
  }
  public String getCaseStatusPreliminary(){
    return "PREL";
  }
  public String getCaseStatusPlaced(){
    return "PLAC";
  }
  public String getCaseStatusGrouped(){
    return "GROU";
  }
  public String getCaseStatusMoved(){
    return "FLYT";
  }

  public void initializeAttributes() {
    addGeneralCaseRelation();
    this.addAttribute(CHILD,"child_id",true,true,Integer.class,MANY_TO_ONE,User.class);
    this.addAttribute(CURRENT_SCHOOL,"Current school",true,true,Integer.class,MANY_TO_ONE,School.class);
    this.addAttribute(GRADE,"Grade",Integer.class);
    this.addAttribute(CHOSEN_SCHOOL,"Chosen school",true,true,Integer.class,MANY_TO_ONE,School.class);
	this.addAttribute(SCHOOL_SEASON,"School season",true,true,Integer.class,MANY_TO_ONE,SchoolSeason.class);
    this.addAttribute(SCHOOL_TYPE,"School type",true,true,Integer.class,MANY_TO_ONE,SchoolType.class);

    this.addAttribute(WORK_SITUATION_1,"Work situation one",Integer.class);
    this.addAttribute(WORK_SITUATION_2,"Work situation two",Integer.class);
    this.addAttribute(LANGUAGECHOICE,"Language choice",String.class);
    this.addAttribute(GROUP_PLACE,"Language choice",String.class,10);
    this.addAttribute(SCHOOLCHOICEDATE,"choice date",Timestamp.class);
    this.addAttribute(MESSAGE,"message",String.class,4000);
    this.addAttribute(CHOICEORDER,"choice order",Integer.class);
    this.addAttribute(METHOD,"method",Integer.class);

    this.addAttribute(CHANGEOFSCHOOL,"Change of school",Boolean.class);
    this.addAttribute(KEEPCHILDRENCARE,"Keep children care",Boolean.class);
    this.addAttribute(AUTOASSIGNMENT,"Autoassignment",Boolean.class);
    this.addAttribute(CUSTODIANSAGGREE,"Custodian agree",Boolean.class);
    this.addAttribute(SCHOOLCATALOGUE,"School catalogue",Boolean.class);


    //this.addManyToOneRelationship(CURRENT_SCHOOL,School.class);
    //this.addManyToOneRelationship(CHOSEN_SCHOOL,School.class);
    //this.addManyToOneRelationship(CHILD,User.class);


  }
  public String getEntityName() {
    return SCHOOLCHOICE;
  }
  public String getCaseCodeKey() {
    return CASECODE;
  }
  public String getCaseCodeDescription() {
    return "School choice application";
  }

  public String[] getCaseStatusKeys(){
    return CASE_STATUS_KEYS;
  }
  public String[] getCaseStatusDescriptions(){
    return CASE_STATUS_DESCRIPTIONS;
  }

  public int getChildId(){
    return getIntColumnValue(CHILD);
  }
  public void setChildId(int id){
    setColumn(CHILD,id);
  }
  public int getCurrentSchoolId(){
    return getIntColumnValue(CURRENT_SCHOOL);
  }
  public School setCurrentSchool(){
    return (School) getColumnValue(CURRENT_SCHOOL);
  }
  public void setCurrentSchoolId(int id){
    setColumn(CURRENT_SCHOOL,id);
  }
   public int getSchoolSeasonId(){
    return getIntColumnValue(SCHOOL_SEASON);
  }
  public void setSchoolSeasonId(int id){
    setColumn(SCHOOL_SEASON,id);
  }
   public int getSchoolTypeId(){
    return getIntColumnValue(SCHOOL_TYPE);
  }
  public void setSchoolTypeId(int id){
    setColumn(SCHOOL_TYPE,id);
  }
  public int getChosenSchoolId(){
    return getIntColumnValue(CHOSEN_SCHOOL);
  }
  public School getChosenSchool(){
    return (School) getColumnValue(CHOSEN_SCHOOL);
  }
  public void setChosenSchoolId(int id){
    setColumn(CHOSEN_SCHOOL,id);
  }
  public int getGrade(){
    return getIntColumnValue(GRADE);
  }
  public void setGrade(int grade){
    setColumn(GRADE,grade);
  }
  public int getWorkSituation1(){
    return getIntColumnValue(WORK_SITUATION_1);
  }
  public void setWorksituation1(int situation){
    setColumn(WORK_SITUATION_1,situation);
  }
  public int getWorkSituation2(){
    return getIntColumnValue(WORK_SITUATION_1);
  }
  public void setWorksituation2(int situation){
    setColumn(WORK_SITUATION_2,situation);
  }
  public String getLanguageChoice(){
    return getStringColumnValue(LANGUAGECHOICE);
  }
  public void setLanguageChoice(String language){
    setColumn(LANGUAGECHOICE,language);
  }

  public String getGroupPlace(){
  	return getStringColumnValue(GROUP_PLACE);
  }

  public void setGroupPlace(String place){
  	setColumn(GROUP_PLACE,place);
  }

  public Timestamp getSchoolChoiceDate(){
    return (Timestamp) getColumnValue(SCHOOLCHOICEDATE);
  }
  public void setSchoolChoiceDate(Timestamp stamp){
    setColumn(SCHOOLCHOICEDATE,stamp);
  }
  public String getMessage(){
    return getStringColumnValue(MESSAGE);
  }
  public void setMessage(String msg){
    setColumn(MESSAGE,msg);
  }
  public int getChoiceOrder(){
    return getIntColumnValue(CHOICEORDER);
  }
  public void setChoiceOrder(int order){
    setColumn(CHOICEORDER,order);
  }
  public int getMethod(){
    return getIntColumnValue(METHOD);
  }
  public void setMethod(int method){
    setColumn(METHOD,method);
  }
  public boolean getChangeOfSchool(){
    return getBooleanColumnValue(CHANGEOFSCHOOL);
  }
  public void setChangeOfSchool(boolean change){
    setColumn(CHANGEOFSCHOOL,change);
  }
   public boolean getKeepChildrenCare(){
    return getBooleanColumnValue(KEEPCHILDRENCARE);
  }
  public void setKeepChildrenCare(boolean keepchildcare){
    setColumn(KEEPCHILDRENCARE,keepchildcare);
  }
   public boolean getAutoAssign(){
    return getBooleanColumnValue(AUTOASSIGNMENT);
  }
  public void setAutoAssign(boolean auto){
    setColumn(AUTOASSIGNMENT,auto);
  }
   public boolean getCustodiansAgree(){
    return getBooleanColumnValue(CUSTODIANSAGGREE);
  }
  public void setCustodiansAgree(boolean agree){
    setColumn(CUSTODIANSAGGREE,agree);
  }
   public boolean getSchoolCatalogue(){
    return getBooleanColumnValue(SCHOOLCATALOGUE);
  }
  public void setSchoolCatalogue(boolean catalogue){
    setColumn(SCHOOLCATALOGUE,catalogue);
  }

  public Collection ejbFindByChosenSchoolId(int chosenSchoolId,int schoolSeasonId)throws javax.ejb.FinderException{
    return idoFindPKsBySQL("select * from "+getEntityName()+" where "+CHOSEN_SCHOOL+" = "+chosenSchoolId+" and "+SCHOOL_SEASON+" = "+schoolSeasonId);
  }

  public Collection ejbFindByChildId(int childId)throws javax.ejb.FinderException{
    return idoFindPKsBySQL("select * from "+getEntityName()+" where "+CHILD+" = "+childId);
  }

  public Collection ejbFindByChildId(int childId,int schoolSeasonId)throws javax.ejb.FinderException{
    return idoFindPKsBySQL("select * from "+getEntityName()+" where "+CHILD+" = "+childId+" and "+SCHOOL_SEASON+" = "+schoolSeasonId);
  }

  public Collection ejbFindByCodeAndStatus(String caseCode,String[] caseStatus, int schoolId,int schoolSeasonId)throws javax.ejb.FinderException{
  	return ejbFindByCodeAndStatus(caseCode,caseStatus,schoolId,schoolSeasonId,null);
  }

  public Collection ejbFindByCodeAndStatus(String caseCode,String[] caseStatus, int schoolId,int schoolSeasonId,String ordered)throws javax.ejb.FinderException{

    StringBuffer sql = new StringBuffer("select s.* from ");
    sql.append(getEntityName()).append( " s ");
    sql.append(",").append(CaseBMPBean.TABLE_NAME).append(" c ");
    sql.append(" where s.COMM_SCH_CHOICE_ID = c.PROC_CASE_ID ");
    //try{

    sql.append(" and c.CASE_CODE = '").append(caseCode).append("' ");
    sql.append(" and s.SCHOOL_ID = ").append(schoolId);
    sql.append(" and ").append(SCHOOL_SEASON).append(" = ").append(schoolSeasonId);
    sql.append(" and c.CASE_STATUS in (");
    for (int i = 0; i < caseStatus.length; i++) {
      if(i>0)
        sql.append(",");
      sql.append("'");
      sql.append(caseStatus[i]);
      sql.append("'");
    }
    sql.append(" ) ");
    if(ordered!=null && !"".equals(ordered)){
    	sql.append(" order by ").append(ordered);
    }
    /*}
    catch(java.rmi.RemoteException ex){}*/
    //System.err.println(" \n "+sql.toString()+" \n");
    return (Collection)super.idoFindPKsBySQL(sql.toString());

  }
  
  public int ejbHomeGetNumberOfApplications(String caseStatus, int schoolID, int schoolSeasonID) throws FinderException, IDOException {
    StringBuffer sql = new StringBuffer("select count(*) from ");
    sql.append(getEntityName()).append( " s ");
    sql.append(",").append(CaseBMPBean.TABLE_NAME).append(" c ");
    sql.append(" where s.COMM_SCH_CHOICE_ID = c.PROC_CASE_ID ");
    sql.append(" and c.CASE_CODE = '").append(CASECODE).append("'");
    sql.append(" and s.SCHOOL_ID = ").append(schoolID);
    sql.append(" and s.").append(SCHOOL_SEASON).append(" = ").append(schoolSeasonID);
    sql.append(" and c.CASE_STATUS = '").append(caseStatus).append("'");
    
    return super.idoGetNumberOfRecords(sql.toString());
  }

  public int ejbHomeGetNumberOfApplications(String caseStatus, int schoolID, int schoolSeasonID, int grade) throws FinderException, IDOException {
    StringBuffer sql = new StringBuffer("select count(*) from ");
    sql.append(getEntityName()).append( " s ");
    sql.append(",").append(CaseBMPBean.TABLE_NAME).append(" c ");
    sql.append(" where s.COMM_SCH_CHOICE_ID = c.PROC_CASE_ID ");
    sql.append(" and c.CASE_CODE = '").append(CASECODE).append("'");
    sql.append(" and s.SCHOOL_ID = ").append(schoolID);
    sql.append(" and s.").append(SCHOOL_SEASON).append(" = ").append(schoolSeasonID);
    sql.append(" and s.").append(GRADE).append(" = ").append(grade);
    sql.append(" and c.CASE_STATUS = '").append(caseStatus).append("'");

    return super.idoGetNumberOfRecords(sql.toString());
  }

  public Collection ejbFindByChildAndSeason(int childID, int seasonID)throws javax.ejb.FinderException{
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(SCHOOLCHOICE);
    sql.append(" where ");
    sql.append(CHILD);
    sql.append(" = ");
    sql.append(childID);
    sql.append(" and ");
    sql.append(SCHOOL_SEASON);
    sql.append(" = ");
    sql.append(seasonID);
    sql.append(" order by ");
    sql.append(CHOICEORDER);
    return super.idoFindPKsBySQL(sql.toString());
  }
  
  public Collection ejbFindByChildAndSchoolAndSeason(int childID, int schoolID, int seasonID)throws javax.ejb.FinderException{
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(SCHOOLCHOICE);
    sql.append(" where ");
    sql.append(CHILD);
    sql.append(" = ");
    sql.append(childID);
    sql.append(" and ");
    sql.append(SCHOOL_SEASON);
    sql.append(" = ");
    sql.append(seasonID);
    sql.append(" and ");
    sql.append(CHOSEN_SCHOOL);
    sql.append(" = ");
    sql.append(schoolID);
    return super.idoFindPKsBySQL(sql.toString());
  }

	public Collection ejbFindAll() throws FinderException {
		return this.idoFindIDsBySQL("select * from "+getEntityName());	
	}

  public Collection ejbFindChoices(int schoolID, int seasonID, int gradeYear, String[] validStatuses, String searchStringForUser) throws FinderException, RemoteException {
		return ejbFindChoices(schoolID, seasonID, gradeYear, validStatuses, searchStringForUser);
  }
  
  public Collection ejbFindChoices(int schoolID, int seasonID, int gradeYear, int[] choiceOrder ,String[] validStatuses, String searchStringForUser) throws FinderException, RemoteException {
  	boolean search = searchStringForUser != null && !searchStringForUser.equals("");
  	boolean statuses = validStatuses != null && validStatuses.length > 0;
  	
  	if (schoolID < 1 && seasonID < 1 && gradeYear < 1 && !search && !statuses) {
  		return ejbFindAll();
  	} 
  	
  	boolean needAnd = false;

  	IDOQuery query = new IDOQuery();
  	query.appendSelectAllFrom(getEntityName());

  	if (search) {
  		query.append(", ").append(UserBMPBean.TABLE_NAME);
  	}
  	if (statuses) {
  		query.append(", ").append(CaseBMPBean.TABLE_NAME);
  	}
  	
  	query.appendWhere();
  	
  	if (statuses) {
  		query.append(getEntityName()).append(".").append(getIDColumnName())
  		.appendEqualSign().append(CaseBMPBean.TABLE_NAME).append(".").append(CaseBMPBean.TABLE_NAME+"_ID")
  		.appendAnd().append("(");
  		for (int i = 0; i < validStatuses.length; i++) {
  			if (i != 0) {
  				query.appendOr();	
  			}
	  		query.append(CaseBMPBean.TABLE_NAME).append(".").append(CaseBMPBean.COLUMN_CASE_STATUS)
	  		.append(" like '").append(validStatuses[i]).append("'");
  		}
  		query.append(")");
  		needAnd = true;
  	}
  	
  	if (search) {
  		if (needAnd) {
				query.appendAnd();
  		}
  		query.append(getEntityName()).append(".").append(CHILD)
  		.appendEqualSign().append(UserBMPBean.TABLE_NAME).append(".").append(UserBMPBean.getColumnNameUserID())
  		.appendAnd().append("(");
  		query.append(UserBMPBean.TABLE_NAME).append(".").append(UserBMPBean.getColumnNameFirstName())
  		.append(" like '%").append(searchStringForUser).append("%'")
  		.appendOr().append(UserBMPBean.TABLE_NAME).append(".").append(UserBMPBean.getColumnNameLastName())
  		.append(" like '%").append(searchStringForUser).append("%'")
  		.appendOr().append(UserBMPBean.TABLE_NAME).append(".").append(UserBMPBean.getColumnNameMiddleName())
  		.append(" like '%").append(searchStringForUser).append("%'")
  		.appendOr().append(UserBMPBean.TABLE_NAME).append(".").append(UserBMPBean.getColumnNamePersonalID())
  		.append(" like '%").append(searchStringForUser).append("%'");
  		query.append(")");
  		needAnd = true;;
  	}
  	
  	if (seasonID > 0) {
			if (needAnd) {
				query.appendAnd();
			}
  		query.append(SCHOOL_SEASON).appendEqualSign().append(seasonID);
  		needAnd = true;
  	}

  	if (schoolID > 0) {
			if (needAnd) {
				query.appendAnd();
			}
  		query.append(CHOSEN_SCHOOL).appendEqualSign().append(schoolID);	
  		needAnd = true;
  	}

  	if (gradeYear > 0) {
			if (needAnd) {
				query.appendAnd();
			}
  		query.append(GRADE).appendEqualSign().append(gradeYear);	
  		needAnd = true;
  	}
  	
  	if (choiceOrder != null && choiceOrder.length > 0) {
  		if (needAnd) {
  			query.appendAnd();	
  		}
  		query.append(CHOICEORDER).append(" in (");
  		for (int i = 0; i < choiceOrder.length; i++) {
  			if (	i != 0 ) {
  				query.append(", ");	
  			}
  			query.append(choiceOrder[i]);
  		}
  		query.append(")");
  		needAnd = true;
  	}
  	
  	//System.out.println("[SchoolChoiceBean] sql : "+query.toString());
  	return this.idoFindPKsByQuery(query);
  }
  
  public Collection ejbFindBySchoolAndSeasonAndGrade(int schoolID, int seasonID, int gradeYear) throws FinderException {
  	IDOQuery sql = new IDOQuery();
  	sql.appendSelectAllFrom(getEntityName()).appendWhere()
  	.append(CHOSEN_SCHOOL).appendEqualSign().append(schoolID)
  	.appendAnd().append(SCHOOL_SEASON).appendEqualSign().append(seasonID)
  	.appendAnd().append(GRADE).appendEqualSign().append(gradeYear);

  	return super.idoFindPKsBySQL(sql.toString());
  }

}