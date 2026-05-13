import { useState, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';

export const useEnvios = () => {
    const { obtenerToken } = useAuth();
    const [envios, setEnvios] = useState([]);
    const [cargando, setCargando] = useState(false);
    const API_URL = 'http://localhost:8091/api/v1/envios';
    const mapearEnvios = (data) => {
        const contenido = data.content || data;
        return contenido.map(envio => ({
            id: envio.envioId,
            estado: envio.estado,
            origenStr: `${envio.origen.calle} ${envio.origen.numero}, ${envio.origen.ciudad}`,
            origenCoords: { lat: Number(envio.origen.latitude), lng: Number(envio.origen.longitude) },
            destinoStr: `${envio.destino.calle} ${envio.destino.numero}, ${envio.destino.ciudad}`,
            destinoCoords: { lat: Number(envio.destino.latitude), lng: Number(envio.destino.longitude) }
        }));
    };
    const listarEnviosDisponibles = useCallback(async () => {
        setCargando(true);
        try {
            const token = await obtenerToken();
            const res = await axios.get(`${API_URL}/disponibles`, { headers: { Authorization: `Bearer ${token}` } });
            setEnvios(mapearEnvios(res.data));
        } catch (err) {
            console.error(err);
        } finally {
            setCargando(false);
        }
    }, [obtenerToken]);

    const listarEnviosEnRuta = useCallback(async (repartidorId) => {
        setCargando(true);
        try {
            const token = await obtenerToken();
            const res = await axios.get(`${API_URL}/${repartidorId}/enruta`, { headers: { Authorization: `Bearer ${token}` } });
            setEnvios(mapearEnvios(res.data));
        } catch (err) {
            console.error(err);
         setCargando(false);
        }
    }, [obtenerToken]);

    const listarEnviosEntregados = useCallback(async (repartidorId) => {
        setCargando(true);
        try {
            const token = await obtenerToken();
            const res = await axios.get(`${API_URL}/${repartidorId}/entregado`, { headers: { Authorization: `Bearer ${token}` } });
            setEnvios(mapearEnvios(res.data));
        } catch (err) {
            console.error(err);
        } finally {
            setCargando(false);
        }
    }, [obtenerToken]);

    const tomarPedido = async (envioId, repartidorId) => {
        const token = await obtenerToken();
        await axios.post(`${API_URL}/${envioId}/asignar?repartidorId=${repartidorId}`, null, {
            headers: { Authorization: `Bearer ${token}` }
        });
    };

    const entregarPedido = async (envioId) => {
        const token = await obtenerToken();
        await axios.post(`${API_URL}/${envioId}/entregar`, null, {
            headers: { Authorization: `Bearer ${token}` }
        });
    };

    return {
        envios, cargando,
        listarEnviosDisponibles, listarEnviosEnRuta, listarEnviosEntregados,
        tomarPedido, entregarPedido
    };
};