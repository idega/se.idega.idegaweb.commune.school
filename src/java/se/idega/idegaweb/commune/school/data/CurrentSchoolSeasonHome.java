package se.idega.idegaweb.commune.school.data;


public interface CurrentSchoolSeasonHome extends com.idega.data.IDOHome
{
 public CurrentSchoolSeason create() throws javax.ejb.CreateException;
 public CurrentSchoolSeason findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public CurrentSchoolSeason findCurrentSeason()throws javax.ejb.FinderException;

}