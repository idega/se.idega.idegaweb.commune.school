package se.idega.idegaweb.commune.school.data;


public interface SCBCodeHome extends com.idega.data.IDOHome
{
 public SCBCode create() throws javax.ejb.CreateException;
 public SCBCode findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}