package se.idega.idegaweb.commune.school.data;

import java.util.Collection;
import com.idega.data.*;


/**
 * Bean handles SCB School mark codes
 * <p>
 * $Id: SCBCodeBMPBean.java,v 1.1 2003/09/25 07:59:14 kjell Exp $
 *
 * @author <a href="mailto:kjell@lindman.com">Kjell Lindman</a>
 * @version $version$
 */
public class SCBCodeBMPBean extends GenericEntity implements SCBCode {

  private static String ENTITY_NAME = "SCB_SCHOOLCODES";
 
  public void initializeAttributes() {
	addAttribute(getIDColumnName());
	addAttribute("SCB_SCHOOL_ID", "SCB ID", String.class);
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
	
	public Collection ejbFindAll() throws javax.ejb.FinderException {
		return idoFindPKsBySQL("select * from " + getEntityName());
	}
}
