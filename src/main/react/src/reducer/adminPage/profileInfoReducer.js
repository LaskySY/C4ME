import { ADMIN } from '../../action/actionType'

export default function (state = [], action) {
  switch (action.type) {
    case ADMIN.GET_ALL_PROFILE_INFO_SAVE:
      const updateData = action.data
      return updateData
    default:
      return state
  }
}