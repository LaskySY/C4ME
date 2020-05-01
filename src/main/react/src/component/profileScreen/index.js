import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'

import UserInfoCard from './userInfoCard'
import InfoCompleteCard from './infoComplete'
import EducationCard from './card/EducationCard'
import ACTGradeCard from './card/ACTGradeCard'
import SATGradeCard from './card/SATGradeCard'
import ApplicationCard from './card/ApplicationCard'
import EditingApplicationCard from './editingCard/editingApplicationCard'
import EditingEducationCard from './editingCard/editingEducationCard'
import EditingACTGradeCard from './editingCard/editingACTGradeCard'
import EditingSATGradeCard from './editingCard/editingSATGradeCard'
import AddingApplicationCard from './editingCard/addingApplicationCard'
import { getProfileAction, getApplicationAction } from '../../action'

class ProfileScreen extends Component {
  state = {
    addingApplication: false
  }

  componentDidMount = () => {
    const username = this.props.match.params.username
    this.props.getApplicationData(username)
    this.props.getProfileData(username)
  }

  render () {
    const isUser = localStorage.getItem('username') === this.props.match.params.username
    return (
      <div className="p-3 mb-2 bg-light text-dark">
        <div className="page">
          <div className="row">
            <div className="profile-left col-4">
              <UserInfoCard />
              { isUser ? <InfoCompleteCard /> : null }
            </div>
            <div className="profile-right col-8">
              <div className="profileCard card">
                {
                  this.props.editingEducation
                    ? <EditingEducationCard/>
                    : <EducationCard edit={isUser}/>
                }
              </div>
              <div className="profileCard card">
                {
                  this.props.editingACTGrade
                    ? <EditingACTGradeCard/>
                    : <ACTGradeCard edit={isUser}/>
                }
                {
                  this.props.editingSATGrade
                    ? <EditingSATGradeCard/>
                    : <SATGradeCard edit={isUser}/>
                }
              </div>
              <div className="profileCard card" id="application_card">
                {
                  // display applications in editingApplication cards or Application cards
                  this.props.applications.map((application, index) => {
                    return this.props.editingApplication.includes(index)
                      ? <EditingApplicationCard key={application.college.label}
                        id={index} application={application}/>
                      : <ApplicationCard key={application.college.label}
                        id={index} edit={isUser} application={application}/>
                  })
                }
                {
                  isUser
                    ? this.state.addingApplication
                      ? <AddingApplicationCard index={this.props.applications.length}
                        changeState={() => this.setState({ addingApplication: false })}/>
                      : <div className="addNew card-footer btn btn-light"
                        onClick={() => this.setState({ addingApplication: true })}>
                        <span>Add New Application</span>
                      </div>
                    : null
                }
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  applications: state.applications.data,
  editingApplication: state.profile.editingApplication,
  editingEducation: state.profile.editingEducation,
  editingACTGrade: state.profile.editingACTGrade,
  editingSATGrade: state.profile.editingSATGrade
})

const mapDispatchToProps = dispatch => ({
  getProfileData: (...args) => dispatch(getProfileAction(...args)),
  getApplicationData: (...args) => dispatch(getApplicationAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(ProfileScreen))
