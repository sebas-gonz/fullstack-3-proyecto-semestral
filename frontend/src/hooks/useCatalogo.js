import { useState, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';

export const useCatalogo = () => {
    const { obtenerToken } = useAuth();
    const [productos, setProductos] = useState([]);
    const [cargando, setCargando] = useState(false);
    const [error, setError] = useState(null);
    const API_URL = 'http://localhost:8091/api/v1/categorias';

    const listarProductosDelCatalogo = useCallback(async () => {
        setCargando(true);
        setError(null);
        try {
            const token = await obtenerToken();
            const config = {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            };

            const response = await axios.get(API_URL, config);
            const categorias = response.data;
            let todosLosProductos = [];
            categorias.forEach(categoria => {
                if (categoria.productos) {
                    const productosMapeados = categoria.productos.map(prod => ({
                        id: prod.productoId,
                        sku: prod.sku,
                        nombre: prod.nombre,
                        descripcion: prod.descripcion,
                        precio: prod.precioBase,
                        stockDisponible: prod.cantidadTotal,
                        categoria: categoria.nombre
                    }));
                    todosLosProductos = [...todosLosProductos, ...productosMapeados];
                }
            });
            setProductos(todosLosProductos);
        } catch (err) {
            console.error(err);
            setError("No se pudo cargar el catálogo de productos.");
        } finally {
            setCargando(false);
        }
    }, [obtenerToken]);

    return {
        productos,
        cargando,
        error,
        listarProductosDelCatalogo
    };
};