import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'


class InfoCompleteCard extends Component {
  getPercent = (a, b, c, d) => {
    let t = 0
    if (a) t += 25
    if (b) t += 25
    if (c) t += 25
    if (d) t += 25
    return t
  }

  render () {
    const {
      schoolName,
      actMath, actEnglish, actReading, actScience, actComposite,
      satMath, satEbrw
    } = this.props.profile
    const getEducation = schoolName != null
    const getACTGrade = actMath && actEnglish && actReading && actScience && actComposite
    const getSATGrade = satMath && satEbrw
    const getApplication = this.props.applications.length !== 0
    const percent = this.getPercent(getEducation, getACTGrade, getSATGrade, getApplication) + '%'
    return (
      <div className="profileCard card">
        <div className="card-body text-center">
          <h5> Profile {percent} complete </h5>
          <div className="progress  " style={{ width: '100%', marginTop: '15px' }}>
            <div className="progress-bar" role="progressbar" id="profile-progress" style={{ width: percent }} ></div>
          </div>
          { getEducation
            ? null
            : <button type="button" className="btn btn-light  " style={{ width: '100%', marginTop: '15px' }}
              onClick={() => document.getElementById('education_card').scrollIntoView({ behavior: 'smooth' })}
            >Add Education</button>
          }
          {
            getACTGrade
              ? null
              : <button type="button" className="btn btn-light  " style={{ width: '100%', marginTop: '15px' }}
                onClick={() => document.getElementById('act_card').scrollIntoView({ behavior: 'smooth' })}
              >Add ACT Grade</button>
          }
          {
            getSATGrade
              ? null
              : <button type="button" className="btn btn-light  " style={{ width: '100%', marginTop: '15px' }}
                onClick={() => document.getElementById('sat_card').scrollIntoView({ behavior: 'smooth' })}
              >Add SAT Grade</button>
          }
          {
            getApplication
              ? null
              : <button type="button" className="btn btn-light  " style={{ width: '100%', marginTop: '15px' }}
                onClick={() => document.getElementById('application_card').scrollIntoView({ behavior: 'smooth' })}
              >Add Application</button>
          }
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  profile: state.profile.data,
  applications: state.applications.data
})

const mapDispatchToProps = dispatch => ({})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(InfoCompleteCard))
