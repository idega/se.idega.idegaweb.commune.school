/*
 * Created on Dec 11, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;

import com.idega.block.school.data.SchoolClass;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolClassViewer extends SchoolCommuneBlock {

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.presentation.SchoolCommuneBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		// TODO Auto-generated method stub
		_business = getSchoolCommuneBusiness(iwc);
		//_columnsToShow = new ArrayList();
		getViewForClasses();
	}
	
	private Table getViewForClasses() {
		Table result = new Table();
		try {
			Collection schoolClasses = _business.getSchoolBusiness().findSchoolClassesBySchoolAndYear(_schoolId, _schoolYearId);
			Iterator scIter = schoolClasses.iterator();
			while(scIter.hasNext()) {
				SchoolClass schoolClass = (SchoolClass) scIter.next();
				add(schoolClass.getSchoolClassName());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) throws RemoteException {
		return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);	
	}
	
	private SchoolCommuneBusiness _business;
	private int _schoolId = -1;
	private int _schoolYearId = -1;
	
	//private List _columnsToShow;
}




















