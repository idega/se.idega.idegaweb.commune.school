/*
 * $Id: SchoolCommuneBusinessHomeImpl.java 1.1 Oct 21, 2004 thomas Exp $
 * Created on Oct 21, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public class SchoolCommuneBusinessHomeImpl extends IBOHomeImpl implements SchoolCommuneBusinessHome {

	protected Class getBeanInterfaceClass() {
		return SchoolCommuneBusiness.class;
	}

	public SchoolCommuneBusiness create() throws javax.ejb.CreateException {
		return (SchoolCommuneBusiness) super.createIBO();
	}
}
