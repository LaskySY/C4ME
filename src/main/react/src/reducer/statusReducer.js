import { STATUS } from '../action/actionType'

const initialState = {
  language: 'en',
  loginState: 0,
  registerState: 0
}

export default (state = initialState, action) => {
  switch (action.type) {
    case STATUS.LANGUAGE_UPDATE:
      return {
        ...state,
        language: action.newLanguage
      }
    case STATUS.UPDATE_ERROR_DETAIL:
      return {
        ...state,
        error: action.data
      }
    case STATUS.UPDATE_LOGIN_STATE:
      return {
        ...state,
        loginState: action.newState
      }
    case STATUS.UPDATE_REGISTER_STATE:
      return {
        ...state,
        registerState: action.newState
      }
    default:
      return state
  }
}
