package se.idega.idegaweb.commune.school.data;

import java.util.Collection;
import com.idega.data.*;
import com.idega.block.school.data.School;

/**
 * Bean handles SCB School mark codes
 * <p>
 * $Id: SCBCodeBMPBean.java,v 1.3 2003/12/02 11:36:39 kjell Exp $
 *
 * @author <a href="mailto:kjell@lindman.com">Kjell Lindman</a>
 * @version $version$
 */
public class SCBCodeBMPBean extends GenericEntity implements SCBCode {

  private static String ENTITY_NAME = "SCB_SCHOOLCODES";
 
  public void initializeAttributes() {
	addAttribute(getIDColumnName(), "School ID", true, true, 
					Integer.class, "many-to-one", School.class);
	addAttribute("SCB_SCHOOL_ID", "SCB ID", String.class);
	addAttribute("SCHOOL_NAME", "School name taken from the SCB import", String.class);
	addAttribute("SCHOOL_ORDER", "School order", Integer.class);
	setAsPrimaryKey(getIDColumnName(), true);
  }

  public String getEntityName() {
	return ENTITY_NAME;
  }

	public String getIDColumnName(){
		return "SCH_SCHOOL_ID";
	}

	public String getCode(){
	  return getStringColumnValue("SCB_SCHOOL_ID");
	}

	public int getOrder(){
	  return getIntColumnValue("SCHOOL_ORDER");
	}
	
	public School getSchool(){
	  return (School) getColumnValue(getIDColumnName());
	}
	
	public Collection ejbFindAll() throws javax.ejb.FinderException {
		String sql = 	"select SCB_SCHOOLCODES.*, " +						"SCH_SCHOOL.SCHOOL_NAME as REAL_SCHOOL, " +						"SCH_SCHOOL.MANAGMENT_TYPE as REAL_TYPE " +						" from " +						"SCB_SCHOOLCODES, SCH_SCHOOL " +
						" where " +						"SCB_SCHOOLCODES.SCH_SCHOOL_ID = SCH_SCHOOL.SCH_SCHOOL_ID " +						"order by SCHOOL_ORDER";
	
		return idoFindPKsBySQL(sql);
	}
}
