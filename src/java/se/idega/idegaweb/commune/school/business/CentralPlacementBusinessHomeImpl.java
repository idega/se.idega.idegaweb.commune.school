package se.idega.idegaweb.commune.school.business;


public class CentralPlacementBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements CentralPlacementBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return CentralPlacementBusiness.class;
 }


 public CentralPlacementBusiness create() throws javax.ejb.CreateException{
  return (CentralPlacementBusiness) super.createIBO();
 }



}