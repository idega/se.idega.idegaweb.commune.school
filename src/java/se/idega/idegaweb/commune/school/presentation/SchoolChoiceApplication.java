package se.idega.idegaweb.commune.school.presentation;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoCustodianFound;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.presentation.CitizenChildren;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.util.PIDChecker;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.builder.data.IBPage;
import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega</p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
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
	private String prmPreGrade = prefix + "pre_grd";
	private String prmMessage = prefix + "msg";
	private String prmAutoAssign = prefix + "aut_ass";
	private String prmSchoolChange = prefix + "scl_chg";
	private String prmSixYearCare = prefix + "six_car";
	private String prmLanguage = prefix + "cho_lng";
	private String prmAction = prefix + "snd_frm";
	private String prmChildId = CitizenChildren.getChildIDParameterName();
	private String prmParentId = CitizenChildren.getParentIDParameterName();
	private String prmForm = prefix + "the_frm";
	//private String prmCaseOwner = prefix+"cse_own";

	private boolean valSendCatalogue = false;
	private boolean valSixyearCare = false;
	private boolean valAutoAssign = false;
	private boolean valSchoolChange = false;
	private boolean valCustodiansAgree = false;
	private String valMessage = "";
	private String valLanguage = "";
	private int valFirstSchool = -1;
	private int valSecondSchool = -1;
	private int valThirdSchool = -1;
	private int valFirstArea = -1;
	private int valSecondArea = -1;
	private int valThirdArea = -1;
	private int valPreGrade = -1;
	private int valPreType = -1;
	private int valPreArea = -1;
	private int valPreSchool = -1;
	private int valType = -1;
	private int childId = -1;
	private int valCaseOwner = -1;
	private int valMethod = 1; // Citizen:1; Quick: 2; Automatic: 3
	private boolean showAgree = false;
	protected boolean quickAdmin = false;
	SchoolChoiceBusiness schBuiz;
	SchoolClassMember schoolClassMember = null;
	SchoolCommuneBusiness schCommBiz;
	SchoolClass schoolClass = null;
	School school = null;
	SchoolYear schoolYear = null;
	SchoolArea schoolArea = null;
	SchoolType schoolType = null;

	private boolean hasPreviousSchool = false;
	private boolean schoolChange = false;
	private boolean[] canApply = {false,false,false};
	private boolean hasChosen = false;
	private Age age;
	
	private IBPage childcarePage;
	
	private boolean isOwner = true;
	private User owner;
	private Date choiceDate;

	public void main(IWContext iwc) throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		df = DateFormat.getDateInstance(df.SHORT, iwc.getCurrentLocale());
		schBuiz = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
		canApply[0] = checkCanApply(iwc)[0];
		control(iwc);
	}

	public void control(IWContext iwc) throws Exception {
		//debugParameters(iwc);
		String ID = iwc.getParameter(prmChildId);
		if (iwc.isLoggedOn() && canApply[0]) {
			if (ID != null) {
				childId = Integer.parseInt(ID);
				userbuiz = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
				schCommBiz = (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);
				boolean custodiansAgree = true;

				User child = userbuiz.getUser(childId);
				if (child != null) {
					initSchoolChild(iwc, child);
					boolean hasChoosed = false;
					Collection currentChildChoices = null;
					parse(iwc);
					boolean saved = false;
					if (iwc.isParameterSet(prmAction) && iwc.getParameter(prmAction).equals("true")) {
						saved = saveSchoolChoice(iwc);
					}
					else {
						int CurrentSeasonID = ((Integer) schBuiz.getCurrentSeason().getPrimaryKey()).intValue();
						currentChildChoices = schBuiz.getSchoolChoiceHome().findByChildAndSeason(childId, CurrentSeasonID);
						if (!currentChildChoices.isEmpty()) {
							hasChosen = true;
							Iterator iter = currentChildChoices.iterator();
							int count = 1;
							while (iter.hasNext()) {
								SchoolChoice element = (SchoolChoice) iter.next();
								School school = element.getChosenSchool();
								int schoolID = ((Integer)school.getPrimaryKey()).intValue();
								int areaID = school.getSchoolAreaId();
								
								if (count == 1) {
									valFirstSchool = schoolID;
									valFirstArea = areaID;
									valSixyearCare = element.getKeepChildrenCare();
									if (element.getLanguageChoice() != null)
										valLanguage = element.getLanguageChoice();
									count++;
									if (!hasPreviousSchool) {
										initSchoolFromChoice(element);
									}
									owner = element.getOwner();
									choiceDate = element.getCreated();
									isOwner = iwc.getCurrentUser().equals(owner);
									custodiansAgree = element.getCustodiansAgree();
								}
								else if (count == 2) {
									valSecondSchool = schoolID;
									valSecondArea = areaID;
									count++;
								}
								else {
									valThirdSchool = schoolID;
									valThirdArea = areaID;
									count++;
								}
							}	
						}
					}
					
					if (hasPreviousSchool)
						valType = 4;
					else
						valType = 5;
						
					schoolTypes = getSchoolTypes(iwc, "SCHOOL");
					if (saved) {
						if (valSixyearCare && childcarePage != null) {
							iwc.setSessionAttribute(CitizenChildren.getChildIDParameterName(), new Integer(childId));
							iwc.forwardToIBPage(getParentPage(), childcarePage);
						}
						else
							add(getSchoolChoiceAnswer(iwc, child));
					}
					else if (hasChoosed) {
						add(getAlreadyChosenAnswer(iwc, child, currentChildChoices));
					}
					else {
						if (custodiansAgree || isOwner)
							add(getSchoolChoiceForm(iwc, child));
						else 
							add(getLocalizedHeader("school.cannot_alter_choice", "You cannot alter the school choice already made."));
					}
				}
			}
			else
				add(getLocalizedHeader("school.no_student_id_provided", "No student provided"));
		}
		else if (!iwc.isLoggedOn())
			add(getLocalizedHeader("school.need_to_be_logged_on", "You need to log in"));
		else if (!canApply[0])
			add(getLocalizedHeader("school_choice.last_date_expired", "Time limits to apply expired"));
	}

	private boolean saveSchoolChoice(IWContext iwc) {
		try {
			schBuiz.createSchoolChoices(valCaseOwner, childId, valType, valPreSchool, valFirstSchool, valSecondSchool, valThirdSchool, valPreGrade, valMethod, -1, -1, valLanguage, valMessage, valSchoolChange, valSixyearCare, valAutoAssign, valCustodiansAgree, valSendCatalogue);
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
		valAutoAssign = true;
		valSchoolChange = iwc.isParameterSet(prmSchoolChange);
		valCustodiansAgree = iwc.isParameterSet(prmFirstSchool) ? Boolean.valueOf(iwc.getParameter(prmCustodiansAgree)).booleanValue() : false;
		valMessage = iwc.getParameter(prmMessage);
		valLanguage = iwc.getParameter(prmLanguage);
		valFirstSchool = iwc.isParameterSet(prmFirstSchool) ? Integer.parseInt(iwc.getParameter(prmFirstSchool)) : -1;
		valSecondSchool = iwc.isParameterSet(prmSecondSchool) ? Integer.parseInt(iwc.getParameter(prmSecondSchool)) : -1;
		valThirdSchool = iwc.isParameterSet(prmThirdSchool) ? Integer.parseInt(iwc.getParameter(prmThirdSchool)) : -1;
		valFirstArea = iwc.isParameterSet(prmFirstArea) ? Integer.parseInt(iwc.getParameter(prmFirstArea)) : -1;
		valSecondArea = iwc.isParameterSet(prmSecondArea) ? Integer.parseInt(iwc.getParameter(prmSecondArea)) : -1;
		valThirdArea = iwc.isParameterSet(prmThirdArea) ? Integer.parseInt(iwc.getParameter(prmThirdArea)) : -1;
		valPreGrade = iwc.isParameterSet(prmPreGrade) ? Integer.parseInt(iwc.getParameter(prmPreGrade)) : -1;
		valPreType = iwc.isParameterSet(prmPreType) ? Integer.parseInt(iwc.getParameter(prmType)) : -1;
		valPreArea = iwc.isParameterSet(prmPreArea) ? Integer.parseInt(iwc.getParameter(prmPreArea)) : -1;
		valPreSchool = iwc.isParameterSet(prmPreSchool) ? Integer.parseInt(iwc.getParameter(prmPreSchool)) : -1;
		valType = iwc.isParameterSet(prmType) ? Integer.parseInt(iwc.getParameter(prmType)) : -1;
		valCaseOwner = iwc.isParameterSet(prmParentId) ? Integer.parseInt(iwc.getParameter(prmParentId)) : -1;
		if (!quickAdmin) {
			valCaseOwner = iwc.getUserId();
		}
		else {
			valMethod = 2;
		}
	}

	public PresentationObject getSchoolChoiceForm(IWContext iwc, User child) throws java.rmi.RemoteException {
		Form myForm = new Form();
		myForm.setName(prmForm);
		myForm.setOnSubmit("return checkApplication()");
		
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		myForm.add(T);
		int row = 1;
		
		if (!isOwner) {
			T.add(getAlterChoiceInfo(iwc), 1, row++);
			T.setHeight(row++, 12);
		}
		T.add(getCurrentSchoolSeasonInfo(iwc), 1, row++);
		T.setHeight(row++, 12);
		T.add(getChildInfo(iwc, child), 1, row++);
		T.setHeight(row++, 12);
		T.add(getCurrentSchool(iwc, child), 1, row++);
		T.setHeight(row++, 12);
		T.add(getChoiceSchool(iwc, child), 1, row++);
		T.setHeight(row++, 12);
		T.add(getMessagePart(iwc), 1, row++);
		T.setHeight(row++, 12);

		SubmitButton button = (SubmitButton) getButton(new SubmitButton("submit",localize("school_choice.ready", "Ready")));
		
		T.add(button, 1, row++);
		T.add(new HiddenInput(prmAction, "false"));
		T.add(new HiddenInput(prmChildId, child.getPrimaryKey().toString()));

		Page p = this.getParentPage();
		if (p != null) {
			Script S = p.getAssociatedScript();
			Script F = new Script();
			S.addFunction("initFilter", getInitFilterScript());
			S.addFunction("checkApplication", getSchoolCheckScript());
			S.addFunction("setSelected", getSetSelectedScript());

			S.addFunction("changeFilter", getFilterScript(iwc));
			S.addFunction("changeFilter2", getFilterScript2(iwc));
			if (valPreType > 0 || valPreArea > 0 || valPreSchool > 0) {
				p.setOnLoad(getInitFilterCallerScript(iwc, prmPreType, prmPreArea, prmPreSchool, valPreType, valPreArea, valPreSchool));
			}
			if (valType > 0) {
				if (valFirstArea > 0 || valFirstSchool > 0)
					p.setOnLoad(getInitFilterCallerScript(iwc, prmType, prmFirstArea, prmFirstSchool, valType, valFirstArea, valFirstSchool));
				if (valSecondArea > 0 || valSecondSchool > 0)
					p.setOnLoad(getInitFilterCallerScript(iwc, prmType, prmSecondArea, prmSecondSchool, valType, valSecondArea, valSecondSchool));
				if (valThirdArea > 0 || valThirdSchool > 0)
					p.setOnLoad(getInitFilterCallerScript(iwc, prmType, prmThirdArea, prmThirdSchool, valType, valThirdArea, valThirdSchool));
			}
			T.add(F, 1, T.getColumns());
			if (school != null && hasChosen) {
				Script initScript = new Script();

				String initFunction = (getInitFilterCallerScript(iwc, prmType, prmFirstArea, prmFirstSchool, ((Integer) schoolType.getPrimaryKey()).intValue(), ((Integer) schoolArea.getPrimaryKey()).intValue(), ((Integer) school.getPrimaryKey()).intValue()));

				initScript.addFunction("sch_init", initFunction);
				myForm.add(initScript);
			}
		}
		
		if (this.schoolChange) {
			if (!canApply[2])
				return getSmallHeader(localize("school_choice.not_possible_to_change_school","It is not possible to change school until after the 22nd of February."));
		}
		return myForm;
	}

	public PresentationObject getSchoolChoiceAnswer(IWContext iwc, User child) throws java.rmi.RemoteException {
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
		T.setHeight (row++, 12);
		T.add (getUserHomePageLink (iwc), 1, row++);

		return T;
	}

	public PresentationObject getAlreadyChosenAnswer(IWContext iwc, User child, Collection choices) throws java.rmi.RemoteException {
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

	public PresentationObject getAlterChoiceInfo(IWContext iwc) throws RemoteException {
		Table T = new Table();
		if (owner != null && choiceDate != null) {
			Object[] arguments = { owner.getName(), new IWCalendar(choiceDate).getLocaleDate(iwc.getCurrentLocale(), IWCalendar.SHORT) };
			String message = iwrb.getLocalizedString("school_choice.school_choice_already_made", "The school choice has already been done by {0} on {1}.");
			Text t = getHeader(MessageFormat.format(message, arguments));
			t.setFontColor("FF0000");
			T.add(t, 1, 1);
		}
		return T;
	}

	public PresentationObject getCurrentSchoolSeasonInfo(IWContext iwc) throws RemoteException {
		SchoolChoiceBusiness choiceBean = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
		SchoolSeason season = null;
		try {
			season = choiceBean.getCurrentSeason();
		}
		catch (RemoteException e) {
		}
		catch (FinderException e) {
		}
		Table T = new Table();
		if (season != null) {
			String message = iwrb.getLocalizedString("school_choice.last_date_for_choice_is", "Last date to choose is");
			String date = df.format(season.getSchoolSeasonDueDate());
			Text t = getHeader(message + " " + date);
			if (canApply[1])
				t.setFontColor("FF0000");
			T.add(t, 1, 1);

		}
		return T;

	}

	private void initSchoolChild(IWContext iwc, User child) {

		try {
			Date d = child.getDateOfBirth();
			if (d == null) {
				if (child.getPersonalID() != null) {
					d = PIDChecker.getInstance().getDateFromPersonalID(child.getPersonalID());
				}
				if (d == null)
					d = new Date();
			}
			age = new Age(d);
			SchoolSeason previousSeason = schCommBiz.getPreviousSchoolSeason(schBuiz.getCurrentSeason());
			schoolClassMember = schBuiz.getSchoolBusiness().getSchoolClassMemberHome().findByUserAndSeason(child, previousSeason);
			schoolClass = schBuiz.getSchoolBusiness().getSchoolClassHome().findByPrimaryKey(new Integer(schoolClassMember.getSchoolClassId()));
			school = schBuiz.getSchool(schoolClass.getSchoolId());
			if (school != null)
				this.hasPreviousSchool = true;
			schoolArea = schBuiz.getSchoolBusiness().getSchoolAreaHome().findByPrimaryKey(new Integer(school.getSchoolAreaId()));
			Collection Stypes = school.findRelatedSchoolTypes();
			if (!Stypes.isEmpty())
				schoolType = (SchoolType) Stypes.iterator().next();
			schoolYear = schBuiz.getSchoolBusiness().getSchoolYearHome().findByPrimaryKey(new Integer(schoolClass.getSchoolYearId()));
		}
		catch (Exception e) {
		}
	}
	
	private void initSchoolFromChoice(SchoolChoice choice) {
		try {
			school = schBuiz.getSchool(choice.getChosenSchoolId());
			schoolArea = schBuiz.getSchoolBusiness().getSchoolAreaHome().findByPrimaryKey(new Integer(school.getSchoolAreaId()));
			schoolYear = schCommBiz.getSchoolYear(choice.getGrade());
			schoolType = schBuiz.getSchoolBusiness().getSchoolTypeHome().findByPrimaryKey(new Integer(choice.getSchoolTypeId()));
		}
		catch (Exception e) {
		}
	}

	private PresentationObject getChildInfo(IWContext iwc, User child) throws java.rmi.RemoteException {
		Table table = new Table();
		table.setColumns(3);
		table.setWidth(1, "100");
		table.setWidth(2, "8");
		table.setCellpadding(2);
		table.setCellspacing(0);

		table.add(getSmallHeader(iwrb.getLocalizedString("school.name", "Name")+":"), 1, 1);
		table.add(getSmallHeader(iwrb.getLocalizedString("school.personal_id", "Personal ID")+":"), 1, 2);
		table.add(getSmallHeader(iwrb.getLocalizedString("school.address", "Address")+":"), 1, 3);

		table.add(getSmallText(child.getNameLastFirst(true)), 3, 1);
		String personalID = PersonalIDFormatter.format(child.getPersonalID(), iwc.getApplication().getSettings().getApplicationLocale());
		table.add(getSmallText(personalID), 3, 2);

		try {
			Address address = userbuiz.getUsersMainAddress(child);
			table.add(getSmallText(address.getStreetAddress() + ", " + address.getPostalAddress()), 3, 3);
		}
		catch (Exception ex) {
		}

		MemberFamilyLogic mlogic = (MemberFamilyLogic) IBOLookup.getServiceInstance(iwc, MemberFamilyLogic.class);
		try {
			Collection parents = mlogic.getCustodiansFor(child);
			Iterator iter = parents.iterator();
			String address = "";
			boolean caseOwning = true;
			boolean parentsSeparated = false;
			while (iter.hasNext()) {
				User parent = (User) iter.next();
				
				Address parAddress	=	userbuiz.getUsersMainAddress(parent);
				String sParAddress = null;
				if(parAddress!=null){
					sParAddress = parAddress.getStreetAddress();
				}
				else{
					sParAddress = "";
				}
				
				if (address == null) {
					address = sParAddress;	
				}
				else {
					if (!parentsSeparated)
						parentsSeparated = address.equals(sParAddress);	
				}

				if (quickAdmin && caseOwning && valCaseOwner == -1) {
					valCaseOwner = ((Integer) parent.getPrimaryKey()).intValue();
					caseOwning = false;
				}
			}
			if (valCaseOwner != -1)
				table.add(new HiddenInput(prmParentId, String.valueOf(valCaseOwner)));
			showAgree = parentsSeparated;
		}
		catch (NoCustodianFound ex) {
			ex.printStackTrace();
			table.add(getSmallErrorText(iwrb.getLocalizedString("school.no_registered_custodians", "No registered custodians")), 3, 4);
		}
	
		return table;
	}

	private PresentationObject getCurrentSchool(IWContext iwc, User child) throws java.rmi.RemoteException {
		if (age.getYears() <= 6) {
			hasPreviousSchool = true;
			PresentationObjectContainer container = new PresentationObjectContainer();
			container.add(new HiddenInput(prmPreGrade, String.valueOf(5)));
			container.add(new HiddenInput(prmPreSchool, "-1"));
			return container;
		}
		else {
			Table table = new Table();
			table.setColumns(3);
			table.setCellpadding(2);
			table.setCellspacing(0);
			table.mergeCells(1, 1, table.getColumns(), 1);

			table.add(getHeader(iwrb.getLocalizedString("school.child_is_now_in", "Child is now in :")), 1, 1);

			if (schoolClassMember != null || hasChosen) {
				table.add(getSmallHeader(iwrb.getLocalizedString("school.school_name", "School name")+":"), 1, 2);
				table.add(getSmallHeader(iwrb.getLocalizedString("school.school_class", "School class")+":"), 1, 3);
				table.add(getSmallHeader(iwrb.getLocalizedString("school.school_year", "School year")+":"), 1, 4);

				if (school != null)
					table.add(getSmallText(school.getName()), 3, 2);
				if (schoolClass != null)
					table.add(getSmallText(schoolClass.getName()), 3, 3);
				else
					table.add(getSmallText("-"), 3, 3);
				if (schoolYear != null) {
					table.add(getSmallText(schoolYear.getName()), 3, 4);
					table.add(new HiddenInput(prmPreGrade, String.valueOf(schoolYear.getSchoolYearAge())), 4, 4);
				}
				table.add(new HiddenInput(prmPreSchool, school.getPrimaryKey().toString()), 4, 4);
			}
			else {
				table.add(getSmallHeader(iwrb.getLocalizedString("school.school_type", "Type")+":"), 1, 2);
				table.add(getSmallHeader(iwrb.getLocalizedString("school.school_area", "Area")+":"), 1, 3);
				table.add(getSmallHeader(iwrb.getLocalizedString("school.school_name", "School name")+":"), 1, 4);
				table.add(getSmallHeader(iwrb.getLocalizedString("school.grade", "Grade")+":"), 1, 5);
			
				DropdownMenu drpTypes = getTypeDrop(iwc, prmPreType);
				drpTypes.setOnChange(getFilterCallerScript(iwc, prmPreType, prmPreArea, prmPreSchool, 1));
				
				DropdownMenu drpAreas = (DropdownMenu) getStyledInterface(new DropdownMenu(prmPreArea));
				drpAreas.addMenuElementFirst("-1", iwrb.getLocalizedString("school.area", "School area..........."));
				drpAreas.setOnChange(getFilterCallerScript(iwc, prmPreType, prmPreArea, prmPreSchool, 2));

				DropdownMenu drpSchools = (DropdownMenu) getStyledInterface(new DropdownMenu(prmPreSchool));
				drpSchools.addMenuElementFirst("-1", iwrb.getLocalizedString("school.school", "School................"));
				
				DropdownMenu drpGrade = (DropdownMenu) getStyledInterface(new DropdownMenu(prmPreGrade));
				drpGrade.addMenuElement("-1", "");
				Collection coll = getSchoolYears(iwc);
				if (coll != null) {
					Iterator iter = coll.iterator();
					while (iter.hasNext()) {
						SchoolYear element = (SchoolYear) iter.next();
						drpGrade.addMenuElement(element.getSchoolYearAge(), element.getName());
					}
				}
				drpGrade.setSelectedElement(String.valueOf(valPreGrade));

				table.add(drpTypes, 3, 2);
				table.add(drpAreas, 3, 3);
				table.add(drpSchools, 3, 4);
				table.add(drpGrade, 3, 5);
			}
			table.setWidth(1, "100");
			table.setWidth(2, "8");

			return table;
		}
	}

	private DropdownMenu getTypeDrop(IWContext iwc, String name) throws java.rmi.RemoteException {
		DropdownMenu drp = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		drp.addMenuElement("-1", iwrb.getLocalizedString("school.school_type_select", "School type select"));
		Iterator iter = schoolTypes.iterator();
		boolean canChoose = true;
		
		while (iter.hasNext()) {
			SchoolType type = (SchoolType) iter.next();
			if (age.getYears() <= type.getMaxSchoolAge())
				canChoose = true;
			else
				canChoose = false;
				
			if (canChoose)
				drp.addMenuElement(type.getPrimaryKey().toString(), localize(schCommBiz.getLocalizedSchoolTypeKey(type),type.getSchoolTypeName()));
		}
		
		return drp;
	}

	/**
	 * Method getChoiceSchool.
	 * @param iwc
	 * @param child
	 * @return PresentationObject
	 * @throws RemoteException
	 */
	private PresentationObject getChoiceSchool(IWContext iwc, User child) throws java.rmi.RemoteException {
		Table table = new Table();
		table.setCellpadding(2);
		table.setCellspacing(0);
		table.setColumns(5);
		table.mergeCells(1, 1, table.getColumns(), 1);
		table.add(getHeader(iwrb.getLocalizedString("school.choice_for_schoolyear", "Choice for the schoolyear")), 1, 1);

		DropdownMenu typeDrop = getTypeDrop(iwc, prmType);
		typeDrop.setOnChange(getFilterCallerScript(iwc, prmType, prmFirstArea, prmFirstSchool, 1));

		CheckBox chkChildCare = getCheckBox(prmSixYearCare, "true");
		chkChildCare.setChecked(valSixyearCare);

		DropdownMenu txtLangChoice = (DropdownMenu) getStyledInterface(new DropdownMenu(prmLanguage));
		txtLangChoice.addMenuElement("school.language_german", localize("school.language_german","German"));
		txtLangChoice.addMenuElement("school.language_french", localize("school.language_french","French"));
		txtLangChoice.addMenuElement("school.language_spanish", localize("school.language_spanish","Spanish"));
		txtLangChoice.addMenuElement("school.language_swedish_english", localize("school.language_swedish_english","Swedish/English"));
		if (valLanguage != null)
			txtLangChoice.setSelectedElement(valLanguage);

		DropdownMenu drpFirstArea = getDropdown(iwc, prmFirstArea, null, prmType, prmFirstArea, prmFirstSchool, 2, iwrb.getLocalizedString("school.area_first", "School Area...................."));
		DropdownMenu drpFirstSchool = (DropdownMenu) getStyledInterface(new DropdownMenu(prmFirstSchool));
		drpFirstSchool.addMenuElementFirst("-1", iwrb.getLocalizedString("school.school_first", "School........................."));

		int row = 2;
		table.add(getSmallHeader(iwrb.getLocalizedString("school.school_type", "SchoolType")+":"), 1, row);
		table.add(typeDrop, 3, row++);

		table.add(getSmallHeader(iwrb.getLocalizedString("school.first_choice", "First choice")+":"), 1, row);
		table.add(drpFirstArea, 3, row);
		table.add(drpFirstSchool, 5, row++);

		if (!this.schoolChange) {
			typeDrop.setOnChange(getFilterCallerScript(iwc, prmType, prmSecondArea, prmSecondSchool, 1));
			typeDrop.setOnChange(getFilterCallerScript(iwc, prmType, prmThirdArea, prmThirdSchool, 1));

			DropdownMenu drpSecondArea = getDropdown(iwc, prmSecondArea, null, prmType, prmSecondArea, prmSecondSchool, 2, iwrb.getLocalizedString("school.area_second", "School Area...................."));
			DropdownMenu drpSecondSchool = (DropdownMenu) getStyledInterface(new DropdownMenu(prmSecondSchool));
			drpSecondSchool.addMenuElementFirst("-1", iwrb.getLocalizedString("school.school_second", "School........................."));

			DropdownMenu drpThirdArea = getDropdown(iwc, prmThirdArea, null, prmType, prmThirdArea, prmThirdSchool, 2, iwrb.getLocalizedString("school.area_third", "School Area...................."));
			DropdownMenu drpThirdSchool = (DropdownMenu) getStyledInterface(new DropdownMenu(prmThirdSchool));
			drpThirdSchool.addMenuElementFirst("-1", iwrb.getLocalizedString("school.school_third", "School........................."));

			table.add(getSmallHeader(iwrb.getLocalizedString("school.second_choice", "Second choice")+":"), 1, row);
			table.add(drpSecondArea, 3, row);
			table.add(drpSecondSchool, 5, row++);
	
			table.add(getSmallHeader(iwrb.getLocalizedString("school.third_choice", "Third choice")+":"), 1, row);
			table.add(drpThirdArea, 3, row);
			table.add(drpThirdSchool, 5, row++);
		}
		
		if (schoolYear != null && schoolYear.getSchoolYearAge() >= 12) {
			table.setHeight(row++, 5);
			table.add(getSmallHeader(iwrb.getLocalizedString("school.six_year_language", "Language")+":"), 1, row);
			table.add(txtLangChoice, 3, row);
			table.mergeCells(3, row, 5, row++);
		}
		
		if (age.getYears() <= 10) {
			table.setHeight(row++, 5);
			table.mergeCells(1, row, 5, row);
			table.setWidth(1, row, Table.HUNDRED_PERCENT);
			table.add(chkChildCare, 1, row);
			table.add(getSmallHeader(Text.NON_BREAKING_SPACE + iwrb.getLocalizedString("school.child_care_requested", "Interested in after school child care")), 1, row);
		}
		
		table.add(new HiddenInput(prmCustodiansAgree,String.valueOf(showAgree)), 1, row);
				
		table.setWidth(1, "100");
		table.setWidth(2, "8");
		table.setWidth(4, "3");

		return table;
	}
	
	private DropdownMenu getDropdown(IWContext iwc, String name, String value, String type, String area, String school, int index, String firstElement) {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(name));
		menu.addMenuElementFirst("-1", firstElement);
		menu.setOnChange(getFilterCallerScript(iwc, type, area, school, index));
		return menu;
	}

	private PresentationObject getMessagePart(IWContext iwc) throws java.rmi.RemoteException {
		Table table = new Table(1, 2);
		table.setCellpadding(2);
		table.setCellspacing(0);

		TextArea message = (TextArea) getStyledInterface(new TextArea(prmMessage));
		message.setRows(6);
		message.setColumns(65);
		
		if(schoolChange)
			table.add(getSmallHeader(localize("school.application_change_required_reason", "Reason for schoolchange (Required)")), 1, 1);
		else
		table.add(getSmallHeader(localize("school.application_extra_info", "Extra info")), 1, 1);
		table.add(message, 1, 2);

		return table;
	}

	private Collection getSchoolTypes(IWContext iwc, String category) {
		try {
			return schCommBiz.getSchoolBusiness().findAllSchoolTypesInCategory(category);
		}
		catch (Exception ex) {

		}
		return null;
	}

	private Collection getSchoolYears(IWContext iwc) {
		try {
			return schCommBiz.getSchoolBusiness().findAllSchoolYears();
		}
		catch (Exception e) {
		}
		return null;
	}

	private Collection getSchools(IWContext iwc, int area, int type) {
		try {
			SchoolBusiness sBuiz = (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
			return sBuiz.findAllSchoolsByAreaAndType(area, type);

		}
		catch (Exception ex) {
			ex.printStackTrace();
			return new java.util.Vector();
		}
	}

	private Collection getSchoolAreasWithType(IWContext iwc, int type) {
		try {
			return schCommBiz.getSchoolBusiness().findAllSchoolAreasByType(type);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private Collection getSchoolByAreaAndType(IWContext iwc, int area, int type) {
		try {
			SchoolBusiness sBuiz = (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
			return sBuiz.findAllSchoolsByAreaAndType(area, type);

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private String getFilterCallerScript(IWContext iwc, String typeName, String areaName, String schoolName, int Type) {
		StringBuffer script = new StringBuffer("changeFilter(");
		script.append(Type);
		script.append(",");
		script.append("findObj('").append(typeName).append("')");
		script.append(",");
		script.append("findObj('").append(areaName).append("')");
		script.append(",");
		script.append("findObj('").append(schoolName).append("')");
		script.append(")");
		return script.toString();
	}

	private String getInitFilterCallerScript(IWContext iwc, String typeName, String areaName, String schoolName, int typeSel, int areaSel, int schoolSel) {
		StringBuffer script = new StringBuffer("initFilter(");
		script.append("findObj('").append(typeName).append("')");
		script.append(",");
		script.append("findObj('").append(areaName).append("')");
		script.append(",");
		script.append("findObj('").append(schoolName).append("')");
		script.append(",");
		script.append(typeSel);
		script.append(",");
		script.append(areaSel);
		script.append(",");
		script.append(schoolSel);
		script.append(")");
		return script.toString();
	}

	private String getFilterScript(IWContext iwc) throws java.rmi.RemoteException {
		StringBuffer s = new StringBuffer();
		s.append("function changeFilter(index,type,area,school){").append(" \n\t");
		s.append("changeFilter2(index,type,area,school,-1);").append("\n").append("}");
		return s.toString();
	}
 	
 	private String getFilterScript2(IWContext iwc) throws java.rmi.RemoteException {
		StringBuffer s = new StringBuffer();
		s.append("function changeFilter2(index,type,area,school,selectedValue){").append(" \n\t");
		s.append("var typeSelect = type;").append(" \n\t");
		s.append("var areaSelect = area;").append(" \n\t");
		s.append("var schoolSelect = school;").append(" \n\t");
		s.append("var selected = 0;").append(" \n\t");
		s.append("if(index == 1){").append(" \n\t\t");
		s.append("selected = typeSelect.options[typeSelect.selectedIndex].value;").append("\n\t\t");
		s.append("areaSelect.options.length = 0;").append("\n\t\t");
		s.append("schoolSelect.options.length = 0;").append("\n\t\t");
		s.append("areaSelect.options[areaSelect.options.length] = new Option(\"");
		s.append(iwrb.getLocalizedString("choose_area", "Choose Area")).append("\",\"-1\",true,true);").append("\n\t\t");
		//s.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"Veldu sk?la\",\"-1\",true,true);").append("\n\t");
		s.append("}else if(index == 2){").append(" \n\t\t");
		s.append("selected = areaSelect.options[areaSelect.selectedIndex].value;").append("\n\t\t");
		s.append("if (selectedValue > -1) \n\t\t\tselected = selectedValue;").append("\n\t\t");
		s.append("schoolSelect.options.length = 0;").append("\n\t\t");
		s.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"");
		s.append(iwrb.getLocalizedString("choose_school", "Choose School")).append("\",\"-1\",true,true);").append("\n\t");
		s.append("} else if(index == 3){").append("\n\t\t");
		s.append("selected = schoolSelect.options[schoolSelect.selectedIndex].value;").append("\n\t");
		s.append("if (selectedValue > -1) \n\t\t\tselected = selectedValue;").append("\n\t\t");
		s.append("}").append("\n\t\t\t");

		// Data Filling ::

		StringBuffer t = new StringBuffer("if(index==1){\n\t");
		StringBuffer a = new StringBuffer("else if(index==2){\n\t");
		StringBuffer c = new StringBuffer("else if(index==3){\n\t");

		Collection Types = this.schoolTypes;
		if (Types != null && !Types.isEmpty()) {
			Iterator iter = Types.iterator();
			SchoolType type;
			SchoolArea area;
			School school;
			Collection areas;
			Collection schools;
			// iterate through schooltypes
			while (iter.hasNext()) {
				type = (SchoolType) iter.next();

				Integer tPK = (Integer) type.getPrimaryKey();
				//System.err.println("checking type "+tPK.toString());
				areas = getSchoolAreasWithType(iwc, tPK.intValue());
				if (areas != null && !areas.isEmpty()) {
					Iterator iter2 = areas.iterator();
					t.append("if(selected == \"").append(tPK.toString()).append("\"){").append("\n\t\t");

					Hashtable aHash = new Hashtable();

					// iterate through areas whithin types
					while (iter2.hasNext()) {
						area = (SchoolArea) iter2.next();
						Integer aPK = (Integer) area.getPrimaryKey();
						// System.err.println("checking area "+aPK.toString());
						if (!aHash.containsKey(aPK)) {
							aHash.put(aPK, aPK);
							schools = getSchoolByAreaAndType(iwc, aPK.intValue(), tPK.intValue());
							if (schools != null) {
								Iterator iter3 = schools.iterator();
								a.append("if(selected == \"").append(aPK.toString()).append("\"){").append("\n\t\t");
								Hashtable hash = new Hashtable();
								// iterator through schools whithin area and type
								while (iter3.hasNext()) {
									school = (School) iter3.next();
									String pk = school.getPrimaryKey().toString();
									//System.err.println("checking school "+pk.toString());
									if (!hash.containsKey(pk)) {
										a.append("schoolSelect.options[schoolSelect.options.length] = new Option(\"");
										a.append(school.getSchoolName()).append("\",\"");
										a.append(pk).append("\");\n\t\t");
										hash.put(pk, pk);
									}

								}
								a.append("}\n\t\t");
							}
						}
						else {
							System.err.println("shools empty");
						}
						t.append("areaSelect.options[areaSelect.options.length] = new Option(\"");
						t.append(area.getSchoolAreaName()).append("\",\"");
						t.append(area.getPrimaryKey().toString()).append("\");").append("\n\t\t");
						;

					}
					t.append("}\n\t");
				}
				else
					System.err.println("areas empty");
			}
		}
		else
			System.err.println("types empty");

		s.append("\n\n");

		t.append("\n\t }");
		a.append("\n\t }");
		c.append("\n\t }");

		s.append(t.toString());
		s.append(a.toString());
		s.append(c.toString());
		s.append("\n}");

		return s.toString();
	}

	public String getInitFilterScript() {
		StringBuffer s = new StringBuffer();
		s.append("function initFilter(type,area,school,type_sel,area_sel,school_sel){ \n  ");
		s.append("changeFilter2(1,type,area,school,type_sel); \n  ");
		s.append("setSelected(type,type_sel); \n  ");
		s.append("changeFilter2(2,type,area,school,area_sel); \n  ");
		s.append("setSelected(area,area_sel); \n  ");
		s.append("changeFilter2(3,type,area,school,school_sel); \n ");
		s.append("setSelected(school,school_sel); \n}");
		return s.toString();
	}

	public String getSetSelectedScript() {
		StringBuffer s = new StringBuffer();
		s.append("function setSelected(input,selectedValue) { \n");
		s.append("\tif (input.length > 1) { \n");
		s.append("\t\tfor (var a = 0; a < input.length; a++) { \n");
		s.append("\t\t\tif (input.options[a].value == selectedValue) \n");
		s.append("\t\t\t\tinput.options[a].selected = true; \n");
		s.append("\t\t} \n ");
		s.append("\t} \n}");
		return s.toString();
	}

	public String getSchoolCheckScript() {
		StringBuffer s = new StringBuffer();
		s.append("\nfunction checkApplication(){\n\t");
		//s.append("\n\t\t alert('").append("checking choices").append("');");
		if (!this.hasPreviousSchool && !hasChosen)
			s.append("\n\t var currSchool = ").append("findObj('").append(prmPreSchool).append("');");
		s.append("\n\t var dropOne = ").append("findObj('").append(prmFirstSchool).append("');");
		if(!schoolChange){
			s.append("\n\t var dropTwo = ").append("findObj('").append(prmSecondSchool).append("');");
			s.append("\n\t var dropThree = ").append("findObj('").append(prmThirdSchool).append("');");
		}
		if (!this.hasPreviousSchool && !hasChosen)
			s.append("\n\t var gradeDrop = ").append("findObj('").append(prmPreGrade).append("');");
		s.append("\n\t var one = 0;");
		if(!schoolChange){
			s.append("\n\t var two = 0;");
			s.append("\n\t var three = 0;");
		}
		s.append("\n\t var year = 0;");
		s.append("\n\t var school = 0;");

		s.append("\n\n\t if (dropOne.selectedIndex > 0) one = dropOne.options[dropOne.selectedIndex].value;");
		if(!schoolChange){
			s.append("\n\t if (dropTwo.selectedIndex > 0) two = dropTwo.options[dropTwo.selectedIndex].value;");
			s.append("\n\t if (dropThree.selectedIndex > 0) three = dropThree.options[dropThree.selectedIndex].value;");
		}
		if (!this.hasPreviousSchool)
			s.append("\n\t if (gradeDrop.selectedIndex > 0) year = gradeDrop.options?gradeDrop.options[gradeDrop.selectedIndex].value:document.sch_app_the_frm.elements['" + prmPreGrade + "'].value;");
		if (!this.hasPreviousSchool)
			s.append("\n\t if (currSchool.selectedIndex > 0) school = currSchool.options?currSchool.options[currSchool.selectedIndex].value:document.sch_app_the_frm.elements['" + prmPreSchool + "'].value;");

		// current school check
		if (!this.hasPreviousSchool) {
			s.append("\n\n\t if(school <= 0){");
			String msg1 = iwrb.getLocalizedString("school_choice.must_set_current_school", "You must provide current shool");
			s.append("\n\t\t\t alert('").append(msg1).append("');");
			s.append("\n\t\t return false;");
			s.append("\n\t\t ");
			s.append("\n\t }");
		}

		// year check
		s.append("\n\t if(year <= 0 && school > 0){");
		String msg2 = iwrb.getLocalizedString("school_choice.must_set_grade", "You must provide current shool year");
		s.append("\n\t\t\t alert('").append(msg2).append("');");
		s.append("\n\t\t return false;");
		s.append("\n\t\t ");
		s.append("\n\t }");

		// schoolchoices checked
		
		if(!schoolChange){
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
		else{
			s.append("\n\t var reason =  findObj('").append(prmMessage).append("';");
			s.append("\n\t if(reason.value.length<=10){");
			String msg = iwrb.getLocalizedString("school_school.change_must_give_reason", "You must give a reason for  the change !");
			s.append("\n\t\t alert('").append(msg).append("');");
			s.append("\n\t\t return false;");
			s.append("\n\t }");
		}
		s.append("\n\t\t findObj('").append(prmAction).append("').value='true';");
		s.append("\n\t return true;");
		s.append("\n}\n");
		return s.toString();
	}

	private boolean[] checkCanApply(IWContext iwc) throws RemoteException {
		boolean[] checkCanApply = {true,true,true};
		try {
			SchoolSeason season = schBuiz.getCurrentSeason();
			if (season != null) {
				IWPropertyList properties = iwc.getSystemProperties().getProperties("school_properties");
				String choiceStart = properties.getProperty("choice_start_date");
				String choiceEnd = properties.getProperty("choice_end_date");
				String choiceRed = properties.getProperty("choice_critical_date");
				String changeStart = properties.getProperty("school_change_start");
				
				IWTimestamp seasonStart = new IWTimestamp(season.getSchoolSeasonStart());
				IWTimestamp dateNow = new IWTimestamp();
				if (choiceStart != null && choiceEnd != null && choiceRed != null) {
					IWTimestamp start = new IWTimestamp(seasonStart);
					start.setDay(Integer.parseInt(choiceStart.substring(0, 2)));
					start.setMonth(Integer.parseInt(choiceStart.substring(3)));

					IWTimestamp end = new IWTimestamp(seasonStart);
					end.setDay(Integer.parseInt(choiceEnd.substring(0, 2)));
					end.setMonth(Integer.parseInt(choiceEnd.substring(3)));

					IWTimestamp red = new IWTimestamp(seasonStart);
					red.setDay(Integer.parseInt(choiceRed.substring(0, 2)));
					red.setMonth(Integer.parseInt(choiceRed.substring(3)));
					
					if (dateNow.isBetween(start, end))
						checkCanApply[0] = true;
					else
						checkCanApply[0] = false;
						
					if (red.isEarlierThan(dateNow))
						checkCanApply[1] = true;
						
					//temporary, for testing only...
					if (dateNow.getYear() <= 2002) {
						checkCanApply[0] = true;
						checkCanApply[1] = false;
					}
				}
				if (changeStart != null) {
					IWTimestamp change = new IWTimestamp(seasonStart);
					change.setDay(Integer.parseInt(changeStart.substring(0, 2)));
					change.setMonth(Integer.parseInt(changeStart.substring(3)));
					if (dateNow.isLaterThan(change))
						checkCanApply[2] = true;
						
					if (dateNow.getYear() <= 2002)
						checkCanApply[2] = true;
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
	 * @param childcarePage The childcarePage to set
	 */
	public void setChildcarePage(IBPage childcarePage) {
		this.childcarePage = childcarePage;
	}

	private Link getUserHomePageLink (final IWContext iwc)
        throws RemoteException {
		final Text userHomePageText = new Text
                (iwrb.getLocalizedString (prefix + "go_back_to_my_page",
                                          "Tillbaka till Min sida"));
 		final UserBusiness userBusiness = (UserBusiness)
                IBOLookup.getServiceInstance (iwc, UserBusiness.class);
        final User user = iwc.getCurrentUser ();
		final Link link = new Link (userHomePageText);
        link.setPage (userBusiness.getHomePageIDForUser (user));
		return (link);
	}
}
