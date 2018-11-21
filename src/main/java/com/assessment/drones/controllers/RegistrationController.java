package com.assessment.drones.controllers;

import com.assessment.drones.domain.RegistrationDTO;
import com.assessment.drones.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @RequestMapping(path="/registration", method= RequestMethod.GET)
    public String register(Model model){
        RegistrationDTO registrationDTO = new RegistrationDTO();
        model.addAttribute("registration", registrationDTO);
        return "register";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registerUserAccount(
            @ModelAttribute("user") @Valid RegistrationDTO accountDto,
            BindingResult result) {

//        User registered = new User();
//        if (!result.hasErrors()) {
//            registered = createUserAccount(accountDto, result);
//        }
//        if (registered == null) {
//            result.rejectValue("email", "message.regError");
//        }
        if (result.hasErrors()) {
            return new ModelAndView("registration", "user", accountDto);
        }
        else {
            return new ModelAndView("login", "user", accountDto);
        }
    }
//    private User createUserAccount(UserDto accountDto, BindingResult result) {
//        User registered = null;
//        try {
//            registered = service.registerNewUserAccount(accountDto);
//        } catch (EmailExistsException e) {
//            return null;
//        }
//        return registered;
//    }
}
