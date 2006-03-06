/**
 * 
 */
package se.idega.idegaweb.commune.school.business;

import se.idega.idegaweb.commune.school.data.SchoolChoice;
import com.idega.block.process.business.CaseChangeEvent;
import com.idega.block.school.data.SchoolClassMember;


/**
 * <p>
 * TODO tryggvil Describe Type SchoolChoiceApprovalEvent
 * </p>
 *  Last modified: $Date: 2006/03/06 13:02:01 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:tryggvil@idega.com">tryggvil</a>
 * @version $Revision: 1.1 $
 */
public class SchoolChoiceApprovalEvent extends CaseChangeEvent {

	/**
	 * @param myCase
	 */
	public SchoolChoiceApprovalEvent(SchoolChoice choice,SchoolClassMember placement) {
		super(choice);
		setSchoolPlacement(placement);
	}
	

	/**
	 * @return Returns the schoolPlacement.
	 */
	public SchoolClassMember getSchoolPlacement() {
		return (SchoolClassMember) getAttributes().get("schoolPlacement");
	}

	
	public SchoolChoice getSchoolChoice(){
		return (SchoolChoice)getCase();
	}
	
	
	/**
	 * @param schoolPlacement The schoolPlacement to set.
	 */
	public void setSchoolPlacement(SchoolClassMember schoolPlacement) {
		getAttributes().put("schoolPlacement",schoolPlacement);
	}
}
