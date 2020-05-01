import { PROFILE } from '../action/actionType'

const initialStatusState = {
  editingEducation: false,
  editingACTGrade: false,
  editingSATGrade: false,
  editingApplication: [],
  data: {}
}

export default (state = initialStatusState, action) => {
  switch (action.type) {
    case PROFILE.EDIT_EDUCATION:
      return {
        ...state,
        editingEducation: action.newState
      }
    case PROFILE.EDIT_SAT_GRADE:
      return {
        ...state,
        editingSATGrade: action.newState
      }
    case PROFILE.EDIT_ACT_GRADE:
      return {
        ...state,
        editingACTGrade: action.newState
      }
    case PROFILE.EDIT_APPLICATION:
      return {
        ...state,
        editingApplication: [
          ...state.editingApplication,
          action.index
        ]
      }
    case PROFILE.UNEDIT_APPLICATION:
      return {
        ...state,
        editingApplication: state.editingApplication.filter(
          application => application !== action.index
        )
      }
    case PROFILE.SAVE_PROFILE:
      return {
        ...state,
        data: action.data
      }
    case PROFILE.UPDATE_PROFILE:
      return {
        ...state,
        data: {
          ...state.data,
          ...action.data
        }
      }
    default:
      return state
  }
}
