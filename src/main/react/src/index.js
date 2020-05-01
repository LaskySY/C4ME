import React from 'react'
import { render } from 'react-dom'
import { createStore, applyMiddleware } from 'redux'
import { Provider } from 'react-redux'
import { routerMiddleware } from 'react-router-redux'
import { createBrowserHistory as createHistory } from 'history'
import { composeWithDevTools } from 'redux-devtools-extension'
import createSagaMiddleware from 'redux-saga'
import { PersistGate } from 'redux-persist/lib/integration/react'
import { persistStore, persistReducer } from 'redux-persist'
import autoMergeLevel2 from 'redux-persist/lib/stateReconciler/autoMergeLevel2'
import storage from 'redux-persist/lib/storage'
import logger from 'redux-logger'
import App from './App'
import reducers from './reducer'
import rootSaga from './saga'

const persistConfig = {
  key: 'root',
  storage,
  whitelist: ['status'],
  stateReconciler: autoMergeLevel2
}

const history = createHistory()
const sagaMiddleware = createSagaMiddleware()

const store = createStore(
  persistReducer(
    persistConfig,
    reducers
  ),
  composeWithDevTools(
    applyMiddleware(
      sagaMiddleware,
      logger,
      routerMiddleware(history)
    )
  )
)

sagaMiddleware.run(rootSaga)

const persistor = persistStore(store)

render(
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <App history={history}/>
    </PersistGate>
  </Provider>,
  document.getElementById('root')
)
