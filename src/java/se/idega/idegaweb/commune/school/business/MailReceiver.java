package se.idega.idegaweb.commune.school.business;

import com.idega.core.location.data.Address;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoCustodianFound;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;

public class MailReceiver implements Serializable {
	private  String studentName;
	private  String ssn;
	private  String parentName;
	private  String streetAddress;
	private  String postalAddress;
	private  boolean isInDefaultCommune;
	
	public MailReceiver(){
	    
	}

	MailReceiver(FamilyLogic familyLogic, UserBusiness userBusiness, Integer userId) throws RemoteException, FinderException {
		User student = userBusiness.getUser(userId);
		studentName = student.getName();
		ssn = student.getPersonalID();
		Address address = userBusiness.getUsersMainAddress(student);
		Collection parents = null;
		try {
		    if(familyLogic!=null)
			 parents = familyLogic.getCustodiansFor(student);
		}
		catch (NoCustodianFound e) {
			parents = null;
		}
		if (parents == null || parents.isEmpty()) {
			parentName = "?";
		}
		else {
			User parent = (User) parents.iterator().next();
			parentName = parent.getName();
		}
		streetAddress = address != null ? address.getStreetAddress() : "?";
		postalAddress = address != null ? address.getPostalAddress() : "?";
		isInDefaultCommune = userBusiness.isInDefaultCommune (student);
	}

	public String getStudentName() {
		return studentName;
	}

	public String getSsn() {
		return ssn;
	}

	public String getParentName() {
		return parentName;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public boolean isInDefaultCommune () {
		return isInDefaultCommune;
	}
	
    public void setInDefaultCommune(boolean isInDefaultCommune) {
        this.isInDefaultCommune = isInDefaultCommune;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
