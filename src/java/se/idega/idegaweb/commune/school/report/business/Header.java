/*
 * $Id: Header.java,v 1.4 2004/01/23 09:23:00 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

/** 
 * Holds report header localization keys. Headers can be organized in
 * a hierarchical way.
 * <p>
 * Last modified: $Date: 2004/01/23 09:23:00 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.4 $
 */
public class Header {
	
	public final static int HEADERTYPE_ROW_NORMAL = 1;
	public final static int HEADERTYPE_ROW_HEADER = 2;
	public final static int HEADERTYPE_ROW_SUM = 2;
	public final static int HEADERTYPE_ROW_TOTAL = 3;
	public final static int HEADERTYPE_ROW_NONLOCALIZED_NORMAL = 4;
	public final static int HEADERTYPE_ROW_NONLOCALIZED_HEADER = 5;
	public final static int HEADERTYPE_ROW_LABEL = 6;
	public final static int HEADERTYPE_ROW_SPACER = 7;
	
	public final static int HEADERTYPE_COLUMN_NORMAL = 100;
	public final static int HEADERTYPE_COLUMN_HEADER = 101;
	public final static int HEADERTYPE_COLUMN_NONLOCALIZED_HEADER = 102;
	
	private String _localizationKey = null;
	private Header[] _children = null;
	private int _headerType = 0;
	
	/**
	 * Constructs a report header with the specified localization key and type.
	 * @param localizationKey the localization key for this header
	 * @param headerType the type of header (row/column, normal/heading e t c) 
	 */	
	public Header(String localizationKey, int headerType) {
		_localizationKey = localizationKey;
		_headerType = headerType;
	}
	
	/**
	 * Constructs a report header with the specified localization key, type and number of children.
	 * @param localizationKey the localization key for this header
	 * @param headerType the type of header (row/column, normal/heading e t c) 
	 * @param nrOfChildren the number of child headers for this header 
	 */	
	public Header(String localizationKey, int headerType, int nrOfChildren) {
		this(localizationKey, headerType);
		_children = new Header[nrOfChildren];
	}
	
	/**
	 * Returns the localization key for this header.
	 */
	public String getLocalizationKey() {
		return _localizationKey;
	}
	
	/**
	 * Returns the type for this header.
	 */
	public int getHeaderType() {
		return _headerType;
	}
	
	/**
	 * Returns the children for this header.
	 */
	public Header[] getChildren() {
		return _children;
	}
	
	/**
	 * Sets the specified child header.
	 */
	protected void setChild(int childIndex, Header header) {
		_children[childIndex] = header;
	}
}
