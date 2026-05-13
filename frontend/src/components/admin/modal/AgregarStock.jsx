import React, { useState, useEffect } from 'react';
import { ModalBase} from "../../ModalBase.jsx";
import { useGestionCatalogo} from "../../../hooks/useGestionCatalogo.js";

export const AgregarStock = ({ inventarioId, onCerrar, onGuardar }) => {
    const { productosMaestros, listarCatalogoCompleto } = useGestionCatalogo();

    const [productoId, setProductoId] = useState('');
    const [lote, setLote] = useState('');
    const [cantidad, setCantidad] = useState('');
    const [fechaRegistroLote, setFechaRegistroLote] = useState('');

    useEffect(() => {
        listarCatalogoCompleto();
    }, [listarCatalogoCompleto]);

    const handleSubmit = () => {
        if (!productoId) return alert("Debes seleccionar un producto.");
        if (!lote.trim()) return alert("Debes ingresar el código del lote.");
        if (!cantidad || parseInt(cantidad) <= 0) return alert("Debes ingresar una cantidad válida mayor a 0.");
        if (!fechaRegistroLote) return alert("Debes ingresar la fecha de registro del lote.");

        const fechaFormateada = new Date(fechaRegistroLote).toISOString();
        onGuardar(inventarioId, {
            productoId: productoId,
            lote: lote.trim(),
            cantidad: parseInt(cantidad),
            fechaRegistroLote: fechaFormateada
        });
    };

    return (
        <ModalBase titulo="Agregar Nuevo Lote (Stock)" onCerrar={onCerrar}>
            <div className="mb-3">
                <label className="form-label fw-bold">Producto</label>
                <select
                    className="form-select"
                    value={productoId}
                    onChange={(e) => setProductoId(e.target.value)}
                >
                    <option value="">-- Selecciona el producto que llegó a bodega --</option>
                    {productosMaestros.map(prod => (
                        <option key={prod.productoId} value={prod.productoId}>
                            [{prod.sku}] {prod.nombre} - {prod.categoria}
                        </option>
                    ))}
                </select>
            </div>

            <div className="row">
                <div className="col-md-4 mb-3">
                    <label className="form-label fw-bold">Código de Lote</label>
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Ej: LOTE-A"
                        value={lote}
                        onChange={e => setLote(e.target.value)}
                    />
                </div>
                <div className="col-md-4 mb-3">
                    <label className="form-label fw-bold">Cantidad</label>
                    <input
                        type="number"
                        className="form-control"
                        min="1"
                        value={cantidad}
                        onChange={e => setCantidad(e.target.value)}
                    />
                </div>
                <div className="col-md-4 mb-3">
                    <label className="form-label fw-bold">Fecha del Lote</label>
                    <input
                        type="datetime-local"
                        className="form-control"
                        value={fechaRegistroLote}
                        onChange={e => setFechaRegistroLote(e.target.value)}
                    />
                </div>
            </div>

            <div className="mt-4 d-flex justify-content-end gap-2">
                <button className="btn btn-secondary" onClick={onCerrar}>Cancelar</button>
                <button className="btn btn-primary fw-bold" onClick={handleSubmit}>+ Agregar Stock</button>
            </div>
        </ModalBase>
    );
};