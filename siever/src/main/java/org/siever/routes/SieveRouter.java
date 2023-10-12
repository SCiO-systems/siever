package org.siever.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.siever.beans.TeiExtractorBean;
import org.siever.beans.UrlDownloaderBean;

public class SieveRouter extends RouteBuilder {

    public void configure() throws Exception {

        from ("direct:sieve")
                .routeId("siever")
                .bean(UrlDownloaderBean.class, "pdfPath")
                .bean(TeiExtractorBean.class, "extractTei");

    }

}
