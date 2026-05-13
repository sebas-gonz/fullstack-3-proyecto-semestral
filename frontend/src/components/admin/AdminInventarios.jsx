import React, { useState, useEffect } from 'react';
import TablaGenerica from "../TablaGenerica.jsx";
import { CrearInventario} from "../CrearInventario.jsx";
import { useInventarios } from '../../hooks/useInventarios';
import {AdminStocks} from "./AdminStocks.jsx";

export const AdminInventarios = () => {

    const [mostrarModalInv, setMostrarModalInv] = useState(false);
    const [bodegaVistaDetalle, setBodegaVistaDetalle] = useState(null);
    const [bodegaAEditar, setBodegaAEditar] = useState(null);
    const { inventarios, listarInventarios, crearInventario,actualizarInventario } = useInventarios();
    useEffect(() => {
        listarInventarios();
    }, [listarInventarios]);

    const columnasInventario = [
        { label: 'Nombre Bodega', key: 'nombre' },
        { label: 'Ubicación', key: 'ubicacion' },
        { label: 'Stock Total', key: 'stockTotal' }
    ];
    const handleGuardarInventario = async (datos) => {
        try {
            if (bodegaAEditar) {
                await actualizarInventario(bodegaAEditar.id, datos);
                alert("Bodega actualizada exitosamente.");
            } else {
                await crearInventario(datos);
                alert("Bodega creada exitosamente.");
            }
            setMostrarModalInv(false);
            setBodegaAEditar(null);
            listarInventarios();
        } catch (error) {
            console.log(error);
        }
    };
    const abrirModalEdicion = (inv) => {
        setBodegaAEditar(inv);
        setMostrarModalInv(true);
    };
    if (bodegaVistaDetalle) {
        return (
            <AdminStocks
                bodega={bodegaVistaDetalle}
                onVolver={() => {
                    setBodegaVistaDetalle(null);
                    listarInventarios();
                }}
            />
        );
    }
    return (
        <div>
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h5 className="mb-0 text-primary fw-bold"><i className="bi bi-building me-2"></i>Inventarios y Bodegas</h5>
                <button className="btn btn-outline-secondary btn-sm" onClick={listarInventarios}>
                    <i className="bi bi-arrow-clockwise me-1"></i> Refrescar
                </button>
            </div>

            <TablaGenerica
                columns={columnasInventario}
                data={inventarios}
                actions={(inv) => (
                    <div className="btn-group">
                        <button className="btn btn-sm btn-info text-white fw-bold" onClick={() => setBodegaVistaDetalle(inv)}>
                            <i className="bi bi-list-check"></i> Ver Lotes
                        </button>
                        <button className="btn btn-sm btn-outline-secondary" onClick={() => abrirModalEdicion(inv)}>
                            <i className="bi bi-pencil"></i> Editar
                        </button>
                    </div>
                )}
            />

            <hr className="my-4" />

            <div className="d-flex justify-content-between align-items-center mb-3">
                <h5 className="mb-0 text-secondary">Gestión de Instalaciones</h5>
                <button className="btn btn-primary btn-sm fw-bold shadow-sm" onClick={() => {
                    setBodegaAEditar(null);
                    setMostrarModalInv(true);
                }}>
                    <i className="bi bi-plus-lg me-1"></i> Nueva Bodega
                </button>
            </div>

            {mostrarModalInv && (
                <CrearInventario
                    inventarioInicial={bodegaAEditar}
                    onCerrar={() => {
                        setMostrarModalInv(false);
                        setBodegaAEditar(null);
                    }}
                    onGuardar={handleGuardarInventario}
                />
            )}
        </div>
    );
};