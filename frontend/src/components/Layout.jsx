import React from 'react';
import { useAuth } from '../hooks/useAuth.js';

export const Layout = ({ children }) => {
    const { cerrarSesion, usuario } = useAuth();
    return (
        <div className="d-flex bg-light min-vh-100">
            <div className="bg-dark text-white p-3 shadow-sm d-flex flex-column" style={{ width: '250px' }}>
                <h4 className="fw-bold text-center mt-2 text-uppercase tracking-wide">Inventario System</h4>
                <div className="text-center text-secondary small mb-3">System</div>
                <hr className="border-secondary mt-0" />
                <ul className="nav nav-pills flex-column mt-2 flex-grow-1">
                    <li className="nav-item mb-2">
                        <span className="nav-link text-white" style={{ cursor: 'pointer' }}>
                             Inicio
                        </span>
                    </li>
                    <li className="nav-item">
                        <span className="nav-link text-white text-muted" style={{ cursor: 'pointer' }}>
                             Ajustes
                        </span>
                    </li>
                </ul>
                <hr className="border-secondary mb-3" />
                <div className="mt-auto">
                    {usuario?.name && (
                        <div className="text-center mb-3 small text-light">
                             {usuario.name}
                        </div>
                    )}
                    <button
                        onClick={cerrarSesion}
                        className="btn btn-outline-danger w-100 fw-bold d-flex align-items-center justify-content-center gap-2"
                    >
                        <span>🚪</span> Cerrar Sesión
                    </button>
                </div>
            </div>

            <div className="w-100 p-4 overflow-auto">
                {children}
            </div>
        </div>
    );
};