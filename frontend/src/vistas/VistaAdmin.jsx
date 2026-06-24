import React, { useState } from 'react';
import {AdminInventarios} from "../components/admin/AdminInventarios.jsx";
import {AdminCategorias} from "../components/admin/AdminCategorias.jsx";
import { AdminPedidos} from "../components/admin/AdminPedidos.jsx";
import {AdminLogistica} from "../components/admin/AdminLogistica.jsx";
import {AdminMonitoreoEnvios} from "../components/admin/AdminEnvios.jsx";

export const VistaAdmin = () => {
    const [tabActiva, setTabActiva] = useState('inventarios');

    return (
        <div className="container mt-4">
            <div className="card shadow-sm">
                <div className="card-header bg-dark text-white">
                    <h3 className="mb-0">Panel de Administración</h3>
                </div>
                <div className="card-body">
                    <ul className="nav nav-tabs mb-4">
                        <li className="nav-item">
                            <button className={`nav-link ${tabActiva === 'inventarios' ? 'active' : ''}`}
                                    onClick={() => setTabActiva('inventarios')}>Inventarios
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={`nav-link ${tabActiva === 'pedidos' ? 'active' : ''}`}
                                    onClick={() => setTabActiva('pedidos')}>Todos los pedidos
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={`nav-link ${tabActiva === 'productos' ? 'active' : ''}`}
                                    onClick={() => setTabActiva('productos')}>Catálogo de productos
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={`nav-link ${tabActiva === 'logistica' ? 'active' : ''}`}
                                    onClick={() => setTabActiva('logistica')}>Asignar Envios
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={`nav-link fw-bold ${tabActiva === 'monitoreo' ? 'active' : 'text-dark'}`}
                                    onClick={() => setTabActiva('monitoreo')}>
                                <i className="bi bi-globe"></i> Envíos
                            </button>
                        </li>
                    </ul>
                    {tabActiva === 'inventarios' && <AdminInventarios />}
                    {tabActiva === 'pedidos' && <AdminPedidos />}
                    {tabActiva === 'productos' && <AdminCategorias/>}
                    {tabActiva === 'logistica' && <AdminLogistica />}
                    {tabActiva === 'monitoreo' && <AdminMonitoreoEnvios />}
                </div>
            </div>
        </div>
    );
};