import { put } from 'redux-saga/effects'
import axios from 'axios'
import { push } from 'react-router-redux'
import { updateErrorDetailAction, saveApplicationAction, getApplicationAction } from '../action'

import { BASE_URL } from '../config'


export function * getApplicationAsync (action) {
  try {
    const response = yield axios.get(BASE_URL + '/api/v1/application',
      {
        headers: { Authorization: localStorage.getItem('userToken') },
        params: { username: action.username }
      }
    )
    if (response.data.code === 'success') {
      console.log('get application info success')
      yield put(saveApplicationAction(response.data.data.applications))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'get Application info Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'get Application info Async', error.message))
    yield put(push('/error'))
  }
}

export function * updateApplicationAsync (action) {
  try {
    const response = yield axios.post(BASE_URL + '/api/v1/application',
      {
        collegeId: action.data.college.value,
        admissionTerm: action.data.admissionTerm,
        status: action.data.status
      },
      {
        headers: { Authorization: localStorage.getItem('userToken') },
        params: { username: localStorage.getItem('username') }
      }
    )
    if (response.data.code === 'success') {
      console.log('update application success')
      yield put(getApplicationAction(localStorage.getItem('username')))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'update Application Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'update Application Async', error.message))
    yield put(push('/error'))
  }
}

export function * deleteApplicationAsync (action) {
  try {
    const response = yield axios.post(BASE_URL + '/api/v1/application/delete',
      { collegeId: action.data.collegeId },
      {
        headers: { Authorization: localStorage.getItem('userToken') },
        params: { username: localStorage.getItem('username') }
      }
    )
    if (response.data.code === 'success') {
      console.log('delete application success')
      yield put(getApplicationAction(localStorage.getItem('username')))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'delete Application Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'delete Application Async', error.message))
    yield put(push('/error'))
  }
}