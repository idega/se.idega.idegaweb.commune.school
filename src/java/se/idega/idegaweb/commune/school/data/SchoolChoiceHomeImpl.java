package se.idega.idegaweb.commune.school.data;


public class SchoolChoiceHomeImpl extends com.idega.data.IDOFactory implements SchoolChoiceHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolChoice.class;
 }


 public SchoolChoice create() throws javax.ejb.CreateException{
  return (SchoolChoice) super.createIDO();
 }


public java.util.Collection findByChildId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChildId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildId(int childId,int seasonId)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChildId(childId,seasonId);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChosenSchoolId(int schoolId,int schoolSeasonId)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChosenSchoolId(schoolId,schoolSeasonId);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCodeAndStatus(String caseCode,String[] caseStatus,int schoolId,int seasonId)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByCodeAndStatus(caseCode,caseStatus,schoolId,seasonId);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCodeAndStatus(String caseCode,String[] caseStatus,int schoolId,int seasonId,String ordered)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByCodeAndStatus(caseCode,caseStatus,schoolId,seasonId,ordered);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolChoice) super.findByPrimaryKeyIDO(pk);
 }



}