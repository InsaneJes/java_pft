package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.appmanager.HttpSession;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.*;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ResetPasswordTest extends TestBase{

    @BeforeMethod
    public void startMailServer() {
        app.mail().start();
    }

    @Test
    public void resetPassTest() throws IOException, MessagingException {
        Connection conn = null;     //тут коннектимся к базе и получаем нашего заранее созданного пользователя
        String currLogin = new String();
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bugtracker?user=root&password=");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select username from mantis_user_table where username = 'user_to_reset'");
            rs.next();
            currLogin = rs.getString("username");
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        app.resetPassHelper().login("administrator", "root");   //логинимся под админом
        app.resetPassHelper().chooseUser(currLogin);    //идём на страницу изменения профиля конкретного юзера
        String email = app.resetPassHelper().getEmailFromProfilePage();     //получаем его мыло
        app.resetPassHelper().resetPassword();      //жмём сменить пароль

        List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);      //ниже получаем письмо и берём ссылку
        String confirmationLink = findConfirmationLink(mailMessages, email);

        app.resetPassHelper().gotoConfirmationLink(confirmationLink);       //идём по ссылке
        String newPassword = "newPassword" + System.currentTimeMillis();        //задаём новый пароль
        System.out.println("Новый пароль пользователя " + currLogin + ": " + newPassword);  //на всякий случай выводим новый пароль в консоль
        app.resetPassHelper().editPassword(newPassword);        //устанавливаем ему этот пароль

        HttpSession session = app.newSession();     //коннектимся по http
        assertTrue(session.login(currLogin, newPassword));      //проверяем
        assertTrue(session.isLoggedInAs(currLogin));

    }

    private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }

    @AfterMethod(alwaysRun = true)
    public void stopMailServer() {
        app.mail().stop();
    }
}
