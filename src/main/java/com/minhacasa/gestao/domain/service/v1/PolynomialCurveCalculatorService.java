package com.minhacasa.gestao.domain.service.v1;

import com.minhacasa.gestao.api.v1.controller.dtos.Chart;
import com.minhacasa.gestao.api.v1.controller.dtos.Coordinate;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.minhacasa.gestao.api.v1.controller.dtos.Coordinate.buildCoordinate;
import static com.minhacasa.gestao.domain.utils.NumberUtils.round;

@Service
public class PolynomialCurveCalculatorService {

    private static  String functionLaw;

    public PolynomialCurveCalculatorService() {
    }

    public Chart save(double[][] cordinatesInput) {

        int polynomialDegree = cordinatesInput.length - 1;
        double[] coefficients = calcularFuncao(cordinatesInput, polynomialDegree);
        functionLaw = buildFunctionLaw(polynomialDegree, coefficients);

        List<Coordinate> coordinates = new ArrayList<>();
        for (int i= 0; i < cordinatesInput.length; i++) {
            double x = cordinatesInput[i][0];

            double y = calculateY(functionLaw, x);
            System.out.println("x = " + x + " | y = " + y );
            coordinates.add(buildCoordinate(x, y,true));
        }

        List<Coordinate> lowerCoordinates = buildLowerCoordinates(cordinatesInput);
        List<Coordinate> superiorCoordinates = superiorCoordinates(cordinatesInput);

        List<Coordinate> combinedList = new ArrayList<>(lowerCoordinates);
        combinedList.addAll(coordinates);
        combinedList.addAll(superiorCoordinates);

        return new Chart(combinedList, "f(x) = " + functionLaw);
    }

    private double calculateY(String formula, double x) {
        Map<String, Double> variables = new HashMap<String, Double>() {};
        variables.put("x", x);

        Set<String> nameOfVariables = new HashSet<>();
        variables.forEach((variable, coeficient) -> nameOfVariables.add(variable));

        double y = new ExpressionBuilder(formula)
                .variables(nameOfVariables)
                .build()
                .setVariables(variables)
                .evaluate();

        return round(y);
    }

    private String buildFunctionLaw(int polynomialDegree, double[] coefficients) {
//        StringBuilder functionLaw = new StringBuilder("f(x) = ");
        StringBuilder functionLaw = new StringBuilder();

        for (int i = 0; i <= polynomialDegree; i++) {
            if (i == 0)
                functionLaw.append(coefficients[i]);
            else
                functionLaw.append(" + ").append(coefficients[i]).append(" * x^").append(i);
        }
        return functionLaw.toString();
    }


    private static double[] calcularFuncao(double[][] coordenadas, int grauPolinomio) {
        WeightedObservedPoints pontosObservados = new WeightedObservedPoints();
        for (double[] coordenada : coordenadas) {
            double x = coordenada[0];
            double y = coordenada[1];
            pontosObservados.add(x, y);
        }

        // Realizar o ajuste de curva polinomial
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(grauPolinomio);
        return fitter.fit(pontosObservados.toList());
    }

    private void buildCoordinatesCurve(double[][] coordinatesInput, String functionLaw) {
//        List<Coordinate> coordinates = new ArrayList<>();
//        double minValue = Double.MAX_VALUE;
//        double maxValue = Double.MIN_VALUE;
//
//        for (double[] row : coordinatesInput) {
//            for (double value : row) {
//                if (value < minValue) {
//                    minValue = (int) value;
//                }
//            }
//        }
//
//        for (double[] row : coordinatesInput) {
//            for (double value : row) {
//                if (value > maxValue) {
//                    maxValue = (int) value;
//                }
//            }
//        }
//
//        minValue = minValue * -7;
//        maxValue = maxValue * 7;
//
//        for (double i = minValue; i <= coordinatesInput.length; i = i + 5) {
//            coordinates.add(Coordinate.build());
//        }
    }

    private List<Coordinate> superiorCoordinates(double[][] cordinatesInput) {

        int lowerCoordinatesNumber = (21 - cordinatesInput.length) / 2;
        double firstX = cordinatesInput[cordinatesInput.length-1][0];
        double position = firstX+1;

        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; (i < lowerCoordinatesNumber && position > firstX); i++) {
            coordinates.add(buildCoordinate(position, calculateY(functionLaw, position),false));
            position = position+1;
        }

        return coordinates;
    }

    private List<Coordinate> buildLowerCoordinates(double[][] cordinatesInput) {

        int lowerCoordinatesNumber = (21 - cordinatesInput.length) / 2;
        double firstX = cordinatesInput[0][0];
        double position = firstX-1;

        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; (i < lowerCoordinatesNumber && position < firstX); i++) {
            coordinates.add(buildCoordinate(position, calculateY(functionLaw, position),false));
            position = position-1;
        }

        Collections.reverse(coordinates);

        return coordinates;
    }


}