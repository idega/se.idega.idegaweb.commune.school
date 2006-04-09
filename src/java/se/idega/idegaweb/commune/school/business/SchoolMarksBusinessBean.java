
package se.idega.idegaweb.commune.school.business;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;

import com.idega.data.IDOLookup;
import com.idega.block.school.data.School;
import com.idega.presentation.IWContext;

import se.idega.idegaweb.commune.school.data.SCBCode;
import se.idega.idegaweb.commune.school.data.SCBCodeHome;
import se.idega.idegaweb.commune.school.data.SchoolMarksHome;
import se.idega.idegaweb.commune.school.data.SchoolMarks;
import se.idega.idegaweb.commune.school.data.SchoolStatisticsDataHome;
import se.idega.idegaweb.commune.school.data.SchoolStatisticsData;

/**
 * School marks business
 * <p>
 * $Id: SchoolMarksBusinessBean.java,v 1.13 2006/04/09 11:39:54 laddi Exp $
 *
 * I will add some comments on the school marks calculation technique here later.
 * However I am waiting for Nacka to present me that specification.
 * 
 * Meanwhile if anyone has questions about school statistics calculation please
 * contact either Kelly at Lindman IT AB or
 * Hans Wahlander, Jenny Dahlborg at Agura IT or
 * Jill Salander at Nacka / Team SF
 *
 * @author <a href="mailto:kjell@lindman.com">Kjell Lindman</a>
 * @author <a href="http://www.ncmedia.com">Anders Lindman</a>
 * @version $version$
 */
public class SchoolMarksBusinessBean extends com.idega.business.IBOServiceBean implements SchoolMarksBusiness {

	private static final int TOTAL_MARKS = 16;
	private static final int MERITE_G = 10;
	private static final int MERITE_VG = 15;
	private static final int MERITE_MVG = 20;

	
	private String MARK_G = "G";
	private String MARK_VG = "VG";
	private String MARK_MVG = "MVG";
	private String MARK_IG1 = "1";
	private String MARK_IG2 = "2";
	
	private static int sumTotalMerit = 0;
	private static int numberOfSchools = 0;
	
	private static float _sumMeriteValue = 0;
	private static float _sumMarksPoints = 0;
	private static float _sumAuthPoints = 0;
	private static float _sumMarksNumberShare = 0;
	private static float _sumMarksFailed = 0;
	private static float _sumMarksNoGoal = 0;

	private int _sumEnglishEG = 0;
	private int _sumMathsEG = 0;
	private int _sumSwedishEG = 0;
	private int _sumSwedish2EG = 0;
	//private int _sumTotalEG = 0;

	private int _sumEnglishG = 0;
	private int _sumMathsG = 0;
	private int _sumSwedishG = 0;
	private int _sumSwedish2G = 0;
	//private int _sumTotalG = 0;

	private int _sumEnglishVG = 0;
	private int _sumMathsVG = 0;
	private int _sumSwedishVG = 0;
	private int _sumSwedish2VG = 0;
	//private int _sumTotalVG = 0;

	private int _sumEnglishMVG = 0;
	private int _sumMathsMVG = 0;
	private int _sumSwedishMVG = 0;
	private int _sumSwedish2MVG = 0;
	//private int _sumTotalMVG = 0;

	private static int _tallyMeriteValue = 0;
	private static int _tallyMarksPoints = 0;
	private static int _tallyAuthPoints = 0;
	private static int _tallyMarksNumberShare = 0;
	private static int _tallyMarksFailed = 0;
	private static int _tallyMarksNoGoal = 0;
	
	private int _gNumber = 0;
	private int _vgNumber = 0;
	private int _mvgNumber = 0;
	private int _ig1Number = 0;
	
	public String getBundleIdentifier() {
		return se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER;
	}

	/**
	 * Gets School marks statistics  
	 * @return Collection of SchoolStatistics   
	 * @see se.idega.idegaweb.commune.school.data.SCBCode
	 * @see se.idega.idegaweb.commune.school.business.SchoolStatistics
	 */
	public Collection findSchoolMarksStatistics(IWContext iwc) {
		Collection schoolIDs = null;
		Collection stats = new ArrayList();
		
		schoolIDs = findAllSchoolIDsWithStatistics();
		Iterator iter = schoolIDs.iterator();

		while (iter.hasNext()) {
			SCBCode scb = (SCBCode) iter.next();	
			String scbCode = scb.getCode();
			School school = scb.getSchool();
//			if(scbCode.compareTo("018201001") == 0) {
				SchoolStatistics sc = new SchoolStatistics(iwc, scbCode, school.getSchoolName(), school.getManagementTypeId());
				stats.add(sc);
//			}
		}
		
		return stats;		
	}

	public SchoolStatistics findCommuneStatistics(IWContext iwc) {
		SchoolStatistics sc = new SchoolStatistics(iwc, "", "", null);
		return sc;
	}

