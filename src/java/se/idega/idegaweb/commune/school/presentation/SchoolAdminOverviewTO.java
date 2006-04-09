/*
 * Created on 2003-sep-15
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation;

/**
 * @author wmgobom
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SchoolAdminOverviewTO {
  private int studentID = -1;
  private int classMemberID = -1;
  private int seasonID = -1;
  private int schMemberID = -1;

	/**
	 * @return
	 */
	public int getClassMemberID() {
		return this.classMemberID;
	}

	/**
	 * @return
	 */
	public int getSchMemberID() {
		return this.schMemberID;
	}

	/**
	 * @return
	 */
	public int getSeasonID() {
		return this.seasonID;
	}

	/**
	 * @return
	 */
	public int getStudentID() {
		return this.studentID;
	}

	/**
	 * @param i
	 */
	public void setClassMemberID(int i) {
		this.classMemberID = i;
	}

	/**
	 * @param i
	 */
	public void setSchMemberID(int i) {
		this.schMemberID = i;
	}

	/**
	 * @param i
	 */
	public void setSeasonID(int i) {
		this.seasonID = i;
	}

	/**
	 * @param i
	 */
	public void setStudentID(int i) {
		this.studentID = i;
	}

}
