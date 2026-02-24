package Pathology.Care.Platform.Cotroller;

import org.springframework.web.bind.annotation.*;

import Pathology.Care.Platform.Service.OpenRouterService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final OpenRouterService service;

    public ChatController(OpenRouterService service) {
        this.service = service;
    }

    @PostMapping
    public String chat(@RequestParam String message) {
        return service.getResponse(message);
    }
}