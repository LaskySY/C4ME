import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'

import { editingEducationAction } from '../../../action'


class EducationCard extends Component {
  render () {
    const {
      schoolName,
      gpa,
      schoolYear,
      numApCourses,
      major1,
      major2
    } = this.props.profile
    return (
      <div className="card-body" id="education_card">
        {
          this.props.edit
            ? <i className=" profile_edit_button fas fa-edit   float-right"
              onClick={() => this.props.changeState(true)}/>
            : null
        }
        <h5 className="card-title"><span className="profile-card-title">Education</span></h5>
        <div className="card-text">
          { schoolName ? <div>School: &nbsp; {schoolName}</div> : null}
          { gpa ? <div>Cumulative GPA: &nbsp; {gpa}</div> : null}
          { schoolYear ? <div>College Class:  &nbsp; {schoolYear}</div> : null}
          { numApCourses ? <div>Number of Ap Course: &nbsp; {numApCourses}</div> : null}
          { major1 || major2 ? <div>Prefered Major: &nbsp;
            <label>
              { major1 ? <span className="major_tag">{major1}</span> : null }
              { major2 ? <span className="major_tag">{major2}</span> : null }
            </label>
          </div> : null
          }
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  profile: state.profile.data
})

const mapDispatchToProps = dispatch => ({
  changeState: (...args) => dispatch(editingEducationAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(EducationCard))
