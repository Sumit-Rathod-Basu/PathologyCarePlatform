package Pathology.Care.Platform.Cotroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/chat1")
    public String index() {
        return "PCPChatbot";
    }
}