package com.grash.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userSettings")
@Api(tags = "userSettings")
@RequiredArgsConstructor
public class UserSettingsController {
}
