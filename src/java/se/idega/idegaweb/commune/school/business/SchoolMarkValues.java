package se.idega.idegaweb.commune.school.business;

/**
 * School mark value holder
 * <p>
 * $Id: SchoolMarkValues.java,v 1.2 2003/10/06 11:58:45 kjell Exp $
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

	public int ceg;
	public int cg;
	public int cvg;
	public int cmvg;
	public int ctot;
	
	public SchoolMarkValues() {
		eg = new PercentValue();
		g = new PercentValue();
		vg = new PercentValue();
		mvg = new PercentValue();
		tot = new PercentValue();
		ceg = 0;
		cg = 0;
		cvg = 0;
		cmvg = 0;
		ctot = 0;
	}
}