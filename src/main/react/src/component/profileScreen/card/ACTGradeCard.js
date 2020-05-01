import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'

import { editingACTGradeAction } from '../../../action'


class ACTGradeCard extends Component {
  render () {
    const {
      actMath,
      actEnglish,
      actReading,
      actScience,
      actComposite
    } = this.props.profile
    return (
      <div className="card-body" id="act_card">
        {
          this.props.edit
            ? <i className="profile_edit_button fas fa-edit float-right"
              onClick={() => this.props.changeState(true)}/>
            : null
        }
        <h5 className="card-title"><span className="profile-card-title">ACT Grade</span></h5>
        <div className="row card-text">
          {actMath ? <div className="gradeField col-6">Math: &nbsp; {actMath}</div> : null}
          {actEnglish ? <div className="gradeField col-6">English: &nbsp; {actEnglish}</div> : null}
          {actReading ? <div className="gradeField col-6">Reading: &nbsp; {actReading}</div> : null}
          {actScience ? <div className="gradeField col-6">Science: &nbsp; {actScience}</div> : null}
          {actComposite ? <div className="gradeField col-6">Composite: &nbsp; {actComposite}</div> : null}
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  profile: state.profile.data
})

const mapDispatchToProps = dispatch => ({
  changeState: (...args) => dispatch(editingACTGradeAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(ACTGradeCard))
