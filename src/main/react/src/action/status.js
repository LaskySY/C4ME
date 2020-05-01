import { STATUS } from './actionType'

export const changeLanguage = newLanguage => ({
  type: STATUS.LANGUAGE_UPDATE,
  newLanguage
})

export const updateErrorDetail = (code, field, message) => ({
  type: STATUS.UPDATE_ERROR_DETAIL,
  data: { code, field, message }
})

export const clearLoginState = () => ({
  type: STATUS.UPDATE_LOGIN_STATE,
  newState: 0
})

export const loginSuccessStudent = () => ({
  type: STATUS.UPDATE_LOGIN_STATE,
  newState: 1
})

export const loginSuccessAdmin = () => ({
  type: STATUS.UPDATE_LOGIN_STATE,
  newState: 2
})

export const loginBadCredential = () => ({
  type: STATUS.UPDATE_LOGIN_STATE,
  newState: 3
})

export const clearRegisterState = () => ({
  type: STATUS.UPDATE_REGISTER_STATE,
  newState: 0
})

export const registerSuccess = () => ({
  type: STATUS.UPDATE_REGISTER_STATE,
  newState: 1
})

export const registerDuplicate = () => ({
  type: STATUS.UPDATE_REGISTER_STATE,
  newState: 2
})