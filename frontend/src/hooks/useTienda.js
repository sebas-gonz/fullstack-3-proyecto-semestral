import { useState, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';
export const useTienda = () => {
    const { obtenerToken } = useAuth();
    const API_GW = 'http://localhost:8091/api/v1';

    const [categorias, setCategorias] = useState([]);
    const [productos, setProductos] = useState([]);
    const [cargando, setCargando] = useState(false);

    const listarCategorias = useCallback(async () => {
        try {
            const token = await obtenerToken();
            const res = await axios.get(`${API_GW}/categorias`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setCategorias(res.data);
        } catch (error) {
            console.error(error);
        }
    }, [obtenerToken]);

    const listarProductosPorCategoria = async (categoriaId) => {
        setCargando(true);
        try {
            const token = await obtenerToken();
            const res = await axios.get(`${API_GW}/categorias/${categoriaId}/producto/disponibles`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setProductos(res.data);
        } catch (error) {
            console.error(error);
        } finally {
            setCargando(false);
        }
    };

    const consultarBodegasPorProducto = async (productoId) => {
        try {
            const token = await obtenerToken();
            const res = await axios.get(`${API_GW}/inventarios/producto/${productoId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return res.data;
        } catch (error) {
            console.error(error);
            throw error;
        }
    };

    return {
        categorias,
        productos,
        cargando,
        listarCategorias,
        listarProductosPorCategoria,
        consultarBodegasPorProducto
    };
};