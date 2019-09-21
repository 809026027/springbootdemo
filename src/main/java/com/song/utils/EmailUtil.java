package com.song.utils;

import com.song.entity.EmailBean;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
/**
 * Created by 17060342 on 2019/6/6.
 */
public class EmailUtil {
    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    /**
     * 发送邮件
     * @param subject 主题
     * @param toUsers 收件人
     * @param ccUsers 抄送
     * @param content 邮件内容
     * @param attachfiles 附件列表  List<Map<String, String>> key: name && file
     */
    public static boolean sendEmail(String subject, String[] toUsers, String[] ccUsers, String content, List<Map<String, String>> attachfiles, EmailBean emailBean) {
        boolean flag = true;
        logger.info("发送邮件开始,主题【{}】,收件人【{}】,抄送人【{}】",subject,toUsers,ccUsers);
        try {
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(emailBean.getHost());
            javaMailSender.setUsername(emailBean.getAuthName());
            javaMailSender.setPassword(emailBean.getPassword());
            javaMailSender.setDefaultEncoding(emailBean.getCharset());
            javaMailSender.setProtocol(emailBean.getProtocol());
            javaMailSender.setPort(emailBean.getPort());

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.auth", emailBean.getAuth());
            properties.setProperty("mail.smtp.timeout", emailBean.getTimeout());
            if(emailBean.isSSL()){
                MailSSLSocketFactory sf = null;
                try {
                    sf = new MailSSLSocketFactory();
                    sf.setTrustAllHosts(true);
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.ssl.socketFactory", sf);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
            javaMailSender.setJavaMailProperties(properties);

            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
            messageHelper.setTo(toUsers);
            if (ccUsers != null && ccUsers.length > 0) {
                messageHelper.setCc(ccUsers);
            }
            messageHelper.setFrom(emailBean.getAuthName());
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

            if (attachfiles != null && attachfiles.size() > 0) {
                for (Map<String, String> attachfile : attachfiles) {
                    String attachfileName = attachfile.get("name");
                    File file = new File(attachfile.get("file"));
                    messageHelper.addAttachment(attachfileName, file);
                }
            }
            javaMailSender.send(mailMessage);

        } catch (Exception e) {
            logger.error("发送邮件失败!", e);
            return false;
        }
        logger.info("发送邮件成功!");
        return flag;
    }
}
