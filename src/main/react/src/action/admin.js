import { ADMIN } from './actionType'

export const getCollegeInfoActionAsync = () => ({
  type: ADMIN.GET_ALL_COLLEGE_INFO
})

export const getCollegeInfoAction = (collegeInfo) => ({
  type: ADMIN.GET_ALL_COLLEGE_INFO_SAVE,
  data: collegeInfo
})

export const scrapCollegeDataActionAsync = () => ({
  type: ADMIN.SCRAP_COLLEGE_DATA_ASYNC
})

export const scrapCollegeRankingActionAsync = () => ({
  type: ADMIN.SCRAP_COLLEGE_RANKING_ASYNC
})

export const importCollegeScorecardActionAsync = () => ({
  type: ADMIN.IMPORT_COLLEGE_SCORECARD_ASYNC
})

export const exportCollegeInfoActionAsync = () => ({
  type: ADMIN.EXPORT_COLLEGE_INFO_ASYNC
})

export const getProfileInfoActionAsync = () => ({
  type: ADMIN.GET_ALL_PROFILE_INFO
})

export const getProfileInfoAction = (profileInfo) => ({
  type: ADMIN.GET_ALL_PROFILE_INFO_SAVE,
  data: profileInfo
})

export const importProfileActionAsync = () => ({
  type: ADMIN.IMPORT_PROFILE_ASYNC
})

export const deleteAllProfilesActionAsync = () => ({
  type: ADMIN.DELETE_ALL_PROFILES_ASYNC
})

export const getApplicationInfoActionAsync = () => ({
  type: ADMIN.GET_APPLICATIONS_INFO
})

export const getApplicationInfoAction = (applicationInfo) => ({
  type: ADMIN.GET_APPLICATIONS_INFO_SAVE,
  data: applicationInfo
})

export const changeQuestionableDecisionActionAsync = (application, questionable) => ({
  type: ADMIN.CHANGE_QUESTIONABLE_DECISION_ASYNC,
  data: application,
  questionable: questionable
})