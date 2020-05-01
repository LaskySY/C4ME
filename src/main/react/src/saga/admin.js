import { put } from 'redux-saga/effects'
import axios from 'axios'
import { push } from 'react-router-redux'
import {
  updateErrorDetailAction,
  getCollegeInfoAction,
  getCollegeInfoActionAsync,
  getProfileInfoAction,
  getProfileInfoActionAsync,
  getApplicationInfoAction,
  getApplicationInfoActionAsync
} from '../action'
import { BASE_URL } from '../../src/config/index'


export function * getCollegeInfoAsync () {
  try {
    const response = yield axios.get(BASE_URL + '/admin/college',
      { headers: { Authorization: localStorage.getItem('userToken') } }
    )
    if (response.data.code === 'success') {
      yield put(getCollegeInfoAction(response.data.data.collegeInfo))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'get College Info Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'get College Info Async', error.message))
    yield put(push('/error'))
  }
}

export function * scrapCollegeDataAsync () {
  const token = localStorage.getItem('userToken')
  try {
    const response = yield axios.post(BASE_URL + '/admin/college/data',
      {},
      {
        headers: { Authorization: token },
        params: { username: localStorage.getItem('username') }
      }
    )
    if (response.data.code === 'success') {
      console.log('Scraping Data success')
      yield put(getCollegeInfoActionAsync(token))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'scrap College Data Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'scrap College Data Async', error.message))
    yield put(push('/error'))
  }
}

export function * scrapCollegeRankingAsync () {
  const token = localStorage.getItem('userToken')
  try {
    const response = yield axios.post(BASE_URL + '/admin/college/rank',
      {},
      {
        headers: { Authorization: token },
        params: { username: localStorage.getItem('username') }
      }
    )
    if (response.data.code === 'success') {
      console.log('Scraping Ranking success')
      yield put(getCollegeInfoActionAsync(token))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'scrap College Ranking Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'scrap College Ranking Async', error.message))
    yield put(push('/error'))
  }
}

export function * importCollegeScorecardAsync () {
  const token = localStorage.getItem('userToken')
  try {
    const response = yield axios.post(BASE_URL + '/admin/college/scorecard',
      {},
      {
        headers: { Authorization: token },
        params: { username: localStorage.getItem('username') }
      }
    )
    if (response.data.code === 'success') {
      console.log('Importing scorecard success')
      yield put(getCollegeInfoActionAsync(token))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'import College Scorecard Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'import College Scorecard Async', error.message))
    yield put(push('/error'))
  }
}

export function * exportCollegeInfoAsync () {
  try {
    const response = yield axios.post(BASE_URL + '/admin/college/export',
      { headers: { Authorization: localStorage.getItem('userToken') } }
    )
    if (response.data.code === 'success') {
      console.log('Exporting college info success')
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'export College Scorecard Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'export College Scorecard Async', error.message))
    yield put(push('/error'))
  }
}

export function * getProfileInfoAsync () {
  try {
    const response = yield axios.get(BASE_URL + '/admin/profile',
      { headers: { Authorization: localStorage.getItem('userToken') } }
    )
    if (response.data.code === 'success') {
      yield put(getProfileInfoAction(response.data.data.profiles))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'get Profile Info Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'get Profile Info Async', error.message))
    yield put(push('/error'))
  }
}

export function * importProfileAsync () {
  const token = localStorage.getItem('userToken')
  try {
    const response = yield axios.post(BASE_URL + '/admin/profile',
      {},
      {
        headers: { Authorization: token },
        params: { username: localStorage.getItem('username') }
      }
    )
    if (response.data.code === 'success') {
      console.log('Importing profiles success')
      yield put(getProfileInfoActionAsync(token))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'import Profile Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'import Profile Async', error.message))
    yield put(push('/error'))
  }
}

export function * deleteAllProfilesAsync () {
  const token = localStorage.getItem('userToken')
  try {
    const response = yield axios.post(BASE_URL + '/admin/profile/delete',
      {},
      {
        headers: { Authorization: token },
        params: { username: localStorage.getItem('username') }
      }
    )
    if (response.data.code === 'success') {
      console.log('Deleting all profiles success')
      yield put(getProfileInfoActionAsync(token))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'delete All Profiles Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'delete All Profiles Async', error.message))
    yield put(push('/error'))
  }
}

export function * getApplicaitonInfoAsync () {
  try {
    const response = yield axios.get(BASE_URL + '/admin/application',
      { headers: { Authorization: localStorage.getItem('userToken') } }
    )
    if (response.data.code === 'success') {
      const data = response.data.data.applications
      yield put(getApplicationInfoAction(data))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'get Applicaiton Info Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'get Applicaiton Info Async', error.message))
    yield put(push('/error'))
  }
}

export function * changeQuestionableDecisionAsync (action) {
  const token = localStorage.getItem('userToken')
  try {
    const response = yield axios.post(BASE_URL + '/admin/application',
      {
        collegeId: action.data.collegeId,
        questionable: action.questionable
      },
      {
        headers: { Authorization: token },
        params: { username: action.data.username }
      }
    )
    if (response.data.code === 'success') {
      console.log('changed questionable decision success!')
      yield put(getApplicationInfoActionAsync(token))
    } else {
      yield put(updateErrorDetailAction(response.data.code, 'change Questionable Decision Async', response.data.message))
      yield put(push('/error'))
    }
  } catch (error) {
    yield put(updateErrorDetailAction(null, 'change Questionable Decision Async', error.message))
    yield put(push('/error'))
  }
}