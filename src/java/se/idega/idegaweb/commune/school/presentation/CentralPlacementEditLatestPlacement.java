/*
 * Created on 2003-sep-23
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author 
 * @author <br><a href="mailto:gobom@wmdata.com">G�ran Borgman</a><br>
 * Last modified: $Date: 2006/04/09 11:39:53 $ by $Author: laddi $
 * @version $Revision: 1.4 $
 */
public class CentralPlacementEditLatestPlacement extends CommuneBlock {
	
	// *** Localization keys ***
	private static final String KP = "central_placement_floating_windows.";
	private static final String KEY_WINDOW_HEADING = KP + "edit_latest_heading";
	
	// *** Params ***
	public static final String PARAM_ACTION = "param_action";
	public static final String PARAM_PLACEMENT_PARAGRAPH = "param_placement_paragraph";
	public static final String PARAM_LATEST_PLACEMENT_ID = "param_latest_placement_id";
	
	public static final int ACTION_SAVE = 1;
	public static final int ACTION_CANCEL = 2;

	// Instance variables
	private IWResourceBundle iwrb;
	private Table mainTable;
	private int mainTableRow;
	private SchoolClassMember latestPl;
	private int _action = -1;
	private String paragraphTxt;

	public void main(IWContext iwc) throws Exception {
		this.iwrb = getResourceBundle(iwc);
		parse(iwc);
		this.latestPl = getPlacement(iwc);
		if (this._action == 1) { //ACTION_SAVE) {
			save();			
		}
		Form form = new Form();
		form.maintainParameter(PARAM_LATEST_PLACEMENT_ID);
		this.mainTable = getMainTable();		
		setMainTableContent(getEditTable());
		form.add(this.mainTable);
		add(form);
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
		this.mainTable.add(getLocalizedSmallHeader(KEY_WINDOW_HEADING, "Edit Latest Placement"), col, this.mainTableRow);
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

	public Table getEditTable() {
		Table table = new Table();
		table.setBorder(1);
		table.setWidthAndHeightToHundredPercent();
		table.setRowHeight(1, "10");

		table.setWidth(1, 1, "10");
		table.setWidth(2, 1, "110");
		table.setWidth(4, 1, "10");
		table.setBorder(0);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setVerticalAlignment(2, 2, Table.VERTICAL_ALIGN_TOP);
		
		table.add(new HiddenInput(PARAM_ACTION, "1"), 1, 1);
		
		int row = 2;
		int col = 2;
		
		table.add(
				getSmallHeader(localize(CentralPlacementEditorConstants.KEY_PLACEMENT_PARAGRAPH_LABEL, "Placement paragraph: ")),
				col++, row);
		table.add(getPlacementParagraphTextInput(), col, row);
		row++;
		table.setRowHeight(row, "20");
		row++;
		SubmitButton save =  new SubmitButton(this.iwrb.getLocalizedImageButton("central_placement_editor.button_save",
													"Save"));
		save.setValueOnClick(PARAM_ACTION, String.valueOf(ACTION_SAVE));
		table.add(save, 2, row);
		
		table.add(Text.getNonBrakingSpace(2), 2, row);
		
		SubmitButton cancel =  new SubmitButton(this.iwrb.getLocalizedImageButton("central_placement_editor.button_cancel",
						"Cancel"));
		cancel.setValueOnClick(PARAM_ACTION, String.valueOf(ACTION_CANCEL));
		table.add(cancel, 2, row);

		table.add(Text.getNonBrakingSpace(2), 2, row);
		
		//CloseButton close = (CloseButton) getStyledInterface(new CloseButton(localize("close_window", "Close")));
		CloseButton close = new CloseButton(this.iwrb.getLocalizedImageButton("close_window", "Close"));
		
		table.add(close, 2, row);
		
		// Last empty row
		row++;
		col = 2;
		table.add(Text.getNonBrakingSpace(), col, row);
		table.setRowHeight(table.getRows(), "100%");
		
		return table;
	}
	
	private TextInput getPlacementParagraphTextInput() {
		TextInput txt = (TextInput) getStyledInterface(new TextInput(PARAM_PLACEMENT_PARAGRAPH));
		txt.setLength(25);
		if (this.latestPl != null && this.latestPl.getPlacementParagraph() != null) {
			txt.setContent(this.latestPl.getPlacementParagraph());
		}
		return txt;
	}
	
	private void parse(IWContext iwc) {
		this._action = iwc.isParameterSet(PARAM_ACTION) ? Integer.parseInt(iwc.getParameter(PARAM_ACTION)) : -1;
		this.paragraphTxt = iwc.isParameterSet(PARAM_PLACEMENT_PARAGRAPH) ? iwc.getParameter(PARAM_PLACEMENT_PARAGRAPH) : null;
	}
	
	private void save() {
		if (this.latestPl != null && this.paragraphTxt != null) {
			this.latestPl.setPlacementParagraph(this.paragraphTxt);
			this.latestPl.store();
		}
	}
	
	private SchoolClassMember getPlacement(IWContext iwc) {
		SchoolClassMember plc = null;
		if (iwc.isParameterSet(PARAM_LATEST_PLACEMENT_ID)) {
			String idStr = iwc.getParameter(PARAM_LATEST_PLACEMENT_ID);
			
			try {
				plc = getSchoolBusiness(iwc).getSchoolClassMemberHome().findByPrimaryKey(new Integer(idStr));
			} catch (Exception e) {}
		}
		return plc;
	}
	
	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}	
}
