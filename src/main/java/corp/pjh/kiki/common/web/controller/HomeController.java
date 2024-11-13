package corp.pjh.kiki.common.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "딥링킹 or 플레이,앱 스토어로 리다이렉트";
    }

}
