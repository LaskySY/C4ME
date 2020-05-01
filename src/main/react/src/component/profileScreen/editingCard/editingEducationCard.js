import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'
import axios from 'axios'
import AsyncSelect from 'react-select/async'
import Select from 'react-select'
import { push } from 'react-router-redux'
import {
  editingEducationAction,
  updateProfileAction,
  updateErrorDetailAction
} from '../../../action'
import { updateStringCheck, updateNumberCheck } from '../../../util/validateCheck'
import { BASE_URL } from '../../../config'
import LoadingPage from '../../loadingPage'
import ReactTooltip from 'react-tooltip'

class EditingEducationCard extends Component {
  constructor (props) {
    super(props)
    this.majors = []
    this.highSchools = []
    this.state = {
      loading: true,
      schoolName: this.props.profile.schoolName,
      gpa: this.props.profile.gpa,
      schoolYear: this.props.profile.schoolYear,
      numApCourses: this.props.profile.numApCourses,
      major1: this.props.profile.major1,
      major2: this.props.profile.major2
    }
  }

  componentDidUpdate () {
    ReactTooltip.rebuild()
  }

  componentDidMount = () => {
    axios.all([
      axios.get(BASE_URL + '/profile/highSchool',
        {
          headers: { Authorization: localStorage.getItem('userToken') }
        }
      ),
      axios.get(BASE_URL + '/collegeSearch/getmajor',
        {
          headers: { Authorization: localStorage.getItem('userToken') }
        }
      )
    ]).then(axios.spread((highSchoolRes, majorRes) => {
      if (highSchoolRes.data.code === 'success') {
        highSchoolRes.data.data.highSchools.forEach((schoolName, index) => {
          this.highSchools.push({ value: index, label: schoolName })
        })
      } else {
        this.props.updateErrorDetail(highSchoolRes.data.code, 'editing education card', highSchoolRes.data.message)
        this.props.redirectErrorPage()
      }
      if (majorRes.data.code === 'success') {
        this.majors = majorRes.data.data
      } else {
        this.props.updateErrorDetail(majorRes.data.code, 'filter panel - get Major', majorRes.data.message)
        this.props.redirectErrorPage()
      }
      this.setState({ loading: false })
    })).catch(error => {
      this.props.updateErrorDetail(null, 'filter panel', error.message)
      this.props.redirectErrorPage()
    })
  }

  filterName = inputValue => {
    const options = []

    var counter = 0
    var index = 0
    var min = 0
    var max = this.highSchools.length
    var highSchool
    var idx
    index = Math.floor((max - min) / 2)

    var found = false
    while (!found) {
      if (max - min === 1) break
      highSchool = this.highSchools[index]
      var cmp = inputValue.localeCompare(highSchool.label)
      if (cmp < 0) {
        max = index
        index = Math.floor((index + min) / 2)
      } else if (cmp > 0) {
        min = index
        index = Math.floor((max + index) / 2)
      } else break
    }

    while (counter < 10 && index < this.highSchools.length) {
      highSchool = this.highSchools[index]
      idx = this.highSchools[index].value
      if (highSchool.label.toLowerCase().startsWith(inputValue.toLowerCase())) {
        options.push({ value: idx, label: highSchool.label })
        counter++
      }
      index++
    }

    if (counter < 10) {
      index = 0
      while (counter < 10 && index < this.highSchools.length) {
        highSchool = this.highSchools[index]
        idx = this.highSchools[index].value
        if (highSchool.label.toLowerCase().includes(inputValue.toLowerCase()) && !(highSchool.label.toLowerCase().startsWith(inputValue.toLowerCase()))) {
          options.push({ value: idx, label: highSchool.label })
          counter++
        }
        index++
      }
    }
    return options
  }

  loadOptions = (inputValue, callback) => {
    setTimeout(() => {
      callback(this.filterName(inputValue))
    }, 10)
  };

  createoption = (list) => {
    const option = []
    list.map(item =>
      option.push({ value: item, label: item })
    )
    return option
  }

  handleInputChange = newValue => {
    const schoolName = newValue.replace(/\W/g, '')
    this.setState({ schoolName })
    return schoolName
  }

  save = () => {
    this.props.updateProfile({
      schoolName: this.state.schoolName,
      gpa: parseFloat(this.state.gpa),
      schoolYear: this.state.schoolYear,
      numApCourses: this.state.numApCourses,
      major1: this.state.major1,
      major2: this.state.major2
    }, 'education')
    this.props.changeState(false)
  }

