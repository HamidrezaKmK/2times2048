package Controller;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.Account;

import java.io.*;

public class MainMenu extends GraphicsMenu {
    private Account account;

    public void initiateMainMenuGraphicsView() {
        //getGraphicsView().clear();
        getView().setPlayButton();
        getView().setQuitButton();
        getView().setCreateAccountButton();
        getView().setLoginButton();
    }

    // login options:
    private boolean hasLoginFields = false;
    public void login() { // true if successful
        if (!hasLoginFields) {
            getView().setLoginTextFields();
            hasLoginFields = true;
        } else {
            String username = getView().getLoginUserName();
            String password = getView().getLoginPassword();

            Account account = null;

            try {
                File file = new File("./accounts/" + username + ".json");
                YaGson yaGson = new YaGson();
                account = yaGson.fromJson(new BufferedReader(new FileReader(file)), Account.class);
            } catch (IOException ignore) {
                System.err.println("No accounts while loading!");
            }

            if (account == null) {
                System.err.println("No such username!");
                return;
            }

            if (!account.getPassword().equals(password)) {
                System.err.println("Wrong password for respective username!");
                return;
            }

            this.account = account;
            System.err.println("successfully logged in!");
            getView().deleteLoginTextFields();
            getView().deleteLoginButton();
            getView().setLogoutButton(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 5 * 4);
            getView().setChangeAccountNameButton(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 5 * 4 + WINDOW_HEIGHT / 5 / 3);
            getView().setChangeUserNameTextField(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 5 * 4 + WINDOW_HEIGHT / 5 / 3 * 2);
            hasLoginFields = false;
        }
    }

    public void logout() {
        account = null;
        getView().setLoginButton();
        getView().deleteLogoutButton();
        getView().deleteChangeAccountNameButton();
        getView().deleteChangeUserNameTextField();
    }

    public void changeAccount() {
        String newUsername = getView().getChangeUserNameTextField();
        if (newUsername == null) {
            return;
        }
        removeAccount(account);
        account.setUsername(newUsername);
        saveAccount(account);
    }

    // create account options:
    private boolean hasCreateAccountFields = false;
    public void createAccount() {
        if (!hasCreateAccountFields) {
            getView().setCreateAccountTextFields();
            hasCreateAccountFields = true;
        } else {
            String username = getView().getCreateAccountUserName();
            String password = getView().getCreateAccountPassword();

            File file = new File("./accounts/" + username + ".json");
            if (file.exists() && !file.isDirectory()) {
                System.err.println("Account with this name already exists!");
                return;
            }

            getView().deleteCreateAccountTextFields();

            Account account = new Account(username, password);
            saveAccount(account);
            System.err.println("Account successfully created!");
            hasCreateAccountFields = false;
        }
    }

    public void saveAccount(Account account) {
        try {
            FileWriter out = new FileWriter("./accounts/" + account.getUsername() + ".json", false);
            YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
            out.write(yaGson.toJson(account, Account.class));
            out.flush();
        } catch (IOException ignore) {
            System.err.println("No accounts directory!");
        }
    }

    public void removeAccount(Account account) {
        File file = new File("./accounts/" + account.getUsername() + ".json");
        if (file.exists() && !file.isDirectory()){
            System.err.println("File delete value: " + file.delete());
        }
    }
    public Account getAccount() {
        return account;
    }

    public void quit() {
        if (account != null) {
            saveAccount(account);
        }
    }


    public void setAccount(Account account) {
        this.account = account;
    }
}
