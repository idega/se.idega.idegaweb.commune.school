package se.idega.idegaweb.commune.school.data;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.user.data.User;
import java.sql.Timestamp;
import java.util.Collection;

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
  final static public String CASECODE = "MBSKOLV";

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

  private static final String[] CASE_STATUS_KEYS = {"UBEH","TYST","PREL","PLAC"};
  private static final String[] CASE_STATUS_DESCRIPTIONS = {"Case open","Sleep","Preliminary","Placed"};


  public void initializeAttributes() {
    addGeneralCaseRelation();
    this.addAttribute(CHILD,"child_id",true,true,Integer.class,MANY_TO_ONE,User.class);
    this.addAttribute(CURRENT_SCHOOL,"Current school",true,true,Integer.class,MANY_TO_ONE,School.class);
    this.addAttribute(GRADE,"Grade",Integer.class);
    this.addAttribute(CHOSEN_SCHOOL,"Chosen school",true,true,Integer.class,MANY_TO_ONE,School.class);

    this.addAttribute(WORK_SITUATION_1,"Work situation one",Integer.class);
    this.addAttribute(WORK_SITUATION_2,"Work situation two",Integer.class);
    this.addAttribute(LANGUAGECHOICE,"Language choice",String.class);
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
  public void setCurrentSchoolId(int id){
    setColumn(CURRENT_SCHOOL,id);
  }
  public int getChosenSchoolId(){
    return getIntColumnValue(CHOSEN_SCHOOL);
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

  public Collection ejbFindByChosenSchoolId(int chosenSchoolId)throws javax.ejb.FinderException{
    return idoFindIDsBySQL("select * from "+getEntityName()+" where "+CHOSEN_SCHOOL+" = "+chosenSchoolId);
  }

  public Collection ejbFindByChildId(int childId)throws javax.ejb.FinderException{
    return idoFindIDsBySQL("select * from "+getEntityName()+" where "+CHILD+" = "+childId);
  }

}