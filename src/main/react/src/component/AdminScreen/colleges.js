import React from 'react'
import ReactTable from 'react-table'
import { connect } from 'react-redux'
import {
  getCollegeInfoActionAsync,
  scrapCollegeDataActionAsync,
  scrapCollegeRankingActionAsync,
  importCollegeScorecardActionAsync
} from '../../action'
import { bindActionCreators } from 'redux'

class Colleges extends React.Component {
  state = {
    loading: true
  }

  componentDidMount=() => {
    this.props.getCollegeInfo()
  }

  componentWillReceiveProps = () => {
    if (this.state.loading === true) { this.setState({ loading: false }) }
  }

  render () {
    const columns = [
      {
        Header: 'Name',
        accessor: 'name'
      },
      {
        Header: 'Type',
        accessor: 'typeString'
      },
      {
        Header: 'Average GPA',
        accessor: 'averageGPA'
      },
      {
        Header: 'Admission Rate',
        accessor: 'admissionRate'
      },
      {
        Header: 'City',
        accessor: 'city'
      },
      {
        Header: 'State',
        accessor: 'state'
      },
      {
        Header: 'Webpage',
        accessor: 'webpage'
      },
      {
        Header: 'Latitude',
        accessor: 'latitude'
      },
      {
        Header: 'Longtitude',
        accessor: 'longitude'
      },
      {
        Header: 'In State Tuition',
        accessor: 'instateTuition'
      },
      {
        Header: 'Out State Tuition',
        accessor: 'outstateTuition'
      },
      {
        Header: 'Net Price',
        accessor: 'netPrice'
      },
      {
        Header: 'Median Debt',
        accessor: 'medianDebt'
      },
      {
        Header: 'Number of Student Enrolled',
        accessor: 'numStudentsEnrolled'
      },
      {
        Header: 'Retention Rate',
        accessor: 'retentionRate'
      },
      {
        Header: 'Completion Rate',
        accessor: 'completionRate'
      },
      {
        Header: 'Mean Earnings',
        accessor: 'meanEarnings'
      },
      {
        Header: 'Updated Time',
        accessor: 'updatedTime'
      },
      {
        Header: 'Ranking',
        accessor: 'ranking'
      },
      {
        Header: 'SAT Math 25',
        accessor: 'satMath25'
      },
      {
        Header: 'SAT Math 50',
        accessor: 'satMath50'
      },
      {
        Header: 'SAT Math 75',
        accessor: 'satMath75'
      },
      {
        Header: 'SAT EBRW 25',
        accessor: 'satEbrw25'
      },
      {
        Header: 'SAT EBRW 50',
        accessor: 'satEbrw50'
      },
      {
        Header: 'SAT EBRW 75',
        accessor: 'satEbrw75'
      },
      {
        Header: 'SAT Overall',
        accessor: 'satOverall'
      },
      {
        Header: 'ACT Math 25',
        accessor: 'actMath25'
      },
      {
        Header: 'ACT Math 50',
        accessor: 'actMath50'
      },
      {
        Header: 'ACT Math 75',
        accessor: 'actMath75'
      },
      {
        Header: 'ACT English 25',
        accessor: 'actEnglish25'
      },
      {
        Header: 'ACT English 50',
        accessor: 'actEnglish50'
      },
      {
        Header: 'ACT English 75',
        accessor: 'actEnglish75'
      },
      {
        Header: 'Act Composite',
        accessor: 'actComposite'
      }

    ]
    return (
      <div className="page justify-content-center">
        <h3 className="admin-title" style={{ textAlign: 'center' }}>Colleges</h3>
        <div className="btn-group" role="group">
          <button type="button" className="btn btn-outline-info"
            onClick = {() => { this.props.scrapCollegeData(); this.setState({ loading: true }) }}>Scrape College Data</button>
          <button type="button" className="btn btn-outline-info"
            onClick = {() => { this.props.scrapCollegeRanking(); this.setState({ loading: true }) }}>Scrape College Ranking</button>
          <button type="button" className="btn btn-outline-info"
            onClick = {() => { this.props.importCollegeScorecard(); this.setState({ loading: true }) }}>Import College Scorecard</button>
        </div>
        <button type="button" className="btn btn-outline-info float-right"
          onClick = {() => { this.props.getCollegeInfo(); this.setState({ loading: true }) }}
        ><i className="fas fa-sync"/>
        </button>

        <ReactTable columns={columns} data={this.props.collegeInfo} defaultPageSize={10} loading={this.state.loading}/>
      </div>
    )
  }
}

function mapStateToProps (state) {
  return {
    collegeInfo: state.collegeInfo
  }
}

function matchDispatchToProps (dispatch) {
  return bindActionCreators({
    getCollegeInfo: getCollegeInfoActionAsync,
    scrapCollegeData: scrapCollegeDataActionAsync,
    scrapCollegeRanking: scrapCollegeRankingActionAsync,
    importCollegeScorecard: importCollegeScorecardActionAsync
  }, dispatch)
}

export default connect(mapStateToProps, matchDispatchToProps)(Colleges)