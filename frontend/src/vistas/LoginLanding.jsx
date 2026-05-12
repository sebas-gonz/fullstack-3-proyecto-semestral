import React from 'react';
import { useAuth } from '../hooks/useAuth.js';

export const LoginLanding = () => {
    const { iniciarSesion } = useAuth();

    return (
        <div className="d-flex flex-column justify-content-center align-items-center vh-100 bg-light">
            <div className="card shadow-lg p-5 text-center" style={{ maxWidth: '500px' }}>
                <h1 className="mb-4 text-primary fw-bold">Inventario </h1>
                <p className="lead mb-4">Identifícate para acceder al sistema</p>
                <button className="btn btn-primary btn-lg px-5 shadow-sm" onClick={iniciarSesion}>
                    Iniciar Sesión
                </button>
            </div>
        </div>
    );
};