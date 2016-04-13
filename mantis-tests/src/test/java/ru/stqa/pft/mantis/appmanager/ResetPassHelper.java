package ru.stqa.pft.mantis.appmanager;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ResetPassHelper extends HelperBase{

    public ResetPassHelper(ApplicationManager app) {
        super (app);
    }

    public void login(String username, String password) {
        wd.get(app.getProperty("web.baseUrl") + "/login_page.php");
        type(By.xpath("//input[@name='username']"),username);
        type(By.xpath("//input[@name='password']"), password);
        click(By.xpath("//input[@value='Login']"));
    }

    public void goToUsersManagePage() {
        wd.get(app.getProperty("web.baseUrl") + "/manage_user_page.php");
    }

    public void editUser(String login) {
        click(By.xpath("//a[text()='"+ login +"']"));
    }

    public String getEmailFromProfilePage() {
        return wd.findElement(By.xpath("//input[@name='email']")).getAttribute("value");
    }

    public void resetPassword() {
        click(By.xpath("//input[@value='Reset Password']"));
    }

    public void gotoConfirmationLink(String confirmationLink){
        wd.get(confirmationLink);
    }

    public void editPassword(String newPass) {
        type(By.xpath("//input[@name='password']"), newPass);
        type(By.xpath("//input[@name='password_confirm']"), newPass);
        click(By.xpath("//input[@value='Update User']"));
    }






}
