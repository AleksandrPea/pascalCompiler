package com.apea;

import com.apea.compiler.ast.Program;
import com.apea.compiler.codeGeneration.IrGenerator;
import com.apea.compiler.codeGeneration.ia32.IA32CodeGenerator;
import com.apea.compiler.ir.IrCommand;
import com.apea.compiler.lexical.Lexeme;
import com.apea.compiler.lexical.PascalAnalyzer;
import com.apea.compiler.semantic.TypeChecker;
import com.apea.compiler.syntactic.PascalParser;
import com.apea.compiler.tools.misc.FileUtils;
import com.apea.compiler.tools.misc.StreamUtils;
import com.apea.compiler.types.StandardTypes;
import com.apea.compiler.types.Type;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {

    private static String sourcePath = "F:\\Download\\";
    private static String destPath = "F:\\Download\\Lab2\\";

    public static void main(String[] args) throws IOException {
        String code = FileUtils.readTextFile(new File(sourcePath+"test.pas"));
        ArrayList<Lexeme> lexemes = PascalAnalyzer.instance().makeLexemes(code);
        Program program = PascalParser.instance().parseInAST(lexemes);
        System.out.println(program);
        HashMap<String, Type> varTable = TypeChecker.checkTypes(program, StandardTypes.getTypes());
        List<IrCommand> commands = IrGenerator.generate(program, varTable, StandardTypes.getTypes());
        List<String> asmLines = IA32CodeGenerator.generateAsmProgram(varTable, commands);
        StreamUtils.writeLines(asmLines, new FileWriter((destPath + "lab2.asm")));;
        System.out.println(IrGenerator.generate(program, varTable, StandardTypes.getTypes() ));

//        String[] terms1 = {"+", "-", "/", "*", "a", "b", "(", ")"};
//        String[] nterms1 = {"S", "R", "T", "F", "E"};
//        Grammar cfGR = new Grammar(terms1, nterms1);
//        try {
//            cfGR.createRule("S", new Symbol("T", false), new Symbol("R", false));
//            cfGR.createRule("R", new Symbol("+", true),new Symbol("T", false), new Symbol("R", false));
//            cfGR.createRule("R", new Symbol("-", true),new Symbol("T", false), new Symbol("R", false));
//            cfGR.createEmptyRule("R");
//            cfGR.createRule("T", new Symbol("E", false), new Symbol("F", false));
//            cfGR.createRule("F", new Symbol("*", true),new Symbol("E", false), new Symbol("F", false));
//            cfGR.createRule("F", new Symbol("/", true),new Symbol("E", false), new Symbol("F", false));
//            cfGR.createEmptyRule("F");
//            cfGR.createRule("E", new Symbol("(", true),new Symbol("S", false), new Symbol(")", true));
//            cfGR.createRule("E", new Symbol("a", true));
//            cfGR.createRule("E", new Symbol("b", true));
//        } catch (IsNotInAlphabetException e) {
//            e.printStackTrace();
//        }
//        ArrayList<Lexeme> lexemes = new ArrayList<>();
//        lexemes.add(new Lexeme("a", "", 0, 0));
//        Parser parser = new Parser(cfGR);
//        parser.parse(lexemes);
    }
}
