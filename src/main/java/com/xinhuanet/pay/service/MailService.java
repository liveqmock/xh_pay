package com.xinhuanet.pay.service;

import java.io.File;

public interface MailService {
	public void sendMail(String userTo, String mailCCUser, String mailTitle,
			String mailContent, String attachmentFileName, File attachmentFile)
			throws Exception;
}
