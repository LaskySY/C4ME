import React, { Component } from 'react'
import { BASE_URL } from '../../config'
import { push } from 'react-router-redux'
import { connect } from 'react-redux'
import axios from 'axios'
import TagFilter from '../FilterPanel/tagFilter'
import LimitFilter from '../FilterPanel/limitFilter'
import { stateMapping, regionMapping } from '../../config/mapping'
import { updateErrorDetailAction } from '../../action'
import { integerSchema, decimalSchema } from '../../util/validationSchema'
import { createYupSchema } from '../../util/validateCheck'

class Filter extends Component {
  constructor (props) {
    super(props)
    this.majorList = []
    this.dirtyFilters = []
    this.state = {
      rankKey: 'rank',
      admissionRateKey: 'admissionRate',
      costOfAttendanceKey: 'costOfAttendance',
      numStudentsEnrolledKey: 'numStudentsEnrolled',
      satMathKey: 'satMath',
      satEbrwKey: 'satEbrw',
      actCompositeKey: 'actComposite',
      loading: true,
      strict: false,
      states: null,
      major: null,
      region: null,
      rank: { lower: null, upper: null },
      admissionRate: { lower: null, upper: null },
      costOfAttendance: { lower: null, upper: null },
      numStudentsEnrolled: { lower: null, upper: null },
      satMath: { lower: null, upper: null },
      satEbrw: { lower: null, upper: null },
      actComposite: { lower: null, upper: null }
    }
  }

