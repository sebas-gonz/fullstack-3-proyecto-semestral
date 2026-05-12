import React, { useEffect, useState } from 'react';
import TablaGenerica from "../TablaGenerica.jsx";
import { AgregarStock } from "./modal/AgregarStock.jsx";
import {EditarStock} from "./modal/EditarStock.jsx";
import { useStocks } from '../../hooks/useStock.js';
import { useGestionCatalogo } from '../../hooks/useGestionCatalogo.js';

export const AdminStocks = ({ bodega, onVolver }) => {
    const { stocks, listarStocks, crearStock, actualizarStock, eliminarStock } = useStocks();
    const { productosMaestros, listarCatalogoCompleto } = useGestionCatalogo();

    const [modalStock, setModalStock] = useState({ visible: false });
    const [stockAEditar, setStockAEditar] = useState(null);
    useEffect(() => {
        listarStocks(bodega.id);
        listarCatalogoCompleto();
    }, [listarStocks, listarCatalogoCompleto, bodega.id]);

    const stocksMapeados = stocks.map(st => {
        const prod = productosMaestros.find(p => p.productoId === st.productoId);
        return {
            ...st,
            productoNombre: prod ? prod.nombre : 'Cargando...',
            fechaFormateada: new Date(st.fechaRegistroLote).toLocaleDateString()
        };
    });

    const columnas = [
        { label: 'Lote', key: 'lote' },
        { label: 'Producto', key: 'productoNombre' },
        { label: 'Cantidad', key: 'cantidad' },
        { label: 'Fecha Lote', key: 'fechaFormateada' }
    ];
    const handleGuardarStock = async (inventarioId, stockRequest) => {
        try {
            await crearStock(inventarioId, stockRequest);
            setModalStock({ visible: false });
            listarStocks(bodega.id);
        } catch (error) {
            console.error(error);
        }
    };
    const handleActualizarStock = async (inventarioId, stockId, stockRequest) => {
        try {
            await actualizarStock(inventarioId, stockId, stockRequest);
            setStockAEditar(null);
            alert("¡Lote modificado correctamente!");
            listarStocks(bodega.id);
        } catch (error) {
            console.error(error);
        }
    };

    const handleEliminarStock = async (stockId) => {
        if(window.confirm("¿Seguro que deseas eliminar este lote? Esto impactará el catálogo general.")) {
            await eliminarStock(bodega.id, stockId);
            listarStocks(bodega.id);
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

                <TablaGenerica
                    columns={columnas}
                    data={stocksMapeados}
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