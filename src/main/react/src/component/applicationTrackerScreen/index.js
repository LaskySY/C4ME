import React, { Component } from 'react'
import { push } from 'react-router-redux'
import { updateErrorDetailAction } from '../../action'
import { connect } from 'react-redux'
import { BASE_URL } from '../../config/index'
import axios from 'axios'
import Filter from './applicationFilterPanel'
import SearchCard from '../searchCard'
import LoadingPage from '../loadingPage'
import Select from 'react-select'
import Scatter from './scatterChart'
class applicationTrackerScreen extends Component {
  constructor (props) {
    super(props)
    this.filterInputs = {}
    this.colleges = []
    this.highSchools = []
    this.oldSearchInput = ''
    this.state = {
      isFirst: true,
      showFilterPanel: false,
      allowScatterPlot: false,
      searchInput: '',
      profileList: [],
      averageSatMath: 0,
      averageSatEbrw: 0,
      averageActComposite: 0,
      averageGpa: 0,
      averageSat: 0,
      averageWeightedAvgPercentile: 0,
      averageSatMathAccepted: 0,
      averageSatEbrwAccepted: 0,
      averageActCompositeAccepted: 0,
      averageGpaAccepted: 0,
      loading: true,
      isSearching: false
    }
  }

  componentDidMount () {
    if (this.props.location.state !== undefined) {
      this.setState(this.props.location.state)
    }
    if (this.props.location.query && this.props.location.query.college) {
      this.setState({ searchInput: this.props.location.query.college },
          () => this.handleSearch())
      this.setState({ loading: false })
      return
    }

    axios.get(BASE_URL + '/profile/college',
        {
          headers: { Authorization: localStorage.getItem('userToken') }
        }
    ).then(res => {
      if (res.data.code === 'success') {
        var data = res.data.data.colleges
        this.colleges = data
        this.setState({ loading: false })
      } else {
        this.props.updateErrorDetail(res.data.code, 'Application Tracker Screen', res.data.message)
        this.props.redirectErrorPage()
      }
    }).catch(error => {
      this.props.updateErrorDetail(null, 'Application Tracker Screen', error.message)
      this.props.redirectErrorPage()
    })
  }

  handleSearch = () => {
    if (!this.state.isFirst) this.setState({ isSearching: true })
    this.setState({ profileList: [] })
    if (this.state.searchInput !== this.oldSearchInput) { this.filterInputs = {} }
    axios.post(BASE_URL + '/matchStudents',
        {
          name: this.state.searchInput !== '' ? this.state.searchInput : null,
          ...this.filterInputs
        },
        {
          headers: { Authorization: localStorage.getItem('userToken') },
          params: { username: localStorage.getItem('username') }
        }
    ).then(response => {
      if (response.data.code === 'success') {
        if (this.state.searchInput !== this.oldSearchInput) {
          var tempArray = []
          response.data.data.profiles.forEach(profile => {
            if (profile.schoolName !== null) {
              if (tempArray.indexOf(profile.schoolName) === -1) { tempArray.push(profile.schoolName) }
            }
          })
          this.highSchools = tempArray
        }
        this.oldSearchInput = this.state.searchInput
        this.setState({
          profileList: response.data.data.profiles ? response.data.data.profiles : [],
          allowScatterPlot: true,
          averageSatMath: response.data.data.averageSatMath,
          averageSatEbrw: response.data.data.averageSatEbrw,
          averageActComposite: response.data.data.averageActComposite,
          averageGpa: response.data.data.averageGpa,
          averageSat: response.data.data.averageSat,
          averageWeightedAvgPercentile: response.data.data.averageWeightedAvgPercentileScore,
          averageSatMathAccepted: response.data.data.averageSatMathAccepted,
          averageSatEbrwAccepted: response.data.data.averageSatEbrwAccepted,
          averageActCompositeAccepted: response.data.data.averageActCompositeAccepted,
          averageGpaAccepted: response.data.data.averageGpaAccepted
        })
      } else {
        this.props.updateErrorDetail(response.data.code, 'Application Tracker Screen - handleSearch', response.data.message)
        this.props.redirectErrorPage()
      }
      this.setState({ isSearching: false })
      this.setState({ isFirst: false })
    }).catch(error => {
      this.props.updateErrorDetail(null, 'Application Tracker Screen', error.message)
      this.props.redirectErrorPage()
    })
  }

  redirectProfilePage = (username) => {
    this.props.history.replace(this.props.location.pathname, this.state)
    this.props.history.push('/profile/' + username)
  }

  handleFilterInputs = (filter) => {
    this.filterInputs = filter
    this.handleSearch()
  }


