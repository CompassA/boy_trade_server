package org.study.validation;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author fanqie
 * Created on 2020/1/11
 */
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    @Override
    public void afterPropertiesSet() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public ValidationResult validate(final Object bean) {
        final ValidationResult result = new ValidationResult();
        final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean);
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            result.setHasErrorsTrue();
            constraintViolations.forEach(constraintViolation -> result.addErrMsg(
                    constraintViolation.getPropertyPath().toString(),
                    constraintViolation.getMessage()));
        }
        return result;
    }
}
