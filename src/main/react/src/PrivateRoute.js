import React from 'react'
import { Route, Redirect } from 'react-router-dom'
import { connect } from 'react-redux'

const auth = {
  student: 1,
  admin: 2
}

const PrivateRoute = ({ component: Component, role, loginState, ...rest }) => {
  return <Route {...rest}
    render={props =>
      auth[role] <= loginState && loginState !== 0 && loginState !== 3
        ? <Component {...props} />
        : <Redirect to="/login" />
    }
  />
}

const mapStateToProps = state => ({
  loginState: state.status.loginState
})

export default connect(mapStateToProps, null)(PrivateRoute)