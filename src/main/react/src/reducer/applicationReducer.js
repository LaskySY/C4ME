import { APPLICATION } from '../action/actionType'

export default (state = { data: [] }, action) => {
  switch (action.type) {
    // save application
    case APPLICATION.SAVE_APPLICATION:
      return {
        data: action.data
      }
    // update application
    case APPLICATION.UPDATE_APPLICATION:
      let updateIndex = null
      state.data.map((application, index) =>
        application.college.value === action.data.college.value
          ? updateIndex = index
          : null
      )
      return updateIndex !== null
        ? ({
          data: [
            ...state.data.slice(0, updateIndex),
            action.data,
            ...state.data.slice(updateIndex + 1)
          ]
        })
        : ({
          data: [
            ...state.data,
            action.data
          ]
        })
      // delete application
    case APPLICATION.DELETE_APPLICATION:
      return {
        data:
          state.data.filter(application =>
            application.college.value !== action.data.collegeId
          )
      }
    default:
      return state
  }
}
