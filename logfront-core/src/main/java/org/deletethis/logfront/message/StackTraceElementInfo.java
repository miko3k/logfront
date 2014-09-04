/*
 * xxx
 */
package org.deletethis.logfront.message;

import java.util.Objects;

/**
 *
 * @author miko
 */
public class StackTraceElementInfo {
    final private Name declaringClass;
    final private String methodName;
    final private String fileName;
    final private Integer lineNumber;
    final private boolean nativeMethod;

    public StackTraceElementInfo(Name declaringClass, String methodName,
            String fileName, Integer lineNumber, boolean nativeMethod) {
        this.declaringClass = declaringClass;
        this.methodName = methodName;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.nativeMethod = nativeMethod;
    }
  
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(64);
        String unk = "Unknown Source";
        
        res.append(declaringClass);
        res.append(".");
        res.append(methodName);
        res.append("(");
        
        boolean hasSomething = false;
        
        if(nativeMethod) {
            hasSomething = true;
            
            res.append("Native Method");
        } else {
            if (fileName != null || lineNumber != null) {
                hasSomething = true;

                if (fileName == null) {
                    res.append(unk);
                } else {
                    res.append(fileName);
                }

                if (lineNumber != null) {
                    res.append(":");
                    res.append(lineNumber);
                }
            }
        }
        
        if(!hasSomething)
            res.append(unk);
        
        res.append(")");
        return res.toString();
    }

    public Name getDeclaringClass() {
        return declaringClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getFileName() {
        return fileName;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public boolean isNativeMethod() {
        return nativeMethod;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.declaringClass);
        hash = 13 * hash + Objects.hashCode(this.methodName);
        hash = 13 * hash + Objects.hashCode(this.fileName);
        hash = 13 * hash + Objects.hashCode(this.lineNumber);
        hash = 13 * hash + (this.nativeMethod ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final StackTraceElementInfo other = (StackTraceElementInfo) obj;
        if(!Objects.equals(this.declaringClass, other.declaringClass)) {
            return false;
        }
        if(!Objects.equals(this.methodName, other.methodName)) {
            return false;
        }
        if(!Objects.equals(this.fileName, other.fileName)) {
            return false;
        }
        if(!Objects.equals(this.lineNumber, other.lineNumber)) {
            return false;
        }
        return this.nativeMethod == other.nativeMethod;
    }
    
    
}
