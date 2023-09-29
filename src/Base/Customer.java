package Base;

import java.text.SimpleDateFormat;
import java.util.Date;



public class Customer {
    private int id;
    private String username;
    private String userLevel;
    private String registrationTime;
    private double totalSpending;
    private String phoneNumber;
    private String email;
    private int passwordAttempts = 0; // 密码错误次数
    private boolean locked = false; // 账户锁定状态

    // 将客户信息转换为表格行的数据
    public Object[] toTableRow() {
        // 返回一个包含客户信息的 Object 数组，顺序要与列名对应
        String formattedRegistrationTime = (registrationTime != null && !registrationTime.isEmpty()) ? getFormattedRegistrationTime() : "";
        return new Object[]{id, username, userLevel, formattedRegistrationTime, totalSpending, phoneNumber, email};
    }


    // 获取客户注册时间的年月日格式
    public String getFormattedRegistrationTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date registrationDate = new Date(Long.parseLong(registrationTime) * 1000);
        return dateFormat.format(registrationDate);
    }

    // 获取密码错误次数
    public int getPasswordAttempts() {
        return passwordAttempts;
    }

    // 增加密码错误次数
    public void incrementPasswordAttempts() {
        passwordAttempts++;
        if (passwordAttempts >= 5) {
            locked = true;
        }
    }

    // 检查账户是否被锁定
    public boolean isLocked() {
        return locked;
    }

    // 获取客户ID
    public int getId() {
        return id;
    }

    // 设置客户ID
    public void setId(int id) {
        this.id = id;
    }

    // 获取用户名
    public String getUsername() {
        return username;
    }

    // 设置用户名
    public void setUsername(String username) {
        this.username = username;
    }

    // 获取用户级别
    public String getUserLevel() {
        return userLevel;
    }

    // 设置用户级别
    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    // 获取注册时间
    public String getRegistrationTime() {
        return registrationTime;
    }

    // 设置注册时间
    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    // 获取累计消费总金额
    public double getTotalSpending() {
        return totalSpending;
    }

    // 设置累计消费总金额
    public void setTotalSpending(double totalSpending) {
        this.totalSpending = totalSpending;
    }

    // 获取手机号
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // 设置手机号
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // 获取邮箱
    public String getEmail() {
        return email;
    }

    // 设置邮箱
    public void setEmail(String email) {
        this.email = email;
    }

    // 验证用户名是否满足要求
    public static boolean isValidUsername(String username) {
        // 添加你的用户名验证逻辑
        return username.length() >= 5;
    }

    // 验证密码是否满足要求
    public static boolean isValidPassword(String password) {
        // 添加你的密码验证逻辑
        return password.length() > 8 && containsLowerCase(password) && containsUpperCase(password) && containsDigit(password) && containsSpecialCharacter(password);
    }

    private static boolean containsLowerCase(String str) {
        return !str.equals(str.toUpperCase());
    }

    private static boolean containsUpperCase(String str) {
        return !str.equals(str.toLowerCase());
    }

    private static boolean containsDigit(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsSpecialCharacter(String str) {
        String specialCharacters = "!@#$%^&*()_+-";
        for (char c : str.toCharArray()) {
            if (specialCharacters.indexOf(c) >= 0) {
                return true;
            }
        }
        return false;
    }
}
