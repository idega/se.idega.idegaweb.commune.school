
package se.idega.idegaweb.commune.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import java.sql.Date;
import com.idega.data.*;

/**
 * This BMP bean handles caclulated statistics and stores them for
 * fast retrieval.
 * <p>
 * $Id: SchoolStatisticsDataBMPBean.java,v 1.1 2003/09/25 07:59:14 kjell Exp $
 *
 * @author <a href="mailto:kjell@lindman.com">Kjell Lindman</a>
 * @version $version$
 */
public class SchoolStatisticsDataBMPBean extends GenericEntity implements SchoolStatisticsData {

	private static String ENTITY_NAME = "SCHOOLSTATISTICS";

	private static String ID_COLUMN_NAME = "SCHOOLSTAT_ID";
	private static String SCHOOLCODE = "SCHOOLCODE";
	private static String PERIOD_FROM = "PERIOD_FORM";
	private static String PERIOD_TO = "PERIOD_TO";
	private static String MERITE_VALUE_NUM ="MERITE_VALUE_NUM";
	private static String MERITE_VALUE_PCT ="MERITE_VALUE_PCT";
	private static String MARKS_POINTS_NUM ="MARKS_POINTS_NUM";
	private static String MARKS_POINTS_PCT ="MARKS_POINTS_PCT";
	private static String AUTH_POINTS_NUM ="AUTH_POINTS_NUM";
	private static String AUTH_POINTS_PCT ="AUTH_POINTS_PCT";
	private static String MARKS_NR_SHARE_NUM ="MARKS_NR_SHARE_NUM";
	private static String MARKS_NR_SHARE_PCT ="MARKS_NR_SHARE_PCT";
	private static String MARKS_FAILED_NUM ="MARKS_FAILED_NUM";
	private static String MARKS_FAILED_PCT ="MARKS_FAILED_PCT";
	private static String MARKS_NO_GOAL_NUM ="MARKS_NO_GOAL_NUM";
	private static String MARKS_NO_GOAL_PCT ="MARKS_NO_GOAL_PCT";
	
	/*********************** New from Nacka ****************************/
	
	private static String ENGLISH_MARKS_EG_NUM = "ENGLISH_MARKS_EG_NUM";
	private static String ENGLISH_MARKS_EG_PCT = "ENGLISH_MARKS_EG_PCT";
	private static String ENGLISH_MARKS_G_NUM = "ENGLISH_MARKS_G_NUM";
	private static String ENGLISH_MARKS_G_PCT = "ENGLISH_MARKS_G_PCT";
	private static String ENGLISH_MARKS_VG_NUM = "ENGLISH_MARKS_VG_NUM";
	private static String ENGLISH_MARKS_VG_PCT = "ENGLISH_MARKS_VG_PCT";
	private static String ENGLISH_MARKS_MVG_NUM = "ENGLISH_MARKS_MVG_NUM";
	private static String ENGLISH_MARKS_MVG_PCT = "ENGLISH_MARKS_MVG_PCT";
	private static String ENGLISH_MARKS_TOT_NUM = "ENGLISH_MARKS_TOT_NUM";
	private static String ENGLISH_MARKS_TOT_PCT = "ENGLISH_MARKS_TOT_PCT";
	
	private static String MATHS_MARKS_EG_NUM = "MATHS_MARKS_EG_NUM";
	private static String MATHS_MARKS_EG_PCT = "MATHS_MARKS_EG_PCT";
	private static String MATHS_MARKS_G_NUM = "MATHS_MARKS_G_NUM";
	private static String MATHS_MARKS_G_PCT = "MATHS_MARKS_G_PCT";
	private static String MATHS_MARKS_VG_NUM = "MATHS_MARKS_VG_NUM";
	private static String MATHS_MARKS_VG_PCT = "MATHS_MARKS_VG_PCT";
	private static String MATHS_MARKS_MVG_NUM = "MATHS_MARKS_MVG_NUM";
	private static String MATHS_MARKS_MVG_PCT = "MATHS_MARKS_MVG_PCT";
	private static String MATHS_MARKS_TOT_NUM = "MATHS_MARKS_TOT_NUM";
	private static String MATHS_MARKS_TOT_PCT = "MATHS_MARKS_TOT_PCT";
	
