import React, { useEffect, useState } from 'react';
import { ModalBase } from '../../ModalBase.jsx';
import { usePedidos } from '../../../hooks/usePedidos.js';
import { useGestionCatalogo } from '../../../hooks/useGestionCatalogo.js';
import { MapaRuta} from "../../MapaRuta.jsx";

export const DetallePedido = ({ pedido, onCerrar }) => {
    const { obtenerDetallesPorPedido } = usePedidos();
    const { productosMaestros, listarCatalogoCompleto } = useGestionCatalogo();
    const [detalles, setDetalles] = useState([]);
    const [cargando, setCargando] = useState(true);

    const [distanciaRuta, setDistanciaRuta] = useState('');

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

    const subtotalProductos = detallesMapeados.reduce((acc, item) => acc + (item.cantidad * item.precio), 0);
    const origenCoords = pedido.origen ? { lat: pedido.origen.latitude, lng: pedido.origen.longitude } : null;
    const destinoCoords = pedido.destino ? { lat: pedido.destino.latitude, lng: pedido.destino.longitude } : null;

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

            {origenCoords && destinoCoords && (
                <div className="mb-4 border rounded overflow-hidden shadow-sm">
                    <div className="bg-secondary text-white p-2 text-center fw-bold d-flex justify-content-between align-items-center px-3">
                        <span><i className="bi bi-map-fill me-2"></i>Ruta del Envío</span>
                        {distanciaRuta && <span className="badge bg-light text-dark">{distanciaRuta}</span>}
                    </div>

                    <MapaRuta
                        origenCoords={origenCoords}
                        destinoCoords={destinoCoords}
                        onDistanciaCalculada={setDistanciaRuta}
                    />
                </div>
            )}

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

            <div className="bg-light p-3 rounded border">
                <div className="d-flex justify-content-between mb-1">
                    <span className="text-muted small">Subtotal Productos:</span>
                    <span className="fw-bold text-secondary">${subtotalProductos}</span>
                </div>
                <div className="d-flex justify-content-between mb-1">
                    <span className="text-muted small">Distancia:</span>
                    <span className="fw-bold text-secondary">{distanciaRuta || 'Calculando...'}</span>
                </div>
                <div className="d-flex justify-content-between mb-1">
                    <span className="text-muted small">Costo de Envío:</span>
                    <span className="fw-bold text-secondary">${pedido.costoEnvio || 0}</span>
                </div>
                <hr className="my-2"/>
                <div className="d-flex justify-content-between align-items-center">
                    <span className="fw-bold text-dark">Total Transacción:</span>
                    <h4 className="mb-0 text-primary fw-bold">${pedido.total}</h4>
                </div>
            </div>

            <div className="mt-4 d-flex justify-content-end">
                <button className="btn btn-secondary fw-bold" onClick={onCerrar}>Cerrar</button>
            </div>
        </ModalBase>
    );
};