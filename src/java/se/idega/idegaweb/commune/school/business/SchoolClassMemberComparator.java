package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.text.Collator;
import java.util.Comparator;
import java.util.Map;
import java.util.Locale;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

/**
 * @author laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolClassMemberComparator implements Comparator {

  private Locale locale;
  private UserBusiness business;
  private Collator collator;
  private Map students;
  
  public SchoolClassMemberComparator(Locale locale, UserBusiness business, Map students) {
  	this.locale = locale;
  	this.business = business;
  	this.students = students;
  }
  
	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) {
		collator = Collator.getInstance(locale);
		
    try {
	   	if ( locale.getLanguage().equalsIgnoreCase("is") )
	    	return nameSort(o1,o2);
	    else
	    	return lastNameSort(o1,o2);
    }
    catch (RemoteException re) {
    	return 0;
    }
	}
	
	public int nameSort(Object o1, Object o2) throws RemoteException {
		User p1 = (User) students.get(new Integer(((SchoolClassMember)o1).getClassMemberId()));
		User p2 = (User) students.get(new Integer(((SchoolClassMember)o2).getClassMemberId()));
		
		String one = p1.getFirstName()!=null?p1.getFirstName():"";
		String two = p2.getFirstName()!=null?p2.getFirstName():"";
		int result = collator.compare(one,two);
		
		if (result == 0){
		  one = p1.getMiddleName()!=null?p1.getMiddleName():"";
		  two = p2.getMiddleName()!=null?p2.getMiddleName():"";
		  result = collator.compare(one,two);
		}
		
		if (result == 0){
			one = p1.getLastName()!=null?p1.getLastName():"";
			two = p2.getLastName()!=null?p2.getLastName():"";
			result = collator.compare(one,two);
		}

		return result;
	}
	
	public int lastNameSort(Object o1, Object o2) throws RemoteException {
		User p1 = business.getUser(((SchoolClassMember)o1).getClassMemberId());
		User p2 = business.getUser(((SchoolClassMember)o2).getClassMemberId());
		
		String one = p1.getLastName()!=null?p1.getLastName():"";
		String two = p2.getLastName()!=null?p2.getLastName():"";
		int result = collator.compare(one,two);
		
		if (result == 0){
		  one = p1.getFirstName()!=null?p1.getFirstName():"";
		  two = p2.getFirstName()!=null?p2.getFirstName():"";
		  result = collator.compare(one,two);
		}

		if (result == 0){
		  //result = p1.getMiddleName().compareTo(p2.getMiddleName());
		  one = p1.getMiddleName()!=null?p1.getMiddleName():"";
		  two = p2.getMiddleName()!=null?p2.getMiddleName():"";
		  result = collator.compare(one,two);
		}
		
		return result;
	}	

}
