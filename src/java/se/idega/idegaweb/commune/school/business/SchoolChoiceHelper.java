package se.idega.idegaweb.commune.school.business;

import java.util.Collection;
import com.idega.block.school.data.*;
import com.idega.block.school.business.*;
import com.idega.data.IDOLookup;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolChoiceHelper {

  Collection schools;
  SchoolArea schoolArea;
  SchoolType schoolType;

  public SchoolChoiceHelper(SchoolArea area,SchoolType type)throws java.rmi.RemoteException,javax.ejb.FinderException {
    this.schoolArea = area;
    this.schoolType = type;
    init();
  }

  private void init()throws java.rmi.RemoteException,javax.ejb.FinderException{
/*
    if(schoolArea!=null && schoolType !=null){
      SchoolHome schoolHome = (SchoolHome) IDOLookup.getHome(School.class);
      int areaId = ((Integer) schoolArea.getPrimaryKey()).intValue();
      int typeId = ((Integer) schoolArea.getPrimaryKey()).intValue();
      schools = schoolHome.findAllBySchoolAreaAndType(areaId,typeId);
    }
  */
  }

  public SchoolArea getSchoolArea(){
    return this.schoolArea;
  }
  public SchoolType getSchoolType(){
    return this.schoolType;
  }
  public Collection getSchools(){
    return schools;
  }
}