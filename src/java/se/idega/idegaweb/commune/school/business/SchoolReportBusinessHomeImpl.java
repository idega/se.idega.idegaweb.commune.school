package se.idega.idegaweb.commune.school.business;


public class SchoolReportBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolReportBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolReportBusiness.class;
 }


 public SchoolReportBusiness create() throws javax.ejb.CreateException{
  return (SchoolReportBusiness) super.createIBO();
 }



}