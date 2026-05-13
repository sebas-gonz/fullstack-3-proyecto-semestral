import { useState } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';

export const useAdmin = () => {
    const { obtenerToken } = useAuth();
    const [enviando, setEnviando] = useState(false);

    const API_GATEWAY = 'http://localhost:8091/api/v1';
    const crearInventario = async (datos) => {
        setEnviando(true);
        try {
            const token = await obtenerToken();
            await axios.post(`${API_GATEWAY}/inventarios`, datos, {
                headers: { Authorization: `Bearer ${token}` }
            });
        } catch (err) {
            console.error(err)
        } finally {
            setEnviando(false);
        }
    };
    const agregarLote = async (inventarioId, datosLote) => {
        setEnviando(true);
        try {
            const token = await obtenerToken();
            await axios.post(`${API_GATEWAY}/inventarios/${inventarioId}/stocks`, datosLote, {
                headers: { Authorization: `Bearer ${token}` }
            });
        } catch (err) {
            console.error(err)
        } finally { setEnviando(false); }
    };
    return { crearInventario, agregarLote, enviando };
};