package com.c4me.server.core.profile.service;

import com.c4me.server.config.exception.HighSchoolDoesNotExistException;
import com.c4me.server.config.exception.UserDoesNotExistException;
import com.c4me.server.core.credential.repository.HighschoolRepository;
import com.c4me.server.core.profile.domain.ProfileInfo;
import com.c4me.server.core.profile.repository.MajorRepository;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.entities.HighschoolEntity;
import com.c4me.server.entities.MajorEntity;
import com.c4me.server.entities.ProfileEntity;
import com.c4me.server.utils.CopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.c4me.server.config.constant.Const.ProfilePropertyClasses.*;

/**
 * @Description: Implementation of the profile service
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Service
public class ProfileServiceImpl {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    HighschoolRepository highschoolRepository;
    @Autowired
    HighSchoolScraperServiceImpl highSchoolScraperService;
    @Autowired
    MajorRepository majorRepository;

    /**
     * Get profile info for a given student
     * @param username {@link String}
     * @return {@link ProfileInfo}
     * @throws UserDoesNotExistException
     */
    public ProfileInfo getInfoByUsername(String username) throws UserDoesNotExistException {
        ProfileEntity pe = profileRepository.findByUsername(username);
        if(pe == null) {
            throw new UserDoesNotExistException("cannot find user");
        }
        ProfileInfo pi = new ProfileInfo(pe);
        return pi;
    }

    /**
     * Create an entity object from a domain object and the high school.
     * @param profileInfo {@link ProfileInfo}
     * @param he {@link HighschoolEntity}
     * @return {@link ProfileEntity}
     */
    public ProfileEntity entityFromDomain(ProfileInfo profileInfo, HighschoolEntity he) {
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

        return pe;
    }

    /**
     * Set profile info according to the group of properties specified by field
     * @param profileInfo {@link ProfileInfo}
     * @param field {@link String} should be either "education", "act", or "sat"
     * @throws UserDoesNotExistException
     * @throws IOException
     * @throws HighSchoolDoesNotExistException
     */
    public void setProfileInfo(ProfileInfo profileInfo, String field) throws UserDoesNotExistException, IOException, HighSchoolDoesNotExistException {
        if(!PROPERTIES_MAP.containsKey(field)) {
            field = NONE;
        }

        boolean exists = profileInfo.getUsername() != null && profileRepository.existsById(profileInfo.getUsername());
        if (!exists) throw new UserDoesNotExistException("cannot find user to update profile");

        ProfileEntity existingEntity = profileRepository.findByUsername(profileInfo.getUsername());

        HighschoolEntity he = null;
        MajorEntity major1 = null;
        MajorEntity major2 = null;
        if(field.equals(EDUCATION)) {
            if (profileInfo.getSchoolName() != null) {
                System.out.println("searching for school " + profileInfo.getSchoolName());
                he = highschoolRepository.findByName(profileInfo.getSchoolName());
                if (he != null) {
                    System.out.println("found highschool " + profileInfo.getSchoolName());
                } else  {
                    System.out.println("could not find highschool " + profileInfo.getSchoolName() + " in database; scraping from niche.com");
                    he = highSchoolScraperService.scrapeHighSchool(profileInfo.getSchoolName(), false);
                }
            }

//            /*
//             TODO: Fix major 1 and major 2 - should be querying major and majorAlias tables to check if it's a proper major name; then set majorByMajor1 and majorByMajor2 of pe
//              Either create a major doesn't exist exception, or make a getAllMajors request --> dropdown to choose major.
//             */
//
//            major1 = MajorEntity.builder().name(profileInfo.getMajor1()).build();
//            major2 = MajorEntity.builder().name(profileInfo.getMajor2()).build();
//            try {
//                majorRepository.save(major1);
//            } catch (Exception e) {
//                System.out.println("ex1");
//                e.printStackTrace();
//            }
//            try {
//                majorRepository.save(major2);
//            } catch (Exception e) {
//                System.out.println("ex");
//                e.printStackTrace();
//            }
            MajorAliasTable majorAliasTable = new MajorAliasTable();
            major1 = majorAliasTable.parseMajorName(profileInfo.getMajor1());
            major2 = majorAliasTable.parseMajorName(profileInfo.getMajor2());
        }

        ProfileEntity pe = entityFromDomain(profileInfo, he);
        pe.setMajorByMajor1(major1);
        pe.setMajorByMajor2(major2);

        String[] propertiesToIgnore = CopyUtils.invertProperties(pe, PROPERTIES_MAP.get(field));
        //BeanUtils.copyProperties(pe, existingEntity, CopyUtils.getNullPropertyNames(pe));
        BeanUtils.copyProperties(pe, existingEntity, propertiesToIgnore);


        profileRepository.save(existingEntity);
    }
}
