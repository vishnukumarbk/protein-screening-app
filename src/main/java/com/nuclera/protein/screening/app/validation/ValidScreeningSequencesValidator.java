package com.nuclera.protein.screening.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Objects;

public class ValidScreeningSequencesValidator implements ConstraintValidator<ValidScreeningSequences, List<String>> {

    private static final int SEQUENCE_COUNT = 20;
    private static final int MIN_COMMON_LENGTH = 5;
    private static final int MIN_VARIABLE_REGION_LENGTH = 2;
    private static final int MAX_VARIABLE_REGION_LENGTH = 9;

    @Override
    public boolean isValid(final List<String> sequences, final ConstraintValidatorContext context) {

        if (sequences == null || sequences.stream().anyMatch(Objects::isNull)) {
            return true;
        }
        if (sequences.size() != SEQUENCE_COUNT) {
            return reject(context, "must contain exactly " + SEQUENCE_COUNT + " sequences");
        }

        final int prefixLength = commonPrefixLength(sequences);
        if (prefixLength < MIN_COMMON_LENGTH) {
            return reject(context, "must share a common prefix of at least " + MIN_COMMON_LENGTH + " amino acids");
        }

        final int postfixLength = commonPrefixLength(sequences.stream()
                .map(ValidScreeningSequencesValidator::reverse)
                .toList());
        if (postfixLength < MIN_COMMON_LENGTH) {
            return reject(context, "must share a common postfix of at least " + MIN_COMMON_LENGTH + " amino acids");
        }

        // Measured between the longest common prefix and postfix, which overlap (a negative length) for identical sequences
        final boolean variableRegionsValid = sequences.stream()
                .mapToInt(sequence -> sequence.length() - prefixLength - postfixLength)
                .allMatch(length -> length >= MIN_VARIABLE_REGION_LENGTH && length <= MAX_VARIABLE_REGION_LENGTH);
        if (!variableRegionsValid) {
            return reject(context, "must have " + MIN_VARIABLE_REGION_LENGTH + " to " + MAX_VARIABLE_REGION_LENGTH
                    + " amino acids between the common prefix and postfix of every sequence");
        }
        return true;
    }

    private static int commonPrefixLength(final List<String> sequences) {
        final String first = sequences.getFirst();
        final int shortest = sequences.stream().mapToInt(String::length).min().orElse(0);
        for (int i = 0; i < shortest; i++) {
            final int index = i;
            if (sequences.stream().anyMatch(sequence -> sequence.charAt(index) != first.charAt(index))) {
                return i;
            }
        }
        return shortest;
    }

    private static String reverse(final String sequence) {
        return new StringBuilder(sequence).reverse().toString();
    }

    private static boolean reject(final ConstraintValidatorContext context, final String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}
