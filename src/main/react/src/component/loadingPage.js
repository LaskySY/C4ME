import React, { Component } from 'react'

class LoadingComponent extends Component {
  static defaultProps = {
    fullScreen: false,
    color: 'dark'
  }

  render () {
    var layoutStyle = { margin: '5px' }
    var innerStyle = {}
    if (this.props.fullScreen === true) { layoutStyle.paddingTop = '20%' }
    if (this.props.color === 'light') { innerStyle.color = '#eff3c6' }
    if (this.props.color === 'dark') { innerStyle.color = '#f0a500' }
    return (
      <div className="d-flex justify-content-center"
        style={layoutStyle}>
        <div className="spinner-border" role="status"
          style={innerStyle}>
          <span className="sr-only">Loading...</span>
        </div>
      </div>
    )
  }
}

export default LoadingComponent
