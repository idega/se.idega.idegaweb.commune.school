package se.idega.idegaweb.commune.school.business;


public class SchoolChoiceHelperBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolChoiceHelperBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolChoiceHelperBusiness.class;
 }


 public SchoolChoiceHelperBusiness create() throws javax.ejb.CreateException{
  return (SchoolChoiceHelperBusiness) super.createIBO();
 }



}