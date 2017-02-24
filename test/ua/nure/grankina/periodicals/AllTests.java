package ua.nure.grankina.periodicals;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ua.nure.grankina.periodicals.web.filters.*;
import ua.nure.grankina.periodicals.web.listeners.ListenerSessionTest;
import ua.nure.grankina.periodicals.web.strategy.RechargeActionTest;

/**
 * Created by USER-PC on 18.01.2017.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({UserTest.class
        , SetPublicKeyActionTest.class
        , NoActionTest.class
        , PageRedirectActionTest.class
        , FilterAdminTest.class
        , FilterBlockedUsersTest.class
        , FilterEncodeRequestTest.class
        , FilterProtectedTest.class
        , FilterValidateTest.class
        , FilterClearSessionTest.class
        , ListenerSessionTest.class
        , FilterPersistentSessionTest.class
        , FilterSessionSyncTest.class
        , RechargeActionTest.class
})

public class AllTests {
}
