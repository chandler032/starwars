import {configureStore} from "@reduxjs/toolkit";
import searchSlice from "./searchSlice.js";

const store = configureStore({
  reducer: {
    search: searchSlice,
  }
});

export default store;