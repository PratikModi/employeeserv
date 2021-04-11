package com.paypal.bfs.test.employeeserv.validate;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class RequestValidator {
    public static final String FIRST_NAME = "First Name";
    public static final String LAST_NAME = "Last Name";
    public static final String DATE_OF_BIRTH = "Date of Birth";
    public static final String ADDRESS = "Address";
    public static final String ADDRESS_LINE_1 = "Address Line 1";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String ZIP_CODE = "zip code";
    public static final String COUNTRY = "country";
    public static final int MAX_LENGTH = 255;
    public static final String REQUIRED = "This is mandatory field";

    public Optional<List<ValidationError>> getValidationErrors(Employee employee) {
        List<ValidationError> errorsList = new ArrayList<>();

        checkForRequired(employee, errorsList);

        checkForLength(employee, errorsList);

        checkDateFormat(employee.getDateOfBirth(),errorsList);

        return errorsList.size() > 0 ? Optional.of(errorsList) : Optional.empty();

    }

    private void checkForLength(Employee employeeRequest, List<ValidationError> errorsList) {
        if(isMaxLength(employeeRequest.getFirstName(),MAX_LENGTH)){
            errorsList.add(ValidationError.builder().field(FIRST_NAME).message("Max length is "+MAX_LENGTH).build());
        }
        if(isMaxLength(employeeRequest.getLastName(),MAX_LENGTH)){
            errorsList.add(ValidationError.builder().field(LAST_NAME).message("Max length is "+MAX_LENGTH).build());
        }
        if(Objects.nonNull(employeeRequest.getAddress()) && isMaxLength(employeeRequest.getAddress().getLine1(),MAX_LENGTH)){
            errorsList.add(ValidationError.builder().field(ADDRESS_LINE_1).message("Max length is "+MAX_LENGTH).build());
        }
        if(Objects.nonNull(employeeRequest.getAddress()) && isMaxLength(employeeRequest.getAddress().getLine2(),MAX_LENGTH)){
            errorsList.add(ValidationError.builder().field("Address Line 2").message("Max length is "+MAX_LENGTH).build());
        }
        if(Objects.nonNull(employeeRequest.getAddress()) && isMaxLength(employeeRequest.getAddress().getCountry(),MAX_LENGTH)){
            errorsList.add(ValidationError.builder().field(COUNTRY).message("Max length is "+MAX_LENGTH).build());
        }
        if(Objects.nonNull(employeeRequest.getAddress()) && isMaxLength(employeeRequest.getAddress().getState(),MAX_LENGTH)){
            errorsList.add(ValidationError.builder().field(STATE).message("Max length is "+MAX_LENGTH).build());
        }
        if(Objects.nonNull(employeeRequest.getAddress()) && isMaxLength(employeeRequest.getAddress().getZipCode(),10)){
            errorsList.add(ValidationError.builder().field(ZIP_CODE).message("Max length is "+10).build());
        }
    }

    private void checkDateFormat(String dateOfBirth,List<ValidationError> errorsList){
        try{
            LocalDate dob = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch (Exception e){
            errorsList.add(ValidationError.builder().field(DATE_OF_BIRTH).message("Incorrect date format. Expected format: yyyy-MM-dd").build());
        }
    }

    private boolean isMaxLength(String value,int maxLength) {
        return !isEmpty(value) && value.length() > maxLength;
    }

    private void checkForRequired(Employee employeeRequest, List<ValidationError> errorsList) {

        if(isEmpty(employeeRequest.getFirstName())){
            errorsList.add(ValidationError.builder().field(FIRST_NAME).message(REQUIRED).build());
        }

        if(isEmpty(employeeRequest.getLastName())){
            errorsList.add(ValidationError.builder().field(LAST_NAME).message(REQUIRED).build());
        }

        if(isEmpty(employeeRequest.getDateOfBirth())){
            errorsList.add(ValidationError.builder().field(DATE_OF_BIRTH).message(REQUIRED).build());
        }

        if(Objects.isNull(employeeRequest.getAddress())){
            errorsList.add(ValidationError.builder().field(ADDRESS).message(REQUIRED).build());
        }

        if(Objects.nonNull(employeeRequest.getAddress()) && isEmpty(employeeRequest.getAddress().getLine1())){
            errorsList.add(ValidationError.builder().field(ADDRESS_LINE_1).message(REQUIRED).build());
        }

        if(Objects.nonNull(employeeRequest.getAddress()) && isEmpty(employeeRequest.getAddress().getCity())){
            errorsList.add(ValidationError.builder().field(CITY).message(REQUIRED).build());
        }
        if(Objects.nonNull(employeeRequest.getAddress()) && isEmpty(employeeRequest.getAddress().getState())){
            errorsList.add(ValidationError.builder().field(STATE).message(REQUIRED).build());
        }
        if(Objects.nonNull(employeeRequest.getAddress()) && isEmpty(employeeRequest.getAddress().getZipCode())){
            errorsList.add(ValidationError.builder().field(ZIP_CODE).message(REQUIRED).build());
        }
        if(Objects.nonNull(employeeRequest.getAddress()) && isEmpty(employeeRequest.getAddress().getCountry())){
            errorsList.add(ValidationError.builder().field(COUNTRY).message(REQUIRED).build());
        }
    }


    private boolean isEmpty(String value){
        return null == value;
    }
}
