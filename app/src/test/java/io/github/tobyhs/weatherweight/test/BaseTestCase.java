package io.github.tobyhs.weatherweight.test;

import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

/**
 * Base abstract test class to hold common hooks
 */
public abstract class BaseTestCase {
    @Rule public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
}
