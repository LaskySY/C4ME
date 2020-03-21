package com.c4me.server.core.profile.service;

import com.c4me.server.config.exception.UserDoesNotExistException;
import com.c4me.server.core.credential.repository.HighschoolRepository;
import com.c4me.server.core.profile.domain.ProfileInfo;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.entities.HighschoolEntity;
import com.c4me.server.entities.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Service
public class ProfileServiceImpl {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    HighschoolRepository highschoolRepository;

    public ProfileInfo getInfoByUsername(String username) throws UserDoesNotExistException {
        ProfileEntity pe = profileRepository.findByUsername(username);
        if(pe == null) {
            throw new UserDoesNotExistException("cannot find user");
        }
        ProfileInfo pi = new ProfileInfo(pe);
//        pi.setName(pe.getUserByUsername().getName());
//        pi.setGpa(pe.getGpa());
//        pi.setNumApCourses(pe.getNumApCourses());
//        pi.setSatMath(pe.getSatMath());
//        pi.setUsername(username);
        return pi;
    }


    public void setProfileInfo(ProfileInfo profileInfo) throws UserDoesNotExistException { //TODO: maybe just copy non-null bean properties instead
        boolean exists = profileRepository.existsById(profileInfo.getUsername());
        if (!exists) throw new UserDoesNotExistException("cannot find user to update profile");
//        ProfileEntity pe = profileRepository.findByUsername(profileInfo.getUsername());
//        if(pe == null) {
//            throw new UserDoesNotExistException("cannot find user");
//        }

        HighschoolEntity he = highschoolRepository.findByName(profileInfo.getSchoolName());
        if(he != null) {
            System.out.println("found highschool "  + profileInfo.getSchoolName());
        }
        else {
            System.out.println("could not find highschool " + profileInfo.getSchoolName());
            //TODO: have to scrape hs website (unless profileInfo.getName() == null)
        }
        //TODO: Fix major 1 and major 2 - should be querying major and majorAlias tables to check if it's a proper major name; then set majorByMajor1 and majorByMajor2 of pe
        //TODO: Either create a major doesn't exist exception, or make a getAllMajors request --> dropdown to choose major.

        ProfileEntity pe = ProfileEntity.builder()
                .username(profileInfo.getUsername())
                .schoolYear(profileInfo.getSchoolYear())
                //.schoolId(hs_id)
                .highschoolBySchoolId(he)
                .numApCourses(profileInfo.getNumApCourses())
                .gpa(profileInfo.getGpa())
                .satMath(profileInfo.getSatMath())
                .satEbrw(profileInfo.getSatEbrw())
                .actMath(profileInfo.getActMath())
                .actEnglish(profileInfo.getActEnglish())
                .actReading(profileInfo.getActReading())
                .actScience(profileInfo.getActScience())
                .actComposite(profileInfo.getActComposite())
                .satLiterature(profileInfo.getSatLiterature())
                .satUsHist(profileInfo.getSatUsHist())
                .satWorldHist(profileInfo.getSatWorldHist())
                .satMathI(profileInfo.getSatMathI())
                .satMathIi(profileInfo.getSatMathIi())
                .satEcoBio(profileInfo.getSatEcoBio())
                .satMolBio(profileInfo.getSatMolBio())
                .satChemistry(profileInfo.getSatChemistry())
                .satPhysics(profileInfo.getSatPhysics())
                .major1(profileInfo.getMajor1())
                .major2(profileInfo.getMajor2()).build();

        profileRepository.save(pe);


    }
}
