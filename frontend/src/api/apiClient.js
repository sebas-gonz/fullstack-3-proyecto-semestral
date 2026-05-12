import axios from 'axios';
const apiClient = axios.create({
    baseURL: 'http://localhost:8091/api/v1',
    headers: {
        'Content-Type': 'application/json'
    }
});
export const configurarInterceptor = (obtenerToken) => {
    apiClient.interceptors.request.use(
        async (config) => {
            try {
                const token = await obtenerToken();
                if (token) {
                    config.headers.Authorization = `Bearer ${token}`;
                }
            } catch (error) {
                console.error("Error al obtener el token de Auth0", error);
            }
            return config;
        },
        (error) => {
            return Promise.reject(error);
        }
    );
};

export default apiClient;