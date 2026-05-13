import React, { useState } from 'react';
import { ClientePerfil } from '../components/cliente/ClientePerfil.jsx';
import { ClienteCatalogo } from '../components/cliente/ClienteCatalogo.jsx';
import {MisPedidos} from "../components/Cliente/MisPedidos.jsx";

export const VistaCliente = () => {
    const [tabActiva, setTabActiva] = useState('perfil');
    return (
        <div className="container mt-4">
            <div className="card shadow-sm border-0">
                <div className="card-header bg-primary text-white">
                    <h3 className="mb-0">Mi Portal</h3>
                </div>
                <div className="card-body">
                    <ul className="nav nav-tabs mb-4">
                        <li className="nav-item">
                            <button className={`nav-link fw-bold ${tabActiva === 'perfil' ? 'active text-primary' : 'text-secondary'}`}
                                    onClick={() => setTabActiva('perfil')}>
                                 Mi Perfil y Direcciones
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={`nav-link fw-bold ${tabActiva === 'catalogo' ? 'active text-primary' : 'text-secondary'}`}
                                    onClick={() => setTabActiva('catalogo')}>
                                 Comprar
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={`nav-link fw-bold ${tabActiva === 'pedido' ? 'active text-primary' : 'text-secondary'}`}
                                    onClick={() => setTabActiva('pedido')}>
                                Mis pedidos
                            </button>
                        </li>
                    </ul>
                    {tabActiva === 'perfil' && <ClientePerfil />}
                    {tabActiva === 'catalogo' && <ClienteCatalogo />}
                    {tabActiva === 'pedido' && <MisPedidos/>}
                </div>
            </div>
        </div>
    );
};