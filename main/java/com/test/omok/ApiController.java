package com.test.omok;

import com.test.omok.Form.ResponseForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/hello")
    public ResponseEntity<ResponseForm> hello(){
        ResponseForm response = new ResponseForm();
        response.setResult("Success");
        response.setMessage("hello client! welcome to SpringBootServer!");
        response.setData(null);
        return ResponseEntity.ok(response);
    }
}
