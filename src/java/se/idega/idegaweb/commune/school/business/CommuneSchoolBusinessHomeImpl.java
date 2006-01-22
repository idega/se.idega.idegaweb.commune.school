/*
 * $Id: CommuneSchoolBusinessHomeImpl.java,v 1.7 2006/01/22 16:25:11 laddi Exp $
 * Created on Jan 22, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO laddi Describe Type CommuneSchoolBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2006/01/22 16:25:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public class CommuneSchoolBusinessHomeImpl extends IBOHomeImpl implements CommuneSchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return CommuneSchoolBusiness.class;
	}

	public CommuneSchoolBusiness create() throws javax.ejb.CreateException {
		return (CommuneSchoolBusiness) super.createIBO();
	}
}
