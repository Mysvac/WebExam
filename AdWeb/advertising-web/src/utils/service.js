import axios from "axios";
const base_url = "http://localhost:8080";
const api_url = '';

const service = axios.create({
    baseURL: base_url + api_url,
    timeout: 5000,
    withCredentials:true
})

export default service