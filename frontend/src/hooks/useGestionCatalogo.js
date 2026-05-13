import { useState, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';

export const useGestionCatalogo = () => {
    const { obtenerToken } = useAuth();
    const [categorias, setCategorias] = useState([]);
    const [productosDeCategoria, setProductosDeCategoria] = useState([]);
    const [productosMaestros, setProductosMaestros] = useState([]);
    const [cargandoCatalogo, setCargandoCatalogo] = useState(false);
    const baseUrlCategorias = 'http://localhost:8091/api/v1/categorias';

    const listarCatalogoCompleto = useCallback(async () => {
        setCargandoCatalogo(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(baseUrlCategorias, {
                headers: { Authorization: `Bearer ${token}` }
            });
            const categoriasData = response.data;
            setCategorias(categoriasData);

            let todosLosProductos = [];
            categoriasData.forEach(cat => {
                if (cat.productos) {
                    const prodConContexto = cat.productos.map(p => ({
                        ...p,
                        categoria: cat.nombre
                    }));
                    todosLosProductos = [...todosLosProductos, ...prodConContexto];
                }
            });
            setProductosMaestros(todosLosProductos);
        } catch (error) {
            console.error(error);
        } finally {
            setCargandoCatalogo(false);
        }
    }, [obtenerToken]);
    const listarCategorias = useCallback(async () => {
        setCargandoCatalogo(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(baseUrlCategorias, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setCategorias(response.data);
        } catch (error) {
            console.error(error);
        } finally {
            setCargandoCatalogo(false);
        }
    }, [obtenerToken]);

    const listarProductosDeCategoria = useCallback(async (categoriaId) => {
        setCargandoCatalogo(true);
        try {
            const token = await obtenerToken();
            const response = await axios.get(`${baseUrlCategorias}/${categoriaId}/producto`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setProductosDeCategoria(response.data);
        } catch (error) {
            console.error(error);
        } finally {
            setCargandoCatalogo(false);
        }
    }, [obtenerToken]);

    const crearNuevaCategoria = async (categoriaData) => {
        try {
            const token = await obtenerToken();
            await axios.post(baseUrlCategorias, categoriaData, {
                headers: { Authorization: `Bearer ${token}` }
            });
        } catch (error) {
            console.error(error);
            throw error;
        }
    };

    const eliminarCategoria = async (categoriaId) => {
        try {
            const token = await obtenerToken();
            await axios.delete(`${baseUrlCategorias}/${categoriaId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
        } catch (error) {
            console.error(error);
            throw error;
        }
    };

    const crearNuevoProducto = async (categoriaId, productoData) => {
        try {
            const token = await obtenerToken();
            await axios.post(`${baseUrlCategorias}/${categoriaId}/producto`, productoData, {
                headers: { Authorization: `Bearer ${token}` }
            });
        } catch (error) {
            console.error(error);
            throw error;
        }
    };

    const eliminarProducto = async (productoId) => {
        try {
            const token = await obtenerToken();
            await axios.delete(`${baseUrlCategorias}/ignorado/producto/${productoId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
        } catch (error) {
            console.error(error);
            throw error;
        }
    };

    return {
        categorias,
        productosDeCategoria,
        productosMaestros,
        cargandoCatalogo,
        listarCategorias,
        listarProductosDeCategoria,
        listarCatalogoCompleto,
        crearNuevaCategoria,
        eliminarCategoria,
        crearNuevoProducto,
        eliminarProducto
    };
};