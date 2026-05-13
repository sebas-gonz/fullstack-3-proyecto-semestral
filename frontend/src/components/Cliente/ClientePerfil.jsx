import React, { useState, useEffect } from 'react';
import { ModalBase } from '../ModalBase.jsx';
import { BuscarUbicaciones } from '../BuscarUbicaciones.jsx';
import { useUsuario } from '../../hooks/useUsuario';
import { useAuth } from '../../hooks/useAuth';
import {useDireccionesUsuario} from "../../hooks/useDireccionesUsuario.js";

export const ClientePerfil = () => {
    const { usuario: usuarioAuth0, idBackend } = useAuth();
    const { verificarUsuarioPorAuth0, actualizarUsuario, cargandoUsuario } = useUsuario();
    const { agregarDireccion, eliminarDireccion, cargandoDireccion } = useDireccionesUsuario();
    const [cargandoPerfil, setCargandoPerfil] = useState(true);

    const [usuarioDB, setUsuarioDB] = useState({
        usuarioId: '',
        nombre: '',
        apellido: '',
        email: '',
        rol: '',
        direcciones: []
    });

    const [mostrarModalDir, setMostrarModalDir] = useState(false);
    const [nuevaUbicacion, setNuevaUbicacion] = useState(null);

    useEffect(() => {
        const cargarDatos = async () => {
            if (!idBackend){
              return
            }
            try {
                const datos = await verificarUsuarioPorAuth0(idBackend);
                if (datos) {
                    setUsuarioDB({
                        ...datos,
                        direcciones: datos.direcciones || []
                    });
                }
            } catch (error) {
                console.error("Error al cargar perfil", error);
            } finally {
                setCargandoPerfil(false);
            }
        };
        cargarDatos();
    }, [idBackend, verificarUsuarioPorAuth0]);

    const handleChangeInput = (e) => {
        setUsuarioDB({
            ...usuarioDB,
            [e.target.name]: e.target.value
        });
    };
    const handleActualizarDatos = async () => {
        if (!usuarioDB.nombre.trim() || !usuarioDB.apellido.trim()) {
            return alert("El nombre y apellido no pueden estar vacíos.");
        }
        const requestDTO = {
            nombre: usuarioDB.nombre,
            apellido: usuarioDB.apellido,
            email: usuarioDB.email,
            idAuth0: idBackend,
            rol: usuarioDB.rol
        };
        try {
            await actualizarUsuario(usuarioDB.usuarioId, requestDTO);
        } catch (error) {
            alert("Error al actualizar los datos");
        }
    };

    const handleGuardarDireccion = async () => {
        console.log('Guardar direccion');
        if (!nuevaUbicacion) return alert("Busca y selecciona una dirección válida.");
        const direccionRequestDTO = {
            pais: nuevaUbicacion.pais || "Chile",
            ciudad: nuevaUbicacion.ciudad,
            calle: nuevaUbicacion.calle,
            numero: nuevaUbicacion.numero,
        };

        try {
            const direccionGuardada = await agregarDireccion(usuarioDB.usuarioId, direccionRequestDTO);
            setUsuarioDB(prev => ({
                ...prev,
                direcciones: [...prev.direcciones, direccionGuardada]
            }));
            setMostrarModalDir(false);
            setNuevaUbicacion(null);
        } catch (error) {
            console.error(error);
        }
    };

    const handleEliminarDireccion = async (direccionId) => {
        const confirmar = window.confirm("¿Seguro que deseas eliminar esta dirección?");
        if (!confirmar){
          return
        }
        try {
            await eliminarDireccion(usuarioDB.usuarioId, direccionId);
            setUsuarioDB(prev => ({
                ...prev,
                direcciones: prev.direcciones.filter(dir => dir.ubicacionId !== direccionId)
            }));
        } catch (error) {
            alert("Error al eliminar la dirección.");
        }
    };

    if (cargandoPerfil) {
        return <div className="text-center py-5"><div className="spinner-border text-primary"></div></div>;
    }
    return (
        <div className="row">
            <div className="col-md-5 border-end">
                <h5 className="mb-3 text-secondary">Mis Datos</h5>
                <div className="mb-3">
                    <label className="form-label text-muted small mb-0">Nombre</label>
                    <input type="text" className="form-control"
                           name="nombre"
                           value={usuarioDB.nombre}
                           onChange={handleChangeInput} />
                </div>
                <div className="mb-3">
                    <label className="form-label text-muted small mb-0">Apellido</label>
                    <input type="text" className="form-control"
                           name="apellido"
                           value={usuarioDB.apellido}
                           onChange={handleChangeInput} />
                </div>
                <div className="mb-4">
                    <label className="form-label text-muted small mb-0">Correo (Solo lectura)</label>
                    <input type="email" className="form-control bg-light" disabled value={usuarioDB.email} />
                </div>
                <button
                    className="btn btn-outline-primary w-100 fw-bold"
                    onClick={handleActualizarDatos}
                    disabled={cargandoUsuario}
                >
                    {cargandoUsuario ? 'Actualizando...' : 'Actualizar Datos'}
                </button>
            </div>

            <div className="col-md-7 ps-md-4">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h5 className="text-secondary mb-0">Mis Direcciones de Envío</h5>
                    <button className="btn btn-sm btn-success fw-bold" onClick={() => setMostrarModalDir(true)}>
                        + Agregar Dirección
                    </button>
                </div>

                {usuarioDB.direcciones.length === 0 ? (
                    <div className="alert alert-light text-center border text-muted">
                        No hay direcciones registradas. Agrega una para recibir los pedidos.
                    </div>
                ) : (
                    <div className="list-group">
                        {usuarioDB.direcciones.map((dir, idx) => (
                            <div key={idx} className="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                                <div>
                                    <strong className="d-block text-dark">{dir.calle} {dir.numero}</strong>
                                    <small className="text-muted">{dir.ciudad}, {dir.pais || 'Chile'}</small>
                                </div>
                                <button className="btn btn-sm btn-outline-danger"
                                onClick={() => handleEliminarDireccion(dir.ubicacionId)}>Eliminar</button>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {mostrarModalDir && (
                <ModalBase titulo="Agregar Dirección de Envío" onCerrar={() => setMostrarModalDir(false)}>
                    <div className="mb-3">
                        <label className="form-label fw-bold">Buscar mi dirección</label>
                        <BuscarUbicaciones
                            onUbicacionConfirmada={(ubicacion) => setNuevaUbicacion(ubicacion)}
                        />
                        {nuevaUbicacion && (
                            <div className="alert alert-success p-2 mt-3 small shadow-sm">
                                <strong>Encontrado:</strong><br/>
                                {nuevaUbicacion.calle} {nuevaUbicacion.numero}, {nuevaUbicacion.ciudad}, {nuevaUbicacion.pais}
                            </div>
                        )}
                    </div>
                    <div className="mt-4 d-flex justify-content-end gap-2">
                        <button className="btn btn-secondary" onClick={() => setMostrarModalDir(false)}>Cancelar</button>
                        <button className="btn btn-primary fw-bold" onClick={handleGuardarDireccion}>
                            Guardar Dirección
                        </button>
                    </div>
                </ModalBase>
            )}
        </div>
    );
};