package rewriter;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodRewriter extends MethodVisitor {

    public MethodRewriter(MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }

    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        System.out.println("MethodInst: " + owner + " " + name);
        if (owner.equals("java/lang/Math") || owner.equals("java/lang/StrictMath")) {
            switch (name) {
                case "log":
                    mv.visitMethodInsn(opcode, "lib/DMathMPFR", "log", descriptor, isInterface);
                    break;
                case "log10":
                    mv.visitMethodInsn(opcode, "lib/DMathMPFR", "log10", descriptor, isInterface);
                    break;
                case "log1p":
                    mv.visitMethodInsn(opcode, "lib/DMathMPFR", "log1p", descriptor, isInterface);
                    break;
                case "exp":
                    mv.visitMethodInsn(opcode, "lib/DMathMPFR", "exp", descriptor, isInterface);
                    break;
                case "pow":
                    mv.visitMethodInsn(opcode, "lib/DMathMPFR", "pow", descriptor, isInterface);
                    break;
                case "sin":
                    mv.visitMethodInsn(opcode, "lib/DMathMPFR", "sin", descriptor, isInterface);
                    break;
                case "cos":
                    mv.visitMethodInsn(opcode, "lib/DMathMPFR", "cos", descriptor, isInterface);
                    break;
                case "tan":
                    mv.visitMethodInsn(opcode, "lib/DMathMPFR", "tan", descriptor, isInterface);
                    break;
                case "cbrt":
                    mv.visitMethodInsn(opcode, "lib/DMathMPFR", "cbrt", descriptor, isInterface);
                    break;
                case "hypot":
                    mv.visitMethodInsn(opcode, "lib/DMathMPFR", "hypot", descriptor, isInterface);
                    break;
                case "abs":
                    mv.visitMethodInsn(opcode, "lib/DMathMPFR", "abs", descriptor, isInterface);
                    break;
                default:
            }
        } else {
            mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}
