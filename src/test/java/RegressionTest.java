import app.frontend.screens.LoginScreen;
import app.frontend.screens.MainScreen;
import app.frontend.screens.StartScreen;
import static core.mt.utils.PropertyLoader.getProperty;
import core.mt.ProjectPackages;
import core.screen_driver.DriverFactory;
import core.mt.rest.APIClient;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;
import java.util.List;
import static core.mt.utils.ValuesUtils.getDoubleValue;
import static org.testng.Assert.*;


public class RegressionTest extends BaseTest{

    private StartScreen start;
    private LoginScreen login;
    private MainScreen main;
    private static String instrument_set = getProperty("instrument.set");
    private static String group = getProperty("group.set");
    private static final String VOLUME_DEFAULT = getProperty("volume.default");
    private static final String VOLUME_SET = getProperty("volume.set");
    private String tp_set;
    private String sl_set;
    private String opened_on;
    private String order_number = null;
    private double open_price;
    private final String OPERATION_TYPE = getProperty("operation.type");
    private boolean order_will_not_be_closed = false;
    private String asset_leverage;
    private static double default_volume_units = getDoubleValue(VOLUME_DEFAULT)*100;

    public static void setPreconditionData(){
        if(ProjectPackages.INCEPTIAL.getValueAsList().contains(DriverFactory.package_name)){
            group = "CRYPTO_1";
            instrument_set = getProperty("instrument.set.inceptial");
            default_volume_units = getDoubleValue(VOLUME_DEFAULT);
        }
    }

    @Test
    public void checkStartScreenIsOpen(){
        logger.debug("test starts - " + DriverFactory.project_name);
        start = new StartScreen(driver);
        assertTrue(start.waitIsScreenLoaded());
        logger.debug("Start screen is open");
    }

    @Test(dependsOnMethods = {"checkStartScreenIsOpen"})
    public void openLoginScreen(){
        assertTrue(start.clickLoginButton());
        login = new LoginScreen(driver);
        assertTrue(login.waitIsScreenLoaded());
        logger.debug("Login screen is open");
    }

    @Test(dependsOnMethods = {"openLoginScreen"})
    public void makeLogin(){
        assertTrue(login.makeLogin());
        main = new MainScreen(driver);
        assertTrue(main.waitIsScreenLoaded());
        logger.debug("Main screen is open");
    }

    @Test(priority = 1, dependsOnMethods = {"makeLogin"})
    public void setGroup() {
        setPreconditionData();
        main.selected_group_name.click();
        main.swipeToDirection_iOS_XCTest(main.getElement(group),"d");
        assertEquals(main.selected_group_name.getText(), group);
        logger.debug("Group: " + group + " is set");
    }

    @Test(dependsOnMethods = {"setGroup"})
    public void setInstrument() {
        if (!main.default_instrument.getText().equals(instrument_set)){
            main.default_instrument.click();
            main.swipeToDirection_iOS_XCTest(main.getElement(instrument_set),"d");
        }
        assertEquals(main.default_instrument.getText(), instrument_set);
        logger.debug("Instrument: " + instrument_set + " is set");
    }

    @Test(dependsOnMethods = {"setInstrument"})
    public void checkInstrumentIsAvailable(){
        assertTrue(main.waitIsElementVisible(main.BUY_trade));
    }

