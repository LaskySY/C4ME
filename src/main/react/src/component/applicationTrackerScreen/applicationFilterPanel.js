import React, { Component } from 'react'
import { push } from 'react-router-redux'
import { connect } from 'react-redux'
import TagFilter from '../FilterPanel/tagFilter'
import LimitFilter from '../FilterPanel/limitFilter'
import { applicationStatusMapping } from '../../config/mapping'
import { updateErrorDetailAction } from '../../action'
import { integerSchema, decimalSchema } from '../../util/validationSchema'
import { createYupSchema } from '../../util/validateCheck'

class Filter extends Component {
  constructor (props) {
    super(props)
    this.dirtyFilters = []
    this.state = {
      collegeClassKey: 'collegeClass',
      loading: true,
      strict: false,
      highSchool: null,
      applicationStatus: null,
      collegeClass: { lower: null, upper: null }
    }
  }

  dirtyCheck = (dirty, stateName) => {
    if (dirty && this.dirtyFilters.indexOf(stateName) <= -1) {
      this.dirtyFilters.push(stateName)
    }
    if (!dirty && this.dirtyFilters.indexOf(stateName) > -1) {
      this.dirtyFilters.splice(this.dirtyFilters.indexOf(stateName), 1)
    }
  }

  confirm = e => {
    this.dirtyFilters.map(filter => {
      this.setState({ [filter + 'Key']: this.state[filter + 'Key'] + '.' })
    })
    e.preventDefault()
    e.stopPropagation()
    this.props.confirm({
      minCollegeClass: this.state.collegeClass.lower,
      maxCollegeClass: this.state.collegeClass.upper,
      highSchools: this.state.highSchool ? this.state.highSchool.map(highSchoolsIndex => this.props.highSchools[highSchoolsIndex]) : null,
      applicationStatus: this.state.applicationStatus ? this.state.applicationStatus.map(applicationStatusIndex => Object.values(applicationStatusMapping)[applicationStatusIndex]) : null,
      strict: this.state.strict
    })
  }

  render () {
    const date = new Date()
    return (
      <div className={'filter_menu ' + this.props.className}>
        <div className="collapse row justify-content-center" id={this.props.id}>
          <div className="card-body col select">
            <div className="row">
              <ul className="list-group list-group-flush col">
                <li className="list-group-item">
                  < LimitFilter
                    key = {this.state.collegeClassKey}
                    idLabel= "class"
                    title = "College Class"
                    value={this.state.collegeClass}
                    dirtyCheck = {(dirty) => this.dirtyCheck(dirty, 'collegeClass')}
                    validationSchema = {
                      integerSchema('class', date.getFullYear() - 10.0, date.getFullYear() + 10).reduce(createYupSchema, {})}
                    onChangeLimit={(lower, upper) => this.setState({ collegeClass: { lower, upper } })}
                  />
                </li>
                <li className="list-group-item ">
                  < TagFilter
                    title = "Application Status"
                    tags = {Object.keys(applicationStatusMapping)}
                    id = "applicationStatus"
                    multiSelect
                    selected = {this.state.applicationStatus}
                    onSelectTag={(value) => this.setState({ applicationStatus: value })}
                  />
                </li>
                <li className="list-group-item ">
                  < TagFilter
                    title = "High Schools"
                    tags = {this.props.highSchools}
                    id = "highSchools"
                    multiSelect
                    expand
                    selected = {this.state.highSchool}
                    onSelectTag={(value) => this.setState({ highSchool: value })}
                  />
                </li>
              </ul>
            </div>
            <div className="row justify-content-center float-right">
              <div className="form-check">
                <input className="form-check-input" type="checkbox" checked={this.state.strict} id="defaultCheck1"
                  onChange={() => this.setState({ strict: !this.state.strict })}/>
                <label className="form-check-label" htmlFor="defaultCheck1">Strict</label>
              </div>
              <div className="col">
                <button className="btn btn-outline-dark btn-sm filter_button float-right"
                  data-toggle="collapse" data-target={'#' + this.props.id} aria-expanded="false" aria-controls="collapsePanel"
                > Cancel</button>
                <button className="btn btn-outline-dark btn-sm filter_button float-right"
                  data-toggle="collapse" data-target={'#' + this.props.id} aria-expanded="false" aria-controls="collapsePanel"
                  onMouseDown={this.confirm}
                > Confirm</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

const mapDispatchToProps = dispatch => ({
  redirectErrorPage: () => dispatch(push('/error')),
  updateErrorDetail: (...args) => dispatch(updateErrorDetailAction(...args))
})

export default connect(
  null,
  mapDispatchToProps
)(Filter)