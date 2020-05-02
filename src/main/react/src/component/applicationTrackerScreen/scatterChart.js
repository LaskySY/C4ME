import React, { Component } from 'react'
import { Scatter } from 'react-chartjs-2'
import 'chartjs-plugin-annotation'

class scatterChart extends Component {
  constructor (props) {
    super(props)
    this.state = {
      scoreType: 'SAT',
      chartData: {
        labels: 'My First dataset',
        datasets: [
          {
            label: 'Accepted',
            fill: true,
            backgroundColor: '#4efc03',
            pointBackgroundColor: '#4efc03',
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: '#4efc03',
            pointHoverBorderColor: '#4efc03',
            pointHoverBorderWidth: 1,
            pointRadius: 5,
            pointHitRadius: 10,
            data: []
          },
          {
            label: 'Denied',
            fill: true,
            backgroundColor: '#fc4103',
            pointBorderColor: '#fc4103',
            pointBackgroundColor: '#fc4103',
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: '#fc4103',
            pointHoverBorderColor: '#fc4103',
            pointHoverBorderWidth: 1,
            pointRadius: 5,
            pointHitRadius: 10,
            data: []
          },
          {
            label: 'Other',
            fill: true,
            backgroundColor: '#fcec03',
            pointBorderColor: '#fcec03',
            pointBackgroundColor: '#fcec03',
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            pointHoverBackgroundColor: '#fcec03',
            pointHoverBorderColor: '#fcec03',
            pointHoverBorderWidth: 1,
            pointRadius: 5,
            pointHitRadius: 10,
            data: []
          }
        ]
      },
      options: {
        maintainAspectRatio: false,
        annotation: {
          annotations: []
        },
        legend: {
          onClick: (e) => e.stopPropagation()
        }
      }
    }
  }

  componentWillReceiveProps (props) {
    // construct initialChartData
    var acceptedData = []
    var rejectedData = []
    var otherData = []
    var dashedLineData = []
    props.profileList.forEach(student => {
      if (student.applicationStatus === 'Accepted') {
        if (student.satMath === null) { acceptedData.push({ x: student.satEbrw, y: student.gpa }) } else if (student.satEbrw === null) { acceptedData.push({ x: student.satMath, y: student.gpa }) } else if (student.satEbrw !== null && student.satMath !== null) { acceptedData.push({ x: student.satEbrw + student.satMath, y: student.gpa }) }
      } else if (student.applicationStatus === 'Denied') {
        if (student.satMath === null) { rejectedData.push({ x: student.satEbrw, y: student.gpa }) } else if (student.satEbrw === null) { rejectedData.push({ x: student.satMath, y: student.gpa }) } else if (student.satEbrw !== null && student.satMath !== null) { rejectedData.push({ x: student.satEbrw + student.satMath, y: student.gpa }) }
      } else {
        if (student.satMath === null) { otherData.push({ x: student.satEbrw, y: student.gpa }) } else if (student.satEbrw === null) { otherData.push({ x: student.satMath, y: student.gpa }) } else if (student.satEbrw !== null && student.satMath !== null) { otherData.push({ x: student.satEbrw + student.satMath, y: student.gpa }) }
      }
    })
    if ((props.meanGpa !== 0 && props.meanGpa !== null) && (props.meanSat !== 0 && props.meanSat !== null)) {
      // construct dashed mean GPA and mean SAT line data
      dashedLineData = [
        {
          type: 'line',
          mode: 'horizontal',
          scaleID: 'y-axis-1',
          value: props.meanGpa,
          borderColor: 'black',
          borderWidth: 1,
          borderDash: [3, 3],
          label: {
            enabled: false,
            content: 'mean GPA'
          }
        },
        {
          type: 'line',
          mode: 'vertical',
          scaleID: 'x-axis-1',
          value: props.meanSat,
          borderColor: 'black',
          borderWidth: 1,
          borderDash: [3, 3],
          label: {
            enabled: false,
            content: 'mean SAT'
          }
        }
      ]
    }
    var initialChart = this.state.chartData
    initialChart.datasets[0].data = acceptedData
    initialChart.datasets[1].data = rejectedData
    initialChart.datasets[2].data = otherData
    var initialOptions = this.state.options
    initialOptions.annotation.annotations = dashedLineData
    this.setState({
      chartData: initialChart,
      scoreType: 'SAT',
      options: initialOptions
    })
    // construct initial mean dash lines
  }

