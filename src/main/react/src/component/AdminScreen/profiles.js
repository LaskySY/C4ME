import React from 'react'
import ReactTable from 'react-table'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import {
  getProfileInfoActionAsync,
  importProfileActionAsync,
  deleteAllProfilesActionAsync
} from '../../action'

class Profiles extends React.Component {
  state = {
    loading: true
  }

  componentDidMount=() => {
    this.props.getProfileInfo()
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
        Header: 'School Year',
        accessor: 'schoolYear'
      },
      {
        Header: 'Number of AP Courses',
        accessor: 'numApCourses'
      },
      {
        Header: 'GPA',
        accessor: 'gpa'
      },
      {
        Header: 'SAT Math',
        accessor: 'satMath'
      },
      {
        Header: 'SAT EBRW',
        accessor: 'satEbrw'
      },
      {
        Header: 'ACT Math',
        accessor: 'actMath'
      },
      {
        Header: 'ACT English',
        accessor: 'actEnglish'
      },
      {
        Header: 'ACT Reading',
        accessor: 'actReading'
      },
      {
        Header: 'ACT Science',
        accessor: 'actScience'
      },
      {
        Header: 'ACT Composite',
        accessor: 'actComposite'
      },
      {
        Header: 'SAT Literature',
        accessor: 'satLiterature'
      },
      {
        Header: 'SAT U.S History',
        accessor: 'satUsHist'
      },
      {
        Header: 'SAT World History',
        accessor: 'satWorldHist'
      },
      {
        Header: 'SAT Math I',
        accessor: 'satMathI'
      },
      {
        Header: 'SAT Math II',
        accessor: 'satMathIi'
      },
      {
        Header: 'SAT EcoBio',
        accessor: 'satEcoBio'
      },
      {
        Header: 'SAT MolBio',
        accessor: 'satMolBio'
      },
      {
        Header: 'SAT Chemistry',
        accessor: 'satChemistry'
      },
      {
        Header: 'SAT Physics',
        accessor: 'satPhysics'
      },
      {
        Header: 'Create Time',
        accessor: 'createTime'
      },
      {
        Header: 'Update Time',
        accessor: 'updateTime'
      }
    ]
    return (
      <div className="page justify-content-center">
        <h3 className="admin-title" style={{ textAlign: 'center' }}>Profiles</h3>
        <div className="btn-group" role="group">
          <button type="button" className="btn btn-outline-info"
            onClick={() => { this.props.importProfile(); this.setState({ loading: true }) }}>Import Profiles</button>
          <button type="button" className="btn btn-outline-info"
            onClick={() => { this.props.deleteAllProfiles(); this.setState({ loading: true }) }}>Delete ALL Profiles</button>
        </div>
        <button type="button" className="btn btn-outline-info float-right"
          onClick={() => { this.props.getProfileInfo(); this.setState({ loading: true }) }}
        ><i className="fas fa-sync"/>
        </button>
        <ReactTable columns={columns} data={this.props.profileInfo} defaultPageSize={10} loading={this.state.loading}/>
      </div>
    )
  }
}

function mapStateToProps (state) {
  return {
    profileInfo: state.profileInfo
  }
}

function matchDispatchToProps (dispatch) {
  return bindActionCreators({
    getProfileInfo: getProfileInfoActionAsync,
    importProfile: importProfileActionAsync,
    deleteAllProfiles: deleteAllProfilesActionAsync
  }, dispatch)
}

export default connect(mapStateToProps, matchDispatchToProps)(Profiles)