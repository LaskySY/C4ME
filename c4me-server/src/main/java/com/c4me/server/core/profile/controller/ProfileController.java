package com.c4me.server.core.profile.controller;

import com.c4me.server.config.exception.DuplicateUsernameException;
import com.c4me.server.config.exception.HighSchoolDoesNotExistException;
import com.c4me.server.config.exception.UserDoesNotExistException;
import com.c4me.server.core.profile.domain.ProfileInfo;
import com.c4me.server.core.profile.domain.ProfileRequest;
import com.c4me.server.core.profile.service.ProfileServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

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
    public BaseResponse<HashMap<String, ProfileInfo>> getUserInfo(@RequestParam String username) throws UserDoesNotExistException {
        //profileService.register(username);
        ProfileInfo pi = profileService.getInfoByUsername(username);
        //ResponseEntity<ProfileInfo> re = new ResponseEntity<ProfileInfo>(pi, null, HttpStatus.OK);

        HashMap<String, ProfileInfo> responseMap = new HashMap<>();
        responseMap.put("profile", pi);

        return BaseResponse.<HashMap<String, ProfileInfo>>builder()
                .code("success")
                .message("")
                .data(responseMap)
                .build();
    }

//    @RequestMapping(method = RequestMethod.POST, path = "/update")
    @PostMapping
    public BaseResponse setUserInfo(@RequestParam String username, @RequestParam String field, @RequestBody ProfileInfo profileInfo) throws UserDoesNotExistException, IOException, HighSchoolDoesNotExistException {

        System.out.println("Test set user info");
        profileInfo.setUsername(username);
        profileService.setProfileInfo(profileInfo, field);

        return BaseResponse.builder()
                .code("success")
                .message("")
                .data(null)
                .build();

    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public BaseResponse setUserInfo() {
        System.out.println("got OPTIONS request");
        return BaseResponse.builder()
                .code("success")
                .message("")
                .data(null)
                .build();
    }


}
