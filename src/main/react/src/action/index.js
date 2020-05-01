import * as Status from './status'
import * as User from './user'
import * as Admin from './admin'
import * as Profile from './profile'
import * as Application from './application'
import * as Highschool from './highschool'

export const changeLanguageAction = Status.changeLanguage
export const updateErrorDetailAction = Status.updateErrorDetail
export const clearLoginStateAction = Status.clearLoginState
export const loginSuccessAdminAction = Status.loginSuccessAdmin
export const loginSuccessStudentAction = Status.loginSuccessStudent
export const loginBadCredentialAction = Status.loginBadCredential
export const clearRegisterStateAction = Status.clearRegisterState
export const registerSuccessAction = Status.registerSuccess
export const registerDuplicateAction = Status.registerDuplicate

export const registerAction = User.register
export const loginAction = User.login

export const getCollegeInfoActionAsync = Admin.getCollegeInfoActionAsync
export const getCollegeInfoAction = Admin.getCollegeInfoAction
export const scrapCollegeDataActionAsync = Admin.scrapCollegeDataActionAsync
export const scrapCollegeRankingActionAsync = Admin.scrapCollegeRankingActionAsync
export const importCollegeScorecardActionAsync = Admin.importCollegeScorecardActionAsync
export const exportCollegeInfoActionAsync = Admin.exportCollegeInfoActionAsync
export const getProfileInfoActionAsync = Admin.getProfileInfoActionAsync
export const getProfileInfoAction = Admin.getProfileInfoAction
export const importProfileActionAsync = Admin.importProfileActionAsync
export const deleteAllProfilesActionAsync = Admin.deleteAllProfilesActionAsync
export const getApplicationInfoActionAsync = Admin.getApplicationInfoActionAsync
export const getApplicationInfoAction = Admin.getApplicationInfoAction
export const changeQuestionableDecisionActionAsync = Admin.changeQuestionableDecisionActionAsync

export const editingEducationAction = Profile.editingEducation
export const editingACTGradeAction = Profile.editingACTGrade
export const editingSATGradeAction = Profile.editingSATGrade
export const editingApplicationAction = Profile.editingApplication
export const uneditingApplicationAction = Profile.uneditingApplication
export const saveProfileAction = Profile.saveProfile
export const getProfileAction = Profile.getProfile
export const updateProfileAction = Profile.updateProfile

export const saveApplicationAction = Application.saveApplication
export const getApplicationAction = Application.getApplication
export const updateApplicationAction = Application.updateApplication
export const deleteApplicationAction = Application.deleteApplication

export const searchSimilarHighSchools = Highschool.searchHighSchoolsInfoActionAsync
export const saveSimilarHighSchools = Highschool.saveHighSchoolsInfoAction
