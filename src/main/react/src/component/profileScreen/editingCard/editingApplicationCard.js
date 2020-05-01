import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'
import {
  uneditingApplicationAction,
  updateApplicationAction,
  deleteApplicationAction
} from '../../../action'
import { updateStringCheck } from '../../../util/validateCheck'


class EditingApplicationCard extends Component {
  state = {
    college: this.props.application.college,
    admissionTerm: this.props.application.admissionTerm,
    status: this.props.application.status
  }

  save = () => {
    let status = null
    const applicationStatus = document.getElementsByName('applicationStatus')
    applicationStatus.forEach(node => node.checked === true ? status = parseInt(node.value) : null)
    this.props.updateApplication({
      college: this.state.college,
      admissionTerm: this.state.admissionTerm,
      status: status,
      questionable: this.props.application.questionable
    })
    this.props.changeState(this.props.id)
  }

  delete = () => {
    this.props.deleteApplication({
      collegeId: this.state.college.value
    })
    this.props.changeState(this.props.id)
  }

  render () {
    return (
      <div className="card-body">
        <div className="card-text">
          <label htmlFor="collegeName">College Name </label>
          <input type="text" className="form-control" readOnly="readonly" id="collegeName"
            value={this.state.college.label} />
          <label htmlFor="admissionTerm">Admission Term </label>
          <input type="text" className="form-control" id="admissionTerm"
            value={this.state.admissionTerm} onChange={e => this.setState({ admissionTerm: updateStringCheck(e.target.value) })}/>
          <label>Admission Status</label>
          <div className="form-group row">
            <label className="radio-inline col-4">
              <input type="radio" name="applicationStatus" value={0} defaultChecked={this.state.status === 0}/>pending
            </label>
            <label className="radio-inline col-4">
              <input type="radio" name="applicationStatus" value={1} defaultChecked={this.state.status === 1}/>accepted
            </label>
            <label className="radio-inline col-4">
              <input type="radio" name="applicationStatus" value={2} defaultChecked={this.state.status === 2}/>denied
            </label>
            <label className="radio-inline col-4">
              <input type="radio" name="applicationStatus" value={3} defaultChecked={this.state.status === 3}/>deferred
            </label>
            <label className="radio-inline col-4">
              <input type="radio" name="applicationStatus" value={4} defaultChecked={this.state.status === 4}/>wait-listed
            </label>
            <label className="radio-inline col-4">
              <input type="radio" name="applicationStatus" value={5} defaultChecked={this.state.status === 5}/>withdrawn
            </label>
          </div>
        </div>
        <button className="editcard-button btn float-right" onClick={this.delete}>Delete</button>
        <button className="editcard-button btn float-right"
          onClick={() => this.props.changeState(this.props.id)}>Cancel</button>
        <button className="editcard-button btn float-right" onClick={this.save}>Save</button>
      </div>
    )
  }
}

const mapStateToProps = state => ({})

const mapDispatchToProps = dispatch => ({
  changeState: (...args) => dispatch(uneditingApplicationAction(...args)),
  updateApplication: (...args) => dispatch(updateApplicationAction(...args)),
  deleteApplication: (...args) => dispatch(deleteApplicationAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(EditingApplicationCard))
