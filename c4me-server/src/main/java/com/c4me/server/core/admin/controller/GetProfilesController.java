package com.c4me.server.core.admin.controller;

import com.c4me.server.core.profile.domain.ProfileInfo;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.domain.BaseResponse;
import com.c4me.server.entities.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-24-2020
 */

@RestController
@RequestMapping("/admin/profile")
public class GetProfilesController {

    @Autowired
    ProfileRepository profileRepository;

    @GetMapping
    public BaseResponse<HashMap<String, List<ProfileInfo>>> getAllProfiles() {
        List<ProfileEntity> allProfiles = profileRepository.findAll();
        List<ProfileInfo> profiles = allProfiles.stream().map(ProfileInfo::new).collect(Collectors.toList());

        HashMap<String, List<ProfileInfo>> map = new HashMap<>();
        map.put("profiles", profiles);
        return BaseResponse.<HashMap<String, List<ProfileInfo>>>builder().code("success").message("").data(map).build();
    }
}
