package se.idega.idegaweb.commune.school.data;


public class AfterSchoolChoiceHomeImpl extends com.idega.data.IDOFactory implements AfterSchoolChoiceHome
{
 protected Class getEntityInterfaceClass(){
  return AfterSchoolChoice.class;
 }


 public AfterSchoolChoice create() throws javax.ejb.CreateException{
  return (AfterSchoolChoice) super.createIDO();
 }


public java.util.Collection findAllCasesByProviderAndNotInStatus(int p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndNotInStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}
public java.util.Collection findAllCasesByProviderAndNotInStatus(int p0,java.lang.String[] p1, String p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndNotInStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(int p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllCasesByProviderAndStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindAllCasesByProviderAndStatus(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindByChildAndChoiceNumberAndSeason(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.String[] p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindByChildAndChoiceNumberAndSeason(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public AfterSchoolChoice findByChildAndProviderAndSeason(int p0,int p1,int p2,java.lang.String[] p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((AfterSchoolChoiceBMPBean)entity).ejbFindByChildAndProviderAndSeason(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findByChildAndSeason(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AfterSchoolChoiceBMPBean)entity).ejbFindByChildAndSeason(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public AfterSchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AfterSchoolChoice) super.findByPrimaryKeyIDO(pk);
 }



}