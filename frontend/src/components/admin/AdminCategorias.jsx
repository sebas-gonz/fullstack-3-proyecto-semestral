import React, {useEffect, useState} from 'react';
import TablaGenerica from "../TablaGenerica.jsx";
import {ModalBase} from "../ModalBase.jsx";
import {useGestionCatalogo} from '../../hooks/useGestionCatalogo';
import {AdminProductosDeCategoria} from "./AdminProductosCategoria.jsx";

export const AdminCategorias = () => {
    const {
        categorias, cargandoCatalogo,
        listarCategorias, crearNuevaCategoria, eliminarCategoria
    } = useGestionCatalogo();
    const [modalVisible, setModalVisible] = useState(false);
    const [nuevaCategoria, setNuevaCategoria] = useState({nombre: '', descripcion: ''});
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null);
    useEffect(() => {
        listarCategorias();
    }, [listarCategorias]);
    const categoriasMapeadas = categorias.map(cat => ({
        ...cat,
        totalProductos: cat.productos ? cat.productos.length : 0
    }));

    const columnasCategorias = [
        {label: 'Nombre', key: 'nombre'},
        {label: 'Descripción', key: 'descripcion'},
        {label: 'Cant. Productos', key: 'totalProductos'}
    ];

    const handleCrearCategoria = async () => {
        if (!nuevaCategoria.nombre.trim()) return alert("El nombre de la categoría es obligatorio.");

        try {
            await crearNuevaCategoria(nuevaCategoria);
            setModalVisible(false);
            setNuevaCategoria({nombre: '', descripcion: ''});
            alert("¡Categoría creada con éxito!");
            listarCategorias();
        } catch (error) {
           console.error(error);
        }
    };

    const handleEliminarCategoria = async (id) => {
        if (window.confirm("¿Estás seguro de eliminar esta categoría? Si tiene productos, esto podría causar un error en la base de datos o eliminarlos en cascada.")) {
            try {
                await eliminarCategoria(id);
                alert("Categoría eliminada correctamente.");
                listarCategorias();
            } catch (error) {
                alert("Error al eliminar la categoría. Verifica que no tenga productos asociados.");
            }
        }
    };

    if (categoriaSeleccionada) {
        return (
            <AdminProductosDeCategoria
                categoria={categoriaSeleccionada}
                onVolver={() => {
                    setCategoriaSeleccionada(null);
                    listarCategorias();
                }}
            />
        );
    }

    return (
        <div className="card border-primary shadow-sm">
            <div className="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                <h5 className="mb-0"><i className="bi bi-tags-fill me-2"></i>Gestión de Categorías</h5>
                <button className="btn btn-sm btn-light text-primary fw-bold" onClick={listarCategorias}>
                    <i className="bi bi-arrow-clockwise"></i> Actualizar
                </button>
            </div>

            <div className="card-body">
                <div className="d-flex justify-content-end mb-3">
                    <button className="btn btn-success fw-bold shadow-sm" onClick={() => setModalVisible(true)}>
                        + Nueva Categoría
                    </button>
                </div>

                {cargandoCatalogo ? (
                    <div className="text-center py-4">
                        <div className="spinner-border text-primary"></div>
                    </div>
                ) : (
                    <TablaGenerica
                        columns={columnasCategorias}
                        data={categoriasMapeadas}
                        actions={(cat) => (
                            <div className="btn-group">
                                <button className="btn btn-sm btn-info text-white fw-bold"
                                        onClick={() => setCategoriaSeleccionada(cat)}>
                                    <i className="bi bi-box-seam me-1"></i> Ver Productos
                                </button>
                                <button className="btn btn-sm btn-outline-danger"
                                        onClick={() => handleEliminarCategoria(cat.categoriaId)}>
                                    <i className="bi bi-trash"></i> Eliminar
                                </button>
                            </div>
                        )}
                    />
                )}
                {modalVisible && (
                    <ModalBase titulo="Registrar Nueva Categoría" onCerrar={() => setModalVisible(false)}>
                        <div className="mb-3">
                            <label className="form-label fw-bold">Nombre de la Categoría</label>
                            <input
                                type="text"
                                className="form-control"
                                placeholder="Ej: Polerones"
                                value={nuevaCategoria.nombre}
                                onChange={e => setNuevaCategoria({...nuevaCategoria, nombre: e.target.value})}
                            />
                        </div>
                        <div className="mb-3">
                            <label className="form-label fw-bold">Descripción (Opcional)</label>
                            <textarea
                                className="form-control"
                                rows="3"
                                placeholder="Breve descripción de los artículos de esta categoría..."
                                value={nuevaCategoria.descripcion}
                                onChange={e => setNuevaCategoria({...nuevaCategoria, descripcion: e.target.value})}
                            ></textarea>
                        </div>
                        <div className="d-flex justify-content-end gap-2 mt-4">
                            <button className="btn btn-secondary" onClick={() => setModalVisible(false)}>Cancelar
                            </button>
                            <button className="btn btn-primary fw-bold" onClick={handleCrearCategoria}>Guardar
                                Categoría
                            </button>
                        </div>
                    </ModalBase>
                )}
            </div>
        </div>
    );
};