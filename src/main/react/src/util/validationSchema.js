import * as yup from 'yup'

export function integerSchema (id, min, max) {
  const maxV = {
    type: 'max',
    params: [max, id + ' cannot be more than ' + max]
  }
  const minV = {
    type: 'min',
    params: min === 0
      ? [min, id + ' should positive']
      : [min, id + ' cannot be less than ' + min]
  }
  const maxScale = {
    type: 'test',
    params: ['test scale', 'Invalid Scale',
      function (upperValue) {
        const lowerValue = this.parent[id + ' lower']
        if (lowerValue !== undefined && lowerValue > upperValue) { return false } else { return true }
      }]
  }
  const minScale = {
    type: 'test',
    params: ['test scale', 'Invalid Scale',
      function (lowerValue) {
        const upperValue = this.parent[id + ' upper']
        if (upperValue !== undefined && lowerValue > upperValue) { return false } else { return true }
      }]
  }
  return [
    {
      id: id + ' lower',
      validationType: 'number',
      validations: [minV, maxV, minScale, {
        type: 'integer',
        params: [id + ' should be an integer']
      }]
    },
    {
      id: id + ' upper',
      validationType: 'number',
      validations: [minV, maxV, maxScale, {
        type: 'integer',
        params: [id + ' should be an integer']
      }]
    }
  ]
}

export function decimalSchema (id, min, max) {
  const maxV = {
    type: 'max',
    params: [max, id + ' cannot be more than ' + max]
  }
  const minV = {
    type: 'min',
    params: min === 0
      ? [min, id + ' should positive']
      : [min, id + ' cannot be less than ' + min]
  }
  const maxScale = {
    type: 'test',
    params: ['test scale', 'Invalid Scale',
      function (upperValue) {
        const lowerValue = this.parent[id + ' lower']
        if (lowerValue !== undefined && lowerValue > upperValue) { return false } else { return true }
      }]
  }
  const minScale = {
    type: 'test',
    params: ['test scale', 'Invalid Scale',
      function (lowerValue) {
        const upperValue = this.parent[id + ' upper']
        if (upperValue !== undefined && lowerValue > upperValue) { return false } else { return true }
      }]
  }
  return [
    {
      id: id + ' lower',
      validationType: 'number',
      validations: [minV, maxV, minScale]
    },
    {
      id: id + ' upper',
      validationType: 'number',
      validations: [minV, maxV, maxScale]
    }
  ]
}