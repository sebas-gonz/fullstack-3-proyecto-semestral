import { useAuth0 } from "@auth0/auth0-react";

export const useAuth = () => {
    const {
        loginWithRedirect,
        logout,
        user,
        isAuthenticated,
        isLoading,
        getAccessTokenSilently
    } = useAuth0();
    const iniciarSesion = () => loginWithRedirect();

    const cerrarSesion = () => logout({
        logoutParams: { returnTo: window.location.origin }
    });
    return {
        iniciarSesion,
        cerrarSesion,
        usuario: user,
        idBackend: user?.sub,
        estaAutenticado: isAuthenticated,
        cargando: isLoading,
        obtenerToken: getAccessTokenSilently
    };
};