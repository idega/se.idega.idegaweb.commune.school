/*
 * Created on 17.12.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation.inputhandler;

import java.util.Collection;
import java.util.Collections;

import com.idega.business.InputHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.TextArea;

/**
 * @author laddi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TextAreaHandler extends TextArea implements InputHandler {

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		this.setName(name);
		if (value != null) {
			this.setContent(value);
		}
		this.setStyleClass("commune_Interface");
		this.setRows(6);
		this.setWidth("200");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getResultingObject(java.lang.String[], com.idega.presentation.IWContext)
	 */
	public Object getResultingObject(String[] value, IWContext iwc) throws Exception {
		if (value != null) {
			return value[0];
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.Object, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		return (String)value;
	}

	public PresentationObject getHandlerObject(String name, Collection values, IWContext iwc) {
		String value = (String) Collections.min(values);
		return getHandlerObject(name, value, iwc);
	}


	public Object convertSingleResultingObjectToType(Object value, String className) {
		return value;
	}

}
