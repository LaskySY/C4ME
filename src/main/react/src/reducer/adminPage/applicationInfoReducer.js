import { ADMIN } from '../../action/actionType'

export default function (state = [], action) {
  let updateData
  switch (action.type) {
    case ADMIN.GET_APPLICATIONS_INFO_SAVE:
      updateData = action.data
      return updateData
    default:
      return state
  }
}
