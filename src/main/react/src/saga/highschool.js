import { put } from 'redux-saga/effects'
import { push } from 'react-router-redux'
import axios from 'axios'
import { BASE_URL } from '../../src/config/index'
import {
  saveSimilarHighSchools,
  updateErrorDetailAction
} from '../action'

export function * searchSimilarHighSchoolAsync (action) {
  const token = localStorage.getItem('userToken')
  try {
    const response = yield axios.post(BASE_URL + '/admin/finSimilarHighSchool',
      {
        highschoolName: action.data
      },
      {
        headers: { Authorization: token }
      }
    )
    if (response.data.code === 'success') {
      yield put(saveSimilarHighSchools(response.data.data))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'search similar high School Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'search similar high School Async', error.message))
    yield put(push('/error'))
  }
}