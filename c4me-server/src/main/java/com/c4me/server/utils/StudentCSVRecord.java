package com.c4me.server.utils;

import lombok.AllArgsConstructor;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-22-2020
 */

@AllArgsConstructor
public class StudentCSVRecord {
    String userid;
    String password;
    String residence_state;
    String high_school_name;
    String high_school_city;
    String high_school_state;
    Double GPA;
    Integer college_class;
    String major_1;
    String major_2;
    Integer SAT_math;
    Integer SAT_EBRW;
    Integer ACT_English;
    Integer ACT_math;
    Integer ACT_reading;
    Integer ACT_science;
    Integer ACT_composite;
    Integer SAT_literature;
    Integer SAT_US_hist;
    Integer SAT_world_hist;
    Integer SAT_math_I;
    Integer SAT_math_II;
    Integer SAT_eco_bio;
    Integer SAT_mol_bio;
    Integer SAT_chemistry;
    Integer SAT_physics;
    Integer num_AP_passed;
}
