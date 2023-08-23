package com.example.operativosui.utils;

public class RandomUtil {
    public static int randomNumber(int maxNumber){
        return (int) (Math.random() * maxNumber) + 1;
    }
}
