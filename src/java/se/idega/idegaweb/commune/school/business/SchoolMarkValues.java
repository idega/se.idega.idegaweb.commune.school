package se.idega.idegaweb.commune.school.business;

/**
 * School mark value holder
 * <p>
 * $Id: SchoolMarkValues.java,v 1.3 2006/04/09 11:39:54 laddi Exp $
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
		this.eg = new PercentValue();
		this.g = new PercentValue();
		this.vg = new PercentValue();
		this.mvg = new PercentValue();
		this.tot = new PercentValue();
		this.ceg = 0;
		this.cg = 0;
		this.cvg = 0;
		this.cmvg = 0;
		this.ctot = 0;
	}
}