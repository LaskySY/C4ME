import { takeEvery, all } from 'redux-saga/effects'
import { USER, ADMIN, PROFILE, APPLICATION, FINDHIGHSCHOOL } from '../action/actionType'
import { registerUserAsync, loginUserAsync } from './user'
import { getProfileAsync, updateProfileAsync } from './profile'
import {
  getApplicationAsync,
  updateApplicationAsync,
  deleteApplicationAsync
} from './application'

import {
  getCollegeInfoAsync,
  scrapCollegeDataAsync,
  scrapCollegeRankingAsync,
  importCollegeScorecardAsync,
  exportCollegeInfoAsync,
  getProfileInfoAsync,
  importProfileAsync,
  deleteAllProfilesAsync,
  getApplicaitonInfoAsync,
  changeQuestionableDecisionAsync
} from './admin'

import {
  searchSimilarHighSchoolAsync
} from './highschool'

export function * load () {
  yield takeEvery(USER.REGISTER, registerUserAsync)
  yield takeEvery(USER.LOGIN, loginUserAsync)
  yield takeEvery(ADMIN.GET_ALL_COLLEGE_INFO, getCollegeInfoAsync)
  yield takeEvery(ADMIN.SCRAP_COLLEGE_DATA_ASYNC, scrapCollegeDataAsync)
  yield takeEvery(ADMIN.SCRAP_COLLEGE_RANKING_ASYNC, scrapCollegeRankingAsync)
  yield takeEvery(ADMIN.IMPORT_COLLEGE_SCORECARD_ASYNC, importCollegeScorecardAsync)
  yield takeEvery(ADMIN.EXPORT_COLLEGE_INFO_ASYNC, exportCollegeInfoAsync)
  yield takeEvery(ADMIN.GET_ALL_PROFILE_INFO, getProfileInfoAsync)
  yield takeEvery(ADMIN.IMPORT_PROFILE_ASYNC, importProfileAsync)
  yield takeEvery(ADMIN.DELETE_ALL_PROFILES_ASYNC, deleteAllProfilesAsync)
  yield takeEvery(ADMIN.GET_APPLICATIONS_INFO, getApplicaitonInfoAsync)
  yield takeEvery(ADMIN.CHANGE_QUESTIONABLE_DECISION_ASYNC, changeQuestionableDecisionAsync)
  yield takeEvery(PROFILE.GET_PROFILE, getProfileAsync)
  yield takeEvery(APPLICATION.GET_APPLICATION, getApplicationAsync)
  yield takeEvery(PROFILE.UPDATE_PROFILE, updateProfileAsync)
  yield takeEvery(APPLICATION.UPDATE_APPLICATION, updateApplicationAsync)
  yield takeEvery(APPLICATION.DELETE_APPLICATION, deleteApplicationAsync)
  yield takeEvery(FINDHIGHSCHOOL.SEARCH_SIMILAR_HIGHSCHOOLS, searchSimilarHighSchoolAsync)
}

export default function * rootSaga () {
  yield all([load()])
}
