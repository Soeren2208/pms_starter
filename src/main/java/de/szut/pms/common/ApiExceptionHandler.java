package de.szut.pms.common;

import java.util.LinkedHashMap;
import java.util.Map;

import de.szut.pms.common.exceptions.CustomerNotFoundException;
import de.szut.pms.common.exceptions.EmployeeAlreadyExistsInProjectException;
import de.szut.pms.common.exceptions.EmployeeNotFoundException;
import de.szut.pms.common.exceptions.ProjectAlreadyExistsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import de.szut.pms.hello.HelloNotFoundException;

/**
 * Zentrale Übersetzung von Ausnahmen in HTTP-Antworten nach RFC 9457
 * (ProblemDetail). Eine Methode je Ausnahmetyp, wie in Tutorial 4, AB 03.
 * <p>
 * Ergänze hier eigene {@code @ExceptionHandler}-Methoden für die fachlichen
 * Ausnahmen deiner Ressourcen — nach demselben Muster wie
 * {@link # handleNotFound}.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {

    @ExceptionHandler(ProjectAlreadyExistsException.class)
    public ProblemDetail handleAlreadyExists(ProjectAlreadyExistsException ex) {
        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Projekt existiert bereits.");

        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Die Anfrage enthält ungültige Felder.");
        problem.setTitle("Ungültige Eingabe");
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ProblemDetail handleEmployeeNotExists(EmployeeNotFoundException ex) {
        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Mitarbeitenden-ID existiert nicht.");

        return problem;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleCustomerNotExists(CustomerNotFoundException ex) {
        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Kunden-ID existiert nicht.");

        return problem;
    }

    @ExceptionHandler(EmployeeAlreadyExistsInProjectException.class)
    public ProblemDetail handleEmployeeAlreadyExistsInProject(EmployeeAlreadyExistsInProjectException ex) {
        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Mitarbeiter*in arbeitet bereits am Projekt.");

        return problem;
    }
/*
    @ExceptionHandler(HelloNotFoundException.class)
    public ProblemDetail handleNotFound(HelloNotFoundException ex) {
        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Nicht gefunden");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Die Anfrage enthält ungültige Felder.");
        problem.setTitle("Ungültige Eingabe");
        problem.setProperty("errors", errors);
        return problem;
    }*/
}
