import {BrowserRouter as Router, Routes, Route, Navigate, useNavigate} from 'react-router-dom';
import { useAuth } from "./hooks/useAuth.js";
import { VistaAdmin } from "./vistas/VistaAdmin.jsx";
import { VistaCliente } from "./vistas/VistaCliente.jsx";
import { VistaRepartidor } from "./vistas/vistaRepartidor.jsx";
import { RegistroUsuario } from "./vistas/RegistroUsuario.jsx";
import { LoginLanding } from "./vistas/LoginLanding.jsx";
import { Layout } from "./components/Layout.jsx";
import {useUsuario} from "./hooks/useUsuario.js";
import {useEffect, useState} from "react";
const InterceptorAuth0 = ({ children }) => {
    const { estaAutenticado, idBackend, cargando: cargandoAuth } = useAuth();
    const { verificarUsuarioPorAuth0 } = useUsuario();
    const navigate = useNavigate();
    const [verificandoDB, setVerificandoDB] = useState(true);

    useEffect(() => {
        const sincronizarConBackend = async () => {
            if (cargandoAuth || !estaAutenticado){
              return
            }
            try {
                const usuarioDB = await verificarUsuarioPorAuth0(idBackend);

                if (!usuarioDB) {
                    setVerificandoDB(false);
                    navigate('/completar-perfil');
                } else {
                    setVerificandoDB(false);
                }
            } catch (error) {
                console.error("Error sincronizando", error);
                setVerificandoDB(false);
            }
        };
        sincronizarConBackend();
    }, [estaAutenticado, idBackend, cargandoAuth, verificarUsuarioPorAuth0, navigate]);
    if (cargandoAuth || verificandoDB) {
        return <div className="spinner-border text-primary m-5"></div>;
    }
    return children;
};
function App() {
    const { usuario, estaAutenticado, cargando } = useAuth();
    const roles = usuario?.["https://erp-api.com/roles"] || [];
    if (cargando) {
        return (
            <div className="min-vh-100 d-flex justify-content-center align-items-center bg-light">
                <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Cargando...</span>
                </div>
            </div>
        );
    }
    return (
        <Router>
            <Routes>
                <Route path="/" element={!estaAutenticado ? <LoginLanding /> : <Navigate to="/dashboard" />} />
                <Route path="/completar-perfil" element={<RegistroUsuario />} />
                <Route path="/dashboard" element={
                    estaAutenticado ? (
                        <InterceptorAuth0>
                            <Layout>
                                {roles.includes('ADMIN') && <VistaAdmin />}
                                {roles.includes('USER') && <VistaCliente />}
                                {roles.includes('DELIVER') && <VistaRepartidor />}
                            </Layout>
                        </InterceptorAuth0>
                    ) : (
                        <Navigate to="/" />
                    )
                } />
            </Routes>
        </Router>
    );
}
export default App;