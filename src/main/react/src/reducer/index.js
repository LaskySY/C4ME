import { combineReducers } from 'redux'
import { routerReducer } from 'react-router-redux'
import status from './statusReducer'
import collegeInfo from './adminPage/collegeInfoReducer'
import profileInfo from './adminPage/profileInfoReducer'
import applicationInfo from './adminPage/applicationInfoReducer'
import profile from './profileReducer'
import applications from './applicationReducer'
import highSchoolInfo from './highSchoolReducer'

const rootReducer = combineReducers({
  routing: routerReducer,
  applications,
  profile,
  status,
  collegeInfo,
  profileInfo,
  applicationInfo,
  highSchoolInfo
})

export default rootReducer
