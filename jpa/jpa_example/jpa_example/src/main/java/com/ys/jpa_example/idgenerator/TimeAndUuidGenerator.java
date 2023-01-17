package com.ys.jpa_example.idgenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class TimeAndUuidGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
        throws HibernateException {

        String format = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        return String.format("%sYS!%s", format, UUID.randomUUID());
    }

}
