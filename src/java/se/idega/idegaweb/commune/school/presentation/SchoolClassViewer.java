/*
 * Created on Dec 11, 2003
 */
package se.idega.idegaweb.commune.school.presentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import se.idega.idegaweb.commune.school.event.SchoolEventListener;

import com.idega.block.school.business.SchoolYearComparator;
import com.idega.block.school.data.SchoolYear;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;

/**
 * @author laddi
 */
public class SchoolClassViewer extends SchoolCommuneBlock {

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.presentation.SchoolCommuneBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		Form form = new Form();
		form.setEventListener(SchoolEventListener.class);
		if (getResponsePage() != null) {
			form.setPageToSubmitTo(getResponsePage());
		}

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		form.add(table);

		int row = 1;
		
		List schoolYears = null;
		try {
			schoolYears = new ArrayList(getSession().getSchool().findRelatedSchoolYears());
		}
		catch (IDORelationshipException e1) {
			schoolYears = new ArrayList();
		}
		if (!schoolYears.isEmpty())
			Collections.sort(schoolYears, new SchoolYearComparator());
		
		table.add(getSmallHeader(localize("school_years", "Years") + ":"), 1, row);
		table.setNoWrap(1, row);
		
		Iterator iter = schoolYears.iterator();
		while (iter.hasNext()) {
			SchoolYear year = (SchoolYear) iter.next();
			CheckBox box = getCheckBox(getSession().getParameterSchoolGroupIDs(), year.getPrimaryKey().toString());
			
			table.setCellpadding(1, row, 2);
			table.add(box, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(getSmallText(year.getSchoolYearName()), 1, row++);
		}
		table.setHeight(row++, 15);
		
		SubmitButton button = (SubmitButton) getButton(new SubmitButton(localize("school.continue", "Continue")));
		button.setToEnableWhenChecked(getSession().getParameterSchoolGroupIDs());
		
		if (getBackPage() != null) {
			GenericButton back = getButton(new GenericButton("back", localize("back", "Back")));
			back.setPageToOpen(getBackPage());
			
			table.add(back, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
		}
		table.add(button, 1, row++);

		add(form);
	}
}