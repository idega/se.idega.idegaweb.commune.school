package se.idega.idegaweb.commune.school.data;


public class CurrentSchoolSeasonHomeImpl extends com.idega.data.IDOFactory implements CurrentSchoolSeasonHome
{
 protected Class getEntityInterfaceClass(){
  return CurrentSchoolSeason.class;
 }


 public CurrentSchoolSeason create() throws javax.ejb.CreateException{
  return (CurrentSchoolSeason) super.createIDO();
 }


public CurrentSchoolSeason findCurrentSeason()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((CurrentSchoolSeasonBMPBean)entity).ejbFindCurrentSeason();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public CurrentSchoolSeason findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CurrentSchoolSeason) super.findByPrimaryKeyIDO(pk);
 }



}