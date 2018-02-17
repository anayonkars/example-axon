package code.exampleaxon.accountdomain.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping(value = "echo/{userName}", method = RequestMethod.GET)
    public String test(@PathVariable String userName) {
        return "Hello, " + userName + "!";
    }

}
