import React, { useEffect, useState } from 'react';
import { ModalBase } from '../../ModalBase.jsx';
import { usePedidos } from '../../../hooks/usePedidos.js';
import { useGestionCatalogo } from '../../../hooks/useGestionCatalogo.js';

export const DetallePedido = ({ pedido, onCerrar }) => {
    const { obtenerDetallesPorPedido } = usePedidos();
    const { productosMaestros, listarCatalogoCompleto } = useGestionCatalogo();
    const [detalles, setDetalles] = useState([]);
    const [cargando, setCargando] = useState(true);

    useEffect(() => {
        listarCatalogoCompleto();

        const fetchDetalles = async () => {
            try {
                const data = await obtenerDetallesPorPedido(pedido.pedidoId);
                setDetalles(data);
            } catch (error) {
                console.error("Error cargando detalles del pedido", error);
            } finally {
                setCargando(false);
            }
        };
        fetchDetalles();
    }, [pedido.pedidoId]);
    const detallesMapeados = detalles.map(det => {
        const productoReal = productosMaestros.find(p => p.productoId === det.productoId);
        return {
            ...det,
            nombreProducto: productoReal ? productoReal.nombre : 'Cargando...',
            sku: productoReal ? productoReal.sku : 'N/A'
        };
    });

    return (
        <ModalBase titulo={`Detalle de Pedido: ${pedido.pedidoId.split('-')[0].toUpperCase()}`} onCerrar={onCerrar}>
            <div className="bg-light p-3 rounded mb-3 border">
                <div className="row">
                    <div className="col-md-6 mb-2">
                        <small className="text-muted d-block">Fecha de Compra:</small>
                        <strong>{new Date(pedido.fechaRegistro).toLocaleString()}</strong>
                    </div>
                    <div className="col-md-6 mb-2">
                        <small className="text-muted d-block">Estado Actual:</small>
                        <span className={`badge ${pedido.estado === 'PENDIENTE' ? 'bg-warning text-dark' : 'bg-success'}`}>
                            {pedido.estado}
                        </span>
                    </div>
                    <div className="col-12 mt-2">
                        <small className="text-muted d-block">Dirección de Envío:</small>
                        <strong>{pedido.destino.calle} {pedido.destino.numero}, {pedido.destino.ciudad} - {pedido.destino.pais}</strong>
                    </div>
                </div>
            </div>
            <h6 className="fw-bold mb-3 border-bottom pb-2">Artículos del Pedido</h6>
            {cargando ? (
                <div className="text-center py-3"><div className="spinner-border spinner-border-sm text-primary"></div></div>
            ) : (
                <ul className="list-group mb-3">
                    {detallesMapeados.map((item, index) => (
                        <li key={index} className="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <h6 className="my-0">[{item.sku}] {item.nombreProducto}</h6>
                                <small className="text-muted">Cantidad: {item.cantidad} x ${item.precio}</small>
                            </div>
                            <span className="text-muted fw-bold">${item.cantidad * item.precio}</span>
                        </li>
                    ))}
                </ul>
            )}
            <div className="d-flex justify-content-end align-items-center bg-light p-2 rounded border">
                <span className="me-3 fw-bold">Total Pagado:</span>
                <h4 className="mb-0 text-primary fw-bold">${pedido.total}</h4>
            </div>

            <div className="mt-4 d-flex justify-content-end">
                <button className="btn btn-secondary fw-bold" onClick={onCerrar}>Cerrar</button>
            </div>
        </ModalBase>
    );
};