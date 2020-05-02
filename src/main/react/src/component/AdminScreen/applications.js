import React from 'react'
import ReactTable from 'react-table'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import {
  getApplicationInfoActionAsync,
  changeQuestionableDecisionActionAsync
} from '../../action'

class Applications extends React.Component {
  state = {
    loading: true
  }

  componentDidMount=() => {
    this.props.getApplicationInfo()
  }

  componentWillReceiveProps = () => {
    if (this.state.loading === true) { this.setState({ loading: false }) }
  }

  render () {
    const columns = [
      {
        Header: 'Username',
        accessor: 'username'
      },
      {
        Header: 'College Name',
        accessor: 'college.label'
      },
      {
        Header: 'Admission Term',
        accessor: 'admissionTerm'
      },
      {
        Header: 'Status',
        accessor: 'statusString'
      },
      {
        Header: '',
        Cell: props => {
          return (
            <button onClick={
              () => {
                this.props.changeQuestionableDecision(props.original, 0)
              }
            } style={{ backgroundColor: '#1eb2a6', color: 'white' }}>Pass</button>
          )
        }
      },
      {
        Header: '',
        Cell: props => {
          return (
            <button onClick={
              () => {
                this.props.changeQuestionableDecision(props.original, 2)
              }
            } style={{ backgroundColor: '#f67575', color: 'white' }}>Dishonest</button>
          )
        }
      }
    ]
    return (
      <div className="page justify-content-center">
        <h3 className="admin-title" style={{ textAlign: 'center' }}>Applications</h3>
        <div className="btn-group" role="group" style={{ height: '40px' }}/>
        <button className="btn btn-outline-info float-right"
          onClick={() => { this.props.getApplicationInfo(); this.setState({ loading: true }) }}
        ><i className="fas fa-sync"/>
        </button>
        <ReactTable columns={columns} data={this.props.applicationInfo} defaultPageSize={10} loading={this.state.loading}/>
      </div>
    )
  }
}

function mapStateToProps (state) {
  return {
    applicationInfo: state.applicationInfo
  }
}

function matchDispatchToProps (dispatch) {
  return bindActionCreators({
    getApplicationInfo: getApplicationInfoActionAsync,
    changeQuestionableDecision: changeQuestionableDecisionActionAsync
  }, dispatch)
}

export default connect(mapStateToProps, matchDispatchToProps)(Applications)