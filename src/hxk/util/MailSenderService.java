package hxk.util;


import java.io.File;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;


import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author hxk
 * @description 发送邮箱的service工具类
 *2015年10月8日  下午2:40:23
 */
public class MailSenderService {
	private static final Logger logger = Logger.getLogger(MailSenderService.class);
	private JavaMailSender mailSender;//spring配置中定义
	private VelocityEngine velocityEngine;//spring配置中定义
	private SimpleMailMessage simpleMailMessage;//spring配置中定义
	private String from;
	private String to;
	private String subject;
	private String content;
	private String templateName;
	
	
	/**
	 * 发送模板邮件
	 *
	 */
	public boolean sendWithTemplate(Map<String,Object> model){
		
		logger.info( "=========sendWithTemplate start=========");
		boolean b=false;
		mailSender = this.getMailSender();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
			System.out.println(simpleMailMessage.getFrom());
			messageHelper.setFrom(MimeUtility.encodeWord(simpleMailMessage.getFrom()) + " <" + simpleMailMessage.getFrom() + ">" );//发送人,从配置文件中取得
			messageHelper.setTo(this.getTo()); //接收人  
			messageHelper.setSubject(this.getSubject()); //标题
			 String result = null;
			 try {
		        	result = VelocityEngineUtils.mergeTemplateIntoString(this.getVelocityEngine(), this.getTemplateName(), "UTF-8",model);
		        	logger.info( "=========sendWithTemplate VelocityData");
		        } catch (Exception e) {
					e.printStackTrace();
				       logger.info( "=========VelocityData error=========");
				       return false;
					}
			 messageHelper.setText(result);//内容
			
		} catch (Exception e) {
			logger.info( "=========sendWithTemplate error"+e);
			e.printStackTrace();
			return false;
		}
	
		try {
			 mailSender.send(mimeMessage);
			 b=true;
			 logger.info( "=========mailSender success");
		} catch (Exception e) {
			logger.info( "=========mailSender error"+e);
			return false;
		}
		
		
	  
	   
	   logger.info( "=========sendWithTemplate end=========");
		return b;
	}
	/**
	 * 发送普通文本邮件
	 *
	 */
	public void sendText(){
		mailSender = this.getMailSender();
		simpleMailMessage.setTo(this.getTo()); //接收人  
		simpleMailMessage.setFrom(simpleMailMessage.getFrom()); //发送人,从配置文件中取得
		simpleMailMessage.setSubject(this.getSubject());
		simpleMailMessage.setText(this.getContent());
		mailSender.send(simpleMailMessage);
	}
	/**
	 * 发送普通Html邮件
	 *
	 */
	public void sendHtml(){
		mailSender = this.getMailSender();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
		try {
			messageHelper.setTo(this.getTo());
			messageHelper.setFrom(simpleMailMessage.getFrom());
			messageHelper.setSubject(this.getSubject());
			messageHelper.setText(this.getContent(),true);      
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	    mailSender.send(mimeMessage);
	}
	/**
	 * 发送普通带一张图片的Html邮件
	 *
	 */
	public void sendHtmlWithImage(String imagePath){
		mailSender = this.getMailSender();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
			messageHelper.setTo(this.getTo());
			messageHelper.setFrom(simpleMailMessage.getFrom());
			messageHelper.setSubject(this.getSubject());
			// 邮件内容，第二个参数指定发送的是HTML格式  
			//内嵌图片，给定一个CID值即可，增加附件，使用MimeMessageHelper的addAttachment即可
			//图片必须这样子：<img src='cid:image'/>
			messageHelper.setText(this.getContent()+"<br><img src='cid:img'>",true);
			FileSystemResource img = new FileSystemResource(new File(imagePath));
			messageHelper.addInline("img",img);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	    mailSender.send(mimeMessage);
	}
	/**
	 * 发送普通带附件的Html邮件
	 *
	 */
	public void sendHtmlWithAttachment(String filePath){
		mailSender = this.getMailSender();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
			messageHelper.setTo(this.getTo());
			messageHelper.setFrom(simpleMailMessage.getFrom());
			messageHelper.setSubject(this.getSubject());
			
			messageHelper.setText(this.getContent(),true);
			FileSystemResource file = new FileSystemResource(new File(filePath));
			messageHelper.addAttachment(file.getFilename(),file);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	    mailSender.send(mimeMessage);
	}
	
	/**
	 * 得到短信的模版记录
	 *
	 */
	public String get(Map<String,Object> model){
		String result="";
		try {
			 logger.info( "=========VelocityMessageData start");
	        	result = VelocityEngineUtils.mergeTemplateIntoString(this.getVelocityEngine(), this.getTemplateName(), "UTF-8",model);
	        	logger.info( "========VelocityMessageData end");	
		 } catch (Exception e) {
			e.printStackTrace();
			   logger.info( "=========VelocityMessageData error=========");
		 }
	   return result;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public SimpleMailMessage getSimpleMailMessage() {
		return simpleMailMessage;
	}
	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}
	
	public JavaMailSender getMailSender() {
		return mailSender;
	}
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
}

