/*
 * $Id: SchoolChoice.java,v 1.30 2004/10/20 15:46:00 aron Exp $
 * Created on 20.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.data;

import java.sql.Date;
import java.sql.Timestamp;



import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.data.IDOEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/10/20 15:46:00 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.30 $
 */
public interface SchoolChoice extends IDOEntity, Case {
    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCaseStatusCreated
     */
    public String getCaseStatusCreated();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCaseStatusQuiet
     */
    public String getCaseStatusQuiet();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCaseStatusPreliminary
     */
    public String getCaseStatusPreliminary();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCaseStatusPlaced
     */
    public String getCaseStatusPlaced();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCaseStatusGrouped
     */
    public String getCaseStatusGrouped();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCaseStatusMoved
     */
    public String getCaseStatusMoved();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCaseCodeKey
     */
    public String getCaseCodeKey();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCaseCodeDescription
     */
    public String getCaseCodeDescription();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCaseStatusKeys
     */
    public String[] getCaseStatusKeys();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCaseStatusDescriptions
     */
    public String[] getCaseStatusDescriptions();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getChildId
     */
    public int getChildId();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getChild
     */
    public User getChild();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setChildId
     */
    public void setChildId(int id);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCurrentSchoolId
     */
    public int getCurrentSchoolId();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCurrentSchool
     */
    public School getCurrentSchool();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setCurrentSchoolId
     */
    public void setCurrentSchoolId(int id);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getSchoolSeasonId
     */
    public int getSchoolSeasonId();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getSchoolSeason
     */
    public SchoolSeason getSchoolSeason();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setSchoolSeasonId
     */
    public void setSchoolSeasonId(int id);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getSchoolTypeId
     */
    public int getSchoolTypeId();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getSchoolType
     */
    public SchoolType getSchoolType();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setSchoolTypeId
     */
    public void setSchoolTypeId(int id);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getChosenSchoolId
     */
    public int getChosenSchoolId();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getChosenSchool
     */
    public School getChosenSchool();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setChosenSchoolId
     */
    public void setChosenSchoolId(int id);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getSchoolYear
     */
    public SchoolYear getSchoolYear();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getSchoolYearID
     */
    public int getSchoolYearID();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setSchoolYear
     */
    public void setSchoolYear(int schoolYearID);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCurrentSchoolYear
     */
    public SchoolYear getCurrentSchoolYear();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCurrentSchoolYearID
     */
    public int getCurrentSchoolYearID();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setCurrentSchoolYear
     */
    public void setCurrentSchoolYear(int schoolYearID);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getWorkSituation1
     */
    public int getWorkSituation1();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setWorksituation1
     */
    public void setWorksituation1(int situation);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getWorkSituation2
     */
    public int getWorkSituation2();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setWorksituation2
     */
    public void setWorksituation2(int situation);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getPlacementDate
     */
    public Date getPlacementDate();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setPlacementDate
     */
    public void setPlacementDate(Date date);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getLanguageChoice
     */
    public String getLanguageChoice();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setLanguageChoice
     */
    public void setLanguageChoice(String language);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getGroupPlace
     */
    public String getGroupPlace();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setGroupPlace
     */
    public void setGroupPlace(String place);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getSchoolChoiceDate
     */
    public Timestamp getSchoolChoiceDate();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setSchoolChoiceDate
     */
    public void setSchoolChoiceDate(Timestamp stamp);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getMessage
     */
    public String getMessage();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setMessage
     */
    public void setMessage(String msg);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getChoiceOrder
     */
    public int getChoiceOrder();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setChoiceOrder
     */
    public void setChoiceOrder(int order);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getMethod
     */
    public int getMethod();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setMethod
     */
    public void setMethod(int method);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getChangeOfSchool
     */
    public boolean getChangeOfSchool();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setChangeOfSchool
     */
    public void setChangeOfSchool(boolean change);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getKeepChildrenCare
     */
    public boolean getKeepChildrenCare();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setKeepChildrenCare
     */
    public void setKeepChildrenCare(boolean keepchildcare);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getAutoAssign
     */
    public boolean getAutoAssign();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setAutoAssign
     */
    public void setAutoAssign(boolean auto);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getCustodiansAgree
     */
    public boolean getCustodiansAgree();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setCustodiansAgree
     */
    public void setCustodiansAgree(boolean agree);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getSchoolCatalogue
     */
    public boolean getSchoolCatalogue();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setSchoolCatalogue
     */
    public void setSchoolCatalogue(boolean catalogue);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getFreetimeInThisSchool
     */
    public boolean getFreetimeInThisSchool();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setFreetimeInThisSchool
     */
    public void setFreetimeInThisSchool(boolean freetimeInThisSchool);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getFreetimeOther
     */
    public String getFreetimeOther();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setFreetimeOther
     */
    public void setFreetimeOther(String other);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setHasReceivedPlacementMessage
     */
    public void setHasReceivedPlacementMessage(boolean hasReceivedMessage);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setHasReceivedConfirmationMessage
     */
    public void setHasReceivedConfirmationMessage(boolean hasReceivedMessage);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getHasReceivedPlacementMessage
     */
    public boolean getHasReceivedPlacementMessage();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getHasReceivedConfirmationMessage
     */
    public boolean getHasReceivedConfirmationMessage();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getExtraChoiceMessage
     */
    public String getExtraChoiceMessage();

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#setExtraChoiceMessage
     */
    public void setExtraChoiceMessage(String extraMessage);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getIDOQuery
     */
    public IDOQuery getIDOQuery(int schoolID, int seasonID, int gradeYear,
            int[] choiceOrder, String[] validStatuses,
            String searchStringForUser, boolean selectCount,
            boolean selectOnlyChildIDs, int orderBy);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getIDOQuery
     */
    public IDOQuery getIDOQuery(int schoolID, int seasonID, int schoolYear,
            int[] choiceOrder, String[] validStatuses,
            String searchStringForUser, boolean selectCount,
            boolean selectOnlyChildIDs, int orderBy, int placementType);

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#getIDOQuery
     */
    public IDOQuery getIDOQuery(int schoolID, int seasonID, int schoolYear,
            int[] choiceOrder, String[] validStatuses,
            String searchStringForUser, boolean selectCount,
            boolean selectOnlyChildIDs, boolean searchOnAddr, int orderBy,
            int placementType);

}
