import React, { useState } from 'react';
import { BuscarUbicaciones } from "./BuscarUbicaciones.jsx";
import { ModalBase } from "./ModalBase.jsx";

export const CrearInventario = ({ onCerrar, onGuardar }) => {
    const [nombre, setNombre] = useState('');
    const [ubicacionSeleccionada, setUbicacionSeleccionada] = useState(null);

    const handleSubmit = () => {
        if (!nombre.trim()) {
            return alert("Ingresa un nombre para la bodega");
        }
        if (!ubicacionSeleccionada) {
            return alert("Se necesita una ubicacion para la bodega");
        }
        onGuardar({ nombre, ubicacion: ubicacionSeleccionada });
    };
 
    return (
        <ModalBase titulo="Registrar Nueva Bodega" onCerrar={onCerrar}>
            <div className="mb-3">
                <label className="form-label fw-bold">Nombre del Inventario</label>
                <input type="text" className="form-control"
                       value={nombre} onChange={e => setNombre(e.target.value)} />
            </div>
            <div className="mb-3">
                <label className="form-label fw-bold">Ubicación</label>
                <BuscarUbicaciones
                    onUbicacionConfirmada={(ubicacion) => setUbicacionSeleccionada(ubicacion)}
                />
                {ubicacionSeleccionada && (
                    <div className="alert alert-success p-2 small mt-2 mb-0 shadow-sm">
                        ✅ {ubicacionSeleccionada.direccionFormateada}
                    </div>
                )}
            </div>
            <div className="mt-4 d-flex justify-content-end gap-2">
                <button className="btn btn-secondary" onClick={onCerrar}>Cancelar</button>
                <button className="btn btn-primary fw-bold" onClick={handleSubmit}>Guardar</button>
            </div>
        </ModalBase>
    );
};