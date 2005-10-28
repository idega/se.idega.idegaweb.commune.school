package se.idega.idegaweb.commune.school.presentation;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoCustodianFound;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.FinderException;
import javax.faces.component.UIComponent;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.care.presentation.ChildContracts;
import se.idega.idegaweb.commune.presentation.CitizenChildren;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.util.PIDChecker;
import com.idega.block.navigation.presentation.UserHomeLink;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolCategoryBMPBean;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolYear;
import com.idega.block.school.presentation.SchoolYearSelectorCollectionHandler;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.core.localisation.data.ICLanguage;
import com.idega.core.localisation.data.ICLanguageHome;
import com.idega.core.location.data.Address;
import com.idega.data.IDOEntity;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.user.presentation.UserSearcher;
import com.idega.util.Age;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: idega
 * </p>
 * 
 * @author <br>
 *         <a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * @version 1.0
 */

public class SchoolChoiceApplication extends CommuneBlock {

	IWBundle iwb;
	IWResourceBundle iwrb;
	UserBusiness userbuiz;
	Collection schoolTypes = null;
	List[] schools = null;
	int preTypeId = -1;
	int preAreaId = -1;
	DateFormat df;

	private String prefix = "sch_app_";
	private String prmSendCatalogue = prefix + "snd_cat";
	private String prmCustodiansAgree = prefix + "cus_agr";
	private String prmFirstSchool = prefix + "fst_scl";
	private String prmSecondSchool = prefix + "snd_scl";
	private String prmThirdSchool = prefix + "trd_scl";
	private String prmFirstArea = prefix + "fst_ara";
	private String prmSecondArea = prefix + "snd_ara";
	private String prmThirdArea = prefix + "trd_ara";
	private String prmPreSchool = prefix + "pre_scl";
	private String prmPreArea = prefix + "pre_ara";
	private String prmPreType = prefix + "pre_typ";
	private String prmType = prefix + "cho_typ";
	//private String prmPreGrade = prefix + "pre_grd";
	private String prmYear = prefix + "year";
	private String prmYearReload = prefix + "yearReload";
	private String prmPreYear = prefix + "pre_year";
	private String prmMessage = prefix + "msg";
	private String prmRealSubmit = prefix + "real_submit";
	//private String prmAutoAssign = prefix + "aut_ass";
	//private String prmSchoolChange = prefix + "scl_chg";
	private String prmSixYearCare = prefix + "six_car";
	private String prmLanguage = prefix + "cho_lng";
	private String prmAfterschool = prefix + "aft_schl";
	private String prmAction = prefix + "snd_frm";
	private String prmChildId = CitizenChildren.getChildIDParameterName();
	private String prmChildUniqueId = CitizenChildren.getChildUniqueIDParameterName();
	private String prmParentId = CitizenChildren.getParentIDParameterName();
	
	private String prmForm = prefix + "the_frm";
	//private String prmCaseOwner = prefix+"cse_own";
	private String prmNativeLangIsChecked = prefix + "native_lang_is_checked";
	private String prmNativeLang = prefix + "native_lang";
	private String prmExtraChoiceMessage = prefix + "choice_message";

	private boolean valSendCatalogue = false;
	private boolean valSixyearCare = false;
	private boolean valAutoAssign = false;
	private boolean valWantsAfterSchool = false;
	//private boolean valSchoolChange = false;
	private boolean valCustodiansAgree = false;
	private String valMessage = "";
	private String valLanguage = "";
	private int valFirstSchool = -1;
	private int valSecondSchool = -1;
	private int valThirdSchool = -1;
	//private int valPreGrade = -1;
	private int valYear = -1;
	private int valPreYear = -1;
	private int valPreSchool = -1;
	private int valType = -1;
	private int childId = -1;
	private int valCaseOwner = -1;
	private int valMethod = 1; // Citizen:1; Quick: 2; Automatic: 3
	private boolean valNativeLangIsChecked = false;
	private int valNativeLang = -1;
	private String[] valExtraChoiceMessages = new String[3];
	private boolean showAgree = false;
	protected boolean quickAdmin = false;
	//private boolean hasPermisson = false;
	
	SchoolChoiceBusiness schBuiz;
	SchoolClassMember schoolClassMember = null;
	SchoolClassMember schoolClassMemberNew = null;
	SchoolCommuneBusiness schCommBiz;
	CareBusiness careBuiz = null;
	SchoolClass schoolClass = null;
	SchoolClass schoolClassNew = null;
	School school = null;
	School schoolNew = null;
	SchoolYear schoolYear = null;
	SchoolArea schoolArea = null;
	SchoolType schoolType = null;
	SchoolSeason season = null;
	SchoolSeason seasonNew = null;

	
	Map schoolsByType = null;

	private boolean schoolChange = false;
	private boolean[] canApply = { false, false, false};
	private Age age;
	private Form myForm;

	private Integer afterSchoolPageID = null;
	private Integer checkPageID = null;

	private boolean isOwner = true;
	private User owner;
	private Date choiceDate;
	private boolean hasLanguageSelection = false;
	private boolean _useOngoingSeason = false;
	private String prmPlacementDate = "prm_placement_date";
	private java.sql.Date valPlacementDate = null;
	private boolean _isForTesting = false;
	private boolean _showChoiceMessage = false;
	private ICPage _outsideCommunePage=null;
	
	private boolean _forwardToCheckPage = true;
	private int _maxAge = 0;
	private boolean _useCheckBoxForAfterSchoolCare = false;
	private boolean _showChildCareTypes = false;
	
	
	//variables for admin user
	private boolean _useAsAdmin = false;
	private static int MAX_FOUND_USER_COLS = 20;
	private static int MAX_FOUND_USER_ROWS = 1;
	private static int MAX_FOUND_USERS = MAX_FOUND_USER_COLS * MAX_FOUND_USER_ROWS;
	private final String TOOMANYSTUDENTSFOUND_DEFAULT
	= "Too many hits. Try to constrain your search";
	private final String TOOMANYSTUDENTSFOUND_KEY = prefix + "tooManyStudentsFound";
	private final String NOUSERFOUND_KEY = prefix + "noUserFound";
	private final String NOUSERFOUND_DEFAULT
	= "No matches were found on given search criteria";
	//private String prmChildIdAdmin = CitizenChildren.getChildIDParameterName() + "_admin";
	private String _childId = null;
	
	
	
	
	/**
	 * @param ongoingSeason
	 */
	public void setUseOngoingSeason(boolean ongoingSeason) {
		_useOngoingSeason = ongoingSeason;
	}

	public void main(IWContext iwc) throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		df = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
		schBuiz = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
		careBuiz = (CareBusiness) IBOLookup.getServiceInstance(iwc, CareBusiness.class);
		canApply = checkCanApply(iwc);
		
