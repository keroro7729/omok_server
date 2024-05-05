package com.test.omok.Form;

public class InputChecker {

    public static boolean checkRegisterForm(RegisterForm form){
        return (checkuserName(form.getUserName()) && checkPassword(form.getPassword()));
    }

    public static boolean checkLoginForm(LoginForm form){
        return (checkuserName(form.getUserName()) && checkPassword(form.getPassword()));
    }
    public static boolean checkuserName(String userName){
        if(userName.length() < 5 || 30 < userName.length()) return false;
        for(int i=0; i<userName.length(); i++){
            if(!isEngOrNum(userName.charAt(i)) && userName.charAt(i) != '_')
                return false;
        }
        return true;
    }

    public static boolean checkPassword(String password){
        if(password.length() < 8 || 30 < password.length()) return false;
        for(int i=0; i<password.length(); i++){
            if(!isEngOrNum(password.charAt(i)) && !isValidSign(password.charAt(i)))
                return false;
        }
        return true;
    }

    public static boolean checkNickName(String nickname){
        return checkuserName(nickname);
    }

    private static boolean isEngOrNum(char c){
        return (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9'));
    }
    private static final String signs = "!@#$%^&*-_=+|:/?";
    private static boolean isValidSign(char c){
        return (signs.indexOf(c) != -1);
    }
}
