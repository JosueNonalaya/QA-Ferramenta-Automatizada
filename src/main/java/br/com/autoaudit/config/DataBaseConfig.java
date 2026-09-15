package br.com.autoaudit.config;

public class DataBaseConfig {
    private static final String DATABASE_URL = "jdbc:sqlite:autoaudit.db";

    private DataBaseConfig(){
    }

    public static String getDatabaseUrl(){
        return DATABASE_URL;
    }
}
