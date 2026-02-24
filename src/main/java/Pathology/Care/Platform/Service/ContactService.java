package Pathology.Care.Platform.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import Pathology.Care.Platform.Model.ContactMessage;
import Pathology.Care.Platform.Repository.ContactMessageRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactMessageRepository repository;

    @Autowired
    private JavaMailSender mailSender;

    public ContactMessage saveMessage(ContactMessage message) {
        return repository.save(message);
    }

    public List<ContactMessage> getAllMessages() {
        return repository.findAll();
    }

    public Optional<ContactMessage> getMessageById(Long id) {
        return repository.findById(id);
    }

    public ContactMessage replyToMessage(Long id, String replyText) {
        ContactMessage message = repository.findById(id).orElseThrow();
        message.setReply(replyText);
        message.setReplied(true);
        message.setRepliedAt(LocalDateTime.now());
        repository.save(message);

        // Send Email to user
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(message.getEmail());
        mail.setSubject("Reply from Pathology Care Platform");
        mail.setText(replyText);
        mailSender.send(mail);

        return message;
    }

    public void deleteMessage(Long id) {
        repository.deleteById(id);
    }
}