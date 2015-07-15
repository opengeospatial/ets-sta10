package org.opengis.cite.sta10;

import com.sun.jersey.api.client.Client;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import org.opengis.cite.sta10.util.ClientUtils;
import org.opengis.cite.sta10.util.XMLUtils;
import org.opengis.cite.sta10.util.TestSuiteLogger;
import org.opengis.cite.sta10.util.URIUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.w3c.dom.Document;

/**
 * A listener that performs various tasks before and after a test suite is run,
 * usually concerned with maintaining a shared test suite fixture. Since this
 * listener is loaded using the ServiceLoader mechanism, its methods will be
 * called before those of other suite listeners listed in the test suite
 * definition and before any annotated configuration methods.
 *
 * Attributes set on an ISuite instance are not inherited by constituent test
 * group contexts (ITestContext). However, suite attributes are still accessible
 * from lower contexts.
 *
 * @see org.testng.ISuite ISuite interface
 */
public class SuiteFixtureListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        processSuiteParameters(suite);
        registerClientComponent(suite);
    }

    @Override
    public void onFinish(ISuite suite) {
    }

    /**
     * Processes test suite arguments and sets suite attributes accordingly. The
     * entity referenced by the {@link TestRunArg#IUT iut} argument is parsed
     * and the resulting Document is set as the value of the "testSubject"
     * attribute.
     * 
     * @param suite
     *            An ISuite object representing a TestNG test suite.
     */
    void processSuiteParameters(ISuite suite) {
        Map<String, String> params = suite.getXmlSuite().getParameters();
        TestSuiteLogger.log(Level.CONFIG,
                "Suite parameters\n" + params.toString());
        String iutParam = params.get(TestRunArg.IUT.toString());
        if ((null == iutParam) || iutParam.isEmpty()) {
            throw new IllegalArgumentException(
                    "Required test run parameter not found: "
                            + TestRunArg.IUT.toString());
        }
        URI iutRef = URI.create(iutParam.trim());
        File entityFile = null;
        try {
            entityFile = URIUtils.dereferenceURI(iutRef);
        } catch (IOException iox) {
            throw new RuntimeException("Failed to dereference resource located at "
                    + iutRef, iox);
        }
        Document iutDoc = null;
        try {
            iutDoc = URIUtils.parseURI(entityFile.toURI());
        } catch (Exception x) {
            throw new RuntimeException("Failed to parse resource retrieved from "
                    + iutRef, x);
        }
        suite.setAttribute(SuiteAttribute.TEST_SUBJECT.getName(), iutDoc);
        if (TestSuiteLogger.isLoggable(Level.FINE)) {
            StringBuilder logMsg = new StringBuilder(
                    "Parsed resource retrieved from ");
            logMsg.append(iutRef).append("\n");
            logMsg.append(XMLUtils.writeNodeToString(iutDoc));
            TestSuiteLogger.log(Level.FINE, logMsg.toString());
        }
    }

    /**
     * A client component is added to the suite fixture as the value of the
     * {@link SuiteAttribute#CLIENT} attribute; it may be subsequently accessed
     * via the {@link org.testng.ITestContext#getSuite()} method.
     *
     * @param suite The test suite instance.
     */
    void registerClientComponent(ISuite suite) {
        Client client = ClientUtils.buildClient();
        if (null != client) {
            suite.setAttribute(SuiteAttribute.CLIENT.getName(), client);
        }
    }
}