  componentDidMount = () => {
    axios.get(BASE_URL + '/collegeSearch/getmajor',
      {
        headers: { Authorization: localStorage.getItem('userToken') }
      }
    ).then(res => {
      if (res.data.code === 'success') {
        this.majorList = res.data.data
        this.setState({ loading: false })
      } else {
        this.props.updateErrorDetail(res.data.code, 'filter panel - get Major', res.data.message)
        this.props.redirectErrorPage()
      }
    }).catch(error => {
      this.props.updateErrorDetail(null, 'filter panel', error.message)
      this.props.redirectErrorPage()
    })
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
      minAdmissionRate: this.state.admissionRate.lower,
      maxAdmissionRate: this.state.admissionRate.upper,
      minCostOfAttendance: this.state.costOfAttendance.lower,
      maxCostOfAttendance: this.state.costOfAttendance.upper,
      states: this.state.states ? this.state.states.map(regionIndex => Object.keys(stateMapping)[regionIndex]) : null,
      majors: this.state.major ? this.state.major.map(majorsIndex => this.majorList[majorsIndex]) : null,
      region: this.state.region ? this.state.region.map(regionIndex => Object.values(regionMapping)[regionIndex]) : null,
      minRanking: this.state.rank.lower,
      maxRanking: this.state.rank.upper,
      minNumStudentsEnrolled: this.state.numStudentsEnrolled.lower,
      maxNumStudentsEnrolled: this.state.numStudentsEnrolled.upper,
      minSatMath50: this.state.satMath.lower,
      maxSatMath50: this.state.satMath.upper,
      minSatEbrw50: this.state.satEbrw.lower,
      maxSatEbrw50: this.state.satEbrw.upper,
      minActComposite: this.state.actComposite.lower,
      maxActComposite: this.state.actComposite.upper,
      strict: this.state.strict
    })
  }

  render () {
    return (
      <div className={'filter_menu ' + this.props.className}>
        <div className="collapse row justify-content-center" id={this.props.id}>
          <div className="card-body col select">
            <div className="row">
              <ul className="list-group list-group-flush col-6">
                <li className="list-group-item">
                  < LimitFilter
                    key= { this.state.rankKey }
                    title = "Rank"
                    idLabel = "rank"
                    value={this.state.rank}
                    dirtyCheck = {(dirty) => this.dirtyCheck(dirty, 'rank')}
                    validationSchema = {integerSchema('rank', 0, 1000).reduce(createYupSchema, {})}
                    onChangeLimit={(lower, upper) => this.setState({ rank: { lower, upper } })}
                  />
                </li>
                <li className="list-group-item">
                  < LimitFilter
                    key= { this.state.costOfAttendanceKey }
                    title = "Tuition"
                    idLabel = "tuition"
                    value={this.state.costOfAttendance}
                    dirtyCheck = {(dirty) => this.dirtyCheck(dirty, 'costOfAttendance')}
                    validationSchema = {integerSchema('tuition', 0, 1000000).reduce(createYupSchema, {})}
                    onChangeLimit={(lower, upper) => this.setState({ costOfAttendance: { lower, upper } })}
                  />
                </li>
                <li className="list-group-item">
                  < LimitFilter
                    key= { this.state.admissionRateKey }
                    title = "Admission Rate"
                    idLabel = "rate"
                    value={this.state.admissionRate}
                    dirtyCheck = {(dirty) => this.dirtyCheck(dirty, 'admissionRate')}
                    validationSchema = {decimalSchema('rate', 0, 1).reduce(createYupSchema, {})}
                    onChangeLimit={(lower, upper) => this.setState({ admissionRate: { lower, upper } })}
                  />
                </li>
              </ul>
              <ul className="list-group list-group-flush col-6">
                <li className="list-group-item">
                  < LimitFilter
                    key= { this.state.satMathKey }
                    title = "SAT Math"
                    idLabel = "math"
                    value={this.state.satMath}
                    dirtyCheck = {(dirty) => this.dirtyCheck(dirty, 'satMath')}
                    validationSchema = {integerSchema('math', 200, 800).reduce(createYupSchema, {})}
                    onChangeLimit={(lower, upper) => this.setState({ satMath: { lower, upper } })}
                  />
                </li>
                <li className="list-group-item">
                  < LimitFilter
                    key= { this.state.satEbrwKey }
                    title = "SAT EBRW"
                    idLabel = "Ebrw"
                    value={this.state.satEbrw}
                    dirtyCheck = {(dirty) => this.dirtyCheck(dirty, 'satEbrw')}
                    validationSchema = {integerSchema('Ebrw', 200, 800).reduce(createYupSchema, {})}
                    onChangeLimit={(lower, upper) => this.setState({ satEbrw: { lower, upper } })}
                  />
                </li>
                <li className="list-group-item">
                  < LimitFilter
                    key= { this.state.actCompositeKey }
                    title = "ACT Composite"
                    idLabel = "composite"
                    value={this.state.actComposite}
                    dirtyCheck = {(dirty) => this.dirtyCheck(dirty, 'actComposite')}
                    validationSchema = {integerSchema('composite', 1, 36).reduce(createYupSchema, {})}
                    onChangeLimit={(lower, upper) => this.setState({ actComposite: { lower, upper } })}
                  />
                </li>
              </ul>
            </div>
            <div className="row">
              <ul className="list-group list-group-flush col">
                <li className="list-group-item">
                  < LimitFilter
                    key= { this.state.numStudentsEnrolledKey }
                    title = "Number of Enrolled"
                    idLabel = "student"
                    value={this.state.numStudentsEnrolled}
                    dirtyCheck = {(dirty) => this.dirtyCheck(dirty, 'numStudentsEnrolled')}
                    validationSchema = {integerSchema('student', 0, 100000).reduce(createYupSchema, {})}
                    onChangeLimit={(lower, upper) => this.setState({ numStudentsEnrolled: { lower, upper } })}
                  />
                </li>
                <li className="list-group-item ">
                  < TagFilter
                    title = "Region"
                    tags = {Object.keys(regionMapping)}
                    id = "region"
                    multiSelect
                    selected = {this.state.region}
                    onSelectTag={(value) => this.setState({ region: value })}
                  />
                </li>
                <li className="list-group-item ">
                  < TagFilter
                    title = "States"
                    tags = {Object.keys(stateMapping)}
                    tagMapping = {stateMapping}
                    id = "state"
                    multiSelect
                    expand
                    selected = {this.state.states}
                    onSelectTag={(value) => this.setState({ states: value })}
                  />
                </li>
                <li className="list-group-item ">
                  < TagFilter
                    title = "Majors"
                    tags = {this.majorList}
                    id = "major"
                    multiSelect
                    expand
                    selected = {this.state.major}
                    onSelectTag={(value) => this.setState({ major: value })}
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
                <button className="btn btn-sm filter_button float-right"
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