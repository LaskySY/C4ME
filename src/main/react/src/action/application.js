import { APPLICATION } from './actionType'


export const getApplication = (username) => ({
  type: APPLICATION.GET_APPLICATION,
  username
})

export const saveApplication = data => ({
  type: APPLICATION.SAVE_APPLICATION,
  data
})

export const updateApplication = data => ({
  type: APPLICATION.UPDATE_APPLICATION,
  data
})

export const deleteApplication = data => ({
  type: APPLICATION.DELETE_APPLICATION,
  data
})