    @Test(dependsOnMethods = {"checkInstrumentIsAvailable"})
    public void openPositionOpeningDialog() {
        main.tapElement_iOS_XCTest(main.BUY_trade);
        assertTrue(main.waitIsElementVisible(main.volume_lots));
        logger.debug("Position opening dialog is open");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void checkInstrument() {
        assertEquals(main.instrument_set.getText(), instrument_set);
        logger.debug("Instrument is correct");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void checkDefaultVolumeLots(){
        String volumeLots = main.volume_lots.getText();
        double actualVolume = getDoubleValue(volumeLots);
        double expectedVolume = getDoubleValue(VOLUME_DEFAULT);
        assertEquals(actualVolume,expectedVolume);
        logger.debug("Default volume lots is correct");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void checkDefaultVolumeUnits(){
        assertEquals(getDoubleValue(main.volume_units.getText()), default_volume_units);
        logger.debug("Default volume units is correct");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"}, enabled = true)
    public void checkAssetLeverage(){
        asset_leverage = String.valueOf(APIClient.getAssetLeverage(DriverFactory.package_name));
        assertEquals(main.asset_leverage.getText(), "1:" + asset_leverage);
        logger.debug("Asset leverage is correct");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void checkSwitcherTpIsNotChecked(){
        assertEquals(main.Close_at_Profit.getAttribute("value"),"0");
        logger.debug("TP is disabled");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void checkSwitcherSlIsNotChecked(){
        assertEquals(main.Close_at_Loss.getAttribute("value"),"0");
        logger.debug("SL is disabled");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void openNewPosition() {
        String decimalSeparator = ".";
        if(main.volume_lots.getText().contains(",")){
            decimalSeparator = ",";
        }
        main.waitClickClearEnterDataInField(main.volume_lots, VOLUME_SET.replace(".",decimalSeparator));
        logger.debug("Volume input");
        main.Close_at_Profit.click();
        String tp_input = String.valueOf((double)Math.round((getDoubleValue(main.tp_create.getText()) + 0.1) * 100000d) / 100000d);
        main.waitClickClearEnterDataInField(main.tp_create,tp_input.replace(".",decimalSeparator));
        logger.debug("TP input");
        main.Close_at_Loss.click();
        String sl_input = String.valueOf((double)Math.round((getDoubleValue(main.sl_create.getText()) - 0.1) * 100000d) / 100000d);
        main.waitClickClearEnterDataInField(main.sl_create,sl_input.replace(".",decimalSeparator));
        logger.debug("SL input");
        tp_set = String.valueOf(getDoubleValue(main.tp_create.getText()));
        sl_set = String.valueOf(getDoubleValue(main.sl_create.getText()));
        driver.hideKeyboard();
        main.touchAndHold(main.BUY_order);
        opened_on = main.order_open_date.getText();
        order_number = main.order_name.getText().split("#")[1];
        open_price = Double.parseDouble(main.open_price_notification.getText());
        assertNotNull(order_number);
        /* debug info */
        logger.debug("..............[set values]..............");
        logger.debug("TP: " + tp_set);
        logger.debug("SL: " + sl_set);
        logger.debug("..............[open order]..............");
        logger.debug("OPENED 0N(notification OPEN): " + opened_on);
        logger.debug("ORDER NUMBER(notification OPEN): " + order_number);
        logger.debug("Order was opened successfully. OPEN PRICE(notification): " + open_price);
    }

    @Test(dependsOnMethods = {"openNewPosition"})
    public void openPortfolio() {
        main.waitGetClickableElement(main.Portfolio).click();
        assertTrue(main.waitIsElementVisible(main.Open));
        logger.debug("'Portfolio' is selected");
    }

    @Test(dependsOnMethods = {"openPortfolio"})
    public void checkOrderIsInListOfOpenOrders(){
        assertTrue(main.isElementPresent(opened_on));
        logger.debug("Order is in list of open orders('OPEN'). Dialog is open");
    }

    @Test(dependsOnMethods = {"checkOrderIsInListOfOpenOrders"})
    public void expandOrder() {
        assertTrue(main.tapExistedOrderInPortfolio(opened_on));
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkOrderNumberInOpenOrder(){
        String orderNumber = main.order_number_position_opening.getText();
        /* debug info */
        logger.debug("..............[expand order].............");
        logger.debug("ORDER NUMBER(position opening dialog): " + orderNumber);
        assertEquals(orderNumber, order_number);
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkTpInOpenOrder(){
        assertEquals(String.valueOf(getDoubleValue(main.tp_open.getText())), tp_set);
        logger.debug("TP is correct");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkSlInOpenOrder(){
        assertEquals(String.valueOf(getDoubleValue(main.sl_open.getText())), sl_set);
        logger.debug("SL is correct");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkOrderInstrumentInOpenOrder(){
        assertEquals(main.instrument_order_open.getText(), instrument_set);
        logger.debug("Instrument is correct");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkVolumeLotsInOpenOrder(){
        assertEquals(getDoubleValue(main.volume_open.getText()), getDoubleValue(VOLUME_SET));
        logger.debug("Volume lots is correct");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkVolumeUnitsInOpenOrder(){
        if(ProjectPackages.INCEPTIAL.getValueAsList().contains(DriverFactory.package_name)){
            assertEquals(getDoubleValue(main.volume_units.getText()), getDoubleValue(VOLUME_SET));
        }else{
            assertEquals(getDoubleValue(main.volume_units.getText()), getDoubleValue(VOLUME_SET)*100);
        }
        logger.debug("Volume units is correct");
    }

    @Test(dependsOnMethods = {"expandOrder"}, enabled = true)
    public void checkAssetLeverageInOpenOrder(){
        asset_leverage = String.valueOf(APIClient.getAssetLeverage(DriverFactory.package_name));
        assertEquals(main.asset_leverage.getText(), "1:" + asset_leverage);
        logger.debug("Asset leverage is correct");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkOperationTypeInOpenOrder(){
        assertEquals(main.operation_open_buy.getText(),OPERATION_TYPE);
        logger.debug("Operation type is correct");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkOpenPriceInOpenOrder(){
        assertTrue(main.isOpenPriceValidInOrderDialog(String.valueOf(open_price)));
        logger.debug("Open price is correct");
    }

    @Test(priority = 2, dependsOnMethods = {"expandOrder"})
    public void closeOrder() {
        String PROFIT = null;
        try {
            main.tapClosePositionInOrderDialog();
            PROFIT = main.profit_notification.getText();
            assertNotNull(PROFIT);
            /* debug info */
            logger.debug("..............[close order]..............");
            logger.debug("Order was closed successfully. PROFIT(notification): " + PROFIT);
        } catch (NoSuchElementException e) {
            if (main.No_quotes.getText().equals("No quotes")) {
                logger.debug("Order cannot be closed. The reason is: 'No quotes'");
                order_will_not_be_closed = true;
                PROFIT = "";
                main.OK.click();
            } else {
                logger.debug("Order closing message could not be found. Message 'No quotes' not found");
            }
        }
        assertNotNull(PROFIT);
    }

    @Test(dependsOnMethods = {"closeOrder"})
    public void openHistory() throws InterruptedException {
        Thread.sleep(5000);
        main.tapElement_iOS_XCTest(main.History);
        assertEquals(main.History.getAttribute("value"), "1");
        logger.debug("'HISTORY' is open");
    }

    @Test(dependsOnMethods = {"openHistory"})
    public void setPeriod(){
        main.history_settings.click();
        main.getElement("Week").click();
        main.scrollToElementInDataPicker_iOS_XCTest("Today");
        main.getElement("Done").click();
        main.getElement("Back").click();
        String fromPeriod = main.From_period.getText();
        assertEquals(fromPeriod.substring(12,23),fromPeriod.substring(25,36));
    }

    @Test(dependsOnMethods = {"setPeriod"})
    public void checkClosedOrderIsInHistory() {
        List<IOSElement> orders = main.history_profit_text;
        boolean found = false;
        if(orders.size() > 0){
            for(int i = 0; i < 3; i++){
                orders.get(i).click();
                String orderInfo = main.history_info_text.getText();
                if (!order_will_not_be_closed){
                    if(orderInfo.contains(order_number)&
                            getDoubleValue(orderInfo.split("rate: ")[1].split(" Order")[0])== open_price){
                        found = true;
                        break;
                    }else{
                        orders.get(i).click();
                    }
                }else{
                    if (orderInfo.contains("Opened on") & orderInfo.contains("On rate") & orderInfo.contains("Order #")) {
                        found = true;
                        break;
                    }
                }
            }
        }else{
            if(order_will_not_be_closed & main.Nothing_yet.isEnabled()){
                found = true;
                logger.debug("List of closed orders is empty ('Nothing yet. Change the filter settings or choose an instrument to trade')");
            }
        }
        assertTrue(found);
        logger.debug("test finished - " + DriverFactory.project_name);
    }
}