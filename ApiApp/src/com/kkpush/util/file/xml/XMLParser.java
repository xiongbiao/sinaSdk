package com.kkpush.util.file.xml;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kkpush.util.file.monitor.FileChangeListener;
import com.kkpush.util.file.monitor.FileMonitor;

/**
 * The class is use to parse xml file
 * 
 * @author Martin Liu
 * 
 */

public abstract class XMLParser implements FileChangeListener {

	protected Document doc;

	public final static String XML_ELEMENT_NAME = "function";

	public final static String XML_CELL_NAME = "field";

	private static Logger logger = LoggerFactory.getLogger(XMLParser.class.getName());

	public XMLParser() {
		parseXmlFile();
		parseContent();
		FileMonitor.getInstance().addFileChangeListener(this, getFileName(), getMonitorPeriod());

	}

	/**
	 * parse the xml file
	 */
	protected void parseXmlFile() {
		SAXBuilder builder = new SAXBuilder(false);
		try {
			doc = builder.build(getFileName());
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
	}

	/**
	 * get the parse file name
	 * 
	 * @return
	 */
	protected abstract String getFileName();

	protected abstract void parseContent();

	public void fileChanged(String filename) {
		parseXmlFile();
		parseContent();
	}

	/**
	 * get the file change monitor period
	 * 
	 * @return monitor period
	 */
	protected int getMonitorPeriod() {
		return 60 * 1000;
	}
}
