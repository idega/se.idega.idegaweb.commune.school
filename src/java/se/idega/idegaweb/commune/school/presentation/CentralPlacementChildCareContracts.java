package se.idega.idegaweb.commune.school.presentation;

import se.idega.idegaweb.commune.care.presentation.ChildContracts;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
import com.idega.repository.data.ImplementorRepository;

/**
 * @author borgman
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class CentralPlacementChildCareContracts extends Window {

	private static final String IW_BUNDLE_NAME = "se.idega.idegaweb.commune";

	// *** Localization keys ***
	private static final String KP = "central_placement_floating_windows.";
	private static final String KEY_WINDOW_HEADING_1 = KP + "edit_window_opened_from";
	private static final String KEY_WINDOW_HEADING_2 = KP + "CENTRAL_PLACEMENT_EDITOR";
	private static final String KEY_WINDOW_HEADING_RELOAD_MSG = KP + "close_reload_msg";
	private static final String KEY_BUTTON_CLOSE_AND_RELOAD = KP + "button.close_and_reload";
	private static final String KEY_HEADING_CC_CONTRACTS = KP + "heading_child_care_contracts";

	private static final String PARAM_CLOSE_AND_RELOAD = "close_and_reload";

	Table mainTable = null;
	Table innerTable = null;
	String FRAME_COLOR = "#DDDDDD";
	String INNER_HEADING_BGCOLOR = "#EEEEEE";
	IWResourceBundle iwrb = null;
	 
	// CSS styles   
	private static final String STYLE_BIG_HEADER =
		"font-style:normal;text-decoration:none;color:#000000;"
			+ "font-size:12px;font-family:Verdana,Arial,Helvetica;font-weight:bold;";
	private static final String STYLE_SMALL_HEADER =
		"font-style:normal;text-decoration:none;color:#000000;"
			+ "font-size:10px;font-family:Verdana,Arial,Helvetica;font-weight:bold;";
			
	private static final String STYLE_SMALL_TEXT =
		"font-style:normal;text-decoration:none;color:#000000;"
			+ "font-size:10px;font-family:Verdana,Arial,Helvetica;font-weight:normal;";
	

	// Paths
	private static final String PATH_TRANS_GIF =
		"/idegaweb/bundles/com.idega.core.bundle/resources/transparentcell.gif";

	private Image transGIF = new Image(PATH_TRANS_GIF);
		
	public CentralPlacementChildCareContracts() {
		this.setWidth(640);
		this.setHeight(400);
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
		mainTable.setWidth(Table.HUNDRED_PERCENT);
		mainTable.setHeight(Table.HUNDRED_PERCENT);
		mainTable.setColumns(4);
		mainTable.setRows(3);
		mainTable.setBorder(0);
		mainTable.setCellpadding(2);
		mainTable.setCellspacing(0);
		int col = 1;
		
		// row 1
		mainTable.setWidth(col, 1, "2px");
		mainTable.setWidth(col+3, 1, "2px");
		mainTable.setRowColor(1, FRAME_COLOR);
		mainTable.setAlignment(col+1, 1, Table.HORIZONTAL_ALIGN_CENTER);
		mainTable.setVerticalAlignment(col+1, 1, Table.VERTICAL_ALIGN_MIDDLE);
		mainTable.setRowHeight(1, "20");
		// row 2
		mainTable.setColor(col, 2, FRAME_COLOR);
		mainTable.mergeCells(col+1, 2, col+2, 2);
		mainTable.setColor(col+3, 2, FRAME_COLOR);
		// row 3		
		mainTable.setRowColor(3, FRAME_COLOR);
		mainTable.setRowHeight(3, "2px");

		//  *** WINDOW HEADING ***
		Text heading1 = new Text(iwrb.getLocalizedString(KEY_WINDOW_HEADING_1, 
												"edit window opened from"));
		Text heading2 = new Text(iwrb.getLocalizedString(KEY_WINDOW_HEADING_2, 
												"Central Placement Editor"));
		Text reloadMsg = new Text(iwrb.getLocalizedString(KEY_WINDOW_HEADING_RELOAD_MSG, 
												"Press to close this window and reload Central Placement Editor"));
									
		heading1.setFontStyle(STYLE_SMALL_TEXT);
		heading2.setFontStyle(STYLE_BIG_HEADER);
		reloadMsg.setFontStyle(STYLE_SMALL_TEXT);
		
		mainTable.add(heading1, col+1, 1);
		mainTable.add(Text.getBreak(), col+1, 1);
		mainTable.add(heading2, col+1, 1);
		
		Image buttonImg = iwrb.getLocalizedImageButton(KEY_BUTTON_CLOSE_AND_RELOAD, 
																			"Close and reload");
		SubmitButton button = new SubmitButton(buttonImg);
		Form closeAndReloadForm = new Form();
		closeAndReloadForm.add(button);
		closeAndReloadForm.add(new HiddenInput(PARAM_CLOSE_AND_RELOAD, "true"));
		mainTable.add(closeAndReloadForm, col+2, 1);

		return mainTable;
	}
	
	private void setMainTableContent(PresentationObject obj) {
		int col = 2;
		int row = 2;
		mainTable.add(obj, col, row);
	}
	
	private Table getInnerTable() {
		innerTable = new Table(1, 4);
		innerTable.setBorder(0);
		innerTable.setWidth(Table.HUNDRED_PERCENT);
		innerTable.setHeight(Table.HUNDRED_PERCENT);
		innerTable.setCellpadding(2);
		innerTable.setCellspacing(0);
		int col = 1;
		int row = 1;
		
		//  *** TABLE HEADING ***
		Text heading = new Text(iwrb.getLocalizedString(KEY_HEADING_CC_CONTRACTS, 
																				 "Childcare child contracts"));
		heading.setFontStyle(STYLE_SMALL_HEADER);		

		innerTable.add(heading, col, row);
		innerTable.setColor(col, row, INNER_HEADING_BGCOLOR);
		innerTable.setAlignment(col, row, Table.HORIZONTAL_ALIGN_CENTER);
		innerTable.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_MIDDLE);
		innerTable.setRowHeight(row++, "18");
		
		// empty space row
		innerTable.add(transGIF, col, row);
		innerTable.setRowHeight(row, "20");
		
		// *** ROW 3 WILL CARRY THE CONTENT
		
		// bottom row to fill up empty space at the bottom
		innerTable.add(transGIF, col, innerTable.getRows());
		innerTable.setRowHeight(4, Table.HUNDRED_PERCENT);

		return innerTable;
	}

	private void setInnerTableContent(PresentationObject obj) {
		int col = 1;
		innerTable.add(obj, col, 3);
	}


	/**
	 * Reload ParentPage and close this window
	 */
	private void reloadParentAndClose() {
		setParentPageFormToSubmitOnUnLoad(CentralPlacementEditorConstants.FORM_NAME);
		close();
	}

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		iwrb = getResourceBundle(iwc);
		if (iwc.isParameterSet(PARAM_CLOSE_AND_RELOAD)) {
			reloadParentAndClose();
		} else {
			mainTable = getMainTable();
			innerTable = getInnerTable();
			ChildContracts childContracts = (ChildContracts) ImplementorRepository.getInstance().newInstanceOrNull(ChildContracts.class, this.getClass());
			if (childContracts != null) {
				PresentationObject childContractsPresentation = childContracts.getPresentation();
				setInnerTableContent(childContractsPresentation);
			}
			setMainTableContent(innerTable);
			add(mainTable);
		}
	}
	
}