	private static String SWEDISH_MARKS_EG_NUM = "SWEDISH_MARKS_EG_NUM";
	private static String SWEDISH_MARKS_EG_PCT = "SWEDISH_MARKS_EG_PCT";
	private static String SWEDISH_MARKS_G_NUM = "SWEDISH_MARKS_G_NUM";
	private static String SWEDISH_MARKS_G_PCT = "SWEDISH_MARKS_G_PCT";
	private static String SWEDISH_MARKS_VG_NUM = "SWEDISH_MARKS_VG_NUM";
	private static String SWEDISH_MARKS_VG_PCT = "SWEDISH_MARKS_VG_PCT";
	private static String SWEDISH_MARKS_MVG_NUM = "SWEDISH_MARKS_MVG_NUM";
	private static String SWEDISH_MARKS_MVG_PCT = "SWEDISH_MARKS_MVG_PCT";
	private static String SWEDISH_MARKS_TOT_NUM = "SWEDISH_MARKS_TOT_NUM";
	private static String SWEDISH_MARKS_TOT_PCT = "SWEDISH_MARKS_TOT_PCT";

	private static String SWEDISH2_MARKS_EG_NUM = "SWEDISH2_MARKS_EG_NUM";
	private static String SWEDISH2_MARKS_EG_PCT = "SWEDISH2_MARKS_EG_PCT";
	private static String SWEDISH2_MARKS_G_NUM = "SWEDISH2_MARKS_G_NUM";
	private static String SWEDISH2_MARKS_G_PCT = "SWEDISH2_MARKS_G_PCT";
	private static String SWEDISH2_MARKS_VG_NUM = "SWEDISH2_MARKS_VG_NUM";
	private static String SWEDISH2_MARKS_VG_PCT = "SWEDISH2_MARKS_VG_PCT";
	private static String SWEDISH2_MARKS_MVG_NUM = "SWEDISH2_MARKS_MVG_NUM";
	private static String SWEDISH2_MARKS_MVG_PCT = "SWEDISH2_MARKS_MVG_PCT";
	private static String SWEDISH2_MARKS_TOT_NUM = "SWEDISH2_MARKS_TOT_NUM";
	private static String SWEDISH2_MARKS_TOT_PCT = "SWEDISH2_MARKS_TOT_PCT";

	private static String SUM_AUTH_MARKS_EG_NUM = "SUM_AUTH_MARKS_EG_NUM";
	private static String SUM_AUTH_MARKS_EG_PCT = "SUM_AUTH_MARKS_EG_PCT";
	private static String SUM_AUTH_MARKS_G_NUM = "SUM_AUTH_MARKS_G_NUM";
	private static String SUM_AUTH_MARKS_G_PCT = "SUM_AUTH_MARKS_G_PCT";
	private static String SUM_AUTH_MARKS_VG_NUM = "SUM_AUTH_MARKS_VG_NUM";
	private static String SUM_AUTH_MARKS_VG_PCT = "SUM_AUTH_MARKS_VG_PCT";
	private static String SUM_AUTH_MARKS_MVG_NUM = "SUM_AUTH_MARKS_MVG_NUM";
	private static String SUM_AUTH_MARKS_MVG_PCT = "SUM_AUTH_MARKS_MVG_PCT";
	private static String SUM_AUTH_MARKS_TOT_NUM = "SUM_AUTH_MARKS_TOT_NUM";
	private static String SUM_AUTH_MARKS_TOT_PCT = "SUM_AUTH_MARKS_TOT_PCT";

