package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.message.presentation.PrintMessageViewer;
import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;

/**
 * Title: EventList
 * Description: A class to view and manage PrintedLetterMessage in the idegaWeb Commune application
 * Copyright:    Copyright idega Software (c) 2002
 * Company:	idega Software
 * @author <a href="mailto:roar@idega.is">roar</a>
 * @version $Id: EventList.java,v 1.18 2004/02/16 17:33:26 aron Exp $
 * @since 17.3.2003 
 */
// refactored by aron 16.04.2004

public class EventList extends PrintMessageViewer {

	private SchoolCommuneSession getSchoolSession(IWContext iwc)
		throws RemoteException {
		return (SchoolCommuneSession) IBOLookup.getSessionInstance(
			iwc,
			SchoolCommuneSession.class);
	}
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.message.presentation.PrintMessageViewer#getLetters(com.idega.presentation.IWContext)
	 */
	protected Collection getLetters(IWContext iwc) throws FinderException {
		try {
			int schoolID = getSchoolSession(iwc).getSchoolID();
			return getPrintedLetter().
			findAllLettersBySchool(
						schoolID,
						getSearchSsn(),
						getSearchMsgId(),
						getUFrom(),
						getUTo());
		}
		catch (RemoteException e) {
			throw new FinderException(e.getMessage());
		}
		
		
	}

}
