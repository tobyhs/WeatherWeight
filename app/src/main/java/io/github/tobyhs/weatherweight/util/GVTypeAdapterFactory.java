package io.github.tobyhs.weatherweight.util;

import com.google.gson.TypeAdapterFactory;

import me.tatarka.gsonvalue.annotations.GsonValueTypeAdapterFactory;

/**
 * A Gson type adapter factory for our classes that use GsonValue
 */
@GsonValueTypeAdapterFactory
public abstract class GVTypeAdapterFactory implements TypeAdapterFactory {
    /**
     * @return a new {@link GVTypeAdapterFactory}
     */
    public static GVTypeAdapterFactory create() {
        return new GsonValue_GVTypeAdapterFactory();
    }
}
