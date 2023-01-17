package com.ys.jpa_example.idgenerator;

import java.io.Serializable;
import java.util.Random;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class RandomIdGenerator implements IdentifierGenerator {

    // hibernate 6에서는 Serializable 반환 타입이 아닌 Object 반환 타입이다.
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
        throws HibernateException {
        Random random = new Random();
        int randomId = random.nextInt(10000);
        return (long) randomId;
    }

}
