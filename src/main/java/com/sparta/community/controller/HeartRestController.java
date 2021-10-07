package com.sparta.community.controller;

import com.sparta.community.dto.HeartRequestDto;
import com.sparta.community.model.UserRoleEnum;
import com.sparta.community.repository.HeartRepository;
import com.sparta.community.security.UserDetailsImpl;
import com.sparta.community.service.HeartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class HeartRestController {

    private final HeartService heartService;

    @Autowired
    public HeartRestController(HeartService heartService) {
        this.heartService = heartService;
    }

    @Secured(value = UserRoleEnum.Authority.USER)
    @PostMapping("/api/posts/{id}/like")
    public String likePost(@PathVariable Long id, @RequestBody HeartRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean liked = heartService.liked(id, requestDto, userDetails);
        if(liked){
            return "true";
        }
        else{
            return "false";
        }
    }
}
