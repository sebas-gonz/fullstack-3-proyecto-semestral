import { useState } from 'react';

export const useBuscarUbicaciones = () => {
    const [busqueda, setBusqueda] = useState('');
    const [resultados, setResultados] = useState([]);
    const [buscando, setBuscando] = useState(false);

    const buscarUbicaciones = async () => {
        if (!busqueda.trim()){
            return
        }
        setBuscando(true);
        setResultados([]);
        try {
            const response = await fetch('https://places.googleapis.com/v1/places:searchText', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Goog-Api-Key': import.meta.env.VITE_GOOGLE_MAPS_API_KEY,
                    'X-Goog-FieldMask': 'places.displayName,places.formattedAddress,places.location,places.addressComponents'
                },
                body: JSON.stringify({
                    textQuery: busqueda,
                    languageCode: 'es'
                })
            });
            const data = await response.json();
            if (data.places.length > 0) {
                setResultados(data.places);
            } else {
                alert("Ingrese una ubicacion real.");
            }
        } catch (error) {
            console.error("Error en la busqueda de ubicaciones", error);
        } finally {
            setBuscando(false);
        }
    };
    const obtenerUbicacion = (lugarCrudo) => {
        let calle = '';
        let numero = 'S/N';
        let ciudad = '';
        let pais = '';

        lugarCrudo.addressComponents.forEach(component => {
            const types = component.types;
            if (types.includes('route')){
                calle = component.longText;
            }
            if (types.includes('street_number')){
                numero = component.longText;
            }
            if (types.includes('locality') || types.includes('administrative_area_level_3')){
                ciudad = component.longText;
            }
            if (types.includes('country')){
                pais = component.longText;
            }
        });
        return {
            calle,
            numero,
            ciudad,
            pais,
            latitude: lugarCrudo.location.latitude,
            longitude: lugarCrudo.location.longitude,
            direccionFormateada: lugarCrudo.formattedAddress
        };
    };

    const limpiarBuscador = () => {
        setBusqueda('');
        setResultados([]);
    };

    return {
        busqueda, setBusqueda,
        resultados, buscando,
        buscarUbicaciones, obtenerUbicacion, limpiarBuscador
    };
};