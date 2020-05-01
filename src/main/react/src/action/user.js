import { USER } from './actionType'

export const register = (newUser) => ({
  type: USER.REGISTER,
  data: newUser
})

export const login = (user) => ({
  type: USER.LOGIN,
  data: user
})