package com.sparta.springtrello.workspaceMember.controller;


import com.sparta.springtrello.workspaceMember.service.WsmemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Wsmember")
public class WsmemberController {
    private final WsmemberService wsmemberService;
}