	private static String TOTAL_MARKS_EG_NUM = "TOTAL_MARKS_EG_NUM";
	private static String TOTAL_MARKS_EG_PCT = "TOTAL_MARKS_EG_PCT";
	private static String TOTAL_MARKS_G_NUM = "TOTAL_MARKS_G_NUM";
	private static String TOTAL_MARKS_G_PCT = "TOTAL_MARKS_G_PCT";
	private static String TOTAL_MARKS_VG_NUM = "TOTAL_MARKS_VG_NUM";
	private static String TOTAL_MARKS_VG_PCT = "TOTAL_MARKS_VG_PCT";
	private static String TOTAL_MARKS_MVG_NUM = "TOTAL_MARKS_MVG_NUM";
	private static String TOTAL_MARKS_MVG_PCT = "TOTAL_MARKS_MVG_PCT";
	private static String TOTAL_MARKS_TOT_NUM = "TOTAL_MARKS_TOT_NUM";
	private static String TOTAL_MARKS_TOT_PCT = "TOTAL_MARKS_TOT_PCT";

 
  public void initializeAttributes() {
  	
	addAttribute(getIDColumnName());
	addAttribute(SCHOOLCODE, "Unique SCB (Statistiska Centralbyr?n) school code", String.class);
	addAttribute(PERIOD_FROM, "Start term", Date.class);
	addAttribute(PERIOD_TO, "End term", Date.class);
	addAttribute(MERITE_VALUE_NUM, "Merite value numeric", String.class);
	addAttribute(MERITE_VALUE_PCT, "Merite value percent", String.class);
	addAttribute(MARKS_POINTS_NUM, "Marks points numeric", String.class);
	addAttribute(MARKS_POINTS_PCT, "Marks points percent", String.class);
	addAttribute(AUTH_POINTS_NUM, "Auth points numeric", String.class);
	addAttribute(AUTH_POINTS_PCT, "Auth points percent", String.class);
	addAttribute(MARKS_NR_SHARE_NUM, "Marks number share numeric", String.class);
	addAttribute(MARKS_NR_SHARE_PCT, "Marks number share percent", String.class);
	addAttribute(MARKS_FAILED_NUM, "Marks failed numeric", String.class);
	addAttribute(MARKS_FAILED_PCT, "Marks failed percent", String.class);
	addAttribute(MARKS_NO_GOAL_NUM, "Marks no goal numeric", String.class);
	addAttribute(MARKS_NO_GOAL_PCT, "Marks no goal percent", String.class);
	
	/***** new from Nacka ******/
	
	addAttribute(ENGLISH_MARKS_EG_NUM, "Marks English numeric", String.class);
	addAttribute(ENGLISH_MARKS_EG_PCT, "Marks English percent", String.class);
	addAttribute(ENGLISH_MARKS_G_NUM, "Marks English numeric", String.class);
	addAttribute(ENGLISH_MARKS_G_PCT, "Marks English percent", String.class);
	addAttribute(ENGLISH_MARKS_VG_NUM, "Marks English numeric", String.class);
	addAttribute(ENGLISH_MARKS_VG_PCT, "Marks English percent", String.class);
	addAttribute(ENGLISH_MARKS_MVG_NUM, "Marks English numeric", String.class);
	addAttribute(ENGLISH_MARKS_MVG_PCT, "Marks English percent", String.class);
	addAttribute(ENGLISH_MARKS_TOT_NUM, "Marks English numeric", String.class);
	addAttribute(ENGLISH_MARKS_TOT_PCT, "Marks English percent", String.class);

	addAttribute(MATHS_MARKS_EG_NUM, "Marks Math numeric", String.class);
	addAttribute(MATHS_MARKS_EG_PCT, "Marks Math percent", String.class);
	addAttribute(MATHS_MARKS_G_NUM, "Marks Math numeric", String.class);
	addAttribute(MATHS_MARKS_G_PCT, "Marks Math percent", String.class);
	addAttribute(MATHS_MARKS_VG_NUM, "Marks Math numeric", String.class);
	addAttribute(MATHS_MARKS_VG_PCT, "Marks Math percent", String.class);
	addAttribute(MATHS_MARKS_MVG_NUM, "Marks Math numeric", String.class);
	addAttribute(MATHS_MARKS_MVG_PCT, "Marks Swedish percent", String.class);
	addAttribute(MATHS_MARKS_TOT_NUM, "Marks Swedish numeric", String.class);
	addAttribute(MATHS_MARKS_TOT_PCT, "Marks Swedish percent", String.class);

	addAttribute(SWEDISH_MARKS_EG_NUM, "Marks Swedish numeric", String.class);
	addAttribute(SWEDISH_MARKS_EG_PCT, "Marks Swedish percent", String.class);
	addAttribute(SWEDISH_MARKS_G_NUM, "Marks Swedish numeric", String.class);
	addAttribute(SWEDISH_MARKS_G_PCT, "Marks Swedish percent", String.class);
	addAttribute(SWEDISH_MARKS_VG_NUM, "Marks Swedish numeric", String.class);
	addAttribute(SWEDISH_MARKS_VG_PCT, "Marks Swedish percent", String.class);
	addAttribute(SWEDISH_MARKS_MVG_NUM, "Marks Swedish numeric", String.class);
	addAttribute(SWEDISH_MARKS_MVG_PCT, "Marks Swedish percent", String.class);
	addAttribute(SWEDISH_MARKS_TOT_NUM, "Marks Swedish numeric", String.class);
	addAttribute(SWEDISH_MARKS_TOT_PCT, "Marks Swedish percent", String.class);

	addAttribute(SWEDISH2_MARKS_EG_NUM, "Marks Swedish2 numeric", String.class);
	addAttribute(SWEDISH2_MARKS_EG_PCT, "Marks Swedish2 percent", String.class);
	addAttribute(SWEDISH2_MARKS_G_NUM, "Marks Swedish2 numeric", String.class);
	addAttribute(SWEDISH2_MARKS_G_PCT, "Marks Swedish2 percent", String.class);
	addAttribute(SWEDISH2_MARKS_VG_NUM, "Marks Swedish2 numeric", String.class);
	addAttribute(SWEDISH2_MARKS_VG_PCT, "Marks Swedish2 percent", String.class);
	addAttribute(SWEDISH2_MARKS_MVG_NUM, "Marks Swedish2 numeric", String.class);
	addAttribute(SWEDISH2_MARKS_MVG_PCT, "Marks Swedish2 percent", String.class);
	addAttribute(SWEDISH2_MARKS_TOT_NUM, "Marks Swedish2 numeric", String.class);
	addAttribute(SWEDISH2_MARKS_TOT_PCT, "Marks Swedish2 percent", String.class);

	addAttribute(SUM_AUTH_MARKS_EG_NUM, "Sum auth marks numeric", String.class);
	addAttribute(SUM_AUTH_MARKS_EG_PCT, "Sum auth marks percent", String.class);
	addAttribute(SUM_AUTH_MARKS_G_NUM, "Sum auth marks numeric", String.class);
	addAttribute(SUM_AUTH_MARKS_G_PCT, "Sum auth marks percent", String.class);
	addAttribute(SUM_AUTH_MARKS_VG_NUM, "Sum auth marks numeric", String.class);
	addAttribute(SUM_AUTH_MARKS_VG_PCT, "Sum auth marks percent", String.class);
	addAttribute(SUM_AUTH_MARKS_MVG_NUM, "Sum auth marks numeric", String.class);
	addAttribute(SUM_AUTH_MARKS_MVG_PCT, "Sum auth marks percent", String.class);
	addAttribute(SUM_AUTH_MARKS_TOT_NUM, "Sum auth marks numeric", String.class);
	addAttribute(SUM_AUTH_MARKS_TOT_PCT, "Sum auth marks percent", String.class);

	addAttribute(TOTAL_MARKS_EG_NUM, "Total marks numeric", String.class);
	addAttribute(TOTAL_MARKS_EG_PCT, "Total marks percent", String.class);
	addAttribute(TOTAL_MARKS_G_NUM, "Total marks numeric", String.class);
	addAttribute(TOTAL_MARKS_G_PCT, "Total marks percent", String.class);
	addAttribute(TOTAL_MARKS_VG_NUM, "Total marks numeric", String.class);
	addAttribute(TOTAL_MARKS_VG_PCT, "Total marks percent", String.class);
	addAttribute(TOTAL_MARKS_MVG_NUM, "Total marks numeric", String.class);
	addAttribute(TOTAL_MARKS_MVG_PCT, "Total marks percent", String.class);
	addAttribute(TOTAL_MARKS_TOT_NUM, "Total marks numeric", String.class);
	addAttribute(TOTAL_MARKS_TOT_PCT, "Total marks percent", String.class);
	
	setAsPrimaryKey(ID_COLUMN_NAME, true);
  }

