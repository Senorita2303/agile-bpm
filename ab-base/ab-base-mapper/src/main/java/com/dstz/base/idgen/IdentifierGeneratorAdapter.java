package com.dstz.base.idgen;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.dstz.base.common.idgen.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * Adapting Mybatis Plus generator
 *
 */
@Component
public class IdentifierGeneratorAdapter implements IdentifierGenerator {

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public Number nextId(Object entity) {
        return new BigInteger(idGenerator.nextId());
    }

    @Override
    public String nextUUID(Object entity) {
        return idGenerator.nextId();
    }
}
