import React from 'react';

const TablaGenerica = ({columns, data = [], actions}) => {
    return (
        <div className="table-responsive shadow-sm rounded">
            <table className="table table-hover align-middle bg-white">
                <thead className="table-light">
                <tr>
                    {columns.map((col, index) => (
                        <th key={index} scope="col" className="text-secondary py-3">
                            {col.label}
                        </th>
                    ))}
                    {actions && <th scope="col" className="text-secondary py-3 text-center">Acciones</th>}
                </tr>
                </thead>
                <tbody>
                {data.length > 0 ? (
                    data.map((item, rowIndex) => (
                        <tr key={item.id || rowIndex}>
                            {columns.map((col, colIndex) => (
                                <td key={colIndex} className="py-3">
                                    {item[col.key]}
                                </td>
                            ))}
                            {actions && (
                                <td className="text-center">
                                    {actions(item)}
                                </td>
                            )}
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan={columns.length + (actions ? 1 : 0)} className="text-center py-4 text-muted">
                            No se encontraron registros.
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
};

export default TablaGenerica