import axios from "axios";
const api_url = '';

const service = axios.create({
    baseURL: api_url,
    timeout: 5000,
    withCredentials:true
})

export default service