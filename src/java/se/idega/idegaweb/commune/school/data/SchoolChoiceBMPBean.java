package se.idega.idegaweb.commune.school.data;

import com.idega.block.process.data.*;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.user.data.User;
import java.sql.Timestamp;
import java.util.Collection;

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

  private static final String[] CASE_STATUS_KEYS = {"UBEH","TYST","PREL","PLAC"};
  private static final String[] CASE_STATUS_DESCRIPTIONS = {"Case open","Sleep","Preliminary","Placed"};

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

  public void initializeAttributes() {
    addGeneralCaseRelation();
    this.addAttribute(CHILD,"child_id",true,true,Integer.class,MANY_TO_ONE,User.class);
    this.addAttribute(CURRENT_SCHOOL,"Current school",true,true,Integer.class,MANY_TO_ONE,School.class);
    this.addAttribute(GRADE,"Grade",Integer.class);
    this.addAttribute(CHOSEN_SCHOOL,"Chosen school",true,true,Integer.class,MANY_TO_ONE,School.class);
	this.addAttribute(SCHOOL_SEASON,"School season",true,true,Integer.class,MANY_TO_ONE,SchoolSeason.class);

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
    return super.idoFindPKsBySQL(sql.toString());
  }

}