  public String getEntityName() {
	return ENTITY_NAME;
  }

	public String getIDColumnName(){
		return ID_COLUMN_NAME;
	}

	public String getSchoolCode() {return getStringColumnValue(SCHOOLCODE);}
	public Date getPeriodFrom() {return getDateColumnValue(PERIOD_FROM);} 
	public Date getPeriodTo() {return getDateColumnValue(PERIOD_FROM);} 
	public String getMeritValueNum() {return getStringColumnValue(MERITE_VALUE_NUM);}
	public String getMeritValuePct() {return getStringColumnValue(MERITE_VALUE_PCT);}

	/*** these are obsolete ***/
	public String getMarksPointsNum() {return getStringColumnValue(MARKS_POINTS_NUM);}
	public String getMarksPointsPct() {return getStringColumnValue(MARKS_POINTS_PCT);}
	public String getAuthPointsNum() {return getStringColumnValue(AUTH_POINTS_NUM);}
	public String getAuthPointsPct() {return getStringColumnValue(AUTH_POINTS_PCT);}
	public String getMarksNrShareNum() {return getStringColumnValue(MARKS_NR_SHARE_NUM);}
	public String getMarksNrSharePct() {return getStringColumnValue(MARKS_NR_SHARE_PCT);}
	public String getMarksFailedNum() {return getStringColumnValue(MARKS_FAILED_NUM);}
	public String getMarksFailedPct() {return getStringColumnValue(MARKS_FAILED_PCT);}
	public String getMarksNoGoalNum() {return getStringColumnValue(MARKS_NO_GOAL_NUM);}
	public String getMarksNoGoalPct() {return getStringColumnValue(MARKS_NO_GOAL_PCT);}
	/**************************/

