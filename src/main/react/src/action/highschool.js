import { FINDHIGHSCHOOL } from './actionType'

export const searchHighSchoolsInfoActionAsync = (highSchoolName) => ({
  type: FINDHIGHSCHOOL.SEARCH_SIMILAR_HIGHSCHOOLS,
  data: highSchoolName
})

export const saveHighSchoolsInfoAction = (highschoolInfo) => ({
  type: FINDHIGHSCHOOL.SAVE_SIMILAR_HIGHSCHOOLS,
  data: highschoolInfo
})