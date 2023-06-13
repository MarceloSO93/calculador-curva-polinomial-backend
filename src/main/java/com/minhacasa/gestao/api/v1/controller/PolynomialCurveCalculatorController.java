package com.minhacasa.gestao.api.v1.controller;

import com.minhacasa.gestao.api.v1.controller.dtos.Chart;
import com.minhacasa.gestao.domain.service.v1.PolynomialCurveCalculatorService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/process-coordinates")
public class PolynomialCurveCalculatorController {

    private final PolynomialCurveCalculatorService polynomialCurveCalculatorService;


    public PolynomialCurveCalculatorController(PolynomialCurveCalculatorService polynomialCurveCalculatorService) {
        this.polynomialCurveCalculatorService = polynomialCurveCalculatorService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Chart save(@Valid @RequestBody double[][] cordinateInput) {
        return polynomialCurveCalculatorService.save(cordinateInput);
    }


}

