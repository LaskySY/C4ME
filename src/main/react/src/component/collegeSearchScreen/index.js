import React, { Component } from 'react'
import { push } from 'react-router-redux'
import { updateErrorDetailAction } from '../../action'
import { connect } from 'react-redux'
import { BASE_URL } from '../../config/index'
import axios from 'axios'
import Filter from './collegeFilterPanel'
import SearchCard from '../searchCard'
import LoadingPage from '../loadingPage'

class collegeSearchScreen extends Component {
  constructor (props) {
    super(props)
    this.filterInputs = {}
    this.sortBy = 'ranking'
    this.sortOrder = true
    this.state = {
      isFirst: true,
      showFilterPanel: false,
      allowRecommender: false,
      searchInput: '',
      collegeList: [],
      isLoading: false
    }
  }

  componentDidMount () {
    if (this.props.location.state !== undefined) { this.setState(this.props.location.state) }
  }

  handleSearch = () => {
    if (!this.state.isFirst) this.setState({ isLoading: true })
    this.setState({ collegeList: [] })
    axios.post(BASE_URL + '/api/v1/collegeSearch',
      {
        name: this.state.searchInput !== '' ? this.state.searchInput : null,
        ...this.filterInputs,
        sortBy: this.sortBy,
        ascending: this.sortOrder
      },
      {
        headers: { Authorization: localStorage.getItem('userToken') },
        params: { username: localStorage.getItem('username') }
      }
    ).then(response => {
      if (response.data.code === 'success') {
        this.setState({
          collegeList: response.data.data ? response.data.data : [],
          allowRecommender: true
        })
      } else {
        this.props.updateErrorDetail(response.data.code, 'College Search Screen - handleSearch', response.data.message)
        this.props.redirectErrorPage()
      }
      this.setState({ isFirst: false })
      this.setState({ isLoading: false })
    }).catch(error => {
      this.props.updateErrorDetail(null, 'College Search Screen', error.message)
      this.props.redirectErrorPage()
    })
  }

  handleRecommender = () => {
    this.setState({ isLoading: true })
    this.setState({ collegeList: [] })
    axios.post(BASE_URL + '/api/v1/collegeRecommender',
      {
        collegeList: this.state.collegeList.map(college => college.name)
      },
      {
        headers: { Authorization: localStorage.getItem('userToken') },
        params: { username: localStorage.getItem('username') }
      }
    ).then(response => {
      if (response.data.code === 'success') {
        this.setState({ collegeList: response.data.data })
      } else {
        this.props.updateErrorDetail(response.data.code, 'College Search Screen - handleRecommender', response.data.message)
        this.props.redirectErrorPage()
      }
      this.setState({ isLoading: false })
    }).catch(error => {
      this.props.updateErrorDetail(null, 'College Search Screen', error.message)
      this.props.redirectErrorPage()
    })
  }

  redirectCollegePage = (name) => {
    this.props.history.replace(this.props.location.pathname, this.state)
    this.props.history.push('/college/' + name)
  }

  handleFilterInputs = (filter) => {
    this.filterInputs = filter
    this.handleSearch()
  }

  handleSort = (sortBy, sortOrder) => {
    this.sortBy = sortBy
    this.sortOrder = sortOrder
    this.handleSearch()
  }

  render () {
    const sortByOptions = [
      { value: 'admissionRate', label: 'Admission Rate' },
      { value: 'costOfAttendance', label: 'Cost of Attendance' },
      { value: 'name', label: 'Name' },
      { value: 'ranking', label: 'Ranking' }
    ]
    const titlecolors = ['#ffc7c7', '#ff80b0', '#9399ff', '#a9fffd']
    const cardColors = ['#a8e6cf', '#dcedc1', '#ffd3b6', '#ffaaa5']
    const title = 'C,o,l,l,e,g,e, ,S,e,a,r,c,h'
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
            <input className="search-textfield"
              value={this.state.searchInput}
              onChange={e => this.setState({ searchInput: e.target.value })}/>
            <div className="search-search-icon">
              <i className="fas fa-search float-right"
                onClick={this.handleSearch}/>
            </div>
          </div>
          {
            !this.state.isFirst
              ? <div className="search-icon-box" style={{ fontSize: '18px' }}
                data-toggle="collapse" data-target="#collegeFilterPanel">
                <div className="search-search-icon" >
                  <i className="fas fa-filter"></i>
                </div>
              </div>
              : null
          }
          {
            this.state.collegeList && this.state.collegeList.length > 0
              ? <div className="dropdown">
                <div className="search-icon-box dropdown-toggle"
                  style={{ fontSize: '23px' }} data-toggle="dropdown" >
                  <div className="search-search-icon">
                    <i className="fas fa-sort"/>
                  </div>
                </div>
                <div className="dropdown-menu search-dropdown-menu" >
                  {
                    sortByOptions.map(option => (
                      <li key={option.value} className="search-sort-list">
                        {option.label}
                        <div className="search-sort-icon-box float-right"
                          onClick={() => this.handleSort(option.value, false)} >
                          <i className="search-sort-icon fa fa-caret-down" />
                        </div>
                        <div className="search-sort-icon-box float-right"
                          onClick={() => this.handleSort(option.value, true)} >
                          <i className="search-sort-icon fa fa-caret-up" />
                        </div>
                      </li>
                    ))
                  }
                </div>
              </div>
              : null
          }
          {
            this.state.collegeList && this.state.collegeList.length > 0 &&
            this.props.loginState === 1
              ? <div className="btn  search-icon-box" style={{ fontSize: '20px' }}>
                <div className="search-search-icon" onClick={this.handleRecommender}>
                  Recommender
                </div>
              </div>
              : null
          }
        </div>
        <div className="row justify-content-center">
          < Filter className="col-10" id="collegeFilterPanel"
            confirm = {(filter) => this.handleFilterInputs(filter)}
            cancel = {() => this.setState({ showFilterPanel: false })}/>
        </div>
        {
          this.state.isLoading
            ? <LoadingPage fullScreen={false} color="dark"/>
            : null
        }
        {
          this.state.collegeList.length === 0 && !this.state.isFirst && !this.state.isLoading
            ? <SearchCard
              key = "no result"
              color= {cardColors[Math.floor(Math.random() * cardColors.length)]}
              title= "No search result"
              type="college"
            />
            : null
        }
        {
          this.state.collegeList.map(college =>
            <SearchCard
              key = {college.name}
              color= {cardColors[Math.floor(Math.random() * cardColors.length)]}
              title= {college.name}
              redirectDetailPage = {() => this.redirectCollegePage(college.name)}
              website={college.webpage}
              score = {college.recommendationScore}
              type="college"
              info={{
                actComposite: college.actComposite,
                sat: college.satOverall,
                type: college.typeString,
                first: college.ranking,
                tuition: college.costOfAttendance,
                location: college.city + ' ' + college.state,
                enroll: college.numStudentsEnrolled,
                rate: college.admissionRate,
                latitude: college.latitude,
                  longitude: college.longitude
              }}
            />
          )
        }
      </div>
    )
  }
}

const mapStateToProps = state => ({
  loginState: state.status.loginState
})
const mapDispatchToProps = dispatch => ({
  redirectErrorPage: () => dispatch(push('/error')),
  updateErrorDetail: (...args) => dispatch(updateErrorDetailAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(collegeSearchScreen)