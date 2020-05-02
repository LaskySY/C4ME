package com.c4me.server.utils;

import lombok.AllArgsConstructor;

/**
 * @Description: Domain object representing a StudentApplication line from applications.csv
 * @Author: Maciej Wlodek
 * @CreateDate: 03-24-2020
 */

@AllArgsConstructor
public class StudentApplicationCSVRecord {
    String userid;
    String college;
    String status;
}
