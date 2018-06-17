package code.exampleaxon.accountdomain.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping(value = "echo/{userName}")
    public String test(@PathVariable String userName) {
        return "Hello, " + userName + "!";
    }

}