	/************** New from Nacka ***********/ 
	
	public String getEnglishMarksEGNum() {return getStringColumnValue(ENGLISH_MARKS_EG_NUM);}
	public String getEnglishMarksEGPct() {return getStringColumnValue(ENGLISH_MARKS_EG_PCT);}
	public String getEnglishMarksGNum() {return getStringColumnValue(ENGLISH_MARKS_G_NUM);}
	public String getEnglishMarksGPct() {return getStringColumnValue(ENGLISH_MARKS_G_PCT);}
	public String getEnglishMarksVGNum() {return getStringColumnValue(ENGLISH_MARKS_VG_NUM);}
	public String getEnglishMarksVGPct() {return getStringColumnValue(ENGLISH_MARKS_VG_PCT);}
	public String getEnglishMarksMVGNum() {return getStringColumnValue(ENGLISH_MARKS_MVG_NUM);}
	public String getEnglishMarksMVGPct() {return getStringColumnValue(ENGLISH_MARKS_MVG_PCT);}
	public String getEnglishMarksTotNum() {return getStringColumnValue(ENGLISH_MARKS_TOT_NUM);}
	public String getEnglishMarksTotPct() {return getStringColumnValue(ENGLISH_MARKS_TOT_PCT);}

	public String getMathsMarksEGNum() {return getStringColumnValue(MATHS_MARKS_EG_NUM);}
	public String getMathsMarksEGPct() {return getStringColumnValue(MATHS_MARKS_EG_PCT);}
	public String getMathsMarksGNum() {return getStringColumnValue(MATHS_MARKS_G_NUM);}
	public String getMathsMarksGPct() {return getStringColumnValue(MATHS_MARKS_G_PCT);}
	public String getMathsMarksVGNum() {return getStringColumnValue(MATHS_MARKS_VG_NUM);}
	public String getMathsMarksVGPct() {return getStringColumnValue(MATHS_MARKS_VG_PCT);}
	public String getMathsMarksMVGNum() {return getStringColumnValue(MATHS_MARKS_MVG_NUM);}
	public String getMathsMarksMVGPct() {return getStringColumnValue(MATHS_MARKS_MVG_PCT);}
	public String getMathsMarksTotNum() {return getStringColumnValue(MATHS_MARKS_TOT_NUM);}
	public String getMathsMarksTotPct() {return getStringColumnValue(MATHS_MARKS_TOT_PCT);}

	public String getSwedishMarksEGNum() {return getStringColumnValue(SWEDISH_MARKS_EG_NUM);}
	public String getSwedishMarksEGPct() {return getStringColumnValue(SWEDISH_MARKS_EG_PCT);}
	public String getSwedishMarksGNum() {return getStringColumnValue(SWEDISH_MARKS_G_NUM);}
	public String getSwedishMarksGPct() {return getStringColumnValue(SWEDISH_MARKS_G_PCT);}
	public String getSwedishMarksVGNum() {return getStringColumnValue(SWEDISH_MARKS_VG_NUM);}
	public String getSwedishMarksVGPct() {return getStringColumnValue(SWEDISH_MARKS_VG_PCT);}
	public String getSwedishMarksMVGNum() {return getStringColumnValue(SWEDISH_MARKS_MVG_NUM);}
	public String getSwedishMarksMVGPct() {return getStringColumnValue(SWEDISH_MARKS_MVG_PCT);}
	public String getSwedishMarksTotNum() {return getStringColumnValue(SWEDISH_MARKS_TOT_NUM);}
	public String getSwedishMarksTotPct() {return getStringColumnValue(SWEDISH_MARKS_TOT_PCT);}

