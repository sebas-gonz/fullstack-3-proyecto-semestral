package com.seb.msbffweb.client;

import com.seb.msbffweb.dto.out.envio.EnvioWebResponse;
import com.seb.msbffweb.dto.out.page.PaginaRestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-envio")
public interface EnvioClient {
    @GetMapping("/api/v1/envios")
    PaginaRestResponse<EnvioWebResponse> obtenerTodosLosEnvios(@RequestParam(defaultValue = "0", name = "page") int page,
                                                               @RequestParam(defaultValue = "10", name = "size")  int size);
    @GetMapping("/api/v1/envios/estado-envios")
    List<String> obtenerEstadosPosiblesDeEnvios();

    @GetMapping("/api/v1/envios/estado/{estado}")
    PaginaRestResponse<EnvioWebResponse> obtenerEnviosPorEstado(@PathVariable String estado,
                                                                @RequestParam(defaultValue = "0", name = "page") int page,
                                                                @RequestParam(defaultValue = "10", name = "size")  int size);
}
