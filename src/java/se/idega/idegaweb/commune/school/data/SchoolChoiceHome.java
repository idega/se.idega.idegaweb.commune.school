/*
 * $Id: SchoolChoiceHome.java,v 1.46 2004/10/20 15:46:00 aron Exp $
 * Created on 20.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.data;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.business.MailReceiver;

import com.idega.block.process.data.Case;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolYear;
import com.idega.data.IDOException;
import com.idega.data.IDOHome;

/**
 * 
 *  Last modified: $Date: 2004/10/20 15:46:00 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.46 $
 */
public interface SchoolChoiceHome extends IDOHome {
    public SchoolChoice create() throws javax.ejb.CreateException;

    public SchoolChoice findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeCountBySchoolIDAndSeasonIDAndStatus
     */
    public int countBySchoolIDAndSeasonIDAndStatus(int schoolId, int seasonId,
            String[] statuses) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindBySchoolIDAndSeasonIDAndStatus
     */
    public Collection findBySchoolIDAndSeasonIDAndStatus(int schoolId,
            int seasonId, String[] statuses, int returningEntries,
            int startingEntries) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindByChosenSchoolId
     */
    public Collection findByChosenSchoolId(int chosenSchoolId,
            int schoolSeasonId) throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindByChildId
     */
    public Collection findByChildId(int childId)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindByChildId
     */
    public Collection findByChildId(int childId, int schoolSeasonId)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindByCodeAndStatus
     */
    public Collection findByCodeAndStatus(String caseCode, String[] caseStatus,
            int schoolId, int schoolSeasonId) throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindAllWithLanguageWithinSeason
     */
    public Collection findAllWithLanguageWithinSeason(SchoolSeason season,
            String[] caseStatus) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindByCodeAndStatus
     */
    public Collection findByCodeAndStatus(String caseCode, String[] caseStatus,
            int schoolId, int schoolSeasonId, String ordered)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetNumberOfApplications
     */
    public int getNumberOfApplications(String caseStatus, int schoolID,
            int schoolSeasonID) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetNumberOfApplications
     */
    public int getNumberOfApplications(String caseStatus, int schoolID,
            int schoolSeasonID, int schoolYearID) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetNumberOfHandledMoves
     */
    public int getNumberOfHandledMoves(int seasonID) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetNumberOfUnHandledMoves
     */
    public int getNumberOfUnHandledMoves(int seasonID) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindByChildAndSeason
     */
    public Collection findByChildAndSeason(int childID, int seasonID,
            String[] notInStatuses) throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindAllPlacedBySeason
     */
    public Collection findAllPlacedBySeason(int seasonID)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindByChildAndChoiceNumberAndSeason
     */
    public SchoolChoice findByChildAndChoiceNumberAndSeason(Integer childID,
            Integer choiceNumber, Integer seasonID)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindBySeason
     */
    public Collection findBySeason(int seasonId)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindBySeasonAndSchoolYear
     */
    public Collection findBySeasonAndSchoolYear(SchoolSeason season,
            SchoolYear year) throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetCountByChildAndSchool
     */
    public int getCountByChildAndSchool(int childID, int schoolID)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetCountByChildAndSchoolAndStatus
     */
    public int getCountByChildAndSchoolAndStatus(int childID, int schoolID,
            String[] caseStatus) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindByChildAndSchool
     */
    public Collection findByChildAndSchool(int childID, int schoolID)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindByChildAndSchoolAndSeason
     */
    public Collection findByChildAndSchoolAndSeason(int childID, int schoolID,
            int seasonID) throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindAll
     */
    public Collection findAll() throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindChoices
     */
    public Collection findChoices(int schoolID, int seasonID, int gradeYear,
            String[] validStatuses, String searchStringForUser, int orderBy,
            int numberOfEntries, int startingEntry) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetCount
     */
    public int getCount(String[] validStatuses) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetCount
     */
    public int getCount(String[] validStatuses, int seasonID)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetCount
     */
    public int getCount(int schoolId, String[] validStatuses)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetCount
     */
    public int getCount(SchoolSeason schoolSeason, Date startDate, Date endDate)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetCount
     */
    public int getCount(int schoolId, int seasonID, String[] validStatuses)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetCount
     */
    public int getCount(int schoolID, int seasonID, int gradeYear,
            int[] choiceOrder, String[] validStatuses,
            String searchStringForUser) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetCount
     */
    public int getCount(int schoolID, int seasonID, int gradeYear,
            int[] choiceOrder, String[] validStatuses,
            String searchStringForUser, int placementType) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetCountOutsideInterval
     */
    public int getCountOutsideInterval(int schoolID, int seasonID,
            int gradeYear, int[] choiceOrder, String[] validStatuses,
            String searchStringForUser, Date from, Date to) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindChoices
     */
    public Collection findChoices(int schoolID, int seasonID, int gradeYear,
            int[] choiceOrder, String[] validStatuses,
            String searchStringForUser, int orderBy, int numberOfEntries,
            int startingEntry) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindChoices
     */
    public Collection findChoices(int schoolID, int seasonID, int gradeYear,
            int[] choiceOrder, String[] validStatuses,
            String searchStringForUser, int orderBy, int numberOfEntries,
            int startingEntry, int placementType) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindBySchoolAndSeasonAndGrade
     */
    public Collection findBySchoolAndSeasonAndGrade(int schoolID, int seasonID,
            int schoolYear) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindBySchoolAndFreeTime
     */
    public Collection findBySchoolAndFreeTime(int schoolId, int schoolSeasonID,
            boolean freeTimeInSchool) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindChoicesInClassAndSeasonAndSchool
     */
    public Collection findChoicesInClassAndSeasonAndSchool(int classID,
            int seasonID, int schoolID, boolean confirmation)
            throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetNumberOfChoices
     */
    public int getNumberOfChoices(int userID, int seasonID) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetNumberOfChoices
     */
    public int getNumberOfChoices(int userID, int seasonID,
            String[] notInStatuses) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetMoveChoices
     */
    public int getMoveChoices(int userID, int schoolID, int seasonID)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetChoices
     */
    public int getChoices(int userID, int seasonID, String[] notInStatus)
            throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetChoices
     */
    public int getChoices(int userID, int schoolID, int seasonID,
            String[] notInStatus) throws IDOException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbFindByParent
     */
    public Collection findByParent(Case parent) throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeCountChildrenWithoutSchoolChoice
     */
    public int countChildrenWithoutSchoolChoice(SchoolSeason season,
            SchoolYear year, boolean onlyInCommune, boolean onlyLastSchoolYear)
            throws SQLException;

    /**
     * @see se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean#ejbHomeGetChildrenWithoutSchoolChoice
     */
    public MailReceiver[] getChildrenWithoutSchoolChoice(SchoolSeason season,
            SchoolYear year, boolean onlyInCommune, boolean onlyLastSchoolYear)
            throws FinderException;

}
