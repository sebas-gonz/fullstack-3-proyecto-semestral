import React, { useState, useEffect } from 'react';
import { usePedidos } from '../../hooks/usePedidos';
import { useAuth } from '../../hooks/useAuth';
import { useUsuario } from '../../hooks/useUsuario';
import { DetallePedido } from '../admin/modal/DetallePedido.jsx'

export const MisPedidos = () => {
    const { idBackend } = useAuth();
    const { verificarUsuarioPorAuth0 } = useUsuario();
    const { obtenerPedidosPorUsuario, cargandoPedidos } = usePedidos();
    const [pedidoSeleccionado, setPedidoSeleccionado] = useState(null);
    const [pedidos, setPedidos] = useState([]);

    useEffect(() => {
        const cargarHistorial = async () => {
            if (!idBackend) return;
            try {
                const resUsuario = await verificarUsuarioPorAuth0(idBackend);
                const datosUsuario = resUsuario?.data ? resUsuario.data : resUsuario;
                const uuid = Array.isArray(datosUsuario) ? datosUsuario[0].usuarioId : datosUsuario.usuarioId;
                const historial = await obtenerPedidosPorUsuario(uuid);
                setPedidos(historial);
            } catch (error) {
                console.error("Error al cargar historial", error);
            }
        };
        cargarHistorial();
    }, [idBackend]);
    if (cargandoPedidos) return <div className="text-center py-5"><div className="spinner-border text-primary"></div></div>;
    return (
        <div className="container mt-4">
            <h3 className="mb-4 text-secondary"><i className="bi bi-bag-check-fill me-2"></i>Mis Pedidos</h3>

            {pedidos.length === 0 ? (
                <div className="alert alert-light border text-center py-5 shadow-sm">
                    <p className="mb-0 text-muted">Aún no has realizado ningún pedido.</p>
                </div>
            ) : (
                <div className="row">
                    {pedidos.content.map((pedido) => (
                        <div key={pedido.pedidoId} className="col-12 mb-3">
                            <div className="card shadow-sm border-start border-4 border-primary">
                                <div className="card-body">
                                    <div className="d-flex justify-content-between align-items-center mb-2">
                                        <h6 className="card-title mb-0">
                                            Pedido: <span className="text-muted small">{pedido.pedidoId.split('-')[0].toUpperCase()}</span>
                                        </h6>
                                        <span className={`badge ${pedido.estado === 'PENDIENTE' ? 'bg-warning text-dark' : 'bg-success'}`}>
                                            {pedido.estado}
                                        </span>
                                    </div>

                                    <div className="row small text-muted align-items-center">
                                        <div className="col-md-4 mb-2 mb-md-0">
                                            <strong>Fecha:</strong> {new Date(pedido.fechaRegistro).toLocaleDateString()}
                                        </div>
                                        <div className="col-md-4 mb-2 mb-md-0">
                                            <strong>Destino:</strong> {pedido.destino.calle} {pedido.destino.numero}, {pedido.destino.ciudad}
                                        </div>
                                        <div className="col-md-4 text-md-end d-flex justify-content-between justify-content-md-end align-items-center gap-3">
                                            <span className="h5 text-primary fw-bold mb-0">${pedido.total}</span>
                                            <button
                                                className="btn btn-outline-primary btn-sm fw-bold"
                                                onClick={() => setPedidoSeleccionado(pedido)}
                                            >
                                                <i className="bi bi-eye-fill me-1"></i> Ver Detalle
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
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