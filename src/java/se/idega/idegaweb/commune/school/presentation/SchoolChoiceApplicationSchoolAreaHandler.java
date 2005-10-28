/*
 * $Id: SchoolChoiceApplicationSchoolAreaHandler.java,v 1.1 2005/10/28 14:54:40 gimmi Exp $
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
import com.idega.block.school.data.SchoolArea;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.remotescripting.RemoteScriptCollection;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.remotescripting.RemoteScriptingResults;


public class SchoolChoiceApplicationSchoolAreaHandler implements RemoteScriptCollection {

	public RemoteScriptingResults getResults(IWContext iwc) {
		String schoolType = iwc.getParameter("sch_app_cho_typ");
		String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);

		//String sourceID = iwc.getParameter(sourceName); no use found yet for the year dropdown
		
		Vector ids = new Vector();
		Vector names = new Vector();
		ids.add("-1");
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		names.add(iwrb.getLocalizedString("choose_area", "Choose Area"));
		try {
			Collection areas = getBusiness(iwc).findAllSchoolAreasByType(Integer.parseInt(schoolType));
			Iterator iter = areas.iterator();
			while (iter.hasNext()) {
				SchoolArea schoolArea = (SchoolArea) iter.next();
				ids.add(schoolArea.getPrimaryKey().toString());
				names.add(schoolArea.getName());
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
