package se.idega.idegaweb.commune.school.data;

import com.idega.block.school.data.*;
import com.idega.data.GenericEntity;
import com.idega.user.data.User;
import java.util.Date;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Last modified: $Date: 2003/03/28 11:10:06 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 * @see se.idega.idegaweb.commune.block.importer.business.NackaStudentTimeImportFileHandler
 */
public class SchoolTimeBMPBean extends GenericEntity implements SchoolTime {

    private static final String ENTITY_NAME = "sch_time";
    private static final String COLUMN_ID = ENTITY_NAME + "_id";
	private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_SCHOOL_ID = "school_id";
    private static final String COLUMN_HOURS = "hours";
    private static final String COLUMN_SEASON_ID = "season_id";
    private static final String COLUMN_REGISTRATION_TIME = "registration_time";

	public String getEntityName() {
		return ENTITY_NAME;
	}

    public void initializeAttributes () {
        addAttribute (COLUMN_ID, "Id", Integer.class);
        setAsPrimaryKey (COLUMN_ID, true);
       	addAttribute (COLUMN_USER_ID, "Student", true, true, Integer.class,
                      "many-to-one", User.class);
       	addAttribute (COLUMN_SCHOOL_ID, "School", true, true, Integer.class,
                      "many-to-one", School.class);
        addAttribute (COLUMN_HOURS, "Hours", Integer.class);
       	addAttribute (COLUMN_SEASON_ID, "Season", true, true, Integer.class,
                      "many-to-one", SchoolSeason.class);
        addAttribute (COLUMN_REGISTRATION_TIME, "Registration time",
                      java.sql.Date.class);

        setNullable (COLUMN_USER_ID, false);
        setNullable (COLUMN_SCHOOL_ID, true);
        setNullable (COLUMN_HOURS, false);
        setNullable (COLUMN_SEASON_ID, false);
        setNullable (COLUMN_REGISTRATION_TIME, false);
    }

	public void setUser (final User user) {
        setColumn (COLUMN_USER_ID, user.getPrimaryKey ());
    }
    
    public void setSchool (final School school) throws RemoteException {
        if (school == null) {
            try {
                setColumnAsNull (COLUMN_SCHOOL_ID);
            } catch (SQLException e) {
                throw new RemoteException ("Couldn't set school to null", e);
            }
        } else {
            setColumn (COLUMN_SCHOOL_ID, school.getPrimaryKey ());
        }
    }
    
    public void setHours (final int hours) {
        setColumn (COLUMN_HOURS, hours);
    }
    
    public void setSeason (final SchoolSeason season) {
        setColumn (COLUMN_SEASON_ID, season.getPrimaryKey ());
    }
    
    public void setRegistrationTime (final Date date) {
        setColumn (COLUMN_REGISTRATION_TIME,
                   new java.sql.Date (date.getTime ()));
    }
 }