		//userbuiz = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		//schCommBiz = (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);
		
		
			if (_useAsAdmin) {
				if (!iwc.isParameterSet(prmAction)) {
					add (createMainTable (getUserSearchFormTable (iwc)));
				}
				else{
						control(iwc);	
				}
			}
			else {
					control(iwc);
			}	
		
		
		
	}

	public boolean hasPermission(IWContext iwc) throws Exception {
		
		boolean hasPermission = false;
				
		if (isAdministrator(iwc) || _useAsAdmin){
			hasPermission = true;
		}
		else if (childId != -1){
		User parent = iwc.getCurrentUser();
		Collection children = getUserBusiness(iwc).getChildrenForUser(parent);
		Iterator theChild = children.iterator();
			while(theChild.hasNext()){
				User userChild = (User) theChild.next();
				
				int userChildId = ((Integer) userChild.getPrimaryKey()).intValue();
				if (userChildId == childId){
					hasPermission = true;
					break;
				}
								
			}
		}
		return hasPermission;
	}
	public void control(IWContext iwc) throws Exception {
		//debugParameters(iwc);
		childId = getChildId(iwc);
		
	/*	
		String ID = null;
		ID = iwc.getParameter(prmChildId);
		if (ID == null && _useAsAdmin){
			ID = _childId;	
			childId = Integer.parseInt(ID);
		}
		
		*/	
		
		
		if (iwc.isLoggedOn() && canApply[0] && hasPermission(iwc)) {
			if (childId != -1) {
				userbuiz = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
				schCommBiz = (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);
				boolean custodiansAgree = true;

				if (_useOngoingSeason) {
					try {
						season = careBuiz.getSchoolSeasonHome().findSeasonByDate(schCommBiz.getSchoolBusiness().getCategoryElementarySchool(), new IWTimestamp().getDate());
					}
					catch (FinderException e) {
						season = careBuiz.getCurrentSeason();
					}
				}
				else
					season = careBuiz.getCurrentSeason();

				User child = userbuiz.getUser(childId);
				if (child != null) {
					initSchoolChild(child);
					boolean hasChoosed = false;
					Collection currentChildChoices = null;
					parse(iwc);
					boolean saved = false;
					if (iwc.isParameterSet(prmAction) && iwc.getParameter(prmAction).equals("true") && 
							iwc.isParameterSet(prmRealSubmit) && iwc.getParameter(prmRealSubmit).equals("-1")) {

						///if a change of school is made when there is no placement that is ongoing, only placement for coming season
						if (schoolChange && valPreSchool == -1) {							
							try {
								//SchoolSeason season = null;
								seasonNew = careBuiz.getCurrentSeason();
								
								schoolClassMemberNew = schBuiz.getSchoolBusiness().getSchoolClassMemberHome().findLatestByUserAndSchCategoryAndSeason(child, schBuiz.getSchoolBusiness().getCategoryElementarySchool(), seasonNew);
								schoolClassNew = schoolClassMemberNew.getSchoolClass();
								//schoolNew = schoolClassNew.getSchool();
								valPreSchool = schoolClassNew.getSchoolId();
							}
							catch (FinderException e) {
								log(e);
							}
						}
						
						saved = saveSchoolChoice();
					}
					else {
						int seasonID = ((Integer) season.getPrimaryKey()).intValue();
						String[] statuses = { schBuiz.getCaseStatusDeleted().getStatus()};
						currentChildChoices = schBuiz.getSchoolChoiceHome().findByChildAndSeason(childId, seasonID, statuses);
						if (!currentChildChoices.isEmpty()) {
							Iterator iter = currentChildChoices.iterator();
							int count = 1;
							int size = currentChildChoices.size();
							if (size > 3) {
								valExtraChoiceMessages = new String[size];
							}
							while (iter.hasNext()) {
								SchoolChoice element = (SchoolChoice) iter.next();
								School school = element.getChosenSchool();
								int schoolID = ((Integer) school.getPrimaryKey()).intValue();
								valExtraChoiceMessages[count - 1] = element.getExtraChoiceMessage();

								if (count == 1) {
									valFirstSchool = schoolID;
									/*
									 * if (element.getFreetimeInThisSchool()) valSixyearCare =
									 * true; else if (element.getFreetimeOther() != null)
									 * valSixyearCare = true; else
									 */
									valSixyearCare = element.getKeepChildrenCare();
									if (element.getLanguageChoice() != null) valLanguage = element.getLanguageChoice();
									if (element.getMessage() != null) valMessage = element.getMessage();
									count++;
									owner = element.getOwner();
									choiceDate = element.getCreated();
									isOwner = ((IDOEntity) iwc.getCurrentUser()).equals(owner);
									custodiansAgree = element.getCustodiansAgree();
									if (valType == -1)
										valType = element.getSchoolTypeId();
									valYear = element.getSchoolYearID();
								}
								else if (count == 2) {
									valSecondSchool = schoolID;
									count++;
								}
								else {
									valThirdSchool = schoolID;
									count++;
								}
							}
						}
					}

//					if (valType == -1) {
//						if (hasPreviousSchool)
//							valType = 4;
//						else
//							valType = 5;
//					}

					// Application has been saved

					schoolTypes = getSchoolTypes(schBuiz.getSchoolBusiness().getElementarySchoolSchoolCategory());

					if (saved) {
						/*
						 * if (valSixyearCare && childcarePage != null) {
						 * iwc.setSessionAttribute(CitizenChildren.getChildIDParameterName(),
						 * new Integer(childId)); iwc.forwardToIBPage(getParentPage(),
						 * childcarePage); } else add(getSchoolChoiceAnswer(iwc, child));
						 */
						// User wants to choose
						if (valWantsAfterSchool) {
							boolean hasApprovedCheck = getCareBusiness(iwc).hasGrantedCheck(child);
							ChildContracts childContracts = (ChildContracts) ImplementorRepository.getInstance().newInstanceOrNull(ChildContracts.class, this.getClass());
							if (childContracts != null) {
								childContracts.storeChildInSession(((Integer) child.getPrimaryKey()).intValue(), iwc);
							}
							if (!_forwardToCheckPage) {
								hasApprovedCheck = true;
							}
							// forward to afterschool page
							if (hasApprovedCheck && afterSchoolPageID != null) {
								iwc.forwardToIBPage(getParentPage(), afterSchoolPageID.intValue());
							}
							// forward to check application page
							else if (checkPageID != null) {
								iwc.forwardToIBPage(getParentPage(), checkPageID.intValue());
							}
							add(getSchoolChoiceAnswer(child));
						}
						else {
							if (getResponsePage() != null) iwc.forwardToIBPage(getParentPage(), getResponsePage());
							add(getSchoolChoiceAnswer(child));
						}
					}
					else if (hasChoosed) {
						add(getAlreadyChosenAnswer(child));
					}
					else {
						if (custodiansAgree || isOwner || _useAsAdmin)
							add(getSchoolChoiceForm(iwc, child));
						else
							add(getLocalizedHeader("school.cannot_alter_choice", "You cannot alter the school choice already made."));
					}
				}
			}
			else{			
				add(getLocalizedHeader("school.no_student_id_provided", "No student provided"));
			}
		}
		else if (!iwc.isLoggedOn())
			add(getLocalizedHeader("school.need_to_be_logged_on", "You need to log in"));
		else if (!canApply[0]) add(getLocalizedHeader("school_choice.last_date_expired", "Time limits to apply expired"));
		else if (!hasPermission(iwc))
			add(getErrorText(localize("not_permitted", "You do not have permission")));
	}

	
	private int getChildId(IWContext iwc) {
		if (_childId != null){
			childId = Integer.parseInt(_childId);
			return childId;
		}
		else if (iwc.isParameterSet(prmChildUniqueId)){
			String childUniqueId = iwc.getParameter(prmChildUniqueId);
			User child = null;
			Object objChildId = null;
			if (childUniqueId != null){
				try {
					child = getUserBusiness(iwc).getUserByUniqueId(childUniqueId);	
				}
				catch (IBOLookupException ibe){
					log (ibe);
				}
				catch (FinderException fe){
					log (fe);
				}
				catch (RemoteException re){
					log (re);
				}
				
				if (child != null)
					objChildId = child.getPrimaryKey();
				
				if(objChildId != null)
				    return ((Integer) (objChildId)).intValue();
				else
				    return -1;
				
			}
			else {
				return -1;
			}
		}
		else {
			String childId = iwc.getParameter(prmChildId);
			if (childId != null) {
				iwc.setSessionAttribute(prmChildId, childId);
			}
			else {
				childId = (String) iwc.getSessionAttribute(prmChildId);
			}
			if(childId!=null)
			    return Integer.parseInt(childId);
			else
			    return -1;
		}
	}
	private boolean saveSchoolChoice() {
		try {
			//			schBuiz.createSchoolChoices(valCaseOwner, childId, valType,
			// valPreSchool, valFirstSchool, valSecondSchool, valThirdSchool,
			// valPreGrade, valMethod, -1, -1, valLanguage, valMessage,
			// schoolChange, valSixyearCare, valAutoAssign, valCustodiansAgree,
			// valSendCatalogue, valPlacementDate, season);
						
			schBuiz.createSchoolChoices(valCaseOwner, childId, valType, valPreSchool, valFirstSchool, valSecondSchool, valThirdSchool, valYear, valPreYear, valMethod, -1, -1, valLanguage, valMessage, schoolChange, valSixyearCare, valAutoAssign, valCustodiansAgree, valSendCatalogue, valPlacementDate, season, valNativeLangIsChecked, valNativeLang, valExtraChoiceMessages, _useAsAdmin);
			return true;
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private void parse(IWContext iwc) {
		valSendCatalogue = iwc.isParameterSet(prmSendCatalogue);
		valSixyearCare = iwc.isParameterSet(prmSixYearCare);
		if (iwc.isParameterSet(prmAfterschool)) {
			valWantsAfterSchool = iwc.getParameter(prmAfterschool).equalsIgnoreCase(Boolean.TRUE.toString());
		}

		valAutoAssign = true;
		//valSchoolChange = iwc.isParameterSet(prmSchoolChange);
		valCustodiansAgree = iwc.isParameterSet(prmFirstSchool) ? Boolean.valueOf(iwc.getParameter(prmCustodiansAgree)).booleanValue() : false;
		valMessage = iwc.getParameter(prmMessage);
		valLanguage = iwc.getParameter(prmLanguage);
		valPlacementDate = iwc.isParameterSet(prmPlacementDate) ? new IWTimestamp(iwc.getParameter(prmPlacementDate)).getDate() : null;
		valFirstSchool = iwc.isParameterSet(prmFirstSchool) ? Integer.parseInt(iwc.getParameter(prmFirstSchool)) : -1;
		valSecondSchool = iwc.isParameterSet(prmSecondSchool) ? Integer.parseInt(iwc.getParameter(prmSecondSchool)) : -1;
		valThirdSchool = iwc.isParameterSet(prmThirdSchool) ? Integer.parseInt(iwc.getParameter(prmThirdSchool)) : -1;
		valYear = iwc.isParameterSet(prmYear) ? Integer.parseInt(iwc.getParameter(prmYear)) : -1;
		valPreYear = iwc.isParameterSet(prmPreYear) ? Integer.parseInt(iwc.getParameter(prmPreYear)) : -1;
		valPreSchool = iwc.isParameterSet(prmPreSchool) ? Integer.parseInt(iwc.getParameter(prmPreSchool)) : -1;
		valType = iwc.isParameterSet(prmType) ? Integer.parseInt(iwc.getParameter(prmType)) : -1;
		valCaseOwner = iwc.isParameterSet(prmParentId) ? Integer.parseInt(iwc.getParameter(prmParentId)) : -1;
		valNativeLangIsChecked = iwc.isParameterSet(prmNativeLangIsChecked) ? true : false;
		valNativeLang = iwc.isParameterSet(prmNativeLang) ? Integer.parseInt(iwc.getParameter(prmNativeLang)) : -1;
		valExtraChoiceMessages = iwc.getParameterValues(prmExtraChoiceMessage) != null ? iwc.getParameterValues(prmExtraChoiceMessage) : new String[3];
		if (!quickAdmin) {
			valCaseOwner = iwc.getUserId();
		}
		else {
			valMethod = 2;
		}
	}

	public UIComponent getSchoolChoiceForm(IWContext iwc, User child) throws java.rmi.RemoteException {
		if (this.schoolChange) {
			if (!canApply[2]) return getSmallHeader(localize("school_choice.not_possible_to_change_school", "It is not possible to change school until after the 22nd of February."));
		}

		myForm = new Form();
		myForm.setName(prmForm);

		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setBorder(0);
		myForm.add(T);
		int row = 1;

		if (!isOwner) {
			T.add(getAlterChoiceInfo(iwc), 1, row++);
			T.setHeight(row++, 12);
		}

		T.add(getCurrentSchoolSeasonInfo(), 1, row++);
		//T.setHeight(row++, 12);
		T.add(getChildInfo(iwc, child), 1, row);
		T.mergeCells(1, row, 3, row);
		T.add(getCurrentSchool(), 2, row++);
		T.setHeight(row++, 12);
		
		T.add(getChoiceSchool(), 1, row++);
		T.setHeight(row++, 12);
		
		//add table with messagePart and submit button
		Table table = new Table();
		table.setWidth(300);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);		
		T.add(table, 1, row++);

		if (schoolChange)
			table.add(getSmallHeader(localize("school.application_change_required_reason", "Reason for schoolchange (Required)")), 1, 1);
		else
			table.add(getSmallHeader(localize("school.application_extra_info", "Extra info")), 1, 1);
		
		table.add(getMessagePart2(), 1, 2);
		table.setHeight(1, 3, 50);
		table.setVerticalAlignment(1, 3, Table.VERTICAL_ALIGN_BOTTOM);
		table.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_RIGHT);

		SubmitButton button = (SubmitButton) getButton(new SubmitButton("", localize("school_choice.ready", "Ready"))); 
		table.add(button, 1, 3);
		button.setOnClick("this.form.sch_app_real_submit.value==1;");
		button.setOnSubmitFunction("checkApplication", getSchoolCheckScript());
		
		myForm.setToDisableOnSubmit(button, true);
		
		////
		
		T.add(new HiddenInput(prmAction, "true"), 1, 1);
		T.add(new HiddenInput(prmYearReload, "-1"), 1, 1);
		T.add(new HiddenInput(prmRealSubmit, "-1"), 1, 1);
		T.add(new HiddenInput(prmChildId, child.getPrimaryKey().toString()), 1, 1);
		T.add(new HiddenInput(prmChildUniqueId, child.getUniqueId()), 1, 1);

		Page p = this.getParentPage();
		if (p != null) {
			Script S = p.getAssociatedScript();

			S.addVariable("sportOrMusicSchools", "new Array('2', '3', '4')");
			S.addFunction("getAlertIfSportsOrMusicSchool", getAlertIfSportsOrMusicSchool());

//			S.addFunction("initFilter", getInitFilterScript());
//			S.addFunction("setSelected", getSetSelectedScript());

//			S.addFunction("changeFilter", getFilterScript());
//			S.addFunction("changeFilter2", getFilterScript2(iwc));
			
//			S.addFunction("changeSchools", getSchoolYearScript());

			if (valType > 0) {
//				Script initScript = myForm.getAssociatedFormScript();
//				if (initScript == null) {
//					initScript = new Script();
//					myForm.setAssociatedFormScript(initScript);
//				}

//				String initFunction = getInitFilterCallerScript(prmType, prmFirstArea, prmFirstSchool, valType, valFirstArea, valFirstSchool, false, 1);
//				String initFunction2 = getInitFilterCallerScript(prmType, prmSecondArea, prmSecondSchool, valType, valSecondArea, valSecondSchool, false, 2);
//				String initFunction3 = getInitFilterCallerScript(prmType, prmThirdArea, prmThirdSchool, valType, valThirdArea, valThirdSchool, false, 3);

//				if (!schoolChange) {
//					initScript.addFunction("sch_init3", initFunction3);
//					getParentPage().setOnLoad(initFunction3);
//					initScript.addFunction("sch_init2", initFunction2);
//					getParentPage().setOnLoad(initFunction2);
//				}
//				
//				initScript.addFunction("sch_init", initFunction);
//				getParentPage().setOnLoad(initFunction);
//				
//				if (!schoolChange) {
//					initScript.addFunction("sch_init3_a", initFunction3);
//				}
//				else {
//					initScript.addFunction("sch_init_a", initFunction);
//				}
			}
		}

		return myForm;
	}

	public PresentationObject getSchoolChoiceAnswer(User child) {
		Table T = new Table();
		T.setCellpadding(getCellpadding());
		T.setCellspacing(0);
		String text1 = iwrb.getLocalizedString("school_choice.receipt_1", "The school choice for ");
		String text2 = iwrb.getLocalizedString("school_choice.receipt_2", "has been received. ");
		String text3 = iwrb.getLocalizedString("school_choice.receipt_3", "The application will be processed by each school you applied for .");
		String text4 = iwrb.getLocalizedString("school_choice.receipt_4", "Thank you");

		int row = 1;
		T.add(getText(text1), 1, row);
		T.add(getHeader(child.getName()), 1, row);
		T.add(getText(text2), 1, row++);
		T.add(getText(text3), 1, row++);
		T.add(getText(text4), 1, row++);
		T.setHeight(row++, 12);
		T.add(getUserHomePageLink(), 1, row++);

		return T;
	}

	public PresentationObject getAlreadyChosenAnswer(User child) {
		DataTable T = new DataTable();
		T.setUseBottom(false);
		T.setUseTop(false);
		T.setUseTitles(false);
		String text1 = iwrb.getLocalizedString("school_choice.already_1", "The school choice for ");
		String text2 = iwrb.getLocalizedString("school_choice.already_2", "has already been received. ");

		int row = 1;
		T.add(getText(text1), 1, row);
		T.add(getHeader(child.getName()), 1, row);
		T.add(getText(text2), 1, row);
		row++;
		return T;
	}

	public PresentationObject getAlterChoiceInfo(IWContext iwc) {
		Table T = new Table();
		if (owner != null && choiceDate != null) {
			Object[] arguments = { owner.getName(), new IWCalendar(choiceDate).getLocaleDate(iwc.getCurrentLocale(), IWCalendar.SHORT)};
			String message = iwrb.getLocalizedString("school_choice.school_choice_already_made", "The school choice has already been done by {0} on {1}.");
			Text t = getHeader(MessageFormat.format(message, arguments));
			t.setFontColor("FF0000");
			T.add(t, 1, 1);
		}
		return T;
	}

	public PresentationObject getCurrentSchoolSeasonInfo() {
		SchoolSeason season = null;
		try {
			season = careBuiz.getCurrentSeason();
		}
		catch (RemoteException e) {
		}
		catch (FinderException e) {
		}
		Table T = new Table();
		if (season != null) {
			String message = iwrb.getLocalizedString("school_choice.last_date_for_choice_is", "Last date to choose is");
			String date = df.format(season.getChoiceEndDate());
			Text t = getHeader(message + " " + date);
			if (canApply[1]) t.setFontColor("FF0000");

		}
		return T;

	}

	private void initSchoolChild(User child) {
		try {
			Date d = child.getDateOfBirth();
			if (d == null) {
				if (child.getPersonalID() != null) {
					d = PIDChecker.getInstance().getDateFromPersonalID(child.getPersonalID());
				}
				if (d == null) d = new Date();
			}
			age = new Age(d);
		}
		catch (Exception e) {
		}

		try {
			SchoolSeason previousSeason = null;
			if (_useOngoingSeason)
				previousSeason = season;
			else
				previousSeason = schCommBiz.getPreviousSchoolSeason(season);

			try {
				schoolClassMember = schBuiz.getSchoolBusiness().getSchoolClassMemberHome().findLatestByUserAndSchCategoryAndSeason(child, schBuiz.getSchoolBusiness().getCategoryElementarySchool(), previousSeason);
			}
			catch (FinderException e) {
				if (_showChildCareTypes) {
					// No elementary school placement found, look for one in child care
					try {
						schoolClassMember = schBuiz.getSchoolBusiness().getSchoolClassMemberHome().findLatestByUserAndSchCategory(child, schBuiz.getSchoolBusiness().getCategoryChildcare());
					}
					catch (FinderException fe) {
						return;
					}
				}
				else {
					return;
				}
			}

			schoolClass = schoolClassMember.getSchoolClass();
			school = schoolClass.getSchool();
			if (school != null) {
				schoolArea = school.getSchoolArea();
			}

			schoolType = schoolClassMember.getSchoolType();

			schoolYear = schoolClassMember.getSchoolYear();
			valPreYear = schoolClassMember.getSchoolYearId();
		}
		catch (RemoteException re) {
		}
	}

	private PresentationObject getChildInfo(IWContext iwc, User child) throws java.rmi.RemoteException {
		Table table = new Table();
		table.setColumns(3);
		//table.setWidth(1, "100");
		table.setWidth(2, "8");
		table.setWidth(4, "16");
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setCellpaddingLeft(1, 1, 6);
		table.setCellpaddingLeft(1, 2, 6);
		table.setCellpaddingLeft(1, 3, 6);
		table.setCellpaddingRight(5, 1, 6);
		table.setCellpaddingTop(1, 1, 6);
		table.setCellpaddingTop(3, 1, 6);
		table.setStyleAttribute("background:#f4f4f4;");
		table.setStyleAttribute("border:1px solid #cccccc;");

		table.setBorder(0);
		table.add(getSmallHeader(iwrb.getLocalizedString("school.name", "Name") + ":"), 1, 1);
		table.add(getSmallHeader(iwrb.getLocalizedString("school.personal_id", "Personal ID") + ":"), 1, 2);
		table.add(getSmallHeader(iwrb.getLocalizedString("school.address", "Address") + ":"), 1, 3);

		Name name = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
		table.add(getSmallText(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), 3, 1);
		String personalID = PersonalIDFormatter.format(child.getPersonalID(), iwc.getIWMainApplication().getSettings().getApplicationLocale());
		table.add(getSmallText(personalID), 3, 2);

		Address childAddress = userbuiz.getUsersMainAddress(child);
		if (childAddress != null) {
			table.add(getSmallText(childAddress.getStreetAddress()), 3, 3);
			if (childAddress.getPostalAddress() != null) {
				table.add(getSmallText(", " + childAddress.getPostalAddress()), 3, 3);
			}
		}

		FamilyLogic mlogic = (FamilyLogic) IBOLookup.getServiceInstance(iwc, FamilyLogic.class);
		try {
			Collection parents = mlogic.getCustodiansFor(child);
			Iterator iter = parents.iterator();
			String address = "";
			boolean caseOwning = true;
			boolean parentsSeparated = false;
			while (iter.hasNext()) {
				User parent = (User) iter.next();

				Address parAddress = userbuiz.getUsersMainAddress(parent);
				String sParAddress = null;
				if (parAddress != null) {
					sParAddress = parAddress.getStreetAddress();
				}
				else {
					sParAddress = "";
				}

				if (address == null) {
					address = sParAddress;
				}
				else {
					if (!parentsSeparated) parentsSeparated = address.equals(sParAddress);
				}

				if (quickAdmin && caseOwning && valCaseOwner == -1) {
					valCaseOwner = ((Integer) parent.getPrimaryKey()).intValue();
					caseOwning = false;
				}
			}
			if (valCaseOwner != -1) table.add(new HiddenInput(prmParentId, String.valueOf(valCaseOwner)), 1, 1);
			showAgree = parentsSeparated;
		}
		catch (NoCustodianFound ex) {
			ex.printStackTrace();
			table.add(getSmallErrorText(iwrb.getLocalizedString("school.no_registered_custodians", "No registered custodians")), 3, 4);
		}

		table.add(new Break(3), 1, 5);
		table.mergeCells(5, 1, 5, 5);
		table.setVerticalAlignment(5, 1, Table.VERTICAL_ALIGN_TOP);
		table.add(getCurrentSchool(), 5, 1);
		return table;
	}

	private PresentationObject getCurrentSchool() {
		Table table = new Table();
		table.setColumns(3);
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setBorder(0);

		table.setCellpaddingTop(1, 1, 6);

		table.mergeCells(1, 1, table.getColumns(), 1);

		table.add(getHeader(iwrb.getLocalizedString("school.child_is_now_in", "Child is now in :")), 1, 1);
		table.add(getSmallHeader(iwrb.getLocalizedString("school.school_type", "Type") + ":"), 1, 2);
		table.add(getSmallHeader(iwrb.getLocalizedString("school.school_area", "Area") + ":"), 1, 3);
		table.add(getSmallHeader(iwrb.getLocalizedString("school.school_name", "School name") + ":"), 1, 4);
		table.add(getSmallHeader(iwrb.getLocalizedString("school.school_year", "School year") + ":"), 1, 5);

		Text schoolTypeCurr = null;
		Text schoolAreaCurr = null;
		Text schoolYearCurr = null;
		Text schoolCurr = null;

		if (schoolType != null) {
			schoolTypeCurr = getSmallText(schoolType.toString());
			table.add(new HiddenInput(prmPreType, String.valueOf(schoolType.getPrimaryKey())), 1, 6);
		}
		else {
			schoolTypeCurr = getSmallText("-");
		}
		if (schoolArea != null) {
			schoolAreaCurr = getSmallText(schoolArea.toString());
			table.add(new HiddenInput(prmPreArea, String.valueOf(schoolArea.getPrimaryKey())), 1, 6);
		}
		else {
			schoolAreaCurr = getSmallText("-");
		}
		if (school != null) {
			schoolCurr = getSmallText(school.getName());
			table.add(new HiddenInput(prmPreSchool, String.valueOf(school.getPrimaryKey())), 1, 6);
		}
		else {
			schoolCurr = getSmallText("-");
		}

		if (schoolYear != null) {
			schoolYearCurr = getSmallText(schoolYear.toString());
			table.add(new HiddenInput(prmPreYear, String.valueOf(schoolYear.getPrimaryKey().toString())), 1, 6);
		}
		else {
			schoolYearCurr = getSmallText("-");
		}

		//table.add(drpTypes, 3, 2);
		table.add(schoolTypeCurr, 3, 2);
		//table.add(drpAreas, 3, 3);
		table.add(schoolAreaCurr, 3, 3);
		//table.add(drpSchools, 3, 4);
		table.add(schoolCurr, 3, 4);
		//table.add(drpGrade, 3, 5);
		table.add(schoolYearCurr, 3, 5);
		table.setWidth(1, 2, "100");
		table.setWidth(2, 2, "8");

		return table;
	}

	private DropdownMenu getTypeDrop(String name, boolean useRestrictions, boolean showChildCare) throws java.rmi.RemoteException {
		DropdownMenu drp = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		drp.addMenuElement("-1", iwrb.getLocalizedString("school.school_type_select", "School type select"));

		if (showChildCare) {
			SchoolTypeHome tHome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			try {
				Collection ccTypes = tHome.findAllByCategory(SchoolCategoryBMPBean.CATEGORY_CHILD_CARE, false);
				for (Iterator iter = ccTypes.iterator(); iter.hasNext();) {
					SchoolType tmpType = (SchoolType) iter.next();
					drp.addMenuElement(tmpType.getPrimaryKey().toString(), localize(schCommBiz.getLocalizedSchoolTypeKey(tmpType), tmpType.getSchoolTypeName()));
					schoolTypes.add(tmpType);
				}
			}
			catch (FinderException e) {
				logWarning("No childcare categories found");
				log(e);
			}
		}

		Iterator iter = schoolTypes.iterator();
		boolean canChoose = true;

		while (iter.hasNext()) {
			SchoolType type = (SchoolType) iter.next();
			if (age.getYears() <= type.getMaxSchoolAge())
				canChoose = true;
			else
				canChoose = false;
			if (!useRestrictions) canChoose = true;

			if (canChoose) drp.addMenuElement(type.getPrimaryKey().toString(), localize(schCommBiz.getLocalizedSchoolTypeKey(type), type.getSchoolTypeName()));
		}

		return drp;
	}

	/**
	 * Method getChoiceSchool.
	 * 
	 * @param iwc
	 * @param child
	 * @return PresentationObject
	 * @throws RemoteException
	 */
	private PresentationObject getChoiceSchool() throws java.rmi.RemoteException {
		Table table = new Table();
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setColumns(5);
		table.mergeCells(1, 1, 4, 1);
		table.setBorder(0);
		table.setWidthAndHeightToHundredPercent();
		table.setWidth(5, 1, "100%");
		table.add(getHeader(iwrb.getLocalizedString("school.choice_for_schoolyear", "Choice for the schoolyear")), 1, 1);

		DropdownMenu typeDrop = getTypeDrop(prmType, true, false);
//		typeDrop.setOnChange(getFilterCallerScript(prmType, prmFirstArea, prmFirstSchool, 1, false));

		DropdownMenu drpGrade = (DropdownMenu) getStyledInterface(new DropdownMenu(prmYear));
		drpGrade.addMenuElementFirst("-1", iwrb.getLocalizedString("school.select_year", "Select year"));
		if (valType > 0) {
			typeDrop.setSelectedElement(valType);
			try {
				Collection schoolYears = schBuiz.getSchoolYearHome().findAllSchoolYearBySchoolType(valType);
				drpGrade.addMenuElements(schoolYears);
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}
		
		RemoteScriptHandler yearRemoteScriptHandler = new RemoteScriptHandler(typeDrop, drpGrade);
		
		try {
			yearRemoteScriptHandler.setRemoteScriptCollectionClass(SchoolYearSelectorCollectionHandler.class);
			add(yearRemoteScriptHandler);
		}
		catch (IllegalAccessException iae) {
			iae.printStackTrace();
		}
		catch (InstantiationException ie) {
			ie.printStackTrace();
		}

		
		
		/*if (_reloadYear != -1){
			drpGrade.setSelectedElement(String.valueOf(_reloadYear));
		}else*/ 
		if (valYear > -1) {
//			Collection coll = getSchoolYears();
//			if (coll != null) {
//				Iterator iter = coll.iterator();
//				while (iter.hasNext()) {
//					SchoolYear element = (SchoolYear) iter.next();
//					drpGrade.addMenuElement(element.getPrimaryKey().toString(), element.getName());
//				}
//			}
			drpGrade.setSelectedElement(String.valueOf(valYear));	
		}

		
//		drpGrade.setOnChange(getSchoolGradeScript(prmYear, prmYearReload, prmRealSubmit));
		drpGrade.setAsNotEmpty(iwrb.getLocalizedString("school.school_year_must_select", "You must select a school year"));
		
		
		CheckBox chkChildCare = getCheckBox(prmSixYearCare, "true");
		chkChildCare.setChecked(valSixyearCare);

		DropdownMenu txtLangChoice = (DropdownMenu) getStyledInterface(new DropdownMenu(prmLanguage));
		txtLangChoice.addMenuElement(-1, localize("school.language", "Language"));
		txtLangChoice.addMenuElement("school.language_german", localize("school.language_german", "German"));
		txtLangChoice.addMenuElement("school.language_french", localize("school.language_french", "French"));
		txtLangChoice.addMenuElement("school.language_spanish", localize("school.language_spanish", "Spanish"));
		txtLangChoice.addMenuElement("school.language_swedish_english", localize("school.language_swedish_english", "Swedish/English"));
		if (valLanguage != null) txtLangChoice.setSelectedElement(valLanguage);

		DropdownMenu drpFirstArea = getDropdown(prmFirstArea, iwrb.getLocalizedString("school.area_first", "School Area...................."));
		DropdownMenu drpFirstSchool = (DropdownMenu) getStyledInterface(new DropdownMenu(prmFirstSchool));
		drpFirstSchool.addMenuElementFirst("-1", iwrb.getLocalizedString("school.school_first", "School........................."));
		drpFirstSchool.setOnChange("alertIfSportsOrMusicSchool(this)");


		Collection schoolAreas = null;
		if (valType > 0) {
			schoolAreas = schBuiz.getSchoolBusiness().findAllSchoolAreasByType(valType);
		}
		if (valFirstSchool > 0 && schoolAreas != null) {
			int areaID = Integer.parseInt(schBuiz.getSchool(valFirstSchool).getSchoolArea().getPrimaryKey().toString());
			drpFirstArea.addMenuElements(schoolAreas);
			drpFirstArea.setSelectedElement(areaID);
			Collection schools = schBuiz.getSchoolBusiness().findAllSchoolsByAreaAndTypeAndYear(areaID, valType, valYear);
			drpFirstSchool.addMenuElements(schools);
			drpFirstSchool.setSelectedElement(valFirstSchool);
		}

		try {
			RemoteScriptHandler rsh = new RemoteScriptHandler(drpGrade, drpFirstArea);
			rsh.setRemoteScriptCollectionClass(SchoolChoiceApplicationSchoolAreaHandler.class);
			rsh.setToClear(drpFirstSchool, iwrb.getLocalizedString("choose_school", "Choose School"));
			add(rsh);
			
			RemoteScriptHandler rsh2 = new RemoteScriptHandler(drpFirstArea, drpFirstSchool);
			rsh2.setRemoteScriptCollectionClass(SchoolChoiceApplicationSchoolHandler.class);
			add(rsh2);
		
			if (yearRemoteScriptHandler != null) {
				yearRemoteScriptHandler.setToClear(drpFirstArea, iwrb.getLocalizedString("choose_area", "Choose Area"));
				yearRemoteScriptHandler.setToClear(drpFirstSchool, iwrb.getLocalizedString("choose_school", "Choose School"));
			}
		}
		catch (IllegalAccessException iae) {
			iae.printStackTrace();
		}
		catch (InstantiationException ie) {
			ie.printStackTrace();
		}


		int row = 2;
		table.add(getSmallHeader(iwrb.getLocalizedString("school.school_type", "SchoolType") + ":"), 1, row);
		table.add(typeDrop, 3, row++);

		table.add(getSmallHeader(iwrb.getLocalizedString("school.school_year", "School year") + ":"), 1, row);
		table.add(drpGrade, 3, row++);

		table.add(getSmallHeader(iwrb.getLocalizedString("school.first_choice", "First choice") + ":"), 1, row);
		table.add(drpFirstArea, 3, row);
		table.add(drpFirstSchool, 5, row++);

		if (this._showChoiceMessage) {
			table.setHeight(row++, 2);
			TextInput choiceMessage = (TextInput) getStyledInterface(new TextInput(prmExtraChoiceMessage));
			if (valExtraChoiceMessages[0] != null) {
				choiceMessage.setContent(valExtraChoiceMessages[0]);
			}
			choiceMessage.setLength(50);
			table.mergeCells(3, row, 5, row);
			table.add(getSmallHeader(iwrb.getLocalizedString("school.extra_message", "Extra message") + ":"), 3, row);
			table.add(new Break(), 3, row);
			table.add(choiceMessage, 3, row++);
			table.setHeight(row++, 6);
		}

		if (!this.schoolChange) {
//			typeDrop.setOnChange(getFilterCallerScript(prmType, prmSecondArea, prmSecondSchool, 1, false));
//			typeDrop.setOnChange(getFilterCallerScript(prmType, prmThirdArea, prmThirdSchool, 1, false));

			DropdownMenu drpSecondArea = getDropdown(prmSecondArea, iwrb.getLocalizedString("school.area_second", "School Area...................."));
			DropdownMenu drpSecondSchool = (DropdownMenu) getStyledInterface(new DropdownMenu(prmSecondSchool));
			drpSecondSchool.addMenuElementFirst("-1", iwrb.getLocalizedString("school.school_second", "School........................."));
			drpSecondSchool.setOnChange("alertIfSportsOrMusicSchool(this)");

			DropdownMenu drpThirdArea = getDropdown(prmThirdArea, iwrb.getLocalizedString("school.area_third", "School Area...................."));
			DropdownMenu drpThirdSchool = (DropdownMenu) getStyledInterface(new DropdownMenu(prmThirdSchool));
			drpThirdSchool.addMenuElementFirst("-1", iwrb.getLocalizedString("school.school_third", "School........................."));
			drpThirdSchool.setOnChange("alertIfSportsOrMusicSchool(this)");

			if (valSecondSchool > 0) {
				int areaID = Integer.parseInt(schBuiz.getSchool(valSecondSchool).getSchoolArea().getPrimaryKey().toString());
				drpSecondArea.addMenuElements(schoolAreas);
				drpSecondArea.setSelectedElement(schBuiz.getSchool(valSecondSchool).getSchoolArea().getPrimaryKey().toString());
				Collection schools = schBuiz.getSchoolBusiness().findAllSchoolsByAreaAndTypeAndYear(areaID, valType, valYear);
				drpSecondSchool.addMenuElements(schools);
				drpSecondSchool.setSelectedElement(valSecondSchool);
			}
			if (valThirdSchool > 0) {
				int areaID = Integer.parseInt(schBuiz.getSchool(valThirdSchool).getSchoolArea().getPrimaryKey().toString());
				drpThirdArea.addMenuElements(schoolAreas);
				drpThirdArea.setSelectedElement(schBuiz.getSchool(valThirdSchool).getSchoolArea().getPrimaryKey().toString());
				Collection schools = schBuiz.getSchoolBusiness().findAllSchoolsByAreaAndTypeAndYear(areaID, valType, valYear);
				drpThirdSchool.addMenuElements(schools);
				drpThirdSchool.setSelectedElement(valThirdSchool);
			}
			
			try {
				RemoteScriptHandler rsh = new RemoteScriptHandler(drpFirstSchool, drpSecondArea);
				rsh.setRemoteScriptCollectionClass(SchoolChoiceApplicationSchoolAreaHandler.class);
				rsh.setToClear(drpSecondSchool, iwrb.getLocalizedString("choose_school", "Choose School"));
				add(rsh);
				
				RemoteScriptHandler rsh2 = new RemoteScriptHandler(drpSecondArea, drpSecondSchool);
				rsh2.setRemoteScriptCollectionClass(SchoolChoiceApplicationSchoolHandler.class);
				add(rsh2);
			
				RemoteScriptHandler rsh3 = new RemoteScriptHandler(drpSecondSchool, drpThirdArea);
				rsh3.setRemoteScriptCollectionClass(SchoolChoiceApplicationSchoolAreaHandler.class);
				rsh3.setToClear(drpThirdSchool, iwrb.getLocalizedString("choose_school", "Choose School"));
				add(rsh3);
				
				RemoteScriptHandler rsh4 = new RemoteScriptHandler(drpThirdArea, drpThirdSchool);
				rsh4.setRemoteScriptCollectionClass(SchoolChoiceApplicationSchoolHandler.class);
				add(rsh4);
			
				if (yearRemoteScriptHandler != null) {
					yearRemoteScriptHandler.setToClear(drpSecondArea, iwrb.getLocalizedString("choose_area", "Choose Area"));
					yearRemoteScriptHandler.setToClear(drpSecondSchool, iwrb.getLocalizedString("choose_school", "Choose School"));

					yearRemoteScriptHandler.setToClear(drpThirdArea, iwrb.getLocalizedString("choose_area", "Choose Area"));
					yearRemoteScriptHandler.setToClear(drpThirdSchool, iwrb.getLocalizedString("choose_school", "Choose School"));
				}
			}
			catch (IllegalAccessException iae) {
				iae.printStackTrace();
			}
			catch (InstantiationException ie) {
				ie.printStackTrace();
			}

			
			table.add(getSmallHeader(iwrb.getLocalizedString("school.second_choice", "Second choice") + ":"), 1, row);
			table.add(drpSecondArea, 3, row);
			table.add(drpSecondSchool, 5, row++);

			if (this._showChoiceMessage) {
				table.setHeight(row++, 2);
				TextInput choiceMessage = (TextInput) getStyledInterface(new TextInput(prmExtraChoiceMessage));
				if (valExtraChoiceMessages[1] != null) {
					choiceMessage.setContent(valExtraChoiceMessages[1]);
				}
				choiceMessage.setLength(50);
				table.mergeCells(3, row, 5, row);
				table.add(getSmallHeader(iwrb.getLocalizedString("school.extra_message", "Extra message") + ":"), 3, row);
				table.add(new Break(), 3, row);
				table.add(choiceMessage, 3, row++);
				table.setHeight(row++, 6);
			}

			table.add(getSmallHeader(iwrb.getLocalizedString("school.third_choice", "Third choice") + ":"), 1, row);
			table.add(drpThirdArea, 3, row);
			table.add(drpThirdSchool, 5, row++);

			if (this._showChoiceMessage) {
				table.setHeight(row++, 2);
				TextInput choiceMessage = (TextInput) getStyledInterface(new TextInput(prmExtraChoiceMessage));
				if (valExtraChoiceMessages[2] != null) {
					choiceMessage.setContent(valExtraChoiceMessages[2]);
				}
				choiceMessage.setLength(50);
				table.mergeCells(3, row, 5, row);
				table.add(getSmallHeader(iwrb.getLocalizedString("school.extra_message", "Extra message") + ":"), 3, row);
				table.add(new Break(), 3, row);
				table.add(choiceMessage, 3, row++);
				table.setHeight(row++, 6);
			}
			if (_outsideCommunePage != null){
				Link outsideCommune = new Link();
				outsideCommune.setPage(_outsideCommunePage);
				outsideCommune.setText(iwrb.getLocalizedString("school.outside_commune_link", "For you who choose school outside of the municipality"));
				outsideCommune.setTarget(Link.TARGET_NEW_WINDOW);
				table.setHeight(row++, 6);
				table.mergeCells(1, row, 5, row);
				table.add(outsideCommune, 1, row++);
				table.setHeight(row++, 6);
			}
			
		}
		
	
		if ((schoolYear != null && schoolYear.getSchoolYearAge() >= 12) || age.getYears() >= 11) {
			table.setHeight(row++, 5);
			table.add(getSmallHeader(iwrb.getLocalizedString("school.six_year_language", "Language") + ":"), 1, row);
			table.add(txtLangChoice, 3, row);
			table.mergeCells(3, row, 5, row++);
			hasLanguageSelection = true;
		}

		if (_useOngoingSeason) {
			IWTimestamp stamp = new IWTimestamp();
			stamp.addDays(1);
			DateInput date = (DateInput) getStyledInterface(new DateInput(prmPlacementDate));
			//date.setDate(stamp.getDate());
			date.setEarliestPossibleDate(stamp.getDate(), iwrb.getLocalizedString("school.dates_back_in_time_not_allowed", "You can not choose a date back in time."));
			date.setAsNotEmpty(iwrb.getLocalizedString("school.dates_back_in_time_not_allowed", "You can not choose a date back in time."));
			table.setHeight(row++, 5);
			table.add(getSmallHeader(iwrb.getLocalizedString("school.placement_date", "Placement date") + ":"), 1, row);
			table.add(date, 3, row);
			table.mergeCells(3, row, 5, row++);
		}

		if (_maxAge <= 0) {
			_maxAge = 10;
		}

		if (age.getYears() <= _maxAge) {
			table.setHeight(row++, 5);
			table.mergeCells(1, row, 5, row);
			table.add(getHeader(iwrb.getLocalizedString("school.after_school_choice", "Choice of after school care")), 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			Link infoLink = new Link(this.getInformationIcon(iwrb.getLocalizedString("school.after_school_choice_information", "Information about the after school choice.")));
			infoLink.setToOpenAlert(iwrb.getLocalizedString("school.after_school_choice_message", "Information about the after school choice..."));
			table.add(infoLink, 1, row++);

			table.mergeCells(1, row, 5, row);
			if (_useCheckBoxForAfterSchoolCare) {
				CheckBox wantsAfterSchool = getCheckBox(prmAfterschool, Boolean.TRUE.toString());
				table.add(wantsAfterSchool, 1, row);
				table.add(getSmallHeader(Text.getNonBrakingSpace() + iwrb.getLocalizedString("school.want_after_school_care", "I want afterschool care")), 1, row++);
			}
			else {
				RadioButton rbWantsAfterSchool = getRadioButton(prmAfterschool, Boolean.TRUE.toString());
				RadioButton rbNotAfterSchool = getRadioButton(prmAfterschool, Boolean.FALSE.toString());
				rbNotAfterSchool.setMustBeSelected(iwrb.getLocalizedString("school.must_select_after_school_option", "You have to select an after school option"));
				table.add(rbWantsAfterSchool, 1, row);
				table.add(getSmallHeader(Text.getNonBrakingSpace() + iwrb.getLocalizedString("school.want_after_school_care", "I want afterschool care")), 1, row++);
				table.mergeCells(1, row, 5, row);
				table.add(rbNotAfterSchool, 1, row);
				table.mergeCells(1, row, 5, row);
				table.add(getSmallHeader(Text.getNonBrakingSpace() + iwrb.getLocalizedString("school.not_want_after_school_care", "I do not want afterschool care")), 1, row++);
			}
			table.setHeight(row++, 5);
		}

		// School choice message link
		table.mergeCells(1, row, 5, row);
		table.add(getHeader(iwrb.getLocalizedString("school.language_choice", "Choice of language")), 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		Link msgLink = new Link(this.getInformationIcon(localize("school_choice.form_message_link_text", "School choice message")));
		msgLink.setToOpenAlert(localize("school_choice_form_message.message", "Localized School choice message ... "));
		table.add(msgLink, 1, row++);

		CheckBox langChecked = getCheckBox(prmNativeLangIsChecked, Boolean.TRUE.toString());
		table.add(langChecked, 1, row);
		table.add(getSmallHeader(Text.getNonBrakingSpace() + iwrb.getLocalizedString("school.native_lang_prefix", "I would like")), 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getNativeLanguagesDropdown(), 1, row);
		table.mergeCells(1, row, 5, row);

		table.add(new HiddenInput(prmCustodiansAgree, String.valueOf(showAgree)), 1, row);

		return table;
	}

	private DropdownMenu getDropdown(String name, String firstElement) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		menu.addMenuElementFirst("-1", firstElement);
		return menu;
	}

	private DropdownMenu getNativeLanguagesDropdown() {
		DropdownMenu drop = (DropdownMenu) getStyledInterface(new DropdownMenu(prmNativeLang));
		drop.addMenuElement("-1", localize("school.drp_chose_native_lang", "- Chose languge -"));
		try {
			Collection langs = getICLanguageHome().findAll();
			if (langs != null) {
				for (Iterator iter = langs.iterator(); iter.hasNext();) {
					ICLanguage aLang = (ICLanguage) iter.next();
					int langPK = ((Integer) aLang.getPrimaryKey()).intValue();
					drop.addMenuElement(langPK, aLang.getName());
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
		catch (FinderException fe) {

		}
		return drop;
	}

	/*private PresentationObject getMessagePart() {
	 Table table = new Table(1, 3);
	 table.setWidth(300);
	 table.setCellpadding(2);
	 table.setCellspacing(0);
	 table.setBorder(0);
	 
	 TextArea message = (TextArea) getStyledInterface(new TextArea(prmMessage));
	 message.setRows(6);
	 message.setColumns(65);
	 if (valMessage != null) message.setValue(valMessage);

	 if (schoolChange)
	 table.add(getSmallHeader(localize("school.application_change_required_reason", "Reason for schoolchange (Required)")), 1, 1);
	 else
	 table.add(getSmallHeader(localize("school.application_extra_info", "Extra info")), 1, 1);
	 table.add(message, 1, 2);
	 
	 return table;
	 }*/
	
	private TextArea getMessagePart2() {
		Table table = new Table(1, 3);
		table.setWidth(300);
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setBorder(0);
		
		TextArea message = (TextArea) getStyledInterface(new TextArea(prmMessage));
		message.setRows(6);
		message.setColumns(65);
		if (valMessage != null) message.setValue(valMessage);

		
		
		return message;
	}

	private Collection getSchoolTypes(String category) {
		try {
			return schCommBiz.getSchoolBusiness().findAllSchoolTypesInCategory(category);
		}
		catch (Exception ex) {

		}
		return null;
	}


	public String getSchoolCheckScript() {
		StringBuffer s = new StringBuffer();
		s.append("\nfunction checkApplication(){\n\t");
		//if (!this.hasPreviousSchool && !hasChosen)
		//s.append("\n\t var currSchool =
		// ").append("findObj('").append(prmPreSchool).append("');");
		//s.append("\n\t\t alert(document.forms[1].elements['sch_app_real_submit'].value)");
		
		s.append("\n\t var gradeDrop = ").append("findObj('").append(prmYear).append("');");
		s.append("\n\t var dropOne = ").append("findObj('").append(prmFirstSchool).append("');");

		if (!schoolChange) {
			s.append("\n\t var dropTwo = ").append("findObj('").append(prmSecondSchool).append("');");
			s.append("\n\t var dropThree = ").append("findObj('").append(prmThirdSchool).append("');");
		}
		//if (!this.hasPreviousSchool && !hasChosen)
		//s.append("\n\t var gradeDrop =
		// ").append("findObj('").append(prmPreGrade).append("');");
		s.append("\n\t var dropNativeLang = ").append("findObj('").append(prmNativeLang).append("');");
		s.append("\n\t var checkNativeLang = ").append("findObj('").append(prmNativeLangIsChecked).append("');");

		s.append("\n\n\t if (dropNativeLang.options[dropNativeLang.selectedIndex].value > 0 && checkNativeLang.checked == false){");
		s.append("\n\t\t alert('" + localize("school.must_fill_native_language", "You must check the checkbox if you want native language ") + "');");
		s.append("\n\t\t return false; \n}");
		s.append("\n\n\t else if (dropNativeLang.options[dropNativeLang.selectedIndex].value < 0 && checkNativeLang.checked == true){");
		s.append("\n\t\t alert('" + localize("school.must_fill_native_language_drp", "Select a native language in the dropdown ") + "');");
		s.append("\n\t\t return false; \n}");
		
		if (hasLanguageSelection) s.append("\n\t var languageDrop = ").append("findObj('").append(prmLanguage).append("');");

		s.append("\n\t var one = 0;");
		if (!schoolChange) {
			s.append("\n\t var two = 0;");
			s.append("\n\t var three = 0;");
		}
		s.append("\n\t var year = 0;");
		s.append("\n\t var school = 0;");

		s.append("\n\n\t if (gradeDrop.selectedIndex > 0) grade = gradeDrop.options[gradeDrop.selectedIndex].value;");
		s.append("\n\n\t if (dropOne.selectedIndex > 0) one = dropOne.options[dropOne.selectedIndex].value;");
		if (!schoolChange) {
			s.append("\n\t if (dropTwo.selectedIndex > 0) two = dropTwo.options[dropTwo.selectedIndex].value;");
			s.append("\n\t if (dropThree.selectedIndex > 0) three = dropThree.options[dropThree.selectedIndex].value;");
		}

		if (hasLanguageSelection) {
			s.append("\n\n\t if (languageDrop.options[languageDrop.selectedIndex].value == -1) {");
			s.append("\n\t\t alert('" + localize("school.must_fill_language", "You must select a language choice") + "');");
			s.append("\n\t\t return false; \n}");
		}
		//if (!this.hasPreviousSchool)
		//s.append("\n\t if (gradeDrop.selectedIndex > 0) year =
		// gradeDrop.options?gradeDrop.options[gradeDrop.selectedIndex].value:document.sch_app_the_frm.elements['"
		// + prmPreGrade + "'].value;");
		//if (!this.hasPreviousSchool)
		//s.append("\n\t if (currSchool.selectedIndex > 0) school =
		// currSchool.options?currSchool.options[currSchool.selectedIndex].value:document.sch_app_the_frm.elements['"
		// + prmPreSchool + "'].value;");

		// current school check
		/*
		 * if (!this.hasPreviousSchool) { s.append("\n\n\t if(school <= 0){");
		 * String msg1 =
		 * iwrb.getLocalizedString("school_choice.must_set_current_school", "You
		 * must provide current shool"); s.append("\n\t\t\t
		 * alert('").append(msg1).append("');"); s.append("\n\t\t return false;");
		 * s.append("\n\t\t "); s.append("\n\t }");
		 */

		// year check
		/*
		 * s.append("\n\t if(year <= 0 && school > 0){"); String msg2 =
		 * iwrb.getLocalizedString("school_choice.must_set_grade", "You must provide
		 * current shool year"); s.append("\n\t\t\t
		 * alert('").append(msg2).append("');"); s.append("\n\t\t return false;");
		 * s.append("\n\t\t ");
		 */

		// schoolchoices checked
		if (!schoolChange) {
			s.append("\n\t if(one > 0 && two > 0 && three > 0){");
			s.append("\n\t if(one == two || two == three || three == one){");
			String msg = iwrb.getLocalizedString("school_school.must_not_be_the_same", "Please do not choose the same school more than once");
			s.append("\n\t\t\t alert('").append(msg).append("');");
			s.append("\n\t\t\t return false;");
			s.append("\n\t\t }");
			s.append("\n\t }");
			s.append("\n\t else{");
			msg = iwrb.getLocalizedString("school_school.must_fill_out", "Please fill out all choices");
			s.append("\n\t\t alert('").append(msg).append("');");
			s.append("\n\t\t return false;");
			s.append("\n\t }");
		}
		else {
			s.append("\n\t if (one <= 0) {");
			String msg = iwrb.getLocalizedString("school_school.must_fill_out", "Please fill out all choices");
			s.append("\n\t\t alert('").append(msg).append("');");
			s.append("\n\t\t return false;");
			s.append("\n\t }");
			s.append("\n\t var reason =  findObj('").append(prmMessage).append("');");
			s.append("\n\t if(reason.value.length<=10){");
			msg = iwrb.getLocalizedString("school_school.change_must_give_reason", "You must give a reason for  the change !");
			s.append("\n\t\t alert('").append(msg).append("');");
			s.append("\n\t\t return false;");
			s.append("\n\t }");
		}
		s.append("\n\t if (grade <= 0) {");
		String msg = iwrb.getLocalizedString("school_school.must_fill_out_year", "Please select a year to apply to");
		s.append("\n\t\t alert('").append(msg).append("');");
		s.append("\n\t\t return false;");
		s.append("\n\t }");
		//s.append("\n\t\t
		// findObj('").append("submit").append("').disabled=true;");
		s.append("\n\t document.body.style.cursor = 'wait'");
		s.append("\n\t return true;");
		s.append("\n}\n");
		return s.toString();
	}

	/*
	 * We hard code the schools for which an alert message is shown, until further
	 * notice, since this applies only for the nacka24 site.
	 */
	private String getAlertIfSportsOrMusicSchool() {
		StringBuffer sb = new StringBuffer();
		sb.append("function alertIfSportsOrMusicSchool(thisObject) {\n").append("\t var schools = new Array('Eklidens skola - Idrottsklass', 'Järla skola - Musikklass');\n").append("\t for (loop=0; loop < schools.length; loop++) {\n").append("\t\t tmpSchool = schools[loop];").append("\t\t if (tmpSchool == thisObject.options[thisObject.selectedIndex].text) {\n").append("\t\t\t alert('").append(iwrb.getLocalizedString("school_choice.alert_if_sports_or_music_school_msg", "Information: An approved application test is required for")).append("' + ' ' + tmpSchool);\n").append("\t\t\t return false;\n").append("\t\t }\n").append("\t }\n").append("}\n");

		return sb.toString();
	}

	private boolean[] checkCanApply(IWContext iwc) throws RemoteException {

		if (_useOngoingSeason) {
			boolean[] canApply = { true, true, true};
			return canApply;
		}

		boolean[] checkCanApply = { false, false, false};
		try {
			SchoolSeason season = careBuiz.getCurrentSeason();
			if (season != null) {
				IWPropertyList properties = iwc.getSystemProperties().getProperties("school_properties");
				String choiceStart = properties.getProperty("choice_start_date");
				String choiceEnd = properties.getProperty("choice_end_date");
				String choiceRed = properties.getProperty("choice_critical_date");
				String changeStart = properties.getProperty("school_change_start");

				IWTimestamp seasonStart = new IWTimestamp(season.getSchoolSeasonStart());
				IWTimestamp dateNow = new IWTimestamp();
				if (choiceStart != null && choiceEnd != null && choiceRed != null) {
					SchoolChoiceBusiness business = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
					IWTimestamp start = business.getSchoolChoiceStartDate();
					IWTimestamp end = business.getSchoolChoiceEndDate();
					IWTimestamp red = business.getSchoolChoiceCriticalDate();

					if (dateNow.isBetween(start, end))
						checkCanApply[0] = true;
					else
						checkCanApply[0] = false;

					if (red.isEarlierThan(dateNow)) checkCanApply[1] = true;

					//temporary, for testing only...
					if (_isForTesting) {
						checkCanApply[0] = true;
						checkCanApply[1] = false;
					}
				}
				if (changeStart != null) {
					IWTimestamp change = new IWTimestamp(seasonStart);
					change.setDay(Integer.parseInt(changeStart.substring(0, 2)));
					change.setMonth(Integer.parseInt(changeStart.substring(3)));
					if (dateNow.isLaterThan(change)) {
						checkCanApply[2] = true;
					}

					if (dateNow.getYear() <= 2002) checkCanApply[2] = true;

					if (schoolChange) checkCanApply[0] = true;
				}
			}
			return checkCanApply;
		}
		catch (FinderException fe) {
			return checkCanApply;
		}
	}

	public void setAsAdminQuickChoice(boolean quick) {
		this.quickAdmin = quick;
	}

	public void setAsSchoolChange(boolean change) {
		this.schoolChange = change;
	}

	/**
	 * Sets the childcarePage.
	 * 
	 * @param childcarePage
	 *          The childcarePage to set
	 */
	public void setChildcarePage(ICPage childcarePage) {
		afterSchoolPageID = (Integer) childcarePage.getPrimaryKey();
	}

	/**
	 * Sets the childcarePage.
	 * 
	 * @param childcarePage
	 *          The childcarePage to set
	 */
	public void setAfterschoolPage(ICPage afterSchoolPage) {
		this.afterSchoolPageID = (Integer) afterSchoolPage.getPrimaryKey();
	}

	/**
	 * Sets the childcarePage ID.
	 * 
	 * @param childcarePageID
	 *          The childcarePage to set
	 */
	public void setCheckPage(ICPage checkPage) {
		this.checkPageID = (Integer) checkPage.getPrimaryKey();
	}

	private UserHomeLink getUserHomePageLink() {
		return new UserHomeLink();
	}

	private CareBusiness getCareBusiness(IWApplicationContext iwc) {
		try {
			return (CareBusiness) IBOLookup.getServiceInstance(iwc, CareBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private ICLanguageHome getICLanguageHome() throws RemoteException {
		return (ICLanguageHome) IDOLookup.getHome(ICLanguage.class);
	}

	private UserSearcher createSearcher () {
		final UserSearcher searcher = new UserSearcher ();
		searcher.setOwnFormContainer (false);
		searcher.setShowMiddleNameInSearch (false);
		searcher.setLastNameLength (14);
		searcher.setFirstNameLength (14);
		searcher.setPersonalIDLength (12);
		searcher.setMaxFoundUserCols (MAX_FOUND_USER_COLS); 
		searcher.setMaxFoundUserRows (MAX_FOUND_USER_ROWS);
		return searcher;
	}
	
	/**
	 * Shows a form where the user can enter ssn, first name and/or last name
	 * and then after first search, click on one of possibly more than one name
	 * in the search result.
	 *
	 * @param context session data
	 * @exception RemoteException if exception happens in lower layer
	 */
	private Table getUserSearchFormTable (final IWContext iwc)
	throws RemoteException, Exception {
		// set up searcher
		final UserSearcher searcher = createSearcher ();
		fillSearcherWithStudents (iwc, searcher);
		final User foundUser = searcher.getUser ();
		
		// output result
		final Table table = new Table ();
		final Form searchForm = new Form();
		searchForm.setOnSubmit("return checkInfoForm()");
		
		searchForm.add (searcher);
		table.add (searchForm, 1, 1);
		if (null == foundUser) {
			/*final String message1 = localize (ONLY_MOVING_TO_OTHER_MUNICIP_KEY,
			 ONLY_MOVING_TO_OTHER_MUNICIP_KEY);
			 final String message2 = localize (ONLY_UNENDED_PLACEMENTS_SEARCHABLE_KEY,
			 ONLY_UNENDED_PLACEMENTS_SEARCHABLE_KEY);
			 
			 
			 table.add (getSmallText ("- " + message1), 1, 2);
			 table.add (getSmallText ("- " + message2), 1, 3);
			 */
		} else {
			// exactly one user found - display user and termination form
			/*final Table terminateTable = new Table ();
			 terminateTable.add (getStudentTable (context, foundUser), 1, 2);
			 final Form terminateForm = new Form ();
			 terminateForm.add (terminateTable);
			 table.add (terminateForm, 1, 3);
			 */
			//testar(foundUser);
			//boolean test1 = context.isParameterSet(prmChildIdAdmin);
			//String test = context.getParameter(prmChildIdAdmin);
			try {
				
				control(iwc);
				
			}
			catch (Exception e){
				
			}
			//table.add(getSchoolChoiceForm(context, foundUser), 1, 3);
		}
		
		return table;
	}
	
	private void testar(User foundUser) {
		/*final Table testTable = new Table();
		 testTable.setBorder(1);
		 testTable.add(new HiddenInput(prmChildIdAdmin, foundUser.getPrimaryKey().toString()), 1,1);
		 testTable.add("malin", 1, 2);
		 final Form testForm = new Form();
		 testForm.add(testTable);
		 
		 add(testForm);
		 */
		_childId = foundUser.getPrimaryKey().toString();
	}
	
	
	/**
	 * Retreives students that are currently members of a school class
	 *
	 * @param context session data
	 * @param searcher to use for searching
	 * @return number of users found
	 * @exception RemoteException if exception happens in lower layer
	 */
	private void fillSearcherWithStudents (final IWContext context,
			final UserSearcher searcher)
	throws RemoteException {
		//final Collection students = new HashSet ();
		Collection usersFound = null;
		try {
			// 1. start with a raw search
			searcher.process (context);
			usersFound = searcher.getUsersFound ();
			if (null == usersFound) {
				final User singleUser = searcher.getUser ();
				if (null != singleUser) {
					usersFound = Collections.singleton (singleUser);
				} else {
					throw new FinderException ();
				}
			}
			
			// 2. filter out students that are placed and that the logged on
			// user is authorized to see
			/*	final SchoolCommuneBusiness communeBusiness
			 = (SchoolCommuneBusiness) IBOLookup.getServiceInstance
			 (context, SchoolCommuneBusiness.class);
			 final SchoolBusiness schoolBusiness
			 = communeBusiness.getSchoolBusiness ();
			 final SchoolClassMemberHome memberHome
			 = schoolBusiness.getSchoolClassMemberHome ();
			 final int schoolId = 3;
			 try {
			 final Collection members = memberHome
			 .findAllBySchoolAndUsersWithSchoolYearAndNotRemoved
			 (schoolId, usersFound);
			 for (Iterator i = members.iterator (); i.hasNext ();) {
			 final SchoolClassMember student
			 = (SchoolClassMember) i.next ();
			 if (MAX_FOUND_USERS <= students.size ()) {
			 // too many students found
			 displayRedText (TOOMANYSTUDENTSFOUND_KEY,
			 TOOMANYSTUDENTSFOUND_DEFAULT);
			 throw new FinderException ();
			 }
			 students.add (student.getStudent ());
			 }
			 } catch (FinderException e) {
			 // no problem, nu students found
			 }
			 */
			if (MAX_FOUND_USERS <= usersFound.size ()) {
				// too many students found
				displayRedText (TOOMANYSTUDENTSFOUND_KEY,
						TOOMANYSTUDENTSFOUND_DEFAULT);
				throw new FinderException ();
			}
			
			if (usersFound.isEmpty ()) {
				displayRedText (NOUSERFOUND_KEY, NOUSERFOUND_DEFAULT);
			}
			
		} catch (FinderException e) {
			// no students found or too many students found
			// Collection 'students' will have the right members anyway
		}
		
		// 3. Set up search result for display
		if (usersFound == null) {
			searcher.setUser (null);
			searcher.setUsersFound (null);
		} else if (1 == usersFound.size ()) {
			searcher.setUser ((User) usersFound.toArray () [0]);
			searcher.setUsersFound (null);
			testar(searcher.getUser());
		} else {
			searcher.setUsersFound (usersFound);
		}
		
	}
	/**
	 * Returns a styled table with content placed properly
	 *
	 * @param content the page unique content
	 * @return Table to add to output
	 */
	private Table createMainTable (final PresentationObject content) {
		final Table mainTable = new Table();
		mainTable.setCellpadding (getCellpadding ());
		mainTable.setCellspacing (getCellspacing ());
		mainTable.setWidth (Table.HUNDRED_PERCENT);
		mainTable.setColumns (1);
		//mainTable.setRowColor (1, getHeaderColor ());
		mainTable.setRowAlignment(1, Table.HORIZONTAL_ALIGN_CENTER) ;
		
		/*mainTable.add (getSmallHeader (localize (TERMINATEMEMBERSHIP_KEY,
		 TERMINATEMEMBERSHIP_DEFAULT)),
		 1, 1);
		 */
		mainTable.add (content, 1, 2);
		return mainTable;
	}
	
	private void displayRedText (final String key, final String defaultString) {
		final Text text
		= new Text ('\n' + localize (key, defaultString) + '\n');
		text.setFontColor ("#ff0000");
		add (text);
	}
	
	//Malin
	/**
	 * @param isForTesting
	 *          The isForTesting to set.
	 */
	public void setForTesting(boolean isForTesting) {
		this._isForTesting = isForTesting;
	}
	/**
	 * @param isUseAsAdmin
	 *          The isUseAsAdmin to set.
	 */
	public void setUseAsAdmin(boolean isUseAsAdmin) {
		this._useAsAdmin = isUseAsAdmin;
	}
	/**
	 * @param showChoiceMessage
	 *          The showChoiceMessage to set.
	 */
	public void setShowExtraChoiceMessage(boolean showChoiceMessage) {
		this._showChoiceMessage = showChoiceMessage;
	}

	/**
	 * @param forwardToCheckPage
	 *          The forwardToCheckPage to set.
	 */
	public void setForwardToCheckPage(boolean forwardToCheckPage) {
		this._forwardToCheckPage = forwardToCheckPage;
	}

	public void setMaxAfterCareAge(int age) {
		_maxAge = age;
	}

	/**
	 * @param useCheckBoxForAfterSchoolCare
	 *          The useCheckBoxForAfterSchoolCare to set.
	 */
	public void setUseCheckBoxForAfterSchoolCare(boolean useCheckBoxForAfterSchoolCare) {
		this._useCheckBoxForAfterSchoolCare = useCheckBoxForAfterSchoolCare;
	}

	/**
	 * @param showChildCareTypes
	 *          The showChildCareTypes to set.
	 */
	public void setShowChildCareTypes(boolean showChildCareTypes) {
		this._showChildCareTypes = showChildCareTypes;
	}
	
	/**
	 * @param OutsideOfCommunePage
	 *          The OutsideOfCommunePage to set.
	 */
	public void setOutsideOfCommunePage(ICPage page) {
		this._outsideCommunePage = page;
	}
}