package se.idega.idegaweb.commune.school.business;


public class SchoolChoiceBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolChoiceBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolChoiceBusiness.class;
 }


 public SchoolChoiceBusiness create() throws javax.ejb.CreateException{
  return (SchoolChoiceBusiness) super.createIBO();
 }



}