  apCheck = value => {
    if (value === null) return true
    if (value < 0 || isNaN(value) || Math.floor(value) !== value) { return false } else { return true }
  }

  gpaCheck = value => {
    if (value === null) return true
    if (value < 0 || value > 5 || isNaN(value)) { return false } else { return true }
  }

  yearCheck = value => {
    if (value === null) return true
    if (value < 2010 || value > 2030 || isNaN(value) || Math.floor(value) !== value) { return false } else { return true }
  }

  render () {
    const isValid = this.apCheck(this.state.numApCourses) && this.gpaCheck(this.state.gpa) && this.yearCheck(this.state.schoolYear)
    return (
      this.state.loading
        ? <LoadingPage fullScreen={false} color="light"/>
        : <div className="card-body">
          <div className="row">
            <div className="col">
              <AsyncSelect id="schoolName" defaultOptions cacheOptions loadOptions={this.loadOptions} value={{ value: null, label: this.state.schoolName }}
                onChange={(option) => this.setState({ schoolName: option.label })} />
            </div>
          </div>
          <div className="row">
            <div className="col">
              <label htmlFor="schoolYear">College Class</label>
              <input type="number" id="schoolYear" data-tip={this.state.schoolYear} data-for='yearTootip'
                className={['profile_textfield form-control', this.yearCheck(this.state.schoolYear) ? '' : 'invalid'].join(' ')}
                value={this.state.schoolYear} onChange={e => this.setState({ schoolYear: updateNumberCheck(e.target.value) })}/>
            </div>
            <div className="col">
              <label htmlFor="numApCourses">Number of AP course</label>
              <input type="number" id="numApCourses" data-tip={this.state.numApCourses} data-for='apTooltip'
                className={['profile_textfield form-control', this.apCheck(this.state.numApCourses) ? '' : 'invalid'].join(' ')}
                value={this.state.numApCourses} onChange={e => this.setState({ numApCourses: updateNumberCheck(e.target.value) })}/>
            </div>
            <div className="col">
              <label htmlFor="gpa">GPA</label>
              <input type="number" id="gpa" step="0.1" data-tip={this.state.gpa} data-for='gpaTooltip'
                className={['profile_textfield form-control', this.gpaCheck(this.state.gpa) ? '' : 'invalid'].join(' ')}
                value={this.state.gpa} onChange={e => this.setState({ gpa: updateStringCheck(e.target.value) })}/>
            </div>
          </div>
          <div className="row">
            <div className="col">
              <label htmlFor="major1">Prefer Major 1</label>
              <Select options={this.createoption(this.majors)}
                defaultValue={{ value: this.state.major1, label: this.state.major1 }}
                onChange={option => this.setState({ major1: option.value })}/>
            </div>
            <div className="col">
              <label htmlFor="major2">Prefer Major 2</label>
              <Select options={this.createoption(this.majors)}
                defaultValue={{ value: this.state.major2, label: this.state.major2 }}
                onChange={option => this.setState({ major2: option.value })}/>
            </div>
          </div>
          <button className="editcard-button btn float-right"
            onClick={() => this.props.changeState(false)}
          >Cancel</button>
          <button className={['editcard-button btn float-right', isValid ? '' : 'disabled'].join(' ')}
            onClick={this.save} disabled={!isValid}>Save</button>
          <ReactTooltip id='yearTootip' place="bottom" type="error" effect="solid"
            getContent= { () => this.yearCheck(this.state.schoolYear) ? null : 'year is an integer, and more than 2010, less than 2030'}/>
          <ReactTooltip id='apTooltip' place="bottom" type="error" effect="solid"
            getContent= { () => this.apCheck(this.state.numApCourses) ? null : 'Number of AP course is an integer, and more than 0'}/>
          <ReactTooltip id='gpaTooltip' place="bottom" type="error" effect="solid"
            getContent= { () => this.gpaCheck(this.state.gpa) ? null : 'GPA should less or equal with 5, and more than 0'}/>
        </div>
    )
  }
}

const mapStateToProps = state => ({
  profile: state.profile.data
})

const mapDispatchToProps = dispatch => ({
  changeState: (...args) => dispatch(editingEducationAction(...args)),
  updateProfile: (...args) => dispatch(updateProfileAction(...args)),
  redirectErrorPage: () => dispatch(push('/error')),
  updateErrorDetail: (...args) => dispatch(updateErrorDetailAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(EditingEducationCard))
