package se.idega.idegaweb.commune.school.business;


public class SchoolExportBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolExportBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolExportBusiness.class;
 }


 public SchoolExportBusiness create() throws javax.ejb.CreateException{
  return (SchoolExportBusiness) super.createIBO();
 }



}