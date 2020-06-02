/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Mai 2020
 */
package test;



import com.jasonpercus.restapijson.API;



/**
 * Cette classe correspond à une classe API contentant des méthodes points d'entrés
 * @author Briguet
 * @version 1.0
 */
public class APITest extends API {
    
    
    
    @Override
    public String _getControllerApiContext() {
        //return "/MyName/";
        return super._getControllerApiContext();
    }
    
    
    
    public void a(){
        System.out.println("a");
    }
    
    public int b(){
        System.out.println("b");
        return 0;
    }
    
    public int[] c(){
        System.out.println("c");
        return new int[]{};
    }
    
    public User d(){
        System.out.println("d");
        return new User(0, "test", "test");
    }
    
    public User[] e(){
        System.out.println("e");
        return new User[]{};
    }
    
    public void f(byte a){
        System.out.println("f");
    }
    
    public void g(byte a, byte b){
        System.out.println("g");
    }
    
    public void h(String a, byte b){
        System.out.println("h");
    }
    
    public void i(String a, String b){
        System.out.println("i");
    }
    
    public void j(byte a, String b){
        System.out.println("j");
    }
    
    public void k(String a, boolean b, double c, float d, long e, int f, short g, byte h){
        System.out.println("k");
    }
    
    public void l(int a){
        System.out.println("l");
    }
    
    public void m(int[] a){
        System.out.println("m");
    }
    
    public void n(boolean[] a){
        System.out.println("n");
    }
    
    public void o(User u){
        System.out.println("o");
    }
    
    public void p(java.util.List<User> u){
        System.out.println("p");
    }
    
    public void q(byte a, byte[] u){
        System.out.println("q");
    }
    
    public void r(byte a, byte u){
        System.out.println("r");
    }
    
    public void s(byte a, byte b, byte[] u){
        System.out.println("s");
    }
    
    public void t(byte a, byte b, byte u){
        System.out.println("t");
    }
    
    public void u(byte a, byte b, User u){
        System.out.println("u");
    }
    
    public void v(byte a, byte b, java.util.List<User> u){
        System.out.println("v");
    }
    
    
    
}