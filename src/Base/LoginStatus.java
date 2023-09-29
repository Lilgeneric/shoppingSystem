package Base;

public class LoginStatus {
    private int passwordAttempts;
    private boolean locked;

    // 构造方法和 getter 方法...

    // 设置密码错误次数
    public void setPasswordAttempts(int passwordAttempts) {
        this.passwordAttempts = passwordAttempts;
    }

    // 设置账户锁定状态
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    // 其他方法...
}


