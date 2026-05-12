import React from 'react';

export const ModalBase = ({ titulo, onCerrar, children }) => {
    return (
        <div className="modal d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1050 }}>
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content shadow-lg border-0">
                    <div className="modal-header bg-dark text-white">
                        <h5 className="modal-title">{titulo}</h5>
                        <button type="button" className="btn-close btn-close-white" onClick={onCerrar}></button>
                    </div>
                    <div className="modal-body p-4">
                        {children}
                    </div>
                </div>
            </div>
        </div>
    );
};