import React, { useState, useEffect } from 'react';
import { ModalBase} from "../../ModalBase.jsx";
import { useGestionCatalogo } from '../../../hooks/useGestionCatalogo.js';

export const EditarStock = ({ inventarioId, stockActual, onCerrar, onGuardar }) => {
    const { productosMaestros, listarCatalogoCompleto } = useGestionCatalogo();

    const [productoId, setProductoId] = useState(stockActual.productoId || '');
    const [lote, setLote] = useState(stockActual.lote || '');
    const [cantidad, setCantidad] = useState(stockActual.cantidad !== undefined ? stockActual.cantidad : '');
    const formatearFechaParaInput = (isoString) => {
        if (!isoString) return '';
        const fecha = new Date(isoString);
        fecha.setMinutes(fecha.getMinutes() - fecha.getTimezoneOffset());
        return fecha.toISOString().slice(0, 16);
    };
    const [fechaRegistroLote, setFechaRegistroLote] = useState(formatearFechaParaInput(stockActual.fechaRegistroLote));

    useEffect(() => {
        listarCatalogoCompleto();
    }, [listarCatalogoCompleto]);

    const handleSubmit = () => {
        if (!productoId) return alert("Debes seleccionar un producto.");
        if (!lote.trim()) return alert("Debes ingresar el código del lote.");
        if (cantidad === '' || parseInt(cantidad) < 0) return alert("Debes ingresar una cantidad válida.");
        if (!fechaRegistroLote) return alert("Debes ingresar la fecha de registro del lote.");

        const fechaFormateada = new Date(fechaRegistroLote).toISOString();
        onGuardar(inventarioId, stockActual.stockId, {
            productoId: productoId,
            lote: lote.trim(),
            cantidad: parseInt(cantidad),
            fechaRegistroLote: fechaFormateada
        });
    };

    return (
        <ModalBase titulo="Editar Lote (Stock)" onCerrar={onCerrar}>
            <div className="mb-3">
                <label className="form-label fw-bold text-secondary">Producto Asociado</label>
                <select className="form-select bg-light" value={productoId} disabled>
                    <option value="">-- Selecciona el producto --</option>
                    {productosMaestros.map(prod => (
                        <option key={prod.id} value={prod.id}>
                            [{prod.sku}] {prod.nombre} - {prod.categoria}
                        </option>
                    ))}
                </select>
            </div>

            <div className="row">
                <div className="col-md-4 mb-3">
                    <label className="form-label fw-bold">Código de Lote</label>
                    <input
                        type="text" className="form-control"
                        value={lote} onChange={e => setLote(e.target.value)}
                    />
                </div>
                <div className="col-md-4 mb-3">
                    <label className="form-label fw-bold">Cantidad Actual</label>
                    <input
                        type="number" className="form-control" min="0"
                        value={cantidad} onChange={e => setCantidad(e.target.value)}
                    />
                </div>
                <div className="col-md-4 mb-3">
                    <label className="form-label fw-bold">Fecha del Lote</label>
                    <input
                        type="datetime-local" className="form-control"
                        value={fechaRegistroLote} onChange={e => setFechaRegistroLote(e.target.value)}
                    />
                </div>
            </div>
            <div className="mt-4 d-flex justify-content-end gap-2">
                <button className="btn btn-secondary" onClick={onCerrar}>Cancelar</button>
                <button className="btn btn-primary fw-bold" onClick={handleSubmit}>Guardar Cambios</button>
            </div>
        </ModalBase>
    );
};