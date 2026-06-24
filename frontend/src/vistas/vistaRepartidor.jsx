import React, { useEffect, useState } from 'react';
import TablaGenerica from "../components/TablaGenerica.jsx";
import { useEnvios } from "../hooks/useEnvio.js";
import { useAuth } from "../hooks/useAuth.js";
import { useUsuario } from "../hooks/useUsuario.js";
import { MapaRuta } from "../components/MapaRuta.jsx";

export const VistaRepartidor = () => {
    const { idBackend } = useAuth();
    const { verificarUsuarioPorAuth0 } = useUsuario();
    const {
        envios, cargando, tomarPedido, entregarPedido,
        listarEnviosDisponibles, listarEnviosEnRuta, listarEnviosEntregados
    } = useEnvios();

    const [tabActiva, setTabActiva] = useState('disponibles');
    const [envioSeleccionado, setEnvioSeleccionado] = useState(null);
    const [repartidorId, setRepartidorId] = useState(null);
    useEffect(() => {
        const cargarRepartidor = async () => {
            if (!idBackend) return;
            const respuesta = await verificarUsuarioPorAuth0(idBackend);
            const datos = respuesta?.data || respuesta;
            setRepartidorId(Array.isArray(datos) ? datos[0].usuarioId : datos.usuarioId);
        };
        cargarRepartidor();
    }, [idBackend, verificarUsuarioPorAuth0]);

    useEffect(() => {
        if (!repartidorId) return;
        setEnvioSeleccionado(null);

        if (tabActiva === 'en_ruta') listarEnviosEnRuta(repartidorId);
        else if (tabActiva === 'historial') listarEnviosEntregados(repartidorId);
    }, [tabActiva, repartidorId, listarEnviosDisponibles, listarEnviosEnRuta, listarEnviosEntregados]);

    const handleAccionPrincipal = async () => {
        try {
             if (tabActiva === 'en_ruta') {
                await entregarPedido(envioSeleccionado.id);
                alert("Entrega confirmada.");
                setTabActiva('historial');
            }
        } catch (err) { alert("Error al procesar el envío"); }
    };

    return (
        <div className="container mt-4">
            <h2 className="text-warning fw-bold mb-4"><i className="bi bi-truck me-2"></i>Gestión de Logística</h2>
            <ul className="nav nav-pills mb-4 bg-light p-2 rounded shadow-sm">
                <li className="nav-item">
                    <button className={`nav-link ${tabActiva === 'en_ruta' ? 'active bg-primary' : 'text-muted'}`}
                            onClick={() => setTabActiva('en_ruta')}>🛣️ Mis Rutas</button>
                </li>
                <li className="nav-item">
                    <button className={`nav-link ${tabActiva === 'historial' ? 'active bg-success' : 'text-muted'}`}
                            onClick={() => setTabActiva('historial')}>✅ Entregados</button>
                </li>
            </ul>

            <div className="row">
                <div className="col-md-7">
                    <div className="card shadow-sm p-3">
                        {cargando ? (
                            <div className="text-center py-5"><div className="spinner-border text-warning"></div></div>
                        ) : (
                            <TablaGenerica
                                columns={[
                                    { label: 'ID', key: 'id' },
                                    { label: 'Origen', key: 'origenStr' },
                                    { label: 'Destino', key: 'destinoStr' },
                                    { label: 'Estado', key: 'estado' }
                                ]}
                                data={envios}
                                actions={(envio) => (
                                    <button className={`btn btn-sm ${envioSeleccionado?.id === envio.id ? 'btn-dark' : 'btn-outline-dark'}`}
                                            onClick={() => setEnvioSeleccionado(envio)}>
                                        Ver Mapa
                                    </button>
                                )}
                            />
                        )}
                    </div>
                </div>
                <div className="col-md-5">
                    <div className="card shadow-sm p-3 sticky-top" style={{ top: '20px' }}>
                        {envioSeleccionado ? (
                            <>
                                <h5 className="text-secondary mb-3">Ruta de Entrega</h5>
                                <MapaRuta
                                    origenCoords={envioSeleccionado.origenCoords}
                                    destinoCoords={envioSeleccionado.destinoCoords}
                                />
                                {tabActiva !== 'historial' && (
                                    <button
                                        className={`btn w-100 mt-3 fw-bold py-2 ${tabActiva === 'disponibles' ? 'btn-warning' : 'btn-success'}`}
                                        onClick={handleAccionPrincipal}
                                    >
                                        {tabActiva === 'disponibles' ? '🚀 Tomar este Pedido' : 'Confirmar Entrega'}
                                    </button>
                                )}
                            </>
                        ) : (
                            <div className="text-center py-5 text-muted">
                                <i className="bi bi-map fs-1"></i>
                                <p>Selecciona un envío para trazar la ruta en tiempo real.</p>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};