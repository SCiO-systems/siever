package org.siever.server;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;
import org.apache.camel.spi.PropertiesComponent;
import org.apache.camel.support.DefaultRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.grobid.core.engines.Engine;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.main.GrobidHomeFinder;
import org.grobid.core.utilities.GrobidProperties;
import org.siever.routes.SieveRouter;

import java.util.Arrays;

public class Siever extends Main {

    private static final Logger logger = LogManager.getLogger("org.siever.server");

    public static void main(String[] args){
        CamelContext camelContext = new DefaultCamelContext();
        Siever siever = new Siever();

        PropertiesComponent properties = camelContext.getPropertiesComponent();
        properties.setLocation("classpath:/env.properties");

        DefaultRegistry registry = (DefaultRegistry) camelContext.getRegistry();
        ((org.apache.camel.impl.DefaultCamelContext) camelContext).setRegistry(registry);

        String destPath = properties.loadPropertiesAsMap().get("DESTINATION_PATH").toString();
        registry.bind("destination", destPath);

        String pGrobidHome = properties.loadPropertiesAsMap().get("GROBID_HOME_PATH").toString();
//        logger.error(pGrobidHome);
        GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(pGrobidHome));
        GrobidProperties.getInstance(grobidHomeFinder);

        // Create the GROBID engine and bind it to the registry
        Engine engine = GrobidFactory.getInstance().createEngine();
        registry.bind("engine", engine);

        try {
            camelContext.addRoutes(new SieveRouter());
            camelContext.start();
            ProducerTemplate template = camelContext.createProducerTemplate();
            siever.run();
            // ((DefaultCamelContext) camelContext).startAllRoutes();
        } catch (Exception e) {
            logger.error("Siever failed: {}",e.getMessage());
        }

    }

}
