package se.idega.idegaweb.commune.school.business;


/**
 * Percent and value holder
 * <p>
 * $Id: PercentValue.java,v 1.1 2003/09/25 07:58:41 kjell Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $version$
 */

public class PercentValue {
	public String percent = "";
	public String number = "";

	public PercentValue() {
		this.percent = "";
		this.number = "";
	}

	public PercentValue(String number, String percent) {
		this.percent = percent;
		this.number = number;
	}
}