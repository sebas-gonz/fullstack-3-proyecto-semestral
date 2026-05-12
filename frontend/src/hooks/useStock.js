import { useState, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';

export const useStocks = () => {
    const { obtenerToken } = useAuth();
    const [stocks, setStocks] = useState([]);
    const [cargandoStocks, setCargandoStocks] = useState(false);
    const baseUrl = (inventarioId) => `http://localhost:8091/api/v1/inventarios/${inventarioId}/stock`;

    const listarStocks = useCallback(async (inventarioId) => {
        setCargandoStocks(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(baseUrl(inventarioId), {
                headers: { Authorization: `Bearer ${token}` }
            });
            setStocks(response.data);
            return response.data;
        } catch (error) {
            console.error(`Error al listar stocks del inventario ${inventarioId}:`, error);
            throw error;
        } finally {
            setCargandoStocks(false);
        }
    }, [obtenerToken]);

    const obtenerStockPorId = async (inventarioId, stockId) => {
        setCargandoStocks(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${baseUrl(inventarioId)}/${stockId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error(`Error al obtener el stock ${stockId}:`, error);
            throw error;
        } finally {
            setCargandoStocks(false);
        }
    };

    const crearStock = async (inventarioId, stockRequest) => {
        setCargandoStocks(true);
        try {
            const token = await obtenerToken();
            const response = await axios.post(baseUrl(inventarioId), stockRequest, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error("Error al crear stock:", error);
            throw error;
        } finally {
            setCargandoStocks(false);
        }
    };

    const actualizarStock = async (inventarioId, stockId, stockRequest) => {
        setCargandoStocks(true);
        try {
            const token = await obtenerToken();
            const response = await axios.put(`${baseUrl(inventarioId)}/${stockId}`, stockRequest, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error("Error al actualizar stock:", error);
            throw error;
        } finally {
            setCargandoStocks(false);
        }
    };

    const eliminarStock = async (inventarioId, stockId) => {
        setCargandoStocks(true);
        try {
            const token = await obtenerToken();
            await axios.delete(`${baseUrl(inventarioId)}/${stockId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return true;
        } catch (error) {
            console.error("Error al eliminar stock:", error);
            throw error;
        } finally {
            setCargandoStocks(false);
        }
    };
    return {
        stocks,
        cargandoStocks,
        listarStocks,
        obtenerStockPorId,
        crearStock,
        actualizarStock,
        eliminarStock
    };
};