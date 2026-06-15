package com.seb.mspedido.infrastructure.adapter.in.web.dto.google.distancematrix;

import java.util.List;

public record GoogleDistanceResponse(List<Row> rows,String status) {
}
