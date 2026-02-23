package Pathology.Care.Platform.Cotroller;


import Pathology.Care.Platform.DTO.LabMediaDTO;
import Pathology.Care.Platform.serviceimpl.LabMediaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lab/media")
public class LabMediaController {
     
    @Autowired 
    private  LabMediaService service;

    public LabMediaController(LabMediaService service) {
        this.service = service;
    }

    @GetMapping
    public String showMediaPage(Model model) {
        model.addAttribute("media", service.getMedia());
        return "lab-media";
    }

    @PostMapping
    public String saveMedia(@ModelAttribute("media") LabMediaDTO dto) {
        service.saveOrUpdate(dto);
        return "redirect:/lab/media?success";
    }
}
