package com.dstz.base.common.ext;

import java.util.List;

/**
 * Enumeration extension data loading
 *
 */
public interface IEnumExtraDataLoader {

    /**
     * Tags used to distinguish extensions under specific enumerations
     *
     * @return Extending Enumeration Classes
     */
    Class<?> tag();

    /**
     * Loading extended enumeration data
     *
     * @return Extended Enumeration Data
     */
    List<EnumExtraData> load();
}