	public String getSwedish2MarksEGNum() {return getStringColumnValue(SWEDISH2_MARKS_EG_NUM);}
	public String getSwedish2MarksEGPct() {return getStringColumnValue(SWEDISH2_MARKS_EG_PCT);}
	public String getSwedish2MarksGNum() {return getStringColumnValue(SWEDISH2_MARKS_G_NUM);}
	public String getSwedish2MarksGPct() {return getStringColumnValue(SWEDISH2_MARKS_G_PCT);}
	public String getSwedish2MarksVGNum() {return getStringColumnValue(SWEDISH2_MARKS_VG_NUM);}
	public String getSwedish2MarksVGPct() {return getStringColumnValue(SWEDISH2_MARKS_VG_PCT);}
	public String getSwedish2MarksMVGNum() {return getStringColumnValue(SWEDISH2_MARKS_MVG_NUM);}
	public String getSwedish2MarksMVGPct() {return getStringColumnValue(SWEDISH2_MARKS_MVG_PCT);}
	public String getSwedish2MarksTotNum() {return getStringColumnValue(SWEDISH2_MARKS_TOT_NUM);}
	public String getSwedish2MarksTotPct() {return getStringColumnValue(SWEDISH2_MARKS_TOT_PCT);}

	public String getSumAuthMarksEGNum() {return getStringColumnValue(SUM_AUTH_MARKS_EG_NUM);}
	public String getSumAuthMarksEGPct() {return getStringColumnValue(SUM_AUTH_MARKS_EG_PCT);}
	public String getSumAuthMarksGNum() {return getStringColumnValue(SUM_AUTH_MARKS_G_NUM);}
	public String getSumAuthMarksGPct() {return getStringColumnValue(SUM_AUTH_MARKS_G_PCT);}
	public String getSumAuthMarksVGNum() {return getStringColumnValue(SUM_AUTH_MARKS_VG_NUM);}
	public String getSumAuthMarksVGPct() {return getStringColumnValue(SUM_AUTH_MARKS_VG_PCT);}
	public String getSumAuthMarksMVGNum() {return getStringColumnValue(SUM_AUTH_MARKS_MVG_NUM);}
	public String getSumAuthMarksMVGPct() {return getStringColumnValue(SUM_AUTH_MARKS_MVG_PCT);}
	public String getSumAuthMarksTotNum() {return getStringColumnValue(SUM_AUTH_MARKS_TOT_NUM);}
	public String getSumAuthMarksTotPct() {return getStringColumnValue(SUM_AUTH_MARKS_TOT_PCT);}

	public String getTotalMarksEGNum() {return getStringColumnValue(TOTAL_MARKS_EG_NUM);}
	public String getTotalMarksEGPct() {return getStringColumnValue(TOTAL_MARKS_EG_PCT);}
	public String getTotalMarksGNum() {return getStringColumnValue(TOTAL_MARKS_G_NUM);}
	public String getTotalMarksGPct() {return getStringColumnValue(TOTAL_MARKS_G_PCT);}
	public String getTotalMarksVGNum() {return getStringColumnValue(TOTAL_MARKS_VG_NUM);}
	public String getTotalMarksVGPct() {return getStringColumnValue(TOTAL_MARKS_VG_PCT);}
	public String getTotalMarksMVGNum() {return getStringColumnValue(TOTAL_MARKS_MVG_NUM);}
	public String getTotalMarksMVGPct() {return getStringColumnValue(TOTAL_MARKS_MVG_PCT);}
	public String getTotalMarksTotNum() {return getStringColumnValue(TOTAL_MARKS_TOT_NUM);}
	public String getTotalMarksTotPct() {return getStringColumnValue(TOTAL_MARKS_TOT_PCT);}


