import React, { useEffect } from 'react';
import TablaGenerica from "../components/TablaGenerica.jsx";
import { useInventarios } from '../hooks/useInventarios';

export const VistaInventariosAdmin = () => {
    const { inventarios, cargando, error, listarInventarios } = useInventarios();

    useEffect(() => {
        listarInventarios();
    }, [listarInventarios]);

    const columnasInventario = [
        { label: 'ID Inventario', key: 'id' },
        { label: 'Nombre de Bodega', key: 'nombre' },
        { label: 'Ubicación', key: 'ubicacion' },
        { label: 'Stock Total (Unidades)', key: 'stockTotal' }
    ];
    if (cargando) {
        return (
            <div className="text-center mt-5">
                <div className="spinner-border text-primary" role="status"></div>
                <p className="mt-2 text-muted">Cargando inventarios...</p>
            </div>
        );
    }
    if (error) {
        return <div className="alert alert-danger mt-4">{error}</div>;
    }
    return (
        <div className="container mt-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2 className="text-primary fw-bold">Gestión de Inventarios</h2>
                <button className="btn btn-primary shadow-sm">
                    + Nuevo Inventario
                </button>
            </div>
            <TablaGenerica
                columns={columnasInventario}
                data={inventarios}
                actions={(item) => (
                    <div className="d-flex gap-2 justify-content-center">
                        <button
                            className="btn btn-sm btn-outline-info"
                            onClick={() => console.log("Ver lotes de:", item.id)}
                        >
                            Ver Lotes
                        </button>
                        <button
                            className="btn btn-sm btn-outline-success"
                            onClick={() => console.log("Añadir producto a:", item.id)}
                        >
                            Ingresar Stock
                        </button>
                    </div>
                )}
            />
        </div>
    );
};