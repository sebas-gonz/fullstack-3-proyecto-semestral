import { useState, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from './useAuth';

export const useInventarios = () => {
    const { obtenerToken } = useAuth();
    const [inventarios, setInventarios] = useState([]);
    const [cargando, setCargando] = useState(false);
    const [error, setError] = useState(null);

    const API_URL = 'http://localhost:8091/api/v1/inventarios';

    const listarInventarios = useCallback(async () => {
        setCargando(true);
        setError(null);
        try {
            const token = await obtenerToken();
            const response = await axios.get(API_URL, {
                headers: { Authorization: `Bearer ${token}` }
            });

            const rawData = response.data.content || response.data;
            const inventariosMapeados = rawData.map(inv => {
                const stockTotal = inv.stocks ? inv.stocks.reduce((acc, lote) => acc + lote.cantidad, 0) : 0;
                const direccionFormateada = inv.ubicacion
                    ? `${inv.ubicacion.calle} ${inv.ubicacion.numero}, ${inv.ubicacion.ciudad}`
                    : 'Sin ubicación registrada';
                return {
                    id: inv.inventarioId,
                    nombre: inv.nombre,
                    ubicacion: direccionFormateada,
                    stockTotal: stockTotal,
                    raw: inv
                };
            });
            setInventarios(inventariosMapeados);
        } catch (err) {
            console.error("Error al obtener inventarios:", err);
            setError("No se pudieron cargar los inventarios.");
        } finally {
            setCargando(false);
        }
    }, [obtenerToken]);
    const crearInventario = async (datosInventario) => {
        try {
            const token = await obtenerToken();
            const response = await axios.post(API_URL, datosInventario, {
                headers: { Authorization: `Bearer ${token}` }
            });
            return response.data;
        } catch (err) {
            console.error("Error al crear inventario:", err);
            throw err;
        }
    };
    const actualizarInventario = async (id, datos) => {
        const token = await obtenerToken();
        await axios.put(`${API_URL}/${id}`, datos, { headers: { Authorization: `Bearer ${token}` } });
    };

    return {
        inventarios,
        cargando,
        error,
        listarInventarios,
        crearInventario,
        actualizarInventario
    };
};