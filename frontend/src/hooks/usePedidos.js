import { useState, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';

export const usePedidos = () => {
    const { obtenerToken } = useAuth();

    const [pedidos, setPedidos] = useState([]);
    const [cargandoPedidos, setCargandoPedidos] = useState(false);

    const baseUrl = 'http://localhost:8091/api/v1/pedidos';

    const listarTodosLosPedidos = useCallback(async (page = 0, size = 50) => {
        setCargandoPedidos(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${baseUrl}?page=${page}&size=${size}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            const rawData = response.data.content || response.data;

            const pedidosMapeados = rawData.map(pedido => ({
                idCorto: pedido.pedidoId.split('-')[0].toUpperCase(),
                fecha: new Date(pedido.fechaRegistro).toLocaleDateString(),
                destino: `${pedido.destino.ciudad} - ${pedido.destino.calle}`,
                items: pedido.detalles ? pedido.detalles.reduce((acc, det) => acc + det.cantidad, 0) : 0,
                total: `$${pedido.total}`,
                estado: pedido.estado,
                raw: pedido
            }));

            setPedidos(pedidosMapeados);
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

    const obtenerPedidosPorUsuario = async (usuarioId) => {
        setCargandoPedidos(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${baseUrl}/usuario/${usuarioId}`, {
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

    return {
        pedidos,
        listarTodosLosPedidos,
        crearPedido,
        listarPedidos,
        obtenerPedidoPorId,
        cargandoPedidos,
        obtenerPedidosPorUsuario,
        obtenerDetallesPorPedido
    };
};