import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'
import { loginAction, clearLoginStateAction } from '../../action'
import { Redirect } from 'react-router-dom'

class LoginScreen extends Component {
  state = {
    username: '',
    password: '',
    loading: false
  }

  componentWillReceiveProps = (nextProps) => {
    if (nextProps.loginState === 3 && this.state.loading === true) { this.setState({ loading: false }) }
  }

  handleLogin = () => {
    this.setState({ loading: true })
    this.props.clearLoginState()
    this.props.login({
      username: this.state.username,
      password: this.state.password,
      rememberMe: document.getElementById('loginRememberMe').checked
    })
  }

  render () {
    return (
      <div className="page">
        <div className="authPanel col-3 ">
          <div className="form-group">
            <label htmlFor="loginUsername">Username:</label>
            <input id="loginUsername" className="form-control" type = "text"
              value={this.state.username} onChange={e => this.setState({ username: e.target.value })}/>
          </div>
          <div className="form-group">
            <label htmlFor="loginPassword">Password:</label>
            <input id="loginPassword" className="form-control" type = "text"
              value={this.state.password} onChange={e => this.setState({ password: e.target.value })}/>
          </div>
          <div className="form-check">
            <input id = "loginRememberMe" className="form-check-input" type = "checkbox"/>
            <label className="form-check-label" htmlFor="loginRememberMe">RememberMe</label>
          </div>
          <div style={{ margin: '7px' }}>
            {this.props.loginState === 1 ? <Redirect to="/"/> : null }
            {this.props.loginState === 2 ? <Redirect to="/"/> : null }
            {this.props.loginState === 3 ? <span className="text-danger"> Wrong Username or pasword </span> : null }
          </div>
          {
            this.state.loading === true
              ? <div className="btn auth-button-check form-control btn-success" disabled>
                <div className="spinner-border text-light" role="status" />
              </div>
              : <div className="btn auth-button-check form-control btn-success" onClick={() => this.handleLogin()}>
                <span className="font-weight-bold text-monospace">Sign in</span>
              </div>
          }
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  loginState: state.status.loginState
})

const mapDispatchToProps = dispatch => ({
  login: (...args) => dispatch(loginAction(...args)),
  clearLoginState: () => dispatch(clearLoginStateAction())
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(LoginScreen))