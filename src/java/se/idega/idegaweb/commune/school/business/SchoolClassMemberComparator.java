package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.text.Collator;
import java.util.Comparator;
import java.util.Map;
import java.util.Locale;

import se.idega.util.PIDChecker;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.location.data.Address;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.LocaleUtil;

/**
 * @author laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolClassMemberComparator implements Comparator {

  public static final int NAME_SORT = 1;
  public static final int GENDER_SORT = 2;
  public static final int ADDRESS_SORT = 3;
  public static final int PERSONAL_ID_SORT = 4;
  public static final int LANGUAGE_SORT = 5;
  
  private Locale locale;
  private UserBusiness business;
  private Collator collator;
  private Map students;
  private int sortBy = NAME_SORT;
  
  public SchoolClassMemberComparator(Locale locale, UserBusiness business, Map students) {
  	this(NAME_SORT, locale, business, students);
  }
  
  public SchoolClassMemberComparator(int sortBy, Locale locale, UserBusiness business, Map students) {
  	this.sortBy = sortBy;
  	this.locale = locale;
  	this.business = business;
  	this.students = students;
  }
  
	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) {
		collator = Collator.getInstance(locale);
		int result = 0;
		
    try {
    	switch (sortBy) {
				case NAME_SORT :
					if (locale.equals(LocaleUtil.getIcelandicLocale())) {
						result = firstNameSort(o1,o2);
					}
					else {
						result = lastNameSort(o1,o2);
					}
					break;
				case GENDER_SORT :
					result = genderSort(o1,o2);
					break;
				case ADDRESS_SORT :
					result = addressSort(o1,o2);
					break;
				case LANGUAGE_SORT :
					result = languageSort(o1,o2);
					break;
				case PERSONAL_ID_SORT :
					result = personalIDSort(o1,o2);
					break;
			}
    }
    catch (RemoteException re) {
    	result = 0;
    }
    
    return result;
	}
	
	public int lastNameSort(Object o1, Object o2) {
		User p1 = (User) students.get(new Integer(((SchoolClassMember)o1).getClassMemberId()));
		User p2 = (User) students.get(new Integer(((SchoolClassMember)o2).getClassMemberId()));
		
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

	public int firstNameSort(Object o1, Object o2) {
		User p1 = (User) students.get(new Integer(((SchoolClassMember)o1).getClassMemberId()));
		User p2 = (User) students.get(new Integer(((SchoolClassMember)o2).getClassMemberId()));
		
		String one = p1.getFirstName()!=null?p1.getFirstName():"";
		String two = p2.getFirstName()!=null?p2.getFirstName():"";
		int result = collator.compare(one,two);
		
		if (result == 0){
		  //result = p1.getMiddleName().compareTo(p2.getMiddleName());
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

	public int genderSort(Object o1, Object o2) {
		User p1 = (User) students.get(new Integer(((SchoolClassMember)o1).getClassMemberId()));
		User p2 = (User) students.get(new Integer(((SchoolClassMember)o2).getClassMemberId()));
		int result = 0;
		
		boolean isFemale1 = PIDChecker.getInstance().isFemale(p1.getPersonalID());
		boolean isFemale2 = PIDChecker.getInstance().isFemale(p2.getPersonalID());
		
		if (isFemale1 && !isFemale2)
			result = -1;
		if (!isFemale1 && isFemale2)
			result = 1;
		
		if (result == 0){
		  result = lastNameSort(o1,o2);
		}

		return result;
	}	

	public int addressSort(Object o1, Object o2) throws RemoteException {
		Address p1 = business.getUserAddress1(((SchoolClassMember)o1).getClassMemberId());
		Address p2 = business.getUserAddress1(((SchoolClassMember)o2).getClassMemberId());
		
		if (p1 == null || p2 == null) {
			if (p1 == null && p2 != null)
				return 1;
			else if (p1 != null && p2 == null)
				return -1;
			return 0;
		}
			
		String one = p1.getStreetAddress()!=null?p1.getStreetAddress():"";
		String two = p2.getStreetAddress()!=null?p2.getStreetAddress():"";
		int result = collator.compare(one,two);
				
		return result;
	}	

	public int personalIDSort(Object o1, Object o2) {
		User p1 = (User) students.get(new Integer((((SchoolClassMember)o1).getClassMemberId())));
		User p2 = (User) students.get(new Integer((((SchoolClassMember)o2).getClassMemberId())));
		
		String pID1 = p1.getPersonalID() != null ? p1.getPersonalID() : "";
		String pID2 = p2.getPersonalID() != null ? p2.getPersonalID() : "";
		
		return collator.compare(pID1,pID2);
	}	

	public int languageSort(Object o1, Object o2) {
		SchoolClassMember p1 = (SchoolClassMember) o1;
		SchoolClassMember p2 = (SchoolClassMember) o2;
		
		String one = p1.getLanguage()!=null?p1.getLanguage():"";
		String two = p2.getLanguage()!=null?p2.getLanguage():"";
		int result = collator.compare(one,two);
				
		if (result == 0){
		  result = lastNameSort(o1,o2);
		}

		return result;
	}	
}