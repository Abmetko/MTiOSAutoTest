import app.frontend.screens.LoginScreen;
import app.frontend.screens.MainScreen;
import app.frontend.screens.StartScreen;
import core.utils.APIClient;
import core.utils.PropertyLoader;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;
import java.util.List;
import static org.testng.Assert.*;


public class RegressionTest extends BaseTest{

    private StartScreen start;
    private LoginScreen login;
    private MainScreen main;
    private final String INSTRUMENT_SET = PropertyLoader.getProperty("instrument.set");
    private final String GROUP_SET = PropertyLoader.getProperty("group.set");
    private final String VOLUME_DEFAULT = PropertyLoader.getProperty("volume.default");
    private final String VOLUME_SET = PropertyLoader.getProperty("volume.set");
    private String TP_SET;
    private String SL_SET;
    private String OPENED_ON;
    private String ORDER_NUMBER = null;
    private double OPEN_PRICE;
    private final String OPERATION_TYPE = PropertyLoader.getProperty("operation.type");
    private boolean ORDER_WILL_NOT_BE_CLOSED = false;
    private String ASSET_LEVERAGE;

    @Test
    public void checkStartScreenIsOpen(){
        System.out.println("[DEBUG] -------------------------------------------------------");
        System.out.println("[DEBUG] T E S T  S T A R T S");
        System.out.println("[DEBUG] -------------------------------------------------------");
        start = new StartScreen(driver);
        assertTrue(start.waitIsScreenLoaded());
        System.out.println("[DEBUG] Start screen is open.");
    }

    @Test(dependsOnMethods = {"checkStartScreenIsOpen"})
    public void openLoginScreen(){
        assertTrue(start.clickLoginButton());
        login = new LoginScreen(driver);
        assertTrue(login.waitIsScreenLoaded());
        System.out.println("[DEBUG] Login screen is open.");
    }

    @Test(dependsOnMethods = {"openLoginScreen"})
    public void makeLogin(){
        assertTrue(login.makeLogin());
        main = new MainScreen(driver);
        assertTrue(main.waitIsScreenLoaded());
        System.out.println("[DEBUG] Main screen is open.");
    }

    @Test(priority = 1, dependsOnMethods = {"makeLogin"})
    public void setGroup() {
        main.selected_group_name.click();
        main.swipeToDirection_iOS_XCTest(main.getElement(GROUP_SET),"d");
        assertEquals(main.selected_group_name.getText(),GROUP_SET);
        System.out.println("[DEBUG] Group: " + GROUP_SET + " is set.");
    }

    @Test(dependsOnMethods = {"setGroup"})
    public void setInstrument() {
        if (!main.default_instrument.getText().equals(INSTRUMENT_SET)){
            main.default_instrument.click();
            main.swipeToDirection_iOS_XCTest(main.getElement(INSTRUMENT_SET),"d");
        }
        assertEquals(main.default_instrument.getText(),INSTRUMENT_SET);
        System.out.println("[DEBUG] Instrument: " + INSTRUMENT_SET + " is set.");
    }

    @Test(dependsOnMethods = {"setInstrument"})
    public void checkInstrumentIsAvailable(){
        assertTrue(main.waitIsElementVisible(main.BUY_trade));
    }

