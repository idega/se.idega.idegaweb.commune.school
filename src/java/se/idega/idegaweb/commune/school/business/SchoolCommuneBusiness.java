package se.idega.idegaweb.commune.school.business;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.data.IDOException;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface SchoolCommuneBusiness extends com.idega.business.IBOService,com.idega.block.process.business.CaseBusiness
{
 public void addSchoolAdministrator(com.idega.user.data.User p0)throws java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.CreateException, java.rmi.RemoteException;
 public boolean canMarkSchoolClass(com.idega.block.school.data.SchoolClass p0,java.lang.String p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void finalizeGroup(com.idega.block.school.data.SchoolClass p0,java.lang.String p1,java.lang.String p2, boolean confirmation)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getChosenSchoolID(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getCurrentSchoolSeason()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int getCurrentSchoolSeasonID()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getGradeForYear(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getPreviousSchoolClasses(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getPreviousSchoolSeason(com.idega.block.school.data.SchoolSeason p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getPreviousSchoolSeason(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getPreviousSchoolSeasonID(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getPreviousSchoolYear(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolBusiness getSchoolBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness getSchoolChoiceBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolYear getSchoolYear(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolYear getSchoolYear(int p0, int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getStudentChoices(java.util.Collection p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getStudentList(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getUserAddressesMapFromChoices(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getUserMapFromChoices(com.idega.data.IDOQuery p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getUserPhoneMapFromChoicesUserIdPK(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getUserAddressMapFromChoicesUserIdPK(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean hasChosenOtherSchool(java.util.Collection p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void importStudentInformationToNewClass(com.idega.block.school.data.SchoolClassMember p0,com.idega.block.school.data.SchoolSeason p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void markSchoolClassLocked(com.idega.block.school.data.SchoolClass p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void markSchoolClassReady(com.idega.block.school.data.SchoolClass p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void moveToGroup(int p0,int p1,int p2,int p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setNeedsSpecialAttention(int p0,int p1,boolean p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setNeedsSpecialAttention(com.idega.block.school.data.SchoolClassMember p0,boolean p2) throws java.rmi.RemoteException;
 public void setNeedsSpecialAttention(com.idega.block.school.data.SchoolClassMember p0,com.idega.block.school.data.SchoolSeason p1,boolean p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setStudentAsSpeciallyPlaced(com.idega.block.school.data.SchoolClassMember p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean[] hasSchoolChoices(int userID, int seasonID) throws java.rmi.RemoteException;
 public String getLocalizedSchoolTypeKey(com.idega.block.school.data.SchoolType type);
 public void resetSchoolClassStatus(int schoolClassID) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolYear getNextSchoolYear(com.idega.block.school.data.SchoolYear schoolYear) throws java.rmi.RemoteException;
 public java.util.Map getUserMapFromChoices(java.util.Collection choices) throws java.rmi.RemoteException;
 public java.util.Map getUserAddressesMapFromChoices(com.idega.data.IDOQuery query) throws java.rmi.RemoteException;
 public boolean isAlreadyInSchool(int userID, int schoolID, int seasonID) throws java.rmi.RemoteException;
 public boolean isOngoingSeason(int seasonID) throws java.rmi.RemoteException;
 public boolean hasMoveChoiceToOtherSchool(int userID, int schoolID, int seasonID);
 public boolean hasChoiceToThisSchool(int userID, int schoolID, int seasonID);

    SchoolClassMember [] getCurrentMembersWithInvoiceInterval (String operationalField) throws RemoteException;

    SchoolClassMember getCurrentSchoolClassMembership (User user)
        throws RemoteException;

    SchoolClassMember getCurrentSchoolClassMembership (User user, int schoolId)
        throws RemoteException;

    SchoolStudyPath getStudyPath (SchoolClassMember student);
    SchoolStudyPath [] getAllStudyPaths ();

	public ReportableCollection getReportOfUsersNotRegisteredInAnyClass(Locale currentLocale, Date selectedDate, SchoolSeason currentSeason) throws RemoteException, IDOLookupException, IDOException, FinderException, CreateException;
	public boolean isPlacedAtSchool(int userID, int schoolID) throws java.rmi.RemoteException;
	public se.idega.idegaweb.commune.business.CommuneUserBusiness getUserBusiness() throws java.rmi.RemoteException;
	public java.util.Map getYearClassMap(java.util.Collection schoolYears, int schoolID, int seasonID, String emptyString, boolean showSubGroups) throws java.rmi.RemoteException;
	public boolean removeSubGroupPlacements(int userID, int schoolID, int seasonID) throws java.rmi.RemoteException;
	public boolean hasChoicesForSeason(int userID, int seasonID) throws java.rmi.RemoteException;
	public Map getSchoolTypeClassMap(Collection schoolTypes,int schoolID,int seasonID,Boolean showSubGroups,Boolean showNonSeasonGroups,String noSchoolClassFoundEntry) throws java.rmi.RemoteException;
		
}
