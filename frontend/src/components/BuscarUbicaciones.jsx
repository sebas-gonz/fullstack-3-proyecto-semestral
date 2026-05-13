import React from 'react';
import {useBuscarUbicaciones} from "../hooks/useBuscarUbicaciones.js";

export const BuscarUbicaciones = ({ onUbicacionConfirmada }) => {
    const {
        busqueda, setBusqueda, resultados, buscando,
        buscarUbicaciones, obtenerUbicacion, limpiarBuscador
    } = useBuscarUbicaciones();
    const handleSeleccionar = (lugar) => {
        const ubicacion = obtenerUbicacion(lugar);
        onUbicacionConfirmada(ubicacion);
        limpiarBuscador();
    };

    return (
        <div className="buscador-direccion-container">
            <div className="input-group mb-2">
                <input type="text" className="form-control"
                       placeholder="Ej: Avenida St 123"
                       value={busqueda}
                       onChange={e => setBusqueda(e.target.value)}
                       onKeyDown={e => e.key === 'Enter' && buscarUbicaciones()} />
                <button className="btn btn-outline-primary" onClick={buscarUbicaciones} disabled={buscando}>
                    {buscando ? '...' : 'Buscar'}
                </button>
            </div>
            {resultados.length > 0 && (
                <div className="list-group shadow-sm mb-3" style={{ maxHeight: '200px', overflowY: 'auto' }}>
                    {resultados.map((lugar, index) => (
                        <button
                            key={index}
                            type="button"
                            className="list-group-item list-group-item-action text-start p-2"
                            onClick={() => handleSeleccionar(lugar)}
                        >
                            <strong className="d-block text-primary small">{lugar.displayName.text}</strong>
                            <span className="text-muted" style={{ fontSize: '0.8rem' }}>{lugar.formattedAddress}</span>
                        </button>
                    ))}
                </div>
            )}
        </div>
    );
};