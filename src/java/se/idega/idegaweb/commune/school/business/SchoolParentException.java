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
public class SchoolParentException extends Exception {
  String key;
  String defTrans;
  
  public SchoolParentException(String localizationKey, String defaultText) {
    key = localizationKey;
    defTrans = defaultText;
  }

	/**
	 * @return 
	 */
	public String getDefTrans() {
		return defTrans;
	}

	/**
	 * @return
	 */
	public String getKey() {
		return key;
	}
}
