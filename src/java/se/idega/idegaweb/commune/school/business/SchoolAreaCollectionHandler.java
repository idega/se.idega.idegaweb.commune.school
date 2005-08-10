/*
 * $Id: SchoolAreaCollectionHandler.java,v 1.3 2005/08/10 15:14:57 thomas Exp $
 * Created on May 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.remotescripting.RemoteScriptCollection;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.remotescripting.RemoteScriptingResults;


/**
 * Last modified: $Date: 2005/08/10 15:14:57 $ by $Author: thomas $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class SchoolAreaCollectionHandler implements RemoteScriptCollection {
	
	public static final String PARAMETER_SCHOOL_YEAR = "sb_school_year";
	
	public static final String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.school";

	/* (non-Javadoc)
	 * @see com.idega.presentation.remotescripting.RemoteScriptCollection#getResults(com.idega.presentation.IWContext)
	 */
	public RemoteScriptingResults getResults(IWContext iwc) {
		String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);

		String sourceID = iwc.getParameter(sourceName);

	  return handleCourseUpdate(iwc, sourceName, sourceID);
	}
	
	private RemoteScriptingResults handleCourseUpdate(IWContext iwc, String sourceName, String sourceID) {
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
    Collection ids = new ArrayList();
		ids.add("-1");
    Collection names = new ArrayList();
		names.add(iwrb.getLocalizedString("select_school","Select school"));
    
		Object yearPK = iwc.getParameter(PARAMETER_SCHOOL_YEAR);
		
		try {
			Collection courses = getBusiness(iwc).getSchoolHome().findAllByAreaAndTypesAndYear(Integer.parseInt(sourceID), getBusiness(iwc).getSchoolTypesForCategory(getBusiness(iwc).getCategoryElementarySchool(), false), Integer.parseInt(yearPK.toString()));
	    Iterator iter = courses.iterator();
	    while (iter.hasNext()) {
	    		School school = (School) iter.next();
	    		ids.add(school.getPrimaryKey().toString());
	    		names.add(school.getSchoolName());
	    }
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
		
    RemoteScriptingResults rsr = new RemoteScriptingResults(RemoteScriptHandler.getLayerName(sourceName, "id"), ids);
    rsr.addLayer(RemoteScriptHandler.getLayerName(sourceName, "name"), names);

    return rsr;
	}
	
	private SchoolBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}