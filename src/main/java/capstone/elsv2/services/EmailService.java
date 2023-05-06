package capstone.elsv2.services;


import capstone.elsv2.dto.common.EmailDTO;

public interface EmailService {

    String sendSimpleMail(EmailDTO emailDTO);
}
