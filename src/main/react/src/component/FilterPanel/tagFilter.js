import React, { Component } from 'react'
import { withTranslation } from 'react-i18next'
import ReactTooltip from 'react-tooltip'

class TagFilter extends Component {
  state = {
    showCollapse: false
  }

  static defaultProps = {
    expand: false,
    multiSelect: false
  }

  handleMultiSelect = (e, index) => {
    e.stopPropagation()
    let newSelectedList = []
    if (this.props.selected != null) { newSelectedList = newSelectedList.concat(this.props.selected) }
    newSelectedList.push(index)
    return this.props.onSelectTag(newSelectedList)
  }

  handleRemoveMultiSelect = (e, index) => {
    e.stopPropagation()
    const newSelectedList = this.props.selected
    if (newSelectedList.length === 1) { return this.props.onSelectTag(null) }
    newSelectedList.splice(newSelectedList.indexOf(index), 1)
    return this.props.onSelectTag(newSelectedList)
  }

  handleSelect = (e, index) => {
    e.stopPropagation()
    return this.props.onSelectTag(index)
  }

  handleRemoveSelect = (e, index) => {
    e.stopPropagation()
    return this.props.onSelectTag(null)
  }

  render () {
    const Tags = ({ tags, selected, multiSelect, tagMapping }) => (
      <div className="tags-container card-body"
        style={{ padding: '0px' }}>
        {
          tags.map((tag, index) =>
            multiSelect === true
              ? selected && selected.includes(index)
                ? <span className="filter_tag select" key={tag}
                  onClick={e => this.handleRemoveMultiSelect(e, index)}
                  data-for='test' data-tip={tag}
                >{tag}
                </span>
                : <span className="filter_tag " key={tag}
                  onClick={e => this.handleMultiSelect(e, index)}
                  data-for='test' data-tip={tag}
                >{tag}
                </span>
              : index === selected
                ? <span className="filter_tag select" key={tag}
                  onClick={e => this.handleRemoveSelect(e, index)}
                  data-for='test' data-tip={tag}
                >{tag}
                </span>
                : <span className="filter_tag " key={tag}
                  onClick={e => this.handleSelect(e, index)}
                  data-for='test' data-tip={tag}
                >{tag}
                </span>
          )
        }
        {
          tagMapping !== undefined
            ? <ReactTooltip id='test' place="bottom" effect="solid" type='info' delayShow={600}
              getContent={dataTip => tagMapping[dataTip] } />
            : null
        }

      </div>
    )
    return (
      <div>
        <div className = "filter_tag_row row">
          <label className="filter_label col-auto">{ this.props.title + ': '}</label>
          <div className="filter_content col-auto">
            {
              this.props.expand
                ? <div className="row" style={{ margin: '0px' }}>
                  <div className="filter_multiSelect_bar select col">
                    {
                      this.props.selected == null
                        ? ''
                        : this.props.selected.map(selectTag =>
                          <span className="filter_multiSelect_tag" key={this.props.tags[selectTag]}
                          >{this.props.tags[selectTag]}
                          </span>
                        )
                    }
                    <div className="btn justify-content-center"
                      data-toggle="collapse" data-target={'#multiselectcollapsePanel_' + this.props.id}
                      onClick={() => this.setState({ showCollapse: !this.state.showCollapse })}>
                      {
                        this.state.showCollapse
                          ? <i className="fas fa-angle-up"/>
                          : <i className="fas fa-angle-down"/>
                      }
                    </div>
                  </div>

                </div>
                : <Tags
                  tags = {this.props.tags}
                  selected = {this.props.selected}
                  multiSelect = {this.props.multiSelect}
                  tagMapping = {this.props.tagMapping}
                />
            }
          </div>
        </div>
        <div className="collapse row" style={{ margin: '0px' }}
          id={'multiselectcollapsePanel_' + this.props.id}>
          <div className="collapse_panel col select">
            <Tags
              tags = {this.props.tags}
              selected = {this.props.selected}
              multiSelect = {this.props.multiSelect}
              tagMapping = {this.props.tagMapping}
            />
          </div>
        </div>

      </div>
    )
  }
}

export default withTranslation()(TagFilter)
