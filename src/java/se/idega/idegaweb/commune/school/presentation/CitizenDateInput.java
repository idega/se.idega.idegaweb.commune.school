package se.idega.idegaweb.commune.school.presentation;

import com.idega.presentation.ui.DateInput;
import com.idega.util.IWTimestamp;

/**
 * @author Joakim
 * This is a patch to be able to display a date selector in the format that Nacka wants, 
 * using the reportgenerator from Gummi. This should be removed and configuration should be
 * used of the reportgenerator when that is available.
 */
public class CitizenDateInput extends DateInput{
	public CitizenDateInput(){
		super();
		IWTimestamp date = new IWTimestamp();
		this.setYearRange(date.getYear()-100,date.getYear()+1);
		this.setToDisplayDayLast(true);
	}
}
