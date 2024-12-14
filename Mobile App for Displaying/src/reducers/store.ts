import {configureStore} from '@reduxjs/toolkit';

import reducer from './index.ts';

export const store = configureStore({
  reducer: reducer,
});
