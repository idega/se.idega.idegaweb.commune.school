package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOServiceBean;
import com.idega.block.school.data.*;
import com.idega.block.school.business.*;
import com.idega.data.IDOLookup;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.AbstractMap;
import java.util.Vector;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolChoiceHelperBusinessBean extends IBOServiceBean {

  public SchoolChoiceHelperBusinessBean() {
  }

  public Collection getSchoolChoiceHelperInCategory(String category)throws java.rmi.RemoteException,javax.ejb.FinderException{
    SchoolTypeHome stHome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
    SchoolAreaHome saHome = (SchoolAreaHome) IDOLookup.getHome(SchoolArea.class);
    Collection areas = saHome.findAllSchoolAreas();
    Collection types = stHome.findAllByCategory(category);
    if(areas!=null && types!=null){
        Vector v = new Vector();
        Iterator iterOne = types.iterator();

        SchoolType type;
        SchoolArea area;
        SchoolChoiceHelper helper;
        while(iterOne.hasNext()){
          Iterator iterTwo = areas.iterator();
          type = (SchoolType) iterOne.next();
          while(iterTwo.hasNext()){
            area = (SchoolArea)iterTwo.next();
            helper = new SchoolChoiceHelper(area,type);
            v.add(helper);
          }
        }
        return v;

    }
    return null;
  }


}