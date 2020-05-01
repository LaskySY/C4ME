
import { FINDHIGHSCHOOL } from '../action/actionType'

export default function (state = [], action) {
  let updateData
  switch (action.type) {
    case FINDHIGHSCHOOL.SAVE_SIMILAR_HIGHSCHOOLS:
      updateData = action.data
      return updateData
    default:
      return state
  }
}
