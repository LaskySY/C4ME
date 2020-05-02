import React, { Component } from 'react'
import { connect } from 'react-redux'
import { withTranslation } from 'react-i18next'
import { editingACTGradeAction, updateProfileAction } from '../../../action'
import { updateNumberCheck } from '../../../util/validateCheck'

class EditingACTCard extends Component {
  state = {
    actMath: this.props.profile.actMath,
    actEnglish: this.props.profile.actEnglish,
    actReading: this.props.profile.actReading,
    actScience: this.props.profile.actScience,
    actComposite: this.props.profile.actComposite
  }

  save = () => {
    this.props.updateProfile({
      actMath: this.state.actMath,
      actEnglish: this.state.actEnglish,
      actReading: this.state.actReading,
      actScience: this.state.actScience,
      actComposite: this.state.actComposite
    }, 'act')
    this.props.changeState(false)
  }

  actCheck = value => {
    if (value === null) return true
    if (value > 36 || value < 1 || isNaN(value) || Math.floor(value) !== value) { return false } else { return true }
  }

  render () {
    const isValid = this.actCheck(this.state.actMath) && this.actCheck(this.state.actReading) &&
      this.actCheck(this.state.actEnglish) && this.actCheck(this.state.actComposite) && this.actCheck(this.state.actScience)
    return (
      <div className="card-body">
        <h5 className="card-title"><span className="profile-card-title">ACT Grade</span></h5>
        <div className="row card-text">
          <div className = "col">
            <label htmlFor="actMath">Math</label>
            <input type="number" id="actMath"
              className={['profile_textfield form-control', this.actCheck(this.state.actMath) ? '' : 'invalid'].join(' ')}
              value={this.state.actMath} onChange={e => this.setState({ actMath: updateNumberCheck(e.target.value) })}/>
            <label htmlFor="actReading">Reading</label>
            <input type="number" id="actReading"
              className={['profile_textfield form-control', this.actCheck(this.state.actReading) ? '' : 'invalid'].join(' ')}
              value={this.state.actReading} onChange={e => this.setState({ actReading: updateNumberCheck(e.target.value) })}/>
            <label htmlFor="actComposite">Composite</label>
            <input type="number" id="actComposite"
              className={['profile_textfield form-control', this.actCheck(this.state.actComposite) ? '' : 'invalid'].join(' ')}
              value={this.state.actComposite} onChange={e => this.setState({ actComposite: updateNumberCheck(e.target.value) })}/>
          </div>
          <div className="col">
            <label htmlFor="actEnglish">English</label>
            <input type="number" id="actEnglish"
              className={['profile_textfield form-control', this.actCheck(this.state.actEnglish) ? '' : 'invalid'].join(' ')}
              value={this.state.actEnglish} onChange={e => this.setState({ actEnglish: updateNumberCheck(e.target.value) })}/>
            <label htmlFor="actScience">Science</label>
            <input type="number" id="actScience"
              className={['profile_textfield form-control', this.actCheck(this.state.actScience) ? '' : 'invalid'].join(' ')}
              value={this.state.actScience} onChange={e => this.setState({ actScience: updateNumberCheck(e.target.value) })}/>
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
  changeState: (...args) => dispatch(editingACTGradeAction(...args)),
  updateProfile: (...args) => dispatch(updateProfileAction(...args))
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withTranslation()(EditingACTCard))
