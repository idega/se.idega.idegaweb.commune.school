package se.idega.idegaweb.commune.school.presentation;

//import com.idega.business.IBOLookup;
import com.idega.presentation.*;
//import com.idega.presentation.text.*;
//import com.idega.presentation.ui.*;
//import javax.ejb.*;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * SchoolChoiceReminderView is an IdegaWeb block that registers and handles
 * reminder broadcasts to citizens who should make a school choice. It is based
 * on session ejb classes in {@link se.idega.idegaweb.commune.school.business}
 * and entity ejb classes in {@link se.idega.idegaweb.commune.school.data}.
 * <p>
 * <p>
 * Last modified: $Date: 2002/12/11 09:05:03 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 * @see javax.ejb
 */
public class SchoolChoiceReminderView extends CommuneBlock {
	/**
	 * @param iwc session data like user info etc.
	 */
	public void main(final IWContext iwc) {
		setResourceBundle (getResourceBundle(iwc));
    }

	private String getLocalizedString(final String key, final String value) {
		return getResourceBundle().getLocalizedString(key, value);
	}
}
