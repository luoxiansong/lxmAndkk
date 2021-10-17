package com.cqkk.config.redis.apply;

import com.cqkk.config.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//利用Redis实现高并发计数器
//例子为一天只能发送5封邮件为例
@Component
@PropertySource(value = "classpath:application-dev.properties", ignoreResourceNotFound = true)
public class RedisApplyOne {

    @Resource
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.mail.username}")
    private String mailNm;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${myemail.recipient}")
    private String recipient;//接收者

    @Value("${myemail.cc}")
    private String cc;//抄送人

    @Value("${myemail.bcc}")
    private String bcc;//隐秘抄送人

    public void doSendMail(String username) {
        if (getSetNx(username, 0, 86400)) sendSimpleMail();
        System.out.println("执行完毕");
    }

    /*不存在则进行创建值，并返回实际的值setnx,并设置超时时间*/
    //setIfAbsent如果为空就set值，并返回1 如果存在(不为空)不进行操作，并返回0
    public boolean getSetNx(String user, Object value, long timeOut) {
        String key = new SimpleDateFormat("yyyyMMdd").format(new Date()) + user;
        Boolean absent = redisTemplate.opsForValue().setIfAbsent(key, Integer.parseInt(String.valueOf(value)) + 1, timeOut, TimeUnit.SECONDS);
        if (!absent) redisUtil.incr(key, 1);
        int count = (int) redisUtil.get(key);
        if (count < 5) {
            return true;
        }
        return false;
    }

    //发送邮件
    //抄送：将邮件同时送给收信人以外的人，用户所写的邮件抄送一份给别人,对方可以看见该用户的E-mail发送给了谁。
    //密送：将邮件同时送给收信人以外的人，用户所写的邮件抄送一份给别人,但是对方不能查看到这封邮件同时还发送给了哪些人。
    public void sendSimpleMail() {
        //构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        message.setSubject("么西么西");
        // 设置邮件发送者，这个跟application.yml中设置的要一致
        message.setFrom(mailNm);
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        // message.setTo("10*****16@qq.com","12****32*qq.com");
        message.setTo(recipient);
        // 设置邮件抄送人，可以有多个抄送人
        message.setCc(cc);
        // 设置隐秘抄送人，可以有多个
        message.setBcc(bcc);
        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文
        message.setText("你好呀");
        //发送邮件
        javaMailSender.send(message);
    }

    //发送带附件的邮件
    //注意这里的构建邮件对象的方式跟上面有一点不同，因为这里是需要带附件上传，所以先使用javaMailSender创建一个复杂的邮件对象，
    // 然后使用MimeMessageHelper对邮件进行配置，MimeMessageHelper 是一个邮件配置的辅助工具类，
    // 创建时候的 true 表示构建一个 multipart message 类型的邮件，有了 MimeMessageHelper 之后，
    // 我们针对邮件的配置都是由 MimeMessageHelper 来代劳。然后通过addAttachment()方法添加附件。
    public void sendAttachFileMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // true表示构建一个可以带附件的邮件对象
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("测试邮件");
        helper.setFrom(mailNm);
        helper.setTo("2811702912@qq.com");
        helper.setSentDate(new Date());
        helper.setText("正文");
        // 第一个参数是自定义的名称，后缀需要加上，第二个参数是文件的位置
        helper.addAttachment("lxs.txt", new File("D:/developmentEnvironment/File/file_upload/lkk_20210608231332_lxm.txt"));
        javaMailSender.send(mimeMessage);
    }

    // 正文中带图片的邮件
    public void sendImgResMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom(mailNm);
        helper.setTo("2811702912@qq.com");
        helper.setSentDate(new Date());
        // src='cid:p01' 占位符写法 ，第二个参数true表示这是一个html文本
        helper.setText("<p>hello 大家好，这是一封测试邮件，这封邮件包含两种图片，分别如下</p><p>第一张图片：</p><img src='cid:p01'/><p>第二张图片：</p><img src='cid:p02'/>",true);
        // 第一个参数指的是html中占位符的名字，第二个参数就是文件的位置
        helper.addInline("p01",new FileSystemResource(new File("C:/Users/95640/Pictures/Aurora-1080.jpg")));
        helper.addInline("p02",new FileSystemResource(new File("C:/Users/95640/Pictures/Aurora-1080.jpg")));
        javaMailSender.send(mimeMessage);
    }

    //在公司实际开发中，第一种和第三种都不是使用最多的邮件发送方案。因为正常来说，邮件的内容都是比较的丰富的，
    // 所以大部分邮件都是通过 HTML 来呈现的，如果直接拼接 HTML 字符串，这样以后不好维护，为了解决这个问题，一般邮件发送，都会有相应的邮件模板。

    //使用 Thymeleaf 作邮件模板
    //推荐在 Spring Boot 中使用 Thymeleaf 来构建邮件模板。因为 Thymeleaf 的自动化配置提供了一个 TemplateEngine，
    // 通过 TemplateEngine 可以方便的将 Thymeleaf 模板渲染为 HTML ，同时，Thymeleaf 的自动化配置在这里是继续有效的 。
    public void sendThymeleafMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom(mailNm);
        helper.setTo("2811702912@qq.com");
        helper.setSentDate(new Date());
        // 这里引入的是Template的Context
        Context context = new Context();
        // 设置模板中的变量
        context.setVariable("username", "javaboy");
        context.setVariable("num","000001");
        context.setVariable("salary", "99999");
        // 第一个参数为模板的名称
        String process = templateEngine.process("admin/hello.html", context);
        // 第二个参数true表示这是一个html文本
        helper.setText(process,true);
        javaMailSender.send(mimeMessage);
    }
}
