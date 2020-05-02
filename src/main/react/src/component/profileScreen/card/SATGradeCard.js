import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'

import { editingSATGradeAction } from '../../../action'

class SATGradeCard extends Component {
  render () {
    const {
      satMath,
      satEbrw,
      satLiterature,
      satUsHist,
      satWorldHist,
      satMathI,
      satMathIi,
      satEcoBio,
      satMolBio,
      satChemistry,
      satPhysics
    } = this.props.profile

    return (
      <div className="card-body" id="sat_card">
        {
          this.props.edit
            ? <i className=" profile_edit_button fas fa-edit   float-right"
              onClick={() => this.props.changeState(true)}/>
            : null
        }
        <h5 className="card-title"><span className="profile-card-title">SAT Grade</span></h5>
        <div className="row card-text">
          {satMath ? <div className="gradeField col-6">Math: &nbsp; {satMath}</div> : null}
          {satEbrw ? <div className="gradeField col-6">EBRW: &nbsp; {satEbrw}</div> : null}
          {satLiterature ? <div className="gradeField col-6">Literature: &nbsp; {satLiterature}</div> : null}
          {satUsHist ? <div className="gradeField col-6">US History: &nbsp; {satUsHist}</div> : null}
          {satWorldHist ? <div className="gradeField col-6">World History: &nbsp; {satWorldHist}</div> : null}
          {satEcoBio ? <div className="gradeField col-6">Biology-E: &nbsp; {satEcoBio}</div> : null}
          {satMolBio ? <div className="gradeField col-6">Biology-M: &nbsp; {satMolBio}</div> : null}
          {satChemistry ? <div className="gradeField col-6">Chemistry: &nbsp; {satChemistry}</div> : null}
          {satPhysics ? <div className="gradeField col-6">Physics: &nbsp; {satPhysics}</div> : null}
          {satMathI ? <div className="gradeField col-6">Math I: &nbsp; {satMathI}</div> : null}
          {satMathIi ? <div className="gradeField col-6">Math II: &nbsp; {satMathIi}</div> : null}
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  profile: state.profile.data
})

const mapDispatchToProps = dispatch => ({
  changeState: (...args) => dispatch(editingSATGradeAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(SATGradeCard))
