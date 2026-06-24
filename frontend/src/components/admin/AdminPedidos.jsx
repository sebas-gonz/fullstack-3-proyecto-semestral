import React, {useEffect, useState} from 'react';
import TablaGenerica from "../TablaGenerica.jsx";
import { usePedidos } from '../../hooks/usePedidos';
import {DetallePedido} from "./modal/DetallePedido.jsx";

export const AdminPedidos = () => {
    const {
        pedidos,
        cargandoPedidos,
        listarTodosLosPedidos,
        obtenerEstadosPedidos,
        obtenerPedidosPorEstado,
        buscarPedidos,
    } = usePedidos();
    const [pedidoSeleccionado, setPedidoSeleccionado] = useState(null);
    const [estadosDisponibles, setEstadosDisponibles] = useState([]);
    const [estadoFiltro, setEstadoFiltro] = useState('');
    const [textoBusqueda, setTextoBusqueda] = useState('');

    useEffect(() => {
        listarTodosLosPedidos();
        const cargarEstados = async () => {
            try {
                const data = await obtenerEstadosPedidos();
                setEstadosDisponibles(data);
            } catch (error) {
                console.log("No se pudieron cargar los estados");
            }
        };
        cargarEstados();
    }, [listarTodosLosPedidos]);

    const handleFiltroChange = (e) => {
        const estadoSeleccionado = e.target.value;
        setEstadoFiltro(estadoSeleccionado);
        setTextoBusqueda('');

        if (estadoSeleccionado) {
            obtenerPedidosPorEstado(estadoSeleccionado);
        } else {
            listarTodosLosPedidos();
        }
    };

    const handleBuscarTexto = (e) => {
        e.preventDefault();
        if (textoBusqueda.trim() !== '') {
            setEstadoFiltro('');
            buscarPedidos(textoBusqueda);
        } else {
            listarTodosLosPedidos();
        }
    };
    const handleLimpiarFiltros = () => {
        setTextoBusqueda('');
        setEstadoFiltro('');
        listarTodosLosPedidos();
    };

    const columnasPedidos = [
        { label: 'ID', key: 'idCorto' },
        { label: 'Fecha', key: 'fecha' },
        { label: 'Origen', key: 'origen' },
        { label: 'Destino', key: 'destino' },
        { label: 'Envio', key: 'costoEnvio' },
        { label: 'Artículos', key: 'items' },
        { label: 'Total', key: 'total' },
        { label: 'Estado', key: 'estado' }
    ];
    return (
        <div>
            <div className="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
                <h5 className="mb-0">Historial Global de Pedidos</h5>

                <div className="d-flex gap-2 align-items-center flex-wrap">
                    <form onSubmit={handleBuscarTexto} className="d-flex gap-1">
                        <input
                            type="text"
                            className="form-control form-control-sm"
                            placeholder="Buscar ID, ciudad o calle..."
                            value={textoBusqueda}
                            onChange={(e) => setTextoBusqueda(e.target.value)}
                            style={{ minWidth: '220px' }}
                        />
                        <button type="submit" className="btn btn-primary btn-sm" title="Buscar">
                            <i className="bi bi-search"></i>
                        </button>
                    </form>
                    <select
                        className="form-select form-select-sm"
                        style={{ maxWidth: '180px' }}
                        value={estadoFiltro}
                        onChange={handleFiltroChange}
                    >
                        <option value="">Todos los estados</option>
                        {estadosDisponibles.map(estado => (
                            <option key={estado} value={estado}>{estado}</option>
                        ))}
                    </select>
                    <button
                        className="btn btn-outline-secondary btn-sm"
                        onClick={handleLimpiarFiltros}
                        title="Limpiar filtros y actualizar tabla"
                    >
                        <i className="bi bi-arrow-clockwise"></i>
                    </button>
                </div>
            </div>

            {cargandoPedidos ? (
                <div className="text-center py-5">
                    <div className="spinner-border text-primary"></div>
                    <p className="mt-2 text-muted">Buscando pedidos...</p>
                </div>
            ) : (
                <div className="card shadow-sm border-0">
                    <div className="card-body p-0">
                        <TablaGenerica
                            columns={columnasPedidos}
                            data={pedidos}
                            actions={(pedido) => (
                                <button
                                    className="btn btn-sm btn-info text-white fw-bold"
                                    onClick={() => setPedidoSeleccionado(pedido.raw)}
                                >
                                    Ver Detalle
                                </button>
                            )}
                        />
                    </div>
                </div>
            )}
            {pedidoSeleccionado && (
                <DetallePedido
                    pedido={pedidoSeleccionado}
                    onCerrar={() => setPedidoSeleccionado(null)}
                />
            )}
        </div>
    );
};