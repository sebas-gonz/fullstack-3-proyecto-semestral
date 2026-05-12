import React, { useState } from 'react';
import {AdminInventarios} from "../components/admin/AdminInventarios.jsx";
import {AdminCategorias} from "../components/admin/AdminCategorias.jsx";
import { AdminPedidos} from "../components/admin/AdminPedidos.jsx";

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
                                    onClick={() => setTabActiva('inventarios')}>Gestión Inventarios
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={`nav-link ${tabActiva === 'pedidos' ? 'active' : ''}`}
                                    onClick={() => setTabActiva('pedidos')}>Todos los Pedidos
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={`nav-link ${tabActiva === 'productos' ? 'active' : ''}`}
                                    onClick={() => setTabActiva('productos')}>Catálogo Maestro
                            </button>
                        </li>
                    </ul>
                    {tabActiva === 'inventarios' && <AdminInventarios />}
                    {tabActiva === 'pedidos' && <AdminPedidos />}
                    {tabActiva === 'productos' && <AdminCategorias/>}
                </div>
            </div>
        </div>
    );
};