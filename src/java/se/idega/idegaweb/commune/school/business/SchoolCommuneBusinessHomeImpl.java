package se.idega.idegaweb.commune.school.business;


public class SchoolCommuneBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolCommuneBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolCommuneBusiness.class;
 }


 public SchoolCommuneBusiness create() throws javax.ejb.CreateException{
  return (SchoolCommuneBusiness) super.createIBO();
 }



}