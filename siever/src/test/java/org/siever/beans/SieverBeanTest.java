package org.siever.beans;

import org.apache.camel.*;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.PropertiesComponent;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultRegistry;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.grobid.core.engines.config.GrobidAnalysisConfig;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.main.GrobidHomeFinder;
import org.grobid.core.utilities.GrobidProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.grobid.core.engines.Engine;
import org.junit.Before;
//import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.siever.models.InputJob;
import org.siever.models.OutputJob;
import org.siever.models.Result;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;


class SieverBeanTest {

    private CamelContext ctx;

    Exchange setUpTheTestEnv () {
        this.ctx = new DefaultCamelContext();

        PropertiesComponent properties = ctx.getPropertiesComponent();
        properties.setLocation("classpath:/env.properties");

        DefaultRegistry registry = (DefaultRegistry) ctx.getRegistry();
        ((org.apache.camel.impl.DefaultCamelContext) ctx).setRegistry(registry);

        String destPath = properties.loadPropertiesAsMap().get("DESTINATION_PATH").toString();
        registry.bind("destination", destPath);

        String pGrobidHome = properties.loadPropertiesAsMap().get("GROBID_HOME_PATH").toString();
//        logger.error(pGrobidHome);
        GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(pGrobidHome));
        GrobidProperties.getInstance(grobidHomeFinder);

        Engine engine = GrobidFactory.getInstance().createEngine();
        registry.bind("engine", engine);

        Exchange ex = new DefaultExchange(ctx);

        return ex;
    }

    @Test
    void sieveEmptyInputURLSHouldThrowGrobidException() throws Exception {
        InputJob input = Instancio.create(InputJob.class);
        input.setUrl("");

        Exchange ex = setUpTheTestEnv();
        ex.getIn().setBody(input, InputJob.class);

        SieverBean sieverBean = new SieverBean();
        assertThrows(org.grobid.core.exceptions.GrobidException.class, () -> {
            sieverBean.sieve(ex);
        });
    }

    @Test
    void sieveNoPDFHeaderReturned() throws Exception {
        InputJob input = Instancio.create(InputJob.class);
        input.setUrl("https://www.sfu.ca/~wainwrig/Econ400/documents/460-TERM-PAPER-EXAMPLE.pdf");

        Exchange ex = setUpTheTestEnv();
        ex.getIn().setBody(input, InputJob.class);

        SieverBean sieverBean = new SieverBean();
        sieverBean.sieve(ex);

        assertEquals(ex.getIn().getHeader("pdfPath"), null);
    }

    @Test
    void sievePDFDeleted() throws Exception {
        InputJob input = Instancio.create(InputJob.class);
        input.setUrl("https://www.sfu.ca/~wainwrig/Econ400/documents/460-TERM-PAPER-EXAMPLE.pdf");

        Exchange ex = setUpTheTestEnv();
        ex.getIn().setBody(input, InputJob.class);

        SieverBean sieverBean = new SieverBean();
        sieverBean.sieve(ex);

        String pdfPath = "/home/anastasis/Desktop/downloadedpdfs";
        pdfPath = pdfPath + input.getId() + ".pdf";

        File f = new File(pdfPath);
        assertFalse(f.exists());
    }

//    @Test
//    void sievePDFGetsDownloaded() throws Exception {
//        InputJob input = Instancio.create(InputJob.class);
//        input.setUrl("https://www.sfu.ca/~wainwrig/Econ400/documents/460-TERM-PAPER-EXAMPLE.pdf");
//
//        Exchange ex = setUpTheTestEnv();
//        ex.getIn().setBody(input, InputJob.class);
//
//        SieverBean sieverBean = new SieverBean();
//        sieverBean.sieve(ex);
//
//        String dest = ex.getContext().getRegistry()
//                .lookupByNameAndType("destination", String.class);
//
//        String fileName = input.getId() + ".pdf";
//
//        String pdfPath = dest + "/" + fileName;
//
//        File f = new File(pdfPath);
//        assertEquals(f.exists(), true);
//    }

    @Test
    public void outPutMessageBodyTest () throws Exception {
        InputJob input = Instancio.create(InputJob.class);
        input.setUrl("https://files.eric.ed.gov/fulltext/EJ1172284.pdf");

        Exchange ex = setUpTheTestEnv();
        ex.getIn().setBody(input, InputJob.class);

        SieverBean sieverBean = new SieverBean();
        sieverBean.sieve(ex);

        OutputJob outBody = new OutputJob();
        String S3FileName = ex.getIn().getBody(Result.class).getMetadata().getId();
        outBody.setS3FileName(S3FileName);

        sieverBean.outputMessage(ex);
        String retbody = ex.getIn().getBody(OutputJob.class).getS3FileName();

        assertEquals(retbody, outBody.getS3FileName());
    }


    @Test
    void outputMessage() {
    }
}