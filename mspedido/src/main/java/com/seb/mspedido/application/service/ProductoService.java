package com.seb.mspedido.application.service;

import com.seb.mspedido.application.mapper.ProductoDomainMapper;
import com.seb.mspedido.application.port.in.ActualizarProductoInputPort;
import com.seb.mspedido.application.port.in.command.producto.ProductoActualizadoCommand;
import com.seb.mspedido.application.port.out.ProductoCatalogoOutputPort;
import com.seb.mspedido.domain.model.ProductoReferencia;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ProductoService implements ActualizarProductoInputPort {
    private ProductoCatalogoOutputPort  productoCatalogoOutputPort;
    private ProductoDomainMapper productoDomainMapper;

    @Override
    @Transactional
    public ProductoReferencia actualizarProducto(ProductoActualizadoCommand productoActualizadoCommand) {
        ProductoReferencia producto = productoDomainMapper.toDomain(productoActualizadoCommand);
        return productoCatalogoOutputPort.guardarProductoReferencia(producto);
    }

}
