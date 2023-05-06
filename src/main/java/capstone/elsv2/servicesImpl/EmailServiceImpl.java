package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.common.EmailDTO;
import capstone.elsv2.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public String sendSimpleMail(EmailDTO dto)
    {
        // Try block to check for exceptions
        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(dto.getEmail());
            message.setSubject(dto.getSubject());
            message.setText(dto.getMassage());
            this.javaMailSender.send(message);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }
}
