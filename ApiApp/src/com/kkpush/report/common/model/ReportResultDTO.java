package com.kkpush.report.common.model;

/**
 * This is about <code>com.gse.core.model.ImportResultDTO</code>
 * 
 * @author Martin lee
 * @version $Revision: 1.2 $Date: 2007-10-17
 */
public class ReportResultDTO {

	private static final long serialVersionUID = 6112263052912799511L;

	private boolean isSuccess;

	private String fileName;

	private String encoding;

	private boolean isSync;

	private int successNumber;

	private int failNumber;

	private String xmlData;

	private String jsonData;

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * @param isSuccess the isSuccess to set
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * @return the isSync
	 */
	public boolean isSync() {
		return isSync;
	}

	/**
	 * @param isSync the isSync to set
	 */
	public void setSync(boolean isSync) {
		this.isSync = isSync;
	}

	/**
	 * @return the successNumber
	 */
	public int getSuccessNumber() {
		return successNumber;
	}

	/**
	 * @param successNumber the successNumber to set
	 */
	public void setSuccessNumber(int successNumber) {
		this.successNumber = successNumber;
	}

	/**
	 * @return the failNumber
	 */
	public int getFailNumber() {
		return failNumber;
	}

	/**
	 * @param failNumber the failNumber to set
	 */
	public void setFailNumber(int failNumber) {
		this.failNumber = failNumber;
	}

	/**
	 * @return the xmlData
	 */
	public String getXmlData() {
		return xmlData;
	}

	/**
	 * @param xmlData the xmlData to set
	 */
	public void setXmlData(String xmlData) {
		this.xmlData = xmlData;
	}

	/**
	 * @return the jsonData
	 */
	public String getJsonData() {
		return jsonData;
	}

	/**
	 * @param jsonData the jsonData to set
	 */
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

}
