package se.idega.idegaweb.commune.school.presentation;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;

/**
 * @author borgman
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolChoiceFormMessageWindow extends Window {

	private static final String IW_BUNDLE_NAME = "se.idega.idegaweb.commune";

	private static final String KP = SchoolChoiceFormMessage.KP;
	private static final String KEY_BUTTON_CLOSE = KP + "button_close";
	private static final String PARAM_CLOSE = "param_close";

	Table mainTable = null;
	IWResourceBundle iwrb = null;
	 
	
	public SchoolChoiceFormMessageWindow() {
		this.setWidth(400);
		this.setHeight(200);
		this.setScrollbar(true);
		this.setResizable(true);	
		this.setAllMargins(0);
	}

	/*
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_NAME;
	}

	private Table getMainTable() {
		Table mainTable = new Table();
		mainTable.setColumns(3);
		mainTable.setRows(2);
		mainTable.setWidthAndHeightToHundredPercent();
		mainTable.setBorder(0);
		mainTable.setCellpadding(0);
		mainTable.setCellspacing(0);
		mainTable.mergeCells(1, 1, 3, 1);
		
		// Add close button on row 2
		Image buttonImg = iwrb.getLocalizedImageButton(KEY_BUTTON_CLOSE, "Close");
		SubmitButton button = new SubmitButton(buttonImg);
		Form closeForm = new Form();
		closeForm.add(button);
		closeForm.add(new HiddenInput(PARAM_CLOSE, "true"));
		mainTable.add(closeForm, 2, 2);
		mainTable.setRowHeight(2, "25");
		mainTable.setVerticalAlignment(2, 2, Table.VERTICAL_ALIGN_TOP);
		mainTable.setWidth(1, 2, "30");
		mainTable.setWidth(3, 2, "100%");

		return mainTable;
	}
	
	private void setMainTableContent(PresentationObject obj) {
		int col = 1;
		int row = 1;
		mainTable.add(obj, col, row);
	}
	
	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		if (iwc.isParameterSet(PARAM_CLOSE))
			close();
		iwrb = getResourceBundle(iwc);
		mainTable = getMainTable();
		setMainTableContent(new SchoolChoiceFormMessage());
		add(mainTable);
	}
	
}
