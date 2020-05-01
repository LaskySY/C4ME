import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'
import { push } from 'react-router-redux'
import { updateErrorDetailAction, updateApplicationAction } from '../../../action'
import axios from 'axios'
import Select from 'react-select'
import LoadingPage from '../../loadingPage'
import { updateStringCheck } from '../../../util/validateCheck'
import { BASE_URL } from '../../../config'


class AddingApplicationCard extends Component {
  constructor (props) {
    super(props)
    this.colleges = []
    this.state = {
      loading: true,
      college: { value: null, label: null },
      admissionTerm: null,
      message: null
    }
  }

  componentDidMount = () => {
    axios.get(BASE_URL + '/profile/college',
      { headers: { Authorization: localStorage.getItem('userToken') } }
    ).then(res => {
      if (res.data.code === 'success') {
        var data = res.data.data.colleges
        this.colleges = data
        this.setState({ loading: false })
      } else {
        this.props.updateErrorDetail(res.data.code, 'adding application card', res.data.message)
        this.props.redirectErrorPage()
      }
    }).catch(error => {
      this.props.updateErrorDetail(null, 'adding application card', error.message)
      this.props.redirectErrorPage()
    })
  }

  collegeNameCheck = () => {
    if (this.state.college.value === null) {
      this.setState({ message: 'Required College Name' })
      return false
    }
    for (const college in this.props.applications) {
      if (this.props.applications[college].college.value === this.state.college.value) {
        this.setState({ message: 'You alrealy have an application of ' + this.state.college.label })
        return false
      }
    }
    return true
  }

  save = () => {
    let status = null
    const applicationStatus = document.getElementsByName('applicationStatus')
    applicationStatus.forEach(node => node.checked === true ? status = node.value : null)
    if (this.collegeNameCheck()) {
      this.props.updateApplication({
        college: this.state.college,
        admissionTerm: this.state.admissionTerm,
        status: status,
        questionable: false
      })
      this.props.changeState()
    }
  }

  render () {
    return (
      this.state.loading
        ? <LoadingPage fullScreen={false} color="light"/>
        : <div className="card-body">
          <div className="card-text">
            <label htmlFor="collegeName">College Name </label><span className="text-danger"> * </span>
            <Select id="collegeName" options={this.colleges} value={this.state.college}
              onChange={selectedOption => this.setState({ college: selectedOption })}/>
            { this.state.message ? <small className="form-text text-danger">{this.state.message}</small> : null}
            <label htmlFor="admissionTerm">Admission Term </label>
            <input type="text" className="form-control" id="admissionTerm"
              value={this.state.admissionTerm} onChange={e => this.setState({ admissionTerm: updateStringCheck(e.target.value) })}/>
            <label>Admission Status</label>
            <div className="form-group row">
              <label className="radio-inline col-4"><input type="radio" name="applicationStatus" value={0}/>pending</label>
              <label className="radio-inline col-4"><input type="radio" name="applicationStatus" value={1}/>accepted </label>
              <label className="radio-inline col-4"><input type="radio" name="applicationStatus" value={2}/>denied</label>
              <label className="radio-inline col-4"><input type="radio" name="applicationStatus" value={3}/>deferred</label>
              <label className="radio-inline col-4"><input type="radio" name="applicationStatus" value={4}/>wait-listed</label>
              <label className="radio-inline col-4"><input type="radio" name="applicationStatus" value={5}/>withdrawn</label>
            </div>
          </div>
          <button className="editcard-button btn float-right"
            onClick={() => this.props.changeState()}>Cancel</button>
          <button className="editcard-button btn float-right" onClick={this.save}>Save</button>
        </div>
    )
  }
}

const mapStateToProps = state => ({
  applications: state.applications.data
})

const mapDispatchToProps = dispatch => ({
  updateApplication: (...args) => dispatch(updateApplicationAction(...args)),
  redirectErrorPage: () => dispatch(push('/error')),
  updateErrorDetail: (...args) => dispatch(updateErrorDetailAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(AddingApplicationCard))