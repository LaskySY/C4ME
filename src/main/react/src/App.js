import React, { Component } from 'react'
import { Router, Switch, Route, Redirect } from 'react-router-dom'
import Header from './component/header'
import LoginScreen from './component/loginScreen'
import RegisterScreen from './component/registerScreen'
import AdminApplication from './component/AdminScreen/applications'
import AdminCollege from './component/AdminScreen/colleges'
import AdminProfile from './component/AdminScreen/profiles'
import Profile from './component/profileScreen/index.js'
import FindSimilarHighSchool from './component/FindSimilarHighSchoolScreen'
import CollegeSearch from './component/collegeSearchScreen'
import ApplicationTracker from './component/applicationTrackerScreen'
import College from './component/collegeScreen'
import PrivateRoute from './PrivateRoute'
import ErrorPage from './component/errorPage'
import 'react-table/react-table.css'
import './config/i18'
import './css/profile.css'
import './css/filter.css'
import './css/common.css'
import './css/navbar.css'
import './css/auth.css'
import './css/collegeDetail.css'
import './css/search.css'
import './css/searchCard.css'
import './css/errorPage.css'

class App extends Component {
  render () {
    return (
      <Router history={this.props.history}>
        <div className="App">
          <Header />
          <Switch>
            <PrivateRoute exact path="/" role="student" component={CollegeSearch} />
            <Route path="/login" component={LoginScreen} />
            <Route path="/register" component={RegisterScreen} />
            <Route path="/error" exact component={ErrorPage}/>
            <PrivateRoute path="/admin/application" role="admin" component={AdminApplication} />
            <PrivateRoute path="/admin/college" role="admin" component={AdminCollege} />
            <PrivateRoute path="/admin/profile" role="admin" component={AdminProfile} />
            <PrivateRoute path="/profile/:username" role="student" component={Profile} />
            <PrivateRoute path="/college/:collegeName" role="student" component={College} />
            <PrivateRoute path = '/findSimilarHighSchool' role="student" component = {FindSimilarHighSchool}/>
            <PrivateRoute path = '/applicationTracker' role="student" component = {ApplicationTracker}/>
            <Redirect from='' to="/" />
          </Switch>
        </div>
      </Router>
    )
  }
}

export default App
