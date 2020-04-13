package com.c4me.server.core.test.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.config.constant.Const;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-12-2020
 */

@RestController
@RequestMapping("/reloadConfig")
public class ReloadConfigController {

    @PostMapping
    @LogAndWrap(log="reload config")
    public void reloadConfig() {
        Const.Filenames.readConfigFile();
    }
}
