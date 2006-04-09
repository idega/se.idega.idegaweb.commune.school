/*
 * Created on 2003-sep-23
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation;

import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;

/**
 * @author 
 * @author <br><a href="mailto:gobom@wmdata.com">Göran Borgman</a><br>
 * Last modified: $Date: 2006/04/09 11:39:53 $ by $Author: laddi $
 * @version $Revision: 1.4 $
 */
public class SchoolChoiceFormMessage extends CommuneBlock {
	// *** Localization keys ***
	public static final String KP = "school_choice_form_message.";
	private static final String KEY_WINDOW_HEADING = KP + "window_heading";
	private static final String KEY_MESSAGE = KP + "message";

	// Instance variables
	private Table mainTable;
	private int mainTableRow;

	public void main(IWContext iwc) throws Exception {
		this.mainTable = getMainTable();		
		setMainTableContent(getMessageTable());
		add(this.mainTable);
	}

	private Table getMainTable() {
		this.mainTable = new Table();
		this.mainTable.setBorder(0);
		this.mainTable.setWidthAndHeightToHundredPercent();
		this.mainTable.setCellpadding(0);
		this.mainTable.setCellspacing(0);
		int col = 1;
		this.mainTableRow = 1;
		
		//  *** WINDOW HEADING ***
		this.mainTable.add(
			getLocalizedSmallHeader(KEY_WINDOW_HEADING, "School choice message"), 
																														col, this.mainTableRow);
		this.mainTable.setColor(col, this.mainTableRow, getHeaderColor());
		this.mainTable.setAlignment(col, this.mainTableRow, Table.HORIZONTAL_ALIGN_CENTER);
		this.mainTable.setRowVerticalAlignment(this.mainTableRow, Table.VERTICAL_ALIGN_MIDDLE);
		this.mainTable.setRowHeight(this.mainTableRow++, "20");

		return this.mainTable;
	}

	private void setMainTableContent(PresentationObject obj) {
		int col = 1;
		this.mainTable.add(obj, col, this.mainTableRow++);
	}

	public Table getMessageTable() {
		Table table = new Table(3, 3);
		table.setWidthAndHeightToHundredPercent();
		table.setRowHeight(1, "10");
		table.setRowHeight(3, "10");
		table.setWidth(1, 1, "10");
		table.setWidth(3, 1, "10");
		table.setBorder(0);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setVerticalAlignment(2, 2, Table.VERTICAL_ALIGN_TOP);	
		table.add(getSmallText(localize(KEY_MESSAGE, "Localized School choice message ... ")), 2, 2);		
		return table;
	}
	
}
