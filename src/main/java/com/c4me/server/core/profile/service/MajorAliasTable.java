package com.c4me.server.core.profile.service;

import com.c4me.server.core.profile.repository.MajorAliasRepository;
import com.c4me.server.entities.MajorAliasEntity;
import com.c4me.server.entities.MajorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: Table of major aliases
 * @Author: Maciej Wlodek
 * @CreateDate: 04-12-2020
 */

@Service
public class MajorAliasTable {

    @Autowired
    private MajorAliasRepository majorAliasRepository;

    private static Map<String, MajorEntity> aliases = new HashMap<>();

    public MajorAliasTable() {

    }

    /**
     * Initialize the table on startup
     */
    @PostConstruct
    public void init() {
        List<MajorAliasEntity> majorAliasEntityCollection = majorAliasRepository.findAll();
        for(MajorAliasEntity mae : majorAliasEntityCollection) {
            aliases.put(mae.getAlias(), mae.getMajorByMajorName());
        }
    }

    /**
     * Parse major name to get a match in our database. Currently ad-hoc, but I haven't a better idea at the moment.
     * @param major {@link String}
     * @return {@link MajorEntity} the best match in our database
     */
    //this is ad-hoc, but I haven't a better idea.
    public MajorEntity parseMajorName(String major) {
        if(major == null || major.length() == 0) return null;

        for(MajorEntity majorEntity : aliases.values()) {
            if(majorEntity.getName().equals(major)) return majorEntity;
        }
        for(String alias : aliases.keySet()) {
            //System.out.println("major = " + major + ", alias = " + alias);
            if(major.contains(alias)) return aliases.get(alias);
        }

        if(major.contains("Engineering") && aliases.values().stream().anyMatch(e -> e.getName().equals("Engineering"))) return MajorEntity.builder().name("Engineering").build();
        return null;
    }

}
