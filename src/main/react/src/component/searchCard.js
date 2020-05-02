import React, { Component } from 'react'
import { linkCheck } from '../util/validateCheck'

class searchCard extends Component {
  state = {
    color: '#ffffff'
  }

  render () {
    return (
      <div className="search-card-box"
        style={{ backgroundColor: this.state.color }}
        onMouseOver={() => this.setState({ color: this.props.color })}
        onMouseOut={() => this.setState({ color: '#ffffff' })}>
        <div className="card-body"
          onClick={this.props.redirectDetailPage}
          style={{ cursor: this.props.redirectDetailPage ? 'pointer' : 'default' }} >
          <div className="row">
            <h5 className="search-card-title col">
              <a target ="_blank" href={linkCheck(this.props.website)}
                style={{ cursor: this.props.website ? 'pointer' : 'default' }}
                onClick={e => e.stopPropagation()}>
                {this.props.title}
              </a>
            </h5>
            {
              this.props.type === 'college'
                ? <div className="col-3" style={{ paddingTop: '0.45rem' }}>
                  {
                    this.props.score
                      ? <span calssName="float-right" >
                        <i className="search-card-info-icon fas" style={{ fontSize: '0.9rem' }}>Score</i>
                        {this.props.score.toFixed(4)}
                      </span>
                      : null
                  }
                </div>
                : null
            }
          </div>
          <div className="info-row row">
            {
              this.props.type === 'college' || this.props.type === 'highSchool'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.first
                      ? <span>
                        <i className="search-card-info-icon fas fa-medal"/>
                        {this.props.info.first}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'college' || this.props.type === 'highSchool'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.type
                      ? <span>
                        <i className="search-card-info-icon fas fa-university"/>
                        {this.props.info.type}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'college' || this.props.type === 'highSchool'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.sat
                      ? <span>
                        <i className="search-card-info-icon fas" style={{ fontSize: '0.9rem' }}>median SAT</i>
                        {this.props.info.sat}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'application'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.gpa
                      ? <span>
                        <i className="search-card-info-icon fas" style={{ fontSize: '0.9rem' }}>GPA</i>
                        {this.props.info.gpa}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'application'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.schoolYear
                      ? <span>
                        <i className="search-card-info-icon fas" style={{ fontSize: '0.9rem' }}>School Year</i>
                        {this.props.info.schoolYear}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'application'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.satMath
                      ? <span>
                        <i className="search-card-info-icon fas" style={{ fontSize: '0.9rem' }}>SAT Math</i>
                        {this.props.info.satMath}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'application'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.satEbrw
                      ? <span>
                        <i className="search-card-info-icon fas" style={{ fontSize: '0.9rem' }}>SAT EBRW</i>
                        {this.props.info.satEbrw}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'college' || this.props.type === 'application'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.actComposite
                      ? <span>
                        <i className="search-card-info-icon fas" style={{ fontSize: '0.9rem' }}>ACT Composite</i>
                        {this.props.info.actComposite}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'application'
                ? <div className="col-6">
                  {
                    this.props.info && this.props.info.applicationStatus
                      ? <span>
                        <i className="search-card-info-icon fas" style={{ fontSize: '0.9rem' }}>Application Status</i>
                        {this.props.info.applicationStatus}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'college'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.tuition
                      ? <span>
                        <i className="search-card-info-icon fas fa-dollar-sign"/>
                        {this.props.info.tuition}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'highSchool'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.location
                      ? <span>
                        <i className="search-card-info-icon fas fa-map-marker-alt"/>
                        {this.props.info.location}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'college'
              ? <div className="col-3">
                {
                  this.props.info && this.props.info.location
                      ? <span>
                            <i className="search-card-info-icon fas fa-map-marker-alt"/>
                              <a target="_blank"
                                 href={"http://maps.google.com/?q=" + this.props.info.latitude + "," + this.props.info.longitude}
                                 style={{cursor: this.props.info.latitude && this.props.info.longitude ? 'pointer' : 'default'}}
                                 onClick={e => e.stopPropagation()}>
                                {this.props.info.location}
                              </a>
                        {/*{this.props.info.location}*/}
                          </span>
                      : null
                }
              </div>
              : null
            }
            {
              this.props.type === 'college'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.enroll
                      ? <span>
                        <i className="search-card-info-icon fas fa-user"/>
                        {this.props.info.enroll}
                      </span>
                      : null
                  }
                </div>
                : null
            }
            {
              this.props.type === 'college'
                ? <div className="col-3">
                  {
                    this.props.info && this.props.info.rate
                      ? <span>
                        <i className="search-card-info-icon fas fa-percentage"/>
                        {this.props.info.rate}
                      </span>
                      : null
                  }
                </div>
                : null
            }
          </div>
        </div>
      </div>
    )
  }
}

export default searchCard
