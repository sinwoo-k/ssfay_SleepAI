package com.c208.sleephony.global.utils;

import com.c208.sleephony.domain.sleep.dto.request.RawSequenceRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EqualLengthListsValidator
        implements ConstraintValidator<EqualLengthLists, RawSequenceRequest> {

    @Override
    public boolean isValid(RawSequenceRequest req, ConstraintValidatorContext ctx) {
        if (req == null) return true;          // @NotNull 로 따로 검사
        int len = req.getAccX().size();
        return len == req.getAccY().size() &&
                len == req.getAccZ().size() &&
                len == req.getTemp().size() &&
                len == req.getHr().size();
    }
}