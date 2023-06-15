package com.minhacasa.gestao.domain.service.v1;

import com.minhacasa.gestao.api.v1.controller.dtos.Chart;
import com.minhacasa.gestao.api.v1.controller.dtos.Coordinate;
import com.minhacasa.gestao.domain.enums.CurveTypeEnum;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.SimpleCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.minhacasa.gestao.api.v1.controller.dtos.Coordinate.buildCoordinate;
import static com.minhacasa.gestao.domain.enums.CurveTypeEnum.EXPONENTIAL;
import static com.minhacasa.gestao.domain.enums.CurveTypeEnum.POLINOMIAL;
import static com.minhacasa.gestao.domain.utils.NumberUtils.round;
import static java.util.Collections.emptyList;

@Service
public class PolynomialCurveCalculatorService {

    private static String functionLaw;

    public PolynomialCurveCalculatorService() {
    }

    public Chart save(double[][] cordinatesInput, CurveTypeEnum curveType) {

        int polynomialDegree = cordinatesInput.length - 1;

        if (POLINOMIAL.equals(curveType)) {
            double[] coefficients = polinomialCurveCalculate(cordinatesInput, polynomialDegree);
            functionLaw = buildPolinomialFunctionLaw(polynomialDegree, coefficients);
        } else if (EXPONENTIAL.equals(curveType)){
            double[] bestParamether = exponentialCurveCalculate(cordinatesInput, polynomialDegree);
            functionLaw = buildExponentialFunctionLaw(bestParamether);
        } else {
            return new Chart(emptyList(), "f(x) = Error");
        }

        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < cordinatesInput.length; i++) {
            double x = cordinatesInput[i][0];

            double y = calculateY(functionLaw, x);
            coordinates.add(buildCoordinate(x, y, true));
        }

        List<Coordinate> lowerCoordinates = buildLowerCoordinates(cordinatesInput);
        List<Coordinate> superiorCoordinates = superiorCoordinates(cordinatesInput);

        List<Coordinate> combinedList = new ArrayList<>(lowerCoordinates);
        combinedList.addAll(coordinates);
        combinedList.addAll(superiorCoordinates);

        return new Chart(combinedList, "f(x) = " + functionLaw);
    }

    private double calculateY(String formula, double x) {
        Map<String, Double> variables = new HashMap<String, Double>() {
        };
        variables.put("x", x);

        Set<String> nameOfVariables = new HashSet<>();
        variables.forEach((variable, coeficient) -> nameOfVariables.add(variable));

        double y = new ExpressionBuilder(formula)
                .variables(nameOfVariables)
                .build()
                .setVariables(variables)
                .evaluate();
        return  Math.round(y);
    }

    private static double[] polinomialCurveCalculate(double[][] coordenadas, int grauPolinomio) {
        WeightedObservedPoints points = new WeightedObservedPoints();
        for (double[] coordenada : coordenadas) {
            double x = coordenada[0];
            double y = coordenada[1];
            points.add(x, y);
        }

        // Realizar o ajuste de curva polinomial
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(grauPolinomio);
        return fitter.fit(points.toList());
    }

    private String buildPolinomialFunctionLaw(int polynomialDegree, double[] coefficients) {
        StringBuilder functionLaw = new StringBuilder();

        for (int i = 0; i <= polynomialDegree; i++) {
            if (i == 0)
                functionLaw.append(coefficients[i]);
            else
                functionLaw.append(" + ").append(coefficients[i]).append(" * x^").append(i);
        }
        return functionLaw.toString();
    }

    private static double[] exponentialCurveCalculate(double[][] coordenadas, int grauPolinomio) {
        WeightedObservedPoints points = new WeightedObservedPoints();
        for (double[] coordenada : coordenadas) {
            double x = coordenada[0];
            double y = coordenada[1];
            points.add(x, y);
        }

        ParametricUnivariateFunction function = new ParametricUnivariateFunction() {
            @Override
            public double value(double x, double... parameters) {
                return parameters[0] * Math.exp(parameters[1] * x);
            }

            @Override
            public double[] gradient(double x, double... parameters) {
                double[] gradient = new double[parameters.length];
                gradient[0] = Math.exp(parameters[1] * x);
                gradient[1] = parameters[0] * x * Math.exp(parameters[1] * x);
                return gradient;
            }
        };

        SimpleCurveFitter fitter = SimpleCurveFitter.create(function, new double[]{1.0, 1.0});

        return fitter.fit(points.toList());
    }

    private String buildExponentialFunctionLaw(double[] bestParameters) {
        //double y = melhoresParametros[0] * Math.exp(melhoresParametros[1] * x);
        return bestParameters[0] + " * exp(" + bestParameters[1] + " * x)";
    }

    private List<Coordinate> superiorCoordinates(double[][] cordinatesInput) {

        int lowerCoordinatesNumber = (21 - cordinatesInput.length) / 2;
        double firstX = cordinatesInput[cordinatesInput.length - 1][0];
        double position = firstX + 1;

        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; (i < lowerCoordinatesNumber && position > firstX); i++) {
            coordinates.add(buildCoordinate(position, calculateY(functionLaw, position), false));
            position = position + 1;
        }

        return coordinates;
    }

    private List<Coordinate> buildLowerCoordinates(double[][] cordinatesInput) {

        int lowerCoordinatesNumber = (21 - cordinatesInput.length) / 2;
        double firstX = cordinatesInput[0][0];
        double position = firstX - 1;

        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; (i < lowerCoordinatesNumber && position < firstX); i++) {
            coordinates.add(buildCoordinate(position, calculateY(functionLaw, position), false));
            position = position - 1;
        }

        Collections.reverse(coordinates);

        return coordinates;
    }


}