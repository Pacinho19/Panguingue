package pl.pracinho.panguingue.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.pracinho.panguingue.config.UIConfig;

@Controller
public class HomeController {

    @GetMapping
    public String home() {
        return "redirect:" + UIConfig.HOME;
    }
}