package com.cinepax.mg.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v1/test")
public class TestController {

    @GetMapping("")
    public String index(){
        return "Test/index";
    }

    @PostMapping("/save")
    public void save(@RequestParam(name = "caractere") String[] allCaracteres ,@RequestParam("idRevena") String isRevena , @RequestParam("desc") String desc){
        System.out.println(desc);
        System.out.println(isRevena);
        for (String a : allCaracteres){
            System.out.println(a);
        }
    }

}
