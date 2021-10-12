package com.example.db_hw;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    List<String > persons = new ArrayList();
    MyDB myDB = new MyDB();

    @GetMapping("/")
    public String home(Model model){
        myDB.getAllUsers();
        if(persons.isEmpty()){
            persons = myDB.getPersons();
        }
        model.addAttribute("persons", persons);
        return "index";
    }

    @PostMapping("addUser")
    public String addUser(){
        System.out.println("add user...");
        myDB.addUser();
        return "redirect:/";
    }
}
