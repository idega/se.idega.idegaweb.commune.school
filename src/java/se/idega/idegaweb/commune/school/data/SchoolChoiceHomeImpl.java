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

public java.util.Collection findAllPlacedBySeason(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindAllPlacedBySeason(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllWithLanguageWithinSeason(com.idega.block.school.data.SchoolSeason p0,java.lang.String[] p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindAllWithLanguageWithinSeason(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SchoolChoice findByChildAndChoiceNumberAndSeason(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolChoiceBMPBean)entity).ejbFindByChildAndChoiceNumberAndSeason(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findByChildAndSchool(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChildAndSchool(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildAndSchoolAndSeason(int p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChildAndSchoolAndSeason(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildAndSeason(int p0,int p1,java.lang.String[] p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChildAndSeason(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChildId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChildId(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChildId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByChosenSchoolId(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByChosenSchoolId(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCodeAndStatus(java.lang.String p0,java.lang.String[] p1,int p2,int p3,java.lang.String p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByCodeAndStatus(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCodeAndStatus(java.lang.String p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByCodeAndStatus(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByParent(com.idega.block.process.data.Case p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindByParent(p0);
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

public java.util.Collection findBySchoolIDAndSeasonIDAndStatus(int p0,int p1,java.lang.String[] p2,int p3,int p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindBySchoolIDAndSeasonIDAndStatus(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySeason(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindBySeason(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySeasonAndSchoolYear(com.idega.block.school.data.SchoolSeason p0,com.idega.block.school.data.SchoolYear p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindBySeasonAndSchoolYear(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findChoices(int p0,int p1,int p2,int[] p3,java.lang.String[] p4,java.lang.String p5,int p6,int p7,int p8)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindChoices(p0,p1,p2,p3,p4,p5,p6,p7,p8);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findChoices(int p0,int p1,int p2,int[] p3,java.lang.String[] p4,java.lang.String p5,int p6,int p7,int p8,int p9)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindChoices(p0,p1,p2,p3,p4,p5,p6,p7,p8,p9);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findChoices(int p0,int p1,int p2,java.lang.String[] p3,java.lang.String p4,int p5,int p6,int p7)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceBMPBean)entity).ejbFindChoices(p0,p1,p2,p3,p4,p5,p6,p7);
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


public int countBySchoolIDAndSeasonIDAndStatus(int p0,int p1,java.lang.String[] p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeCountBySchoolIDAndSeasonIDAndStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getChoices(int p0,int p1,int p2,java.lang.String[] p3)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetChoices(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getChoices(int p0,int p1,java.lang.String[] p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetChoices(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCount(java.lang.String[] p0,int p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCount(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCount(java.lang.String[] p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCount(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCount(com.idega.block.school.data.SchoolSeason p0,java.sql.Date p1,java.sql.Date p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCount(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCount(int p0,java.lang.String[] p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCount(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCount(int p0,int p1,java.lang.String[] p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCount(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCount(int p0,int p1,int p2,int[] p3,java.lang.String[] p4,java.lang.String p5)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCount(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCount(int p0,int p1,int p2,int[] p3,java.lang.String[] p4,java.lang.String p5, int p6)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCount(p0,p1,p2,p3,p4,p5,p6);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


public int getCountByChildAndSchool(int p0,int p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCountByChildAndSchool(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountByChildAndSchoolAndStatus(int p0,int p1,java.lang.String[] p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCountByChildAndSchoolAndStatus(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getCountOutsideInterval(int p0,int p1,int p2,int[] p3,java.lang.String[] p4,java.lang.String p5,java.sql.Date p6,java.sql.Date p7)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetCountOutsideInterval(p0,p1,p2,p3,p4,p5,p6,p7);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getMoveChoices(int p0,int p1,int p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetMoveChoices(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(java.lang.String p0,int p1,int p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetNumberOfApplications(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfApplications(java.lang.String p0,int p1,int p2,int p3)throws com.idega.data.IDOException{
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

public int getNumberOfChoices(int p0,int p1,java.lang.String[] p2)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetNumberOfChoices(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfHandledMoves(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetNumberOfHandledMoves(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfUnHandledMoves(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolChoiceBMPBean)entity).ejbHomeGetNumberOfUnHandledMoves(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}