package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;

import com.idega.block.school.presentation.SchoolContentItem;
import com.idega.core.accesscontrol.business.NotLoggedOnException;
import com.idega.user.data.User;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;

/**
 * @author gimmi
 */
public class SchoolContentItemEditorButton extends SchoolContentItem {

	/**
	 * @see com.idega.block.school.presentation.SchoolContentItem#getObject()
	 */
	protected PresentationObject getObject() {
//		System.out.println("SchoolContentItemEditButton : hasContentEdit = "+super.getSchoolContentBusiness(_iwc).hasEditPermission(_school, super._iwc));
		
		
		Link link = SchoolContentEditor.getLink(_school, _iwrb.getLocalizedImageButton("content_editor","Content Editor"));
		
		if (hasEditPermission()) {
			return link;
		}
		return null;
	}
	
	public boolean hasEditPermission(){
		if(super.hasEditPermission()){
			return true;
		}
		else{
			try{
				User user = _iwc.getCurrentUser();
				return super.getSchoolBusiness(_iwc).hasEditPermission(user, _school);
			}
			catch(NotLoggedOnException nle){
				
			}
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}
