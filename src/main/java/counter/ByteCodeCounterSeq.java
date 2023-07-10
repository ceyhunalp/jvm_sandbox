package counter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteCodeCounterSeq {

    public static void main(String[] args) throws IOException {

        FileInputStream is_c = new FileInputStream("target/classes/Counter.class");
        ClassReader cr_c = new ClassReader(is_c);
        final ClassNode classNode_c = new ClassNode();
        cr_c.accept(classNode_c, 0);

        FileInputStream is = new FileInputStream("target/classes/TestContract.class");

        ClassReader cr = new ClassReader(is);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        final ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
//        classNode.fields.add(new FieldNode(Opcodes.ACC_STATIC + Opcodes.ACC_PUBLIC, "count", "I", null, 0));

        for (final MethodNode mn : classNode.methods) {
            // core insert
            for (AbstractInsnNode node : mn.instructions) {
                if (node.getOpcode() == -1) {
                    continue;
                }
                System.out.println(node.getOpcode());
                InsnList numCounting = new InsnList();
                // insert count++
                numCounting.add(new FieldInsnNode(Opcodes.GETSTATIC, classNode_c.name, "count", "I"));
                numCounting.add(new InsnNode(Opcodes.ICONST_1));
                numCounting.add(new InsnNode(Opcodes.IADD));
                numCounting.add(new FieldInsnNode(Opcodes.PUTSTATIC, classNode_c.name, "count", "I"));

//                Insert print for debugging
//                numCounting.add(new MethodInsnNode(Opcodes.INVOKESTATIC, classNode_c.name, "print", "()V", false));
                if (node.getOpcode() == 177) {
//                    System.out.println("Return Statement");
                    mn.instructions.insertBefore(node, numCounting);
                } else if (node.getOpcode() == 172) {
//                    System.out.println("Return Statement");
                    mn.instructions.insertBefore(node, numCounting);
                } else if (node.getOpcode() == 167) {
//                    System.out.println("Goto Statement");
                    mn.instructions.insertBefore(node, numCounting);
                } else if (node.getOpcode() == 158) {
//                    System.out.println("if Statement");
                    mn.instructions.insertBefore(node, numCounting);
                } else {
                    mn.instructions.insert(node, numCounting);
                }
            }
        }
        FileOutputStream fos = new FileOutputStream("target/classes/TestContract.class");
        classNode.accept(cw);
        fos.write(cw.toByteArray());
        fos.close();
    }


}