  handleRadioButton = (e) => {
    var acceptedData = []
    var rejectedData = []
    var otherData = []
    var dashedLineData = []
    var basicLine = [
      {
        type: 'line',
        mode: 'horizontal',
        scaleID: 'y-axis-1',
        value: 0,
        borderColor: 'black',
        borderWidth: 1,
        borderDash: [3, 3],
        label: {
          enabled: false,
          content: 'mean GPA'
        }
      },
      {
        type: 'line',
        mode: 'vertical',
        scaleID: 'x-axis-1',
        value: 0,
        borderColor: 'black',
        borderWidth: 1,
        borderDash: [3, 3],
        label: {
          enabled: false,
          content: 'mean SAT'
        }
      }
    ]
    this.props.profileList.forEach(student => {
      if (e.target.name === 'SAT') {
        if (student.applicationStatus === 'Accepted') {
          if (student.satMath === null) { acceptedData.push({ x: student.satEbrw, y: student.gpa }) } else if (student.satEbrw === null) { acceptedData.push({ x: student.satMath, y: student.gpa }) } else if (student.satEbrw !== null && student.satMath !== null) { acceptedData.push({ x: student.satEbrw + student.satMath, y: student.gpa }) }
        } else if (student.applicationStatus === 'Denied') {
          if (student.satMath === null) { rejectedData.push({ x: student.satEbrw, y: student.gpa }) } else if (student.satEbrw === null) { rejectedData.push({ x: student.satMath, y: student.gpa }) } else if (student.satEbrw !== null && student.satMath !== null) { rejectedData.push({ x: student.satEbrw + student.satMath, y: student.gpa }) }
        } else {
          if (student.satMath === null) { otherData.push({ x: student.satEbrw, y: student.gpa }) } else if (student.satEbrw === null) { otherData.push({ x: student.satMath, y: student.gpa }) } else if (student.satEbrw !== null && student.satMath !== null) { otherData.push({ x: student.satEbrw + student.satMath, y: student.gpa }) }
        }
      } else if (e.target.name === 'ACT') {
        if (student.applicationStatus === 'Accepted') {
          if (student.actComposite !== null) { acceptedData.push({ x: student.actComposite, y: student.gpa }) }
        } else if (student.applicationStatus === 'Denied') {
          if (student.actComposite !== null) { rejectedData.push({ x: student.actComposite, y: student.gpa }) }
        } else {
          if (student.actComposite !== null) { otherData.push({ x: student.actComposite, y: student.gpa }) }
        }
      } else {
        if (student.applicationStatus === 'Accepted') {
          if (student.weightedAvgPercentileScore !== null) { acceptedData.push({ x: student.weightedAvgPercentileScore, y: student.gpa }) }
        } else if (student.applicationStatus === 'Denied') {
          if (student.weightedAvgPercentileScore !== null) { rejectedData.push({ x: student.weightedAvgPercentileScore, y: student.gpa }) }
        } else {
          if (student.actComposite !== null) { otherData.push({ x: student.weightedAvgPercentileScore, y: student.gpa }) }
        }
      }
    })
    if (e.target.name === 'SAT') {
      if ((this.props.meanGpa !== 0 && this.props.meanGpa !== null) && (this.props.meanSat !== 0 && this.props.meanSat !== null)) {
        basicLine[0].value = this.props.meanGpa
        basicLine[1].value = this.props.meanSat
        dashedLineData = basicLine
      }
    } else if (e.target.name === 'ACT') {
      if ((this.props.meanGpa !== 0 && this.props.meanGpa !== null) && (this.props.meanAct !== 0 && this.props.meanAct !== null)) {
        basicLine[0].value = this.props.meanGpa
        basicLine[1].value = this.props.meanAct
        dashedLineData = basicLine
      }
    } else {
      if ((this.props.meanGpa !== 0 && this.props.meanGpa !== null) && (this.props.meanWeightedAvgPercentileScore !== 0 && this.props.meanWeightedAvgPercentileScore !== null)) {
        basicLine[0].value = this.props.meanGpa
        basicLine[1].value = this.props.meanWeightedAvgPercentileScore
        dashedLineData = basicLine
      }
    }
    var newChartData = this.state.chartData
    var newOptions = this.state.options
    newChartData.datasets[0].data = acceptedData
    newChartData.datasets[1].data = rejectedData
    newChartData.datasets[2].data = otherData
    newOptions.annotation.annotations = dashedLineData
    this.setState({
      chartData: newChartData,
      scoreType: e.target.name,
      options: newOptions
    })
  }

  render () {
    return (
        <div style={{ width: '600px', height: '500px' }} className="chart">
          <h6 style={{
            fontWeight: 'bold',
            fontFamily: 'Asul',
            marginTop: '20px'
          }}>GPA</h6>
          <Scatter data={this.state.chartData} options={this.state.options}/>
          <div className="btn-group btn-group-toggle" data-toggle="buttons" style={{ marginLeft: '130px' }}>
            <label className={this.state.scoreType === 'SAT' ? 'btn btn-secondary active' : 'btn btn-secondary'}>
              <input type="radio" name="SAT" id="option1" autoComplete="off" onClick={this.handleRadioButton}/> SAT
            </label>
            <label className={this.state.scoreType === 'ACT' ? 'btn btn-secondary active' : 'btn btn-secondary'}>
              <input type="radio" name="ACT" id="option2" autoComplete="off" onClick={this.handleRadioButton}/> ACT
            </label>
            <label className={this.state.scoreType === 'weightedAveragePercentile' ? 'btn btn-secondary active' : 'btn btn-secondary'}>
              <input type="radio" name="weightedAveragePercentile" id="option3" autoComplete="off" onClick={this.handleRadioButton}/> Weighted Average Percentile
            </label>
          </div>
        </div>
    )
  }
}

export default scatterChart