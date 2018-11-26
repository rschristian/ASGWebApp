package com.assessment.drones.controllers;

import com.assessment.drones.domain.FlyTraining;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    //method to take the user to the admin page
    @RequestMapping(path = "/admin", method = RequestMethod.GET)
    public String viewAdmin (){
        return "admin";
    }

    @RequestMapping(path = "/admin/flyingTraining", method = RequestMethod.GET)
    public String viewFlyTraining () {
        return "flyTrainForm";
    }

    @RequestMapping(path = "/admin/flyingTraining", method = RequestMethod.POST)
    public String getFlyTraining (@RequestParam("candidateNum") String candidateNum,
                                  @RequestParam("type") String type,
                                  @RequestParam("instructorId") Long instructorId,
                                  @RequestParam("skillsDate") String skillsDate){
        FlyTraining train = new FlyTraining();
        train.setCandidateNum(candidateNum);
        train.setType(type);
        train.setInstructorId(instructorId);
        train.setSkillsDate(skillsDate);

        return "admin";
    }
}
