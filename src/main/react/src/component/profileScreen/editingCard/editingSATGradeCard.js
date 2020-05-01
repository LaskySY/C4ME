import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'

import { editingSATGradeAction, updateProfileAction } from '../../../action'
import { updateNumberCheck } from '../../../util/validateCheck'


class EditingSATCard extends Component {
  state = {
    satMath: this.props.profile.satMath,
    satEbrw: this.props.profile.satEbrw,
    satLiterature: this.props.profile.satLiterature,
    satUsHist: this.props.profile.satUsHist,
    satWorldHist: this.props.profile.satWorldHist,
    satMathI: this.props.profile.satMathI,
    satMathIi: this.props.profile.satMathIi,
    satEcoBio: this.props.profile.satEcoBio,
    satMolBio: this.props.profile.satMolBio,
    satChemistry: this.props.profile.satChemistry,
    satPhysics: this.props.profile.satPhysics
  }

  save = () => {
    this.props.updateProfile({
      satMath: this.state.satMath,
      satEbrw: this.state.satEbrw,
      satLiterature: this.state.satLiterature,
      satUsHist: this.state.satUsHist,
      satWorldHist: this.state.satWorldHist,
      satMathI: this.state.satMathI,
      satMathIi: this.state.satMathIi,
      satEcoBio: this.state.satEcoBio,
      satMolBio: this.state.satMolBio,
      satChemistry: this.state.satChemistry,
      satPhysics: this.state.satPhysics
    }, 'sat')
    this.props.changeState(false)
  }

  satCheck = value => {
    if (value === null) return true
    if (value > 800 || value < 200 || isNaN(value) || Math.floor(value) !== value || value % 10 !== 0) { return false } else { return true }
  }

  render () {
    const isValid = this.satCheck(this.state.satMath) && this.satCheck(this.state.satEbrw) &&
      this.satCheck(this.state.satLiterature) && this.satCheck(this.state.satUsHist) &&
      this.satCheck(this.state.satWorldHist) && this.satCheck(this.state.satMathI) &&
      this.satCheck(this.state.satMathIi) && this.satCheck(this.state.satEcoBio) &&
      this.satCheck(this.state.satMolBio) && this.satCheck(this.state.satChemistry) &&
      this.satCheck(this.state.satPhysics)
    return (
      <div className="card-body">
        <h5 className="card-title" ><span className="profile-card-title">SAT Grade</span></h5>
        <div className="row card-text">
          <div className="col">
            <label htmlFor="satMath">Math</label>
            <input type="number" id="satMath"
              className={['profile_textfield form-control', this.satCheck(this.state.satMath) ? '' : 'invalid'].join(' ')}
              value={this.state.satMath} onChange={e => this.setState({ satMath: updateNumberCheck(e.target.value) })}/>
            <label htmlFor="satWorldHist">World History</label>
            <input type="number" id="satWorldHist"
              className={['profile_textfield form-control', this.satCheck(this.state.satWorldHist) ? '' : 'invalid'].join(' ')}
              value={this.state.satWorldHist} onChange={e => this.setState({ satWorldHist: updateNumberCheck(e.target.value) })}/>
            <label htmlFor="satLiterature">Literature</label>
            <input type="number" id="satLiterature"
              className={['profile_textfield form-control', this.satCheck(this.state.satLiterature) ? '' : 'invalid'].join(' ')}
              value={this.state.satLiterature} onChange={e => this.setState({ satLiterature: updateNumberCheck(e.target.value) })}/>
            <label htmlFor="satUsHist">US History</label>
            <input type="number" id="satUsHist"
              className={['profile_textfield form-control', this.satCheck(this.state.satUsHist) ? '' : 'invalid'].join(' ')}
              value={this.state.satUsHist} onChange={e => this.setState({ satUsHist: updateNumberCheck(e.target.value) })}/>
          </div>
          <div className="col">
            <label htmlFor="satEbrw">EBRW</label>
            <input type="number" id="satEbrw"
              className={['profile_textfield form-control', this.satCheck(this.state.satEbrw) ? '' : 'invalid'].join(' ')}
              value={this.state.satEbrw} onChange={e => this.setState({ satEbrw: updateNumberCheck(e.target.value) })}/>
            <label htmlFor="satEcoBio">Biology-E</label>
            <input type="number" id="satEcoBio"
              className={['profile_textfield form-control', this.satCheck(this.state.satEcoBio) ? '' : 'invalid'].join(' ')}
              value={this.state.satEcoBio} onChange={e => this.setState({ satEcoBio: updateNumberCheck(e.target.value) })}/>
            <label htmlFor="satMolBio">Biology-M</label>
            <input type="number" id="satMolBio"
              className={['profile_textfield form-control', this.satCheck(this.state.satMolBio) ? '' : 'invalid'].join(' ')}
              value={this.state.satMolBio} onChange={e => this.setState({ satMolBio: updateNumberCheck(e.target.value) })}/>
            <label htmlFor="satChemistry">Chemistry</label>
            <input type="number" id="satChemistry"
              className={['profile_textfield form-control', this.satCheck(this.state.satChemistry) ? '' : 'invalid'].join(' ')}
              value={this.state.satChemistry} onChange={e => this.setState({ satChemistry: updateNumberCheck(e.target.value) })}/>
          </div>
          <div className="col">
            <label htmlFor="satPhysics">Physics</label>
            <input type="number" id="satPhysics"
              className={['profile_textfield form-control', this.satCheck(this.state.satPhysics) ? '' : 'invalid'].join(' ')}
              value={this.state.satPhysics} onChange={e => this.setState({ satPhysics: updateNumberCheck(e.target.value) })}/>
            <label htmlFor="satMathI">Math I</label>
            <input type="number" id="satMathI"
              className={['profile_textfield form-control', this.satCheck(this.state.satMathI) ? '' : 'invalid'].join(' ')}
              value={this.state.satMathI} onChange={e => this.setState({ satMathI: updateNumberCheck(e.target.value) })}/>
            <label htmlFor="satMathIi">Math II</label>
            <input type="number" id="satMathIi"
              className={['profile_textfield form-control', this.satCheck(this.state.satMathIi) ? '' : 'invalid'].join(' ')}
              value={this.state.satMathIi} onChange={e => this.setState({ satMathIi: updateNumberCheck(e.target.value) })}/>
          </div>
        </div>
        <button className="editcard-button btn float-right"
          onClick={() => this.props.changeState(false)}
        >Cancel</button>
        <button className={['editcard-button btn float-right', isValid ? '' : 'disabled'].join(' ')}
          onClick={this.save} disabled={!isValid}>Save</button>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  profile: state.profile.data
})

const mapDispatchToProps = dispatch => ({
  changeState: (...args) => dispatch(editingSATGradeAction(...args)),
  updateProfile: (...args) => dispatch(updateProfileAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(EditingSATCard))
