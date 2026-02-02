package jp.co.wordbook.controllers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

@WebListener
public class DatabaseCleanupListener implements ServletContextListener {
    
    // アプリ起動時の処理
    @Override
    public void contextInitialized(ServletContextEvent sce) {}


    // アプリ終了時の処理
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        // AbandonedConnectionCleanupThread を停止
        //      DB接続が閉じられなかった場合、自動で切断する機能
        //      メモリリークを引き起こす可能性が非常に高い」という警告が出るのは、
        //      Tomcatの管理外 (WEB-INF/lib内) にJDBCドライバあるため、停止に失敗している
        try {
            // mysql-connector-java-5.1.38.jar 環境のため shutdown() を使用
            // 新しい場合は checkedShutdown() を使用
            AbandonedConnectionCleanupThread.shutdown();

        } catch (Exception e) {
            sce.getServletContext().log("AbandonedConnectionCleanupThread の停止に失敗", e);
        }

        // JDBCドライバの登録解除
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();

            // このアプリ（ClassLoader）がロードしたドライバのみを解除
            if (driver.getClass().getClassLoader() ==
                Thread.currentThread().getContextClassLoader()
            ) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    sce.getServletContext().log("JDBCドライバの登録解除に失敗", e);
                }
            }
        }
    }
}
