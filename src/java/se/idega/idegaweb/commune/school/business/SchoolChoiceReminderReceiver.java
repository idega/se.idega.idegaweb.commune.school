package se.idega.idegaweb.commune.school.business;

import com.idega.core.location.data.Address;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoCustodianFound;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;

public class SchoolChoiceReminderReceiver implements Serializable {
	private final String studentName;
	private final String ssn;
	private final String parentName;
	private final String streetAddress;
	private final String postalAddress;

	SchoolChoiceReminderReceiver(MemberFamilyLogic familyLogic, UserBusiness userBusiness, Integer userId) throws RemoteException {
		User student = userBusiness.getUser(userId);
		studentName = student.getName();
		ssn = student.getPersonalID();
		Address address = userBusiness.getUsersMainAddress(student);
		Collection parents = null;
		try {
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
	}

	SchoolChoiceReminderReceiver(String studentName, String parentName, String ssn, String streetAddress, String postalAddress) {
		this.studentName = studentName;
		this.parentName = parentName;
		this.ssn = ssn;
		this.streetAddress = streetAddress;
		this.postalAddress = postalAddress;
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
}