import React, { useState, useEffect } from 'react';
import { GoogleMap, useJsApiLoader, DirectionsRenderer, Marker } from '@react-google-maps/api';

const containerStyle = { width: '100%', height: '400px', borderRadius: '8px' };
const centroPorDefecto = { lat: -34.1708, lng: -70.7444 };

export const MapaRuta = ({ origenCoords, destinoCoords }) => {
    const { isLoaded } = useJsApiLoader({
        id: 'google-map-script',
        googleMapsApiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY
    });

    const [directionsResponse, setDirectionsResponse] = useState(null);
    useEffect(() => {
        if (isLoaded && origenCoords && destinoCoords) {
            const directionsService = new window.google.maps.DirectionsService();
            directionsService.route(
                {
                    origin: origenCoords,
                    destination: destinoCoords,
                    travelMode: window.google.maps.TravelMode.DRIVING,
                },
                (result, status) => {
                    if (status === window.google.maps.DirectionsStatus.OK) {
                        setDirectionsResponse(result);
                    } else {
                        console.error("Error al calcular la ruta:", status);
                    }
                }
            );
        }
    }, [isLoaded, origenCoords, destinoCoords]);

    if (!isLoaded) return <div>Cargando mapa...</div>;

    return (
        <GoogleMap mapContainerStyle={containerStyle} center={centroPorDefecto} zoom={13}>
            {!directionsResponse && origenCoords && <Marker position={origenCoords} label="A" />}
            {!directionsResponse && destinoCoords && <Marker position={destinoCoords} label="B" />}
            {directionsResponse && (
                <DirectionsRenderer directions={directionsResponse} />
            )}
        </GoogleMap>
    );
};