    @Test(dependsOnMethods = {"checkInstrumentIsAvailable"})
    public void openPositionOpeningDialog() {
        main.tapElement_iOS_XCTest(main.BUY_trade);
        assertTrue(main.waitIsElementVisible(main.volume_lots));
        System.out.println("[DEBUG] Position opening dialog is open.");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void checkInstrument() {
        assertEquals(main.instrument_set.getText(),INSTRUMENT_SET);
        System.out.println("[DEBUG] Instrument is correct.");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void checkDefaultVolumeLots(){
        String volumeLots = main.volume_lots.getText();
        if(volumeLots.contains(","))volumeLots = volumeLots.replace(",",".");
        double actualVolume = Double.parseDouble(volumeLots);
        double expectedVolume = Double.parseDouble(VOLUME_DEFAULT);
        assertEquals(actualVolume,expectedVolume);
        System.out.println("[DEBUG] Default volume lots is correct.");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void checkDefaultVolumeUnits(){
        assertEquals(Double.parseDouble(main.volume_units.getText()), Double.parseDouble(VOLUME_DEFAULT)*100);
        System.out.println("[DEBUG] Default volume units is correct.");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"}, enabled = false)
    public void checkAssetLeverage(){
        ASSET_LEVERAGE = String.valueOf(APIClient.getAssetLeverage());
        assertEquals(main.asset_leverage.getText(), "1:" + ASSET_LEVERAGE);
        System.out.println("[DEBUG] Asset leverage is correct.");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void checkSwitcherTpIsNotChecked(){
        assertEquals(main.Close_at_Profit.getAttribute("value"),"0");
        System.out.println("[DEBUG] TP is disabled.");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void checkSwitcherSlIsNotChecked(){
        assertEquals(main.Close_at_Loss.getAttribute("value"),"0");
        System.out.println("[DEBUG] SL is disabled.");
    }

    @Test(dependsOnMethods = {"openPositionOpeningDialog"})
    public void openNewPosition() {
        String decimalSeparator = ".";
        if(main.volume_lots.getText().contains(",")){
            decimalSeparator = ",";
        }
        main.waitClickClearEnterDataInField(main.volume_lots, VOLUME_SET.replace(".",decimalSeparator));
        System.out.println("[DEBUG] Volume input.");
        main.Close_at_Profit.click();
        String tp_input = String.valueOf((double)Math.round((Double.parseDouble(main.tp_create.getText().replace(",",".")) + 0.1) * 100000d) / 100000d);
        main.waitClickClearEnterDataInField(main.tp_create,tp_input.replace(".",decimalSeparator));
        System.out.println("[DEBUG] TP input.");
        main.Close_at_Loss.click();
        String sl_input = String.valueOf((double)Math.round((Double.parseDouble(main.sl_create.getText().replace(",",".")) - 0.1) * 100000d) / 100000d);
        main.waitClickClearEnterDataInField(main.sl_create,sl_input.replace(".",decimalSeparator));
        System.out.println("[DEBUG] SL input.");
        TP_SET = String.valueOf(Double.parseDouble(main.tp_create.getText().replace(",",".")));
        SL_SET = String.valueOf(Double.parseDouble(main.sl_create.getText().replace(",",".")));
        main.touchAndHold(main.BUY_order);
        OPENED_ON = main.order_open_date.getText();
        ORDER_NUMBER = main.order_name.getText().split("#")[1];
        OPEN_PRICE = Double.parseDouble(main.open_price_notification.getText());
        assertNotNull(ORDER_NUMBER);
        /* debug info */
        System.out.println("[DEBUG]..............[set values]..............");
        System.out.println("[DEBUG] TP: " + TP_SET);
        System.out.println("[DEBUG] SL: " + SL_SET);
        System.out.println("[DEBUG]..............[open order]..............");
        System.out.println("[DEBUG] OPENED 0N(notification OPEN): " + OPENED_ON);
        System.out.println("[DEBUG] ORDER NUMBER(notification OPEN): " + ORDER_NUMBER);
        System.out.println("[DEBUG] Order was opened successfully. OPEN PRICE(notification): " + OPEN_PRICE);
    }

    @Test(dependsOnMethods = {"openNewPosition"})
    public void openPortfolio() {
        main.waitGetClickableElement(main.Portfolio).click();
        assertTrue(main.waitIsElementVisible(main.Open));
        System.out.println("[DEBUG] 'Portfolio' is selected.");
    }

    @Test(dependsOnMethods = {"openPortfolio"})
    public void checkOrderIsInListOfOpenOrders(){
        assertTrue(main.isElementPresent(OPENED_ON));
        System.out.println("[DEBUG] Order is in list of open orders('OPEN'). Dialog is open.");
    }

    @Test(dependsOnMethods = {"checkOrderIsInListOfOpenOrders"})
    public void expandOrder() {
        assertTrue(main.tapExistedOrderInPortfolio(OPENED_ON));
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkOrderNumberInOpenOrder(){
        String orderNumber = main.order_number_position_opening.getText();
        /* debug info */
        System.out.println("[DEBUG]..............[expand order].............");
        System.out.println("[DEBUG] ORDER NUMBER(position opening dialog): " + orderNumber);
        assertEquals(orderNumber,ORDER_NUMBER);
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkTpInOpenOrder(){
        assertEquals(String.valueOf(Double.parseDouble(main.tp_open.getText().replace(",","."))),TP_SET);
        System.out.println("[DEBUG] TP is correct.");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkSlInOpenOrder(){
        assertEquals(String.valueOf(Double.parseDouble(main.sl_open.getText().replace(",","."))),SL_SET);
        System.out.println("[DEBUG] SL is correct.");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkOrderInstrumentInOpenOrder(){
        assertEquals(main.instrument_order_open.getText(),INSTRUMENT_SET);
        System.out.println("[DEBUG] Instrument is correct.");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkVolumeLotsInOpenOrder(){
        assertEquals(Double.parseDouble(main.volume_open.getText()), Double.parseDouble(VOLUME_SET));
        System.out.println("[DEBUG] Volume lots is correct.");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkVolumeUnitsInOpenOrder(){
        assertEquals(Double.parseDouble(main.volume_units.getText()), Double.parseDouble(VOLUME_SET)*100);
        System.out.println("[DEBUG] Volume units is correct.");
    }

    @Test(dependsOnMethods = {"expandOrder"}, enabled = false)
    public void checkAssetLeverageInOpenOrder(){
        ASSET_LEVERAGE = String.valueOf(APIClient.getAssetLeverage());
        assertEquals(main.asset_leverage.getText(), "1:" + ASSET_LEVERAGE);
        System.out.println("[DEBUG] Asset leverage is correct.");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkOperationTypeInOpenOrder(){
        assertEquals(main.operation_open_buy.getText(),OPERATION_TYPE);
        System.out.println("[DEBUG] Operation type is correct.");
    }

    @Test(dependsOnMethods = {"expandOrder"})
    public void checkOpenPriceInOpenOrder(){
        assertTrue(main.isOpenPriceValidInOrderDialog(String.valueOf(OPEN_PRICE)));
        System.out.println("[DEBUG] Open price is correct.");
    }

    @Test(priority = 2, dependsOnMethods = {"expandOrder"})
    public void closeOrder() {
        String PROFIT = null;
        try {
            main.tapClosePositionInOrderDialog();
            PROFIT = main.profit_notification.getText();
            assertNotNull(PROFIT);
            /* debug info */
            System.out.println("[DEBUG]..............[close order]..............");
            System.out.println("[DEBUG] Order was closed successfully. PROFIT(notification): " + PROFIT);
        } catch (NoSuchElementException e) {
            if (main.No_quotes.getText().equals("No quotes")) {
                System.out.println("[DEBUG] Order cannot be closed. The reason is: 'No quotes'.");
                ORDER_WILL_NOT_BE_CLOSED = true;
                PROFIT = "";
                main.OK.click();
            } else {
                System.out.println("[ERROR] Order closing message could not be found. Message 'No quotes' not found.");
            }
        }
        assertNotNull(PROFIT);
    }

    @Test(dependsOnMethods = {"closeOrder"})
    public void openHistory() throws InterruptedException {
        Thread.sleep(5000);
        main.tapElement_iOS_XCTest(main.History);
        assertEquals(main.History.getAttribute("value"), "1");
        System.out.println("[DEBUG] 'HISTORY' is open.");
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
                if (!ORDER_WILL_NOT_BE_CLOSED){
                    if(orderInfo.contains(ORDER_NUMBER)&
                            Double.parseDouble(orderInfo.split("rate: ")[1].split(" Order")[0])==OPEN_PRICE){
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
            if(ORDER_WILL_NOT_BE_CLOSED & main.Nothing_yet.isEnabled()){
                found = true;
                System.out.println("[DEBUG] List of closed orders is empty ('Nothing yet. Change the filter settings or choose an instrument to trade.').");
            }
        }
        assertTrue(found);
        System.out.println("[DEBUG] -------------------------------------------------------");
        System.out.println("[DEBUG] T E S T  F I N I S H E D");
        System.out.println("[DEBUG] -------------------------------------------------------");
    }
}