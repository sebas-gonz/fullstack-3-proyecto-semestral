package com.seb.msinventario.infrastructure.adapter.in.kafka.dto.pedido;

import com.seb.msinventario.infrastructure.adapter.in.kafka.dto.pedido.producto.ProductoPedidoEventDto;

import java.util.List;
import java.util.UUID;

public record PedidoCreadoEventDto(UUID pedidoId,
                                   List<ProductoPedidoEventDto> productosPedidos) {
}
