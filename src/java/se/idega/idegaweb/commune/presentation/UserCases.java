package se.idega.idegaweb.commune.presentation;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.builder.data.IBPage;
import com.idega.business.IBOLookup;
import com.idega.core.user.data.User;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.Converter;
import com.idega.user.business.*;
import com.idega.user.data.Group;
import com.idega.util.*;
import java.text.DateFormat;
import java.util.*;
import javax.ejb.EJBException;
import se.cubecon.bun24.viewpoint.business.ViewpointBusiness;
import se.cubecon.bun24.viewpoint.data.Viewpoint;
import se.cubecon.bun24.viewpoint.presentation.ViewpointForm;
import se.idega.idegaweb.commune.business.CommuneCaseBusiness;
import se.idega.idegaweb.commune.message.business.*;
import se.idega.idegaweb.commune.message.data.*;

/**
 * Title: UserCases
 * Description: A class to view Cases for a User in the idegaWeb Commune application
 * Copyright:    Copyright idega Software (c) 2002
 * Company:	idega Software
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class UserCases extends CommuneBlock {
	private final static String IW_BUNDLE_IDENTIFIER
        = "se.idega.idegaweb.commune";
	private final static int ACTION_VIEW_CASE_LIST = 1;
	private final static String PARAM_CASE_ID = "USC_CASE_ID";
	private final static String PARAM_MANAGER_ID = ManagerView.PARAM_MANAGER_ID;
	private Table mainTable = null;
	private int manager_page_id = -1;
    private int viewpointPageId = -1;    

    public final static String MYCASES_KEY = "usercases.myCases";
    public final static String MYCASES_DEFAULT = "Mina ärenden";
    public final static String CASENUMBER_KEY = "usercases.caseNumber";
    public final static String CASENUMBER_DEFAULT = "Ärendenummer";
    public final static String CONCERNING_KEY = "usercases.concerning";
    public final static String CONCERNING_DEFAULT = "Avser";
    public final static String NAME_KEY = "usercases.name";
    public final static String NAME_DEFAULT = "Namn";
    public final static String DATE_KEY = "usercases.date";
    public final static String DATE_DEFAULT = "Datum";
    public final static String MANAGER_KEY = "usercases.manager";
    public final static String MANAGER_DEFAULT = "Handläggare";
    public final static String STATUS_KEY = "usercases.status";
    public final static String STATUS_DEFAULT = "Status";
    public final static String NOONGOINGCASES_KEY = "usercases.noOngoingCases";
    public final static String NOONGOINGCASES_DEFAULT = "Inga pågående ärenden";
    public final static String UNHANDLEDCASESINMYGROUPS_KEY
        = "usercases.unhandledCasesInMyGroups";
    public final static String UNHANDLEDCASESINMYGROUPS_DEFAULT
        = "Pågående ärenden i mina grupper";
    public final static String SUBJECT_KEY = "usercases.subject";
    public final static String SUBJECT_DEFAULT = "Rubrik";
    public final static String HANDLERGROUP_KEY = "usercases.handlerGroup";
    public final static String HANDLERGROUP_DEFAULT = "Handläggargrupp";

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
    
	public void main(IWContext iwc) {
		//this.setResourceBundle(getResourceBundle(iwc));
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW_CASE_LIST :
					viewCaseList(iwc);
					break;
			}
			super.add(mainTable);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}
    
	public void add(PresentationObject po) {
		if (mainTable == null) {
			mainTable = new Table();
			mainTable.setCellpadding(14);
			mainTable.setCellspacing(0);
			mainTable.setColor(getBackgroundColor());
			mainTable.setWidth(600);
		}
		mainTable.add(po);
	}
    
	private int parseAction(IWContext iwc) {
		int action = ACTION_VIEW_CASE_LIST;
		return action;
	}
    
	private void viewCaseList (IWContext iwc) throws Exception {
		add(new Break(2));
		add(getLocalizedHeader(MYCASES_KEY, MYCASES_DEFAULT));
		add(new Break(2));
		if (iwc.isLoggedOn()) {
			Collection cases = getCommuneCaseBusiness(iwc)
                    .getAllCasesDefaultVisibleForUser(iwc.getCurrentUser());
			if (cases != null & !cases.isEmpty()) {
                ColumnList messageList = new ColumnList(6);
				Form f = new Form();
				f.add(messageList);
				messageList.setBackroundColor("#e0e0e0");
				messageList.setHeader(localize(CASENUMBER_KEY,
                                               CASENUMBER_DEFAULT), 1);
				messageList.setHeader(localize(CONCERNING_KEY,
                                               CONCERNING_DEFAULT), 2);
				messageList.setHeader(localize(NAME_KEY, NAME_DEFAULT), 3);
				messageList.setHeader(localize(DATE_KEY, DATE_DEFAULT), 4);
				messageList.setHeader(localize(MANAGER_KEY, MANAGER_DEFAULT),
                                      5);
				messageList.setHeader(localize(STATUS_KEY, STATUS_DEFAULT), 6);
				Collection messages = getCaseBusiness(iwc).getAllCasesForUser
                        (Converter.convertToNewUser(iwc.getUser()));
				boolean isRead = false;
				if (cases != null) {
					for (Iterator i = cases.iterator(); i.hasNext();) {
                        try {
                            final Case useCase = (Case) i.next();
                            addCaseToMessageList (iwc, useCase, messageList);
                        } catch (Exception e) {
							add(e);
							e.printStackTrace();
						}
					}
				}
				// messageList.skip(6);
				add(f);
			} else {
				add(getSmallText(localize(NOONGOINGCASES_KEY,
                                          NOONGOINGCASES_DEFAULT)));
            }
        
            // 1. find my groups
            final GroupBusiness groupBusiness =
                    (GroupBusiness) IBOLookup.getServiceInstance
                    (iwc, GroupBusiness.class);
            final int userId
                    = ((Integer) iwc.getCurrentUser ().getPrimaryKey())
                    .intValue();
            final Collection groups
                    = groupBusiness.getAllGroupsNotDirectlyRelated
                    (userId, iwc);
            
            // 2. find unhandled viewpoints
            ViewpointBusiness viewpointBusiness
                    = (ViewpointBusiness) IBOLookup.getServiceInstance
                    (iwc, ViewpointBusiness.class);
            final Viewpoint [] viewpoints
                    = viewpointBusiness.findUnhandledViewpointsInGroups
                    ((Group []) groups.toArray (new Group [0]));
            
            // 3. display unhandled viewpoints
            if (viewpoints != null && viewpoints.length > 0) {
                add(new Break(2));
                add(getLocalizedHeader
                    (UNHANDLEDCASESINMYGROUPS_KEY,
                     UNHANDLEDCASESINMYGROUPS_DEFAULT));
                add(new Break(2));
                ColumnList messageList = new ColumnList(5);
				Form f = new Form();
				f.add(messageList);
				messageList.setBackroundColor("#e0e0e0");
                int col = 1;
				messageList.setHeader(localize(CASENUMBER_KEY,
                                               CASENUMBER_DEFAULT), col++);
				messageList.setHeader(localize(CONCERNING_KEY,
                                               CONCERNING_DEFAULT), col++);
				messageList.setHeader(localize(SUBJECT_KEY, SUBJECT_DEFAULT),
                                      col++);
				messageList.setHeader(localize(DATE_KEY, DATE_DEFAULT),
                                      col++);
				messageList.setHeader(localize(HANDLERGROUP_KEY,
                                               HANDLERGROUP_DEFAULT), col++);

                for (int i = 0; i < viewpoints.length; i++) {
                    addViewpointToMessageList (iwc, viewpoints [i],
                                               messageList);
                }                
				add(f);
            }
		}
	}
    
    private void addCaseToMessageList
        (final IWContext iwc, final Case useCase, final ColumnList messageList)
        throws Exception {
        
        DateFormat dateFormat = CustomDateFormat.getDateTimeInstance
                (iwc.getCurrentLocale());
        Date caseDate
                = new Date(useCase.getCreated().getTime());
        Text caseNumber = getSmallText
                (useCase.getPrimaryKey().toString());
        if (useCase.getCode().equalsIgnoreCase (Viewpoint.CASE_CODE_KEY) &&
            getViewpointPage() != -1) {
            Link viewpointLink = new Link (caseNumber);
            viewpointLink.setPage(getViewpointPage());
            viewpointLink.addParameter
                    (ViewpointForm.PARAM_ACTION,
                     ViewpointForm.SHOWANSWERFORM_ACTION + "");
            viewpointLink.addParameter (ViewpointForm.PARAM_VIEWPOINT_ID,
                                        useCase.getPrimaryKey().toString());
            caseNumber = viewpointLink;
        }

        final Text caseType = getSmallText
                (getCaseBusiness(iwc) .getCaseBusiness
                 (useCase.getCaseCode())
                 .getLocalizedCaseDescription
                 (useCase, iwc.getCurrentLocale()));
        final Text date = getSmallText(dateFormat.format(caseDate));
        Text manager = null;
        String managerName = "";
        int managerID = -1;
        try {
            final Group handler = useCase.getHandler();
            if (handler != null) {
                managerID = ((Integer) handler.getPrimaryKey()).intValue();
                managerName
                        = getUserBusiness(iwc).getNameOfGroupOrUser(handler);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        if (managerName == null) {
            manager = getSmallText("-");
        } else {
            manager = getSmallText(managerName);
            if (getManagerPage() != -1) {
                Link managerLink = new Link(manager);
                managerLink.setPage(getManagerPage());
                managerLink.addParameter(PARAM_MANAGER_ID,
                                         managerID);
                manager = managerLink;
            }
        }
        final Text caseOwnerName = getSmallText
                (useCase.getOwner ().getFirstName());
        final Text status = getSmallText
                (getCaseBusiness(iwc)
                 .getLocalizedCaseStatusDescription
                 (useCase.getCaseStatus(),
                  iwc.getCurrentLocale()));
        messageList.add(caseNumber);
        messageList.add(caseType);
        messageList.add(caseOwnerName);
        messageList.add(date);
        messageList.add(manager);
        messageList.add(status);
    }
    
    private void addViewpointToMessageList
        (final IWContext iwc, final Viewpoint viewpoint,
         final ColumnList messageList) throws Exception {
        
        DateFormat dateFormat = CustomDateFormat.getDateTimeInstance
                (iwc.getCurrentLocale());
        Date caseDate
                = new Date(viewpoint.getCreated().getTime());
        Text caseNumber = getSmallText
                (viewpoint.getPrimaryKey().toString());
        final Text category = getSmallText (viewpoint.getCategory ());
        final Text subject = getSmallText (viewpoint.getSubject ());
        final Text date = getSmallText(dateFormat.format(caseDate));
        final Group handlerGroup = viewpoint.getHandlerGroup ();
        final Text group = getSmallText(handlerGroup != null
                                        ? handlerGroup.getName ()
                                        : "-");

        if (getViewpointPage() != -1) {
            Link viewpointLink = new Link (caseNumber);
            viewpointLink.setPage(getViewpointPage());
            viewpointLink.addParameter
                    (ViewpointForm.PARAM_ACTION,
                     ViewpointForm.SHOWACCEPTFORM_ACTION + "");
            viewpointLink.addParameter
                    (ViewpointForm.PARAM_VIEWPOINT_ID,
                     viewpoint.getPrimaryKey().toString());
            caseNumber = viewpointLink;
        } else {
            System.out.println ("[" + this.getClass ().getName () + "] Property"
                                + " 'Viewpoint Page' not set in IdegaWeb "
                                + "Builder. Can't create links to viewpoints.");
        }
        messageList.add(caseNumber);
        messageList.add(category);
        messageList.add(subject);
        messageList.add(date);
        messageList.add(group);
    }
    
	private CaseBusiness getCaseBusiness(IWContext iwc) throws Exception {
		return (CaseBusiness) IBOLookup.getServiceInstance(iwc,
                                                           CaseBusiness.class);
	}
    
	private CommuneCaseBusiness getCommuneCaseBusiness(IWContext iwc)
        throws Exception {
		return (CommuneCaseBusiness) IBOLookup.getServiceInstance
                (iwc, CommuneCaseBusiness.class);
	}
    
	private UserBusiness getUserBusiness(IWContext iwc) throws Exception {
		return (UserBusiness) IBOLookup.getServiceInstance(iwc,
                                                           UserBusiness.class);
	}	
	
	private Case getCase(String id, IWContext iwc) throws Exception {
		int msgId = Integer.parseInt(id);
		Case msg = getCaseBusiness(iwc).getCase(msgId);
		return msg;
	}
    
    public void setViewpointPage (final IBPage page) {
        viewpointPageId = page.getID();
    }

    public int getViewpointPage () {
        return viewpointPageId;
    }

	public void setManagerPage(IBPage page) {
		manager_page_id = page.getID();
	}
    
	public void setManagerPage(int ib_page_id) {
		manager_page_id = ib_page_id;
	}
    
	public int getManagerPage() {
		return manager_page_id;
	}
}
