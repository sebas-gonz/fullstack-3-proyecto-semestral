import { useState, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';

export const useUsuario = () => {
    const { obtenerToken } = useAuth();
    const [cargandoUsuario, setCargandoUsuario] = useState(false);

    const API_URL = 'http://localhost:8091/api/v1/usuarios';
    const bffUrl = 'http://localhost:8091/api/bff/usuarios';
    const verificarUsuarioPorAuth0 = useCallback(async (idAuth0) => {
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${API_URL}/${idAuth0}/auth0`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            if (error.response && error.response.status === 404) {
                return null;
            }
            throw error;
        }
    }, [obtenerToken]);
    const registrarUsuario = async (datosUsuario) => {
        setCargandoUsuario(true);
        try {
            const token = await obtenerToken();
            const response = await axios.post(API_URL, datosUsuario, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error(error);
            throw error;
        } finally {
            setCargandoUsuario(false);
        }
    };
    const actualizarUsuario = async (uuid, datosUsuario) => {
        setCargandoUsuario(true);
        try {
            const token = await obtenerToken();
            const response = await axios.put(`${API_URL}/${uuid}`, datosUsuario, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error(error);
            throw error;
        } finally {
            setCargandoUsuario(false);
        }
    };
    const obtenerRepartidores = async (page = 0) => {
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${bffUrl}/repartidores?page=${page}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error("Error al obtener repartidores:", error);
            return { content: [], totalPages: 0 };
        }
    };
    return {
        verificarUsuarioPorAuth0,
        registrarUsuario,
        actualizarUsuario,
        cargandoUsuario,
        obtenerRepartidores
    };
};