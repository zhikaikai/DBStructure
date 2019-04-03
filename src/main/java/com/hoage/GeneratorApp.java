package com.hoage;

import com.hogae.generator.doc.GeneratorExcel;
import com.hogae.generator.doc.GeneratorHandler;

public class GeneratorApp {

    public static GeneratorHandler excel = new GeneratorExcel();

    public static void main(String[] args) {
        excel.generator();
    }
}
