import React, { useEffect, useState } from 'react';
import TablaGenerica from "../TablaGenerica.jsx";
import { ModalBase } from "../ModalBase.jsx";
import { useGestionCatalogo } from '../../hooks/useGestionCatalogo';

export const AdminProductosDeCategoria = ({ categoria, onVolver }) => {
    const {
        productosDeCategoria, cargandoCatalogo,
        listarProductosDeCategoria, crearNuevoProducto, eliminarProducto
    } = useGestionCatalogo();

    const [modalVisible, setModalVisible] = useState(false);
    const [nuevoProducto, setNuevoProducto] = useState({ sku: '', nombre: '', descripcion: '', precioBase: '' });

    useEffect(() => {
        listarProductosDeCategoria(categoria.categoriaId);
    }, [categoria.categoriaId, listarProductosDeCategoria]);

    const columnas = [
        { label: 'SKU', key: 'sku' },
        { label: 'Nombre', key: 'nombre' },
        { label: 'Precio Base', key: 'precioBase' },
        { label: 'Stock Global', key: 'cantidadTotal' }
    ];

    const handleGuardarProducto = async () => {
        if (!nuevoProducto.sku || !nuevoProducto.nombre || !nuevoProducto.precioBase) {
            return alert("SKU, Nombre y Precio son obligatorios.");
        }

        try {
            await crearNuevoProducto(categoria.categoriaId, {
                ...nuevoProducto,
                precioBase: parseFloat(nuevoProducto.precioBase)
            });
            setModalVisible(false);
            setNuevoProducto({ sku: '', nombre: '', descripcion: '', precioBase: '' });
            alert("Producto añadido al catálogo.");
            listarProductosDeCategoria(categoria.categoriaId);
        } catch (error) {
            alert("Error al crear el producto.");
        }
    };

    const handleEliminar = async (id) => {
        if (window.confirm("¿Eliminar este producto del catálogo maestro?")) {
            await eliminarProducto(id);
            listarProductosDeCategoria(categoria.categoriaId);
        }
    };

    return (
        <div className="card border-info shadow-sm">
            <div className="card-header bg-info text-white d-flex justify-content-between align-items-center">
                <h5 className="mb-0"><i className="bi bi-box-seam me-2"></i>Productos en: {categoria.nombre}</h5>
                <button className="btn btn-sm btn-light text-info fw-bold" onClick={onVolver}>
                    Volver a Categorías
                </button>
            </div>
            <div className="card-body">
                <div className="d-flex justify-content-end mb-3">
                    <button className="btn btn-success btn-sm fw-bold" onClick={() => setModalVisible(true)}>
                        + Nuevo Producto
                    </button>
                </div>

                {cargandoCatalogo ? (
                    <div className="text-center py-4"><div className="spinner-border text-info"></div></div>
                ) : (
                    <TablaGenerica
                        columns={columnas}
                        data={productosDeCategoria}
                        actions={(prod) => (
                            <button className="btn btn-sm btn-outline-danger" onClick={() => handleEliminar(prod.productoId)}>
                                <i className="bi bi-trash"></i>
                            </button>
                        )}
                    />
                )}
            </div>

            {modalVisible && (
                <ModalBase titulo={`Nuevo Producto en ${categoria.nombre}`} onCerrar={() => setModalVisible(false)}>
                    <div className="row">
                        <div className="col-md-6 mb-3">
                            <label className="form-label fw-bold">SKU</label>
                            <input type="text" className="form-control" value={nuevoProducto.sku}
                                   onChange={e => setNuevoProducto({...nuevoProducto, sku: e.target.value})} />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label fw-bold">Precio Base</label>
                            <input type="number" className="form-control" value={nuevoProducto.precioBase}
                                   onChange={e => setNuevoProducto({...nuevoProducto, precioBase: e.target.value})} />
                        </div>
                    </div>
                    <div className="mb-3">
                        <label className="form-label fw-bold">Nombre del Producto</label>
                        <input type="text" className="form-control" value={nuevoProducto.nombre}
                               onChange={e => setNuevoProducto({...nuevoProducto, nombre: e.target.value})} />
                    </div>
                    <div className="mb-3">
                        <label className="form-label fw-bold">Descripción</label>
                        <textarea className="form-control" rows="2" value={nuevoProducto.descripcion}
                                  onChange={e => setNuevoProducto({...nuevoProducto, descripcion: e.target.value})} />
                    </div>
                    <div className="d-flex justify-content-end gap-2 mt-3">
                        <button className="btn btn-secondary" onClick={() => setModalVisible(false)}>Cancelar</button>
                        <button className="btn btn-primary fw-bold" onClick={handleGuardarProducto}>Guardar en Catálogo</button>
                    </div>
                </ModalBase>
            )}
        </div>
    );
};