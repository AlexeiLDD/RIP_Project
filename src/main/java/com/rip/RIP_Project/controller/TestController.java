package com.rip.RIP_Project.controller;

import com.rip.RIP_Project.dto.TestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/")
public class TestController {
    public TestController() {}

    @GetMapping("test1")
    public ResponseEntity<TestResponse> test1() {
        var test1 = new TestResponse();
        test1.setTest(true);
        test1.setTestNumber(1);

        return new ResponseEntity<>(test1, HttpStatus.OK);
    }
}
