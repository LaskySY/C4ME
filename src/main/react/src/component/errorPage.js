import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Link } from 'react-router-dom'

class ErrorPage extends Component {
  render () {
    const errorDetail = this.props.errorDetail ? this.props.errorDetail : { code: null, field: null, message: null }
    return (
      <div className="errorPage">
        <div className="error-title">
          <div className="error-text text-1">O</div>
          <div className="error-text text-2">O</div>
          <div className="error-text text-3">P</div>
          <div className="error-text text-4">S</div>
          <div className="error-text text-2">!</div>
        </div>
        <div className="errorMessage"><span style={{ fontWeight: 'bold' }}>Looks like something went wrong!</span></div>
        <div className="errorContent">{errorDetail.code}</div>
        <div className="errorContent">{errorDetail.field}</div>
        <div className="errorContent">{errorDetail.message}</div>
        <Link to='/' className="errorButton btn btn-outline-success">HomePage</Link>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  errorDetail: state.status.error
})

export default connect(
  mapStateToProps,
  null
)(ErrorPage)
