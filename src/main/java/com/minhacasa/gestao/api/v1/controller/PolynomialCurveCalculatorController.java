package com.minhacasa.gestao.api.v1.controller;

import com.minhacasa.gestao.api.v1.controller.dtos.Chart;
import com.minhacasa.gestao.domain.enums.CurveTypeEnum;
import com.minhacasa.gestao.domain.service.v1.PolynomialCurveCalculatorService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/v1/process-coordinates")
public class PolynomialCurveCalculatorController {

    private final PolynomialCurveCalculatorService polynomialCurveCalculatorService;


    public PolynomialCurveCalculatorController(PolynomialCurveCalculatorService polynomialCurveCalculatorService) {
        this.polynomialCurveCalculatorService = polynomialCurveCalculatorService;
    }

    @PostMapping(
            path = "/curve-type/{curveType}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Chart save(
            @PathVariable CurveTypeEnum curveType,
            @RequestBody double[][] cordinateInput
    ) {
        return polynomialCurveCalculatorService.save(cordinateInput, curveType);
    }


}

