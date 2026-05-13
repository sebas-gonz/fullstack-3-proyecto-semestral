import React, { useEffect, useState } from 'react';
import TablaGenerica from "../TablaGenerica.jsx";
import { AgregarStock } from "./modal/AgregarStock.jsx";
import { EditarStock } from "./modal/EditarStock.jsx";
import { useInventarios } from "../../hooks/useInventarios.js";
import {useStocks} from "../../hooks/useStock.js";

export const AdminStocks = ({ bodega, onVolver }) => {
    const {
        obtenerStocksDetallados,
        eliminarStock
    } = useInventarios();
    const {
        crearStock,
        actualizarStock,
    } = useStocks()

    const [stocks, setStocks] = useState([]);
    const [cargando, setCargando] = useState(true);

    const [modalStock, setModalStock] = useState({ visible: false });
    const [stockAEditar, setStockAEditar] = useState(null);
    const cargarDatos = async () => {
        if (!bodega?.id) return;
        setCargando(true);
        try {
            const data = await obtenerStocksDetallados(bodega.id);
            setStocks(data);
        } catch (error) {
            console.error(error);
        } finally {
            setCargando(false);
        }
    };
    useEffect(() => {
        cargarDatos();
    }, [bodega.id]);

    const columnas = [
        { label: 'Lote', key: 'lote' },
        { label: 'Producto', key: 'productoNombre' },
        { label: 'SKU', key: 'productoSku' },
        { label: 'Cantidad', key: 'cantidad' }
    ];

    const handleGuardarStock = async (inventarioId, stockRequest) => {
        try {
            await crearStock(inventarioId, stockRequest);
            setModalStock({ visible: false });
            cargarDatos();
        } catch (error) {
            console.error(error);
        }
    };

    const handleActualizarStock = async (inventarioId, stockId, stockRequest) => {
        try {
            await actualizarStock(inventarioId, stockId, stockRequest);
            setStockAEditar(null);
            cargarDatos();
        } catch (error) {
            console.error(error);
        }
    };

    const handleEliminarStock = async (stockId) => {
        if(window.confirm("¿Seguro que deseas eliminar este lote? Esto impactará el catálogo general.")) {
            await eliminarStock(bodega.id, stockId);
            cargarDatos();
        }
    };

    return (
        <div className="card border-info shadow-sm">
            <div className="card-header bg-info text-white d-flex justify-content-between align-items-center">
                <h5 className="mb-0"><i className="bi bi-box-seam me-2"></i>Lotes en: {bodega.nombre}</h5>
                <button className="btn btn-sm btn-light text-info fw-bold" onClick={onVolver}>
                    Volver a Bodegas
                </button>
            </div>
            <div className="card-body">
                <div className="d-flex justify-content-end mb-3">
                    <button className="btn btn-success btn-sm fw-bold" onClick={() => setModalStock({ visible: true })}>
                        + Nuevo Lote
                    </button>
                </div>

                {cargando ? (
                    <div className="text-center py-4"><div className="spinner-border text-info"></div></div>
                ) : (
                    <TablaGenerica
                        columns={columnas}
                        data={stocks}
                        actions={(stock) => (
                            <div className="btn-group">
                                <button className="btn btn-sm btn-outline-secondary" onClick={() => setStockAEditar(stock)}>
                                    <i className="bi bi-pencil"></i> Editar
                                </button>
                                <button className="btn btn-sm btn-outline-danger" onClick={() => handleEliminarStock(stock.stockId)}>
                                    <i className="bi bi-trash"></i> Eliminar
                                </button>
                            </div>
                        )}
                    />
                )}
                {modalStock.visible && (
                    <AgregarStock
                        inventarioId={bodega.id}
                        onCerrar={() => setModalStock({ visible: false })}
                        onGuardar={handleGuardarStock}
                    />
                )}
                {stockAEditar && (
                    <EditarStock
                        inventarioId={bodega.id}
                        stockActual={stockAEditar}
                        onCerrar={() => setStockAEditar(null)}
                        onGuardar={handleActualizarStock}
                    />
                )}
            </div>
        </div>
    );
};