import { put } from 'redux-saga/effects'
import { push } from 'react-router-redux'
import axios from 'axios'
import jwt_decode from 'jwt-decode'
import { BASE_URL } from '../config'
import {
  loginSuccessStudentAction,
  updateErrorDetailAction,
  loginSuccessAdminAction,
  loginBadCredentialAction,
  registerDuplicateAction,
  registerSuccessAction
} from '../action'

export function * registerUserAsync (action) {
  try {
    const response = yield axios.post(BASE_URL + '/auth/register',
      {
        username: action.data.username,
        password: action.data.password,
        name: action.data.name
      }
    )
    if (response.data.code === 'success') { yield put(registerSuccessAction()) } else if (response.data.code === 'duplicateUsername') { yield put(registerDuplicateAction()) } else {
      yield put(updateErrorDetailAction(response.data.code, 'Register Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'Register Async', error.message))
    yield put(push('/error'))
  }
}

export function * loginUserAsync (action) {
  try {
    const response = yield axios.post(BASE_URL + '/api/v1/auth/login',
      {
        username: action.data.username,
        password: action.data.password,
        rememberMe: action.data.rememberMe
      }
    )
    if (response.data.code === 'success') {
      localStorage.setItem('userToken', response.data.data.token)
      const tokenInfo = jwt_decode(response.data.data.token)
      localStorage.setItem('username', tokenInfo.sub)
      if (tokenInfo.rol === 'ROLE_ADMIN') { yield put(loginSuccessAdminAction()) }
      if (tokenInfo.rol === 'ROLE_STUDENT') { yield put(loginSuccessStudentAction()) }
    } else if (response.data.code === 'badCredential') { yield put(loginBadCredentialAction()) } else {
      yield put(updateErrorDetailAction(response.data.code, 'login Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'login Async', error.message))
    yield put(push('/error'))
  }
}

