package com.GreenDAOExampleLOL.generator;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Main {
    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.example.technorides.greendaoexamplelol.db");

        //generates a class named program which is a DAO. Also ProgramDAO
        Entity program = schema.addEntity("Program");
        program.addIdProperty();
        program.addStringProperty("name");
        program.addDateProperty("beginDate");

        DaoGenerator daoGenerator = new DaoGenerator();

        //generator of directory that the generated code will be put into
        daoGenerator.generateAll(schema, "./app/src/main/java");


    }
}
