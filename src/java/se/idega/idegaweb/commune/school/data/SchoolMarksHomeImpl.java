package se.idega.idegaweb.commune.school.data;


public class SchoolMarksHomeImpl extends com.idega.data.IDOFactory implements SchoolMarksHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolMarks.class;
 }


 public SchoolMarks create() throws javax.ejb.CreateException{
  return (SchoolMarks) super.createIDO();
 }


public java.util.Collection findAllSchoolMarks()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolMarksBMPBean)entity).ejbFindAllSchoolMarks();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySCBCode(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolMarksBMPBean)entity).ejbFindBySCBCode(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findNumberOfSchools()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolMarksBMPBean)entity).ejbFindNumberOfSchools();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolMarks findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolMarks) super.findByPrimaryKeyIDO(pk);
 }



}