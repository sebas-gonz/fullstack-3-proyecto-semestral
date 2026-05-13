import { useState } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';

export const useDireccionesUsuario = () => {
    const { obtenerToken } = useAuth();
    const [cargandoDireccion, setCargandoDireccion] = useState(false);
    const baseUrl = (usuarioId) => `http://localhost:8091/api/v1/usuarios/${usuarioId}/ubicaciones`;

    const agregarDireccion = async (usuarioId, datosDireccion) => {
        setCargandoDireccion(true);
        try {
            const token = await obtenerToken();
            const response = await axios.post(baseUrl(usuarioId), datosDireccion, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error(error);
            throw error;
        } finally {
            setCargandoDireccion(false);
        }
    };

    const eliminarDireccion = async (usuarioId, direccionId) => {
        setCargandoDireccion(true);
        try {
            const token = await obtenerToken();
            await axios.delete(`${baseUrl(usuarioId)}/delete/${direccionId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return true;
        } catch (error) {
            console.error(error);
            throw error;
        } finally {
            setCargandoDireccion(false);
        }
    };

    return {
        agregarDireccion,
        eliminarDireccion,
        cargandoDireccion
    };
};