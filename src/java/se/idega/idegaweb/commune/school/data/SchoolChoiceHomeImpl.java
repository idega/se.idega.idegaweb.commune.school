package se.idega.idegaweb.commune.school.data;


public class SchoolChoiceHomeImpl extends com.idega.data.IDOFactory implements SchoolChoiceHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolChoice.class;
 }


 public SchoolChoice create() throws javax.ejb.CreateException{
  return (SchoolChoice) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildAndSchoolAndSeason(int p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChildAndSchoolAndSeason(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildAndSeason(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChildAndSeason(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildId(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChildId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChildId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChosenSchoolId(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChosenSchoolId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCodeAndStatus(java.lang.String p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByCodeAndStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCodeAndStatus(java.lang.String p0,java.lang.String[] p1,int p2,int p3,java.lang.String p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByCodeAndStatus(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolAndFreeTime(int p0,int p1,boolean p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindBySchoolAndFreeTime(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolAndSeasonAndGrade(int p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindBySchoolAndSeasonAndGrade(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySeason(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindBySeason(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findChoices(int p0,int p1,int p2,java.lang.String[] p3,java.lang.String p4,int p5,int p6,int p7)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindChoices(p0,p1,p2,p3,p4,p5,p6,p7);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findChoices(int p0,int p1,int p2,int[] p3,java.lang.String[] p4,java.lang.String p5,int p6,int p7,int p8)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindChoices(p0,p1,p2,p3,p4,p5,p6,p7,p8);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findChoicesInClassAndSeasonAndSchool(int p0,int p1,int p2,boolean p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindChoicesInClassAndSeasonAndSchool(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolChoice) super.findByPrimaryKeyIDO(pk);
 }


public int getCount(java.lang.String[] p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCount(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCount(int p0,java.lang.String[] p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCount(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCount(int p0,int p1,int p2,int[] p3,java.lang.String[] p4,java.lang.String p5)throws javax.ejb.FinderException,java.rmi.RemoteException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCount(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getMoveChoices(int p0,int p1,int p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetMoveChoices(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(java.lang.String p0,int p1,int p2)throws javax.ejb.FinderException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(java.lang.String p0,int p1,int p2,int p3)throws javax.ejb.FinderException,com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfChoices(int p0,int p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetNumberOfChoices(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}