package org.opengis.cite.sta10.readEntities;

import org.opengis.cite.sta10.SuiteAttribute;
import org.opengis.cite.sta10.util.TestSuiteLogger;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.w3c.dom.Document;

import java.util.logging.Level;

/**
 * Checks that various preconditions are satisfied before the test suite is run.
 * If any of these (BeforeSuite) methods fail, all tests are skipped.
 */
public class SuitePreconditions {

    /**
     * Verifies that a service capabilities document was supplied as a test run
     * argument and that the implementation it describes is available.
     *
     * @param testContext Information about the (pending) test run.
     */
    @BeforeSuite
    public void verifyTestSubject(ITestContext testContext) {
        Object sutObj = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName());
        if (null != sutObj && Document.class.isInstance(sutObj)) {
            // TODO: Verify test subject
        } else {
            String msg = String.format(
                    "Value of test suite attribute %s is missing or is not a DOM Document.",
                    SuiteAttribute.TEST_SUBJECT.getName());
            TestSuiteLogger.log(Level.SEVERE, msg);
            throw new AssertionError(msg);
        }
    }

}
