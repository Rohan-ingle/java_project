package com.example.java_project;

import com.example.java_project.GenerateKeySalt;

public class generatorDebug {

    public static void main(String[] args) {
        GenerateKeySalt generator = new GenerateKeySalt();
        String username = "username";
        String password = "password";
        generator.generate(username,password);
    }

}
