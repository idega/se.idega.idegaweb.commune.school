package se.idega.idegaweb.commune.school.report.business;


public class ReportBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements ReportBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return ReportBusiness.class;
 }


 public ReportBusiness create() throws javax.ejb.CreateException{
  return (ReportBusiness) super.createIBO();
 }



}