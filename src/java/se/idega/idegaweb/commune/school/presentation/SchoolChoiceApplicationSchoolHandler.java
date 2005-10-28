/*
 * $Id: SchoolChoiceApplicationSchoolHandler.java,v 1.2 2005/10/28 17:30:51 gimmi Exp $
 * Created on Oct 28, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
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


public class SchoolChoiceApplicationSchoolHandler implements RemoteScriptCollection {

	public RemoteScriptingResults getResults(IWContext iwc) {
		String schoolType = iwc.getParameter("sch_app_cho_typ");
		String schoolYear = iwc.getParameter("sch_app_year");
		String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);
		String areaID = iwc.getParameter(sourceName);

		Vector ids = new Vector();
		Vector names = new Vector();
		ids.add("-1");
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		names.add(iwrb.getLocalizedString("choose_school", "Choose School"));
		
		Collection coll;
		try {
			coll = getBusiness(iwc).findAllSchoolsByAreaAndTypeAndYear(Integer.parseInt(areaID), Integer.parseInt(schoolType), Integer.parseInt(schoolYear));
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				School school = (School) iter.next();
				if (!school.getInvisibleForCitizen()) {
					ids.add(school.getPrimaryKey().toString());
					names.add(school.getSchoolName());
				}
			}
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
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
