package se.idega.idegaweb.commune.school.business;

/**
 * School mark value holder
 * <p>
 * $Id: SchoolMarkValues.java,v 1.1 2003/09/25 07:58:41 kjell Exp $
 *
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $version$
 */

public class SchoolMarkValues {
	public PercentValue eg;
	public PercentValue g;
	public PercentValue vg;
	public PercentValue mvg;
	public PercentValue tot;
	
	public SchoolMarkValues() {
		eg = new PercentValue();
		g = new PercentValue();
		vg = new PercentValue();
		mvg = new PercentValue();
		tot = new PercentValue();
	}
}