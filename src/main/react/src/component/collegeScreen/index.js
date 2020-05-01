import React, { Component } from 'react'
import { push } from 'react-router-redux'
import { updateErrorDetailAction } from '../../action'
import { connect } from 'react-redux'
import axios from 'axios'
import { BASE_URL } from '../../config'
import LoadingPage from '../loadingPage'
import { linkCheck } from '../../util/validateCheck'

class CollegePage extends Component {
  state = {
    college: null
  }

  componentDidMount = () => {
    axios.get(BASE_URL + '/api/v1/college',
      {
        headers: { Authorization: localStorage.getItem('userToken') },
        params: { name: this.props.match.params.collegeName }
      }
    ).then(response => {
      if (response.data.code === 'success') {
        this.setState({ college: response.data.data })
      } else {
        this.updateErrorDetail(response.data.code, 'collegeScreen', response.data.message)
        this.props.redirectErrorPage()
      }
    }).catch(error => {
      this.props.updateErrorDetail(null, 'collegeScreen', error.message)
      this.props.redirectErrorPage()
    })
  }

  render () {
    if (this.state.college == null) return <LoadingPage fullScreen={true} color="dark"/>
    return (
      <div className="collegePage page">
        <div className="college-title-box row">
          <div className="college-title ">{this.state.college.name}</div>
          <button className="application-button btn btn-outline-success float-right"
            onClick={() => this.props.history.push(
              { pathname: '/applicationTracker', query: { college: this.props.match.params.collegeName } })}
          ><span>{'Application Tracker '}</span><i className="fas fa-caret-right"/>
          </button>
        </div>
        <div className="row ">
          <div className="col-6">
            <div className="college_list">
              <h4 className="college_list_title">General Information</h4>
              <ul className="list-group">
                <li className="college_list_item list-group-item list-group-fill-color">
                  <span>University Type:</span>
                  <span className="float-right">{this.state.college.type === 1 ? 'Public' : 'Private' }</span>
                </li>
                <li className="college_list_item list-group-item">
                  <span>Location:</span>
                  <span className="float-right">{this.state.college.city + ', ' + this.state.college.state}</span>
                </li>
                <li className="college_list_item list-group-item list-group-fill-color">
                  <span>Website</span>
                  <a target="_blank" href={linkCheck(this.state.college.webpage)} className="float-right">{this.state.college.webpage}</a>
                </li>
                <li className="college_list_item list-group-item">
                  <span>National Rank</span>
                  <span className="float-right">{this.state.college.ranking}</span>
                </li>
                <li className="college_list_item list-group-item list-group-fill-color">
                  <span>Average GPA</span>
                  <span className="float-right">{this.state.college.averageGPA}</span>
                </li>
                <li className="college_list_item list-group-item">
                  <span>Admission Rate</span>
                  <span className="float-right">{this.state.college.admissionRate}</span>
                </li>
                <li className="college_list_item list-group-item list-group-fill-color">
                  <span>Completion Rate</span>
                  <span className="float-right">{this.state.college.completionRate}</span>
                </li>
              </ul>
            </div>
            <div className="college_list">
              <h4 className="college_list_title">SAT Average Score</h4>
              <ul className="list-group">
                <li className="college_list_item list-group-item list-group-fill-color">
                  <span>SAT Overall</span>
                  <span className="float-right">{this.state.college.satOverall}</span>
                </li>
                <li className="college_list_item list-group-item">
                  <span>SAT Math</span>
                  <span className="float-right">{this.state.college.satMath50}</span>
                </li>
                <li className="college_list_item list-group-item list-group-fill-color">
                  <span>SAT EBRW</span>
                  <span className="float-right">{this.state.college.satEbrw50}</span>
                </li>
              </ul>
            </div>
          </div>
          <div className="col-6">
            <div className="college_list">
              <h4 className="college_list_title">Finance</h4>
              <ul className="list-group">
                <li className="college_list_item list-group-item list-group-fill-color">
                  <span>Instate Tuition:</span>
                  <span className="float-right">{this.state.college.instateTuition}</span>
                </li>
                <li className="college_list_item list-group-item">
                  <span>Outstate Tuition:</span>
                  <span className="float-right">{this.state.college.outstateTuition}</span>
                </li>
                <li className="college_list_item list-group-item list-group-fill-color">
                  <span>Median Debt:</span>
                  <span className="float-right">{this.state.college.medianDebt}</span>
                </li>
                <li className="college_list_item list-group-item">
                  <span>Net Price</span>
                  <span className="float-right">{this.state.college.netPrice}</span>
                </li>
                <li className="college_list_item list-group-item list-group-fill-color">
                  <span>Expect Mean Earning:</span>
                  <span className="float-right">{this.state.college.meanEarnings}</span>
                </li>
              </ul>
            </div>
            <div className="college_list">
              <h4 className="college_list_title">ACT Average Score</h4>
              <ul className="list-group">
                <li className="college_list_item list-group-item list-group-fill-color">
                  <span>ACT Math</span>
                  <span className="float-right">{this.state.college.actMath50}</span>
                </li>
                <li className="college_list_item list-group-item">
                  <span>ACT English</span>
                  <span className="float-right">{this.state.college.actEnglish50}</span>
                </li>
                <li className="college_list_item list-group-item list-group-fill-color">
                  <span>ACT Composite</span>
                  <span className="float-right">{this.state.college.actComposite}</span>
                </li>
              </ul>
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
)(CollegePage)