	/**
	 * Finds all schools that have statistics attached  
	 * @return Collection of SCBcodes   
	 * @see se.idega.idegaweb.commune.school.data.SCBCode
	 */
	public Collection findAllSchoolIDsWithStatistics() {
		try {
			SCBCodeHome home = (SCBCodeHome) IDOLookup.getHome(SCBCode.class);
			return home.findAll();				
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;		
	}

	/**
	 */
	public void calculateStatistics(String scbCode) {
		
		PercentValue mv = new PercentValue();
		PercentValue mp = new PercentValue();
		PercentValue ap = new PercentValue();

		PercentValue mns = new PercentValue();
		PercentValue mf = new PercentValue();
		PercentValue mng = new PercentValue();

		mv.number = "";
		mv.percent = "";
		if(scbCode.length() == 0) {
			return;
		}
		SchoolStatisticsData ssd = getStoredValues(scbCode);
		if (ssd != null) {
			return;
		}

		int totalMerit = 0;
		int meritCount = 0;
		
		try {
			SchoolMarksHome home = (SchoolMarksHome) IDOLookup.getHome(SchoolMarks.class);
			Collection allmarks = home.findBySCBCode(scbCode);
			Iterator iter = allmarks.iterator();
			
			totalMerit = 0;
			meritCount = 0;
			
			this._gNumber = 0;
			this._vgNumber = 0;
			this._mvgNumber = 0;
			this._ig1Number = 0;
			
			resetSchoolValues(); 
			
			while (iter.hasNext()) {
				SchoolMarks marks = null;
				Object o = iter.next();
				if (o != null) {
					marks = (SchoolMarks) o;
					int tmc = getStudentMeritValue(marks);
//					System.out.println(""+tmc);
					if (tmc > 0) {
						meritCount++;
					}
					totalMerit += tmc;
				}
			}
						
			float mg = Float.parseFloat(""+MERITE_G);
			float mvg = Float.parseFloat(""+MERITE_VG);
			float mmvg = Float.parseFloat(""+MERITE_MVG);

			float fg = Float.parseFloat(""+this._gNumber);
			float fvg = Float.parseFloat(""+this._vgNumber);
			float fmvg = Float.parseFloat(""+this._mvgNumber);
			
			System.out.println("GNUMBER VGNUMBER MVGNUMBER"+this._gNumber+" " +this._vgNumber + " "+this._mvgNumber);
			System.out.println("MERITCOUND TOTALMERIT"+meritCount+" " +totalMerit);
			
			
			Float fr = new Float(0);
			if((fg + fvg + fmvg) != 0) {
				fr = new Float((((fg * mg) + (fvg * mvg) + (fmvg * mmvg))/(fg + fvg + fmvg)));
			}	
			float x = (float) (Math.rint(fr.floatValue() * 100) / 100);

			mp.number = ""+x; 
			mp.percent = " ";
			saveMarksPoints(scbCode, mp, totalMerit, meritCount);

			ap.number = " "; 
			ap.percent = " ";
			saveAuthPoints(scbCode, ap);

			mns.number = "" + (this._gNumber + this._vgNumber + this._mvgNumber);
			mns.percent = " ";
			saveMarksNumberShare(scbCode, mns);

			mf.number = "" + (this._ig1Number);
			mf.percent = " ";
			saveMarksFailed(scbCode, mf);
			
			mng.number="";
			mng.percent="";
			saveMarksNoGoal(scbCode, mng);

			// Save all marks (Eng, Math, Swe, Swe2, SumAuth, Total)
			saveAllMarks(scbCode);
			
			int meanMerit = 0;
			if (meritCount > 0) {
				meanMerit = totalMerit / meritCount;
			}
			
			mv.number = "" + meanMerit;
			int tot = getTotalMerit();
			if (tot > 0) {
				mv.percent = "" + ((meanMerit * 100) / tot) + " " + getNumberOfSchools(home);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveMeriteValue(scbCode, mv);
	}

	/*
	 * 
	 */
	private void saveAllMarks(String scbCode) {

		SchoolMarkValues smvEnglish = new SchoolMarkValues();
		SchoolMarkValues smvMaths = new SchoolMarkValues();
		SchoolMarkValues smvSwedish = new SchoolMarkValues();
		SchoolMarkValues smvSwedish2 = new SchoolMarkValues();
		SchoolMarkValues smvSumAuth = new SchoolMarkValues();
		SchoolMarkValues smvTotal = new SchoolMarkValues();

		int sumEng = this._sumEnglishEG + this._sumEnglishG + this._sumEnglishVG + this._sumEnglishMVG;
		smvEnglish.eg.number = "" + this._sumEnglishEG;
		smvEnglish.eg.percent = "" + (((float)this._sumEnglishEG / (float)sumEng)*100);
		smvEnglish.g.number = "" + this._sumEnglishG;
		smvEnglish.g.percent = "" + (((float)this._sumEnglishG / (float)sumEng)*100);
		smvEnglish.vg.number = "" + this._sumEnglishVG;
		smvEnglish.vg.percent = "" + (((float)this._sumEnglishVG / (float)sumEng)*100);
		smvEnglish.mvg.number = "" + this._sumEnglishMVG;
		smvEnglish.mvg.percent = "" + (((float)this._sumEnglishMVG / (float)sumEng)*100);
		smvEnglish.tot.number = "" + sumEng;  
		smvEnglish.tot.percent = "" + 	((float)((this._sumEnglishG * MERITE_G) + 
										(this._sumEnglishVG * MERITE_VG) + 
										(this._sumEnglishMVG * MERITE_MVG)) / (float) sumEng);

		int sumMath = this._sumMathsEG + this._sumMathsG + this._sumMathsVG + this._sumMathsMVG;
		smvMaths.eg.number = "" + this._sumMathsEG;
		smvMaths.eg.percent = "" + (((float)this._sumMathsEG / (float)sumMath)*100);
		smvMaths.g.number = "" + this._sumMathsG;
		smvMaths.g.percent = "" + (((float)this._sumMathsG / (float)sumMath)*100);
		smvMaths.vg.number = "" + this._sumMathsVG;
		smvMaths.vg.percent = "" + (((float)this._sumMathsVG / (float)sumMath)*100);
		smvMaths.mvg.number = "" + this._sumMathsMVG;
		smvMaths.mvg.percent = "" + (((float)this._sumMathsMVG / (float)sumMath)*100);
		smvMaths.tot.number = "" + sumMath;  
		smvMaths.tot.percent = "" + 	((float)((this._sumMathsG * MERITE_G) + 
										(this._sumMathsVG * MERITE_VG) + 
										(this._sumMathsMVG * MERITE_MVG)) / (float) sumMath);

		int sumSwe = this._sumSwedishEG + this._sumSwedishG + this._sumSwedishVG + this._sumSwedishMVG;
		smvSwedish.eg.number = "" + this._sumSwedishEG;
		smvSwedish.eg.percent = "" + (((float)this._sumSwedishEG / (float)sumSwe)*100);
		smvSwedish.g.number = "" + this._sumSwedishG;
		smvSwedish.g.percent = "" + (((float)this._sumSwedishG / (float)sumSwe)*100);
		smvSwedish.vg.number = "" + this._sumSwedishVG;
		smvSwedish.vg.percent = "" + (((float)this._sumSwedishVG / (float)sumSwe)*100);
		smvSwedish.mvg.number = "" + this._sumSwedishMVG;
		smvSwedish.mvg.percent = "" + (((float)this._sumSwedishMVG / (float)sumSwe)*100);
		smvSwedish.tot.number = "" + sumSwe;  
		smvSwedish.tot.percent = "" + 	((float)((this._sumSwedishG * MERITE_G) + 
										(this._sumSwedishVG * MERITE_VG) + 
										(this._sumSwedishMVG * MERITE_MVG)) / (float) sumSwe);

		int sumSwe2 = this._sumSwedish2EG + this._sumSwedish2G + this._sumSwedish2VG + this._sumSwedish2MVG;
		smvSwedish2.eg.number = "" + this._sumSwedish2EG;
		smvSwedish2.eg.percent = "" + (((float)this._sumSwedish2EG / (float)sumSwe2)*100);
		smvSwedish2.g.number = "" + this._sumSwedish2G;
		smvSwedish2.g.percent = "" + (((float)this._sumSwedish2G / (float)sumSwe2)*100);
		smvSwedish2.vg.number = "" + this._sumSwedish2VG;
		smvSwedish2.vg.percent = "" + (((float)this._sumSwedish2VG / (float)sumSwe2)*100);
		smvSwedish2.mvg.number = "" + this._sumSwedish2MVG;
		smvSwedish2.mvg.percent = "" + (((float)this._sumSwedish2MVG / (float)sumSwe2)*100);
		smvSwedish2.tot.number = "" + sumSwe2;  
		smvSwedish2.tot.percent = "" + 	((float)((this._sumSwedish2G * MERITE_G) + 
										(this._sumSwedish2VG * MERITE_VG) + 
										(this._sumSwedish2MVG * MERITE_MVG)) / (float) sumSwe2);


		int sumAuth = Integer.parseInt(smvEnglish.tot.number) + Integer.parseInt(smvMaths.tot.number) + 
						Integer.parseInt(smvSwedish.tot.number) + Integer.parseInt(smvSwedish2.tot.number);


		smvSumAuth.eg.number = "" + (Integer.parseInt(smvEnglish.eg.number) + Integer.parseInt(smvMaths.eg.number) + Integer.parseInt(smvSwedish.eg.number) + Integer.parseInt(smvSwedish2.eg.number));
		smvSumAuth.eg.percent = "" +  (((float) (Integer.parseInt(smvSumAuth.eg.number))) / 
										((float)sumAuth)) * 100;
		 
		smvSumAuth.g.number = "" + (Integer.parseInt(smvEnglish.g.number) + Integer.parseInt(smvMaths.g.number) + Integer.parseInt(smvSwedish.g.number) + Integer.parseInt(smvSwedish2.g.number));
		smvSumAuth.g.percent = "" +  (((float) (Integer.parseInt(smvSumAuth.g.number))) / 
										((float)sumAuth)) * 100;
		
		smvSumAuth.vg.number = "" + (Integer.parseInt(smvEnglish.vg.number) + Integer.parseInt(smvMaths.vg.number) + Integer.parseInt(smvSwedish.vg.number) + Integer.parseInt(smvSwedish2.vg.number));
		smvSumAuth.vg.percent = "" +  (((float) (Integer.parseInt(smvSumAuth.vg.number))) / 
										((float)sumAuth)) * 100;

		smvSumAuth.mvg.number = "" + (Integer.parseInt(smvEnglish.mvg.number) + Integer.parseInt(smvMaths.mvg.number) + Integer.parseInt(smvSwedish.mvg.number) + Integer.parseInt(smvSwedish2.mvg.number));
		smvSumAuth.mvg.percent = "" +  (((float) (Integer.parseInt(smvSumAuth.mvg.number))) / 
										((float)sumAuth)) * 100;
		
		smvSumAuth.tot.number = "" + sumAuth;  
		float sw2 = 0;
		float authDiv = 3;
		if (Integer.parseInt(smvSwedish2.tot.number) != 0) {
			sw2 = Float.parseFloat(smvSwedish2.tot.percent);
			authDiv = 4;
		}
		smvSumAuth.tot.percent = "" +  	((Float.parseFloat(smvEnglish.tot.percent) +
										Float.parseFloat(smvMaths.tot.percent) +
									 	Float.parseFloat(smvSwedish.tot.percent) + 
										sw2)/authDiv);

		int sumTotal = this._ig1Number + this._gNumber + this._vgNumber + this._mvgNumber;

		smvTotal.eg.number = "" + this._ig1Number;
		smvTotal.eg.percent = "" +  (((float) (Integer.parseInt(smvTotal.eg.number))) / 
										((float)sumTotal))*100;
		 
		smvTotal.g.number = "" +  this._gNumber;
		smvTotal.g.percent = "" +  (((float) (Integer.parseInt(smvTotal.g.number))) / 
										((float)sumTotal))*100;
		
		smvTotal.vg.number = "" + this._vgNumber;
		smvTotal.vg.percent = "" +  (((float) (Integer.parseInt(smvTotal.vg.number))) / 
										((float)sumTotal))*100;

		smvTotal.mvg.number = "" + this._mvgNumber;
		smvTotal.mvg.percent = "" +  (((float) (Integer.parseInt(smvTotal.mvg.number))) / 
										((float)sumTotal))*100;
		
		smvTotal.tot.number = "" + sumTotal;  
		smvTotal.tot.percent = "" + ((float)((this._gNumber * MERITE_G) + (this._vgNumber * MERITE_VG) + (this._mvgNumber * MERITE_MVG))) / ((float)(this._gNumber + this._vgNumber +this._mvgNumber)); 	
		
		boolean found = true;
		SchoolStatisticsDataHome home = null;
		SchoolStatisticsData ss = null;
		try {
			home = (SchoolStatisticsDataHome) IDOLookup.getHome(SchoolStatisticsData.class);
			ss = home.findBySCBCode(scbCode);
		} catch (RemoteException e) {
			e.printStackTrace();
			found = false;
		} catch (FinderException e) {
			found = false;
		}

		if(!found) {
			try {
				ss = home.create();
				ss.setSchoolCode(scbCode);

			} catch (CreateException e) {
				e.printStackTrace();
			}
		}
		ss.setEnglishMarksEGNum(smvEnglish.eg.number);
		ss.setEnglishMarksEGPct(smvEnglish.eg.percent);
		ss.setEnglishMarksGNum(smvEnglish.g.number);
		ss.setEnglishMarksGPct(smvEnglish.g.percent);
		ss.setEnglishMarksVGNum(smvEnglish.vg.number);
		ss.setEnglishMarksVGPct(smvEnglish.vg.percent);
		ss.setEnglishMarksMVGNum(smvEnglish.mvg.number);
		ss.setEnglishMarksMVGPct(smvEnglish.mvg.percent);
		ss.setEnglishMarksTotNum(smvEnglish.tot.number);
		ss.setEnglishMarksTotPct(smvEnglish.tot.percent);

		ss.setMathsMarksEGNum(smvMaths.eg.number);
		ss.setMathsMarksEGPct(smvMaths.eg.percent);
		ss.setMathsMarksGNum(smvMaths.g.number);
		ss.setMathsMarksGPct(smvMaths.g.percent);
		ss.setMathsMarksVGNum(smvMaths.vg.number);
		ss.setMathsMarksVGPct(smvMaths.vg.percent);
		ss.setMathsMarksMVGNum(smvMaths.mvg.number);
		ss.setMathsMarksMVGPct(smvMaths.mvg.percent);
		ss.setMathsMarksTotNum(smvMaths.tot.number);
		ss.setMathsMarksTotPct(smvMaths.tot.percent);

		ss.setSwedishMarksEGNum(smvSwedish.eg.number);
		ss.setSwedishMarksEGPct(smvSwedish.eg.percent);
		ss.setSwedishMarksGNum(smvSwedish.g.number);
		ss.setSwedishMarksGPct(smvSwedish.g.percent);
		ss.setSwedishMarksVGNum(smvSwedish.vg.number);
		ss.setSwedishMarksVGPct(smvSwedish.vg.percent);
		ss.setSwedishMarksMVGNum(smvSwedish.mvg.number);
		ss.setSwedishMarksMVGPct(smvSwedish.mvg.percent);
		ss.setSwedishMarksTotNum(smvSwedish.tot.number);
		ss.setSwedishMarksTotPct(smvSwedish.tot.percent);

		ss.setSwedish2MarksEGNum(smvSwedish2.eg.number);
		ss.setSwedish2MarksEGPct(smvSwedish2.eg.percent);
		ss.setSwedish2MarksGNum(smvSwedish2.g.number);
		ss.setSwedish2MarksGPct(smvSwedish2.g.percent);
		ss.setSwedish2MarksVGNum(smvSwedish2.vg.number);
		ss.setSwedish2MarksVGPct(smvSwedish2.vg.percent);
		ss.setSwedish2MarksMVGNum(smvSwedish2.mvg.number);
		ss.setSwedish2MarksMVGPct(smvSwedish2.mvg.percent);
		ss.setSwedish2MarksTotNum(smvSwedish2.tot.number);
		ss.setSwedish2MarksTotPct(smvSwedish2.tot.percent);

		ss.setSumAuthMarksEGNum(smvSumAuth.eg.number);
		ss.setSumAuthMarksEGPct(smvSumAuth.eg.percent);
		ss.setSumAuthMarksGNum(smvSumAuth.g.number);
		ss.setSumAuthMarksGPct(smvSumAuth.g.percent);
		ss.setSumAuthMarksVGNum(smvSumAuth.vg.number);
		ss.setSumAuthMarksVGPct(smvSumAuth.vg.percent);
		ss.setSumAuthMarksMVGNum(smvSumAuth.mvg.number);
		ss.setSumAuthMarksMVGPct(smvSumAuth.mvg.percent);
		ss.setSumAuthMarksTotNum(smvSumAuth.tot.number);
		ss.setSumAuthMarksTotPct(smvSumAuth.tot.percent);

		ss.setTotalMarksEGNum(smvTotal.eg.number);
		ss.setTotalMarksEGPct(smvTotal.eg.percent);
		ss.setTotalMarksGNum(smvTotal.g.number);
		ss.setTotalMarksGPct(smvTotal.g.percent);
		ss.setTotalMarksVGNum(smvTotal.vg.number);
		ss.setTotalMarksVGPct(smvTotal.vg.percent);
		ss.setTotalMarksMVGNum(smvTotal.mvg.number);
		ss.setTotalMarksMVGPct(smvTotal.mvg.percent);
		ss.setTotalMarksTotNum(smvTotal.tot.number);
		ss.setTotalMarksTotPct(smvTotal.tot.percent);
		
		ss.store();

	}

	/**
	 * Find MeritValue (Meritv�rde) for a specified school
	 * @param ssd SchoolStatisticsData
	 * @return Meritvalue (Meritv�rde) 
	 * @see se.idega.idegaweb.commune.school.data.SchoolMarks;
	 */
	public PercentValue getMeritValue(SchoolStatisticsData ssd) {
		PercentValue ret = new PercentValue();
		if (ssd == null) {
			ret.number = " ";
			ret.percent = " ";
			return ret;
		}
		ret.number = ssd.getMeritValueNum() != null ? ssd.getMeritValueNum() : " ";
		ret.percent = ssd.getMeritValuePct() != null ? ssd.getMeritValuePct() : " ";
		return ret;
	}


	/**
	 * Gets the mean value ("Betygspo�ng") and percent for the specified school. 
	 * @param ssd SchoolStatisticsData
	 * @return Mean and percent values 
	 * @see se.idega.idegaweb.commune.school.business.PercentValue
	 */
	public PercentValue getMarksPoints(SchoolStatisticsData ssd) {
		PercentValue ret = new PercentValue();
		if (ssd == null) {
			ret.number = " ";
			ret.percent = " ";
			return ret;
		}
		ret.number = ssd.getMarksPointsNum() != null ? ssd.getMarksPointsNum() : " ";
		ret.percent = ssd.getMarksPointsPct() != null ? ssd.getMarksPointsPct() : " ";
		return ret;
	}

	/**
	 * Gets the mean value ("Beh�righetspo�ng") and percent for the specified school. 
	 * @param ssd SchoolStatisticsData
	 * @return Mean and percent values
	 * @see se.idega.idegaweb.commune.school.business.PercentValue
	 */
	public PercentValue getAuthPoints(SchoolStatisticsData ssd) {
		PercentValue ret = new PercentValue();
		if (ssd == null) {
			ret.number = " ";
			ret.percent = " ";
			return ret;
		}
		ret.number = ssd.getAuthPointsNum() != null ? ssd.getAuthPointsNum() : " ";
		ret.percent = ssd.getAuthPointsPct() != null ? ssd.getAuthPointsPct() : " ";
		return ret;
	}

	/**
	 * Gets the mean value ("Antal och andel G, VG och MVG") and 
	 * percent for the specified school. 
	 * @param ssd SchoolStatisticsData
	 * @return Mean and percent values
	 * @see se.idega.idegaweb.commune.school.business.PercentValue
	 */
	public PercentValue getMarksNumberShare(SchoolStatisticsData ssd) {
		PercentValue ret = new PercentValue();
		if (ssd == null) {
			ret.number = " ";
			ret.percent = " ";
			return ret;
		}
		ret.number = ssd.getMarksNrShareNum() != null ? ssd.getMarksNrShareNum() : " ";
		ret.percent = ssd.getMarksNrSharePct() != null ? ssd.getMarksNrSharePct() : " ";
		return ret;
	}

	/**
	 * Gets the mean value ("Antal och andel ej godk�nda") and 
	 * percent for the specified school. 
	 * @param ssd SchoolStatisticsData
	 * @return Mean and percent values
	 * @see se.idega.idegaweb.commune.school.business.PercentValue
	 */
	public PercentValue getMarksFailed(SchoolStatisticsData ssd) {
		PercentValue ret = new PercentValue();
		if (ssd == null) {
			return ret;
		}
		ret.number = ssd.getMarksFailedNum() != null ? ssd.getMarksFailedNum() : " ";
		ret.percent = ssd.getMarksFailedPct() != null ? ssd.getMarksFailedPct() : " ";
		return ret;
	}

	/**
	 * Gets the mean value ("Antal som ej uppn�tt m�l i beh�righetsgivande �mne") and 
	 * percent for the specified school. 
	 * @param ssd SchoolStatisticsData
	 * @return Mean and percent values
	 * @see se.idega.idegaweb.commune.school.business.PercentValue
	 */
	public PercentValue getMarksNoGoal(SchoolStatisticsData ssd) {
		PercentValue ret = new PercentValue();
		if (ssd == null) {
			ret.number = " ";
			ret.percent = " ";
			return ret;
		}
		ret.number = ssd.getMarksNoGoalNum() != null ? ssd.getMarksNoGoalNum() : " ";
		ret.percent = ssd.getMarksNoGoalPct() != null ? ssd.getMarksNoGoalPct() : " ";
		return ret;
	}

	/**
	 * Gets English marks (EG, G, VG, MVG and Total) 
	 * number and percent for the specified school. 
	 * @param ssd SchoolStatisticsData
	 * @return SchoolMarkValues  (EG, G, VG, MVG and Total)
	 * @see se.idega.idegaweb.commune.school.business.PercentValue#
	 * @see se.idega.idegaweb.commune.school.business.SchoolMarkValues#
	 */
	public SchoolMarkValues getEnglishMarks(SchoolStatisticsData ssd) {
		SchoolMarkValues smv = new SchoolMarkValues();
		if (ssd != null) {
			smv.eg = new PercentValue(ssd.getEnglishMarksEGNum(), ssd.getEnglishMarksEGPct()); 
			smv.g = new PercentValue(ssd.getEnglishMarksGNum(), ssd.getEnglishMarksGPct()); 
			smv.vg = new PercentValue(ssd.getEnglishMarksVGNum(), ssd.getEnglishMarksVGPct()); 
			smv.mvg = new PercentValue(ssd.getEnglishMarksMVGNum(), ssd.getEnglishMarksMVGPct()); 
			smv.tot = new PercentValue(ssd.getEnglishMarksTotNum(), ssd.getEnglishMarksTotPct());
		} 
		return smv;
	}
	
	/**
	 * Gets Math marks (EG, G, VG, MVG and Total) 
	 * number and percent for the specified school. 
	 * @param ssd SchoolStatisticsData
	 * @return SchoolMarkValues  (EG, G, VG, MVG and Total)
	 * @see se.idega.idegaweb.commune.school.business.PercentValue#
	 * @see se.idega.idegaweb.commune.school.business.SchoolMarkValues#
	 */
	public SchoolMarkValues getMathsMarks(SchoolStatisticsData ssd) {
		SchoolMarkValues smv = new SchoolMarkValues();
		if (ssd != null) {
			smv.eg = new PercentValue(ssd.getMathsMarksEGNum(), ssd.getMathsMarksEGPct()); 
			smv.g = new PercentValue(ssd.getMathsMarksGNum(), ssd.getMathsMarksGPct()); 
			smv.vg = new PercentValue(ssd.getMathsMarksVGNum(), ssd.getMathsMarksVGPct()); 
			smv.mvg = new PercentValue(ssd.getMathsMarksMVGNum(), ssd.getMathsMarksMVGPct()); 
			smv.tot = new PercentValue(ssd.getMathsMarksTotNum(), ssd.getMathsMarksTotPct()); 
		}
		return smv;
	}

	/**
	 * Gets Swedish marks (EG, G, VG, MVG and Total) 
	 * number and percent for the specified school. 
	 * @param ssd SchoolStatisticsData
	 * @return SchoolMarkValues  (EG, G, VG, MVG and Total)
	 * @see se.idega.idegaweb.commune.school.business.PercentValue#
	 * @see se.idega.idegaweb.commune.school.business.SchoolMarkValues#
	 */
	public SchoolMarkValues getSwedishMarks(SchoolStatisticsData ssd) {
		SchoolMarkValues smv = new SchoolMarkValues();
		if (ssd != null) {
			smv.eg = new PercentValue(ssd.getSwedishMarksEGNum(), ssd.getSwedishMarksEGPct()); 
			smv.g = new PercentValue(ssd.getSwedishMarksGNum(), ssd.getSwedishMarksGPct()); 
			smv.vg = new PercentValue(ssd.getSwedishMarksVGNum(), ssd.getSwedishMarksVGPct()); 
			smv.mvg = new PercentValue(ssd.getSwedishMarksMVGNum(), ssd.getSwedishMarksMVGPct()); 
			smv.tot = new PercentValue(ssd.getSwedishMarksTotNum(), ssd.getSwedishMarksTotPct());
		} 
		return smv;
	}

	/**
	 * Gets Swedish 2 marks (EG, G, VG, MVG and Total) 
	 * number and percent for the specified school. 
	 * @param ssd SchoolStatisticsData
	 * @return SchoolMarkValues  (EG, G, VG, MVG and Total)
	 * @see se.idega.idegaweb.commune.school.business.PercentValue#
	 * @see se.idega.idegaweb.commune.school.business.SchoolMarkValues#
	 */
	public SchoolMarkValues getSwedish2Marks(SchoolStatisticsData ssd) {
		SchoolMarkValues smv = new SchoolMarkValues();
		if (ssd != null) {
			smv.eg = new PercentValue(ssd.getSwedish2MarksEGNum(), ssd.getSwedish2MarksEGPct()); 
			smv.g = new PercentValue(ssd.getSwedish2MarksGNum(), ssd.getSwedish2MarksGPct()); 
			smv.vg = new PercentValue(ssd.getSwedish2MarksVGNum(), ssd.getSwedish2MarksVGPct()); 
			smv.mvg = new PercentValue(ssd.getSwedish2MarksMVGNum(), ssd.getSwedish2MarksMVGPct()); 
			smv.tot = new PercentValue(ssd.getSwedish2MarksTotNum(), ssd.getSwedish2MarksTotPct()); 
		}
		return smv;
	}

	/**
	 * Gets summed auth marks (EG, G, VG, MVG and Total) 
	 * number and percent for the specified school. 
	 * @param ssd SchoolStatisticsData
	 * @return SchoolMarkValues  (EG, G, VG, MVG and Total)
	 * @see se.idega.idegaweb.commune.school.business.PercentValue#
	 * @see se.idega.idegaweb.commune.school.business.SchoolMarkValues#
	 */
	public SchoolMarkValues getSumAuthMarks(SchoolStatisticsData ssd) {
		SchoolMarkValues smv = new SchoolMarkValues();
		if (ssd != null) {
			smv.eg = new PercentValue(ssd.getSumAuthMarksEGNum(), ssd.getSumAuthMarksEGPct()); 
			smv.g = new PercentValue(ssd.getSumAuthMarksGNum(), ssd.getSumAuthMarksGPct()); 
			smv.vg = new PercentValue(ssd.getSumAuthMarksVGNum(), ssd.getSumAuthMarksVGPct()); 
			smv.mvg = new PercentValue(ssd.getSumAuthMarksMVGNum(), ssd.getSumAuthMarksMVGPct()); 
			smv.tot = new PercentValue(ssd.getSumAuthMarksTotNum(), ssd.getSumAuthMarksTotPct());
		} 
		return smv;
	}

	/**
	 * Gets Total marks (EG, G, VG, MVG and Total) 
	 * number and percent for the specified school. 
	 * @param ssd SchoolStatisticsData
	 * @return SchoolMarkValues  (EG, G, VG, MVG and Total)
	 * @see se.idega.idegaweb.commune.school.business.PercentValue#
	 * @see se.idega.idegaweb.commune.school.business.SchoolMarkValues#
	 */
	public SchoolMarkValues getTotalMarks(SchoolStatisticsData ssd) {
		SchoolMarkValues smv = new SchoolMarkValues();
		if (ssd != null) {
			smv.eg = new PercentValue(ssd.getTotalMarksEGNum(), ssd.getTotalMarksEGPct()); 
			smv.g = new PercentValue(ssd.getTotalMarksGNum(), ssd.getTotalMarksGPct()); 
			smv.vg = new PercentValue(ssd.getTotalMarksVGNum(), ssd.getTotalMarksVGPct()); 
			smv.mvg = new PercentValue(ssd.getTotalMarksMVGNum(), ssd.getTotalMarksMVGPct()); 
			smv.tot = new PercentValue(ssd.getTotalMarksTotNum(), ssd.getTotalMarksTotPct()); 
		}
		return smv;
	}

	/*
	 * SUB METHODS 
	 */
	
	private void saveMeriteValue(String scbCode, PercentValue value) {
		boolean found = true;
		SchoolStatisticsDataHome home = null;
		SchoolStatisticsData ss = null;
		try {
			home = (SchoolStatisticsDataHome) IDOLookup.getHome(SchoolStatisticsData.class);
			ss = home.findBySCBCode(scbCode);
		} catch (RemoteException e) {
			e.printStackTrace();
			found = false;
		} catch (FinderException e) {
			found = false;
		}

		if(!found) {
			try {
				ss = home.create();
				ss.setSchoolCode(scbCode);
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}
		ss.setMeritValueNum(value.number);
		ss.setMeritValuePct(value.percent);
		ss.store();
	}

	private void saveMarksPoints(String scbCode, PercentValue value, int totalMerite, int totalStudents) {
		boolean found = true;
		SchoolStatisticsDataHome home = null;
		SchoolStatisticsData ss = null;
		try {
			home = (SchoolStatisticsDataHome) IDOLookup.getHome(SchoolStatisticsData.class);
			ss = home.findBySCBCode(scbCode);
		} catch (RemoteException e) {
			e.printStackTrace();
			found = false;
		} catch (FinderException e) {
			found = false;
		}

		if(!found) {
			try {
				ss = home.create();
				ss.setSchoolCode(scbCode);
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}
		ss.setMarksPointsNum(value.number);
		ss.setMarksPointsPct(value.percent);
		ss.setTotalAuthPoints(""+totalMerite);
		ss.setTotalAuthStudents(""+totalStudents);
		ss.store();
	}

	private void saveAuthPoints(String scbCode, PercentValue value) {
		boolean found = true;
		SchoolStatisticsDataHome home = null;
		SchoolStatisticsData ss = null;
		try {
			home = (SchoolStatisticsDataHome) IDOLookup.getHome(SchoolStatisticsData.class);
			ss = home.findBySCBCode(scbCode);
		} catch (RemoteException e) {
			e.printStackTrace();
			found = false;
		} catch (FinderException e) {
			found = false;
		}

		if(!found) {
			try {
				ss = home.create();
				ss.setSchoolCode(scbCode);
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}
		ss.setAuthPointsNum(value.number);
		ss.setAuthPointsPct(value.percent);
		ss.store();
	}


	private void saveMarksNumberShare(String scbCode, PercentValue value) {
		boolean found = true;
		SchoolStatisticsDataHome home = null;
		SchoolStatisticsData ss = null;
		try {
			home = (SchoolStatisticsDataHome) IDOLookup.getHome(SchoolStatisticsData.class);
			ss = home.findBySCBCode(scbCode);
		} catch (RemoteException e) {
			e.printStackTrace();
			found = false;
		} catch (FinderException e) {
			found = false;
		}

		if(!found) {
			try {
				ss = home.create();
				ss.setSchoolCode(scbCode);
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}
		ss.setMarksNrShareNum(value.number);
		ss.setMarksNrSharePct(value.percent);
		ss.store();
	}

	private void saveMarksFailed(String scbCode, PercentValue value) {
		boolean found = true;
		SchoolStatisticsDataHome home = null;
		SchoolStatisticsData ss = null;
		try {
			home = (SchoolStatisticsDataHome) IDOLookup.getHome(SchoolStatisticsData.class);
			ss = home.findBySCBCode(scbCode);
		} catch (RemoteException e) {
			e.printStackTrace();
			found = false;
		} catch (FinderException e) {
			found = false;
		}

		if(!found) {
			try {
				ss = home.create();
				ss.setSchoolCode(scbCode);
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}
		ss.setMarksFailedNum(value.number);
		ss.setMarksFailedPct(value.percent);
		ss.store();
	}

	private void saveMarksNoGoal(String scbCode, PercentValue value) {
		boolean found = true;
		SchoolStatisticsDataHome home = null;
		SchoolStatisticsData ss = null;
		try {
			home = (SchoolStatisticsDataHome) IDOLookup.getHome(SchoolStatisticsData.class);
			ss = home.findBySCBCode(scbCode);
		} catch (RemoteException e) {
			e.printStackTrace();
			found = false;
		} catch (FinderException e) {
			found = false;
		}

		if(!found) {
			try {
				ss = home.create();
				ss.setSchoolCode(scbCode);
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}
		ss.setMarksNoGoalNum(value.number);
		ss.setMarksNoGoalPct(value.percent);
		ss.store();
	}



	/**
	 * Finds total MeritValue (Meritv�rde) 
	 * @return Meritvalue (Meritv�rde) 
	 * @see se.idega.idegaweb.commune.school.data.SchoolMarks;
	 */
	public int getTotalMerit() {
		if (sumTotalMerit != 0) {
			return sumTotalMerit;
		}
		try {
			SchoolMarksHome home = (SchoolMarksHome) IDOLookup.getHome(SchoolMarks.class);

			Collection allmarks = home.findAllSchoolMarks();
			Iterator iter = allmarks.iterator();
			
			int totalMerit = 0;
			int meritCount = 0;
						
			while (iter.hasNext()) {
				SchoolMarks marks = null;
				Object o = iter.next();
				if (o != null) {
					marks = (SchoolMarks) o;
					meritCount++;
					totalMerit += getStudentMeritValue(marks);
				}
			}
			
			sumTotalMerit = totalMerit;
			if (meritCount > 0) {
				sumTotalMerit /= meritCount; 
			}
			sumTotalMerit *= getNumberOfSchools(home);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sumTotalMerit;
	}


	/**
	 * Finds number of schools in SchoolMarks 
	 * @param scbCode SCB school code
	 * @return number of schools 
	 * @see se.idega.idegaweb.commune.school.business.PercentValue
	 * @see se.idega.idegaweb.commune.school.data.SchoolMarks;
	 */
	private int getNumberOfSchools(SchoolMarksHome home) {
		if (numberOfSchools != 0) {
			return numberOfSchools;
		}

		int numSchooles = 0;
		try {		
			Collection c = home.findNumberOfSchools();
			Iterator iter = c.iterator();
			if (iter.hasNext()) {
				Integer i = (Integer) iter.next();
				numSchooles = i.intValue();
			}
		} catch (Exception e) {}
		
		numberOfSchools = numSchooles;
		return numberOfSchools;
	}
	

	/*
	 * 
	 */
	private int getStudentGValue(SchoolMarks marks) {
		int gCount = 0;
		if ((""+marks.getBL()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getEN()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getEN()).equals(this.MARK_G)) {
			this._sumEnglishG++;
		}
		if ((""+marks.getHKK()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getIDH()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getMA()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getMA()).equals(this.MARK_G)) {
			this._sumMathsG++;
		}
		if ((""+marks.getM1G()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getM2G()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getMLG()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getMU()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getNO()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getBI()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getFY()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getKE()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getSO()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getGE()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getHI()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getRE()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getSH()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getSL()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getSV()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getSV()).equals(this.MARK_G)) {
			this._sumSwedishG++;
		}
		if ((""+marks.getSVA()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getSVA()).equals(this.MARK_G)) {
			this._sumSwedish2G++;
		}
		if ((""+marks.getTN()).equals(this.MARK_G)) {
			gCount++;
		}
		if ((""+marks.getTK()).equals(this.MARK_G)) {
			gCount++;
		}

		if (((""+marks.getNO()).equals(this.MARK_G)) && 
			((""+marks.getBI()).equals(this.MARK_IG2)) &&
			((""+marks.getFY()).equals(this.MARK_IG2)) &&
			((""+marks.getKE()).equals(this.MARK_IG2))) {
			gCount+=2;		
		}

		if (((""+marks.getSO()).equals(this.MARK_G)) && 
			((""+marks.getGE()).equals(this.MARK_IG2)) &&
			((""+marks.getHI()).equals(this.MARK_IG2)) &&
			((""+marks.getRE()).equals(this.MARK_IG2)) &&
			((""+marks.getSH()).equals(this.MARK_IG2))) {
			gCount+=3;		
		}

		return gCount;
	}

	/*
	 * 
	 */
	private int getStudentVgValue(SchoolMarks marks) {
		int vgCount = 0;
		if ((""+marks.getBL()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getEN()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getEN()).equals(this.MARK_VG)) {
			this._sumEnglishVG++;
		}
		if ((""+marks.getHKK()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getIDH()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getMA()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getMA()).equals(this.MARK_VG)) {
			this._sumMathsVG++;
		}
		if ((""+marks.getM1G()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getM2G()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getMLG()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getMU()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getNO()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getBI()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getFY()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getKE()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getSO()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getGE()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getHI()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getRE()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getSH()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getSL()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getSV()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getSV()).equals(this.MARK_VG)) {
			this._sumSwedishVG++;
		}
		if ((""+marks.getSVA()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getSVA()).equals(this.MARK_VG)) {
			this._sumSwedish2VG++;
		}
		if ((""+marks.getTN()).equals(this.MARK_VG)) {
			vgCount++;
		}
		if ((""+marks.getTK()).equals(this.MARK_VG)) {
			vgCount++;
		}

		if (((""+marks.getNO()).equals(this.MARK_VG)) && 
			((""+marks.getBI()).equals(this.MARK_IG2)) &&
			((""+marks.getFY()).equals(this.MARK_IG2)) &&
			((""+marks.getKE()).equals(this.MARK_IG2))) {
			vgCount+=2;		
		}

		if (((""+marks.getSO()).equals(this.MARK_VG)) && 
			((""+marks.getGE()).equals(this.MARK_IG2)) &&
			((""+marks.getHI()).equals(this.MARK_IG2)) &&
			((""+marks.getRE()).equals(this.MARK_IG2)) &&
			((""+marks.getSH()).equals(this.MARK_IG2))) {
			vgCount+=3;		
		}

		return vgCount;
	}

	/*
	 * 
	 */
	private int getStudentMvgValue(SchoolMarks marks) {
		int mvgCount = 0;
		if ((""+marks.getBL()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getEN()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getEN()).equals(this.MARK_MVG)) {
			this._sumEnglishMVG++;
		}
		if ((""+marks.getHKK()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getIDH()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getMA()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getMA()).equals(this.MARK_MVG)) {
			this._sumMathsMVG++;
		}
		if ((""+marks.getM1G()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getM2G()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getMLG()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getMU()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getNO()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getBI()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getFY()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getKE()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getSO()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getGE()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getHI()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getRE()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getSH()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getSL()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getSV()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getSV()).equals(this.MARK_MVG)) {
			this._sumSwedishMVG++;
		}
		if ((""+marks.getSVA()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getSVA()).equals(this.MARK_MVG)) {
			this._sumSwedish2MVG++;
		}
		if ((""+marks.getTN()).equals(this.MARK_MVG)) {
			mvgCount++;
		}
		if ((""+marks.getTK()).equals(this.MARK_MVG)) {
			mvgCount++;
		}

		if (((""+marks.getNO()).equals(this.MARK_MVG)) && 
			((""+marks.getBI()).equals(this.MARK_IG2)) &&
			((""+marks.getFY()).equals(this.MARK_IG2)) &&
			((""+marks.getKE()).equals(this.MARK_IG2))) {
			mvgCount+=2;		
		}

		if (((""+marks.getSO()).equals(this.MARK_MVG)) && 
			((""+marks.getGE()).equals(this.MARK_IG2)) &&
			((""+marks.getHI()).equals(this.MARK_IG2)) &&
			((""+marks.getRE()).equals(this.MARK_IG2)) &&
			((""+marks.getSH()).equals(this.MARK_IG2))) {
			mvgCount+=3;		
		}

		return mvgCount;
	}


	/*
	 * 
	 */
	private int getStudentIg1Value(SchoolMarks marks) {
		int ig1Count = 0;
		if ((""+marks.getBL()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getEN()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getEN()).equals(this.MARK_IG1)) {
			this._sumEnglishEG++;
		}
		if ((""+marks.getHKK()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getIDH()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getMA()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getMA()).equals(this.MARK_IG1)) {
			this._sumMathsEG++;
		}
		if ((""+marks.getM1G()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getM2G()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getMLG()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getMU()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getNO()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getBI()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getFY()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getKE()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getSO()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getGE()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getHI()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getRE()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getSH()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getSL()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getSV()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getSV()).equals(this.MARK_IG1)) {
			this._sumSwedishEG++;
		}
		if ((""+marks.getSVA()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getSVA()).equals(this.MARK_IG1)) {
			this._sumSwedish2EG++;
		}
		if ((""+marks.getTN()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		if ((""+marks.getTK()).equals(this.MARK_IG1)) {
			ig1Count++;
		}
		return ig1Count;
	}

	
	/**
	 * Gets the mean value ("Meritv�rde") and percent for the specified school.
	 *  
	 * @param marks Home of SchoolMarks
	 * @return Mean and percent values 
	 * @see se.idega.id egaweb.commune.school.data.SchoolMarks;
	 * @see se.idega.idegaweb.commune.school.business.PercentValue
	 */
	private int getStudentMeritValue(SchoolMarks marks) {
		int gCount = 0;
		int vgCount = 0;
		int mvgCount = 0;
		int ig1Count = 0;
		
		gCount = getStudentGValue(marks);
		vgCount = getStudentVgValue(marks);
		mvgCount = getStudentMvgValue(marks);
		ig1Count = getStudentIg1Value(marks);
	
		int maxCount = TOTAL_MARKS;

		
		// MVG
		if (mvgCount > maxCount) {
			mvgCount = maxCount;
		}	
		int totalScore = mvgCount * MERITE_MVG;
		maxCount -= mvgCount;
		// VG
		if (vgCount > maxCount) {
			vgCount = maxCount;
		}
		totalScore += vgCount * MERITE_VG;
		maxCount -= vgCount;

		// G
		if (gCount > maxCount) {
			gCount = maxCount;
		}
		
		totalScore += gCount * MERITE_G;

		this._gNumber += gCount;
		this._vgNumber += vgCount;
		this._mvgNumber += mvgCount;
		this._ig1Number += ig1Count;
		
		return totalScore;
	}
	

	public SchoolStatisticsData getStoredValues(String scbCode) {
		SchoolStatisticsDataHome home = null;
		SchoolStatisticsData ssd = null;
		try {
			home = getSchoolStatisticsDataHome();
			ssd = home.findBySCBCode(scbCode);
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}
		return ssd;	
	}


	/*
	 * Get the Statistics data home 
	 */
	protected SchoolStatisticsDataHome getSchoolStatisticsDataHome() throws RemoteException {
		return (SchoolStatisticsDataHome) com.idega.data.IDOLookup.getHome(SchoolStatisticsData.class);
	}	

	/******************************************/
	/*          Summing methods               */
	/******************************************/

	public void resetMeanValues() {
		_sumMeriteValue = 0;
		_sumMarksPoints = 0;
		_sumAuthPoints = 0;
		_sumMarksNumberShare = 0;
		_sumMarksFailed = 0;
		_sumMarksNoGoal = 0;
		_tallyMeriteValue = 0;
		_tallyMarksPoints = 0;
		_tallyAuthPoints = 0;
		_tallyMarksNumberShare = 0;
		_tallyMarksFailed = 0;
		_tallyMarksNoGoal = 0;
	}

	public void resetSchoolValues() {

		this._sumEnglishEG = 0;
		this._sumMathsEG = 0;
		this._sumSwedishEG = 0;
		this._sumSwedish2EG = 0;
		//_sumTotalEG = 0;

		this._sumEnglishG = 0;
		this._sumMathsG = 0;
		this._sumSwedishG = 0;
		this._sumSwedish2G = 0;
		//_sumTotalG = 0;

		this._sumEnglishVG = 0;
		this._sumMathsVG = 0;
		this._sumSwedishVG = 0;
		this._sumSwedish2VG = 0;
		//_sumTotalVG = 0;

		this._sumEnglishMVG = 0;
		this._sumMathsMVG = 0;
		this._sumSwedishMVG = 0;
		this._sumSwedish2MVG = 0;
		//_sumTotalMVG = 0;

	}
	
	public void sumMeriteValue(String value) {
		System.out.println("SUMMED MERITED THE REAL VALUE:"+value);
		try {
			if(Float.parseFloat(value) != 0) {
				_tallyMeriteValue++;
			}
			_sumMeriteValue += Float.parseFloat(value);
			System.out.println("SUMMED MERITED VALUE:"+_sumMeriteValue);
		} catch (NumberFormatException e) {}
	}

	public void sumMarksPoints(String value) {
		try {
			if(Float.parseFloat(value) != 0) {
				_tallyMarksPoints++;
			}
			_sumMarksPoints += Float.parseFloat(value);
		} catch (NumberFormatException e) {}
	}

	public void sumAuthPoints(String value) {
		try {
			if(Float.parseFloat(value) != 0) {
				_tallyAuthPoints++;
			}
			_sumAuthPoints += Float.parseFloat(value);
		} catch (NumberFormatException e) {}
	}

	public void sumMarksNumberShare(String value) {
		try {
			if(Float.parseFloat(value) != 0) {
				_tallyMarksNumberShare++;
			}
			_sumMarksNumberShare += Float.parseFloat(value);
		} catch (NumberFormatException e) {}
	}

	public void sumMarksFailed(String value) {
		try {
			if(Float.parseFloat(value) != 0) {
				_tallyMarksFailed++;
			}
			_sumMarksFailed += Float.parseFloat(value);
		} catch (NumberFormatException e) {}
	}

	public void sumMarksNoGoal(String value) {
		try {
			if(Float.parseFloat(value) != 0) {
				_tallyMarksNoGoal++;
			} 
			_sumMarksNoGoal += Float.parseFloat(value);
		} catch (NumberFormatException e) {}
	}

	public String getSumMeriteValue() {
		return ""+_sumMeriteValue;
	}

	public String getSumMarksPoints() {
		return "" + _sumMarksPoints;
	}

	public String getSumAuthPoints() {
		return "" + _sumAuthPoints;
	}

	public String getSumMarksNumberShare() {
		return "" + _sumMarksNumberShare;
	}

	public String getSumMarksFailed() {
		return "" + _sumMarksFailed;
	}

	public String getSumMarksNoGoal(){
		return "" + _sumMarksNoGoal;
	}

	public String getCommuneMeanMeriteValue() {
		if (_tallyMeriteValue != 0) {
			return ""+(_sumMeriteValue / _tallyMeriteValue);
		} else {
			return "0";
		}
	}

	public String getCommuneMeanMarksPoints() {
		if (_tallyMarksPoints != 0) {
			return ""+(_sumMarksPoints / _tallyMarksPoints);
		} else {
			return "0";
		}
	}

	public String getCommuneMeanAuthPoints() {
		if (_tallyAuthPoints != 0) {
			return ""+(_sumAuthPoints / _tallyAuthPoints);
		} else {
			return "0";
		}
	}

	public String getCommuneMeanMarksNumberShare() {
		if (_tallyMarksNumberShare != 0) {
			return ""+(_sumMarksNumberShare / _tallyMarksNumberShare);
		} else {
			return "0";
		}
	}
	public String getCommuneMeanMarksFailed() {
		if (_tallyMarksFailed != 0) {
			return ""+(_sumMarksFailed / _tallyMarksFailed);
		} else {
			return "0";
		}
	}
	public String getCommuneMeanMarksNoGoal() {
		if (_tallyMarksNoGoal != 0) {
			return ""+(_sumMarksNoGoal / _tallyMarksNoGoal);
		} else {
			return "0";
		}
	}
	
}
