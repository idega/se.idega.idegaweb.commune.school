package se.idega.idegaweb.commune.school.data;


public interface AfterSchoolChoiceHome extends com.idega.data.IDOHome
{
 public AfterSchoolChoice create() throws javax.ejb.CreateException;
 public AfterSchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndNotInStatus(int p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndNotInStatus(int p0,java.lang.String[] p1, String p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(int p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException;
 public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2)throws javax.ejb.FinderException;
 public AfterSchoolChoice findByChildAndChoiceNumberAndSeason(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.String[] p3)throws javax.ejb.FinderException;
 public AfterSchoolChoice findByChildAndProviderAndSeason(int p0,int p1,int p2,java.lang.String[] p3)throws javax.ejb.FinderException;
 public java.util.Collection findByChildAndSeason(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException;

}