	public void setSchoolCode(String value) {setColumn(SCHOOLCODE, value);}
	public void setPeriodFrom(Date from) {setColumn(PERIOD_FROM, from);} 
	public void setPeriodTo(Date to) {setColumn(PERIOD_TO, to);} 
	public void setMeritValueNum(String value) {setColumn(MERITE_VALUE_NUM, value);}
	public void setMeritValuePct(String value) {setColumn(MERITE_VALUE_PCT, value);}
	/*** Obsoltete ***/
	public void setMarksPointsNum(String value) {setColumn(MARKS_POINTS_NUM, value);}
	public void setMarksPointsPct(String value) {setColumn(MARKS_POINTS_PCT, value);}
	public void setAuthPointsNum(String value) {setColumn(AUTH_POINTS_NUM, value);}
	public void setAuthPointsPct(String value) {setColumn(AUTH_POINTS_PCT, value);}
	public void setMarksNrShareNum(String value) {setColumn(MARKS_NR_SHARE_NUM, value);}
	public void setMarksNrSharePct(String value) {setColumn(MARKS_NR_SHARE_PCT, value);}
	public void setMarksFailedNum(String value) {setColumn(MARKS_FAILED_NUM, value);}
	public void setMarksFailedPct(String value) {setColumn(MARKS_FAILED_PCT, value);}
	public void setMarksNoGoalNum(String value) {setColumn(MARKS_NO_GOAL_NUM, value);}
	public void setMarksNoGoalPct(String value) {setColumn(MARKS_NO_GOAL_PCT, value);}
	/*****************/

	/************** New from Nacka ***********/ 

	public void setEnglishMarksEGNum(String value) {setColumn(ENGLISH_MARKS_EG_NUM, value);}
	public void setEnglishMarksEGPct(String value) {setColumn(ENGLISH_MARKS_EG_PCT, value);}
	public void setEnglishMarksGNum(String value) {setColumn(ENGLISH_MARKS_G_NUM, value);}
	public void setEnglishMarksGPct(String value) {setColumn(ENGLISH_MARKS_G_PCT, value);}
	public void setEnglishMarksVGNum(String value) {setColumn(ENGLISH_MARKS_VG_NUM, value);}
	public void setEnglishMarksVGPct(String value) {setColumn(ENGLISH_MARKS_VG_PCT, value);}
	public void setEnglishMarksMVGNum(String value) {setColumn(ENGLISH_MARKS_MVG_NUM, value);}
	public void setEnglishMarksMVGPct(String value) {setColumn(ENGLISH_MARKS_MVG_PCT, value);}
	public void setEnglishMarksTotNum(String value) {setColumn(ENGLISH_MARKS_TOT_NUM, value);}
	public void setEnglishMarksTotPct(String value) {setColumn(ENGLISH_MARKS_TOT_PCT, value);}

	public void setMathsMarksEGNum(String value) {setColumn(MATHS_MARKS_EG_NUM, value);}
	public void setMathsMarksEGPct(String value) {setColumn(MATHS_MARKS_EG_PCT, value);}
	public void setMathsMarksGNum(String value) {setColumn(MATHS_MARKS_G_NUM, value);}
	public void setMathsMarksGPct(String value) {setColumn(MATHS_MARKS_G_PCT, value);}
	public void setMathsMarksVGNum(String value) {setColumn(MATHS_MARKS_VG_NUM, value);}
	public void setMathsMarksVGPct(String value) {setColumn(MATHS_MARKS_VG_PCT, value);}
	public void setMathsMarksMVGNum(String value) {setColumn(MATHS_MARKS_MVG_NUM, value);}
	public void setMathsMarksMVGPct(String value) {setColumn(MATHS_MARKS_MVG_PCT, value);}
	public void setMathsMarksTotNum(String value) {setColumn(MATHS_MARKS_TOT_NUM, value);}
	public void setMathsMarksTotPct(String value) {setColumn(MATHS_MARKS_TOT_PCT, value);}

	public void setSwedishMarksEGNum(String value) {setColumn(SWEDISH_MARKS_EG_NUM, value);}
	public void setSwedishMarksEGPct(String value) {setColumn(SWEDISH_MARKS_EG_PCT, value);}
	public void setSwedishMarksGNum(String value) {setColumn(SWEDISH_MARKS_G_NUM, value);}
	public void setSwedishMarksGPct(String value) {setColumn(SWEDISH_MARKS_G_PCT, value);}
	public void setSwedishMarksVGNum(String value) {setColumn(SWEDISH_MARKS_VG_NUM, value);}
	public void setSwedishMarksVGPct(String value) {setColumn(SWEDISH_MARKS_VG_PCT, value);}
	public void setSwedishMarksMVGNum(String value) {setColumn(SWEDISH_MARKS_MVG_NUM, value);}
	public void setSwedishMarksMVGPct(String value) {setColumn(SWEDISH_MARKS_MVG_PCT, value);}
	public void setSwedishMarksTotNum(String value) {setColumn(SWEDISH_MARKS_TOT_NUM, value);}
	public void setSwedishMarksTotPct(String value) {setColumn(SWEDISH_MARKS_TOT_PCT, value);}

