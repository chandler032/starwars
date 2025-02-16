import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axiosInstance from "../axios/axiosConfig.js";

const searchState = {
  searchResult: null,
  searchErrorMessage: null,
  enableOfflineMode: false,
  showLoader: false,
  showErrorMessage: false,
  resultCount: null,
  searchText: null,
  searchType: null,
  showResultContent: false
}

export const searchData = createAsyncThunk(
  "search/searchData",
  async (searchQuery, {rejectWithValue}) => {
    try {
      const response = await axiosInstance.get(`/search?type=${searchQuery.type}&value=${searchQuery.value}`);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const enableOfflineMode = createAsyncThunk(
  "search/enableOfflineMode",
  async (offlineMode, {rejectWithValue}) => {
    try {
      const response = await axiosInstance.put(`/offline?enable=${offlineMode}`);
      if (response.status === 200) {
        return response.data;
      }
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
)

const searchSlice = createSlice({
  name: "search",
  initialState: searchState,
  reducers: {
    setOfflineMode: (state, action) => {
      state.enableOfflineMode = !state.enableOfflineMode;
    },
    setSearchQuery: (state, action) => {
      state.searchText = action.payload
    },
    setSearchQueryType: (state, action) => {
      state.searchType = action.payload;
    },
    resetResultCount: (state, action) => {
      state.resultCount = action.payload;
    }
  },
  extraReducers: (builder) => {
    builder.addCase(searchData.pending, (state, action) => {
      state.showLoader = true;
      state.showErrorMessage = false;
      state.resultCount = 0;
      state.showResultContent = true;
    })
      .addCase(searchData.fulfilled, (state, action) => {
        let apiResult = action.payload;
        console.log("api result", apiResult);
        state.searchResult = {
          ...apiResult
        };
        state.resultCount = apiResult.count;
        state.showLoader = false;
        state.showErrorMessage = false;
        state.showResultContent = true;
      })
      .addCase(searchData.rejected, (state, action) => {
        console.log(action.payload);
        state.searchErrorMessage = action.payload.message;
        state.showLoader = false;
        state.showErrorMessage = true;
        state.showResultContent = false;
      });
    builder.addCase(enableOfflineMode.fulfilled, (state, action) => {
      state.enableOfflineMode = !state.enableOfflineMode;
    });
  }
});

export const searchResult = state => state.search.searchResult;
export const resultCount = state => state.search.resultCount;
export const getSearchText = state => state.search.searchText;
export const getSearchType = state => state.search.searchType;
export const getShowResultContent = state => state.search.showResultContent;
export const getErrorMessage = state => state.search.searchErrorMessage;
export const getShowErrorMessage = state => state.search.showErrorMessage;
export const isOfflineModeEnabled = state => state.search.enableOfflineMode;

export const {
  setOfflineMode,
  setSearchQuery,
  setSearchQueryType,
  resetResultCount
} = searchSlice.actions;

export default searchSlice.reducer;