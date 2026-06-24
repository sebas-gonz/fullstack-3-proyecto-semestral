import React, { useState, useEffect } from 'react';
import TablaGenerica from "../TablaGenerica.jsx";
import { MapaRuta } from "../MapaRuta.jsx";
import { ModalBase } from "../ModalBase.jsx";
import { useEnvios } from "../../hooks/useEnvio.js";
import { useUsuario } from "../../hooks/useUsuario.js";

export const AdminLogistica = () => {
    const [repartidores, setRepartidores] = useState([]);
    const [envioSeleccionado, setEnvioSeleccionado] = useState(null);
    const [repartidorSeleccionado, setRepartidorSeleccionado] = useState(null);
    const [mostrarModal, setMostrarModal] = useState(false);

    const { envios, listarEnviosDisponibles, tomarPedido } = useEnvios();
    const { obtenerRepartidores } = useUsuario();

    useEffect(() => {
        let isMounted = true;
        const init = async () => {
            await listarEnviosDisponibles();
            const dataRepartidores = await obtenerRepartidores(0);
            if (isMounted) {
                const repartidoresMapeados = (dataRepartidores?.content || []).map(r => ({
                    id: r.usuarioId,
                    nombreCompleto: `${r.nombre} ${r.apellido}`
                }));
                setRepartidores(repartidoresMapeados);
            }
        };
        init();
        return () => { isMounted = false; };
    }, []);

    return (
        <div className="container-fluid">
            <h5 className="text-primary fw-bold mb-4"><i className="bi bi-geo-alt-fill"></i> Logística de Envíos</h5>
            <div className="row">
                <div className="col-md-7">
                    <TablaGenerica
                        columns={[
                            { label: 'Pedido Asignado', key: 'pedidoIdCorto' },
                            { label: 'Origen', key: 'origenStr' },
                            { label: 'Destino', key: 'destinoStr' }
                        ]}
                        data={envios}
                        actions={(envio) => (
                            <div className="d-flex gap-2">
                                <button className={`btn btn-sm ${envioSeleccionado?.id === envio.id ? 'btn-dark' : 'btn-outline-dark'}`}
                                        onClick={() => setEnvioSeleccionado(envio)}>Ver Mapa</button>
                                <button className="btn btn-sm btn-primary"
                                        onClick={() => { setEnvioSeleccionado(envio); setMostrarModal(true); }}>Asignar</button>
                            </div>
                        )}
                    />
                </div>
                <div className="col-md-5">
                    <div className="card shadow-sm p-3 sticky-top" style={{ top: '20px' }}>
                        {envioSeleccionado ? (
                            <>
                                <h6 className="text-secondary">Ruta: {envioSeleccionado.origenStr} → {envioSeleccionado.destinoStr}</h6>
                                <MapaRuta
                                    origenCoords={envioSeleccionado.origenCoords}
                                    destinoCoords={envioSeleccionado.destinoCoords}
                                />
                            </>
                        ) : (
                            <div className="text-center py-5 text-muted">
                                <i className="bi bi-map fs-1"></i>
                                <p>Selecciona un envío para ver la ruta en el mapa.</p>
                            </div>
                        )}
                    </div>
                </div>
            </div>
            {mostrarModal && (
                <ModalBase titulo="Asignar Repartidor" onCerrar={() => setMostrarModal(false)}>
                    <select className="form-select mb-3" onChange={(e) => setRepartidorSeleccionado(e.target.value)}>
                        <option value="">Seleccione un repartidor...</option>
                        {repartidores.map(r => (
                            <option key={r.id} value={r.id}>{r.nombreCompleto}</option>
                        ))}
                    </select>
                    <button className="btn btn-primary w-100" onClick={async () => {
                        await tomarPedido(envioSeleccionado.id, repartidorSeleccionado);
                        setMostrarModal(false);
                        await listarEnviosDisponibles();
                    }}>Confirmar Asignación</button>
                </ModalBase>
            )}
        </div>
    );
};