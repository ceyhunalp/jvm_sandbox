package checker;

import org.eclipse.jdt.core.dom.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class Checker {

//    public ICompilationUnit createCU(String inPath) {
//        IWorkspace workspace = ResourcesPlugin.getWorkspace();
//        IPath path = Path.fromOSString(inPath);
//        IFile file = workspace.getRoot().getFile(path);
//        CompilationUnit compilationUnit =  (CompilationUnit) JavaCore.create(file);
//        return JavaCore.createCompilationUnitFrom(file);
//    }

    public String readFileToString(String fpath) {
        Path fileName = Path.of(fpath);
        String fStr;
        try {
            fStr = Files.readString(fileName);
            return fStr;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public HashSet<String> parse(String path, String[] classPaths, String[] srcPaths, String src) {
        HashSet<String> classNames = new HashSet<>();
        ASTParser parser = ASTParser.newParser(AST.JLS_Latest);
        parser.setSource(src.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setUnitName(path);
        parser.setEnvironment(classPaths, srcPaths, null, true);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        cu.accept(new ASTVisitor() {

            public boolean visit(TypeDeclaration type) {
                // check if interface or class
                if (!type.isInterface()) {
                    ITypeBinding binding = type.resolveBinding();
                    ITypeBinding[] interfaces = binding.getInterfaces();
                    if (interfaces.length > 0) {
                        for(ITypeBinding iface : interfaces) {
                            classNames.add(iface.getQualifiedName());
                        }
                    }
                    classNames.add(binding.getSuperclass().getQualifiedName());
                }
                return true;
            }

            public boolean visit(MethodInvocation methodInvocation) {
                IMethodBinding binding = methodInvocation.resolveMethodBinding();
                classNames.add(binding.getDeclaringClass().getQualifiedName());
                return true;
            }

            public boolean visit(ClassInstanceCreation instCreation) {
                IMethodBinding binding = instCreation.resolveConstructorBinding();
                classNames.add(binding.getDeclaringClass().getQualifiedName());
                return true;
            }

            public boolean visit(VariableDeclarationStatement varDeclStmt) {
                Type t = varDeclStmt.getType();
                ITypeBinding binding = t.resolveBinding();
                classNames.add(binding.getQualifiedName());
                return true;
            }

            public boolean visit(ReturnStatement returnStmt) {
                ITypeBinding t = returnStmt.getExpression().resolveTypeBinding();
                classNames.add(t.getQualifiedName());
                return true;
            }

//            public boolean visit(ImportDeclaration node) {
//                System.out.println("Imports: " + node.getName());
//                return true;
//            }
        });

        return classNames;
    }
}