  render () {
    const titlecolors = ['#ffc7c7', '#ff80b0', '#9399ff', '#a9fffd']
    const cardColors = ['#a8e6cf', '#dcedc1', '#ffd3b6', '#ffaaa5']
    const title = 'A,p,p,l,i,c,a,t,i,o,n, ,T,r,a,c,k,e,r'
    const customStyles = {
      control: base => ({
        ...base,
        flex: 1,
        cursor: 'auto',
        background: 'transparent',
        borderRadius: '5px',
        borderColor: 'transparent',
        boxShadow: null,
        '&:hover': { borderColor: 'transparent' }
      })
    }
    if (this.state.loading) return <LoadingPage fullScreen={true} color="dark"/>
    return (
        <div className="page">
          {
            this.state.isFirst
                ? <div className="search-title-box">
                  {
                    title.split(',').map((word, index) => {
                      var color = titlecolors[Math.floor(Math.random() * titlecolors.length)]
                      return <span className="search-title" key={index} style={{ color: color }}>{word}</span>
                    })
                  }
                </div>
                : null
          }
          <div className="search-search">
            <div className="search-search-box">
              <Select id="collegeName" className="search-textfield" options={this.colleges}
                      components={{ DropdownIndicator: () => null, IndicatorSeparator: () => null }}
                      defaultValue={{ value: this.state.searchInput, label: this.state.searchInput }} styles={customStyles}
                      onChange={selectedOption => this.setState({ searchInput: selectedOption.label })}/>
              <div className="search-search-icon">
                <i className="fas fa-search float-right"
                   onClick={this.handleSearch}/>
              </div>
            </div>
            {
              !this.state.isFirst
                  ? <div className="search-icon-box" style={{ fontSize: '18px' }}
                         data-toggle="collapse" data-target="#applicationTrackerFilterPanel">
                    <div className="search-search-icon" >
                      <i className="fas fa-filter"></i>
                    </div>
                  </div>
                  : null
            }
            {
              // for scatter plot button
              this.state.profileList && this.state.profileList.length > 0
                  ? <div className="search-icon-box" style={{ fontSize: '18px' }}
                         data-toggle="modal" data-target="#scatterModal">
                    <div className="search-search-icon" >
                      <i className="fas fa-chart-bar"></i>
                    </div>
                  </div>
                  : null
            }
          </div>
          <div className="row justify-content-center">
            < Filter className="col-10" id="applicationTrackerFilterPanel"
                     confirm = {(filter) => this.handleFilterInputs(filter)}
                     highSchools = {this.highSchools}
                     cancel = {() => this.setState({ showFilterPanel: false })}/>
          </div>
          {
            this.state.isSearching
                ? <LoadingPage fullScreen={false} color="dark"/>
                : null
          }
          {
            this.state.profileList.length === 0 && !this.state.isFirst && !this.state.isSearching
                ? <SearchCard
                    key = "no result"
                    color= {cardColors[Math.floor(Math.random() * cardColors.length)]}
                    title= "No search result"
                    type= "application"
                />
                : null
          }
          {
            this.state.profileList.map(student =>
                <SearchCard
                    color= {cardColors[Math.floor(Math.random() * cardColors.length)]}
                    title= {student.username}
                    redirectDetailPage = {() => this.redirectProfilePage(student.username)}
                    type = "application"
                    info={{
                      gpa: student.gpa,
                      schoolYear: student.schoolYear,
                      satMath: student.satMath,
                      satEbrw: student.satEbrw,
                      actComposite: student.actComposite,
                      applicationStatus: student.applicationStatus
                    }}
                    content = {{
                      score: student.satEbrw
                    }}
                />
            )
          }
          {
            <div className="modal fade" id="scatterModal" tabIndex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
              <div className="modal-dialog modal-dialog-centered modal-lg" role="document">
                <div className="modal-content">
                  <div className="modal-header">
                    <h5 className="modal-title" id="scatterTitle" style={{ fontFamily: 'Asul' }}></h5>
                    <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                  <div className="modal-body" style={{ height: '800px' }} >
                  <span className='row' style={{ fontFamily: 'Asul', marginLeft: '250px', fontWeight: 'bold' }}>
                    Average Scores for All Applications
                  </span>
                    <div className='row' style={{ fontFamily: 'Asul', marginLeft: '120px' }}>
                      GPA:&nbsp;{this.state.averageGpa === null ? 'null' :this.state.averageGpa.toFixed(1)}&emsp;
                      SAT Math:&nbsp;{this.state.averageSatMath === null ? ' null' :this.state.averageSatMath.toFixed(1)}&emsp;
                      SAT EBRW:&nbsp;{this.state.averageSatEbrw === null ? ' null' :this.state.averageSatEbrw.toFixed(1)}&emsp;
                      ACT Composite:&nbsp;{this.state.averageActComposite === null ? ' null' :this.state.averageActComposite.toFixed(1)}&emsp;
                    </div>
                    <br/>
                    <span className='row' style={{ fontFamily: 'Asul', marginLeft: '230px', fontWeight: 'bold' }}>
                    Average Scores for Accepted Applications
                  </span>
                    <div className='row' style={{ fontFamily: 'Asul', marginLeft: '120px' }}>
                      GPA:&nbsp;{this.state.averageGpaAccepted === null ? 'null' :this.state.averageGpaAccepted.toFixed(1)}&emsp;
                      SAT Math:&nbsp;{this.state.averageSatMathAccepted === null ? 'null' :this.state.averageSatMathAccepted.toFixed(1)}&emsp;
                      SAT EBRW:&nbsp;{this.state.averageSatEbrwAccepted === null ? 'null' :this.state.averageSatEbrwAccepted.toFixed(1)}&emsp;
                      ACT Composite:&nbsp;{this.state.averageActCompositeAccepted === null ? 'null' :this.state.averageActCompositeAccepted.toFixed(1)}&emsp;
                    </div>
                    <br/>
                    <div style={{ marginLeft: '83px' }}>
                      <Scatter profileList = {this.state.profileList}
                               meanSat = {this.state.averageSat}
                               meanAct = {this.state.averageActComposite}
                               meanGpa = {this.state.averageGpa}
                               meanWeightedAvgPercentileScore = {this.state.averageWeightedAvgPercentile}
                      />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          }
        </div>
    )
  }
}

const mapDispatchToProps = dispatch => ({
  redirectErrorPage: () => dispatch(push('/error')),
  updateErrorDetail: (...args) => dispatch(updateErrorDetailAction(...args))
})

export default connect(
    null,
    mapDispatchToProps
)(applicationTrackerScreen)