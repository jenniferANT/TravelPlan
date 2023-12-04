package com.app.travelplan.controller;

import com.app.travelplan.model.dto.UserDto;
import com.app.travelplan.service.UserService;
import com.app.travelplan.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    @GetMapping("/my")
    public ResponseEntity getMyInfo() {
        return new ResponseEntity(userService.getMyInfor(), HttpStatus.OK);
    }

    @GetMapping("/my-role")
    public ResponseEntity getMyRole() {
        return new ResponseEntity(SecurityUtils.getRoleOfPrincipal(), HttpStatus.OK);
    }

    @PatchMapping("/avatar")
    public ResponseEntity updateAvatar(@RequestParam("id") Long avatarId) {
        return new ResponseEntity(userService.updateAvatar(avatarId), HttpStatus.OK);
    }

    @PatchMapping("/name")
    public ResponseEntity updateName(@RequestParam("name") String name) {
        return new ResponseEntity(userService.updateName(name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getMyInfo(@PathVariable("id")Long id) {
        return new ResponseEntity(userService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return new ResponseEntity(userService.getAll(), HttpStatus.OK);
    }
}