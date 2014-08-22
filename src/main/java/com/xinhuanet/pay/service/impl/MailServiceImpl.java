package com.xinhuanet.pay.service.impl;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.xinhuanet.pay.service.MailService;
import com.xinhuanet.pay.util.Function;


@Service
public class MailServiceImpl implements MailService {
	private static Logger logger = Logger.getLogger(MailServiceImpl.class);

	private static JavaMailSenderImpl JAVAMAILSENDER;

	private static String STMPINFO;

	private static String USERFROM;
	

	@Override
	public void sendMail(String userTo, String mailCCUser, String mailTitle,
			String mailContent, String attachmentFileName, File attachmentFile)
			throws Exception {
		setJavaMailSender();
		final String userTo1 = userTo;
		final String mailCCUser1 = mailCCUser;
		final String userFrom1 = USERFROM;
		final String mailTitle1 = mailTitle;
		final String mailContent1 = mailContent;
		final String attachmentFileName1 = attachmentFileName == null ? null
				: MimeUtility.encodeWord(attachmentFileName);
		final File attachmentFile1 = attachmentFile;

		MimeMessagePreparator mPre = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage)
					throws MessagingException {
				MimeMessageHelper message;
				try {
					message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
					message.setFrom(userFrom1);
					message.setTo(userTo1);
					if (mailCCUser1 != null && !"".equals(mailCCUser1.trim())) {
						message.setCc(mailCCUser1.split(";"));
					}
					message.setSubject(mailTitle1);
					message.setText(mailContent1, true);
					if (attachmentFileName1 != null && attachmentFile1 != null)
						message.addAttachment(attachmentFileName1,
								attachmentFile1);
				} catch (javax.mail.MessagingException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
//		JAVAMAILSENDER.send(mPre);
	}

	private void setJavaMailSender() throws Exception {
//		String newStmpInfo = configSystemService.select(18).get(0)
//				.getParamValue();
//		 String newStmpInfo = "202.123.98.13,noreply@home.news.cn,66242229kwm";
		 String newStmpInfo = "202.123.98.13,40tage@163.com,baxinjiaogeiwo";
		if (JAVAMAILSENDER == null || !newStmpInfo.equals(STMPINFO)) {
			STMPINFO = newStmpInfo;
			String stmpInfoG[] = STMPINFO.split(",");
			USERFROM = stmpInfoG[1];
			JAVAMAILSENDER = new JavaMailSenderImpl();
			JAVAMAILSENDER.setHost(stmpInfoG[0]);
			JAVAMAILSENDER.setUsername(stmpInfoG[1]);
			JAVAMAILSENDER.setPassword(stmpInfoG[2]);
			JAVAMAILSENDER.setDefaultEncoding("gb2312");
			Properties properties = new Properties();
			properties.setProperty("mail.smtp.auth", "true");
			JAVAMAILSENDER.setJavaMailProperties(properties);
		}
	}

	public static void main(String arg[]) {
		MailService ms = new MailServiceImpl();
		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("date", Function.getDateString("yyyy-MM-dd HH:mm:ss"));
			paraMap.put("authType", "authType替换");
			paraMap.put("serviceName", "serviceName替换");
			paraMap.put("reason", "reason替换");
			paraMap.put("authEnterprise", false);
//			String mailContent = AuthUtil
//					.getContentFromTemplate(new File(AuthUtil.getResourceURL()
//							.getPath()
//							+ File.separator
//							+ Constant.EMAIL_FAILURE_TEMPLATEFILE_URI), paraMap);
			String mailContent = "sdfghjdfghdfghfghfg<b><font size=19>四大皆空后就开始的</font></b>";
			System.out.println(mailContent);
			ms.sendMail("327114652@qq.com", null, "个人不成功", "个人不成功", null, null);
			ms.sendMail("327114652@qq.com", null, "个人不成功", mailContent, null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("============");
	}

}
