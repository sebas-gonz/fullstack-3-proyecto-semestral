import React, { useEffect, useState } from 'react';
import TablaGenerica from "../TablaGenerica.jsx";
import { useEnvios } from "../../hooks/useEnvio.js";
import { ModalBase } from "../ModalBase.jsx";
import { MapaRuta } from "../MapaRuta.jsx";

export const AdminMonitoreoEnvios = () => {
    const {
        enviosAdmin,
        cargando,
        listarTodosLosEnviosAdmin,
        obtenerEstadosPosiblesDeEnvio,
        obtenerEnviosPorEstadoAdmin
    } = useEnvios();

    const [estadosDisponibles, setEstadosDisponibles] = useState([]);
    const [estadoFiltro, setEstadoFiltro] = useState('');
    const [envioMapeadoSeleccionado, setEnvioMapeadoSeleccionado] = useState(null);

    useEffect(() => {
        let isMounted = true;
        const inicializar = async () => {
            await listarTodosLosEnviosAdmin();
            const estados = await obtenerEstadosPosiblesDeEnvio();
            if (isMounted && estados) {
                setEstadosDisponibles(estados);
            }
        };
        inicializar();
        return () => { isMounted = false; };
    }, [listarTodosLosEnviosAdmin, obtenerEstadosPosiblesDeEnvio]);

    const handleFiltroChange = (e) => {
        const estadoSeleccionado = e.target.value;
        setEstadoFiltro(estadoSeleccionado);

        if (estadoSeleccionado) {
            obtenerEnviosPorEstadoAdmin(estadoSeleccionado);
        } else {
            listarTodosLosEnviosAdmin();
        }
    };
    const handleLimpiarFiltros = () => {
        setEstadoFiltro('');
        listarTodosLosEnviosAdmin();
    };
    const columnasMonitoreo = [
        { label: 'ID Envío', key: 'idCorto' },
        { label: 'Fecha', key: 'fechaRegistro' },
        { label: 'Comprador', key: 'nombreComprador' },
        { label: 'Repartidor', key: 'nombreRepartidor' },
        { label: 'Total', key: 'total' },
        { label: 'Estado', key: 'estado' }
    ];

    return (
        <div className="container-fluid pt-2">
            <div className="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
                <h5 className="text-primary fw-bold mb-0">
                    <i className="bi bi-eye"></i> Todos los Envios
                </h5>
                <div className="d-flex gap-2 align-items-center flex-wrap">
                    <span className="text-muted fw-bold me-1">Estado:</span>
                    <select
                        className="form-select form-select-sm shadow-sm"
                        style={{ maxWidth: '200px' }}
                        value={estadoFiltro}
                        onChange={handleFiltroChange}
                    >
                        <option value="">Todos los estados</option>
                        {estadosDisponibles.map(estado => (
                            <option key={estado} value={estado}>{estado}</option>
                        ))}
                    </select>

                    <button
                        className="btn btn-outline-secondary btn-sm d-flex align-items-center gap-1 shadow-sm"
                        onClick={handleLimpiarFiltros}
                        title="Quitar filtros y actualizar"
                    >
                        <i className="bi bi-arrow-clockwise"></i> Refrescar
                    </button>
                </div>
            </div>

            {cargando ? (
                <div className="text-center py-5">
                    <div className="spinner-border text-primary" role="status"></div>
                    <p className="mt-2 text-muted fw-bold">Sincronizando...</p>
                </div>
            ) : (
                <div className="card shadow-sm border-0">
                    <div className="card-body p-0">
                        <TablaGenerica
                            columns={columnasMonitoreo}
                            data={enviosAdmin}
                            actions={(envio) => (
                                <button
                                    className="btn btn-sm btn-success text-white fw-bold d-flex align-items-center gap-1 mx-auto"
                                    onClick={() => setEnvioMapeadoSeleccionado(envio)}
                                >
                                    <i className="bi bi-map-fill"></i> Trazabilidad
                                </button>
                            )}
                        />
                    </div>
                </div>
            )}
            {envioMapeadoSeleccionado && (
                <ModalBase
                    titulo={`Trazabilidad - Envío: ${envioMapeadoSeleccionado.idCorto}`}
                    onCerrar={() => setEnvioMapeadoSeleccionado(null)}
                >
                    <div className="row mb-3 text-center">
                        <div className="col-6 border-end">
                            <small className="text-muted d-block text-uppercase">Comprador</small>
                            <span className="fw-bold">{envioMapeadoSeleccionado.nombreComprador}</span>
                        </div>
                        <div className="col-6">
                            <small className="text-muted d-block text-uppercase">Repartidor Asignado</small>
                            <span className="fw-bold text-primary">{envioMapeadoSeleccionado.nombreRepartidor}</span>
                        </div>
                    </div>

                    <div className="bg-light p-2 rounded mb-3 text-center border">
                        <span className={`badge ${envioMapeadoSeleccionado.estado === 'PENDIENTE' ? 'bg-warning text-dark' : 'bg-success'} fs-6`}>
                            ESTADO: {envioMapeadoSeleccionado.estado}
                        </span>
                    </div>

                    <div style={{ height: '350px' }} className="rounded overflow-hidden border">
                        <MapaRuta
                            origenCoords={envioMapeadoSeleccionado.origenCoords}
                            destinoCoords={envioMapeadoSeleccionado.destinoCoords}
                        />
                    </div>
                </ModalBase>
            )}
        </div>
    );
};