package com.jpush.tools.model;

public class Mailinfo {

	
	public Mailinfo(){}
	public Mailinfo(String subject, String mailbody,String formMail, String toMail){
		this.subject=subject;
		this.mailbody=mailbody;
		this.formMail=formMail;
		this.toMail=toMail;
	}
	
	private String subject;
	private String mailbody;

	private String formMail;
	private String toMail;
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMailbody() {
		return mailbody;
	}
	public void setMailbody(String mailbody) {
		this.mailbody = mailbody;
	}
	public String getFormMail() {
		return formMail;
	}
	public void setFormMail(String formMail) {
		this.formMail = formMail;
	}
	public String getToMail() {
		return toMail;
	}
	public void setToMail(String toMail) {
		this.toMail = toMail;
	}
    
}
