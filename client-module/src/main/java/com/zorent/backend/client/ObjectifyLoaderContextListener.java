package com.zorent.backend.client;

import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;
import com.zorent.backend.common.OfyService;
import com.zorent.backend.common.entities.Coupon;
import com.zorent.backend.common.entities.Customer;
import com.zorent.backend.common.entities.DeliveryStaff;
import com.zorent.backend.common.entities.Order;

import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This class processes the classpath for classes with the @Entity or @Subclass annotations from Objectify
 * and registers them with the ObjectifyFactory, it is multi-threaded uses a prebuilt list of classes to process
 * created by the Reflections library at compile time and works very fast!
 */
public class ObjectifyLoaderContextListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(ObjectifyLoaderContextListener.class.getName());

    private final Class[] toInitialize = new Class[]{

            Customer.class,
            Coupon.class,
            Order.class,
            DeliveryStaff.class
    };

    @Override
    public void contextInitialized(@Nonnull final ServletContextEvent sce) {

        final ObjectifyFactory of = OfyService.ofyFactory();
        JodaTimeTranslators.add(of); //add support for joda-time

        for (final Class<?> cls : toInitialize) {
            of.register(cls);
            logger.info("Registered {" + cls + "} with Objectify");
        }
    }

    @Override
    public void contextDestroyed(@Nonnull final ServletContextEvent sce) {
        /* this is intentionally empty */
    }
}