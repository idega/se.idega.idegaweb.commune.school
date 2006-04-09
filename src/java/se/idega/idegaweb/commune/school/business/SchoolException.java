/*
 * Created on 2003-sep-15
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.school.business;

/**
 * @author Goran Borgman
 *
 */
public class SchoolException extends Exception {
  String key;
  String defTrans;
  
  public SchoolException(String localizationKey, String defaultText) {
    this.key = localizationKey;
    this.defTrans = defaultText;
  }

	/**
	 * @return 
	 */
	public String getDefTrans() {
		return this.defTrans;
	}

	/**
	 * @return
	 */
	public String getKey() {
		return this.key;
	}
}
