package Pathology.Care.Platform.Cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Pathology.Care.Platform.DTO.ContactMessageDTO;
import Pathology.Care.Platform.Model.ContactMessage;
import Pathology.Care.Platform.Service.ContactService;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/contact")
public class ContactController {

    @Autowired
   public  ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    // =======================
    // 1️⃣ User Contact Page
    // =======================
    @GetMapping("/form")
    public String showContactForm(Model model) {
        model.addAttribute("contactDTO", new ContactMessageDTO());
        return "contact_form";
    }

    @PostMapping("/submit")
    public String submitContact(@ModelAttribute ContactMessageDTO contactDTO, Model model) {
        ContactMessage message = new ContactMessage();
        message.setName(contactDTO.getName());
        message.setEmail(contactDTO.getEmail());
        message.setMessage(contactDTO.getMessage());

        service.saveMessage(message);
        model.addAttribute("success", "Message sent successfully!");
        model.addAttribute("contactDTO", new ContactMessageDTO()); // Reset form

        return "contact_form";
    }

    // =======================
    // 2️⃣ Admin Page - View All Messages
    // =======================
    @GetMapping("/admin")
    public String viewAllMessages(Model model) {
        List<ContactMessage> messages = service.getAllMessages();
        model.addAttribute("messages", messages);
        return "admin_messages";
    }

    // =======================
    // 3️⃣ Admin - Reply to a Message
    // =======================
    @PostMapping("/admin/reply/{id}")
    public String replyMessage(@PathVariable Long id, @RequestParam String reply) {
        service.replyToMessage(id, reply);
        return "redirect:/contact/admin";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {
        service.deleteMessage(id);
        return "redirect:/contact/admin";
    }

}