package rewriter;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodRewriter extends MethodVisitor {

    String mathLib;

    public MethodRewriter(MethodVisitor mv, String mathLib) {
        super(Opcodes.ASM9, mv);
        this.mathLib = mathLib;
    }

    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        System.out.println("MethodInst: " + owner + " " + name + " " + descriptor);
        if (owner.equals("java/lang/Math") || owner.equals("java/lang/StrictMath")) {
            switch (name) {
                case "abs":
                case "max":
                case "min":
                    if (descriptor.equals("(D)D") || descriptor.equals("(F)F")) {
                        mv.visitMethodInsn(opcode, mathLib, name, descriptor, isInterface);
                    }
                    break;
                case "acos":
                case "asin":
                case "atan":
                case "atan2":
                case "cbrt":
                case "ceil":
                case "copysign":
                case "cos":
                case "cosh":
                case "exp":
                case "expm1":
                case "floor":
                case "getExponent":
                case "hypot":
                case "IEEEremainder":
                case "log":
                case "log10":
                case "log1p":
                case "nextAfter":
                case "nextDown":
                case "nextUp":
                case "pow":
                case "rint":
                case "round":
                case "signum":
                case "sin":
                case "sinh":
                case "tan":
                case "tanh":
                    mv.visitMethodInsn(opcode, mathLib, name, descriptor, isInterface);
                    break;
                default:
            }
        } else if(owner.equals("java/lang/Double")) {
            if ("doubleToRawLongBits".equals(name)) {
                mv.visitMethodInsn(opcode, owner, "doubleToLongBits", descriptor, isInterface);
            } else {
                mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }
        } else {
            mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}
