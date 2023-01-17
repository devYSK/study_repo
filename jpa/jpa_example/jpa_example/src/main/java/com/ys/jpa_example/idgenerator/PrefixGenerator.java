package com.ys.jpa_example.idgenerator;

import java.io.Serializable;
import java.util.Properties;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

@Slf4j
public class PrefixGenerator implements IdentifierGenerator {

    private String prefix;

    @Override
    public Serializable generate(
        SharedSessionContractImplementor session, Object obj)
        throws HibernateException {

        String query = String.format("select %s from %s",
            session.getEntityPersister(obj.getClass().getName(), obj)
                .getIdentifierPropertyName(),
            obj.getClass().getSimpleName());

        log.info("query : {} ", query);


        Stream ids = session.createQuery(query).stream();

        Long max = ids.map(o -> o.toString().replace(prefix + "-", ""))
            .mapToLong(value -> Long.parseLong(value.toString()))
            .max()
            .orElse(0L);

        return prefix + "-" + (max + 1);
    }

    @Override
    public void configure(Type type, Properties properties,
        ServiceRegistry serviceRegistry) throws MappingException {

        System.out.println("!!! 으악!!!");

        String prefix1 = ConfigurationHelper.getString("prefix", properties);
        System.out.println("프리픽스 !! : " + prefix1);

        prefix = properties.getProperty("prefix");
    }

}
