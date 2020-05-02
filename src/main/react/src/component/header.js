import React, { Component } from 'react'
import { connect } from 'react-redux'
import { compose } from 'redux'
import { Link } from 'react-router-dom'
import { clearLoginStateAction } from '../action'
import { withRouter } from 'react-router'

class NaviBar extends Component {
  state = {
    pathname: this.props.history.location.pathname.split('/')[1]
  }

  componentWillReceiveProps = () => {
    const currentPath = this.props.history.location.pathname.split('/')[1]
    if (currentPath !== this.state.pathname &&
      currentPath !== 'college' && currentPath !== 'error') { this.setState({ pathname: currentPath }) }
  }

  render () {
    return (
      <nav className="navbar navbar-expand-lg" >
        <a className="navbar-brand mb-0 h1" href="/">C4ME</a>
        <div className="navbar-collapse">
          <ul className="navbar-nav mr-auto">
            {
              this.props.loginState !== 0 && this.props.loginState !== 3
                ? <li className={['nav-item', this.state.pathname === '' ? 'active' : ''].join(' ')}>
                  <Link className="nav-link"
                    onClick={() => this.setState({ pathname: '' })}
                    to="">College Search</Link>
                </li>
                : null
            }
            {
              this.props.loginState !== 0 && this.props.loginState !== 3
                ? <li className={['nav-item', this.state.pathname === 'findSimilarHighSchool' ? 'active' : ''].join(' ')}>
                  <Link className="nav-link"
                    onClick={() => this.setState({ pathname: 'findSimilarHighSchool' })}
                    to="/findSimilarHighSchool">Find Similar High School</Link>
                </li>
                : null
            }
            {
              this.props.loginState !== 0 && this.props.loginState !== 3
                ? <li className={['nav-item', this.state.pathname === 'applicationTracker' ? 'active' : ''].join(' ')}>
                  <Link className="nav-link"
                    onClick={() => this.setState({ pathname: 'applicationTracker' })}
                    to="/applicationTracker">Application</Link>
                </li>
                : null
            }
            {
              this.props.loginState === 1
                ? <li className={['nav-item', this.state.pathname === 'profile' ? 'active' : ''].join(' ')}>
                  <Link className="nav-link"
                    onClick={() => this.setState({ pathname: 'profile' })}
                    to={'/profile/' + localStorage.getItem('username')}>Profile</Link>
                </li>
                : null
            }
            {
              this.props.loginState === 2
                ? <li className={['nav-item dropdown', this.state.pathname === 'admin' ? 'active' : ''].join(' ')}>
                  <a className="nav-link dropdown-toggle" href="#" id="navbarDropdown"
                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                    onClick={() => this.setState({ pathname: 'admin' })}>
                      Admin
                  </a>
                  <div className="dropdown-menu" aria-labelledby="navbarDropdown"
                    onClick={e => { e.target.parentElement.classList.remove('show') }}>
                    <Link className="dropdown-item" to="/admin/college">College</Link>
                    <Link className="dropdown-item" to="/admin/application">Application</Link>
                    <Link className="dropdown-item" to="/admin/profile">Profile</Link>
                  </div>
                </li>
                : null
            }
          </ul>
          <ul className="navbar-nav pull-right">
            {
              this.props.loginState === 1 || this.props.loginState === 2
                ? <li className={['nav-item', this.state.pathname === 'login' ? 'active' : ''].join(' ')}>
                  <Link className="nav-link"
                    onClick={() => this.setState({ pathname: 'login' })}
                    to="/login" onClick={this.props.clearLoginState}>Logout</Link>
                </li>
                : <li className={['nav-item', this.state.pathname === 'login' ? 'active' : ''].join(' ')}>
                  <Link className="nav-link"
                    onClick={() => this.setState({ pathname: 'login' })}
                    to="/login">Login</Link>
                </li>
            }
            {
              this.props.loginState === 0 || this.props.loginState === 3
                ? <li className={['nav-item', this.state.pathname === 'register' ? 'active' : ''].join(' ')}>
                  <Link className="nav-link"
                    onClick={() => this.setState({ pathname: 'register' })}
                    to="/register">Register</Link>
                </li>
                : null
            }
          </ul>
        </div>
      </nav>
    )
  }
}

const mapStateToProps = state => ({
  loginState: state.status.loginState
})

const mapDispatchToProps = dispatch => ({
  clearLoginState: () => dispatch(clearLoginStateAction())
})

export default compose(
  connect(mapStateToProps, mapDispatchToProps),
  withRouter
)(NaviBar)