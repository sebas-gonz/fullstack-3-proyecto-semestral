import { useState, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';

export const usePedidos = () => {
    const { obtenerToken } = useAuth();

    const [pedidos, setPedidos] = useState([]);
    const [cargandoPedidos, setCargandoPedidos] = useState(false);

    const baseUrl = 'http://localhost:8091/api/v1/pedidos';
    const bffUrl = 'http://localhost:8091/api/bff/pedidos';
    const mapearPedidos = (rawData) => {
        return rawData.map(pedido => ({
            idCorto: pedido.pedidoId.split('-')[0].toUpperCase(),
            fecha: new Date(pedido.fechaRegistro).toLocaleDateString(),
            origen: pedido.origen ? `${pedido.origen.ciudad} - ${pedido.origen.calle} ${pedido.origen.numero}` : 'No especificado',
            destino: pedido.destino ? `${pedido.destino.ciudad} - ${pedido.destino.calle} ${pedido.destino.numero}` : 'No especificado',
            items: pedido.detalles ? pedido.detalles.reduce((acc, det) => acc + det.cantidad, 0) : 0,
            total: `$${pedido.total}`,
            costoEnvio: `$${pedido.costoEnvio || 0}`,
            estado: pedido.estado,
            raw: pedido
        }));
    };

    const listarTodosLosPedidos = useCallback(async (page = 0) => {
        setCargandoPedidos(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${baseUrl}?page=${page}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            const rawData = response.data.content || response.data;
            setPedidos(mapearPedidos(rawData));
            return response.data;
        } catch (error) {
            console.error(error);
            throw error;
        } finally {
            setCargandoPedidos(false);
        }
    }, [obtenerToken]);

    const crearPedido = async (pedidoRequestDTO) => {
        setCargandoPedidos(true);
        try {
            const token = await obtenerToken();
            const response = await axios.post(baseUrl, pedidoRequestDTO, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error(error);
            throw error;
        } finally {
            setCargandoPedidos(false);
        }
    };

    const listarPedidos = async (page = 0, size = 10) => {
        setCargandoPedidos(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${baseUrl}?page=${page}&size=${size}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error(error);
            throw error;
        } finally {
            setCargandoPedidos(false);
        }
    };

    const obtenerPedidoPorId = async (pedidoId) => {
        setCargandoPedidos(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${baseUrl}/${pedidoId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error(`Error al obtener el pedido ${pedidoId} `, error);
            throw error;
        } finally {
            setCargandoPedidos(false);
        }
    };

    const obtenerPedidosPorUsuario = async (usuarioId, page = 0, size = 10) => {
        setCargandoPedidos(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${baseUrl}/usuario/${usuarioId}`, {
                params: {
                    page: page,
                    size: size
                },
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error(error);
            throw error;
        } finally {
            setCargandoPedidos(false);
        }
    };

    const obtenerDetallesPorPedido = async (pedidoId) => {
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${baseUrl}/${pedidoId}/detalles`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error(`Error al obtener los detalles del pedido ${pedidoId} `, error);
            throw error;
        }
    };

    const cotizarCostoEnvio = async (cotizacionRequestDTO) => {
        setCargandoPedidos(true);
        try {
            const token = await obtenerToken();
            const response = await axios.post(`${bffUrl}/cotizar-envio`, cotizacionRequestDTO, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error("Error al cotizar el envío:", error);
            throw error;
        } finally {
            setCargandoPedidos(false);
        }
    };

    const obtenerEstadosPedidos = useCallback(async () => {
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${bffUrl}/estados-pedidos`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error("Error al obtener estados:", error);
            throw error;
        }
    }, [obtenerToken]);

    const obtenerPedidosPorEstado = useCallback(async (estado, page = 0) => {
        setCargandoPedidos(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${bffUrl}/estado/${estado}?page=${page}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            const rawData = response.data.content || response.data;
            setPedidos(mapearPedidos(rawData));
            return response.data;
        } catch (error) {
            console.error(`Error al obtener pedidos por estado ${estado}:`, error);
            throw error;
        } finally {
            setCargandoPedidos(false);
        }
    }, [obtenerToken]);

    const buscarPedidos = useCallback(async (parametro, page = 0) => {
        setCargandoPedidos(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${bffUrl}/buscar-pedido?parametro=${parametro}&page=${page}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            const rawData = response.data.content || response.data;
            setPedidos(mapearPedidos(rawData));
            return response.data;
        } catch (error) {
            console.error(`Error al buscar pedidos con parámetro ${parametro}:`, error);
            throw error;
        } finally {
            setCargandoPedidos(false);
        }
    }, [obtenerToken]);

    return {
        pedidos,
        listarTodosLosPedidos,
        crearPedido,
        listarPedidos,
        obtenerPedidoPorId,
        cargandoPedidos,
        obtenerPedidosPorUsuario,
        obtenerDetallesPorPedido,
        cotizarCostoEnvio,
        obtenerEstadosPedidos,
        obtenerPedidosPorEstado,
        buscarPedidos,
    };
};