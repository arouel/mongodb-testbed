package prototype.testsupport;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * A junit extension that can be used similar to a ExternalResource from junit4. Use it like that in a junit class
 * ({@link #before(ExtensionContext)} and {@link #after(ExtensionContext)} are invoked before/after each test):
 * <p>
 * <code>
 * &#64;RegisterExtension MyRule _rule = new MyRule<>(someParameter);
 * </code>
 * </p>
 *
 * Or like that ({@link #before(ExtensionContext)} and {@link #after(ExtensionContext)} are invoked before/after all
 * test):
 * <p>
 * <code>
 * &#64;RegisterExtension static MyRule _rule = new MyRule<>(someParameter);
 *    </code>
 * </p>
 *
 */
abstract class ExternalResource implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private boolean _beforeAllInvoked;

    protected abstract void after(ExtensionContext context) throws Exception;

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (_beforeAllInvoked) {
            after(context);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (!_beforeAllInvoked) {
            after(context);
        }
    }

    protected abstract void before(ExtensionContext context) throws Exception;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        _beforeAllInvoked = true;
        before(context);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (!_beforeAllInvoked) {
            before(context);
        }
    }

}
