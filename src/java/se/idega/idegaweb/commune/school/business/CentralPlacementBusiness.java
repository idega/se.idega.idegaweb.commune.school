package se.idega.idegaweb.commune.school.business;


public interface CentralPlacementBusiness extends com.idega.business.IBOService
{
 public com.idega.block.school.data.SchoolClassMember getCurrentSchoolClassMembership(com.idega.user.data.User p0,com.idega.presentation.IWContext p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassMember storeSchoolClassMember(com.idega.presentation.IWContext p0,int p1)throws java.rmi.RemoteException,se.idega.idegaweb.commune.school.business.CentralPlacementException, java.rmi.RemoteException;
}
