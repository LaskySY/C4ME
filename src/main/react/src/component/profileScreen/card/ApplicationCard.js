import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'

import { editingApplicationAction } from '../../../action'


class ApplicationCard extends Component {
  getStatus = (status) => {
    switch (status) {
      case 0: return 'pending'
      case 1: return 'accepted'
      case 2: return 'denied'
      case 3: return 'deferred'
      case 4: return 'wait-listed'
      case 5: return 'withdrawn'
      default: return null
    }
  }

  render () {
    const {
      college,
      admissionTerm,
      status,
      questionable
    } = this.props.application
    return (
      <div className="card-body">
        {
          this.props.edit
            ? <i className="profile_edit_button fas fa-edit float-right"
              onClick={() => this.props.changeState(this.props.id)} />
            : null
        }
        <h5 className="card-title"><span className="profile-card-title">{college.label + ' Application'}</span></h5>
        <div className="card-text">
          {
            status != null
              ? <span>Status: &nbsp;{this.getStatus(status)}</span>
              : null
          }
          {
            admissionTerm
              ? <div>Admission Term:&nbsp;{admissionTerm}</div>
              : null
          }
          {
            questionable
              ? <span style={{ color: 'red' }}>This application result are marked as questionable</span>
              : null
          }
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({})

const mapDispatchToProps = dispatch => ({
  changeState: (...args) => dispatch(editingApplicationAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(ApplicationCard))
