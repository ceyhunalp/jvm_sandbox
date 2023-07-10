package checker;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Checker {

    boolean fast;
    String config;
    String[] rootDir;
    List<Object> srcFiles;
    HashSet<String> userDefined;
    HashSet<String> wlistClasses;
    HashMap<String, HashSet<String>> wlistMethods;
    HashMap<String, HashSet<String>> blistMethods;

    public Checker(String config, boolean fast) {
        this.config = config;
        this.fast = fast;
        wlistClasses = new HashSet<>();
        wlistMethods = new HashMap<>();
        blistMethods = new HashMap<>();
        userDefined = new HashSet<>();
        readConfig();
    }

    public void printData() {
        for (String s : wlistClasses) {
            System.out.println("WL class: " + s);
        }
        System.out.println("<<<<<<<< WHITELIST >>>>>>>>>");
        for (String s : wlistMethods.keySet()) {
            HashSet<String> hs = wlistMethods.get(s);
            System.out.println("==== " + s + " ====");
            for (String m : hs) {
                System.out.println(m);
            }
        }
        System.out.println("<<<<<<<< BLACKLIST >>>>>>>>>");
        for (String s : blistMethods.keySet()) {
            HashSet<String> hs = blistMethods.get(s);
            System.out.println("==== " + s + " ====");
            for (String m : hs) {
                System.out.println(m);
            }
        }
    }

    public void execute() {
        HashSet<String> leftovers = new HashSet<>();
        for (Object o : srcFiles) {
            if (o instanceof String) {
                String file = (String) o;
                System.out.println("Checking source file: " + file);
                CompilationUnit cu = createCompilationUnit(file, rootDir);
                ASTData astData = extractASTData(cu);
                printASTData(astData);
                // Check if the declaring classes of the methods are whitelisted
                for (String cname : astData.methods.keySet()) {
//                    System.out.println("Checking class: " + cname);
                    HashSet<String> methods = astData.methods.get(cname);
                    if (wlistClasses.contains(cname)) {
                        // If the declaring class is whitelisted we still need to make sure that we don't use a
                        // blacklisted method from this class
                        if (blistMethods.containsKey(cname)) {
                            HashSet<String> blist = blistMethods.get(cname);
                            for (String mname : methods) {
                                if (blist.contains(mname)) {
                                    System.out.println("Class: " + cname + " is whitelisted but " + mname + " is not");
                                }
                            }
                        } else {
                            System.out.println("Class: " + cname + " is safe!");
                        }
                    } else {
                        // The declaring class is not whitelisted but its methods might be.
                        if (wlistMethods.containsKey(cname)) {
                            HashSet<String> wlist = wlistMethods.get(cname);
                            for (String mname : methods) {
                                if (!wlist.contains(mname)) {
                                    System.out.println("Class: " + cname + " - Method: " + mname + " is not whitelisted");
                                }
                            }
                        } else {
                            System.out.println("Class: " + cname + " does not have any whitelisted methods");
                        }
                    }
                }
                // Check if the classes with no method calls are all whitelisted as well
                for (String cname : astData.classes) {
                    // If the class has a method in the source code, we have already checked it above
                    if (!astData.methods.containsKey(cname)) {
                        if (!wlistClasses.contains(cname)) {
                            leftovers.add(cname);
                        } else {
                            System.out.println("Class: " + cname + " is whitelisted");
                        }
                    }
                }
            } else {
                System.err.println("Wrong type in source files. Expected string!");
                System.exit(1);
            }
        }
        for (String c : leftovers) {
            if (!userDefined.contains(c)) {
                System.out.println("Class: " + c + " is not a user-defined class. It is not whitelisted and there" +
                        " are not whitelisted methods of this class in the code!");
            }
        }
    }

    private void printASTData(ASTData data) {
        System.out.println("====== PRINTING ASTDATA ======");
        for (String c : data.classes) {
            System.out.println("CLASS: " + c);
        }
        System.out.println("============");
        for (String c : data.methods.keySet()) {
            System.out.println(c + " --> " + data.methods.get(c).toString());
        }
        System.out.println("============");
    }

    public CompilationUnit createCompilationUnit(String srcFile, String[] srcPaths) {
        String src = readFileToString(srcFile);
        ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
        parser.setSource(src.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setUnitName(srcFile);
        parser.setEnvironment(null, srcPaths, null, true);
        Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.latestSupportedJavaVersion(), options);
        parser.setCompilerOptions(options);
        return (CompilationUnit) parser.createAST(null);
    }

    private void addUserClasses(String cname) {
        ArrayList<Integer> idxs = new ArrayList<>();
        for (int i = 0; i < cname.length(); i++) {
            if (cname.charAt(i) == '.') {
                idxs.add(i);
            }
        }
        for (int idx : idxs) {
            userDefined.add(cname.substring(0, idx));
        }
        userDefined.add(cname);
    }

    public ASTData extractASTData(CompilationUnit cu) {

        ASTData astData = new ASTData();

        cu.accept(new ASTVisitor() {

            public void getTypeArguments(ITypeBinding arg) {
                if (arg.isArray()) {
                    ITypeBinding eltType = arg.getElementType();
                    getTypeArguments(eltType);
                } else if (arg.isParameterizedType()) {
                    ITypeBinding[] typeArgs = arg.getTypeArguments();
                    for (ITypeBinding t : typeArgs) {
                        getTypeArguments(t);
                    }
                } else if (arg.isClass() || arg.isInterface() || arg.isEnum()) {
                    astData.classes.add(arg.getQualifiedName());
                }
                return;
            }

            public boolean helperTypeBinding(ITypeBinding tBinding) {
                if (!tBinding.isFromSource()) {
                    if(tBinding.isParameterizedType()) {
                        astData.classes.add(tBinding.getTypeDeclaration().getQualifiedName());
                        ITypeBinding[] args = tBinding.getTypeArguments();
                        for (ITypeBinding arg : args) {
                            getTypeArguments(arg);
                        }
                    } else if (tBinding.isArray()) {
                        ITypeBinding eltType = tBinding.getElementType();
                        getTypeArguments(eltType);
                    }
                }
                return true;
            }

            public boolean helperMethodBinding(IMethodBinding mBinding) {
                if (mBinding == null) {
                    return false;
                }
                ITypeBinding cBinding = mBinding.getDeclaringClass();
                if (cBinding == null) {
                    return false;
                }
                String cname;
                if (!cBinding.isFromSource()) {
                    if (cBinding.isParameterizedType()) {
                        cname = cBinding.getTypeDeclaration().getQualifiedName();
                        astData.classes.add(cname);
                        ITypeBinding[] args = cBinding.getTypeArguments();
                        for (ITypeBinding arg : args) {
                            helperTypeBinding(arg);
//                            if (!arg.isPrimitive()) {
//                                astData.classes.add(arg.getQualifiedName());
//                            }
                        }
                    } else {
                        cname = cBinding.getQualifiedName();
                        astData.classes.add(cname);
                    }
                    String methodDeclaration = mBinding.getMethodDeclaration().getKey();
                    String mname = methodDeclaration.split("\\.")[1];
                    if (astData.methods.containsKey(cname)) {
                        astData.methods.get(cname).add(mname);
                    } else {
                        astData.methods.put(cname, new HashSet<>(Arrays.asList(mname)));
                    }
                }
                return true;
            }

            public boolean visit(TypeDeclaration type) {
                ITypeBinding binding = type.resolveBinding();
                ITypeBinding superClass = binding.getSuperclass();
                ITypeBinding[] interfaces = binding.getInterfaces();
                if (superClass != null) {
                    if (!superClass.isFromSource()) {
                        if (superClass.isParameterizedType()) {
                            astData.classes.add(superClass.getTypeDeclaration().getQualifiedName());
                        } else {
                            astData.classes.add(superClass.getQualifiedName());
                        }
                    }
                }
                for (ITypeBinding iface : interfaces) {
                    if (!iface.isFromSource()) {
                        if (iface.isParameterizedType()) {
                            astData.classes.add(iface.getTypeDeclaration().getQualifiedName());
                        } else {
                            astData.classes.add(iface.getQualifiedName());
                        }
                    }
                }
                addUserClasses(binding.getQualifiedName());
                return true;
            }

            public boolean visit(ConstructorInvocation cInvocation) {
                boolean success = helperMethodBinding(cInvocation.resolveConstructorBinding());
                if (!success) {
                    System.out.println("Error in resolving constructor invocation binding: " + cInvocation);
                }
                return true;
            }

            public boolean visit(SuperConstructorInvocation cInvocation) {
                boolean success = helperMethodBinding(cInvocation.resolveConstructorBinding());
                if (!success) {
                    System.out.println("Error in resolving super constructor invocation binding: " + cInvocation);
                }
                return true;
            }

            public boolean visit(SuperMethodInvocation mInvocation) {
                boolean success = helperMethodBinding(mInvocation.resolveMethodBinding());
                if (!success) {
                    System.out.println("Error in resolving super method invocation binding: " + mInvocation);
                }
                return true;
            }

            public boolean visit(SuperFieldAccess access) {
                ITypeBinding tBinding = access.resolveFieldBinding().getDeclaringClass();
                if (tBinding == null) {
                    System.out.println("Error in resolving the type binding for super field access: " + access);
                    return true;
                }
                helperTypeBinding(tBinding);
                return true;
            }

            public boolean visit(MethodInvocation mInvocation) {
                boolean success = helperMethodBinding(mInvocation.resolveMethodBinding());
                if (!success) {
                    System.out.println("Error in resolving method invocation binding: " + mInvocation);
                }
                return true;
            }

            public boolean visit(ClassInstanceCreation instCreation) {
                boolean success = helperMethodBinding(instCreation.resolveConstructorBinding());
                if (!success) {
                    System.out.println("Error in resolving the type binding for class instance creation: " + instCreation);
                }
                return true;
            }

            public boolean visit(VariableDeclarationStatement varDeclStmt) {
                ITypeBinding tBinding = varDeclStmt.getType().resolveBinding();
                if (tBinding == null) {
                    System.out.println("Error in resolving the type binding for variable declaration stmt: " + varDeclStmt);
                    return true;
                }
                helperTypeBinding(tBinding);
                return true;
            }

            public boolean visit(ReturnStatement returnStmt) {
                ITypeBinding tBinding = returnStmt.getExpression().resolveTypeBinding();
                if (tBinding == null) {
                    System.out.println("Error in resolving the type binding for return statement: " + returnStmt);
                    return true;
                }
                helperTypeBinding(tBinding);
                return true;
            }

            public boolean visit(CastExpression castExpr) {
                ITypeBinding tBinding = castExpr.resolveTypeBinding();
                if (tBinding == null) {
                    System.out.println("Error in resolving the type binding for return statement: " + castExpr);
                    return true;
                }
                helperTypeBinding(tBinding);
                return true;
            }

            public boolean visit(SuperMethodReference mReference) {
                boolean success = helperMethodBinding(mReference.resolveMethodBinding());
                if (!success) {
                    System.out.println("Error in resolving super method reference binding: " + mReference);
                }
                return true;
            }

            public boolean visit(ExpressionMethodReference mReference) {
                boolean success = helperMethodBinding(mReference.resolveMethodBinding());
                if (!success) {
                    System.out.println("Error in resolving method reference binding: " + mReference);
                }
                return true;
            }

            public boolean visit(ImportDeclaration decl) {
                astData.classes.add(decl.getName().getFullyQualifiedName());
                return true;
            }
        });
        return astData;
    }

    private void readConfig() {
        TomlTable table;
        Path source = Paths.get(config);
        try {
            TomlParseResult result = Toml.parse(source);
            table = result.getTableOrEmpty("sources");
            if (!table.isEmpty()) {
                rootDir = new String[]{table.getString("root_dir")};
                srcFiles = table.getArray("src_files").toList();
            } else {
                System.err.println("Cannot find [sources] in config.toml");
            }
            table = result.getTableOrEmpty("whitelist");
            if (!table.isEmpty()) {
                readClassFile(table.getString("class_file"), wlistClasses);
                readMethodFile(table.getString("method_file"), wlistMethods);
            } else {
                System.err.println("Cannot find [whitelist] in config.toml");
            }
            table = result.getTableOrEmpty("blacklist");
            if (!table.isEmpty()) {
                readMethodFile(table.getString("method_file"), blistMethods);
            } else {
                System.err.println("Cannot find [blacklist] in config.toml");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readClassFile(String path, HashSet<String> hs) {
        String line;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            line = reader.readLine();
            while (line != null) {
                hs.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMethodFile(String path, HashMap<String, HashSet<String>> hm) {
        String line;
        String[] tokens;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            line = reader.readLine();
            while (line != null) {
                // TODO: Remove this check. This is here temporarily to be able to comment out lines in the .txt files
                if (line.length() > 0) {
                    if (!(line.charAt(0) == '#')) {
                        tokens = line.split(" ");
                        if (hm.containsKey(tokens[0])) {
                            hm.get(tokens[0]).add(tokens[1]);
                        } else {
                            HashSet<String> hs = new HashSet<>();
                            hs.add(tokens[1]);
                            hm.put(tokens[0], hs);
                        }
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFileToString(String fpath) {
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
}

