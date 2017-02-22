package io.github.tobyhs.weatherweight.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

/**
 * Scope for instances which live as long as the activity
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Scope
public @interface ActivityScoped {
}
