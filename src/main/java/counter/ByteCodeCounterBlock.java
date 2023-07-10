package counter;

import br.usp.each.saeg.asm.defuse.FlowAnalyzer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class ByteCodeCounterBlock {

    public static void main(String[] args) throws IOException, AnalyzerException {

        FileInputStream is_c = new FileInputStream("target/classes/Counter.class");
        ClassReader cr_c = new ClassReader(is_c);
        final ClassNode classNode_c = new ClassNode();
        cr_c.accept(classNode_c, 0);


        FileInputStream is = new FileInputStream("target/classes/TestContract.class");

        ClassReader cr = new ClassReader(is);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        final ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);

        for (final MethodNode mn : classNode.methods) {

            FlowAnalyzer<BasicValue> analyzer = new FlowAnalyzer<BasicValue>(new BasicInterpreter());
            analyzer.analyze("package/ClassName", mn);

            int[][] basicBlocks = analyzer.getBasicBlocks();

//            for (int i = 0; i < basicBlocks.length; i++) {
////                System.out.println("Basic block " + i + " contains " + (basicBlocks[i].length) + " instructions");
//            }

            int numIns = mn.instructions.size();
            InsnList instructions = new InsnList();
            for (int i = 0; i < numIns; i++) {
                instructions.add(mn.instructions.get(i));
            }
            for (int i = 0; i < numIns; i++) {
                for (int[] basicBlock : basicBlocks) {
                    if (basicBlock[0] == i) {
                        AbstractInsnNode node = instructions.get(i);
                        int opCodeCount = 0;
                        int endIns = i + basicBlock.length - 1;
                        for (int j = i; j <= endIns; j++) {
                            int opCode = instructions.get(j).getOpcode();
                            if (opCode != -1) {
                                System.out.println(opCode);
                                opCodeCount++;
                            }
                        }
                        InsnList numCounting = new InsnList();
                        numCounting.add(new FieldInsnNode(Opcodes.GETSTATIC, classNode_c.name, "count", "I"));
                        int numberOfNonNullInstructions = opCodeCount;
                        numCounting.add(new LdcInsnNode(numberOfNonNullInstructions));
                        numCounting.add(new InsnNode(Opcodes.IADD));
                        numCounting.add(new FieldInsnNode(Opcodes.PUTSTATIC, classNode_c.name, "count", "I"));
                        mn.instructions.insert(node, numCounting);
                        break;
                    }
                }
            }
        }

        FileOutputStream fos = new FileOutputStream("target/classes/TestContract.class");
        classNode.accept(cw);
        fos.write(cw.toByteArray());
        fos.close();
    }
}