	public void setSwedish2MarksEGNum(String value) {setColumn(SWEDISH2_MARKS_EG_NUM, value);}
	public void setSwedish2MarksEGPct(String value) {setColumn(SWEDISH2_MARKS_EG_PCT, value);}
	public void setSwedish2MarksGNum(String value) {setColumn(SWEDISH2_MARKS_G_NUM, value);}
	public void setSwedish2MarksGPct(String value) {setColumn(SWEDISH2_MARKS_G_PCT, value);}
	public void setSwedish2MarksVGNum(String value) {setColumn(SWEDISH2_MARKS_VG_NUM, value);}
	public void setSwedish2MarksVGPct(String value) {setColumn(SWEDISH2_MARKS_VG_PCT, value);}
	public void setSwedish2MarksMVGNum(String value) {setColumn(SWEDISH2_MARKS_MVG_NUM, value);}
	public void setSwedish2MarksMVGPct(String value) {setColumn(SWEDISH2_MARKS_MVG_PCT, value);}
	public void setSwedish2MarksTotNum(String value) {setColumn(SWEDISH2_MARKS_TOT_NUM, value);}
	public void setSwedish2MarksTotPct(String value) {setColumn(SWEDISH2_MARKS_TOT_PCT, value);}

	public void setSumAuthMarksEGNum(String value) {setColumn(SUM_AUTH_MARKS_EG_NUM, value);}
	public void setSumAuthMarksEGPct(String value) {setColumn(SUM_AUTH_MARKS_EG_PCT, value);}
	public void setSumAuthMarksGNum(String value) {setColumn(SUM_AUTH_MARKS_G_NUM, value);}
	public void setSumAuthMarksGPct(String value) {setColumn(SUM_AUTH_MARKS_G_PCT, value);}
	public void setSumAuthMarksVGNum(String value) {setColumn(SUM_AUTH_MARKS_VG_NUM, value);}
	public void setSumAuthMarksVGPct(String value) {setColumn(SUM_AUTH_MARKS_VG_PCT, value);}
	public void setSumAuthMarksMVGNum(String value) {setColumn(SUM_AUTH_MARKS_MVG_NUM, value);}
	public void setSumAuthMarksMVGPct(String value) {setColumn(SUM_AUTH_MARKS_MVG_PCT, value);}
	public void setSumAuthMarksTotNum(String value) {setColumn(SUM_AUTH_MARKS_TOT_NUM, value);}
	public void setSumAuthMarksTotPct(String value) {setColumn(SUM_AUTH_MARKS_TOT_PCT, value);}

	public void setTotalMarksEGNum(String value) {setColumn(TOTAL_MARKS_EG_NUM, value);}
	public void setTotalMarksEGPct(String value) {setColumn(TOTAL_MARKS_EG_PCT, value);}
	public void setTotalMarksGNum(String value) {setColumn(TOTAL_MARKS_G_NUM, value);}
	public void setTotalMarksGPct(String value) {setColumn(TOTAL_MARKS_G_PCT, value);}
	public void setTotalMarksVGNum(String value) {setColumn(TOTAL_MARKS_VG_NUM, value);}
	public void setTotalMarksVGPct(String value) {setColumn(TOTAL_MARKS_VG_PCT, value);}
	public void setTotalMarksMVGNum(String value) {setColumn(TOTAL_MARKS_MVG_NUM, value);}
	public void setTotalMarksMVGPct(String value) {setColumn(TOTAL_MARKS_MVG_PCT, value);}
	public void setTotalMarksTotNum(String value) {setColumn(TOTAL_MARKS_TOT_NUM, value);}
	public void setTotalMarksTotPct(String value) {setColumn(TOTAL_MARKS_TOT_PCT, value);}


	
	public Object ejbFindBySCBCode(String scbCode) throws FinderException {
			IDOQuery sql = idoQuery();
			sql.appendSelectAllFrom(this).appendWhereEquals(SCHOOLCODE, scbCode);
			return idoFindOnePKByQuery(sql);
	}

	
	public Collection ejbFindAllSchoolStats() throws javax.ejb.FinderException {
		return idoFindPKsBySQL("select * from " + getEntityName());
	}

}
