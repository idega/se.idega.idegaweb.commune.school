package se.idega.idegaweb.commune.school.data;

import java.util.ArrayList;
import java.util.Collection;

import com.idega.data.GenericEntity;
import com.idega.data.IDOException;

/**
 * This BMP bean handles school marks.
 * <p>
 * $Id: SchoolMarksBMPBean.java,v 1.2 2003/10/05 20:07:06 laddi Exp $
 *
 * @author Anders Lindman
 * @version $version$
 */
public class SchoolMarksBMPBean extends GenericEntity implements SchoolMarks {

	private static String ENTITY_NAME = "SCB_SCHOOLMARKS";

	private static String ID_COLUMN_NAME = "SCB_SCHOOLMARKS_ID";
	
	private static String PROVIDER = "PROVIDER";
	private static String SCHOOLCODE = "SCHOOLCODE";
	private static String GROUPCODE = "GROUPCODE";
	private static String PERSONALID = "PERSONALID";
	private static String BL = "BL";
	private static String EN = "EN";
	private static String HKK = "HKK";
	private static String IDH = "IDH";
	private static String MA = "MA";
	private static String M1L = "M1L";
	private static String M1G = "M1G";
	private static String M2L = "M2L";
	private static String M2G = "M2G";
	private static String MLL = "MLL";
	private static String MLG = "MLG";
	private static String MU = "MU";
	private static String NO = "NO";
	private static String BI = "BI";
	private static String FY = "FY";
	private static String KE = "KE";
	private static String SO = "SO";
	private static String GE = "GE";
	private static String HI = "HI";
	private static String RE = "RE";
	private static String SH = "SH";
	private static String SL = "SL";
	private static String SV = "SV";
	private static String SVA = "SVA";
	private static String TN = "TN";
	private static String TK = "TK";
	private static String SCHOOLTERM = "SCHOOLTERM";
	
 
  public void initializeAttributes() {
	addAttribute(getIDColumnName());
	addAttribute(PROVIDER, "Provider of the ADB system", String.class);
	addAttribute(SCHOOLCODE, "Unique SCB (Statistiska Centralbyr?n) school code", String.class);
	addAttribute(GROUPCODE, "School group code", String.class);
	addAttribute(PERSONALID, "Student's social security number", String.class);
	addAttribute(BL, "Art/Image", String.class);
	addAttribute(EN, "English", String.class);
	addAttribute(HKK, "House holding", String.class);
	addAttribute(IDH, "Sport & health", String.class);
	addAttribute(MA, "Mathematics", String.class);
	addAttribute(M1L, "Language 1", String.class);
	addAttribute(M1G, "Language 1 mark", String.class);
	addAttribute(M2L, "Language 2", String.class);
	addAttribute(M2G, "Language 2 mark", String.class);
	addAttribute(MLL, "Mother tongue", String.class);
	addAttribute(MLG, "Mother tongue mark", String.class);
	addAttribute(MU, "Music", String.class);
	addAttribute(NO, "Nature science", String.class);
	addAttribute(BI, "Biology", String.class);
	addAttribute(FY, "Physics", String.class);
	addAttribute(KE, "Chemistry", String.class);
	addAttribute(SO, "Social science", String.class);
	addAttribute(GE, "Geography", String.class);
	addAttribute(HI, "History", String.class);
	addAttribute(RE, "Religion", String.class);
	addAttribute(SH, "Society science", String.class);
	addAttribute(SL, "Crafts", String.class);
	addAttribute(SV, "Swedish", String.class);
	addAttribute(SVA, "Swedish as second language", String.class);
	addAttribute(TN, "Sign language", String.class);
	addAttribute(TK, "Technology", String.class);
	addAttribute(SCHOOLTERM, "School term/year", String.class);
	
	setAsPrimaryKey(ID_COLUMN_NAME, true);
  }

  public String getEntityName() {
	return ENTITY_NAME;
  }

	public String getIDColumnName(){
		return ID_COLUMN_NAME;
	}

	public String getProvider() {return getStringColumnValue(PROVIDER);}
	public String getSchoolCode() {return getStringColumnValue(SCHOOLCODE);}
	public String getGroupCode() {return getStringColumnValue(GROUPCODE);} 
	public String getPersonalId() {return getStringColumnValue(PERSONALID);} 
	public String getBL() {return getStringColumnValue(BL);}
	public String getEN() {return getStringColumnValue(EN);}
	public String getHKK() {return getStringColumnValue(HKK);} 
	public String getIDH() {return getStringColumnValue(IDH);}
	public String getMA() {return getStringColumnValue(MA);} 
	public String getM1L() {return getStringColumnValue(M1L);} 
	public String getM1G() {return getStringColumnValue(M1G);} 
	public String getM2L() {return getStringColumnValue(M2L);} 
	public String getM2G() {return getStringColumnValue(M2G);} 
	public String getMLL() {return getStringColumnValue(MLL);} 
	public String getMLG() {return getStringColumnValue(MLG);} 
	public String getMU() {return getStringColumnValue(MU);} 
	public String getNO() {return getStringColumnValue(NO);} 
	public String getBI() {return getStringColumnValue(BI);} 
	public String getFY() {return getStringColumnValue(FY);} 
	public String getKE() {return getStringColumnValue(KE);} 
	public String getSO() {return getStringColumnValue(SO);} 
	public String getGE() {return getStringColumnValue(GE);} 
	public String getHI() {return getStringColumnValue(HI);} 
	public String getRE() {return getStringColumnValue(RE);} 
	public String getSH() {return getStringColumnValue(SH);} 
	public String getSL() {return getStringColumnValue(SL);} 
	public String getSV() {return getStringColumnValue(SV);} 
	public String getSVA() {return getStringColumnValue(SVA);} 
	public String getTN() {return getStringColumnValue(TN);} 
	public String getTK() {return getStringColumnValue(TK);} 
	public String getSCHOOLTERM() {return getStringColumnValue(SCHOOLTERM);} 

//	public int ejbGetNumberOfSchools() {
//		int count = 0;
//		try { 
//			count = idoGetNumberOfRecords("select unique(" + SCHOOLCODE + ") from " + getEntityName());		
//		} catch (IDOException e) {}
//		return count;
//	}

	public Collection ejbFindBySCBCode(String scbCode) throws javax.ejb.FinderException {
		return idoFindPKsBySQL("select * from " + getEntityName() + " where " + SCHOOLCODE + "='" + scbCode + "'");
	}

	public Collection ejbFindAllSchoolMarks() throws javax.ejb.FinderException {
		return idoFindPKsBySQL("select * from " + getEntityName());
	}

	public Collection ejbFindNumberOfSchools() throws javax.ejb.FinderException {
		int count = 0;
		try { 
			count = idoGetNumberOfRecords("select unique(" + SCHOOLCODE + ") from " + getEntityName());		
		} catch (IDOException e) {}
		ArrayList res = new ArrayList();
		res.add(new Integer(count));
		return res;
	}
}
