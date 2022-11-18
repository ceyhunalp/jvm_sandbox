package rewriter;

import org.objectweb.asm.*;

public class ClassAdapter extends ClassVisitor {

    public ClassAdapter(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String[] subStrs = name.split("/");
        super.visit(version, access, subStrs[subStrs.length-1], signature, superName, interfaces);
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null) {
            mv = new MethodRewriter(mv);
        }
        return mv;
    }

}