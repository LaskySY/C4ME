import * as yup from 'yup'

export const updateStringCheck = (str) => {
  var newValue = str
  if (newValue.trim() === '') newValue = null
  return newValue
}

export const updateNumberCheck = (str) => {
  var newValue
  if (str.trim() === '') { newValue = null } else { newValue = parseFloat(str.trim()) }
  return newValue
}

export const linkCheck = link => {
  if (!link) return null
  if (link.startsWith('https') || link.startsWith('http')) {
    return link
  } else {
    return 'http://' + link
  }
}

export function createYupSchema (schema, config) {
  const { id, validationType, validations = [] } = config
  if (!yup[validationType]) {
    return schema
  }
  let validator = yup[validationType]()
  validations.forEach(validation => {
    const { params, type } = validation
    if (!validator[type]) {
      return
    }
    // console.log(type, params);
    validator = validator[type](...params)
  })
  schema[id] = validator
  return schema
}
