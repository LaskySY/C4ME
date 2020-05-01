import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'


class UserInfoCard extends Component {
  render () {
    const {
      schoolName, name, gpa, schoolYear,
      actMath, actEnglish, actReading, actScience, actComposite,
      satMath, satEbrw
    } = this.props.profile
    return (
      <div className="profileCard card">
        <div className="card-body">
          <div className="card-text text-center">
            { name ? <h4>{name}</h4> : null}
            { schoolName ? <div>{schoolName}</div> : null}
            <div>
              {
                schoolYear
                  ? <span>Class of {schoolYear}</span>
                  : null
              }
              {
                schoolYear && gpa
                  ? ' · '
                  : null
              }
              {
                gpa
                  ? <span>GPA&nbsp; {gpa}</span>
                  : null}
            </div>
            <div>
              {
                actMath && actEnglish && actReading && actScience && actComposite
                  ? <span>ACT&nbsp;{(actMath + actEnglish + actReading + actScience + actComposite) / 5 }</span>
                  : null
              }
              {
                actMath && actEnglish && actReading && actScience && actComposite && satMath && satEbrw
                  ? ' · '
                  : null
              }
              {
                satMath && satEbrw
                  ? <span>SAT&nbsp;{satMath + satEbrw }</span>
                  : null
              }
            </div>
          </div>
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  profile: state.profile.data
})

const mapDispatchToProps = dispatch => ({})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(UserInfoCard))
