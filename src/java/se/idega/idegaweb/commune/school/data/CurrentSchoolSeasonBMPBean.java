package se.idega.idegaweb.commune.school.data;

import com.idega.data.*;
import com.idega.block.school.data.SchoolSeason;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class CurrentSchoolSeasonBMPBean extends GenericEntity implements CurrentSchoolSeason {


  private static String ENTITY = "comm_sch_current_season";
  private static String CURRENT = "sid";

  public void initializeAttributes() {
    addAttribute(CURRENT,"Current season",true,true,Integer.class,ONE_TO_ONE,SchoolSeason.class);
    setAsPrimaryKey(CURRENT,true);
  }

  public String getEntityName() {
    return ENTITY;
  }

  public String getIDColumnName(){
    return CURRENT;
  }

  public void setCurrent(Integer id){
    //this.setColumn(CURRENT,id);
    this.setPrimaryKey(id);
  }
  public Integer getCurrent(){
    return getIntegerColumnValue(CURRENT);
  }

  public Object ejbFindCurrentSeason()throws javax.ejb.FinderException{
    return super.idoFindOnePKBySQL("select * from "+ENTITY);
  }

}