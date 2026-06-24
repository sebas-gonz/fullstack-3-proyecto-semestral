import { useState, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';

export const useEnvios = () => {
    const { obtenerToken } = useAuth();
    const [envios, setEnvios] = useState([]);
    const [enviosAdmin, setEnviosAdmin] = useState([]);

    const [cargando, setCargando] = useState(false);
    const API_URL = 'http://localhost:8091/api/v1/envios';
    const bffAdminUrl = 'http://localhost:8091/api/bff/envios';
    const mapearEnvios = (data) => {
        const contenido = data.content || data;
        return contenido.map(envio => ({
            id: envio.envioId,
            estado: envio.estado,
            pedidoIdCorto:  envio.pedidoId.split('-')[0].toUpperCase(),
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
            return res;
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

        } finally {
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

    const mapearEnviosAdmin = (data) => {
        const contenido = data.content || data;
        return contenido.map(envio => ({
            idCorto: envio.envioId.split('-')[0].toUpperCase(),
            pedidoIdCorto: envio.pedidoId.split('-')[0].toUpperCase(),
            nombreComprador: envio.nombreComprador,
            nombreRepartidor: envio.nombreRepartidor,
            origenStr: `${envio.origen.calle} ${envio.origen.numero}, ${envio.origen.ciudad}`,
            destinoStr: `${envio.destino.calle} ${envio.destino.numero}, ${envio.destino.ciudad}`,
            origenCoords: { lat: Number(envio.origen.latitude), lng: Number(envio.origen.longitude) },
            destinoCoords: { lat: Number(envio.destino.latitude), lng: Number(envio.destino.longitude) },
            total: `$${envio.totalPedido}`,
            estado: envio.estado,
            fechaRegistro: new Date(envio.fechaRegistro).toLocaleDateString(),
            raw: envio
        }));
    };

    const listarTodosLosEnviosAdmin = useCallback(async (page = 0, size = 10) => {
        setCargando(true);
        try {
            const token = await obtenerToken();
            const res = await axios.get(`${bffAdminUrl}/admin?page=${page}&size=${size}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setEnviosAdmin(mapearEnviosAdmin(res.data));
            return res.data;
        } catch (err) {
            console.error("Error al cargar envíos admin:", err);
        } finally {
            setCargando(false);
        }
    }, [obtenerToken]);

    const obtenerEstadosPosiblesDeEnvio = useCallback(async () => {
        try {
            const token = await obtenerToken();
            const res = await axios.get(`${bffAdminUrl}/estados-posibles`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return res.data;
        } catch (err) {
            console.error("Error al obtener estados:", err);
            return [];
        }
    }, [obtenerToken]);

    const obtenerEnviosPorEstadoAdmin = useCallback(async (estado, page = 0, size = 10) => {
        setCargando(true);
        try {
            const token = await obtenerToken();
            const res = await axios.get(`${bffAdminUrl}/estado/${estado}?page=${page}&size=${size}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setEnviosAdmin(mapearEnviosAdmin(res.data));
            return res.data;
        } catch (err) {
            console.error(`Error al buscar envíos por estado ${estado}:`, err);
        } finally {
            setCargando(false);
        }
    }, [obtenerToken]);

    return {
        envios,
        cargando,
        listarEnviosDisponibles,
        listarEnviosEnRuta,
        listarEnviosEntregados,
        tomarPedido, entregarPedido,
        enviosAdmin,
        listarTodosLosEnviosAdmin,
        obtenerEstadosPosiblesDeEnvio,
        obtenerEnviosPorEstadoAdmin
    };
};