package com.test.omok.User;

import com.test.omok.Form.InputChecker;
import com.test.omok.Form.LoginForm;
import com.test.omok.Form.RegisterForm;
import com.test.omok.Form.ResponseForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseForm> register(@RequestBody RegisterForm inForm){
        ResponseForm response = new ResponseForm();
        if(!InputChecker.checkRegisterForm(inForm)){
            response.setResult("Fail");
            response.setMessage("wrong input");
            response.setData(null);
        }
        else if(userService.getUserByUserName(inForm.getUserName()) != null){
            response.setResult("Fail");
            response.setMessage("userName already exist");
            response.setData(null);
        }
        else{
            response.setResult("Success");
            response.setMessage("register done!");
            response.setData(null);
            userService.addUser(inForm.getUserName(), inForm.getPassword());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-id")
    public ResponseEntity<ResponseForm> checkId(@RequestParam String userName){
        ResponseForm response = new ResponseForm();
        if(!InputChecker.checkuserName(userName)){
            response.setResult("Fail");
            response.setMessage("wrong input");
            response.setData(null);
        }
        else if(userService.getUserByUserName(userName) == null){
            response.setResult("Success");
            response.setMessage("available id");
            response.setData(null);
        }
        else{
            response.setResult("Fail");
            response.setMessage("userName exist");
            response.setData(null);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseForm> login(@RequestBody LoginForm inForm){
        ResponseForm response = new ResponseForm();
        if(!InputChecker.checkLoginForm(inForm)){
            response.setResult("Fail");
            response.setMessage("wrong input");
            response.setData(null);
        }
        else{
            OmokUser user = userService.getUserByUserName(inForm.getUserName());
            if(user == null){
                response.setResult("Fail");
                response.setMessage("wrong userName");
                response.setData(null);
            }
            else if(!user.getPasswd().equals(inForm.getPassword())){
                response.setResult("Fail");
                response.setMessage("wrong password");
                response.setData(null);
            }
            else{
                response.setResult("Success");
                response.setMessage("login success!");
                response.setData(null); // TokenManager.makeToken(inForm)
            }
        }
        return ResponseEntity.ok(response);
    }
}
