package se.idega.idegaweb.commune.school.business;


public class SchoolCommuneSessionHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolCommuneSessionHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolCommuneSession.class;
 }


 public SchoolCommuneSession create() throws javax.ejb.CreateException{
  return (SchoolCommuneSession) super.createIBO();
 }



}