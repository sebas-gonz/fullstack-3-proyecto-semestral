import { useState } from 'react';
import { useAuth } from '../hooks/useAuth';
import axios from 'axios';
export const RegistroUsuario = () => {
    const { usuario, obtenerToken, idBackend } = useAuth();
    const [formData, setFormData] = useState({
        nombre: '',
        apellido: '',
        email: usuario?.email || '',
        idAuth0: idBackend,
        rol: 'USER'
    });
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = await obtenerToken();
            await axios.post('http://localhost:8091/api/v1/usuarios', formData, {
                headers: { Authorization: `Bearer ${token}` }
            });
            window.location.href = '/dashboard';
        } catch (error) {
            alert("Error al sincronizar el perfil con el ERP.");
        }
    };
    return (
        <div className="container mt-5">
            <div className="card p-4 shadow">
                <h3>Bienvenido a Innovatech Solutions</h3>
                <p>Para continuar, completa tu perfil corporativo:</p>
                <form onSubmit={handleSubmit}>
                    <input type="text" className="form-control mb-2" placeholder="Nombre"
                           onChange={e => setFormData({...formData, nombre: e.target.value})} required />
                    <input type="text" className="form-control mb-2" placeholder="Apellido"
                           onChange={e => setFormData({...formData, apellido: e.target.value})} required />
                    <button className="btn btn-primary w-100">Finalizar Registro</button>
                </form>
            </div>
        </div>
    );
};