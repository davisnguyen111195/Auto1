package gmail.auto.send;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.io.*;


import java.net.URL;

import java.util.*;

public class controller implements Initializable {

    @FXML
    private Button btnImportAccount;

    @FXML
    private TextField txtAccountList;

    @FXML
    private Button btnAutoSend;

    @FXML
    private Button btnImportEmail;

    @FXML
    private Button btnImportTemplate;

    @FXML
    private Button btnImportSubject;

    @FXML
    private Button btnQuit;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtDelay;

    @FXML
    private TextField txtQuota;


    ArrayList<data> listData = new ArrayList<data>();

    public void ImportAccount(ActionEvent event) throws IOException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Just *.csv");
            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
            File filePath = fileChooser.showOpenDialog(main.getPrimaryStage());

            String file = filePath.toString();
            txtAccountList.setText(file);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void ImportEmail(ActionEvent event) {

    }

    public void ImportTemplate(ActionEvent event) {

    }

    public void ImportSubject(ActionEvent event) {

    }

    public void Quit(ActionEvent event) {

    }

    public void autoSend(ActionEvent event) {

    }

    public void Login(ActionEvent event) {
        String os = System.getProperty("os.name");
        if (os.equals("Linux")) {

            LoginGmail("/media/dat/MyHome/GmailAutoSend/library/chromedriver");
        } else if (os.equals("Windows")) {

            LoginGmail("C:/GmailAutoSend/library/chromedriver");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resoureces) {


    }


    public void LoginGmail(String pathChromeDriver) {

        try {
            String pageTitle;
            String file = txtAccountList.getText();
            FileReader fileReader = new FileReader(file);
            BufferedReader CSVFile = new BufferedReader(fileReader);
            String dataRow = CSVFile.readLine();
            int i = 1;
            while (dataRow != null) {
                String[] dataArray = dataRow.split(",");
                data Data = new data();
                Data.setUser(dataArray[0]);
                Data.setPass(dataArray[1]);
                Data.setRecoveryMail(dataArray[2]);
                System.setProperty("webdriver.chrome.driver", pathChromeDriver);
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("disable-infobars");
                chromeOptions.addArguments("--disable-extensions");

                chromeOptions.addArguments("user-data-dir=/home/dat/.config/google-chrome/Profile " + i);
                WebDriver chromeDriver = new ChromeDriver(chromeOptions);

                chromeDriver.get("https://accounts.google.com/signin");
                Thread.sleep(5000);
                pageTitle = chromeDriver.getTitle();
                System.out.println(pageTitle);
                Thread.sleep(10000);
                Boolean isHave = pageTitle.contains("My Account") || pageTitle.contains("Tài khoản của tôi");
                if (isHave == true) {
                    WriteSuccess(Data.toString());
                    pageTitle = null;
                    chromeDriver.close();
                }
                else if (pageTitle.contains("Đăng nhập - Tài khoản Google") == true || pageTitle.contains("Sign in - Google Accounts") == true) {
                    Thread.sleep(5000);
                    chromeDriver.findElement(By.cssSelector("#identifierId"))
                                .sendKeys(Data.getUser());
                    Thread.sleep(2000);
                    chromeDriver.findElement(By.cssSelector("#identifierNext > content > span")).click();
                    Thread.sleep(5000);
                    chromeDriver.findElement(By.cssSelector("#password > div.aCsJod.oJeWuf > div > div.Xb9hP > input"))
                                .sendKeys(Data.getPass());
                    Thread.sleep(5000);
                    chromeDriver.findElement(By.cssSelector("#passwordNext > content > span")).click();
                    Thread.sleep(10000);
                    String pageSource = chromeDriver.getPageSource();
                    System.out.println(pageSource);
                    Thread.sleep(5000);
                    if (pageSource.contains("Confirm your recovery email") == true ||
                            pageSource.contains("Xác nhận email khôi phục của bạn") == true) {

                        chromeDriver.findElement(By.cssSelector("#view_container > form > div.mbekbe.bxPAYd > div >" +
                                    " div > div > ul > li:nth-child(1) > div > div.vdE7Oc")).click();
                        Thread.sleep(3000);
                        chromeDriver.findElement(By.cssSelector("#knowledge-preregis" +
                                    "tered-email-response")).sendKeys(Data.getRecoveryMail());
                        Thread.sleep(1000);
                        chromeDriver.findElement(By.cssSelector("#next > content > span")).click();
                        Thread.sleep(5000);

                        WriteSuccess(Data.toString());
                        pageTitle = null;
                        pageSource = null;
                        chromeDriver.close();
                    }
                    else if (pageSource.contains("Account disabled") == true || pageSource.contains("Đã vô hiệu hóa tài khoản") == true) {
                        pageTitle = null;
                        pageSource = null;
                        chromeDriver.close();
                    }
                    else if (pageSource.contains("Protect your account") == true || pageSource.contains("Bảo vệ tài khoản của bạn") == true) {
                        chromeDriver.findElement(By.cssSelector(".M9Bg4d > content:nth-child(3) > span:nth-child(1)")).click();
                        Thread.sleep(5000);
                        WriteSuccess(Data.toString());
                        pageTitle = null;
                        pageSource = null;
                        chromeDriver.close();
                    }
                    else if (pageSource.contains("My Account gives you") == true || pageSource.contains("Tài khoản của tôi cho phép") == true) {
                        WriteSuccess(Data.toString());
                        pageTitle = null;
                        pageSource = null;
                        chromeDriver.close();
                    }
                }

                dataRow = CSVFile.readLine();
                i++;
                //Delay
                Integer delayTime = Integer.parseInt(txtDelay.getText()) * 1000;
                Thread.sleep(delayTime);
            }
            CSVFile.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void WriteSuccess (String wri) throws IOException {
        try {
            Writer writer = new FileWriter("/home/dat/Downloads/success.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write(wri);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}

