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

//references
//https://ruwix.com/the-rubiks-cube/japanese-western-color-schemes/
//

public class CubeModel {
    public final int F = 0;
    public final int B = 1;
    public final int U = 2;
    public final int D = 3;
    public final int L = 4;
    public final int R = 5;
    
    public Cubelet[][][] cube;
    
    public CubeModel() {
        //create 3x3 cube
        cube = new Cubelet[3][3][3];
        for(int z = 0; z < 3; z++) {
            for(int y = 0; y < 3; y++) {
                for(int x = 0; x < 3; x++) {
                    cube[z][y][x] = new Cubelet();
                }
            }
        }
        
        //paint faces
        for(int face = 0; face < 6; face++) {
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    switch(face) {
                        case F:
                            cube[0][i][j].setFace(face, face + 1);
                            break;
                        case B:
                            cube[2][i][j].setFace(face, face + 1);
                            break;
                        case U:
                            cube[i][0][j].setFace(face, face + 1);
                            break;
                        case D:
                            cube[i][2][j].setFace(face, face + 1);
                            break;
                        case L:
                            cube[i][j][0].setFace(face, face + 1);
                            break;
                        case R:
                            cube[i][j][2].setFace(face, face + 1);
                            break;
                    }
                }
            }
        }
    }
    
    public Cubelet getCubelet(int z, int y, int x) {
        return cube[z][y][x];
    }
    
    public void rotateAxisX(int axis, boolean clockwise) {
        Cubelet temp = cube[0][0][axis];    //Move Corners
        if(clockwise) { //Clockwise Right Side/Switch For Left
            cube[0][0][axis] = cube[0][2][axis];
            cube[0][2][axis] = cube[2][2][axis];
            cube[2][2][axis] = cube[2][0][axis];      
            cube[2][0][axis] = temp;
        }
        else {
            cube[0][0][axis] = cube[2][0][axis];
            cube[2][0][axis] = cube[2][2][axis];
            cube[2][2][axis] = cube[0][2][axis];      
            cube[0][2][axis] = temp;
        }
        
        temp = cube[0][1][axis];            //Move Slice
        if(clockwise) {
            cube[0][1][axis] = cube[1][2][axis];
            cube[1][2][axis] = cube[2][1][axis];
            cube[2][1][axis] = cube[1][0][axis];
            cube[1][0][axis] = temp;
        }
        else {
            cube[0][1][axis] = cube[1][0][axis];
            cube[1][0][axis] = cube[2][1][axis];
            cube[2][1][axis] = cube[1][2][axis];
            cube[1][2][axis] = temp;
        }
        for(int z = 0; z < 3; z++) {    //Rotate cubelets
            for(int y = 0; y < 3; y++) {
                cube[z][y][axis].rotateX(clockwise);
            }
        }
    }
    public void rotateAxisY(int axis, boolean clockwise) {
        Cubelet temp = cube[0][axis][0];    //Move Corners
        if(clockwise) { //Clockwise Up Side/Switch For Left
            cube[0][axis][0] = cube[0][axis][2];
            cube[0][axis][2] = cube[2][axis][2];
            cube[2][axis][2] = cube[2][axis][0];      
            cube[2][axis][0] = temp;
        }
        else {
            cube[0][axis][0] = cube[2][axis][0];
            cube[2][axis][0] = cube[2][axis][2];
            cube[2][axis][2] = cube[0][axis][2];      
            cube[0][axis][2] = temp;
        }
        
        temp = cube[0][axis][1];            //Move Slice
        if(clockwise) {
            cube[0][axis][1] = cube[1][axis][2];
            cube[1][axis][2] = cube[2][axis][1];
            cube[2][axis][1] = cube[1][axis][0];
            cube[1][axis][0] = temp;
        }
        else {
            cube[0][axis][1] = cube[1][axis][0];
            cube[1][axis][0] = cube[2][axis][1];
            cube[2][axis][1] = cube[1][axis][2];
            cube[1][axis][2] = temp;
        }
        for(int z = 0; z < 3; z++) {    //Rotate cubelets
            for(int x = 0; x < 3; x++) {
                cube[z][axis][x].rotateY(clockwise);
            }
        }
    }
    public void rotateAxisZ(int axis, boolean clockwise) {
        Cubelet temp = cube[axis][0][0];    //Move Corners
        if(clockwise) { //Clockwise Right Side/Switch For Left
            cube[axis][0][0] = cube[axis][2][0];
            cube[axis][2][0] = cube[axis][2][2];
            cube[axis][2][2] = cube[axis][0][2];      
            cube[axis][0][2] = temp;
        }
        else {
            cube[axis][0][0] = cube[axis][0][2];
            cube[axis][0][2] = cube[axis][2][2];
            cube[axis][2][2] = cube[axis][2][0];      
            cube[axis][2][0] = temp;
        }
        
        temp = cube[axis][0][1];            //Move Slice
        if(clockwise) {
            cube[axis][0][1] = cube[axis][1][0];
            cube[axis][1][0] = cube[axis][2][1];
            cube[axis][2][1] = cube[axis][1][2];
            cube[axis][1][2] = temp;
        }
        else {
            cube[axis][0][1] = cube[axis][1][2];
            cube[axis][1][2] = cube[axis][2][1];
            cube[axis][2][1] = cube[axis][1][0];
            cube[axis][1][0] = temp;
        }
        for(int y = 0; y < 3; y++) {    //Rotate cubelets
            for(int x = 0; x < 3; x++) {
                cube[axis][y][x].rotateZ(clockwise);
            }
        }
    }
}
