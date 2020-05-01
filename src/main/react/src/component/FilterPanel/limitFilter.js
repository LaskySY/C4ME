import React, { Component } from 'react'
import { Formik, Form, Field } from 'formik'
import * as yup from 'yup'
import ReactTooltip from 'react-tooltip'

class LimitFilter extends Component {
  constructor (props) {
    super(props)
    this.lower = this.props.idLabel + ' lower'
    this.upper = this.props.idLabel + ' upper'
    this.state = {
      showButton: 'none'
    }
  }

  hidePanel= () => {
    this.setState({ showButton: 'none' })
    const textfield = document.getElementsByClassName('filter_textfield')
    for (const el of textfield) {
      el.blur()
    }
  }

  render () {
    return (
      <div className = "filter_limit_row row">
        <label className="filter_label col-auto">{ this.props.title + ': '}</label>
        <div className="filter_content col-auto row"
          onFocus={() => this.setState({ showButton: 'inline' })}>
          <Formik
            initialValues={{
              [this.lower]: this.props.value.lower ? this.props.value.lower.toString() : '',
              [this.upper]: this.props.value.upper ? this.props.value.upper.toString() : ''
            }}
            onSubmit={ values => {
              let lower = values[this.lower]
              let upper = values[this.upper]
              if (values[this.lower].trim() === '') { lower = null } else { lower = parseFloat(lower) }
              if (values[this.upper].trim() === '') { upper = null } else { upper = parseFloat(upper) }
              console.log(lower, upper)
              this.props.onChangeLimit(lower, upper)
              this.hidePanel()
            }}
            validationSchema={yup.object().shape(this.props.validationSchema)}
          >
            {
              props => {
                this.props.dirtyCheck(props.dirty)
                return <Form>
                  <Field name={this.lower} data-tip data-for={[this.lower] + 'Message'}
                    className={['filter_textfield', props.errors[this.lower] ? 'invalid' : ''].join(' ')}/>
                  <span>&nbsp;{' ~ '}&nbsp;</span>
                  <Field name={this.upper} data-tip data-for={[this.upper] + 'Message'}
                    className={['filter_textfield', props.errors[this.upper] ? 'invalid' : ''].join(' ')}/>
                  <button type="submit" style={{ display: this.state.showButton }}
                    className={['btn btn-sm filter_button', props.isValid ? '' : 'disabled'].join(' ')}
                  ><i className="fas fa-check"/>
                  </button>
                  <ReactTooltip id={[this.lower] + 'Message'} place="bottom" type="error" effect="solid"
                    getContent={() => props.errors[this.lower] ? props.errors[this.lower] : null}/>
                  <ReactTooltip id={[this.upper] + 'Message'} place="bottom" type="error" effect="solid"
                    getContent={() => props.errors[this.upper] ? props.errors[this.upper] : null}/>
                </Form>
              }
            }
          </Formik>
        </div>
      </div>
    )
  }
}

export default LimitFilter