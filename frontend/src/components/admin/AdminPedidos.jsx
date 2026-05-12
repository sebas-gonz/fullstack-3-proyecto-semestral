import React, {useEffect, useState} from 'react';
import TablaGenerica from "../TablaGenerica.jsx";
import { usePedidos } from '../../hooks/usePedidos';
import {DetallePedido} from "./modal/DetallePedido.jsx";

export const AdminPedidos = () => {
    const { pedidos, cargandoPedidos, listarTodosLosPedidos } = usePedidos();
    const [pedidoSeleccionado, setPedidoSeleccionado] = useState(null);
    useEffect(() => {
        listarTodosLosPedidos();
    }, [listarTodosLosPedidos]);
    const columnasPedidos = [
        { label: 'ID', key: 'idCorto' },
        { label: 'Fecha', key: 'fecha' },
        { label: 'Destino', key: 'destino' },
        { label: 'Artículos', key: 'items' },
        { label: 'Total', key: 'total' },
        { label: 'Estado', key: 'estado' }
    ];
    return (
        <div>
            <div className="d-flex justify-content-between mb-3">
                <h5>Historial Global de Pedidos</h5>
                <button className="btn btn-outline-secondary btn-sm" onClick={listarTodosLosPedidos}>
                    Actualizar
                </button>
            </div>
            {cargandoPedidos ? (
                <div className="text-center py-4">
                    <div className="spinner-border text-primary"></div>
                </div>
            ) : (
                <TablaGenerica
                    columns={columnasPedidos}
                    data={pedidos}
                    actions={(pedido) => (
                        <button
                            className="btn btn-sm btn-info text-white"
                            onClick={() => setPedidoSeleccionado(pedido.raw)}
                        >
                            Ver Detalle
                        </button>
                    )}
                />
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