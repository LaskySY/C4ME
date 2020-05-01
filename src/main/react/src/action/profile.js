import { PROFILE } from './actionType'

export const editingApplication = index => ({
  type: PROFILE.EDIT_APPLICATION,
  index
})

export const uneditingApplication = index => ({
  type: PROFILE.UNEDIT_APPLICATION,
  index
})

export const editingEducation = newState => ({
  type: PROFILE.EDIT_EDUCATION,
  newState
})

export const editingSATGrade = newState => ({
  type: PROFILE.EDIT_SAT_GRADE,
  newState
})

export const editingACTGrade = newState => ({
  type: PROFILE.EDIT_ACT_GRADE,
  newState
})

export const getProfile = (username) => ({
  type: PROFILE.GET_PROFILE,
  username
})

export const saveProfile = data => ({
  type: PROFILE.SAVE_PROFILE,
  data
})

export const updateProfile = (data, param) => ({
  type: PROFILE.UPDATE_PROFILE,
  data,
  param
})