import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_SW_WIKI_API_URL

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 60000,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosInstance.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    console.log("Error", error);
    return Promise.reject(error);
  }
);

export default axiosInstance;