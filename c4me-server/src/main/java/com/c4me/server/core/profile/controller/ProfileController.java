package com.c4me.server.core.profile.controller;

import com.c4me.server.config.exception.DuplicateUsernameException;
import com.c4me.server.config.exception.UserDoesNotExistException;
import com.c4me.server.core.profile.domain.ProfileInfo;
import com.c4me.server.core.profile.domain.ProfileRequest;
import com.c4me.server.core.profile.service.ProfileServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    ProfileServiceImpl profileService;

    @GetMapping
    public BaseResponse<ProfileInfo> getUserInfo(@RequestBody ProfileRequest profileRequest) throws UserDoesNotExistException {
        //profileService.register(profileRequest);
        ProfileInfo pi = profileService.getInfoByUsername(profileRequest.getUsername());
        //ResponseEntity<ProfileInfo> re = new ResponseEntity<ProfileInfo>(pi, null, HttpStatus.OK);

        return BaseResponse.<ProfileInfo>builder()
                .code("success")
                .message("")
                .data(pi)
                .build();
    }

    @PutMapping
    public BaseResponse setUserInfo(@RequestBody ProfileInfo profileInfo) throws UserDoesNotExistException {
        profileService.setProfileInfo(profileInfo);

        return BaseResponse.builder()
                .code("success")
                .message("")
                .data(null)
                .build();

    }

}
