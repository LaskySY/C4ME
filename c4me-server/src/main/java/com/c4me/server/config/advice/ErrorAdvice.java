package com.c4me.server.config.advice;

import com.c4me.server.config.constant.Const;
import com.c4me.server.config.exception.*;
import com.c4me.server.config.interceptor.LogInterceptor;
import com.c4me.server.domain.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-22-2020
 */
@RestControllerAdvice(basePackages = "com.c4me.server")
@Slf4j
public class ErrorAdvice {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @ExceptionHandler({DuplicateUsernameException.class})
  @ResponseBody
  public Object DuplicateUsernameException(DuplicateUsernameException exception) {
    LogInterceptor.logExceptionUnExpect(exception, Const.Error.DUPLICATE_USERNAME);
    logger.error(exception.getMessage());
    return BaseResponse.builder()
        .code(Const.Error.DUPLICATE_USERNAME)
        .message(exception.getMessage())
        .build();
  }

  @ExceptionHandler({UserDoesNotExistException.class})
  @ResponseBody
  public Object UserDoesNotExistException(UserDoesNotExistException exception) {
    LogInterceptor.logExceptionUnExpect(exception, Const.Error.USER_NOT_FOUND);
    logger.error(exception.getMessage());
    return BaseResponse.builder()
            .code(Const.Error.USER_NOT_FOUND)
            .message(exception.getMessage())
            .build();
  }

  @ExceptionHandler({CollegeDoesNotExistException.class})
  @ResponseBody
  public Object CollegeDoesNotExistException(CollegeDoesNotExistException exception) {
      LogInterceptor.logExceptionUnExpect(exception, Const.Error.USER_NOT_FOUND);
      logger.error(exception.getMessage());
      return BaseResponse.builder()
              .code(Const.Error.COLLEGE_NOT_FOUND)
              .message(exception.getMessage())
              .build();
  }

  @ExceptionHandler({NoCollegeScorecardException.class})
  @ResponseBody
  public Object NoCollegeScorecardException(NoCollegeScorecardException exception) {
    LogInterceptor.logExceptionUnExpect(exception, Const.Error.COLLEGE_SCORECARD_NOT_FOUND);
    logger.error(exception.getMessage());
    return BaseResponse.builder()
            .code(Const.Error.COLLEGE_SCORECARD_NOT_FOUND)
            .message(exception.getMessage())
            .build();
  }

  @ExceptionHandler({InvalidCollegeScorecardException.class})
  @ResponseBody
  public Object InvalidCollegeScorecardException(InvalidCollegeScorecardException exception) {
    LogInterceptor.logExceptionUnExpect(exception, Const.Error.INVALID_COLLEGE_SCORECARD);
    logger.error(exception.getMessage());
    return BaseResponse.builder()
            .code(Const.Error.INVALID_COLLEGE_SCORECARD)
            .message(exception.getMessage())
            .build();
  }

  @ExceptionHandler({NoCollegeTxtException.class})
  @ResponseBody
  public Object NoCollegeTxtException(NoCollegeTxtException exception) {
    LogInterceptor.logExceptionUnExpect(exception, Const.Error.COLLEGE_TXT_NOT_FOUND);
    logger.error(exception.getMessage());
    return BaseResponse.builder()
            .code(Const.Error.COLLEGE_TXT_NOT_FOUND)
            .message(exception.getMessage())
            .build();
  }


}