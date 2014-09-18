package view.renderer3D.core;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;


/**
 *
 * @author Vouwfietsman
 */
public class TOOLBOX {
    
    public static void printStackTraceFromHere(){
        StackTraceElement[] sta = Thread.currentThread().getStackTrace();
        for (int i = 2; i < sta.length; i ++){
            System.err.println(sta[i].toString());
        }
    }
    
    public static String getLineLink(){
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        StackTraceElement origin = ste[2];
        return " " + origin.toString();
    }
    
    public static String getLineLinkNested(){
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        StackTraceElement origin = ste[3];
        return " " + origin.toString();
    }
    
    public static void print(String s){
        System.out.println(s);
    }
    
    static boolean debug = false;
    public static boolean checkGLERROR(){
        if (!debug){
            return false;
        }
        int error = glGetError();
        
        if (error != GL_NO_ERROR){
            System.out.println(GLU.gluErrorString(error) + TOOLBOX.getLineLinkNested());
            return true;
        }
        return false;
    }
    
    public static boolean checkGLERROR(boolean b){
        int error = glGetError();
        
        if (error != GL_NO_ERROR){
            System.out.println(GLU.gluErrorString(error) + TOOLBOX.getLineLinkNested());
            return true;
        }
        return false;
    }
    
}
