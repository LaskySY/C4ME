import React, { Component } from 'react'
import { connect } from 'react-redux'
import { BASE_URL } from '../../config/index'
import axios from 'axios'
import { push } from 'react-router-redux'
import { updateErrorDetailAction } from '../../action'
import SeachCard from '../searchCard'
import LoadingPage from '../loadingPage'
import AsyncSelect from 'react-select/async'

class FindSimilarHighSchoolScreen extends Component {
  constructor (props) {
    super(props)
    this.highSchools = []
    this.state = {
      isFirst: true,
      isSearching: false,
      searchInput: '',
      highSchoolList: [],
      loading: true
    }
  }

  componentDidMount = () => {
    axios.get(BASE_URL + '/api/v1/profile/highSchool',
      {
        headers: { Authorization: localStorage.getItem('userToken') }
      }
    ).then(res => {
      if (res.data.code === 'success') {
        res.data.data.highSchools.forEach((schoolName, index) => {
          this.highSchools.push({ value: index, label: schoolName })
        })
        this.setState({ loading: false })
      } else {
        this.props.updateErrorDetail(res.data.code, 'highSchool Search Screen', res.data.message)
        this.props.redirectErrorPage()
      }
    }).catch(error => {
      this.props.updateErrorDetail(null, 'highSchool Search Screen', error.message)
      this.props.redirectErrorPage()
    })
  }

  handleSearch = () => {
    this.setState({ isFirst: false })
    this.setState({ highSchoolList: [] })
    if (!this.state.isFirst) this.setState({ isSearching: true })
    axios.post(BASE_URL + '/api/v1/findSimilarHighSchool',
      {
        highschoolName: this.state.searchInput,
        username: localStorage.getItem('username')
      },
      {
        headers: { Authorization: localStorage.getItem('userToken') },
        params: { username: localStorage.getItem('username') }
      }
    ).then(response => {
      if (response.data.code === 'success') {
        this.setState({ highSchoolList: response.data.data })
      } else {
        this.props.updateErrorDetail(response.data.code, 'highSchool Search Screen - handleSearch', response.data.message)
        this.props.redirectErrorPage()
      }
      this.setState({ isSearching: false })
    }).catch(error => {
      this.props.updateErrorDetail(null, 'highSchool Search Screen - handleSearch', error.message)
      this.props.redirectErrorPage()
    })
  }

  filterName = inputValue => {
    const options = []

    var counter = 0
    var index = 0
    var min = 0
    var max = this.highSchools.length
    index = Math.floor((max - min) / 2)

    var found = false
    while (!found) {
      if (max - min === 1) break
      var highSchool = this.highSchools[index]
      var cmp = inputValue.localeCompare(highSchool.label)
      if (cmp < 0) {
        max = index
        index = Math.floor((index + min) / 2)
      } else if (cmp > 0) {
        min = index
        index = Math.floor((max + index) / 2)
      } else break
    }

    while (counter < 10 && index < this.highSchools.length) {
      highSchool = this.highSchools[index]
      var idx = this.highSchools[index].value
      if (highSchool.label.toLowerCase().startsWith(inputValue.toLowerCase())) {
        options.push({ value: idx, label: highSchool.label })
        counter++
      }
      index++
    }

    if (counter < 10) {
      index = 0
      while (counter < 10 && index < this.highSchools.length) {
        highSchool = this.highSchools[index]
        idx = this.highSchools[index].value
        if (highSchool.label.toLowerCase().includes(inputValue.toLowerCase()) &&
            !(highSchool.label.toLowerCase().startsWith(inputValue.toLowerCase()))) {
          options.push({ value: idx, label: highSchool.label })
          counter++
        }
        index++
      }
    }
    return options
  }

  loadOptions = (inputValue, callback) => {
    setTimeout(() => {
      callback(this.filterName(inputValue))
    }, 10)
  }

  render () {
    const titlecolors = ['#ffc7c7', '#ff80b0', '#9399ff', '#a9fffd']
    const cardColors = ['#a8e6cf', '#dcedc1', '#ffd3b6', '#ffaaa5']
    const title = 'S,i,m,i,l,a,r, ,H,i,g,h, ,S,c,h,o,o,l'
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
            <AsyncSelect className="search-textfield" defaultOptions cacheOptions
              loadOptions={this.loadOptions} value={{ value: null, label: this.state.searchInput }}
              onChange={(option) => this.setState({ searchInput: option.label })}
              components={{ DropdownIndicator: () => null, IndicatorSeparator: () => null }}
              styles={customStyles}
            />
            <div className="search-search-icon">
              <i className="fas fa-search float-right"
                onClick={this.handleSearch}/>
            </div>
          </div>
        </div>
        {
          this.state.isSearching
            ? <LoadingPage fullScreen={false} color="dark"/>
            : null
        }
        {
          this.state.highSchoolList.map(highSchool =>
            <SeachCard
              key = {highSchool.highschoolName}
              color= {cardColors[Math.floor(Math.random() * cardColors.length)]}
              title= {highSchool.highschoolName}
              type = "highSchool"
              info={{
                first: highSchool.academicQuality,
                type: highSchool.type,
                sat: highSchool.averageSAT,
                location: highSchool.city + ' ' + highSchool.state
              }}
            />
          )
        }
      </div>
    )
  }
}

const mapDispatchToProps = dispatch => ({
  redirectPage: url => dispatch(push(url)),
  redirectErrorPage: () => dispatch(push('/error')),
  updateErrorDetail: (...args) => dispatch(updateErrorDetailAction(...args))
})

export default connect(
  null,
  mapDispatchToProps
)(FindSimilarHighSchoolScreen)