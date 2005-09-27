package se.idega.idegaweb.commune.school;

import se.idega.idegaweb.commune.school.business.SchoolCaseBusiness;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.business.SchoolChoiceCaseBusiness;
import se.idega.idegaweb.commune.school.business.SchoolConstants;
import com.idega.block.process.business.CaseCodeManager;
import com.idega.block.school.presentation.SchoolBlock;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.include.GlobalIncludeManager;
import com.idega.repository.data.ImplementorRepository;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 10, 2004
 */
public class IWBundleStarter implements IWBundleStartable {
	
	public void start(IWBundle starterBundle) {
		GlobalIncludeManager includeManager = GlobalIncludeManager.getInstance();
		includeManager.addBundleStyleSheet(SchoolBlock.IW_BUNDLE_IDENTIFIER, "/style/commune.css");

		CaseCodeManager caseCodeManager = CaseCodeManager.getInstance();
		caseCodeManager.addCaseBusinessForCode( SchoolConstants.SCHOOL_CHOICE_CASE_CODE_KEY, SchoolChoiceBusiness.class);
		// other bundles are also adding implementors e.g. se.idega.idegaweb.commune.adulteducation
		ImplementorRepository.getInstance().addImplementor(SchoolCaseBusiness.class, SchoolChoiceCaseBusiness.class);
	}

	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
	
}
