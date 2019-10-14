/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectlearn;

/**
 *
 * @author Viet
 */
public class Cubelet {
    public final int F = 0;
    public final int B = 1;
    public final int U = 2;
    public final int D = 3;
    public final int L = 4;
    public final int R = 5;
    
    public int[] faces;
    
    public Cubelet() {
        faces = new int[6]; //  F   B   U   D   L   R
    }
    
    public void setFace(int face, int color) {
        faces[face] = color;
    }
    
    @Override
    public String toString(){
        return faces[0] + " " + faces[1] + " " + faces[2] + " " + faces[3] + " " + faces[4] + " " + faces[5];
    }
    
    public int getF() {return faces[0];}
    public int getB() {return faces[1];}
    public int getU() {return faces[2];}
    public int getD() {return faces[3];}
    public int getL() {return faces[4];}
    public int getR() {return faces[5];}
    
    public void rotate(int a, int b, int c, int d, boolean clockwise) {
        int temp = faces[a];
        if(clockwise) {
            faces[a] = faces[b];
            faces[b] = faces[c];
            faces[c] = faces[d];
            faces[d] = temp;
        }
        else {
            faces[a] = faces[d];
            faces[d] = faces[c];
            faces[c] = faces[b];
            faces[b] = temp;
        }
    }
    
    public void rotateX(boolean clockwise) {
        rotate(F, D, B, U, clockwise);  //Clockwise Right Side/Switch For Left
    }
    
    public void rotateY(boolean clockwise) {
        rotate(F, R, B, L, clockwise);  //Clockwise Up Side/Switch For Down
    }
    
    public void rotateZ(boolean clockwise) {
        rotate(U, L, D, R, clockwise);  //Clockwise Front Side/Switch For Back
    }
}
