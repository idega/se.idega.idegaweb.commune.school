/*
 * $Id: CommuneSchoolBusinessHomeImpl.java,v 1.1 2005/08/09 16:36:28 laddi Exp $
 * Created on Aug 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: $Date: 2005/08/09 16:36:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class CommuneSchoolBusinessHomeImpl extends IBOHomeImpl implements CommuneSchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return CommuneSchoolBusiness.class;
	}

	public CommuneSchoolBusiness create() throws javax.ejb.CreateException {
		return (CommuneSchoolBusiness) super.createIBO();
	}
}