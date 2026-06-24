import React, { useState, useEffect } from 'react';
import TablaGenerica from "../TablaGenerica.jsx";
import { ModalBase } from '../ModalBase.jsx';
import { useTienda } from '../../hooks/useTienda';
import { useAuth } from '../../hooks/useAuth';
import { useUsuario } from '../../hooks/useUsuario';
import { usePedidos } from '../../hooks/usePedidos';

export const ClienteCatalogo = () => {
    const {idBackend} = useAuth();
    const {verificarUsuarioPorAuth0} = useUsuario();
    const { crearPedido, cotizarCostoEnvio, cargandoPedidos } = usePedidos();
    const {
        categorias, productos, cargando,
        listarCategorias, listarProductosPorCategoria, consultarBodegasPorProducto
    } = useTienda();
    const [carrito, setCarrito] = useState([]);
    const [direcciones, setDirecciones] = useState([]);
    const [direccionIdSeleccionada, setDireccionIdSeleccionada] = useState('');
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState('');
    const [bodegaCarrito, setBodegaCarrito] = useState(null);
    const [costoEnvio, setCostoEnvio] = useState(0);
    const [distanciaKm, setDistanciaKm] = useState(0);
    const [modalBodega, setModalBodega] = useState({
        visible: false, producto: null, bodegas: []
    });

    const [usuarioDB, setUsuarioDB] = useState({
        usuarioId: '',
        nombre: '',
        direcciones: []
    });

    useEffect(() => {
        listarCategorias();
        const cargarDatosUsuario = async () => {
            if (!idBackend) {
                return;
            }
            try {
                const respuesta = await verificarUsuarioPorAuth0(idBackend);
                let datosUsuario = respuesta?.data ? respuesta.data : respuesta;
                setUsuarioDB(datosUsuario)
                setDirecciones(datosUsuario.direcciones)
                setDireccionIdSeleccionada(datosUsuario.direcciones[0].ubicacionId)
            } catch (err) {
                console.error(err);
            }
        };
        cargarDatosUsuario();
        const cargarDirecciones = async () => {
            if (!idBackend) return;
            try {
                const respuesta = await verificarUsuarioPorAuth0(idBackend);
                let datos = respuesta?.data ? respuesta.data : respuesta;
                if (Array.isArray(datos)) datos = datos[0];

                if (datos?.direcciones?.length > 0) {
                    setDirecciones(datos.direcciones);
                    setDireccionIdSeleccionada(datos.direcciones[0].ubicacionId);
                }
            } catch (err) {
                console.error("Error al cargar direcciones", err);
            }
        };
        cargarDirecciones();
    }, [listarCategorias, idBackend, verificarUsuarioPorAuth0]);

    useEffect(() => {
        const calcularCostoEnvio = async () => {
            if (bodegaCarrito && direccionIdSeleccionada && direcciones.length > 0) {
                const dirDestino = direcciones.find(d => d.ubicacionId === direccionIdSeleccionada);

                const request = {
                    origenLatitude: bodegaCarrito.lat,
                    origenLongitude: bodegaCarrito.lng,
                    destinoLatitude: dirDestino.latitude,
                    destinoLongitude: dirDestino.longitude
                };

                try {
                    const data = await cotizarCostoEnvio(request);
                    setCostoEnvio(data.costoEnvio);
                    setDistanciaKm(data.distanciaKm);
                } catch (err) {
                    console.error("Error al cotizar:", err);
                }
            } else {
                setCostoEnvio(0);
                setDistanciaKm(0);
            }
        };
        calcularCostoEnvio();
    }, [bodegaCarrito, direccionIdSeleccionada, direcciones]);
    const handleSeleccionarCategoria = (e) => {
        const catId = e.target.value;
        setCategoriaSeleccionada(catId);
        if (catId) {
            listarProductosPorCategoria(catId);
        }
    };
    const handleAbrirSelectorBodega = async (producto) => {
        try {
            const bodegasConStock = await consultarBodegasPorProducto(producto.productoId);
            if (bodegasConStock.length === 0) {
                return alert("Sin stock.");
            }
            setModalBodega({ visible: true, producto, bodegas: bodegasConStock });
        } catch (error) {
            console.error("Error al verificar stock en inventario.");
        }
    };
    const confirmarAñadirAlCarrito = (bodegaId, nombreBodega, ubicacion) => {
        const { producto } = modalBodega;

        const bodegaSeleccionada = {
            id: bodegaId,
            nombre: nombreBodega,
            lat: ubicacion.latitude,
            lng: ubicacion.longitude
        };

        if (carrito.length > 0 && bodegaCarrito.id !== bodegaId) {
            alert(`Tu carrito actual está asociado a los envíos desde "${bodegaCarrito.nombre}". Para pedir desde otra sucursal, debes vaciar tu carrito o hacer pedidos separados.`);
            return;
        }

        if (carrito.length === 0) {
            setBodegaCarrito(bodegaSeleccionada);
        }

        setCarrito(prev => {
            const existe = prev.find(item => item.productoId === producto.productoId);
            if (existe) {
                return prev.map(item =>
                    item.productoId === producto.productoId
                        ? { ...item, cantidad: item.cantidad + 1 }
                        : item
                );
            }
            return [...prev, { ...producto, cantidad: 1 }];
        });

        setModalBodega({ visible: false, producto: null, bodegas: [] });
    };
    const quitarDelCarrito = (productoId) => {
        const nuevoCarrito = carrito.filter(item => item.productoId !== productoId);
        setCarrito(nuevoCarrito);
        if (nuevoCarrito.length === 0) setBodegaCarrito(null);
    };

    const vaciarCarrito = () => {
        if(window.confirm("¿Seguro que quieres vaciar el carrito?")) {
            setCarrito([]);
            setBodegaCarrito(null);
        }
    };
    const confirmarPedido = async () => {
        if (carrito.length === 0){
            return alert("El carrito está vacío");
        }
        if (!direccionIdSeleccionada){
            return alert("Selecciona una dirección de envío");
        }
        const dirDestino = direcciones.find(d => d.ubicacionId === direccionIdSeleccionada);

        const pedidoRequestDTO = {
            usuarioId: usuarioDB.usuarioId,
            destino: {
                calle: dirDestino.calle,
                numero: dirDestino.numero,
                ciudad: dirDestino.ciudad,
                pais: dirDestino.pais || "Chile",
                latitude: dirDestino.latitude,
                longitude: dirDestino.longitude,
            },
            detalles: carrito.map(item => ({
                productoId: item.productoId,
                inventarioId: bodegaCarrito.id,
                cantidad: item.cantidad
            }))
        };

        try {
            await crearPedido(pedidoRequestDTO);
            alert(`¡Pedido confirmado! Se despachará desde la sucursal: ${bodegaCarrito.nombre}`);
            setCarrito([]);
            setBodegaCarrito(null);
        } catch (error) {
            console.log(error)
        }
    };

    const totalCarrito = carrito.reduce((total, item) => total + (item.precioBase * item.cantidad), 0);

    return (
        <div className="row">
            <div className="col-md-8">
                <div className="card shadow-sm p-3 mb-3 border-info bg-light">
                    <h5 className="text-info fw-bold mb-3">1. ¿Qué estás buscando?</h5>
                    <select className="form-select form-select-lg border-info" value={categoriaSeleccionada} onChange={handleSeleccionarCategoria}>
                        <option value="">Selecciona una Categoría para ver los productos</option>
                        {categorias.map(cat => (
                            <option key={cat.categoriaId} value={cat.categoriaId}>{cat.nombre}</option>
                        ))}
                    </select>
                </div>
                {categoriaSeleccionada && (
                    <div className="card shadow-sm p-3 mb-4 border-top border-info border-3">
                        <h4 className="mb-3 text-secondary">2. Productos Disponibles</h4>
                        {cargando ? (
                            <div className="text-center py-4"><div className="spinner-border text-info"></div><p className="mt-2 text-muted">Cargando catálogo...</p></div>
                        ) : productos.length === 0 ? (
                            <div className="alert alert-warning">No hay productos disponibles en esta categoría.</div>
                        ) : (
                            <TablaGenerica
                                columns={[
                                    { label: 'SKU', key: 'sku' },
                                    { label: 'Producto', key: 'nombre' },
                                    { label: 'Precio', key: 'precioBase' }
                                ]}
                                data={productos}
                                actions={(producto) => (
                                    <button
                                        className="btn btn-sm btn-outline-success fw-bold"
                                        onClick={() => handleAbrirSelectorBodega(producto)}
                                    >
                                        + Añadir al carro
                                    </button>
                                )}
                            />
                        )}
                    </div>
                )}
            </div>
            <div className="col-md-4">
                <div className="card shadow-sm p-3 border-primary sticky-top" style={{ top: '20px' }}>
                    <h4 className="mb-3 text-primary">Tu Carrito</h4>
                    {bodegaCarrito && (
                        <div className="alert alert-info py-2 px-3 small mb-3">
                            <i className="bi bi-shop me-2"></i>
                            Despachando desde:<br/>
                            <strong>{bodegaCarrito.nombre}</strong>
                        </div>
                    )}
                    <div className="mb-3 p-2 bg-light rounded border">
                        <label className="form-label small fw-bold text-muted mb-1">Enviar a domicilio:</label>
                        {direcciones.length === 0 ? (
                            <span className="d-block small text-danger">No tienes direcciones. Ve a tu perfil.</span>
                        ) : (
                            <select className="form-select form-select-sm" value={direccionIdSeleccionada} onChange={(e) => setDireccionIdSeleccionada(e.target.value)}>
                                {direcciones.map(dir => (
                                    <option key={dir.ubicacionId} value={dir.ubicacionId}>
                                        {dir.calle} {dir.numero}, {dir.ciudad}
                                    </option>
                                ))}
                            </select>
                        )}
                    </div>

                    <ul className="list-group mb-3 shadow-sm">
                        {carrito.length === 0 ? (
                            <li className="list-group-item text-center text-muted py-4">
                                <small>Tu carrito está vacío</small>
                            </li>
                        ) : carrito.map((item) => (
                            <li key={item.productoId} className="list-group-item d-flex justify-content-between align-items-center">
                                <div>
                                    <h6 className="my-0">{item.nombre} <span className="badge bg-secondary ms-1">x{item.cantidad}</span></h6>
                                    <small className="text-muted">${item.precioBase}</small>
                                </div>
                                <button className="btn btn-sm btn-outline-danger" onClick={() => quitarDelCarrito(item.productoId)}>X</button>
                            </li>
                        ))}
                        <li className="list-group-item bg-light mt-2 border-primary border-2">
                            <div className="d-flex justify-content-between mb-1">
                                <span className="small text-muted">Subtotal:</span>
                                <span className="fw-bold">${totalCarrito}</span>
                            </div>
                            <div className="d-flex justify-content-between mb-1">
                                <span className="small text-muted">Distancia estimada:</span>
                                <span className="fw-bold text-secondary">{distanciaKm ? distanciaKm.toFixed(1) : 0} km</span>
                            </div>
                            <div className="d-flex justify-content-between mb-1">
                                <span className="small text-muted">Costo Envío:</span>
                                <span className="fw-bold">${costoEnvio}</span>
                            </div>
                            <hr className="my-1"/>
                            <div className="d-flex justify-content-between align-items-center">
                                <span className="fw-bold">Total Final</span>
                                <strong className="text-primary fs-5">${totalCarrito + costoEnvio}</strong>
                            </div>
                            <div className="mt-2 text-center">
                                <small className="text-muted" style={{ fontSize: '0.7rem' }}>
                                    * Precio mínimo de envío garantizado: $2.000
                                </small>
                            </div>
                        </li>
                    </ul>

                    <button
                        className="btn btn-primary w-100 fw-bold mb-2 shadow-sm py-2"
                        disabled={carrito.length === 0 || !direccionIdSeleccionada || cargandoPedidos}
                        onClick={confirmarPedido}
                    >
                        {cargandoPedidos ? 'Procesando pago...' : 'Confirmar Pedido'}
                    </button>

                    {carrito.length > 0 && (
                        <button className="btn btn-sm btn-link text-danger w-100 text-decoration-none" onClick={vaciarCarrito}>
                            Vaciar Carrito
                        </button>
                    )}
                </div>
            </div>
            {modalBodega.visible && (
                <ModalBase titulo={`Disponibilidad: ${modalBodega.producto.nombre}`} onCerrar={() => setModalBodega({ visible: false, producto: null, bodegas: [] })}>
                    <p className="mb-3 text-muted">¿Desde qué sucursal deseas que despachemos este producto? Selecciona la más cercana a tu domicilio.</p>
                    <div className="list-group">
                        {modalBodega.bodegas.map(bodega => (
                            <button
                                key={bodega.inventarioId}
                                type="button"
                                className="list-group-item list-group-item-action d-flex justify-content-between align-items-center"
                                onClick={() => confirmarAñadirAlCarrito(bodega.inventarioId, bodega.nombre, bodega.ubicacion)}
                            >
                                <div>
                                    <strong className="d-block text-primary">{bodega.nombre}</strong>
                                    <small className="text-muted">{bodega.ubicacion.calle} {bodega.ubicacion.numero}, {bodega.ubicacion.ciudad}</small>
                                </div>
                                <span className="badge bg-success rounded-pill px-3 py-2">Elegir</span>
                            </button>
                        ))}
                    </div>
                </ModalBase>
            )}
        </div>
    );
};