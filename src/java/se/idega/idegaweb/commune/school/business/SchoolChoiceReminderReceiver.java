package se.idega.idegaweb.commune.school.business;

import com.idega.core.data.Address;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoCustodianFound;
import java.rmi.RemoteException;
import java.util.Collection;

public class SchoolChoiceReminderReceiver {
    private final String studentName;
    private final String ssn;
    private final String parentName;
    private final String streetAddress;
    private final String postalAddress;

    SchoolChoiceReminderReceiver
        (final MemberFamilyLogic familyLogic, final UserBusiness userBusiness,
         final Integer userId) throws RemoteException, NoCustodianFound {
        final User student = userBusiness.getUser (userId);
        studentName = student.getName ();
        ssn = student.getPersonalID();
        final Collection parents = familyLogic.getCustodiansFor(student);
        final User parent = (User) parents.iterator ().next ();
        parentName = parent.getName ();
        final int parentId
                = ((Integer) parent.getPrimaryKey ()).intValue ();
        final Address address = userBusiness.getUserAddress1 (parentId);
        streetAddress = address.getStreetAddress();
        postalAddress = address.getPostalAddress();
    }
    
    SchoolChoiceReminderReceiver
        (final String studentName, final String parentName, final String ssn,
         final String streetAddress, final String postalAddress) {
        this.studentName = studentName;
        this.parentName = parentName;
        this.ssn = ssn;
        this.streetAddress = streetAddress;
        this.postalAddress = postalAddress;
    }

    public String getStudentName () {
        return studentName;
    }

    public String getSsn () {
        return ssn;
    }

    public String getParentName () {
        return parentName;
    }

    public String getStreetAddress () {
        return streetAddress;
    }

    public String getPostalAddress () {
        return postalAddress;
    }
}
