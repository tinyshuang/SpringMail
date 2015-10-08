package hxk.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hxk.util.MailSenderService;

/**
 * @author hxk
 * @description 发送邮件的测试Action
 *2015年10月8日  上午10:34:48
 */
@Controller
@RequestMapping("/Mail/")
public class MailAction {
    @Resource
    private MailSenderService mailSenderService;
    
    
    /**
     * @description 使用velocityEngine模板引擎来配置邮箱模板	
     * @return
     *2015年10月8日  下午1:46:37
     *返回类型:String
     */
    @RequestMapping("send")
    public @ResponseBody String sendWithTemplate(){
	mailSenderService.setTo("javav587@163.com");
	mailSenderService.setSubject("[测试]");
	mailSenderService.setTemplateName("test.vm");//设置的邮件模板
	//这个map可以设置相应的参数过去模板..实现替换..
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("name", "Tinys");
	//可以使用以下方法获取模板转换之后的文本
	String result=mailSenderService.get(map);
	System.out.println(result);
	mailSenderService.sendWithTemplate(map);
	return "ok";
    }
    
    /**
     * @description 直接发送邮件	
     * @return
     *2015年10月8日  下午1:52:35
     *返回类型:String
     */
    @RequestMapping("sendText")
    public @ResponseBody String sendText(){
	mailSenderService.setTo("javav587@163.com");
	mailSenderService.setSubject("[测试]");
	mailSenderService.setContent("  测试Text");
	mailSenderService.sendText();
	return "ok";
    }
    
    /**
     * @description 发送mail里面带图片..
     * 使用"<img src='cid:image'/>"的cid占位符提到路径	
     * @return
     *2015年10月8日  下午1:58:06
     *返回类型:String
     */
    @RequestMapping("sendImg")
    public @ResponseBody String sendImg(){
	mailSenderService.setTo("javav587@163.com");
	mailSenderService.setSubject("[测试]");
	mailSenderService.setContent("  测试Img");
	mailSenderService.sendHtmlWithImage("g://1.png");
	return "ok";
    }
    
    /**
     * @description 发送带附件的mail 	
     * @return
     *2015年10月8日  下午2:13:57
     *返回类型:String
     */
    @RequestMapping("sendWithAttach")
    public @ResponseBody String sendWithAttachemnt(){
	mailSenderService.setTo("javav587@163.com");
	mailSenderService.setSubject("[测试]");
	mailSenderService.setContent("  测试Attach");
	mailSenderService.sendHtmlWithAttachment("g://1.png");
	return "ok";
    }
    
   
    
    
}
