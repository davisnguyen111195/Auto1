package gmail.auto.send;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javafx.stage.FileChooser.ExtensionFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.io.*;


import java.net.URL;

import java.util.*;

public class controller implements Initializable {

    @FXML
    private Button btnImportAccountList;

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
    private Button btnLogin;

    @FXML
    private TextField txtDelay;

    @FXML
    private TextField txtEmailList;

    @FXML
    private TextField txtTemplateList;

    @FXML
    private TextField txtSubjectList;

    @FXML
    private TextField txtLoop;

    public Random rd = new Random();
    public void ImportAccountList(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("*.CSV");


            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
            File filePath = fileChooser.showOpenDialog(main.getPrimaryStage());
            String fileImportAcc = filePath.toString();

            txtAccountList.setText(fileImportAcc);
        }catch (Exception e) {
        }
    }
    List<String> listEmail = new ArrayList<>();
    public void ImportEmail(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Email List");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT File", "*.txt"));
            File fileImportEmail = fileChooser.showOpenDialog(main.getPrimaryStage());
            txtEmailList.setText(fileImportEmail.toString());
            FileReader fileReader = new FileReader(txtEmailList.getText());
            BufferedReader bfreader = new BufferedReader(fileReader);
            String email = bfreader.readLine();

            while (email != null) {
                listEmail.add(email);
                email = bfreader.readLine();
            }
            bfreader.close();
        }catch (Exception e) {

        }
    }
    List<String> listTemplate = new ArrayList<>();
    public void ImportTemplate(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Template Folder");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Html File", "*.html"));
            for (File file : fileChooser.showOpenMultipleDialog(main.getPrimaryStage())) {
                String item = "file://" + file.toString();
                listTemplate.add(item);
            }
            txtTemplateList.setText("Imported");
        } catch (Exception e) {

        }
    }
    List<String> listSubject = new ArrayList<>();
    public void ImportSubject(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Subject Folder");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Txt File", "*.txt"));
            File fileSubject = fileChooser.showOpenDialog(main.getPrimaryStage());
            txtSubjectList.setText(fileSubject.toString());
            FileReader fileReader = new FileReader(txtSubjectList.getText());
            BufferedReader bfreader = new BufferedReader(fileReader);
            String subject = bfreader.readLine();
            String stringReplace = null;
            while (subject != null) {
                stringReplace = subject.replace(" ", "+");
                listSubject.add(stringReplace);
                subject = bfreader.readLine();
            }
            bfreader.close();
        } catch (Exception e) {

        }
    }

    public String pathChromeDriverLinux = "/home/" + main.username +"/Documents/GmailAutoSend/chromedriver";
    public String pathChromeProfileLinux = "user-data-dir=/home/" + main.username +"/.config/google-chrome/Profile ";
    public String pathChromeDriverWindows = "C:\\GmailAutoSend\\chromedriver.exe";
    public String pathChromeProfileWindows = "user-data-dir=C:\\Users\\"+main.username+"\\AppData\\Local\\Google\\Chrome\\User Data\\Profile ";

    public void autoSend(ActionEvent event) {
        try {
            int k = Integer.parseInt(txtLoop.getText());
            for (Integer i = 1; i <= k; i++) {
                System.setProperty("webdriver.chrome.driver", pathChromeDriverLinux);

                String listMail = listEmail.get(i-1);
                String listSbj = listSubject.get(rd.nextInt(3));
                String profile =  pathChromeProfileLinux + i.toString();
                String urlSend = "https://mail.google.com/mail/u/0/?view=cm&fs=1&to=" + listMail + "&su=" + listSbj + "&tf=1";

                ChromeOptions options = new ChromeOptions();
                options.addArguments(profile);
                WebDriver driver = new ChromeDriver(options);
                Actions ac = new Actions(driver);
                driver.navigate().to(listTemplate.get(rd.nextInt(3)));
                Thread.sleep(500);
                ac.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
                Thread.sleep(500);
                ac.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).perform();
                Thread.sleep(500);
                driver.navigate().to(urlSend);
                Thread.sleep(3000);
                ac.keyDown(Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).perform();
                Thread.sleep(1000);
                driver.findElement(By.cssSelector("#\\3a p1")).click();

                Thread.sleep(3000);
                driver.close();
                Thread.sleep(Integer.parseInt(txtDelay.getText()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }



    public void Login(ActionEvent event) {
        try {
            String file = txtAccountList.getText();
            FileReader fileReader = new FileReader(file);
            BufferedReader CSVFile = new BufferedReader(fileReader);
            String os = System.getProperty("os.name");
            String pathSuccess = "";
            if (os.equals("Linux") == true) {
                pathSuccess = "/home/"+ main.username +"/Documents/GmailAutoSend/success.txt";
            } else if (os.equals("Windows") == true) {
                pathSuccess = "C:\\GmailAutoSend\\success.txt";
            }
            FileWriter fileWriter = new FileWriter(pathSuccess);
            String dataRow = CSVFile.readLine();

            System.out.println(dataRow.toString());
            Integer i = 1;
            while (dataRow != null) {
                String[] dataArray = dataRow.split(",");
                System.setProperty("webdriver.chrome.driver", pathChromeDriverLinux);
                ChromeOptions options = new ChromeOptions();
                String profile = pathChromeProfileLinux + i.toString();
                options.addArguments(profile);

                WebDriver driver = new ChromeDriver(options);

                driver.get("https://accounts.google.com/signin");

                String pageSource = driver.getPageSource();
                System.out.println("Page Source");
                if (pageSource.contains("My Account gives you") == true || pageSource.contains("Tài khoản của tôi cho phép") == true) {
                    System.out.println("close");
                    fileWriter.write(dataRow);
                    fileWriter.write("\n");
                    fileWriter.flush();
                } else if (pageSource.contains("Đăng nhập - Tài khoản Google") == true || pageSource.contains("Sign in - Google Accounts") == true) {
                    System.out.println("login");
                    driver.findElement(By.cssSelector("#identifierId")).sendKeys(dataArray[0]);
                    Thread.sleep(2000);

                    driver.findElement(By.cssSelector("#identifierNext > content > span")).click();
                    Thread.sleep(5000);

                    driver.findElement(By.cssSelector("#password > div.aCsJod.oJeWuf > div > div.Xb9hP > input"))
                            .sendKeys(dataArray[1]);
                    Thread.sleep(5000);

                    driver.findElement(By.cssSelector("#passwordNext > content > span")).click();
                    Thread.sleep(5000);
                    pageSource = driver.getPageSource();
                    System.out.println("pageSource 2");
                    if (pageSource.contains("Confirm your recovery email") || pageSource.contains("Xác nhận email khôi phục của bạn")) {
                        driver.findElement(By.cssSelector("#view_container > form > div.mbekbe.bxPAYd > div >" +
                                " div > div > ul > li:nth-child(1) > div > div.vdE7Oc")).click();
                        Thread.sleep(3000);
                        driver.findElement(By.cssSelector("#knowledge-preregis" +
                                "tered-email-response")).sendKeys(dataArray[2]);
                        Thread.sleep(1000);
                        driver.findElement(By.cssSelector("#next > content > span")).click();
                        Thread.sleep(5000);
                        fileWriter.write(dataRow);
                        fileWriter.write("\n");
                        fileWriter.flush();
                    } else {
                        driver.close();
                        fileWriter.write(dataRow);
                        fileWriter.write("\n");
                        fileWriter.flush();
                    }
                }
                dataRow = CSVFile.readLine();
                driver.close();
                Thread.sleep(Integer.parseInt(txtDelay.getText()));
                i++;
            }
            fileWriter.close();
            CSVFile.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resoureces) {


    }
}


