package sg.edu.nus.validation.tutorial;

import jakarta.validation.groups.Default;


/*
 * Validation group used for update operations.
 *
 * Extending Default means the normal validation
 * constraints are also included when Update
 * validation is requested.
 */
public interface Update
        extends Default {
}