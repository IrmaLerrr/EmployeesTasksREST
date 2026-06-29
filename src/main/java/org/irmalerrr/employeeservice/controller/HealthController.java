package org.irmalerrr.employeeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "HealthCheck")
public class HealthController {

    @Operation(summary = "HealthCheck")
    @GetMapping("/health")
    public String health() {
        return "200 OK";
    }  //todo @saivanov: нужен ли вообще этот класс? в будущем мы будущем надо будет познакомиться с акутатором) и этот функционал упраздница
}
