package com.heraizen.security.service;

import com.heraizen.security.domain.User;
import com.heraizen.security.dto.UserDto;
import com.heraizen.security.exception.UserException;
import com.heraizen.security.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.zip.DataFormatException;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public String registerUser(UserDto userDto, HttpServletRequest request) throws DataFormatException {

        try {
            Assert.notNull(userDto, "User object is null");
            log.info("User with username: {}", userDto.getName());
            User user = new User();
            BeanUtils.copyProperties(userDto, user);
            user.setPassword(encoder.encode(userDto.getPassword()));
            user.setRole("USER");
            user.setEnabled(false);
            user.setVerificationCode(UUID.randomUUID().toString());
            userRepository.save(user);
            sendVerificationEmail(user, request);
            return "User registered successfully";
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataFormatException("Exception while registering user");
        }
    }

    @Override
    public void sendVerificationEmail(User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        try {
            String toAddress = user.getEmail();
            String fromAddress = "noRep";
            String senderName = "Spring security";
            String subject = "Please verify your registration";
            String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>" + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Your company name.";

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

//        helper.setFrom(fromAddress.trim().replaceAll(" ", ""), senderName);
            log.info("Email: {}", fromAddress);
            helper.setTo(toAddress.trim().replaceAll(" ", ""));
            helper.setSubject(subject);
            String siteURL = getSiteUrl(request);
            content = content.replace("[[name]]", user.getName());
            String verifyURL = siteURL + "/api/auth/verify?code=" + user.getVerificationCode();
            content = content.replace("[[URL]]", verifyURL);
            log.info("Content of mail: {}", content);
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.info("Exception while sending verification mail to user: {}", e.getMessage());
        }

    }

    @Override
    public String verifyUser(String token) {
        try {
            User user = userRepository.findByVerificationCode(token);
            if (user != null) {
                user.setEnabled(true);
                userRepository.save(user);
                return "Successfully, verified user.";
            }
            return "User not found";
        } catch (Exception e) {
            throw new UserException("Exception while verifying user");
        }

    }

    private String getSiteUrl(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
