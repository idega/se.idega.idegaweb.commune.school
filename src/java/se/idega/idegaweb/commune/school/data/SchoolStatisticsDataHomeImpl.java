package se.idega.idegaweb.commune.school.data;


public class SchoolStatisticsDataHomeImpl extends com.idega.data.IDOFactory implements SchoolStatisticsDataHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolStatisticsData.class;
 }


 public SchoolStatisticsData create() throws javax.ejb.CreateException{
  return (SchoolStatisticsData) super.createIDO();
 }


public java.util.Collection findAllSchoolStats()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStatisticsDataBMPBean)entity).ejbFindAllSchoolStats();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SchoolStatisticsData findBySCBCode(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolStatisticsDataBMPBean)entity).ejbFindBySCBCode(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SchoolStatisticsData findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolStatisticsData) super.findByPrimaryKeyIDO(pk);
 }



}