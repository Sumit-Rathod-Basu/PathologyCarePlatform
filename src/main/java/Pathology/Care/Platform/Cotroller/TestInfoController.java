package Pathology.Care.Platform.Cotroller;

import Pathology.Care.Platform.Model.TestInfo;
import Pathology.Care.Platform.Service.TestInfoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lab/test")
public class TestInfoController {

    @Autowired
    private TestInfoService service;

    // ➤ Add New Test Page
    @GetMapping("/add")
    public String addTestPage(Model model) {
        model.addAttribute("test", new TestInfo());
        return "add-test";
    }

    // ➤ Save Test
    @PostMapping("/save")
public String saveTest(@ModelAttribute TestInfo test, HttpSession session) {

    Long labId = (Long) session.getAttribute("labId");
    test.setLabId(labId);
    test.setLabName((String) session.getAttribute("labName"));
    service.addTest(test);
    return "redirect:/lab/test/all";
}

// ➤ Show All Tests of this Lab Only
@GetMapping("/all")
public String allTests(Model model, HttpSession session) {

    Long labId = (Long) session.getAttribute("labId");
     System.out.println("LAb Id is :"+labId);
    model.addAttribute("tests", service.getTestsByLab(labId));

    return "all-tests";
}
@GetMapping("/publish/{id}")
public String publishTest(@PathVariable Long id, HttpSession session) {

    Long labId = (Long) session.getAttribute("labId");
    if (labId == null) {
        return "redirect:/lab/login";
    }

    TestInfo test = service.getTestById(id);
    if (test != null) {
        test.setPublish("PUBLIC"); // Change status to PUBLIC
        service.addTest(test);     // Save updated test
    }

    return "redirect:/lab/test/all";
}

    // ➤ Edit Test Page
    @GetMapping("/edit/{id}")
    public String editTest(@PathVariable Long id, Model model) {
        model.addAttribute("test", service.getById(id));
        return "edit-test";
    }

    // ➤ Update Test
    @PostMapping("/update")
    public String updateTest(@ModelAttribute TestInfo test, HttpSession session) {
        Long labId = (Long) session.getAttribute("labId");
        test.setLabId(labId);

        service.updateTest(test);
        return "redirect:/lab/test/all";
    }

    // ➤ Delete Test
    @GetMapping("/delete/{id}")
    public String deleteTest(@PathVariable Long id) {
        service.deleteTest(id);
        return "redirect:/lab/test/all";
    }
}
