package se.idega.idegaweb.commune.school.data;


public interface ProcapitaSchoolHome extends com.idega.data.IDOHome
{
 public ProcapitaSchool create() throws javax.ejb.CreateException;
 public ProcapitaSchool findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}