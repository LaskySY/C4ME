import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'
import { registerAction, loginAction, clearRegisterStateAction } from '../../action'
import { Redirect } from 'react-router-dom'

class RegisterScreen extends Component {
  state = {
    username: '',
    password: '',
    name: '',
    loading: false
  }

  handleRegister = () => {
    this.setState({ loading: true })
    this.props.clearRegister()
    this.props.register({
      username: this.state.username,
      password: this.state.password,
      name: this.state.name
    })
  }

  componentWillReceiveProps = (nextProps) => {
    if (nextProps.registerState === 2 && this.state.loading === true) { this.setState({ loading: false }) }
  }

  handleLogin = () => {
    this.props.login({
      username: this.state.username,
      password: this.state.password,
      rememberMe: false
    })
    this.props.clearRegister()
    return <Redirect to ="/" />
  }

  render () {
    return (
      <div className="page">
        <div className="authPanel col-3 ">
          <div className="form-group">
            <label htmlFor="registerUsername">Username:</label>
            <input id="registerUsername" className="form-control" type = "text"
              value={this.state.username} onChange={e => this.setState({ username: e.target.value })}/>
          </div>
          <div className="form-group">
            <label htmlFor="registerPassword">Password:</label>
            <input id="registerPassword" className="form-control" type = "text"
              value={this.state.password} onChange={e => this.setState({ password: e.target.value })}/>
          </div>
          <div className="form-group">
            <label htmlFor="registerName">Name:</label>
            <input id="registerName" className="form-control" type = "text"
              value={this.state.name} onChange={e => this.setState({ name: e.target.value })}/>
          </div>
          <div style={{ margin: '7px' }}>
            {this.props.registerState === 1 ? this.handleLogin() : null }
            {this.props.registerState === 2 ? <span className="text-danger"> Username has been used </span> : null }
          </div>
          {
            this.state.loading === true
              ? <div className="btn auth-button-check form-control btn-success" disabled>
                <div className="spinner-border text-light" role="status" />
              </div>
              : <div className="btn auth-button-check form-control btn-success" onClick={() => this.handleRegister()}>
                <span className="font-weight-bold text-monospace">Sign on</span>
              </div>
          }
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  registerState: state.status.registerState
})


const mapDispatchToProps = dispatch => ({
  login: (...args) => dispatch(loginAction(...args)),
  register: (...args) => dispatch(registerAction(...args)),
  clearRegister: (...args) => dispatch(clearRegisterStateAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(RegisterScreen))
