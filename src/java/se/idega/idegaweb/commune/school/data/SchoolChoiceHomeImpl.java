/*
 * $Id: SchoolChoiceHomeImpl.java,v 1.49 2004/11/18 19:18:38 aron Exp $
 * Created on 18.11.2004
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
import com.idega.data.IDOFactory;

/**
 * 
 *  Last modified: $Date: 2004/11/18 19:18:38 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.49 $
 */
public class SchoolChoiceHomeImpl extends IDOFactory implements
        SchoolChoiceHome {
    protected Class getEntityInterfaceClass() {
        return SchoolChoice.class;
    }

    public SchoolChoice create() throws javax.ejb.CreateException {
        return (SchoolChoice) super.createIDO();
    }

    public SchoolChoice findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (SchoolChoice) super.findByPrimaryKeyIDO(pk);
    }

    public int countBySchoolIDAndSeasonIDAndStatus(int schoolId, int seasonId,
            String[] statuses) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeCountBySchoolIDAndSeasonIDAndStatus(schoolId, seasonId,
                        statuses);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findBySchoolIDAndSeasonIDAndStatus(int schoolId,
            int seasonId, String[] statuses, int returningEntries,
            int startingEntries) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindBySchoolIDAndSeasonIDAndStatus(schoolId, seasonId,
                        statuses, returningEntries, startingEntries);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByChosenSchoolId(int chosenSchoolId,
            int schoolSeasonId) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindByChosenSchoolId(chosenSchoolId, schoolSeasonId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByChildId(int childId)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindByChildId(childId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByChildId(int childId, int schoolSeasonId)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindByChildId(childId, schoolSeasonId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByCodeAndStatus(String caseCode, String[] caseStatus,
            int schoolId, int schoolSeasonId) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindByCodeAndStatus(caseCode, caseStatus, schoolId,
                        schoolSeasonId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllWithLanguageWithinSeason(SchoolSeason season,
            String[] caseStatus) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindAllWithLanguageWithinSeason(season, caseStatus);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByCodeAndStatus(String caseCode, String[] caseStatus,
            int schoolId, int schoolSeasonId, String ordered)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindByCodeAndStatus(caseCode, caseStatus, schoolId,
                        schoolSeasonId, ordered);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getNumberOfApplications(String caseStatus, int schoolID,
            int schoolSeasonID) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeGetNumberOfApplications(caseStatus, schoolID,
                        schoolSeasonID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfApplications(String caseStatus, int schoolID,
            int schoolSeasonID, int schoolYearID) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeGetNumberOfApplications(caseStatus, schoolID,
                        schoolSeasonID, schoolYearID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfHandledMoves(int seasonID) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeGetNumberOfHandledMoves(seasonID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfUnHandledMoves(int seasonID) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeGetNumberOfUnHandledMoves(seasonID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findByChildAndSeason(int childID, int seasonID,
            String[] notInStatuses) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindByChildAndSeason(childID, seasonID, notInStatuses);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllPlacedBySeason(int seasonID)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindAllPlacedBySeason(seasonID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public SchoolChoice findByChildAndChoiceNumberAndSeason(Integer childID,
            Integer choiceNumber, Integer seasonID)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((SchoolChoiceBMPBean) entity)
                .ejbFindByChildAndChoiceNumberAndSeason(childID, choiceNumber,
                        seasonID);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Collection findBySeason(int seasonId)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindBySeason(seasonId);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findBySeasonAndSchoolYear(SchoolSeason season,
            SchoolYear year) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindBySeasonAndSchoolYear(season, year);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getCountByChildAndSchool(int childID, int schoolID)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeGetCountByChildAndSchool(childID, schoolID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getCountByChildAndSchoolAndStatus(int childID, int schoolID,
            String[] caseStatus) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeGetCountByChildAndSchoolAndStatus(childID, schoolID,
                        caseStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findByChildAndSchool(int childID, int schoolID)
            throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindByChildAndSchool(childID, schoolID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByChildAndSchoolAndSeason(int childID, int schoolID,
            int seasonID) throws javax.ejb.FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindByChildAndSchoolAndSeason(childID, schoolID, seasonID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAll() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity).ejbFindAll();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findChoices(int schoolID, int seasonID, int gradeYear,
            String[] validStatuses, String searchStringForUser, int orderBy,
            int numberOfEntries, int startingEntry) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindChoices(schoolID, seasonID, gradeYear, validStatuses,
                        searchStringForUser, orderBy, numberOfEntries,
                        startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getCount(String[] validStatuses) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeGetCount(validStatuses);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getCount(String[] validStatuses, int seasonID)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity).ejbHomeGetCount(
                validStatuses, seasonID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getCount(int schoolId, String[] validStatuses)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity).ejbHomeGetCount(
                schoolId, validStatuses);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getCount(SchoolSeason schoolSeason, Date startDate, Date endDate)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity).ejbHomeGetCount(
                schoolSeason, startDate, endDate);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getCount(int schoolId, int seasonID, String[] validStatuses)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity).ejbHomeGetCount(
                schoolId, seasonID, validStatuses);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getCount(int schoolID, int seasonID, int gradeYear,
            int[] choiceOrder, String[] validStatuses,
            String searchStringForUser) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity).ejbHomeGetCount(
                schoolID, seasonID, gradeYear, choiceOrder, validStatuses,
                searchStringForUser);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getCount(int schoolID, int seasonID, int gradeYear,
            int[] choiceOrder, String[] validStatuses,
            String searchStringForUser, int placementType) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity).ejbHomeGetCount(
                schoolID, seasonID, gradeYear, choiceOrder, validStatuses,
                searchStringForUser, placementType);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getCountOutsideInterval(int schoolID, int seasonID,
            int gradeYear, int[] choiceOrder, String[] validStatuses,
            String searchStringForUser, Date from, Date to) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeGetCountOutsideInterval(schoolID, seasonID, gradeYear,
                        choiceOrder, validStatuses, searchStringForUser, from,
                        to);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findChoices(int schoolID, int seasonID, int gradeYear,
            int[] choiceOrder, String[] validStatuses,
            String searchStringForUser, int orderBy, int numberOfEntries,
            int startingEntry) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindChoices(schoolID, seasonID, gradeYear, choiceOrder,
                        validStatuses, searchStringForUser, orderBy,
                        numberOfEntries, startingEntry);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findChoices(int schoolID, int seasonID, int gradeYear,
            int[] choiceOrder, String[] validStatuses,
            String searchStringForUser, int orderBy, int numberOfEntries,
            int startingEntry, int placementType) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindChoices(schoolID, seasonID, gradeYear, choiceOrder,
                        validStatuses, searchStringForUser, orderBy,
                        numberOfEntries, startingEntry, placementType);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findBySchoolAndSeasonAndGrade(int schoolID, int seasonID,
            int schoolYear) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindBySchoolAndSeasonAndGrade(schoolID, seasonID,
                        schoolYear);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findBySchoolAndFreeTime(int schoolId, int schoolSeasonID,
            boolean freeTimeInSchool) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindBySchoolAndFreeTime(schoolId, schoolSeasonID,
                        freeTimeInSchool);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findChoicesInClassAndSeasonAndSchool(int classID,
            int seasonID, int schoolID, boolean confirmation)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindChoicesInClassAndSeasonAndSchool(classID, seasonID,
                        schoolID, confirmation);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int getNumberOfChoices(int userID, int seasonID) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeGetNumberOfChoices(userID, seasonID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getNumberOfChoices(int userID, int seasonID,
            String[] notInStatuses) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeGetNumberOfChoices(userID, seasonID, notInStatuses);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getMoveChoices(int userID, int schoolID, int seasonID)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity).ejbHomeGetMoveChoices(
                userID, schoolID, seasonID);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getChoices(int userID, int seasonID, String[] notInStatus)
            throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity).ejbHomeGetChoices(
                userID, seasonID, notInStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public int getChoices(int userID, int schoolID, int seasonID,
            String[] notInStatus) throws IDOException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity).ejbHomeGetChoices(
                userID, schoolID, seasonID, notInStatus);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public Collection findByParent(Case parent) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((SchoolChoiceBMPBean) entity)
                .ejbFindByParent(parent);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public int countChildrenWithoutSchoolChoice(SchoolSeason season,
            SchoolYear year, boolean onlyInCommune, boolean onlyLastGrade,
            int maxAge) throws SQLException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        int theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeCountChildrenWithoutSchoolChoice(season, year,
                        onlyInCommune, onlyLastGrade, maxAge);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

    public MailReceiver[] getChildrenWithoutSchoolChoice(SchoolSeason season,
            SchoolYear year, boolean onlyInCommune, boolean onlyLastGrade,
            int maxAge) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        MailReceiver[] theReturn = ((SchoolChoiceBMPBean) entity)
                .ejbHomeGetChildrenWithoutSchoolChoice(season, year,
                        onlyInCommune, onlyLastGrade, maxAge);
        this.idoCheckInPooledEntity(entity);
        return theReturn;
    }

}
