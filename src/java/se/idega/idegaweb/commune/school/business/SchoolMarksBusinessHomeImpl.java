package se.idega.idegaweb.commune.school.business;


public class SchoolMarksBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SchoolMarksBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SchoolMarksBusiness.class;
 }


 public SchoolMarksBusiness create() throws javax.ejb.CreateException{
  return (SchoolMarksBusiness) super.createIBO();
 }



}