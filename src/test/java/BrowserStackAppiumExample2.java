//https://www.browserstack.com/list-of-browsers-and-platforms?product=automate

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.FixedCutProvider;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

@RunWith(Parallell.class)
public class BrowserStackAppiumExample2 {

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    protected String browser;
    protected String os;
    //protected String version;
    protected String deviceName;
    protected String deviceOrientation;

    public static String username = "justin646";
    public static String accesskey = "Vu85HDQC5BxNZx7iWxa8";
    public static String applitoolsKey = "9RkMajXrzS1Zu110oTWQps102CHiPRPmeyND99E9iL0G7yAc110";

    @Parameterized.Parameters
    public static LinkedList getEnvironments() throws Exception {
        LinkedList env = new LinkedList();
//        env.add(new String[]{"Android", "6",    "Samsung Galaxy S8", "chrome", "portrait"});
//        env.add(new String[]{"iPhone",  "10.0", "iPhone 7 Plus",     "Safari", "portrait"});
        env.add(new String[]{"iPhone",  "10.0", "iPhone 7",          "Safari", "portrait"});

        return env;
    }

    public BrowserStackAppiumExample2(String os, String version, String deviceName, String browser,
                                      String deviceOrientation) {
        this.os = os;
        //this.version = version;
        this.deviceName = deviceName;
        this.browser = browser;
        this.deviceOrientation = deviceOrientation;
    }

    private Eyes eyes = new Eyes();
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        eyes.setApiKey(applitoolsKey);
        eyes.setHideScrollbars(true);
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setMatchLevel(MatchLevel.LAYOUT2);

        //eyes.setSaveFailedTests(false);

        BatchInfo batch = new BatchInfo("NYF");
        eyes.setBatch(batch);

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability(CapabilityType.PLATFORM, os);
        capability.setCapability(CapabilityType.BROWSER_NAME, browser);
        //capability.setCapability(CapabilityType.VERSION, version);
        capability.setCapability("deviceName", deviceName);
        capability.setCapability("device-orientation", deviceOrientation);
        capability.setCapability("name", name.getMethodName());
        capability.setCapability("realMobile", true); //Set for real devices on BS.
        capability.setCapability("browserstack.appium_version", "1.6.3");

        if (browser == "Safari") {
            //lower scale gets wider. higher scale gets thinner
            eyes.setScaleRatio(1.0); //scale the device image to appropriate size. 0.65
            eyes.setImageCut(new FixedCutProvider(63,135,0,0)); //remove URL and footer.
        }

        String browserStackUrl = "http://" + username + ":" + accesskey + "@hub-cloud.browserstack.com/wd/hub";
        driver = new RemoteWebDriver(new URL(browserStackUrl), capability);
        driver.get("https://www.newyorkfed.org/outreach-and-education");
    }

    @Test
    public void NYFHomePage() throws Exception {
        eyes.open(driver, "NYF", "Home Page");
        eyes.checkWindow("Home Page Screenshot");
        TestResults results = eyes.close();
        assertEquals(true, results.isPassed());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        eyes.abortIfNotClosed();
    }
}