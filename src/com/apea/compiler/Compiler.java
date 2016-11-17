package com.apea.compiler;

import com.apea.compiler.ast.Program;
import com.apea.compiler.codeGeneration.IrGenerator;
import com.apea.compiler.codeGeneration.ia32.IA32CodeGenerator;
import com.apea.compiler.ir.IrCommand;
import com.apea.compiler.lexical.Lexeme;
import com.apea.compiler.lexical.PascalAnalyzer;
import com.apea.compiler.semantic.TypeChecker;
import com.apea.compiler.syntactic.PascalParser;
import com.apea.compiler.tools.misc.StreamUtils;
import com.apea.compiler.types.StandardTypes;
import com.apea.compiler.types.Type;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Compiler {
    public static void compile(Reader sourceCodeReader, Writer asmOutput) throws IOException {
        String sourceCode = StreamUtils.readAll(sourceCodeReader);
        ArrayList<Lexeme> lexemes = PascalAnalyzer.instance().makeLexemes(sourceCode);
        Program program = PascalParser.instance().parseInAST(lexemes);
        HashMap<String, Type> varTable = TypeChecker.checkTypes(program, StandardTypes.getTypes());
        List<IrCommand> irCommands = IrGenerator.generate(program, varTable, StandardTypes.getTypes());
        List<String> asmLines = IA32CodeGenerator.generateAsmProgram(varTable,irCommands);
        StreamUtils.writeLines(asmLines, asmOutput);
    }
}
