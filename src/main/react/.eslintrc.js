module.exports = {
  env: {
    browser: true,
    es6: true
  },
  extends: [
    'plugin:react/recommended',
    'standard'
  ],
  globals: {
    Atomics: 'readonly',
    SharedArrayBuffer: 'readonly'
  },
  parserOptions: {
    ecmaFeatures: {
      jsx: true
    },
    ecmaVersion: 2018,
    sourceType: 'module'
  },
  plugins: [
    'react'
  ],
  rules: {
    "no-irregular-whitespace": 2,
    "react/prop-types": 0,
    'eol-last': 0,
    'no-multiple-empty-lines': [2, {
      max: 2, // 文件内最多连续 2 个
      maxEOF: 1, // 文件末尾最多连续 1 个
      maxBOF: 1 // 文件头最多连续 1 个
    }],
    "object-curly-newline": 2
  },
  parser: "babel-eslint"
}