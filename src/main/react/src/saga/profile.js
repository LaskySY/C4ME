import { put } from 'redux-saga/effects'
import { push } from 'react-router-redux'
import axios from 'axios'
import { saveProfileAction, getProfileAction, updateErrorDetailAction } from '../action'
import { BASE_URL } from '../config'


export function * getProfileAsync (action) {
  try {
    const response = yield axios.get(BASE_URL + '/profile',
      {
        headers: { Authorization: localStorage.getItem('userToken') },
        params: { username: action.username }
      }
    )
    if (response.data.code === 'success') {
      yield put(saveProfileAction(response.data.data.profile))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'get profile Info Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'get profile Info Async', error.message))
    yield put(push('/error'))
  }
}

export function * updateProfileAsync (action) {
  try {
    const response = yield axios.post(BASE_URL + '/profile',
      action.data,
      {
        headers: { Authorization: localStorage.getItem('userToken') },
        params: {
          username: localStorage.getItem('username'),
          field: action.param
        }
      }
    )
    if (response.data.code === 'success') {
      console.log('update profile success')
      yield put(getProfileAction(localStorage.getItem('username')))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'update profile Async' + action.param, response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'update profile Async' + action.param, error.message))
    yield put(push('/error'))
  }
}