package vn.datnv.generatoruniqueusernamefromfullname.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.datnv.generatoruniqueusernamefromfullname.entity.User;
import vn.datnv.generatoruniqueusernamefromfullname.repository.UserRepository;
import vn.datnv.generatoruniqueusernamefromfullname.util.VNCharacterUtils;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String homePage(Model model) {
        
        List<User> userList = userRepository.findAll(Sort.by(Sort.Direction.ASC, "username"));
        model.addAttribute("userList", userList);
        return "/home-page";
    }

    @PostMapping("/get-username")
    @ResponseBody
    public String getUsername(@RequestBody String fullName) {
        return generatorUsername(fullName);
    }

    @PostMapping("/add-user")
    @ResponseBody
    public void addUser(@RequestBody String fullName) {
        String username = generatorUsername(fullName);
        userRepository.save(User.builder().fullName(fullName).username(username).build());
    }

    public String generatorUsername(String fullName) {
        fullName = VNCharacterUtils.removeAccent(fullName.trim().toLowerCase());
        String username = convertFullNameToUserName(fullName);

        if (!userRepository.existsByUsername(username)) {
            return username;
        }

        String usernameExist = userRepository.findExistUsernameByRegex(username + "\\d+$");

        if (usernameExist == null) {
            return username + "1";
        }

        String[] part = usernameExist.split("(?<=\\D)(?=\\d)");
        int prefixNumber = Integer.parseInt(part[1]) + 1;
        return username + prefixNumber;
    }

    public String convertFullNameToUserName(String fullName) {
        String[] fullNameSplit = fullName.split(" ");

        StringBuilder username = new StringBuilder();
        username.append(StringUtils.capitalize(fullNameSplit[fullNameSplit.length - 1]));

        for (int i = 0; i < fullNameSplit.length - 1; i++) {
            if (StringUtils.hasText(fullNameSplit[i])) {
                username.append(fullNameSplit[i].toUpperCase().charAt(0));
            }
        }
        return username.toString();
    }
}
