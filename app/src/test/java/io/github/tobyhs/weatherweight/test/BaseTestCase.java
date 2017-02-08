package io.github.tobyhs.weatherweight.test;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Base abstract test class to hold common hooks
 */
public abstract class BaseTestCase {
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
}
